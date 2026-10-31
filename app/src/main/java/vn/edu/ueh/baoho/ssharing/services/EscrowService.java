package vn.edu.ueh.baoho.ssharing.services;

import vn.edu.ueh.baoho.ssharing.model.interfaces.IXuLyEscrow;

public class EscrowService implements IXuLyEscrow {

    @Override
    public boolean giuTienKyQuy(String maDonHang, String maNguoiMua, long soXu) {
        return maDonHang != null && maNguoiMua != null && soXu > 0;
    }

    @Override
    public boolean giaiNganNguoiBan(String maDonHang, String maNguoiBan) {
        return maDonHang != null && maNguoiBan != null;
    }

    @Override
    public boolean hoanTienNguoiMua(String maDonHang, String maNguoiMua) {
        return maDonHang != null && maNguoiMua != null;
    }
}
