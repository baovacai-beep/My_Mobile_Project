package vn.edu.ueh.baoho.ssharing.model;

import vn.edu.ueh.baoho.ssharing.model.interfaces.IPhiGiaoDich;

public class VoucherChinhHangMall extends Voucher implements IPhiGiaoDich {

    public VoucherChinhHangMall(String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat) {
        super(id, tieuDe, tenThuongHieu, giaGoc, giaBanXu, hanSuDung, idNguoiDang, maCodeBiMat);
    }

    public VoucherChinhHangMall(String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat, int trustScore) {
        super(id, tieuDe, tenThuongHieu, giaGoc, giaBanXu, hanSuDung, idNguoiDang, maCodeBiMat, trustScore);
    }

    @Override
    public long tinhPhiSanGiaoDich(long giaBanXu) {
        return 0;
    }

    @Override
    public double tinhPhiSanGiaoDich() {
        return 0;
    }

    @Override
    public String layPhanLoaiVoucher() {
        return "CHINH_HANG_MALL";
    }
}
