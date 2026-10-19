package vn.edu.ueh.baoho.ssharing.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MaHoaMD5Utils {

    /**
     * Mã hóa mật khẩu đầu vào sang chuỗi MD5 Hex
     */
    public static String maHoaMatKhauMD5(String matKhauGoc) {
        if (matKhauGoc == null || matKhauGoc.isEmpty()) return "";
        try {
            MessageDigest boMaHoa = MessageDigest.getInstance("MD5");
            byte[] chuoiByte = boMaHoa.digest(matKhauGoc.getBytes());
            StringBuilder chuoiHex = new StringBuilder();
            for (byte b : chuoiByte) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) chuoiHex.append('0');
                chuoiHex.append(hex);
            }
            return chuoiHex.toString();
        } catch (NoSuchAlgorithmException e) {
            return matKhauGoc;
        }
    }
}
