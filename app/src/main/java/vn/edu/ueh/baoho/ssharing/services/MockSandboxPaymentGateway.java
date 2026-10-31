package vn.edu.ueh.baoho.ssharing.services;

import vn.edu.ueh.baoho.ssharing.model.interfaces.ICongThanhToan;

public class MockSandboxPaymentGateway implements ICongThanhToan {

    @Override
    public boolean taoYeuCauNapTien(String maGiaoDich, long soTienVND) {
        return true; // Giả lập thành công Sandbox Demo
    }

    @Override
    public String layMaVietQR(String maGiaoDich, long soTienVND) {
        return "SANDBOX_MOCK_QR_" + maGiaoDich;
    }

    @Override
    public boolean xacThucKetQuaWebhook(String noiDungChuyenKhoan) {
        return true; // Giả lập Webhook auto-approve Sandbox
    }
}
