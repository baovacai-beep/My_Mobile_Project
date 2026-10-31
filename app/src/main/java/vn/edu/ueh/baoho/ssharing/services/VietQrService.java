package vn.edu.ueh.baoho.ssharing.services;

import vn.edu.ueh.baoho.ssharing.model.interfaces.ICongThanhToan;

public class VietQrService implements ICongThanhToan {

    @Override
    public boolean taoYeuCauNapTien(String maGiaoDich, long soTienVND) {
        return maGiaoDich != null && soTienVND > 0;
    }

    @Override
    public String layMaVietQR(String maGiaoDich, long soTienVND) {
        return "https://img.vietqr.io/image/970422-0388915011-compact2.png?amount=" + soTienVND + "&addInfo=" + maGiaoDich + "&accountName=HO%20GIA%20BAO";
    }

    @Override
    public boolean xacThucKetQuaWebhook(String noiDungChuyenKhoan) {
        return noiDungChuyenKhoan != null && noiDungChuyenKhoan.contains("NAPXU");
    }
}
