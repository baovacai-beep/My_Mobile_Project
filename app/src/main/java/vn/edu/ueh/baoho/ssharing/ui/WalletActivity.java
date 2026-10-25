package vn.edu.ueh.baoho.ssharing.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vn.edu.ueh.baoho.ssharing.R;

public class WalletActivity extends AppCompatActivity {

    private TextView nhãnSốDưXu;
    private TextView tabVíVoucher;
    private TextView tabLịchSửGiaoDịch;
    private View khungVíVoucher;
    private View khungLịchSử;
    private LinearLayout containerGiaoDịchĐộng;
    private LinearLayout containerVoucherĐãLưuĐộng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        khởiTạoÁnhXạGiaoDiện();
        càiĐặtSựKiệnLắngNghe();
        cậpNhậtSốDưVàDữLiệuVí();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cậpNhậtSốDưVàDữLiệuVí();
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        nhãnSốDưXu = findViewById(R.id.tvSoDuXuSS);
        tabVíVoucher = findViewById(R.id.tabViVoucher);
        tabLịchSửGiaoDịch = findViewById(R.id.tabLichSuGiaoDich);
        khungVíVoucher = findViewById(R.id.khungSectionVoucher);
        khungLịchSử = findViewById(R.id.khungSectionLichSu);
        containerGiaoDịchĐộng = findViewById(R.id.containerDanhSáchGiaoDịch);
        containerVoucherĐãLưuĐộng = findViewById(R.id.containerDanhSachVoucherDaLuu);
    }

    private void càiĐặtSựKiệnLắngNghe() {
        // Nút mở trang Profile
        View ảnhĐạiDiện = findViewById(R.id.imgAvatar);
        if (ảnhĐạiDiện != null) {
            ảnhĐạiDiện.setOnClickListener(vương ->
                    startActivity(new Intent(this, ProfileActivity.class))
            );
        }

        // Nút Nạp thêm xu -> Mở CoinTopupActivity
        View nútNạpThêm = findViewById(R.id.btnDeposit);
        if (nútNạpThêm != null) {
            nútNạpThêm.setOnClickListener(vương ->
                    startActivity(new Intent(this, CoinTopupActivity.class))
            );
        }

        // Chuyển Tab "Ví Voucher của tôi"
        if (tabVíVoucher != null) {
            tabVíVoucher.setOnClickListener(vương -> chuyểnSangTabVíVoucher());
        }

        // Chuyển Tab "Lịch sử giao dịch"
        if (tabLịchSửGiaoDịch != null) {
            tabLịchSửGiaoDịch.setOnClickListener(vương -> chuyểnSangTabLịchSử());
        }

        // Nút Sao chép mã Voucher Shopee mẫu
        Button nútSaoChépMã1 = findViewById(R.id.btnSaoChep1);
        if (nútSaoChépMã1 != null) {
            nútSaoChépMã1.setOnClickListener(vương -> thựcHiệnSaoChépMãVoucher("SP20K-HOANXU"));
        }

        // Nút Mở App GrabFood mẫu
        Button nútMởAppGrab = findViewById(R.id.btnMoApp2);
        if (nútMởAppGrab != null) {
            nútMởAppGrab.setOnClickListener(vương ->
                    Toast.makeText(this, "Đang mở ứng dụng GrabFood để áp dụng voucher...", Toast.LENGTH_SHORT).show()
            );
        }

        // Điều hướng Bottom Navigation
        càiĐặtThanhĐiềuHướngĐáy();
    }

    private void chuyểnSangTabVíVoucher() {
        if (tabVíVoucher != null && tabLịchSửGiaoDịch != null) {
            tabVíVoucher.setBackgroundResource(R.drawable.bg_tab_active);
            tabVíVoucher.setTextColor(0xFFFFFFFF);
            tabLịchSửGiaoDịch.setBackground(null);
            tabLịchSửGiaoDịch.setTextColor(0xFF6C677A);
        }
        if (khungVíVoucher != null) khungVíVoucher.setVisibility(View.VISIBLE);
        if (khungLịchSử != null) khungLịchSử.setVisibility(View.GONE);
    }

    private void chuyểnSangTabLịchSử() {
        if (tabVíVoucher != null && tabLịchSửGiaoDịch != null) {
            tabLịchSửGiaoDịch.setBackgroundResource(R.drawable.bg_tab_active);
            tabLịchSửGiaoDịch.setTextColor(0xFFFFFFFF);
            tabVíVoucher.setBackground(null);
            tabVíVoucher.setTextColor(0xFF6C677A);
        }
        if (khungLịchSử != null) khungLịchSử.setVisibility(View.VISIBLE);
        if (khungVíVoucher != null) khungVíVoucher.setVisibility(View.GONE);
    }

    private void thựcHiệnSaoChépMãVoucher(String mãVoucher) {
        ClipboardManager quảnLýKhayNhớTạm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData dữLiệuMã = ClipData.newPlainText("MãVoucher", mãVoucher);
        if (quảnLýKhayNhớTạm != null) {
            quảnLýKhayNhớTạm.setPrimaryClip(dữLiệuMã);
            Toast.makeText(this, "Đã sao chép mã voucher: " + mãVoucher, Toast.LENGTH_SHORT).show();
        }
    }

    private void cậpNhậtSốDưVàDữLiệuVí() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        int sốDưXuHiệnTại = bộLưuTrữVí.getInt("SO_DU_XU_SS", 1250);
        if (nhãnSốDưXu != null) {
            nhãnSốDưXu.setText(String.format("%,d", sốDưXuHiệnTại) + " SS");
        }

        cậpNhậtẢnhĐạiDiệnVí(bộLưuTrữVí);
        tảiLịchSửGiaoDịchĐộng(bộLưuTrữVí);
        tảiDanhSáchVoucherĐãLưu(bộLưuTrữVí);
    }

    private void cậpNhậtẢnhĐạiDiệnVí(SharedPreferences bộLưuTrữVí) {
        ImageView ảnhĐạiDiệnWallet = findViewById(R.id.imgAvatar);
        if (ảnhĐạiDiệnWallet == null) return;

        // Reset ảnh mặc định ban đầu để xóa bộ nhớ đệm ảnh cũ
        ảnhĐạiDiệnWallet.setImageResource(R.drawable.ic_user);

        String usernameUser = bộLưuTrữVí.getString("CURRENT_USERNAME", "baoho123").toLowerCase().trim();
        String keyAvatarCáNhân = "CURRENT_USER_AVATAR_PATH_" + usernameUser;
        String đườngDẫnẢnh = bộLưuTrữVí.getString(keyAvatarCáNhân, "");

        if (đườngDẫnẢnh != null && !đườngDẫnẢnh.isEmpty() && new java.io.File(đườngDẫnẢnh).exists()) {
            try {
                android.graphics.Bitmap bitmapAvatar = android.graphics.BitmapFactory.decodeFile(đườngDẫnẢnh);
                if (bitmapAvatar != null) {
                    ảnhĐạiDiệnWallet.setImageBitmap(bitmapAvatar);
                }
            } catch (Throwable ignored) {}
        }
    }

    private void tảiLịchSửGiaoDịchĐộng(SharedPreferences bộLưuTrữVí) {
        if (containerGiaoDịchĐộng == null) return;
        containerGiaoDịchĐộng.removeAllViews();

        String danhSáchChuỗiGiaoDịch = bộLưuTrữVí.getString("DANH_SACH_GIAO_DICH", "");
        if (danhSáchChuỗiGiaoDịch.isEmpty()) {
            // Nạp 2 bản ghi mặc định nếu chưa có lịch sử
            danhSáchChuỗiGiaoDịch = "TX-N892031||Nạp xu qua VietQR Napas247||+500 SS||19/10/2026 14:30:00||Nạp tiền VietQR||Thành công||Nạp 500.000đ nhận 500 SS xu" +
                    ";;TX-E761920||Mua Voucher Highlands Coffee||-35 SS||19/10/2026 12:15:00||Thanh toán Escrow||Thành công||Thanh toán mua voucher Trà Sen Vàng";
        }

        String[] cácBảnGhi = danhSáchChuỗiGiaoDịch.split(";;");
        for (String bảnGhi : cácBảnGhi) {
            if (bảnGhi.trim().isEmpty()) continue;
            String[] mảngTrường = bảnGhi.split("\\|\\|");
            if (mảngTrường.length < 3) continue;

            String mãGiaoDịch = mảngTrường[0];
            String tiêuĐề = mảngTrường[1];
            String sốXu = mảngTrường[2];
            String thờiGian = mảngTrường.length > 3 ? mảngTrường[3] : "19/10/2026";
            String loạiGiaoDịch = mảngTrường.length > 4 ? mảngTrường[4] : "Giao dịch ví";
            String trạngThái = mảngTrường.length > 5 ? mảngTrường[5] : "Thành công";
            String môTả = mảngTrường.length > 6 ? mảngTrường[6] : "Giao dịch nguyên tử qua hệ thống SSharing";

            boolean làCộngXu = sốXu.contains("+");

            // Tạo Card Giao Dịch
            RelativeLayout thẻGiaoDịch = new RelativeLayout(this);
            thẻGiaoDịch.setBackgroundResource(R.drawable.bg_card_dark);
            thẻGiaoDịch.setPadding(32, 32, 32, 32);
            thẻGiaoDịch.setClickable(true);
            thẻGiaoDịch.setFocusable(true);

            LinearLayout.LayoutParams thôngSốThẻ = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            thôngSốThẻ.bottomMargin = 24;
            thẻGiaoDịch.setLayoutParams(thôngSốThẻ);

            // Icon Mũi Tên (+ xanh, - hồng)
            ImageView iconMũiTên = new ImageView(this);
            iconMũiTên.setId(View.generateViewId());
            iconMũiTên.setImageResource(làCộngXu ? R.drawable.ic_arrow_down_left : R.drawable.ic_arrow_up_right);
            iconMũiTên.setColorFilter(Color.parseColor(làCộngXu ? "#43E5A0" : "#FF4081"));
            RelativeLayout.LayoutParams thôngSốIcon = new RelativeLayout.LayoutParams(60, 60);
            thôngSốIcon.addRule(RelativeLayout.CENTER_VERTICAL);
            iconMũiTên.setLayoutParams(thôngSốIcon);

            // Số Xu
            TextView nhãnXu = new TextView(this);
            nhãnXu.setId(View.generateViewId());
            nhãnXu.setText(sốXu);
            nhãnXu.setTextColor(Color.parseColor(làCộngXu ? "#43E5A0" : "#FF4081"));
            nhãnXu.setTextSize(14);
            nhãnXu.setTypeface(null, android.graphics.Typeface.BOLD);
            RelativeLayout.LayoutParams thôngSốXu = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            thôngSốXu.addRule(RelativeLayout.ALIGN_PARENT_END);
            thôngSốXu.addRule(RelativeLayout.CENTER_VERTICAL);
            nhãnXu.setLayoutParams(thôngSốXu);

            // Khung Tiêu Đề & Thời Gian
            LinearLayout khungMôTảGiaoDịch = new LinearLayout(this);
            khungMôTảGiaoDịch.setOrientation(LinearLayout.VERTICAL);
            RelativeLayout.LayoutParams thôngSốMôTả = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            thôngSốMôTả.addRule(RelativeLayout.CENTER_VERTICAL);
            thôngSốMôTả.addRule(RelativeLayout.RIGHT_OF, iconMũiTên.getId());
            thôngSốMôTả.addRule(RelativeLayout.LEFT_OF, nhãnXu.getId());
            thôngSốMôTả.leftMargin = 24;
            thôngSốMôTả.rightMargin = 16;
            khungMôTảGiaoDịch.setLayoutParams(thôngSốMôTả);

            TextView nhãnTiêuĐề = new TextView(this);
            nhãnTiêuĐề.setText(tiêuĐề);
            nhãnTiêuĐề.setTextColor(Color.WHITE);
            nhãnTiêuĐề.setTextSize(14);
            nhãnTiêuĐề.setSingleLine(true);
            nhãnTiêuĐề.setEllipsize(android.text.TextUtils.TruncateAt.END);

            TextView nhãnMốcThờiGian = new TextView(this);
            nhãnMốcThờiGian.setText(thờiGian);
            nhãnMốcThờiGian.setTextColor(Color.parseColor("#858095"));
            nhãnMốcThờiGian.setTextSize(11);

            khungMôTảGiaoDịch.addView(nhãnTiêuĐề);
            khungMôTảGiaoDịch.addView(nhãnMốcThờiGian);

            thẻGiaoDịch.addView(iconMũiTên);
            thẻGiaoDịch.addView(khungMôTảGiaoDịch);
            thẻGiaoDịch.addView(nhãnXu);

            // Sự kiện bấm vào dòng Lịch sử -> Mở Chi Tiết Giao Dịch
            thẻGiaoDịch.setOnClickListener(vương ->
                    hiểnThịChiTiếtGiaoDịch(mãGiaoDịch, tiêuĐề, sốXu, thờiGian, loạiGiaoDịch, trạngThái, môTả)
            );

            containerGiaoDịchĐộng.addView(thẻGiaoDịch);
        }
    }

    private void tảiDanhSáchVoucherĐãLưu(SharedPreferences bộLưuTrữVí) {
        if (containerVoucherĐãLưuĐộng == null) return;
        containerVoucherĐãLưuĐộng.removeAllViews();

        String danhSáchChuỗiVoucher = bộLưuTrữVí.getString("DANH_SACH_VOUCHER_DA_LUU", "");
        if (danhSáchChuỗiVoucher.isEmpty()) {
            danhSáchChuỗiVoucher = "HLM1T1-USER33::Voucher Highlands Mua 1 Tặng 1::HSD: 31 T12 2026::FOOD::BARCODE;;" +
                    "SPFOOD50K-USER22::Voucher ShopeeFood Giảm 50K::HSD: 31 T12 2026::SHOPEE::QR_IMAGE;;" +
                    "SBUX20K-OCTOBER99::Voucher Starbucks Giảm 20K::HSD: 31 T12 2026::BRAND::BARCODE;;" +
                    "GRAB50K-COMMUNITY88::Voucher GrabCar Giảm 50K::HSD: 31 T12 2026::GRAB::CODE_TEXT;;" +
                    "MOMO30K-OCTOBER77::Voucher MoMo Hoàn 30K Nạp ĐT::HSD: 31 T12 2026::SHOP::QR_IMAGE";
        }

        String[] cácVoucher = danhSáchChuỗiVoucher.split(";;");
        for (String chuỗiVoucher : cácVoucher) {
            if (chuỗiVoucher.trim().isEmpty()) continue;
            String[] mảngVoucher = chuỗiVoucher.split("::");
            if (mảngVoucher.length < 2) continue;

            String mãBíMật = mảngVoucher[0];
            String tênVoucher = mảngVoucher[1];
            String hạnDùng = mảngVoucher.length > 2 ? mảngVoucher[2] : "HSD: 31 T12 2026";
            String loạiIcon = mảngVoucher.length > 3 ? mảngVoucher[3] : "FLASH";
            String địnhDạngMã = mảngVoucher.length > 4 ? mảngVoucher[4] : "CODE_TEXT";

            // Tạo Card Voucher
            CardView thẻVoucher = new CardView(this);
            thẻVoucher.setCardBackgroundColor(Color.parseColor("#161327"));
            thẻVoucher.setRadius(32);
            thẻVoucher.setCardElevation(0);

            LinearLayout.LayoutParams thôngSốThẻ = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            thôngSốThẻ.bottomMargin = 24;
            thẻVoucher.setLayoutParams(thôngSốThẻ);

            RelativeLayout khungNộiDung = new RelativeLayout(this);
            khungNộiDung.setPadding(32, 32, 32, 32);

            // Icon Thương Hiệu
            ImageView iconMặtHàng = new ImageView(this);
            iconMặtHàng.setId(View.generateViewId());
            iconMặtHàng.setBackgroundResource(R.drawable.bg_rounded_icon);
            iconMặtHàng.setPadding(24, 24, 24, 24);
            if ("GRAB".equalsIgnoreCase(loạiIcon)) {
                iconMặtHàng.setImageResource(R.drawable.ic_wallet);
                iconMặtHàng.setColorFilter(Color.parseColor("#FF4081"));
            } else if ("SHOPEE".equalsIgnoreCase(loạiIcon)) {
                iconMặtHàng.setImageResource(R.drawable.ic_market);
                iconMặtHàng.setColorFilter(Color.parseColor("#815BF5"));
            } else {
                iconMặtHàng.setImageResource(R.drawable.ic_coin);
                iconMặtHàng.setColorFilter(Color.parseColor("#FFD54F"));
            }
            RelativeLayout.LayoutParams thôngSốIcon = new RelativeLayout.LayoutParams(120, 120);
            thôngSốIcon.addRule(RelativeLayout.CENTER_VERTICAL);
            iconMặtHàng.setLayoutParams(thôngSốIcon);

            // Nút Thao Tác Theo Định Dạng (Mã Vạch, QR Code, Sao chép)
            Button nútThaoTácMã = new Button(this);
            nútThaoTácMã.setId(View.generateViewId());
            nútThaoTácMã.setTextSize(11);
            nútThaoTácMã.setTextColor(Color.parseColor("#E2DEEC"));
            nútThaoTácMã.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#26213D")));
            RelativeLayout.LayoutParams thôngSốNút = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    110
            );
            thôngSốNút.addRule(RelativeLayout.ALIGN_PARENT_END);
            thôngSốNút.addRule(RelativeLayout.CENTER_VERTICAL);
            nútThaoTácMã.setLayoutParams(thôngSốNút);

            String nhãnLoạiMã;
            if ("BARCODE".equalsIgnoreCase(địnhDạngMã)) {
                nútThaoTácMã.setText("||| Mã vạch");
                nútThaoTácMã.setOnClickListener(v -> hiểnThịDialogMãVạch(tênVoucher, mãBíMật));
                nhãnLoạiMã = "||| MÃ VẠCH CỬA HÀNG - " + hạnDùng;
            } else if ("QR_IMAGE".equalsIgnoreCase(địnhDạngMã)) {
                nútThaoTácMã.setText("🖼 Mã QR");
                nútThaoTácMã.setOnClickListener(v -> hiểnThịDialogMãQR(tênVoucher, mãBíMật));
                nhãnLoạiMã = "🖼 MÃ QR ANH CHỤP - " + hạnDùng;
            } else {
                nútThaoTácMã.setText("📋 Sao chép mã");
                nútThaoTácMã.setOnClickListener(v -> thựcHiệnSaoChépMãVoucher(mãBíMật));
                nhãnLoạiMã = "📋 MÃ CHUỖI SAO CHÉP - " + hạnDùng;
            }

            // Khung Tiêu Đề & Hạn Dùng
            LinearLayout khungChiTiết = new LinearLayout(this);
            khungChiTiết.setOrientation(LinearLayout.VERTICAL);
            RelativeLayout.LayoutParams thôngSốChiTiết = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            thôngSốChiTiết.addRule(RelativeLayout.CENTER_VERTICAL);
            thôngSốChiTiết.addRule(RelativeLayout.RIGHT_OF, iconMặtHàng.getId());
            thôngSốChiTiết.addRule(RelativeLayout.LEFT_OF, nútThaoTácMã.getId());
            thôngSốChiTiết.leftMargin = 24;
            thôngSốChiTiết.rightMargin = 16;
            khungChiTiết.setLayoutParams(thôngSốChiTiết);

            TextView nhãnTên = new TextView(this);
            nhãnTên.setText(tênVoucher);
            nhãnTên.setTextColor(Color.WHITE);
            nhãnTên.setTextSize(14);
            nhãnTên.setTypeface(null, android.graphics.Typeface.BOLD);
            nhãnTên.setSingleLine(true);
            nhãnTên.setEllipsize(android.text.TextUtils.TruncateAt.END);

            TextView nhãnBadge = new TextView(this);
            nhãnBadge.setText(nhãnLoạiMã);
            nhãnBadge.setTextColor(Color.parseColor("#FFD54F"));
            nhãnBadge.setTextSize(10);

            khungChiTiết.addView(nhãnTên);
            khungChiTiết.addView(nhãnBadge);

            khungNộiDung.addView(iconMặtHàng);
            khungNộiDung.addView(khungChiTiết);
            khungNộiDung.addView(nútThaoTácMã);

            thẻVoucher.addView(khungNộiDung);
            containerVoucherĐãLưuĐộng.addView(thẻVoucher);
        }
    }

    private void hiểnThịDialogMãVạch(String tênVoucher, String mãCode) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(30, 30, 30, 30);

        TextView tvTitle = new TextView(this);
        tvTitle.setText("||| MÃ VẠCH (BARCODE) QUÉT TẠI CỬA HÀNG");
        tvTitle.setTextColor(Color.parseColor("#43E5A0"));
        tvTitle.setTextSize(13);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitle.setGravity(Gravity.CENTER);

        TextView tvBarcodeBars = new TextView(this);
        tvBarcodeBars.setText("|| ||| |||| | ||| |||| || ||| |");
        tvBarcodeBars.setTextColor(Color.WHITE);
        tvBarcodeBars.setTextSize(36);
        tvBarcodeBars.setTypeface(null, android.graphics.Typeface.BOLD);
        tvBarcodeBars.setGravity(Gravity.CENTER);
        tvBarcodeBars.setPadding(0, 20, 0, 10);

        TextView tvCodeString = new TextView(this);
        tvCodeString.setText("Mã Voucher String: " + mãCode);
        tvCodeString.setTextColor(Color.parseColor("#FFD54F"));
        tvCodeString.setTextSize(15);
        tvCodeString.setTypeface(null, android.graphics.Typeface.BOLD);
        tvCodeString.setGravity(Gravity.CENTER);

        TextView tvSub = new TextView(this);
        tvSub.setText("Đưa màn hình mã vạch này cho nhân viên thu ngân quét áp dụng voucher " + tênVoucher);
        tvSub.setTextColor(Color.parseColor("#A09CAB"));
        tvSub.setTextSize(11);
        tvSub.setGravity(Gravity.CENTER);
        tvSub.setPadding(0, 10, 0, 0);

        layout.addView(tvTitle);
        layout.addView(tvBarcodeBars);
        layout.addView(tvCodeString);
        layout.addView(tvSub);

        new AlertDialog.Builder(this)
                .setTitle("🎟 " + tênVoucher)
                .setView(layout)
                .setPositiveButton("📋 Sao Chép Mã String", (dialog, which) -> thựcHiệnSaoChépMãVoucher(mãCode))
                .setNegativeButton("Đóng", null)
                .show();
    }

    private void hiểnThịDialogMãQR(String tênVoucher, String mãCode) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(30, 30, 30, 30);

        TextView tvTitle = new TextView(this);
        tvTitle.setText("🖼 MÃ QR CODE 2D BẰNG CHỨNG THẬT");
        tvTitle.setTextColor(Color.parseColor("#815BF5"));
        tvTitle.setTextSize(13);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitle.setGravity(Gravity.CENTER);

        ImageView imgQR = new ImageView(this);
        imgQR.setImageResource(R.drawable.ic_document_file);
        imgQR.setColorFilter(Color.parseColor("#815BF5"));
        LinearLayout.LayoutParams paramsQR = new LinearLayout.LayoutParams(200, 200);
        paramsQR.topMargin = 20;
        paramsQR.bottomMargin = 20;
        imgQR.setLayoutParams(paramsQR);

        TextView tvCodeString = new TextView(this);
        tvCodeString.setText("Mã Bí Mật String: " + mãCode);
        tvCodeString.setTextColor(Color.parseColor("#6EE7B7"));
        tvCodeString.setTextSize(15);
        tvCodeString.setTypeface(null, android.graphics.Typeface.BOLD);
        tvCodeString.setGravity(Gravity.CENTER);

        layout.addView(tvTitle);
        layout.addView(imgQR);
        layout.addView(tvCodeString);

        new AlertDialog.Builder(this)
                .setTitle("🎟 " + tênVoucher)
                .setView(layout)
                .setPositiveButton("📋 Sao Chép Mã String", (dialog, which) -> thựcHiệnSaoChépMãVoucher(mãCode))
                .setNegativeButton("Đóng", null)
                .show();
    }

    private void hiểnThịChiTiếtGiaoDịch(String mãGiaoDịch, String tiêuĐề, String sốXuThayĐổi, String thờiGian, String loạiGiaoDịch, String trạngThái, String môTả) {
        String nộiDungChiTiết = "📌 Mã giao dịch: " + mãGiaoDịch + "\n\n" +
                "📝 Nội dung: " + tiêuĐề + "\n" +
                "🪙 Số xu thay đổi: " + sốXuThayĐổi + "\n" +
                "⏱ Thời gian: " + thờiGian + "\n" +
                "📂 Loại giao dịch: " + loạiGiaoDịch + "\n" +
                "✅ Trạng thái: " + trạngThái + "\n\n" +
                "ℹ️ Mô tả chi tiết: " + môTả;

        new AlertDialog.Builder(this)
                .setTitle("Chi Tiết Giao Dịch")
                .setMessage(nộiDungChiTiết)
                .setPositiveButton("Đóng", null)
                .show();
    }

    private void càiĐặtThanhĐiềuHướngĐáy() {
        View nútTrangChủ = findViewById(R.id.btnNavHome);
        View nútĐăngBán = findViewById(R.id.btnNavAdd);
        View nútTrangCáNhân = findViewById(R.id.btnNavProfile);

        if (nútTrangChủ != null) {
            nútTrangChủ.setOnClickListener(vương ->
                    startActivity(new Intent(this, MainActivity.class))
            );
        }
        if (nútĐăngBán != null) {
            nútĐăngBán.setOnClickListener(vương ->
                    startActivity(new Intent(this, PostVoucherActivity.class))
            );
        }
        if (nútTrangCáNhân != null) {
            nútTrangCáNhân.setOnClickListener(vương ->
                    startActivity(new Intent(this, ProfileActivity.class))
            );
        }
    }
}
