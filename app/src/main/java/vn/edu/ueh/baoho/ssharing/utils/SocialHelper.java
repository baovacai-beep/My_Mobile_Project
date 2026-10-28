package vn.edu.ueh.baoho.ssharing.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class SocialHelper {

    /**
     * Lấy danh sách mặc định các tài khoản mà username đang theo dõi
     */
    public static String getDefaultFollowingList(String username) {
        String u = username != null ? username.toLowerCase().trim() : "";
        if ("baoho123".equals(u)) {
            return "beo123;;minh.triet;;khanh.trinh";
        } else if ("beo123".equals(u)) {
            return "baoho123;;minh.triet";
        } else if ("minh.triet".equals(u)) {
            return "baoho123;;beo123";
        } else if ("khanh.trinh".equals(u)) {
            return "baoho123";
        } else if ("bao.ngoc".equals(u)) {
            return "baoho123";
        } else if ("gia.bao".equals(u)) {
            return "baoho123";
        } else if ("hoang.long".equals(u)) {
            return "baoho123";
        }
        return "";
    }

    /**
     * Lấy chuỗi danh sách đang theo dõi của userA từ SharedPreferences
     */
    public static String getFollowingString(Context context, String username) {
        if (username == null || username.trim().isEmpty()) return "";
        String cleanUser = username.toLowerCase().trim();
        SharedPreferences prefs = context.getSharedPreferences("SSHARING_PREFS", Context.MODE_PRIVATE);
        String defaultList = getDefaultFollowingList(cleanUser);
        return prefs.getString("FOLLOWING_LIST_" + cleanUser, defaultList);
    }

    /**
     * Kiểm tra xem userA có đang theo dõi targetUser không
     */
    public static boolean isUserFollowing(Context context, String userA, String targetUser) {
        if (userA == null || targetUser == null) return false;
        String cleanA = userA.toLowerCase().trim();
        String cleanTarget = targetUser.toLowerCase().trim();
        if (cleanA.isEmpty() || cleanTarget.isEmpty()) return false;

        String followingStr = getFollowingString(context, cleanA);
        if (followingStr.isEmpty()) return false;

        String[] parts = followingStr.split(";;");
        for (String p : parts) {
            if (p.trim().equalsIgnoreCase(cleanTarget)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem userA và targetUser có là Bạn Bè 2 Chiều (Mutual Friends - Cả 2 cùng follow nhau) không
     */
    public static boolean isMutualFriend(Context context, String userA, String targetUser) {
        return isUserFollowing(context, userA, targetUser) && isUserFollowing(context, targetUser, userA);
    }

    /**
     * Thêm hoặc bỏ theo dõi targetUser từ userA
     */
    public static boolean toggleFollowUser(Context context, String userA, String targetUser) {
        if (userA == null || targetUser == null) return false;
        String cleanA = userA.toLowerCase().trim();
        String cleanTarget = targetUser.toLowerCase().trim();
        if (cleanA.isEmpty() || cleanTarget.isEmpty()) return false;

        SharedPreferences prefs = context.getSharedPreferences("SSHARING_PREFS", Context.MODE_PRIVATE);
        String currentList = getFollowingString(context, cleanA);

        List<String> list = new ArrayList<>();
        if (!currentList.isEmpty()) {
            for (String item : currentList.split(";;")) {
                String trimmed = item.trim().toLowerCase();
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
        return nowFollowing;
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
            if (isUserFollowing(context, targetUser, cleanA)) {
                mutualCount++;
            }
        }

        return new int[]{followingCount, mutualCount};
    }

    /**
     * Tải và hiển thị ảnh đại diện từ Bộ nhớ Local hoặc Firebase Firestore Cloud
     */
    public static void loadUserAvatar(Context context, android.widget.ImageView targetImageView, String username) {
        if (targetImageView == null || username == null || username.trim().isEmpty()) return;
        String usernameClean = username.toLowerCase().trim();
        SharedPreferences prefs = context.getSharedPreferences("SSHARING_PREFS", Context.MODE_PRIVATE);

        String localPath = prefs.getString("CURRENT_USER_AVATAR_PATH_" + usernameClean, "");
        if (!localPath.isEmpty() && new java.io.File(localPath).exists()) {
            try {
                android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(localPath);
                if (bitmap != null) {
                    targetImageView.setImageBitmap(bitmap);
                    targetImageView.setVisibility(android.view.View.VISIBLE);
                    return;
                }
            } catch (Throwable ignored) {}
        }

        String b64Local = prefs.getString("CURRENT_USER_AVATAR_B64_" + usernameClean, "");
        if (!b64Local.isEmpty()) {
            try {
                byte[] bytes = android.util.Base64.decode(b64Local, android.util.Base64.DEFAULT);
                android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (bitmap != null) {
                    targetImageView.setImageBitmap(bitmap);
                    targetImageView.setVisibility(android.view.View.VISIBLE);
                    return;
                }
            } catch (Throwable ignored) {}
        }

        // Tải trực tiếp từ Firebase Firestore Cloud
        try {
            com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("users").document(usernameClean).get()
                    .addOnSuccessListener(doc -> {
                        if (doc != null && doc.exists()) {
                            String b64Cloud = doc.getString("avatarBase64");
                            if (b64Cloud != null && !b64Cloud.isEmpty()) {
                                prefs.edit().putString("CURRENT_USER_AVATAR_B64_" + usernameClean, b64Cloud).apply();
                                try {
                                    byte[] bytes = android.util.Base64.decode(b64Cloud, android.util.Base64.DEFAULT);
                                    android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    if (bitmap != null && targetImageView != null) {
                                        targetImageView.setImageBitmap(bitmap);
                                        targetImageView.setVisibility(android.view.View.VISIBLE);
                                    }
                                } catch (Throwable ignored) {}
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
}
