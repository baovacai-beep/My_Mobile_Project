package vn.edu.ueh.baoho.ssharing.model;

public class VoucherMall extends Voucher {
    public VoucherMall(String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat) {
        super(id, tieuDe, tenThuongHieu, giaGoc, giaBanXu, hanSuDung, idNguoiDang, maCodeBiMat);
    }

    @Override
    public double tinhPhiSanGiaoDich() {
        return getGiaBanXu() * 0.03; // 3% phí ưu đãi cho gian hàng chính hãng
    }

    @Override
    public String layPhanLoaiVoucher() {
        return "MALL";
    }
}