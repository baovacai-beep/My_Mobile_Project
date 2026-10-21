package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.ueh.baoho.ssharing.R;

public class SearchActivity extends AppCompatActivity {

    private EditText ôNhậpTừKhóaTìmKiếm;
    private LinearLayout containerKếtQuảMànHìnhMới;
    private TextView chipTấtCả, chipMall, chipC2C, chipẨmThực, chipMuaSắm, chipDiChuyển, chipGiảiTrí;

    private String bộLọcĐangChọn = "ALL";
    private String từKhóaĐangNhập = "";

    private static class ThôngTinVoucher {
        String tênBadge;
        String tiêuĐề;
        int giáXu;
        String giáGốc;
        String nhãnBadge;
        boolean làChínhHãng;
        String thươngHiệu;
        String tênNgườiBán;
        String avatarNgườiBán;
        String mãBíMật;
        String loạiDanhMục; // FOOD, SHOP, TRANSPORT, MOVIE

        ThôngTinVoucher(String tênBadge, String tiêuĐề, int giáXu, String giáGốc, String nhãnBadge, boolean làChínhHãng, String thươngHiệu, String tênNgườiBán, String avatarNgườiBán, String mãBíMật, String loạiDanhMục) {
            this.tênBadge = tênBadge;
            this.tiêuĐề = tiêuĐề;
            this.giáXu = giáXu;
            this.giáGốc = giáGốc;
            this.nhãnBadge = nhãnBadge;
            this.làChínhHãng = làChínhHãng;
            this.thươngHiệu = thươngHiệu;
            this.tênNgườiBán = tênNgườiBán;
            this.avatarNgườiBán = avatarNgườiBán;
            this.mãBíMật = mãBíMật;
            this.loạiDanhMục = loạiDanhMục;
        }
    }

