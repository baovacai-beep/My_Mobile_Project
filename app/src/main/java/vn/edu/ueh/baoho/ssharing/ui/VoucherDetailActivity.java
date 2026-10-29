package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vn.edu.ueh.baoho.ssharing.R;

public class VoucherDetailActivity extends AppCompatActivity {

    private TextView nhãnTiêuĐềChiTiết;
    private TextView nhãnMãCheMờ;
    private TextView nhãnTênThươngHiệu;
    private TextView nhãnCấpĐộTínNhiệm;
    private Button nútMuaVoucher;

    private String tênVoucherChiTiết = "Voucher Starbucks Giảm 20K";
    private String tênThươngHiệu = "Starbucks Coffee";
    private String mãBíMậtKíchHoạt = "SBUX20K-OCTOBER99";
    private int giáXuVoucher = 160;
    private boolean làGianHàngChínhHãng = true;
    private String tênNgườiBán = "Starbucks Official Store";
    private String avatarNgườiBán = "SB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_detail);

        khởiTạoÁnhXạGiaoDiện();
        đọcDữLiệuTruyềnTới();
        càiĐặtSựKiệnLắngNghe();
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        nhãnTiêuĐềChiTiết = findViewById(R.id.tvVoucherDetailTitle);
        nhãnMãCheMờ = findViewById(R.id.tvVoucherDetailSecretCode);
        nhãnTênThươngHiệu = findViewById(R.id.tvVoucherDetailSeller);
        nhãnCấpĐộTínNhiệm = findViewById(R.id.tvVoucherDetailTrust);
        nútMuaVoucher = findViewById(R.id.btnBuy);
    }

    private void đọcDữLiệuTruyềnTới() {
        Intent dữLiệuĐầuVào = getIntent();
        if (dữLiệuĐầuVào != null) {
            if (dữLiệuĐầuVào.hasExtra("EXTRA_VOUCHER_TITLE")) {
                tênVoucherChiTiết = dữLiệuĐầuVào.getStringExtra("EXTRA_VOUCHER_TITLE");
            }
            if (dữLiệuĐầuVào.hasExtra("EXTRA_BRAND_NAME")) {
                tênThươngHiệu = dữLiệuĐầuVào.getStringExtra("EXTRA_BRAND_NAME");
            }
            if (dữLiệuĐầuVào.hasExtra("EXTRA_PRICE_SS")) {
                giáXuVoucher = dữLiệuĐầuVào.getIntExtra("EXTRA_PRICE_SS", 160);
            }
            if (dữLiệuĐầuVào.hasExtra("EXTRA_SECRET_CODE")) {
                mãBíMậtKíchHoạt = dữLiệuĐầuVào.getStringExtra("EXTRA_SECRET_CODE");
            }
            if (dữLiệuĐầuVào.hasExtra("EXTRA_IS_OFFICIAL")) {
                làGianHàngChínhHãng = dữLiệuĐầuVào.getBooleanExtra("EXTRA_IS_OFFICIAL", true);
            }
            if (dữLiệuĐầuVào.hasExtra("EXTRA_SELLER_NAME")) {
                tênNgườiBán = dữLiệuĐầuVào.getStringExtra("EXTRA_SELLER_NAME");
            }
            if (dữLiệuĐầuVào.hasExtra("EXTRA_SELLER_AVATAR")) {
                avatarNgườiBán = dữLiệuĐầuVào.getStringExtra("EXTRA_SELLER_AVATAR");
            }
        }

        String địnhDạngMã = "CODE_TEXT";
        String nhãnĐịnhDạng = "📋 MÃ CHUỖI KHUYẾN MÃI NHẬP APP";
        if (mãBíMậtKíchHoạt.contains("HLM1T1") || mãBíMậtKíchHoạt.contains("SBUX") || mãBíMậtKíchHoạt.contains("BARCODE")) {
            địnhDạngMã = "BARCODE";
            nhãnĐịnhDạng = "||| MÃ VẠCH (BARCODE) QUÉT TRỰC TIẾP TẠI CỬA HÀNG";
        } else if (mãBíMậtKíchHoạt.contains("SPFOOD") || mãBíMậtKíchHoạt.contains("MOMO") || mãBíMậtKíchHoạt.contains("QR")) {
            địnhDạngMã = "QR_IMAGE";
            nhãnĐịnhDạng = "🖼 MÃ QR CODE 2D BẰNG CHỨNG HÌNH ẢNH THẬT";
        }

        if (nhãnTiêuĐềChiTiết != null) nhãnTiêuĐềChiTiết.setText(tênVoucherChiTiết);
        if (nhãnTênThươngHiệu != null) {
            nhãnTênThươngHiệu.setText(làGianHàngChínhHãng ? "Thương hiệu: " + tênThươngHiệu : "Người bán C2C: " + tênNgườiBán);
        }

        // Tự động tải tên đầy đủ mới nhất của Người bán C2C từ Cloud Firestore
        if (!làGianHàngChínhHãng && tênNgườiBán != null) {
            String sellerClean = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.cleanUsername(tênNgườiBán);
            if (!sellerClean.isEmpty()) {
                vn.edu.ueh.baoho.ssharing.utils.SocialHelper.getUserFullNameFromCloud(this, sellerClean, latestName -> {
                    if (latestName != null && !latestName.trim().isEmpty() && nhãnTênThươngHiệu != null) {
                        nhãnTênThươngHiệu.setText("Người bán C2C: " + latestName + " (@" + sellerClean + ")");
                    }
                });
            }
        }
        if (nhãnCấpĐộTínNhiệm != null) {
            nhãnCấpĐộTínNhiệm.setText(làGianHàngChínhHãng ? "✓ Đối Tác Chính Hãng (Trust Score: 100%)" : "✓ Thành viên cộng đồng (Trust Score: 98%)");
        }
        if (nhãnMãCheMờ != null) nhãnMãCheMờ.setText(nhãnĐịnhDạng + "\n" + cheMờMãBíMật(mãBíMậtKíchHoạt));

        // Kiểm tra xem đây có phải voucher do chính tài khoản này đăng bán không
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String usernameHiệnTại = bộLưuTrữVí.getString("CURRENT_USERNAME", "baoho123");
        String họTênHiệnTại = bộLưuTrữVí.getString("CURRENT_USER_NAME", "Gia Bảo Hồ");

        boolean làVoucherChínhMìnhBán = !làGianHàngChínhHãng && (
                tênNgườiBán.toLowerCase().contains(usernameHiệnTại.toLowerCase()) ||
                        tênNgườiBán.equalsIgnoreCase(họTênHiệnTại)
        );

        if (nútMuaVoucher != null) {
            if (làVoucherChínhMìnhBán) {
                nútMuaVoucher.setText("✎ Chỉnh Sửa Trong Shop Của Bạn");
                nútMuaVoucher.setBackgroundResource(R.drawable.bg_outline_button);
            } else {
                nútMuaVoucher.setText("Mua Ngay " + String.format("%,d", giáXuVoucher) + " SS");
            }
        }
    }

    private String cheMờMãBíMật(String mãGốc) {
        if (mãGốc == null || mãGốc.length() < 4) return "****";
        return mãGốc.substring(0, 2) + "*****" + mãGốc.substring(mãGốc.length() - 2);
    }

    private void càiĐặtSựKiệnLắngNghe() {
        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        if (findViewById(R.id.btnViewMap) != null) {
            findViewById(R.id.btnViewMap).setOnClickListener(v ->
                    startActivity(new Intent(this, MapStoreLocatorActivity.class))
            );
        }

        if (nútMuaVoucher != null) {
            nútMuaVoucher.setOnClickListener(v -> xửLýGiaoDịchMua());
        }
    }

    private void xửLýGiaoDịchMua() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String usernameHiệnTại = bộLưuTrữVí.getString("CURRENT_USERNAME", "baoho123");
        String họTênHiệnTại = bộLưuTrữVí.getString("CURRENT_USER_NAME", "Gia Bảo Hồ");

        boolean làVoucherChínhMìnhBán = !làGianHàngChínhHãng && (
                tênNgườiBán.toLowerCase().contains(usernameHiệnTại.toLowerCase()) ||
                        tênNgườiBán.equalsIgnoreCase(họTênHiệnTại)
        );

        if (làVoucherChínhMìnhBán) {
            Toast.makeText(this, "⚠️ Đây là voucher do chính bạn đăng bán! Bạn chỉ có thể quản lý bài trong Kênh Người Bán.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, SellerDashboardActivity.class));
            finish();
            return;
        }

        String currentUser = vn.edu.ueh.baoho.ssharing.utils.WalletManager.getCurrentUsername(this);
        int sốDưXuHiệnTại = vn.edu.ueh.baoho.ssharing.utils.WalletManager.getBalance(this, currentUser);

        if (sốDưXuHiệnTại < giáXuVoucher) {
            Toast.makeText(this, "Số dư Xu không đủ! Bạn cần " + giáXuVoucher + " SS.", Toast.LENGTH_LONG).show();
            return;
        }

        if (làGianHàngChínhHãng) {
            // MUA VOUCHER CHÍNH HÃNG MALL -> GIAO TỨC THÌ VÀO VÍ VOUCHER
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận đổi Voucher Mall")
                    .setMessage("Bạn có chắc chắn dùng " + giáXuVoucher + " SS để đổi mã voucher chính hãng '" + tênVoucherChiTiết + "'?")
                    .setPositiveButton("Đổi ngay", (hộpThoại, vịTrí) -> thựcHiệnMuaChínhHãng(currentUser))
                    .setNegativeButton("Hủy", null)
                    .show();
        } else {
            // MUA VOUCHER C2C CỘNG ĐỒNG -> TẠM GIỮ ESCROW VÀ MỞ PHÒNG CHAT VỚI NGƯỜI BÁN CỤ THỂ
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận tạm giữ xu Escrow")
                    .setMessage("Hệ thống sẽ tạm giữ " + giáXuVoucher + " SS và mở phòng chat giao dịch bảo mật với " + tênNgườiBán + ".")
                    .setPositiveButton("Mở Escrow Chat", (hộpThoại, vịTrí) -> mởPhòngChatEscrow())
                    .setNegativeButton("Hủy", null)
                    .show();
        }
    }

    private void thựcHiệnMuaChínhHãng(String currentUser) {
        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        String địnhDạngMã = "CODE_TEXT";
        if (mãBíMậtKíchHoạt.contains("HLM1T1") || mãBíMậtKíchHoạt.contains("SBUX") || mãBíMậtKíchHoạt.contains("BARCODE")) {
            địnhDạngMã = "BARCODE";
        } else if (mãBíMậtKíchHoạt.contains("SPFOOD") || mãBíMậtKíchHoạt.contains("MOMO") || mãBíMậtKíchHoạt.contains("QR")) {
            địnhDạngMã = "QR_IMAGE";
        }

        // 1. Thêm vào Ví Voucher
        String voucherMớiThêm = mãBíMậtKíchHoạt + "::" + tênVoucherChiTiết + "::HSD: 31 T12 2026::BRAND::" + địnhDạngMã;

        // 2. Thêm vào Lịch Sử Giao Dịch
        String mãGiaoDịchRandom = "TX-MALL" + (100000 + (int)(Math.random() * 899999));
        String bảnGhiGiaoDịchMới = mãGiaoDịchRandom + "||Đổi Voucher Mall " + tênThươngHiệu + "||-" + giáXuVoucher + " SS||" + thờiGianThực + "||SSharing Mall||Thành công||Thanh toán " + giáXuVoucher + " SS đổi mã " + mãBíMậtKíchHoạt;

        vn.edu.ueh.baoho.ssharing.utils.WalletManager.addBalance(this, currentUser, -giáXuVoucher);
        vn.edu.ueh.baoho.ssharing.utils.WalletManager.addSavedVoucherRecord(this, currentUser, voucherMớiThêm, mãBíMậtKíchHoạt, tênVoucherChiTiết);
        vn.edu.ueh.baoho.ssharing.utils.WalletManager.addTransactionRecord(this, currentUser, bảnGhiGiaoDịchMới, mãGiaoDịchRandom, "Đổi Voucher Mall " + tênThươngHiệu, -giáXuVoucher, "PURCHASE", "SUCCESS", "Thanh toán đổi voucher " + mãBíMậtKíchHoạt);

        Toast.makeText(this, "🎉 Mua thành công " + tênVoucherChiTiết + "! Mã đã lưu vào Ví của bạn.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, WalletActivity.class));
        finish();
    }

    private void mởPhòngChatEscrow() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String buyerUser = bộLưuTrữVí.getString("CURRENT_USERNAME", "baoho123");
        String sellerClean = tênNgườiBán.toLowerCase().replaceAll("[^a-z0-9]", "").trim();
        if (sellerClean.isEmpty()) sellerClean = "beo123";

        String idPhòngChat = "CHAT_ROOM_" + mãBíMậtKíchHoạt;

        String thờiGianThực = new SimpleDateFormat("HH:mm dd/MM", Locale.getDefault()).format(new Date());
        String tbMới = "🛒 [GIAO DỊCH MỚI] Người dùng @" + buyerUser + " vừa đặt mua voucher '" + tênVoucherChiTiết + "' (" + giáXuVoucher + " SS)! Mở Escrow Chat để giao dịch.||" + thờiGianThực;
        String tbGốc = bộLưuTrữVí.getString("DANH_SACH_THONG_BAO_" + sellerClean, "");

        // Đăng ký đơn hàng chờ xác nhận vào Kênh Người Bán của Seller
        String khoPendingGốc = bộLưuTrữVí.getString("DANH_SACH_ESCROW_PENDING_" + sellerClean, "");
        String bảnGhiPendingMới = mãBíMậtKíchHoạt + "::" + tênVoucherChiTiết + "::" + giáXuVoucher + " SS::" + buyerUser + "::" + buyerUser.substring(0, Math.min(2, buyerUser.length())).toUpperCase() + "::" + buyerUser;
        String khoPendingMới = bảnGhiPendingMới + (khoPendingGốc.isEmpty() ? "" : ";;" + khoPendingGốc);

        // Đánh dấu mã voucher đang trong tiến trình giao dịch Escrow (Ẩn khỏi Sàn C2C Market)
        String khoĐangGiaoDịchGốc = bộLưuTrữVí.getString("DANH_SACH_VOUCHER_DANG_GIAO_DICH_ESCROW", "");
        String khoĐangGiaoDịchMới = mãBíMậtKíchHoạt + ";;" + khoĐangGiaoDịchGốc;

        // Tạo tin nhắn chào mừng mặc định ban đầu trong phòng chat giao dịch Escrow
        String timeShort = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String tinHệThống = "sys||Hệ thống||SS||🛡️ Hệ thống: Người mua @" + buyerUser + " đã khóa " + giáXuVoucher + " SS vào ví Escrow. Người mua có 24 giờ để kiểm tra mã trước khi giải ngân.||" + timeShort;
        String tinChàoMua = buyerUser + "||" + buyerUser + "||NM||Xin chào @" + sellerClean + ", tôi vừa đặt cọc " + giáXuVoucher + " SS qua Escrow để mua voucher '" + tênVoucherChiTiết + "'. Bạn vui lòng gửi mã/hướng dẫn sử dụng nhé!||" + timeShort;
        String tinGốc = bộLưuTrữVí.getString("DANH_SACH_TIN_NHAN_" + idPhòngChat, "");
        String tinMới = tinHệThống + ";;" + tinChàoMua + (tinGốc.isEmpty() ? "" : ";;" + tinGốc);

        bộLưuTrữVí.edit()
                .putLong("ESCROW_START_TIME_" + idPhòngChat, System.currentTimeMillis())
                .putLong("ESCROW_START_TIME_" + mãBíMậtKíchHoạt, System.currentTimeMillis())
                .putBoolean("IS_LOCKED_ROOM_" + idPhòngChat, false) // BẬT MỞ PHÒNG CHAT 100% UNLOCKED!
                .putString("DANH_SACH_VOUCHER_DANG_GIAO_DICH_ESCROW", khoĐangGiaoDịchMới)
                .putString("DANH_SACH_TIN_NHAN_" + idPhòngChat, tinMới)
                .putString("DANH_SACH_ESCROW_PENDING_" + sellerClean, khoPendingMới)
                .putString("DANH_SACH_THONG_BAO_" + sellerClean, tbMới + (tbGốc.isEmpty() ? "" : ";;" + tbGốc))
                .putBoolean("HAS_UNREAD_NOTIFICATIONS_" + sellerClean, true)
                .apply();

        Intent ýĐịnhChat = new Intent(this, EscrowChatActivity.class);
        ýĐịnhChat.putExtra("EXTRA_SELLER_NAME", tênNgườiBán);
        ýĐịnhChat.putExtra("EXTRA_SELLER_AVATAR", avatarNgườiBán);
        ýĐịnhChat.putExtra("EXTRA_VOUCHER_TITLE", tênVoucherChiTiết);
        ýĐịnhChat.putExtra("EXTRA_SECRET_CODE", mãBíMậtKíchHoạt);
        ýĐịnhChat.putExtra("EXTRA_PRICE_SS", giáXuVoucher);
        startActivity(ýĐịnhChat);
        finish();
    }
}
