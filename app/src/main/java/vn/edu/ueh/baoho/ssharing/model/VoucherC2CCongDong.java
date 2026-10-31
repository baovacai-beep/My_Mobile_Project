package vn.edu.ueh.baoho.ssharing.model;

import vn.edu.ueh.baoho.ssharing.model.interfaces.IPhiGiaoDich;

public class VoucherC2CCongDong extends Voucher implements IPhiGiaoDich {
    private String idNguoiBan;
    private String idPhongChatEscrow;
    private String thoiHanBaoHanh;

    public VoucherC2CCongDong(String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat) {
        super(id, tieuDe, tenThuongHieu, giaGoc, giaBanXu, hanSuDung, idNguoiDang, maCodeBiMat);
        this.idNguoiBan = idNguoiDang;
        this.idPhongChatEscrow = "ROOM_" + id;
        this.thoiHanBaoHanh = "24 Giờ Escrow";
    }

    public VoucherC2CCongDong(String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat, int trustScore) {
        super(id, tieuDe, tenThuongHieu, giaGoc, giaBanXu, hanSuDung, idNguoiDang, maCodeBiMat, trustScore);
        this.idNguoiBan = idNguoiDang;
        this.idPhongChatEscrow = "ROOM_" + id;
        this.thoiHanBaoHanh = "24 Giờ Escrow";
    }

    public VoucherC2CCongDong(String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat, String idNguoiBan, String idPhongChatEscrow, String thoiHanBaoHanh, int trustScore) {
        super(id, tieuDe, tenThuongHieu, giaGoc, giaBanXu, hanSuDung, idNguoiDang, maCodeBiMat, trustScore);
        this.idNguoiBan = idNguoiBan;
        this.idPhongChatEscrow = idPhongChatEscrow;
        this.thoiHanBaoHanh = thoiHanBaoHanh;
    }

    public String getIdNguoiBan() { return idNguoiBan; }
    public String getIdPhongChatEscrow() { return idPhongChatEscrow; }
    public String getThoiHanBaoHanh() { return thoiHanBaoHanh; }

    @Override
    public long tinhPhiSanGiaoDich(long giaBanXu) {
        return trustScore >= 80 ? (long)(giaBanXu * 0.01) : (long)(giaBanXu * 0.02);
    }

    @Override
    public double tinhPhiSanGiaoDich() {
        return tinhPhiSanGiaoDich((long) getGiaBanXu());
    }

    @Override
    public String layPhanLoaiVoucher() {
        return "C2C_CONG_DONG";
    }
}
