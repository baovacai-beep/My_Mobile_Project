package vn.edu.ueh.baoho.ssharing.model;

public class KhachDaXacThuc extends NguoiDung {
    private String soCCCD;
    private String ngayXacThuc;

    public KhachDaXacThuc(String id, String hoTen, String email, String sdt, int diem, String soCCCD, String ngayXacThuc) {
        super(id, hoTen, email, sdt, diem);
        this.soCCCD = soCCCD;
        this.ngayXacThuc = ngayXacThuc;
    }

    public String getSoCCCD() { return soCCCD; }
    public String getNgayXacThuc() { return ngayXacThuc; }

    @Override
    public String layVaiTro() {
        return "KHACH_DA_EKYC";
    }

    @Override
    public boolean duocPhepDangBanVoucher() {
        return true;
    }
}