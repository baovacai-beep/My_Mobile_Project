package vn.edu.ueh.baoho.ssharing.model;

import vn.edu.ueh.baoho.ssharing.model.interfaces.IPhiGiaoDich;

public class VoucherCongDong extends Voucher implements IPhiGiaoDich {
    public VoucherCongDong(String id, String tieuDe, String tenThuongHieu, double giaGoc, String hanSuDung, String idNguoiDang, String maCodeBiMat) {
        super(id, tieuDe, tenThuongHieu, giaGoc, 0.0, hanSuDung, idNguoiDang, maCodeBiMat);
    }

    @Override
    public long tinhPhiSanGiaoDich(long giaBanXu) {
        return 0; // 0% phí chia sẻ miễn phí
    }

    @Override
    public double tinhPhiSanGiaoDich() {
        return 0.0;
    }

    @Override
    public String layPhanLoaiVoucher() {
        return "CONG_DONG";
    }
}
