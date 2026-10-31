package vn.edu.ueh.baoho.ssharing.utils;

import vn.edu.ueh.baoho.ssharing.model.VoucherC2CCongDong;
import vn.edu.ueh.baoho.ssharing.model.VoucherChinhHangMall;

public class InstantBuybackHelper {

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
     * Tính toán công thức thu mua tức thì C2C/Cộng đồng: 2% mặc định, 1% nếu Trust Score >= 80 (Kim Cương)
     */
    public static BuybackResult tinhToanThuMuaTucThiC2C(double giaTriNiemYetVoucherXu, int trustScore) {
        VoucherC2CCongDong v = new VoucherC2CCongDong("VC", "Voucher C2C", "Brand", 0, giaTriNiemYetVoucherXu, "31/12/2026", "USER", "SECRET", trustScore);
        long phiSan = v.tinhPhiSanGiaoDich((long) giaTriNiemYetVoucherXu);
        double thucNhan = giaTriNiemYetVoucherXu - phiSan;
        return new BuybackResult(giaTriNiemYetVoucherXu, phiSan, Math.max(0, thucNhan));
    }

    /**
     * Tính toán thu mua chính hãng Mall: Phí sàn luôn trả về 0 SS theo cam kết trợ giá đối tác
     */
    public static BuybackResult tinhToanThuMuaMall(double giaTriNiemYetVoucherXu) {
        VoucherChinhHangMall v = new VoucherChinhHangMall("VM", "Voucher Mall", "Brand Mall", 0, giaTriNiemYetVoucherXu, "31/12/2026", "BRAND", "SECRET");
        long phiSan = v.tinhPhiSanGiaoDich((long) giaTriNiemYetVoucherXu);
        return new BuybackResult(giaTriNiemYetVoucherXu, phiSan, giaTriNiemYetVoucherXu);
    }
}
