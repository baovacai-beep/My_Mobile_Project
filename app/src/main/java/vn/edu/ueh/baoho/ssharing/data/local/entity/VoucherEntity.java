package vn.edu.ueh.baoho.ssharing.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vouchers")
public class VoucherEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String tieuDe;
    private String tenThuongHieu;
    private double giaGoc;
    private double giaBanXu;
    private String hanSuDung;
    private String idNguoiDang;
    private String maCodeBiMat;
    private String phanLoai; // C2C, MALL, CONG_DONG
    private boolean daBan;

    public VoucherEntity(@NonNull String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat, String phanLoai, boolean daBan) {
        this.id = id;
        this.tieuDe = tieuDe;
        this.tenThuongHieu = tenThuongHieu;
        this.giaGoc = giaGoc;
        this.giaBanXu = giaBanXu;
        this.hanSuDung = hanSuDung;
        this.idNguoiDang = idNguoiDang;
        this.maCodeBiMat = maCodeBiMat;
        this.phanLoai = phanLoai;
        this.daBan = daBan;
    }

    @NonNull public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    public String getTenThuongHieu() { return tenThuongHieu; }
    public void setTenThuongHieu(String tenThuongHieu) { this.tenThuongHieu = tenThuongHieu; }
    public double getGiaGoc() { return giaGoc; }
    public void setGiaGoc(double giaGoc) { this.giaGoc = giaGoc; }
    public double getGiaBanXu() { return giaBanXu; }
    public void setGiaBanXu(double giaBanXu) { this.giaBanXu = giaBanXu; }
    public String getHanSuDung() { return hanSuDung; }
    public void setHanSuDung(String hanSuDung) { this.hanSuDung = hanSuDung; }
    public String getIdNguoiDang() { return idNguoiDang; }
    public void setIdNguoiDang(String idNguoiDang) { this.idNguoiDang = idNguoiDang; }
    public String getMaCodeBiMat() { return maCodeBiMat; }
    public void setMaCodeBiMat(String maCodeBiMat) { this.maCodeBiMat = maCodeBiMat; }
    public String getPhanLoai() { return phanLoai; }
    public void setPhanLoai(String phanLoai) { this.phanLoai = phanLoai; }
    public boolean isDaBan() { return daBan; }
    public void setDaBan(boolean daBan) { this.daBan = daBan; }
}