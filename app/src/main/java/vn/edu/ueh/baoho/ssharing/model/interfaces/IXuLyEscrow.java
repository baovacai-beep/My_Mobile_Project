package vn.edu.ueh.baoho.ssharing.model.interfaces;

public interface IXuLyEscrow {
    boolean giuTienKyQuy(String maDonHang, String maNguoiMua, long soXu);
    boolean giaiNganNguoiBan(String maDonHang, String maNguoiBan);
    boolean hoanTienNguoiMua(String maDonHang, String maNguoiMua);
}