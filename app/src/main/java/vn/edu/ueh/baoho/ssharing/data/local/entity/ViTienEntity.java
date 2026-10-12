package vn.edu.ueh.baoho.ssharing.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vi_tien_entity")
public class ViTienEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ma_vi")
    private String maVi;

    @ColumnInfo(name = "ma_nguoi_dung")
    private String maNguoiDung;

    @ColumnInfo(name = "so_du_xu_kha_dung")
    private int soDuXuKhaDung;

    @ColumnInfo(name = "so_du_xu_dong_bang_escrow")
    private int soDuXuDongBangEscrow;

    @ColumnInfo(name = "thoi_gian_cap_nhat_cuoi")
    private String thoiGianCapNhatCuoi;

    public ViTienEntity() {}

    @NonNull
    public String getMaVi() { return maVi; }
    public void setMaVi(@NonNull String maVi) { this.maVi = maVi; }

    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(String maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public int getSoDuXuKhaDung() { return soDuXuKhaDung; }
    public void setSoDuXuKhaDung(int soDuXuKhaDung) { this.soDuXuKhaDung = soDuXuKhaDung; }

    public int getSoDuXuDongBangEscrow() { return soDuXuDongBangEscrow; }
    public void setSoDuXuDongBangEscrow(int soDuXuDongBangEscrow) { this.soDuXuDongBangEscrow = soDuXuDongBangEscrow; }

    public String getThoiGianCapNhatCuoi() { return thoiGianCapNhatCuoi; }
    public void setThoiGianCapNhatCuoi(String thoiGianCapNhatCuoi) { this.thoiGianCapNhatCuoi = thoiGianCapNhatCuoi; }
}