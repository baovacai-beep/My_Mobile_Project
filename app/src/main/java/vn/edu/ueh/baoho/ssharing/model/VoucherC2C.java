package vn.edu.ueh.baoho.ssharing.model;

public class VoucherC2C extends VoucherC2CCongDong {
    public VoucherC2C(String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat) {
        super(id, tieuDe, tenThuongHieu, giaGoc, giaBanXu, hanSuDung, idNguoiDang, maCodeBiMat);
    }

    public VoucherC2C(String id, String tieuDe, String tenThuongHieu, double giaGoc, double giaBanXu, String hanSuDung, String idNguoiDang, String maCodeBiMat, int trustScore) {
        super(id, tieuDe, tenThuongHieu, giaGoc, giaBanXu, hanSuDung, idNguoiDang, maCodeBiMat, trustScore);
    }
}
