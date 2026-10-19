package vn.edu.ueh.baoho.ssharing.model;

public class KhachChuaXacThuc extends NguoiDung {
    public KhachChuaXacThuc(String id, String hoTen, String email, String sdt) {
        super(id, hoTen, email, sdt, 50);
    }

    @Override
    public String layVaiTro() {
        return "KHACH_CHUA_EKYC";
    }

    @Override
    public boolean duocPhepDangBanVoucher() {
        return false; // Chưa xác thực định danh thì cấm đăng bán
    }
}