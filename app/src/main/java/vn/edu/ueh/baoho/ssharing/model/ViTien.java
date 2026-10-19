package vn.edu.ueh.baoho.ssharing.model;

import java.io.Serializable;

/**
 * Thể hiện tính ĐÓNG GÓI: Toàn bộ số dư ví và quỹ ký quỹ Escrow
 * được đặt quyền private, chỉ được can thiệp qua các phương thức nghiệp vụ an toàn.
 */
public class ViTien implements Serializable {
    private String maVi;
    private String maNguoiDung;
    private long soDuXuKhaDung;
    private long soDuXuDongBangEscrow;

    public ViTien(String maVi, String maNguoiDung, long soDuXuKhaDung, long soDuXuDongBangEscrow) {
        this.maVi = maVi;
        this.maNguoiDung = maNguoiDung;
        this.soDuXuKhaDung = Math.max(0, soDuXuKhaDung);
        this.soDuXuDongBangEscrow = Math.max(0, soDuXuDongBangEscrow);
    }

    public String getMaVi() { return maVi; }
    public String getMaNguoiDung() { return maNguoiDung; }
    public long getSoDuXuKhaDung() { return soDuXuKhaDung; }
    public long getSoDuXuDongBangEscrow() { return soDuXuDongBangEscrow; }

    public synchronized boolean napXu(long soXu) {
        if (soXu <= 0) return false;
        this.soDuXuKhaDung += soXu;
        return true;
    }

    public synchronized boolean dongBangXuEscrow(long soXu) {
        if (soXu <= 0 || this.soDuXuKhaDung < soXu) return false;
        this.soDuXuKhaDung -= soXu;
        this.soDuXuDongBangEscrow += soXu;
        return true;
    }

    public synchronized boolean giaiNganEscrow(long soXu) {
        if (soXu <= 0 || this.soDuXuDongBangEscrow < soXu) return false;
        this.soDuXuDongBangEscrow -= soXu;
        return true;
    }

    public synchronized boolean hoanTraEscrow(long soXu) {
        if (soXu <= 0 || this.soDuXuDongBangEscrow < soXu) return false;
        this.soDuXuDongBangEscrow -= soXu;
        this.soDuXuKhaDung += soXu;
        return true;
    }
}