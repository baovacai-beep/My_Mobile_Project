package vn.edu.ueh.baoho.ssharing.utils;

public class VietQrUtils {
    /**
     * Tạo URL mã VietQR động theo chuẩn Napas247 phục vụ hiển thị lên ImageView
     */
    public static String taoUrlVietQr(String bankId, String accountNumber, long amount, String addInfo) {
        // Cấu trúc API public từ VietQR / Napas247
        String template = "compact";
        return String.format("https://img.vietqr.io/image/%s-%s-%s.png?amount=%d&addInfo=%s&accountName=SSHARING%s",
                bankId, accountNumber, template, amount, android.net.Uri.encode(addInfo), "");
    }
}