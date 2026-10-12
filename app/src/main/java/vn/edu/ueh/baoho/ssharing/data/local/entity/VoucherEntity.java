package vn.edu.ueh.baoho.ssharing.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "voucher_entity")
public class VoucherEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ma_voucher")
    private String maVoucher;

    @ColumnInfo(name = "ma_nguoi_dang")
    private String maNguoiDang;

    @ColumnInfo(name = "kenh_ban")
    private String kenhBan;

    @ColumnInfo(name = "ten_thuong_hieu")
    private String tenThuongHieu;

    @ColumnInfo(name = "tieu_de_voucher")
    private String tieuDeVoucher;

    @ColumnInfo(name = "mo_ta_dieu_kien")
    private String moTaDieuKien;

    @ColumnInfo(name = "duong_dan_anh_voucher")
    private String duongDanAnhVoucher;

    @ColumnInfo(name = "danh_muc")
    private String danhMuc;

    @ColumnInfo(name = "gia_tri_giam_vnd")
    private int giaTriGiamVnd;

    @ColumnInfo(name = "gia_tri_don_toi_thieu_vnd")
    private int giaTriDonToiThieuVnd;

    @ColumnInfo(name = "gia_ban_xu")
    private int giaBanXu;

    @ColumnInfo(name = "la_coupon_cong_dong")
    private boolean laCouponCongDong;

    @ColumnInfo(name = "ngay_het_han")
    private String ngayHetHan;

    @ColumnInfo(name = "trang_thai")
    private String trangThai;

    public VoucherEntity() {}

    @NonNull
    public String getMaVoucher() { return maVoucher; }
    public void setMaVoucher(@NonNull String maVoucher) { this.maVoucher = maVoucher; }

    public String getMaNguoiDang() { return maNguoiDang; }
    public void setMaNguoiDang(String maNguoiDang) { this.maNguoiDang = maNguoiDang; }

    public String getKenhBan() { return kenhBan; }
    public void setKenhBan(String kenhBan) { this.kenhBan = kenhBan; }

    public String getTenThuongHieu() { return tenThuongHieu; }
    public void setTenThuongHieu(String tenThuongHieu) { this.tenThuongHieu = tenThuongHieu; }

    public String getTieuDeVoucher() { return tieuDeVoucher; }
    public void setTieuDeVoucher(String tieuDeVoucher) { this.tieuDeVoucher = tieuDeVoucher; }

    public String getMoTaDieuKien() { return moTaDieuKien; }
    public void setMoTaDieuKien(String moTaDieuKien) { this.moTaDieuKien = moTaDieuKien; }

    public String getDuongDanAnhVoucher() { return duongDanAnhVoucher; }
    public void setDuongDanAnhVoucher(String duongDanAnhVoucher) { this.duongDanAnhVoucher = duongDanAnhVoucher; }

    public String getDanhMuc() { return danhMuc; }
    public void setDanhMuc(String danhMuc) { this.danhMuc = danhMuc; }

    public int getGiaTriGiamVnd() { return giaTriGiamVnd; }
    public void setGiaTriGiamVnd(int giaTriGiamVnd) { this.giaTriGiamVnd = giaTriGiamVnd; }

    public int getGiaTriDonToiThieuVnd() { return giaTriDonToiThieuVnd; }
    public void setGiaTriDonToiThieuVnd(int giaTriDonToiThieuVnd) { this.giaTriDonToiThieuVnd = giaTriDonToiThieuVnd; }

    public int getGiaBanXu() { return giaBanXu; }
    public void setGiaBanXu(int giaBanXu) { this.giaBanXu = giaBanXu; }

    public boolean isLaCouponCongDong() { return laCouponCongDong; }
    public void setLaCouponCongDong(boolean laCouponCongDong) { this.laCouponCongDong = laCouponCongDong; }

    public String getNgayHetHan() { return ngayHetHan; }
    public void setNgayHetHan(String ngayHetHan) { this.ngayHetHan = ngayHetHan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}