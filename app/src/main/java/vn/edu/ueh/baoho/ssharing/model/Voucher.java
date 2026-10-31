package vn.edu.ueh.baoho.ssharing.model;

import java.io.Serializable;

public abstract class Voucher implements Serializable {
    private String id;
    private String tieuDe;
    private String tenThuongHieu;
    private double giaGoc;
    private double giaBanXu;
    private String hanSuDung;
    private String idNguoiDang;
    private boolean daBan;
    protected int trustScore = 90;

    // ĐÓNG GÓI: Mã bí mật luôn được ẩn giấu
    private String maCodeBiMat;

    public Voucher(String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat) {
        this.id = id;
        this.tieuDe = tieuDe;
        this.tenThuongHieu = tenThuongHieu;
        this.giaGoc = giaGoc;
        this.giaBanXu = giaBanXu;
        this.hanSuDung = hanSuDung;
        this.idNguoiDang = idNguoiDang;
        this.maCodeBiMat = maCodeBiMat;
        this.daBan = false;
        this.trustScore = 90;
    }

    public Voucher(String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat, int trustScore) {
        this(id, tieuDe, tenThuongHieu, giaGoc, giaBanXu, hanSuDung, idNguoiDang, maCodeBiMat);
        this.trustScore = trustScore;
    }

    public String getId() { return id; }
    public String getTieuDe() { return tieuDe; }
    public String getTenThuongHieu() { return tenThuongHieu; }
    public double getGiaGoc() { return giaGoc; }
    public double getGiaBanXu() { return giaBanXu; }
    public String getHanSuDung() { return hanSuDung; }
    public String getIdNguoiDang() { return idNguoiDang; }
    public boolean isDaBan() { return daBan; }
    public void setDaBan(boolean daBan) { this.daBan = daBan; }
    public int getTrustScore() { return trustScore; }
    public void setTrustScore(int trustScore) { this.trustScore = trustScore; }

    /**
     * Chỉ giải mã khi người mua đã thanh toán vào ví trung gian Escrow
     */
    public String xemMaCodeDaMoKhoa(boolean daGiuTienEscrow) {
        if (daGiuTienEscrow) {
            return this.maCodeBiMat;
        }
        return "SSH***HIDDEN (Bảo vệ bởi Escrow)";
    }

    public void capNhatMaCode(String maMoi, String idNguoiSua) {
        if (idNguoiSua != null && idNguoiSua.equals(this.idNguoiDang)) {
            this.maCodeBiMat = maMoi;
        }
    }

    // ĐA HÌNH: Tính phí sàn khác nhau cho từng loại Voucher
    public abstract long tinhPhiSanGiaoDich(long giaBanXu);
    public abstract double tinhPhiSanGiaoDich();
    public abstract String layPhanLoaiVoucher();
}
