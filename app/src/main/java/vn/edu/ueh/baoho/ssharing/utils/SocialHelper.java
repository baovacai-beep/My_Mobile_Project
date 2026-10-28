package vn.edu.ueh.baoho.ssharing.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class SocialHelper {

    /**
     * Chuẩn hóa Username (bỏ @, khoảng trắng, chữ hoa)
     */
    public static String cleanUsername(String raw) {
        if (raw == null) return "";
        String clean = raw.trim();
        if (clean.contains("(@") && clean.contains(")")) {
            int start = clean.indexOf("(@") + 2;
            int end = clean.indexOf(")", start);
            if (end > start) {
                clean = clean.substring(start, end);
            }
        }
        return clean.replaceAll("[^a-zA-Z0-9._-]", "").toLowerCase().trim();
    }

    /**
     * Lấy danh sách mặc định các tài khoản mà username đang theo dõi (Sạch sẽ, không gán sẵn)
     */
    public static String getDefaultFollowingList(String username) {
        return "";
    }

    /**
     * Lấy chuỗi danh sách đang theo dõi của userA từ SharedPreferences
     */
    public static String getFollowingString(Context context, String username) {
        if (username == null || username.trim().isEmpty()) return "";
        String cleanUser = cleanUsername(username);
        SharedPreferences prefs = context.getSharedPreferences("SSHARING_PREFS", Context.MODE_PRIVATE);
        String defaultList = getDefaultFollowingList(cleanUser);
        return prefs.getString("FOLLOWING_LIST_" + cleanUser, defaultList);
    }

    /**
     * Kiểm tra xem userA có đang theo dõi targetUser không
     */
    public static boolean isUserFollowing(Context context, String userA, String targetUser) {
        if (userA == null || targetUser == null) return false;
        String cleanA = cleanUsername(userA);
        String cleanTarget = cleanUsername(targetUser);
        if (cleanA.isEmpty() || cleanTarget.isEmpty()) return false;

        String followingStr = getFollowingString(context, cleanA);
        if (followingStr.isEmpty()) return false;

        String[] parts = followingStr.split(";;");
        for (String p : parts) {
            if (cleanUsername(p).equalsIgnoreCase(cleanTarget)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem userA có được targetUser theo dõi không (targetUser có nằm trong danh sách follower của userA không?)
     */
    public static boolean isUserFollowedBy(Context context, String userA, String targetUser) {
        if (userA == null || targetUser == null) return false;
        String cleanA = cleanUsername(userA);
        String cleanTarget = cleanUsername(targetUser);
        if (cleanA.isEmpty() || cleanTarget.isEmpty()) return false;

        SharedPreferences prefs = context.getSharedPreferences("SSHARING_PREFS", Context.MODE_PRIVATE);
        String followerStr = prefs.getString("FOLLOWER_LIST_" + cleanA, "NONE");
        if ("NONE".equalsIgnoreCase(followerStr) || followerStr.trim().isEmpty()) {
            return false;
        }

        String[] parts = followerStr.split(";;");
        for (String p : parts) {
            if (cleanUsername(p).equalsIgnoreCase(cleanTarget)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem userA và targetUser có là Bạn Bè 2 Chiều (Mutual Friends - Cả 2 cùng follow nhau) không
     */
    public static boolean isMutualFriend(Context context, String userA, String targetUser) {
        return isUserFollowing(context, userA, targetUser) && isUserFollowedBy(context, userA, targetUser);
    }

    /**
     * Thêm hoặc bỏ theo dõi targetUser từ userA
     */
    public static boolean toggleFollowUser(Context context, String userA, String targetUser) {
        if (userA == null || targetUser == null) return false;
        String cleanA = cleanUsername(userA);
        String cleanTarget = cleanUsername(targetUser);
        if (cleanA.isEmpty() || cleanTarget.isEmpty()) return false;

        SharedPreferences prefs = context.getSharedPreferences("SSHARING_PREFS", Context.MODE_PRIVATE);
        String currentList = getFollowingString(context, cleanA);

        List<String> list = new ArrayList<>();
        if (!currentList.isEmpty()) {
            for (String item : currentList.split(";;")) {
                String trimmed = cleanUsername(item);
                if (!trimmed.isEmpty() && !list.contains(trimmed)) {
                    list.add(trimmed);
                }
            }
        }

        boolean nowFollowing;
        if (list.contains(cleanTarget)) {
            list.remove(cleanTarget);
            nowFollowing = false;
        } else {
            list.add(cleanTarget);
            nowFollowing = true;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(";;");
            sb.append(list.get(i));
        }

        prefs.edit().putString("FOLLOWING_LIST_" + cleanA, sb.toString()).apply();

        // Đồng bộ liên kết Theo Dõi lên Firebase Cloud Firestore
        try {
            com.google.firebase.firestore.FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
            String docId = cleanA + "_" + cleanTarget;
            if (nowFollowing) {
                java.util.Map<String, Object> data = new java.util.HashMap<>();
                data.put("follower", cleanA);
                data.put("following", cleanTarget);
                data.put("timestamp", System.currentTimeMillis());
                db.collection("follows").document(docId).set(data);
            } else {
                db.collection("follows").document(docId).delete();
            }
        } catch (Throwable ignored) {}

        return nowFollowing;
    }

    /**
     * Tự động truy vấn và đồng bộ liên kết Theo dõi 2 chiều từ Cloud Firestore thời gian thực
     */
    public static void syncFollowStateFromCloud(Context context, String username, Runnable onComplete) {
        if (context == null || username == null || username.trim().isEmpty()) {
            if (onComplete != null) onComplete.run();
            return;
        }
        String cleanUser = cleanUsername(username);

        try {
            com.google.firebase.firestore.FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
            
            // Query 1: Lấy CHÍNH XÁC danh sách những người mà cleanUser ĐANG theo dõi từ Cloud
            db.collection("follows").whereEqualTo("follower", cleanUser).get()
                    .addOnSuccessListener(snapshot -> {
                        SharedPreferences prefs = context.getSharedPreferences("SSHARING_PREFS", Context.MODE_PRIVATE);
                        List<String> myFollowingList = new ArrayList<>();
                        if (snapshot != null) {
                            for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
                                String target = cleanUsername(doc.getString("following"));
                                if (!target.isEmpty() && !myFollowingList.contains(target)) {
                                    myFollowingList.add(target);
                                }
                            }
                        }

                        // GHI ĐÈ CHÍNH XÁC DANH SÁCH THEO DÕI CỦA CLEANUSER (Dọn sạch dữ liệu cũ)
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < myFollowingList.size(); i++) {
                            if (i > 0) sb.append(";;");
                            sb.append(myFollowingList.get(i));
                        }
                        prefs.edit().putString("FOLLOWING_LIST_" + cleanUser, sb.toString()).apply();

                        // Query 2: Lấy CHÍNH XÁC danh sách những người ĐANG theo dõi cleanUser từ Cloud
                        db.collection("follows").whereEqualTo("following", cleanUser).get()
                                .addOnSuccessListener(snapshot2 -> {
                                    if (snapshot2 != null) {
                                        List<String> myFollowersList = new ArrayList<>();
                                        for (com.google.firebase.firestore.DocumentSnapshot doc2 : snapshot2.getDocuments()) {
                                            String follower = cleanUsername(doc2.getString("follower"));
                                            if (!follower.isEmpty() && !myFollowersList.contains(follower)) {
                                                myFollowersList.add(follower);
                                            }
                                        }

                                        StringBuilder sbFollowers = new StringBuilder();
                                        for (int i = 0; i < myFollowersList.size(); i++) {
                                            if (i > 0) sbFollowers.append(";;");
                                            sbFollowers.append(myFollowersList.get(i));
                                        }
                                        String valFollowers = sbFollowers.length() > 0 ? sbFollowers.toString() : "NONE";
                                        prefs.edit().putString("FOLLOWER_LIST_" + cleanUser, valFollowers).apply();
                                    }
                                    if (onComplete != null) onComplete.run();
                                })
                                .addOnFailureListener(e -> {
                                    if (onComplete != null) onComplete.run();
                                });
                    })
                    .addOnFailureListener(e -> {
                        if (onComplete != null) onComplete.run();
                    });
        } catch (Throwable ignored) {
            if (onComplete != null) onComplete.run();
        }
    }

    /**
     * Tính toán tổng số lượng Đang theo dõi và Số bạn bè 2 chiều cho userA
     * Trả về mảng: int[0] = sốTheoDõi, int[1] = sốBạnBè2Chiều
     */
    public static int[] calculateFollowingAndMutualCounts(Context context, String userA) {
        if (userA == null) return new int[]{0, 0};
        String cleanA = userA.toLowerCase().trim();

        String followingStr = getFollowingString(context, cleanA);
        if (followingStr.isEmpty()) return new int[]{0, 0};

        String[] parts = followingStr.split(";;");
        List<String> followedList = new ArrayList<>();
        for (String p : parts) {
            String trimmed = p.trim().toLowerCase();
            if (!trimmed.isEmpty() && !followedList.contains(trimmed) && !trimmed.equals(cleanA)) {
                followedList.add(trimmed);
            }
        }

        int followingCount = followedList.size();
        int mutualCount = 0;

        for (String targetUser : followedList) {
            if (isMutualFriend(context, cleanA, targetUser)) {
                mutualCount++;
            }
        }

        return new int[]{followingCount, mutualCount};
    }

    /**
     * Tải và hiển thị ảnh đại diện từ Bộ nhớ Local hoặc Firebase Firestore Cloud (Tải đệm tức thì & Cập nhật Cloud)
     */
    public static void loadUserAvatar(Context context, android.widget.ImageView targetImageView, String username) {
        if (targetImageView == null || username == null || username.trim().isEmpty()) return;
        String usernameClean = username.toLowerCase().trim();
        SharedPreferences prefs = context.getSharedPreferences("SSHARING_PREFS", Context.MODE_PRIVATE);

        // 1. Nạp ảnh tức thì từ Local File hoặc Base64 Cache để tránh giật lag UI
        boolean daHienAnhLocal = false;
        String localPath = prefs.getString("CURRENT_USER_AVATAR_PATH_" + usernameClean, "");
        if (!localPath.isEmpty() && new java.io.File(localPath).exists()) {
            try {
                android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(localPath);
                if (bitmap != null) {
                    targetImageView.setImageBitmap(bitmap);
                    targetImageView.setVisibility(android.view.View.VISIBLE);
                    daHienAnhLocal = true;
                }
            } catch (Throwable ignored) {}
        }

        if (!daHienAnhLocal) {
            String b64Local = prefs.getString("CURRENT_USER_AVATAR_B64_" + usernameClean, "");
            if (!b64Local.isEmpty()) {
                try {
                    byte[] bytes = android.util.Base64.decode(b64Local, android.util.Base64.DEFAULT);
                    android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    if (bitmap != null) {
                        targetImageView.setImageBitmap(bitmap);
                        targetImageView.setVisibility(android.view.View.VISIBLE);
                    }
                } catch (Throwable ignored) {}
            }
        }

        // 2. Luôn đồng bộ kiểm tra ảnh đại diện mới nhất từ Đám Mây Firebase Firestore Cloud
        try {
            com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("users").document(usernameClean).get()
                    .addOnSuccessListener(doc -> {
                        if (doc != null && doc.exists()) {
                            String b64Cloud = doc.getString("avatarBase64");
                            if (b64Cloud != null && !b64Cloud.isEmpty()) {
                                String oldB64 = prefs.getString("CURRENT_USER_AVATAR_B64_" + usernameClean, "");
                                if (!b64Cloud.equals(oldB64)) {
                                    prefs.edit().putString("CURRENT_USER_AVATAR_B64_" + usernameClean, b64Cloud).apply();
                                    try {
                                        byte[] bytes = android.util.Base64.decode(b64Cloud, android.util.Base64.DEFAULT);
                                        android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        if (bitmap != null && targetImageView != null) {
                                            targetImageView.setImageBitmap(bitmap);
                                            targetImageView.setVisibility(android.view.View.VISIBLE);
                                            android.util.Log.d("SSharing_Profile", "☁️ [REALTIME AVATAR SYNC] Successfully updated avatar from Firebase Cloud for @" + usernameClean);
                                        }
                                    } catch (Throwable ignored) {}
                                }
                            }
                        }
                    });
        } catch (Throwable ignored) {}
    }

    /**
     * Nén và đẩy ảnh đại diện Base64 lên Firebase Firestore Cloud Console
     */
    public static void syncAvatarToCloud(Context context, String username, java.io.File avatarFile) {
        if (username == null || avatarFile == null || !avatarFile.exists()) return;
        String usernameClean = username.toLowerCase().trim();

        try {
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(avatarFile.getAbsolutePath());
            if (bitmap == null) return;

            int maxDim = 200;
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float scale = Math.min((float) maxDim / width, (float) maxDim / height);
            if (scale < 1.0f) {
                bitmap = android.graphics.Bitmap.createScaledBitmap(bitmap, Math.round(width * scale), Math.round(height * scale), true);
            }

            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] bytes = baos.toByteArray();
            String b64 = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);

            SharedPreferences prefs = context.getSharedPreferences("SSHARING_PREFS", Context.MODE_PRIVATE);
            prefs.edit().putString("CURRENT_USER_AVATAR_B64_" + usernameClean, b64).apply();

            com.google.firebase.firestore.FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
            java.util.Map<String, Object> update = new java.util.HashMap<>();
            update.put("avatarBase64", b64);
            update.put("updatedAt", System.currentTimeMillis());

            db.collection("users").document(usernameClean).set(update, com.google.firebase.firestore.SetOptions.merge());
        } catch (Throwable ignored) {}
    }

    /**
     * Tạo mã ID phòng chat 1-on-1 cố định & đối xứng giữa 2 tài khoản (sắp xếp theo bảng chữ cái)
     */
    public static String generateSocialChatRoomCode(String userA, String userB) {
        String cleanA = userA != null ? userA.toLowerCase().trim() : "";
        String cleanB = userB != null ? userB.toLowerCase().trim() : "";

        if (cleanA.compareTo(cleanB) <= 0) {
            return "P2P_" + cleanA + "_" + cleanB;
        } else {
            return "P2P_" + cleanB + "_" + cleanA;
        }
    }
}
