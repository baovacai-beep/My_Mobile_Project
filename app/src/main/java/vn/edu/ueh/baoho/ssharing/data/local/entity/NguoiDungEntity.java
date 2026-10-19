package vn.edu.ueh.baoho.ssharing.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class NguoiDungEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private int diemTinNhiem;
    private String vaiTro;
    private boolean daXacThucEkyc;

    public NguoiDungEntity(@NonNull String id, String hoTen, String email, String soDienThoai, int diemTinNhiem, String vaiTro, boolean daXacThucEkyc) {
        this.id = id;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diemTinNhiem = diemTinNhiem;
        this.vaiTro = vaiTro;
        this.daXacThucEkyc = daXacThucEkyc;
    }

    @NonNull public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public int getDiemTinNhiem() { return diemTinNhiem; }
    public void setDiemTinNhiem(int diemTinNhiem) { this.diemTinNhiem = diemTinNhiem; }
    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    public boolean isDaXacThucEkyc() { return daXacThucEkyc; }
    public void setDaXacThucEkyc(boolean daXacThucEkyc) { this.daXacThucEkyc = daXacThucEkyc; }
}