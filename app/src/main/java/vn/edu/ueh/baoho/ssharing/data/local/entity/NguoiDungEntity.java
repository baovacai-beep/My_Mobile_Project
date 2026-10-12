package vn.edu.ueh.baoho.ssharing.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "nguoi_dung_entity")
public class NguoiDungEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ma_nguoi_dung")
    private String maNguoiDung;

    @ColumnInfo(name = "ho_ten")
    private String hoTen;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "so_dien_thoai")
    private String soDienThoai;

    @ColumnInfo(name = "duong_dan_anh_dai_dien")
    private String duongDanAnhDaiDien;

    @ColumnInfo(name = "vai_tro")
    private String vaiTro;

    @ColumnInfo(name = "da_xac_thuc_ekyc")
    private boolean daXacThucEkyc;

    @ColumnInfo(name = "so_cccd")
    private String soCccd;

    @ColumnInfo(name = "da_kich_hoat_2fa")
    private boolean daKichHoat2Fa;

    @ColumnInfo(name = "diem_tin_nhiem")
    private float diemTinNhiem;

    @ColumnInfo(name = "so_voucher_da_tang")
    private int soVoucherDaTang;

    @ColumnInfo(name = "so_giao_dich_thanh_cong")
    private int soGiaoDichThanhCong;

    public NguoiDungEntity() {}

    @NonNull
    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(@NonNull String maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getDuongDanAnhDaiDien() { return duongDanAnhDaiDien; }
    public void setDuongDanAnhDaiDien(String duongDanAnhDaiDien) { this.duongDanAnhDaiDien = duongDanAnhDaiDien; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    public boolean isDaXacThucEkyc() { return daXacThucEkyc; }
    public void setDaXacThucEkyc(boolean daXacThucEkyc) { this.daXacThucEkyc = daXacThucEkyc; }

    public String getSoCccd() { return soCccd; }
    public void setSoCccd(String soCccd) { this.soCccd = soCccd; }

    public boolean isDaKichHoat2Fa() { return daKichHoat2Fa; }
    public void setDaKichHoat2Fa(boolean daKichHoat2Fa) { this.daKichHoat2Fa = daKichHoat2Fa; }

    public float getDiemTinNhiem() { return diemTinNhiem; }
    public void setDiemTinNhiem(float diemTinNhiem) { this.diemTinNhiem = diemTinNhiem; }

    public int getSoVoucherDaTang() { return soVoucherDaTang; }
    public void setSoVoucherDaTang(int soVoucherDaTang) { this.soVoucherDaTang = soVoucherDaTang; }

    public int getSoGiaoDichThanhCong() { return soGiaoDichThanhCong; }
    public void setSoGiaoDichThanhCong(int soGiaoDichThanhCong) { this.soGiaoDichThanhCong = soGiaoDichThanhCong; }
}