    private List<ThôngTinVoucher> khoTấtCảVoucher = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        khởiTạoKhoVoucherMẫu();
        khởiTạoÁnhXạGiaoDiện();
        đọcDữLiệuĐầuVào();
        càiĐặtSựKiệnLắngNghe();
        cậpNhậtDanhSáchKếtQuảTìmKiếm();
    }

    private void khởiTạoKhoVoucherMẫu() {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String danhSáchC2CChung = bộLưuTrữCụcBộ.getString("DANH_SACH_VOUCHER_C2C_CHUNG", "");
        String usernameHiệnTại = bộLưuTrữCụcBộ.getString("CURRENT_USERNAME", "baoho123");
        String họTênHiệnTại = bộLưuTrữCụcBộ.getString("CURRENT_USER_NAME", "Gia Bảo Hồ");

        if (!danhSáchC2CChung.isEmpty()) {
            String[] cácBàiC2C = danhSáchC2CChung.split(";;");
            for (String bài : cácBàiC2C) {
                if (bài.trim().isEmpty()) continue;
                String[] mảngTrường = bài.split("\\|\\|");
                if (mảngTrường.length < 5) continue;

                String môTả = mảngTrường[1];
                String thươngHiệu = mảngTrường[2];
                String mãVoucher = mảngTrường[3];
                int giáXu = 180;
                try {
                    giáXu = Integer.parseInt(mảngTrường[4]);
                } catch (Exception ignored) {}

                String tênNgườiBán = mảngTrường.length > 7 ? mảngTrường[7] : "Người dùng SSharing";
                String avatarInitials = mảngTrường.length > 8 ? mảngTrường[8] : "SS";
                String usernameNgườiBán = mảngTrường.length > 9 ? mảngTrường[9] : "";

                // Bỏ qua sản phẩm do chính tài khoản này đăng bán
                if (usernameHiệnTại.equalsIgnoreCase(usernameNgườiBán) ||
                        tênNgườiBán.toLowerCase().contains(usernameHiệnTại.toLowerCase()) ||
                        tênNgườiBán.equalsIgnoreCase(họTênHiệnTại)) {
                    continue;
                }

                khoTấtCảVoucher.add(new ThôngTinVoucher(
                        thươngHiệu.toUpperCase(), môTả, giáXu, "C2C Market", "👥 Cộng đồng", false, thươngHiệu, tênNgườiBán, avatarInitials, mãVoucher, "C2C"
                ));
            }
        }

        khoTấtCảVoucher.add(new ThôngTinVoucher("STARBUCKS", "Voucher Starbucks Giảm 20K Đơn 50K", 160, "200 SS", "✓ Chính hãng", true, "Starbucks Coffee", "Starbucks Official Store", "SB", "SBUX20K-OCTOBER99", "FOOD"));
        khoTấtCảVoucher.add(new ThôngTinVoucher("MOMO", "Voucher MoMo Hoàn 30K Nạp ĐT / Data 4G", 250, "300 SS", "✓ Chính hãng", true, "MoMo E-Wallet", "MoMo Official Store", "MM", "MOMO30K-OCTOBER77", "SHOP"));
        khoTấtCảVoucher.add(new ThôngTinVoucher("LAZADA", "Voucher Lazada 50K Đơn Từ 150K", 420, "500 SS", "✓ Chính hãng", true, "Lazada Mall", "Lazada Official Store", "LZ", "LAZ50K-OFFICIAL66", "SHOP"));
        khoTấtCảVoucher.add(new ThôngTinVoucher("PHÚC LONG", "Voucher Phúc Long 30K Trà Sữa", 200, "250 SS", "✓ Chính hãng", true, "Phúc Long Tea", "Phúc Long Official Store", "PL", "PL30K-OFFICIAL55", "FOOD"));
        khoTấtCảVoucher.add(new ThôngTinVoucher("CGV", "Vé Xem Phim CGV 2D Vé Đôi 100K", 280, "350 SS", "✓ Chính hãng", true, "CGV Cinemas", "CGV Official Store", "CGV", "CGV2D-OFFICIAL11", "MOVIE"));
        khoTấtCảVoucher.add(new ThôngTinVoucher("GRAB", "Giảm 50K chuyến xe GrabCar tiếp theo", 350, "500 SS", "👥 Cộng đồng", false, "Grab Vietnam", "Minh Anh (@minh.anh)", "MA", "GRAB50K-COMMUNITY88", "TRANSPORT"));
        khoTấtCảVoucher.add(new ThôngTinVoucher("SHOPEE", "Voucher ShopeeFood Giảm 50K Đơn 100K", 500, "650 SS", "👥 Cộng đồng", false, "ShopeeFood", "Bảo Ngọc (@bao.ngoc)", "BN", "SPFOOD50K-USER22", "FOOD"));
        khoTấtCảVoucher.add(new ThôngTinVoucher("HIGHLANDS", "Voucher Highlands Mua 1 Tặng 1 Trà Sen Vàng", 180, "250 SS", "👥 Cộng đồng", false, "Highlands Coffee", "Khánh Trình (@khanh.trinh)", "KT", "HLM1T1-USER33", "FOOD"));
        khoTấtCảVoucher.add(new ThôngTinVoucher("BE", "Voucher BeCar Giảm 30K Đơn Từ 60K", 220, "300 SS", "👥 Cộng đồng", false, "Be Group", "Gia Bảo (@gia.bao)", "GB", "BECAR30K-USER44", "TRANSPORT"));
        khoTấtCảVoucher.add(new ThôngTinVoucher("TIKI", "Voucher TikiTrading 100K Đơn 300K", 600, "800 SS", "👥 Cộng đồng", false, "TikiTrading", "Hoàng Long (@hoang.long)", "HL", "TIKI100K-USER55", "SHOP"));
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        ôNhậpTừKhóaTìmKiếm = findViewById(R.id.etFullSearchQuery);
        containerKếtQuảMànHìnhMới = findViewById(R.id.containerFullSearchResults);
        chipTấtCả = findViewById(R.id.chipAll);
        chipMall = findViewById(R.id.chipMall);
        chipC2C = findViewById(R.id.chipC2C);
        chipẨmThực = findViewById(R.id.chipFood);
        chipMuaSắm = findViewById(R.id.chipShop);
        chipDiChuyển = findViewById(R.id.chipTransport);
        chipGiảiTrí = findViewById(R.id.chipMovie);
    }

    private void đọcDữLiệuĐầuVào() {
        Intent ýĐịnhNhận = getIntent();
        if (ýĐịnhNhận != null) {
            if (ýĐịnhNhận.hasExtra("EXTRA_SEARCH_QUERY")) {
                từKhóaĐangNhập = ýĐịnhNhận.getStringExtra("EXTRA_SEARCH_QUERY");
                if (ôNhậpTừKhóaTìmKiếm != null && từKhóaĐangNhập != null) {
                    ôNhậpTừKhóaTìmKiếm.setText(từKhóaĐangNhập);
                }
            }
            if (ýĐịnhNhận.hasExtra("EXTRA_FILTER_CATEGORY")) {
                bộLọcĐangChọn = ýĐịnhNhận.getStringExtra("EXTRA_FILTER_CATEGORY");
            }
        }
    }

    private void càiĐặtSựKiệnLắngNghe() {
        View nútQuayLại = findViewById(R.id.btnBackSearch);
        if (nútQuayLại != null) nútQuayLại.setOnClickListener(v -> finish());

        if (ôNhậpTừKhóaTìmKiếm != null) {
            ôNhậpTừKhóaTìmKiếm.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    từKhóaĐangNhập = s.toString().trim().toLowerCase();
                    cậpNhậtDanhSáchKếtQuảTìmKiếm();
                }
                @Override public void afterTextChanged(Editable s) {}
            });
        }

        if (chipTấtCả != null) chipTấtCả.setOnClickListener(v -> chọnChipLọc("ALL", chipTấtCả));
        if (chipMall != null) chipMall.setOnClickListener(v -> chọnChipLọc("MALL", chipMall));
        if (chipC2C != null) chipC2C.setOnClickListener(v -> chọnChipLọc("C2C", chipC2C));
        if (chipẨmThực != null) chipẨmThực.setOnClickListener(v -> chọnChipLọc("FOOD", chipẨmThực));
        if (chipMuaSắm != null) chipMuaSắm.setOnClickListener(v -> chọnChipLọc("SHOP", chipMuaSắm));
        if (chipDiChuyển != null) chipDiChuyển.setOnClickListener(v -> chọnChipLọc("TRANSPORT", chipDiChuyển));
        if (chipGiảiTrí != null) chipGiảiTrí.setOnClickListener(v -> chọnChipLọc("MOVIE", chipGiảiTrí));
    }

    private void chọnChipLọc(String loạiLọc, TextView chipĐượcChọn) {
        bộLọcĐangChọn = loạiLọc;
        đặtLạiStyleMàuChip();
        if (chipĐượcChọn != null) {
            chipĐượcChọn.setBackgroundResource(R.drawable.bg_tab_active);
            chipĐượcChọn.setTextColor(Color.WHITE);
        }
        cậpNhậtDanhSáchKếtQuảTìmKiếm();
    }

    private void đặtLạiStyleMàuChip() {
        int màuXám = Color.parseColor("#A09CAB");
        if (chipTấtCả != null) { chipTấtCả.setBackgroundResource(R.drawable.bg_card_dark); chipTấtCả.setTextColor(màuXám); }
        if (chipMall != null) { chipMall.setBackgroundResource(R.drawable.bg_card_dark); chipMall.setTextColor(màuXám); }
        if (chipC2C != null) { chipC2C.setBackgroundResource(R.drawable.bg_card_dark); chipC2C.setTextColor(màuXám); }
        if (chipẨmThực != null) { chipẨmThực.setBackgroundResource(R.drawable.bg_card_dark); chipẨmThực.setTextColor(màuXám); }
        if (chipMuaSắm != null) { chipMuaSắm.setBackgroundResource(R.drawable.bg_card_dark); chipMuaSắm.setTextColor(màuXám); }
        if (chipDiChuyển != null) { chipDiChuyển.setBackgroundResource(R.drawable.bg_card_dark); chipDiChuyển.setTextColor(màuXám); }
        if (chipGiảiTrí != null) { chipGiảiTrí.setBackgroundResource(R.drawable.bg_card_dark); chipGiảiTrí.setTextColor(màuXám); }
    }

    private void cậpNhậtDanhSáchKếtQuảTìmKiếm() {
        if (containerKếtQuảMànHìnhMới == null) return;
        containerKếtQuảMànHìnhMới.removeAllViews();

        int sốMónTìmThấy = 0;

        for (ThôngTinVoucher món : khoTấtCảVoucher) {
            // Lọc theo Chip
            boolean khớpFilter = true;
            if ("MALL".equals(bộLọcĐangChọn) && !món.làChínhHãng) khớpFilter = false;
            if ("C2C".equals(bộLọcĐangChọn) && món.làChínhHãng) khớpFilter = false;
            if ("FOOD".equals(bộLọcĐangChọn) && !"FOOD".equals(món.loạiDanhMục)) khớpFilter = false;
            if ("SHOP".equals(bộLọcĐangChọn) && !"SHOP".equals(món.loạiDanhMục)) khớpFilter = false;
            if ("TRANSPORT".equals(bộLọcĐangChọn) && !"TRANSPORT".equals(món.loạiDanhMục)) khớpFilter = false;
            if ("MOVIE".equals(bộLọcĐangChọn) && !"MOVIE".equals(món.loạiDanhMục)) khớpFilter = false;

            // Lọc theo từ khóa gõ
            boolean khớpTừKhóa = true;
            if (!từKhóaĐangNhập.isEmpty()) {
                String tiêuĐềThường = món.tiêuĐề.toLowerCase();
                String thươngHiệuThường = món.thươngHiệu.toLowerCase();
                if (!tiêuĐềThường.contains(từKhóaĐangNhập) && !thươngHiệuThường.contains(từKhóaĐangNhập)) {
                    khớpTừKhóa = false;
                }
            }

            if (khớpFilter && khớpTừKhóa) {
                sốMónTìmThấy++;
                thêmCardVoucherVàoKếtQuả(món);
            }
        }

        if (sốMónTìmThấy == 0) {
            TextView nhãnTrống = new TextView(this);
            nhãnTrống.setText("❌ Không tìm thấy voucher nào khớp với từ khóa/bộ lọc.");
            nhãnTrống.setTextColor(Color.parseColor("#A09CAB"));
            nhãnTrống.setTextSize(14);
            nhãnTrống.setPadding(0, 40, 0, 0);
            nhãnTrống.setGravity(Gravity.CENTER);
            containerKếtQuảMànHìnhMới.addView(nhãnTrống);
        }
    }

    private void thêmCardVoucherVàoKếtQuả(ThôngTinVoucher món) {
        CardView thẻCard = new CardView(this);
        thẻCard.setCardBackgroundColor(Color.parseColor("#16141E"));
        thẻCard.setRadius(32);
        thẻCard.setCardElevation(0);

        LinearLayout.LayoutParams thôngSốThẻ = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốThẻ.bottomMargin = 24;
        thẻCard.setLayoutParams(thôngSốThẻ);

        LinearLayout khungToànBộ = new LinearLayout(this);
        khungToànBộ.setOrientation(LinearLayout.HORIZONTAL);
        khungToànBộ.setPadding(28, 28, 28, 28);

        TextView badgeThươngHiệu = new TextView(this);
        badgeThươngHiệu.setText(món.tênBadge);
        badgeThươngHiệu.setTextColor(Color.WHITE);
        badgeThươngHiệu.setTextSize(11);
        badgeThươngHiệu.setTypeface(null, android.graphics.Typeface.BOLD);
        badgeThươngHiệu.setGravity(Gravity.CENTER);
        badgeThươngHiệu.setBackgroundResource(R.drawable.bg_badge_pink);
        LinearLayout.LayoutParams thôngSốBadge = new LinearLayout.LayoutParams(140, 140);
        badgeThươngHiệu.setLayoutParams(thôngSốBadge);

        LinearLayout khungThôngTin = new LinearLayout(this);
        khungThôngTin.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams thôngSốMôTả = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốMôTả.leftMargin = 24;
        khungThôngTin.setLayoutParams(thôngSốMôTả);

        TextView nhãnHuyHiệu = new TextView(this);
        nhãnHuyHiệu.setText(món.nhãnBadge);
        nhãnHuyHiệu.setTextColor(Color.parseColor(món.làChínhHãng ? "#6EE7B7" : "#FFD54F"));
        nhãnHuyHiệu.setTextSize(10);
        nhãnHuyHiệu.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView nhãnTiêuĐề = new TextView(this);
        nhãnTiêuĐề.setText(món.tiêuĐề);
        nhãnTiêuĐề.setTextColor(Color.WHITE);
        nhãnTiêuĐề.setTextSize(14);
        nhãnTiêuĐề.setTypeface(null, android.graphics.Typeface.BOLD);

        LinearLayout khungGiá = new LinearLayout(this);
        khungGiá.setOrientation(LinearLayout.HORIZONTAL);
        khungGiá.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams thôngSốKhungGiá = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốKhungGiá.topMargin = 8;
        khungGiá.setLayoutParams(thôngSốKhungGiá);

        TextView nhãnGiáXu = new TextView(this);
        nhãnGiáXu.setText(String.format("%,d", món.giáXu) + " SS");
        nhãnGiáXu.setTextColor(Color.parseColor("#00E676"));
        nhãnGiáXu.setTextSize(15);
        nhãnGiáXu.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView nhãnGiáGốc = new TextView(this);
        nhãnGiáGốc.setText(món.giáGốc);
        nhãnGiáGốc.setTextColor(Color.parseColor("#666666"));
        nhãnGiáGốc.setTextSize(12);
        LinearLayout.LayoutParams thôngSốGiáGốc = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốGiáGốc.leftMargin = 16;
        nhãnGiáGốc.setLayoutParams(thôngSốGiáGốc);

        khungGiá.addView(nhãnGiáXu);
        khungGiá.addView(nhãnGiáGốc);

        TextView nhãnNgườiBán = new TextView(this);
        nhãnNgườiBán.setText("👤 " + món.tênNgườiBán);
        nhãnNgườiBán.setTextColor(Color.parseColor("#9E9DA8"));
        nhãnNgườiBán.setTextSize(11);
        LinearLayout.LayoutParams thôngSốNgườiBán = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốNgườiBán.topMargin = 6;
        nhãnNgườiBán.setLayoutParams(thôngSốNgườiBán);

        khungThôngTin.addView(nhãnHuyHiệu);
        khungThôngTin.addView(nhãnTiêuĐề);
        khungThôngTin.addView(khungGiá);
        khungThôngTin.addView(nhãnNgườiBán);

        khungToànBộ.addView(badgeThươngHiệu);
        khungToànBộ.addView(khungThôngTin);

        thẻCard.addView(khungToànBộ);

        thẻCard.setOnClickListener(v -> {
            Intent ýĐịnhChiTiết = new Intent(this, VoucherDetailActivity.class);
            ýĐịnhChiTiết.putExtra("EXTRA_VOUCHER_TITLE", món.tiêuĐề);
            ýĐịnhChiTiết.putExtra("EXTRA_BRAND_NAME", món.thươngHiệu);
            ýĐịnhChiTiết.putExtra("EXTRA_PRICE_SS", món.giáXu);
            ýĐịnhChiTiết.putExtra("EXTRA_SECRET_CODE", món.mãBíMật);
            ýĐịnhChiTiết.putExtra("EXTRA_IS_OFFICIAL", món.làChínhHãng);
            ýĐịnhChiTiết.putExtra("EXTRA_SELLER_NAME", món.tênNgườiBán);
            ýĐịnhChiTiết.putExtra("EXTRA_SELLER_AVATAR", món.avatarNgườiBán);
            startActivity(ýĐịnhChiTiết);
        });

        if (containerKếtQuảMànHìnhMới != null) {
            containerKếtQuảMànHìnhMới.addView(thẻCard);
        }
    }
}
