package vn.edu.ueh.baoho.ssharing.model;

public class VoucherCongDong extends Voucher {
    public VoucherCongDong(String id, String tieuDe, String tenThuongHieu, double giaGoc, String hanSuDung, String idNguoiDang, String maCodeBiMat) {
        super(id, tieuDe, tenThuongHieu, giaGoc, 0.0, hanSuDung, idNguoiDang, maCodeBiMat);
    }

    @Override
    public double tinhPhiSanGiaoDich() {
        return 0.0; // 0% phí chia sẻ miễn phí
    }

    @Override
    public String layPhanLoaiVoucher() {
        return "CONG_DONG";
    }
}