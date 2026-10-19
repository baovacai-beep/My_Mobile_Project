package vn.edu.ueh.baoho.ssharing.model;

public class NhanVienHoTro extends NguoiDung {
    private String maNhanVien;
    private String boPhan;

    public NhanVienHoTro(String id, String hoTen, String email, String sdt, String maNhanVien, String boPhan) {
        super(id, hoTen, email, sdt, 100);
        this.maNhanVien = maNhanVien;
        this.boPhan = boPhan;
    }

    public String getMaNhanVien() { return maNhanVien; }
    public String getBoPhan() { return boPhan; }

    @Override
    public String layVaiTro() {
        return "TRONG_TAI_VIEN_CSKH";
    }

    @Override
    public boolean duocPhepDangBanVoucher() {
        return false;
    }
}