package vn.edu.ueh.baoho.ssharing.model;

public class NhanHangDoiTac extends NguoiDung {
    private String tenThuongHieu;
    private String maSoThue;

    public NhanHangDoiTac(String id, String hoTen, String email, String sdt, String tenThuongHieu, String maSoThue) {
        super(id, hoTen, email, sdt, 100);
        this.tenThuongHieu = tenThuongHieu;
        this.maSoThue = maSoThue;
    }

    public String getTenThuongHieu() { return tenThuongHieu; }
    public String getMaSoThue() { return maSoThue; }

    @Override
    public String layVaiTro() {
        return "DOI_TAC_OFFICIAL_MALL";
    }

    @Override
    public boolean duocPhepDangBanVoucher() {
        return true;
    }
}