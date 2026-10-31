package vn.edu.ueh.baoho.ssharing.model;

public class VoucherMall extends VoucherChinhHangMall {
    public VoucherMall(String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat) {
        super(id, tieuDe, tenThuongHieu, giaGoc, giaBanXu, hanSuDung, idNguoiDang, maCodeBiMat);
    }

    public VoucherMall(String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat, int trustScore) {
        super(id, tieuDe, tenThuongHieu, giaGoc, giaBanXu, hanSuDung, idNguoiDang, maCodeBiMat, trustScore);
    }
}
