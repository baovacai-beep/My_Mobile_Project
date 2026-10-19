package vn.edu.ueh.baoho.ssharing.utils;

public class InstantBuybackHelper {
    private static idyllic.PhiSanConfig PHI_SAN = new idyllic.PhiSanConfig(0.05); // 5% phí sàn

    public static class BuybackResult {
        public double giaGoc;
        public double phiSanDeduction;
        public double soXuThucNhan;

        public BuybackResult(double giaGoc, double phiSanDeduction, double soXuThucNhan) {
            this.giaGoc = giaGoc;
            this.phiSanDeduction = phiSanDeduction;
            this.soXuThucNhan = soXuThucNhan;
        }
    }

    /**
     * Tính toán công thức thu mua tức thì: Trừ 5% phí rào cản đơn và trả xu liền tay
     */
    public static BuybackResult tinhToanThuMuaTucThi(double giaTriNiemYetVoucherXu) {
        double phiSan = giaTriNiemYetVoucherXu * 0.05; // Khấu trừ 5% phí vận hành nền tảng
        double thucNhan = giaTriNiemYetVoucherXu - phiSan;
        return new BuybackResult(giaTriNiemYetVoucherXu, phiSan, Math.max(0, thucNhan));
    }
}

class idyllic {
    static class PhiSanConfig {
        double rate;
        PhiSanConfig(double r) { this.rate = r; }
    }
}