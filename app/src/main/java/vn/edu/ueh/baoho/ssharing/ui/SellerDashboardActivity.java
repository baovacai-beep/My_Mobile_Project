package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import vn.edu.ueh.baoho.ssharing.R;

public class SellerDashboardActivity extends AppCompatActivity {

    private TextView tabĐangBán, tabChờXácNhận, tabĐãBán, tabHếtHạn;
    private TextView nhãnChỉSốTrang;
    private LinearLayout containerVoucherĐangBán;

    private int vịTríTabHiệnTại = 1;
    private int chỉSốTrangHiệnTại = 1;
    private final int tổngSốTrang = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        khởiTạoÁnhXạGiaoDiện();
        càiĐặtSựKiệnLắngNghe();
        chọnTabKênhBán(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chọnTabKênhBán(vịTríTabHiệnTại);
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        tabĐangBán = findViewById(R.id.tab_dang_ban);
        tabChờXácNhận = findViewById(R.id.tab_cho_xac_nhan);
        tabĐãBán = findViewById(R.id.tab_da_ban);
        tabHếtHạn = findViewById(R.id.tab_het_han);
        containerVoucherĐangBán = findViewById(R.id.containerVoucherDangBan);
        nhãnChỉSốTrang = findViewById(R.id.tv_page_indicator);
    }

