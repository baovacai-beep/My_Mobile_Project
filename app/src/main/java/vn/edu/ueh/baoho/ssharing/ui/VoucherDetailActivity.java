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

        if (nhãnTiêuĐềChiTiết != null) nhãnTiêuĐềChiTiết.setText(tênVoucherChiTiết);
        if (nhãnTênThươngHiệu != null) {
            nhãnTênThươngHiệu.setText(làGianHàngChínhHãng ? "Thương hiệu: " + tênThươngHiệu : "Người bán C2C: " + tênNgườiBán);
        }
        if (nhãnCấpĐộTínNhiệm != null) {
            nhãnCấpĐộTínNhiệm.setText(làGianHàngChínhHãng ? "✓ Đối Tác Chính Hãng (Trust Score: 100%)" : "👥 Thành viên cộng đồng (Trust Score: 98%)");
        }
        if (nhãnMãCheMờ != null) nhãnMãCheMờ.setText("Mã bảo mật: " + cheMờMãBíMật(mãBíMậtKíchHoạt));
        if (nútMuaVoucher != null) nútMuaVoucher.setText("Mua Ngay " + String.format("%,d", giáXuVoucher) + " SS");
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
        int sốDưXuHiệnTại = bộLưuTrữVí.getInt("SO_DU_XU_SS", 1250);

        if (sốDưXuHiệnTại < giáXuVoucher) {
            Toast.makeText(this, "Số dư Xu không đủ! Bạn cần " + giáXuVoucher + " SS.", Toast.LENGTH_LONG).show();
            return;
        }

        if (làGianHàngChínhHãng) {
            // MUA VOUCHER CHÍNH HÃNG MALL -> GIAO TỨC THÌ VÀO VÍ VOUCHER
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận đổi Voucher Mall")
                    .setMessage("Bạn có chắc chắn dùng " + giáXuVoucher + " SS để đổi mã voucher chính hãng '" + tênVoucherChiTiết + "'?")
                    .setPositiveButton("Đổi ngay", (hộpThoại, vịTrí) -> thựcHiệnMuaChínhHãng(bộLưuTrữVí, sốDưXuHiệnTại))
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

    private void thựcHiệnMuaChínhHãng(SharedPreferences bộLưuTrữVí, int sốDưHiệnTại) {
        int sốDưMới = sốDưHiệnTại - giáXuVoucher;
        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        // 1. Thêm vào Ví Voucher
        String danhSáchVoucherĐãLưu = bộLưuTrữVí.getString("DANH_SACH_VOUCHER_DA_LUU", "");
        String voucherMớiThêm = mãBíMậtKíchHoạt + "::" + tênVoucherChiTiết + "::HSD: 31 T12 2026::BRAND";
        String danhSáchVoucherMới = voucherMớiThêm + (danhSáchVoucherĐãLưu.isEmpty() ? "" : ";;" + danhSáchVoucherĐãLưu);

        // 2. Thêm vào Lịch Sử Giao Dịch
        String danhSáchGiaoDịchĐãLưu = bộLưuTrữVí.getString("DANH_SACH_GIAO_DICH", "");
        String mãGiaoDịchRandom = "TX-MALL" + (100000 + (int)(Math.random() * 899999));
        String bảnGhiGiaoDịchMới = mãGiaoDịchRandom + "||Đổi Voucher Mall " + tênThươngHiệu + "||-" + giáXuVoucher + " SS||" + thờiGianThực + "||SSharing Mall||Thành công||Thanh toán " + giáXuVoucher + " SS đổi mã " + mãBíMậtKíchHoạt;
        String danhSáchGiaoDịchMới = bảnGhiGiaoDịchMới + (danhSáchGiaoDịchĐãLưu.isEmpty() ? "" : ";;" + danhSáchGiaoDịchĐãLưu);

        bộLưuTrữVí.edit()
                .putInt("SO_DU_XU_SS", sốDưMới)
                .putString("DANH_SACH_VOUCHER_DA_LUU", danhSáchVoucherMới)
                .putString("DANH_SACH_GIAO_DICH", danhSáchGiaoDịchMới)
                .apply();

        Toast.makeText(this, "🎉 Mua thành công " + tênVoucherChiTiết + "! Mã đã lưu vào Ví của bạn.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, WalletActivity.class));
        finish();
    }

    private void mởPhòngChatEscrow() {
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
