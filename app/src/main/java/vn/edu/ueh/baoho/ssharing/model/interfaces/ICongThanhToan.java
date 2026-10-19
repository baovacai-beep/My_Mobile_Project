package vn.edu.ueh.baoho.ssharing.model.interfaces;

public interface ICongThanhToan {
    boolean taoYeuCauNapTien(String maGiaoDich, long soTienVND);
    String layMaVietQR(String maGiaoDich, long soTienVND);
    boolean xacThucKetQuaWebhook(String noiDungChuyenKhoan);
}