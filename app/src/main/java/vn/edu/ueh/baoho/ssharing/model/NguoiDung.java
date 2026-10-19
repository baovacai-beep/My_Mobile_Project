package vn.edu.ueh.baoho.ssharing.model;

import java.io.Serializable;

public abstract class NguoiDung implements Serializable {
    private String id;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private int diemTinNhiemTrustScore; // 0 - 100

    public NguoiDung(String id, String hoTen, String email, String soDienThoai, int diemTinNhiem) {
        this.id = id;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diemTinNhiemTrustScore = Math.min(100, Math.max(0, diemTinNhiem));
    }

    public String getId() { return id; }
    public String getHoTen() { return hoTen; }
    public String getEmail() { return email; }
    public String getSoDienThoai() { return soDienThoai; }
    public int getDiemTinNhiemTrustScore() { return diemTinNhiemTrustScore; }

    public void setDiemTinNhiemTrustScore(int diem) {
        this.diemTinNhiemTrustScore = Math.min(100, Math.max(0, diem));
    }

    public abstract String layVaiTro();
    public abstract boolean duocPhepDangBanVoucher();
}