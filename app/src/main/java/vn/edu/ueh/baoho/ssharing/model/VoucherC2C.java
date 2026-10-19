package vn.edu.ueh.baoho.ssharing.model;

public class VoucherC2C extends Voucher {
    public VoucherC2C(String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat) {
        super(id, tieuDe, tenThuongHieu, giaGoc, giaBanXu, hanSuDung, idNguoiDang, maCodeBiMat);
    }

    @Override
    public double tinhPhiSanGiaoDich() {
        return getGiaBanXu() * 0.05; // 5% phí giao dịch ngang hàng C2C
    }

    @Override
    public String layPhanLoaiVoucher() {
        return "C2C";
    }
}