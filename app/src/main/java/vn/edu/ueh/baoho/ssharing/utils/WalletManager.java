package vn.edu.ueh.baoho.ssharing.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class WalletManager {

    private static final String TAG = "WalletManager";
    private static final String PREF_NAME = "SSHARING_PREFS";

    public static String getCurrentUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String raw = prefs.getString("CURRENT_USERNAME", "baoho123");
        return SocialHelper.cleanUsername(raw);
    }

    public static int getDefaultBalanceForUser(String username) {
        String clean = SocialHelper.cleanUsername(username);
        if ("baoho123".equalsIgnoreCase(clean)) return 10000;
        if ("hoang.long".equalsIgnoreCase(clean)) return 8744;
        if ("beo123".equalsIgnoreCase(clean)) return 2500;
        if ("minh.triet".equalsIgnoreCase(clean)) return 1250;
        if ("bao.ngoc".equalsIgnoreCase(clean)) return 1800;
        if ("khanh.trinh".equalsIgnoreCase(clean)) return 2000;
        if ("gia.bao".equalsIgnoreCase(clean)) return 1500;
        return 1000;
    }

    public static String getDefaultTransactionsForUser(String username) {
        String clean = SocialHelper.cleanUsername(username);
        if ("hoang.long".equalsIgnoreCase(clean)) {
            return "TX-HL8812||Nạp xu VietQR Napas247 MBBank||+8,744 SS||28/10/2026 10:00:00||Nạp tiền VietQR||Thành công||Nạp tiền thành công qua VietQR MBBank 0913672861 HO GIA BAO (Mã GD: TX-HL8812)" +
                    ";;TX-HL8811||Mua Voucher Starbucks Trà Sen Vàng||-200 SS||27/10/2026 15:20:00||Thanh toán Escrow||Thành công||Đổi voucher Starbucks giảm 20K";
        }
        if ("baoho123".equalsIgnoreCase(clean)) {
            return "TX-N892031||Nạp xu VietQR Napas247 MBBank||+10,000 SS||19/10/2026 14:30:00||Nạp tiền VietQR||Thành công||Nạp tiền VietQR MBBank 0908123456 nhận 10.000 SS xu" +
                    ";;TX-E761920||Mua Voucher Highlands Coffee||-35 SS||19/10/2026 12:15:00||Thanh toán Escrow||Thành công||Thanh toán mua voucher Trà Sen Vàng";
        }
        if ("beo123".equalsIgnoreCase(clean)) {
            return "TX-BEO991||Nạp xu VietQR Napas247 MBBank||+2,500 SS||20/10/2026 09:00:00||Nạp tiền VietQR||Thành công||Nạp 2,500 SS xu vào ví";
        }
        return "TX-INIT" + Math.abs(clean.hashCode() % 100000) + "||Khởi tạo ví riêng cho @" + clean + "||+" + getDefaultBalanceForUser(clean) + " SS||19/10/2026 08:00:00||Khởi tạo ví||Thành công||Tài khoản @" + clean + " sở hữu số dư và lịch sử riêng";
    }

    public static String getDefaultVouchersForUser(String username) {
        String clean = SocialHelper.cleanUsername(username);
        if ("hoang.long".equalsIgnoreCase(clean)) {
            return "HLM1T1-HOANGLONG::Voucher Highlands Mua 1 Tặng 1::HSD: 31 T12 2026::FOOD::BARCODE;;" +
                    "SBUX20K-HOANGLONG::Voucher Starbucks Giảm 20K::HSD: 31 T12 2026::BRAND::BARCODE";
        }
        if ("baoho123".equalsIgnoreCase(clean)) {
            return "HLM1T1-USER33::Voucher Highlands Mua 1 Tặng 1::HSD: 31 T12 2026::FOOD::BARCODE;;" +
                    "SPFOOD50K-USER22::Voucher ShopeeFood Giảm 50K::HSD: 31 T12 2026::SHOPEE::QR_IMAGE;;" +
                    "SBUX20K-OCTOBER99::Voucher Starbucks Giảm 20K::HSD: 31 T12 2026::BRAND::BARCODE";
        }
        return "MOMO30K-" + clean.toUpperCase() + "::Voucher MoMo Hoàn 30K Nạp ĐT::HSD: 31 T12 2026::SHOP::QR_IMAGE";
    }

    public static int getBalance(Context context, String username) {
        String clean = SocialHelper.cleanUsername(username);
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = "SO_DU_XU_SS_" + clean;
        if (!prefs.contains(key)) {
            int initial = getDefaultBalanceForUser(clean);
            prefs.edit().putInt(key, initial).apply();
            setBalanceInFirestore(clean, initial);
            return initial;
        }
        return prefs.getInt(key, getDefaultBalanceForUser(clean));
    }

    public static void setBalance(Context context, String username, int newBalance) {
        String clean = SocialHelper.cleanUsername(username);
        int val = Math.max(0, newBalance);
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt("SO_DU_XU_SS_" + clean, val).apply();
        setBalanceInFirestore(clean, val);
    }

    public static void addBalance(Context context, String username, int delta) {
        int current = getBalance(context, username);
        setBalance(context, username, current + delta);
    }

    public static void setBalanceInFirestore(String cleanUsername, int val) {
        if (cleanUsername == null || cleanUsername.trim().isEmpty()) return;
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("so_du_xu", val);
            updateData.put("soDuXu", val);
            db.collection("users").document(cleanUsername)
                    .set(updateData, SetOptions.merge());

            Map<String, Object> walletData = new HashMap<>();
            walletData.put("soDuXuKhaDung", val);
            walletData.put("username", cleanUsername);
            db.collection("wallets").document(cleanUsername)
                    .set(walletData, SetOptions.merge());
        } catch (Exception e) {
            Log.e(TAG, "Lỗi cập nhật số dư lên Firestore cho user: " + cleanUsername, e);
        }
    }

    public static String getTransactionHistory(Context context, String username) {
        String clean = SocialHelper.cleanUsername(username);
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = "DANH_SACH_GIAO_DICH_" + clean;
        String val = prefs.getString(key, "");
        if (val.isEmpty()) {
            val = getDefaultTransactionsForUser(clean);
            prefs.edit().putString(key, val).apply();
        }
        return val;
    }

    public static void addTransactionRecord(Context context, String username, String recordStr, String txId, String title, int amountSS, String type, String status, String description) {
        String clean = SocialHelper.cleanUsername(username);
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = "DANH_SACH_GIAO_DICH_" + clean;
        String currentList = prefs.getString(key, "");
        if (currentList.isEmpty()) {
            currentList = getDefaultTransactionsForUser(clean);
        }
        String newList = recordStr + ";;" + currentList;
        prefs.edit().putString(key, newList).apply();

        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> txData = new HashMap<>();
            txData.put("tx_id", txId);
            txData.put("title", title != null ? title : "Giao dịch SSharing");
            txData.put("username", clean);
            txData.put("amount_ss", amountSS);
            txData.put("type", type != null ? type : "TRANSACTION");
            txData.put("status", status != null ? status : "SUCCESS");
            txData.put("description", description != null ? description : "");
            txData.put("record_str", recordStr);
            txData.put("created_at", System.currentTimeMillis());

            db.collection("users").document(clean)
                    .collection("transactions").document(txId)
                    .set(txData, SetOptions.merge());

            db.collection("transactions").document(txId)
                    .set(txData, SetOptions.merge());
        } catch (Exception e) {
            Log.e(TAG, "Lỗi đẩy giao dịch lên Firestore", e);
        }
    }

    public static String getSavedVouchers(Context context, String username) {
        String clean = SocialHelper.cleanUsername(username);
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = "DANH_SACH_VOUCHER_DA_LUU_" + clean;
        String val = prefs.getString(key, "");
        if (val.isEmpty()) {
            val = getDefaultVouchersForUser(clean);
            prefs.edit().putString(key, val).apply();
        }
        return val;
    }

    public static void setSavedVouchers(Context context, String username, String vouchersStr) {
        String clean = SocialHelper.cleanUsername(username);
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString("DANH_SACH_VOUCHER_DA_LUU_" + clean, vouchersStr).apply();
    }

    public static void addSavedVoucherRecord(Context context, String username, String voucherRecordStr, String voucherId, String title) {
        String clean = SocialHelper.cleanUsername(username);
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = "DANH_SACH_VOUCHER_DA_LUU_" + clean;
        String currentList = prefs.getString(key, "");
        if (currentList.isEmpty()) {
            currentList = getDefaultVouchersForUser(clean);
        }
        String newList = voucherRecordStr + ";;" + currentList;
        prefs.edit().putString(key, newList).apply();

        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> vData = new HashMap<>();
            vData.put("voucher_id", voucherId);
            vData.put("title", title != null ? title : "Voucher SSharing");
            vData.put("record_str", voucherRecordStr);
            vData.put("username", clean);
            vData.put("saved_at", System.currentTimeMillis());

            db.collection("users").document(clean)
                    .collection("vouchers").document(voucherId)
                    .set(vData, SetOptions.merge());
        } catch (Exception e) {
            Log.e(TAG, "Lỗi đẩy voucher lên Firestore", e);
        }
    }

    public static void syncFromFirestore(Context context, String username, Runnable onComplete) {
        String clean = SocialHelper.cleanUsername(username);
        if (clean.isEmpty()) {
            if (onComplete != null) onComplete.run();
            return;
        }

        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(clean).get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            Long val = doc.getLong("so_du_xu");
                            if (val == null) val = doc.getLong("soDuXu");
                            if (val != null) {
                                SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                                prefs.edit().putInt("SO_DU_XU_SS_" + clean, val.intValue()).apply();
                            } else {
                                int currentLocal = getBalance(context, clean);
                                setBalanceInFirestore(clean, currentLocal);
                            }
                        } else {
                            int currentLocal = getBalance(context, clean);
                            setBalanceInFirestore(clean, currentLocal);
                        }

                        db.collection("users").document(clean)
                                .collection("transactions")
                                .orderBy("created_at", Query.Direction.DESCENDING)
                                .get()
                                .addOnSuccessListener(txDocs -> {
                                    if (!txDocs.isEmpty()) {
                                        StringBuilder sb = new StringBuilder();
                                        for (DocumentSnapshot tDoc : txDocs.getDocuments()) {
                                            String rec = tDoc.getString("record_str");
                                            if (rec != null && !rec.trim().isEmpty()) {
                                                if (sb.length() > 0) sb.append(";;");
                                                sb.append(rec);
                                            }
                                        }
                                        if (sb.length() > 0) {
                                            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                                            prefs.edit().putString("DANH_SACH_GIAO_DICH_" + clean, sb.toString()).apply();
                                        }
                                    }

                                    db.collection("users").document(clean)
                                            .collection("vouchers")
                                            .get()
                                            .addOnSuccessListener(vDocs -> {
                                                if (!vDocs.isEmpty()) {
                                                    StringBuilder sbV = new StringBuilder();
                                                    for (DocumentSnapshot vDoc : vDocs.getDocuments()) {
                                                        String rec = vDoc.getString("record_str");
                                                        if (rec != null && !rec.trim().isEmpty()) {
                                                            if (sbV.length() > 0) sbV.append(";;");
                                                            sbV.append(rec);
                                                        }
                                                    }
                                                    if (sbV.length() > 0) {
                                                        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                                                        prefs.edit().putString("DANH_SACH_VOUCHER_DA_LUU_" + clean, sbV.toString()).apply();
                                                    }
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
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Chế độ Ngoại tuyến - sử dụng dữ liệu SharedPreferences cho: " + clean, e);
                        if (onComplete != null) onComplete.run();
                    });
        } catch (Exception e) {
            if (onComplete != null) onComplete.run();
        }
    }
}