    private void càiĐặtSựKiệnLắngNghe() {
        View nútQuayLại = findViewById(R.id.btnBackShop);
        if (nútQuayLại != null) {
            nútQuayLại.setOnClickListener(vương -> finish());
        }

        // Chuyển Tab trạng thái
        if (tabĐangBán != null) tabĐangBán.setOnClickListener(vương -> chọnTabKênhBán(1));
        if (tabChờXácNhận != null) tabChờXácNhận.setOnClickListener(vương -> chọnTabKênhBán(2));
        if (tabĐãBán != null) tabĐãBán.setOnClickListener(vương -> chọnTabKênhBán(3));
        if (tabHếtHạn != null) tabHếtHạn.setOnClickListener(vương -> chọnTabKênhBán(4));

        // Phân trang
        View nútTrangTrước = findViewById(R.id.btn_prev_page);
        View nútTrangSau = findViewById(R.id.btn_next_page);

        if (nútTrangTrước != null) {
            nútTrangTrước.setOnClickListener(vương -> {
                if (chỉSốTrangHiệnTại > 1) {
                    chỉSốTrangHiệnTại--;
                    cậpNhậtHiểnThịTrang();
                } else {
                    Toast.makeText(this, "Đang ở trang đầu tiên", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (nútTrangSau != null) {
            nútTrangSau.setOnClickListener(vương -> {
                if (chỉSốTrangHiệnTại < tổngSốTrang) {
                    chỉSốTrangHiệnTại++;
                    cậpNhậtHiểnThịTrang();
                } else {
                    Toast.makeText(this, "Đang ở trang cuối cùng", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void chọnTabKênhBán(int vịTríTab) {
        vịTríTabHiệnTại = vịTríTab;
        đặtLạiMàuTab();

        if (containerVoucherĐangBán != null) {
            containerVoucherĐangBán.setVisibility(View.VISIBLE);
        }

        if (vịTríTab == 1) {
            if (tabĐangBán != null) {
                tabĐangBán.setBackgroundResource(R.drawable.bg_tab_active);
                tabĐangBán.setTextColor(Color.WHITE);
            }
            cậpNhậtDanhSáchVoucherĐăngBán();
        } else if (vịTríTab == 2) {
            if (tabChờXácNhận != null) {
                tabChờXácNhận.setBackgroundResource(R.drawable.bg_tab_active);
                tabChờXácNhận.setTextColor(Color.WHITE);
            }
            hiểnThịDanhSáchChờXácNhận();
        } else if (vịTríTab == 3) {
            if (tabĐãBán != null) {
                tabĐãBán.setBackgroundResource(R.drawable.bg_tab_active);
                tabĐãBán.setTextColor(Color.WHITE);
            }
            hiểnThịDanhSáchĐãBán();
        } else if (vịTríTab == 4) {
            if (tabHếtHạn != null) {
                tabHếtHạn.setBackgroundResource(R.drawable.bg_tab_active);
                tabHếtHạn.setTextColor(Color.WHITE);
            }
            hiểnThịDanhSáchHếtHạn();
        }
    }

    private void đặtLạiMàuTab() {
        int màuMặcĐịnh = Color.parseColor("#9E9AA9");
        if (tabĐangBán != null) { tabĐangBán.setBackground(null); tabĐangBán.setTextColor(màuMặcĐịnh); }
        if (tabChờXácNhận != null) { tabChờXácNhận.setBackground(null); tabChờXácNhận.setTextColor(màuMặcĐịnh); }
        if (tabĐãBán != null) { tabĐãBán.setBackground(null); tabĐãBán.setTextColor(màuMặcĐịnh); }
        if (tabHếtHạn != null) { tabHếtHạn.setBackground(null); tabHếtHạn.setTextColor(màuMặcĐịnh); }
    }

    private void cậpNhậtDanhSáchVoucherĐăngBán() {
        if (containerVoucherĐangBán == null) return;
        containerVoucherĐangBán.removeAllViews();

        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String usernameHiệnTại = bộLưuTrữCụcBộ.getString("CURRENT_USERNAME", "baoho123");
        String keyShopCáNhân = "DANH_SACH_VOUCHER_DANG_BAN_" + usernameHiệnTại;

        String danhSáchChuỗiĐăngBán = bộLưuTrữCụcBộ.getString(keyShopCáNhân, "");

        if (danhSáchChuỗiĐăngBán.isEmpty()) {
            if ("baoho123".equalsIgnoreCase(usernameHiệnTại)) {
                danhSáchChuỗiĐăngBán = "POS_BH1||Voucher Starbucks Giảm 30K Đơn 70K||STARBUCKS||SBUX30K-BH||200||31/12/2026||Mã chính chủ do Hồ Gia Bảo quản lý";
            } else if ("beo123".equalsIgnoreCase(usernameHiệnTại)) {
                danhSáchChuỗiĐăngBán = "POS_BEO1||Voucher GrabCar Giảm 50K Chuyến Đêm||GRAB||GRAB50K-BEO||350||31/12/2026||Mã Grab do Gia Beo quản lý";
            } else if ("minh.triet".equalsIgnoreCase(usernameHiệnTại)) {
                danhSáchChuỗiĐăngBán = "POS_MT1||Voucher ShopeeFood Giảm 50K Đơn 100K||SHOPEE||SPFOOD50K-USER22||500||31/12/2026||Mã ShopeeFood do Minh Triết quản lý";
            } else if ("bao.ngoc".equalsIgnoreCase(usernameHiệnTại)) {
                danhSáchChuỗiĐăngBán = "POS_BN1||Voucher MoMo Hoàn 30K Nạp Điện Thoại||MOMO||MOMO30K-OCTOBER77||250||31/12/2026||Mã MoMo do Bảo Ngọc quản lý";
            } else if ("khanh.trinh".equalsIgnoreCase(usernameHiệnTại)) {
                danhSáchChuỗiĐăngBán = "POS_KT1||Voucher Highlands Mua 1 Tặng 1 Trà Sen Vàng||HIGHLANDS||HLM1T1-USER33||180||31/12/2026||Mã Highlands do Khánh Trình quản lý";
            } else if ("gia.bao".equalsIgnoreCase(usernameHiệnTại)) {
                danhSáchChuỗiĐăngBán = "POS_GB1||Voucher BeCar Giảm 30K Đơn Từ 60K||BE||BECAR30K-USER44||220||31/12/2026||Mã BeCar do Gia Bảo quản lý";
            } else if ("hoang.long".equalsIgnoreCase(usernameHiệnTại)) {
                danhSáchChuỗiĐăngBán = "POS_HL1||Voucher TikiTrading 100K Đơn 300K||TIKI||TIKI100K-USER55||600||31/12/2026||Mã Tiki do Hoàng Long quản lý";
            } else {
                danhSáchChuỗiĐăngBán = "POS_GENERIC||Voucher Khuyến Mãi Cá Nhân||SHOP||CODE123||100||31/12/2026||Mã cá nhân quản lý";
            }
            bộLưuTrữCụcBộ.edit().putString(keyShopCáNhân, danhSáchChuỗiĐăngBán).apply();
        }

        String[] cácBàiĐăng = danhSáchChuỗiĐăngBán.split(";;");
        int sốLượngBán = 0;

        for (String bàiĐăng : cácBàiĐăng) {
            if (bàiĐăng.trim().isEmpty()) continue;
            String[] mảngBài = bàiĐăng.split("\\|\\|");
            if (mảngBài.length < 5) continue;

            sốLượngBán++;
            String mãBài = mảngBài[0];
            String tiêuĐề = mảngBài[1];
            String thươngHiệu = mảngBài[2];
            String mãBíMật = mảngBài[3];
            String giáXu = mảngBài[4];
            String hạnDùng = mảngBài.length > 5 ? mảngBài[5] : "31/12/2026";
            String môTả = mảngBài.length > 6 ? mảngBài[6] : tiêuĐề;

            // Dựng CardView bài đăng
            CardView thẻBài = new CardView(this);
            thẻBài.setCardBackgroundColor(Color.parseColor("#161327"));
            thẻBài.setRadius(32);
            thẻBài.setCardElevation(0);

            LinearLayout.LayoutParams thôngSốCard = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            thôngSốCard.bottomMargin = 24;
            thẻBài.setLayoutParams(thôngSốCard);

            LinearLayout khungNộiDungThẻ = new LinearLayout(this);
            khungNộiDungThẻ.setOrientation(LinearLayout.VERTICAL);
            khungNộiDungThẻ.setPadding(32, 32, 32, 32);

            // Hàng 1: Icon + Thông tin
            LinearLayout hàngThôngTin = new LinearLayout(this);
            hàngThôngTin.setOrientation(LinearLayout.HORIZONTAL);

            TextView badgeThươngHiệu = new TextView(this);
            badgeThươngHiệu.setText(thươngHiệu.toUpperCase());
            badgeThươngHiệu.setTextColor(Color.parseColor("#FF4081"));
            badgeThươngHiệu.setTextSize(10);
            badgeThươngHiệu.setTypeface(null, android.graphics.Typeface.BOLD);
            badgeThươngHiệu.setGravity(Gravity.CENTER);
            badgeThươngHiệu.setBackgroundResource(R.drawable.bg_badge_pink);
            LinearLayout.LayoutParams thôngSốBadge = new LinearLayout.LayoutParams(120, 120);
            badgeThươngHiệu.setLayoutParams(thôngSốBadge);

            LinearLayout khungMôTả = new LinearLayout(this);
            khungMôTả.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams thôngSốMôTả = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            thôngSốMôTả.leftMargin = 24;
            khungMôTả.setLayoutParams(thôngSốMôTả);

            TextView nhãnTiêuĐề = new TextView(this);
            nhãnTiêuĐề.setText(tiêuĐề);
            nhãnTiêuĐề.setTextColor(Color.WHITE);
            nhãnTiêuĐề.setTextSize(14);
            nhãnTiêuĐề.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView nhãnGiá = new TextView(this);
            nhãnGiá.setText(giáXu + " SS");
            nhãnGiá.setTextColor(Color.parseColor("#00E676"));
            nhãnGiá.setTextSize(15);
            nhãnGiá.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView nhãnHạnDùng = new TextView(this);
            nhãnHạnDùng.setText("⏱ HSD: " + hạnDùng);
            nhãnHạnDùng.setTextColor(Color.parseColor("#FFA726"));
            nhãnHạnDùng.setTextSize(11);

            khungMôTả.addView(nhãnTiêuĐề);
            khungMôTả.addView(nhãnGiá);
            khungMôTả.addView(nhãnHạnDùng);

            hàngThôngTin.addView(badgeThươngHiệu);
            hàngThôngTin.addView(khungMôTả);

            // Hàng 2: Nút Chỉnh Sửa & Gỡ Bài
            LinearLayout hàngNútNút = new LinearLayout(this);
            hàngNútNút.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams thôngSốHàngNút = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            thôngSốHàngNút.topMargin = 24;
            hàngNútNút.setLayoutParams(thôngSốHàngNút);

            Button nútSửa = new Button(this);
            nútSửa.setText("✎ Chỉnh sửa");
            nútSửa.setTextColor(Color.WHITE);
            nútSửa.setTextSize(12);
            nútSửa.setBackgroundResource(R.drawable.bg_outline_button);
            LinearLayout.LayoutParams thôngSốNútSửa = new LinearLayout.LayoutParams(0, 110, 1);
            thôngSốNútSửa.rightMargin = 12;
            nútSửa.setLayoutParams(thôngSốNútSửa);

            Button nútXóa = new Button(this);
            nútXóa.setText("🗑 Gỡ bài");
            nútXóa.setTextColor(Color.parseColor("#FF4081"));
            nútXóa.setTextSize(12);
            nútXóa.setBackgroundResource(R.drawable.bg_btn_outline_pink);
            LinearLayout.LayoutParams thôngSốNútXóa = new LinearLayout.LayoutParams(0, 110, 1);
            thôngSốNútXóa.leftMargin = 12;
            nútXóa.setLayoutParams(thôngSốNútXóa);

            nútSửa.setOnClickListener(vương -> {
                Intent ýĐịnhSửa = new Intent(this, PostVoucherActivity.class);
                ýĐịnhSửa.putExtra("IS_EDIT", true);
                ýĐịnhSửa.putExtra("VOUCHER_ID", mãBài);
                ýĐịnhSửa.putExtra("BRAND", thươngHiệu);
                ýĐịnhSửa.putExtra("CODE", mãBíMật);
                ýĐịnhSửa.putExtra("PRICE", giáXu);
                ýĐịnhSửa.putExtra("DESC", môTả);
                startActivity(ýĐịnhSửa);
            });

            nútXóa.setOnClickListener(vương -> xácNhậnXóaBàiĐăng(mãBài, tiêuĐề));

            hàngNútNút.addView(nútSửa);
            hàngNútNút.addView(nútXóa);

            khungNộiDungThẻ.addView(hàngThôngTin);
            khungNộiDungThẻ.addView(hàngNútNút);

            thẻBài.addView(khungNộiDungThẻ);
            containerVoucherĐangBán.addView(thẻBài);
        }

        if (tabĐangBán != null) {
            tabĐangBán.setText("Đang bán (" + sốLượngBán + ")");
        }
    }

    private void hiểnThịDanhSáchChờXácNhận() {
        if (containerVoucherĐangBán == null) return;
        containerVoucherĐangBán.removeAllViews();

        thêmThẻKênhBánChờXácNhận("SHOPEE", "Voucher Shopee 50K - Điện tử", "1,500 SS", "🔒 Đang tạm giữ Escrow cho người mua Minh Triết (MT)", "Minh Triết", "MT", "SP50K-OCTOBER99", 1500);
        thêmThẻKênhBánChờXácNhận("GRAB", "Giảm 50K chuyến xe GrabCar tiếp theo", "350 SS", "🔒 Đang tạm giữ Escrow cho người mua Minh Anh (MA)", "Minh Anh (@minh.anh)", "MA", "GRAB50K-COMMUNITY88", 350);

        if (tabChờXácNhận != null) {
            tabChờXácNhận.setText("Chờ xác nhận (2)");
        }
    }

    private void hiểnThịDanhSáchĐãBán() {
        if (containerVoucherĐangBán == null) return;
        containerVoucherĐangBán.removeAllViews();

        thêmThẻKênhBánĐãBán("HIGHLANDS", "Voucher Highlands Coffee Mua 1 Tặng 1 Trà Sen Vàng", "+180 SS", "✅ Đã giải ngân thành công vào ví người bán", "19/10/2026 15:30");
        thêmThẻKênhBánĐãBán("MOMO", "Voucher MoMo Hoàn 30K Nạp Tiền ĐT", "+250 SS", "✅ Đã giải ngân thành công vào ví người bán", "19/10/2026 14:20");
        thêmThẻKênhBánĐãBán("LAZADA", "Voucher Lazada 50K Đơn Từ 150K", "+420 SS", "✅ Đã giải ngân thành công vào ví người bán", "18/10/2026 18:45");
        thêmThẻKênhBánĐãBán("PHÚC LONG", "Voucher Phúc Long 30K Trà Sữa", "+200 SS", "✅ Đã giải ngân thành công vào ví người bán", "18/10/2026 11:10");

        if (tabĐãBán != null) {
            tabĐãBán.setText("Đã bán (45)");
        }
    }

    private void hiểnThịDanhSáchHếtHạn() {
        if (containerVoucherĐangBán == null) return;
        containerVoucherĐangBán.removeAllViews();

        thêmThẻKênhBánHếtHạn("CGV", "Vé Xem Phim CGV 2D Vé Đôi 100K", "280 SS", "⚠️ Đã hết hạn sử dụng (HSD: 10/10/2026)");

        if (tabHếtHạn != null) {
            tabHếtHạn.setText("Hết hạn (1)");
        }
    }

    private void thêmThẻKênhBánChờXácNhận(String badgeText, String tiêuĐề, String giáXuText, String trạngTháiText, String tênNgườiMua, String avatarNgườiMua, String mãBíMật, int giáXu) {
        CardView thẻCard = tạoKhungThẻChung();
        LinearLayout khungNộiDung = tạoKhungNộiDungThẻChung();

        LinearLayout hàng1 = tạoHàngThôngTin(badgeText, tiêuĐề, giáXuText, trạngTháiText, "#FFA726");

        Button nútChatEscrow = new Button(this);
        nútChatEscrow.setText("💬 Mở phòng chat Escrow với người mua");
        nútChatEscrow.setTextColor(Color.WHITE);
        nútChatEscrow.setTextSize(12);
        nútChatEscrow.setBackgroundResource(R.drawable.bg_btn_purple);
        LinearLayout.LayoutParams thôngSốNút = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 110
        );
        thôngSốNút.topMargin = 20;
        nútChatEscrow.setLayoutParams(thôngSốNút);

        nútChatEscrow.setOnClickListener(v -> {
            Intent ýĐịnhChat = new Intent(this, EscrowChatActivity.class);
            ýĐịnhChat.putExtra("EXTRA_SELLER_NAME", tênNgườiMua);
            ýĐịnhChat.putExtra("EXTRA_SELLER_AVATAR", avatarNgườiMua);
            ýĐịnhChat.putExtra("EXTRA_VOUCHER_TITLE", tiêuĐề);
            ýĐịnhChat.putExtra("EXTRA_SECRET_CODE", mãBíMật);
            ýĐịnhChat.putExtra("EXTRA_PRICE_SS", giáXu);
            startActivity(ýĐịnhChat);
        });

        khungNộiDung.addView(hàng1);
        khungNộiDung.addView(nútChatEscrow);
        thẻCard.addView(khungNộiDung);
        containerVoucherĐangBán.addView(thẻCard);
    }

    private void thêmThẻKênhBánĐãBán(String badgeText, String tiêuĐề, String doanhThuText, String trạngTháiText, String thờiGianText) {
        CardView thẻCard = tạoKhungThẻChung();
        LinearLayout khungNộiDung = tạoKhungNộiDungThẻChung();

        LinearLayout hàng1 = tạoHàngThôngTin(badgeText, tiêuĐề, doanhThuText, trạngTháiText + "\n⏱ " + thờiGianText, "#43E5A0");

        khungNộiDung.addView(hàng1);
        thẻCard.addView(khungNộiDung);
        containerVoucherĐangBán.addView(thẻCard);
    }

    private void thêmThẻKênhBánHếtHạn(String badgeText, String tiêuĐề, String giáXuText, String trạngTháiText) {
        CardView thẻCard = tạoKhungThẻChung();
        LinearLayout khungNộiDung = tạoKhungNộiDungThẻChung();

        LinearLayout hàng1 = tạoHàngThôngTin(badgeText, tiêuĐề, giáXuText, trạngTháiText, "#FF4081");

        Button nútĐăngBánLại = new Button(this);
        nútĐăngBánLại.setText("🔄 Gia hạn / Đăng bán lại");
        nútĐăngBánLại.setTextColor(Color.WHITE);
        nútĐăngBánLại.setTextSize(12);
        nútĐăngBánLại.setBackgroundResource(R.drawable.bg_outline_button);
        LinearLayout.LayoutParams thôngSốNút = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 110
        );
        thôngSốNút.topMargin = 20;
        nútĐăngBánLại.setLayoutParams(thôngSốNút);

        nútĐăngBánLại.setOnClickListener(v -> {
            Intent ýĐịnhTạoMới = new Intent(this, PostVoucherActivity.class);
            ýĐịnhTạoMới.putExtra("BRAND", badgeText);
            ýĐịnhTạoMới.putExtra("DESC", tiêuĐề);
            startActivity(ýĐịnhTạoMới);
        });

        khungNộiDung.addView(hàng1);
        khungNộiDung.addView(nútĐăngBánLại);
        thẻCard.addView(khungNộiDung);
        containerVoucherĐangBán.addView(thẻCard);
    }

    private CardView tạoKhungThẻChung() {
        CardView thẻCard = new CardView(this);
        thẻCard.setCardBackgroundColor(Color.parseColor("#161327"));
        thẻCard.setRadius(32);
        thẻCard.setCardElevation(0);

        LinearLayout.LayoutParams thôngSốCard = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốCard.bottomMargin = 24;
        thẻCard.setLayoutParams(thôngSốCard);
        return thẻCard;
    }

    private LinearLayout tạoKhungNộiDungThẻChung() {
        LinearLayout khungNộiDungThẻ = new LinearLayout(this);
        khungNộiDungThẻ.setOrientation(LinearLayout.VERTICAL);
        khungNộiDungThẻ.setPadding(32, 32, 32, 32);
        return khungNộiDungThẻ;
    }

    private LinearLayout tạoHàngThôngTin(String badgeText, String tiêuĐề, String giáXuText, String môTảPhụText, String màuSốTiềnHex) {
        LinearLayout hàngThôngTin = new LinearLayout(this);
        hàngThôngTin.setOrientation(LinearLayout.HORIZONTAL);

        TextView badgeThươngHiệu = new TextView(this);
        badgeThươngHiệu.setText(badgeText.toUpperCase());
        badgeThươngHiệu.setTextColor(Color.parseColor("#FF4081"));
        badgeThươngHiệu.setTextSize(10);
        badgeThươngHiệu.setTypeface(null, android.graphics.Typeface.BOLD);
        badgeThươngHiệu.setGravity(Gravity.CENTER);
        badgeThươngHiệu.setBackgroundResource(R.drawable.bg_badge_pink);
        LinearLayout.LayoutParams thôngSốBadge = new LinearLayout.LayoutParams(120, 120);
        badgeThươngHiệu.setLayoutParams(thôngSốBadge);

        LinearLayout khungMôTả = new LinearLayout(this);
        khungMôTả.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams thôngSốMôTả = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốMôTả.leftMargin = 24;
        khungMôTả.setLayoutParams(thôngSốMôTả);

        TextView nhãnTiêuĐề = new TextView(this);
        nhãnTiêuĐề.setText(tiêuĐề);
        nhãnTiêuĐề.setTextColor(Color.WHITE);
        nhãnTiêuĐề.setTextSize(14);
        nhãnTiêuĐề.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView nhãnGiá = new TextView(this);
        nhãnGiá.setText(giáXuText);
        nhãnGiá.setTextColor(Color.parseColor(màuSốTiềnHex));
        nhãnGiá.setTextSize(15);
        nhãnGiá.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView nhãnPhụ = new TextView(this);
        nhãnPhụ.setText(môTảPhụText);
        nhãnPhụ.setTextColor(Color.parseColor("#A09CAB"));
        nhãnPhụ.setTextSize(11);

        khungMôTả.addView(nhãnTiêuĐề);
        khungMôTả.addView(nhãnGiá);
        khungMôTả.addView(nhãnPhụ);

        hàngThôngTin.addView(badgeThươngHiệu);
        hàngThôngTin.addView(khungMôTả);

        return hàngThôngTin;
    }

    private void xácNhậnXóaBàiĐăng(String mãBàiĐăng, String tiêuĐề) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận gỡ bài đăng")
                .setMessage("Bạn có chắc chắn muốn gỡ voucher '" + tiêuĐề + "' khỏi sàn giao dịch?")
                .setPositiveButton("Gỡ bài ngay", (hộpThoại, vịTrí) -> thựcHiệnXóaBàiĐăng(mãBàiĐăng))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void thựcHiệnXóaBàiĐăng(String mãBàiĐăngCầnXóa) {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String usernameHiệnTại = bộLưuTrữCụcBộ.getString("CURRENT_USERNAME", "baoho123");
        String keyShopCáNhân = "DANH_SACH_VOUCHER_DANG_BAN_" + usernameHiệnTại;

        String danhSáchChuỗiĐăngBán = bộLưuTrữCụcBộ.getString(keyShopCáNhân, "");
        String danhSáchC2CGốc = bộLưuTrữCụcBộ.getString("DANH_SACH_VOUCHER_C2C_CHUNG", "");

        StringBuilder danhSáchMớiShop = new StringBuilder();
        if (!danhSáchChuỗiĐăngBán.isEmpty()) {
            String[] cácBài = danhSáchChuỗiĐăngBán.split(";;");
            for (String bài : cácBài) {
                if (bài.trim().isEmpty()) continue;
                if (!bài.startsWith(mãBàiĐăngCầnXóa)) {
                    if (danhSáchMớiShop.length() > 0) danhSáchMớiShop.append(";;");
                    danhSáchMớiShop.append(bài);
                }
            }
        }

        StringBuilder danhSáchMớiC2C = new StringBuilder();
        if (!danhSáchC2CGốc.isEmpty()) {
            String[] cácBàiC2C = danhSáchC2CGốc.split(";;");
            for (String bài : cácBàiC2C) {
                if (bài.trim().isEmpty()) continue;
                if (!bài.startsWith(mãBàiĐăngCầnXóa)) {
                    if (danhSáchMớiC2C.length() > 0) danhSáchMớiC2C.append(";;");
                    danhSáchMớiC2C.append(bài);
                }
            }
        }

        bộLưuTrữCụcBộ.edit()
                .putString(keyShopCáNhân, danhSáchMớiShop.toString())
                .putString("DANH_SACH_VOUCHER_C2C_CHUNG", danhSáchMớiC2C.toString())
                .apply();

        cậpNhậtDanhSáchVoucherĐăngBán();
        Toast.makeText(this, "Đã gỡ bài đăng voucher thành công!", Toast.LENGTH_LONG).show();
    }

    private void cậpNhậtHiểnThịTrang() {
        if (nhãnChỉSốTrang != null) {
            nhãnChỉSốTrang.setText(chỉSốTrangHiệnTại + " / " + tổngSốTrang);
        }
        Toast.makeText(this, "Đang ở Trang " + chỉSốTrangHiệnTại, Toast.LENGTH_SHORT).show();
    }
}
