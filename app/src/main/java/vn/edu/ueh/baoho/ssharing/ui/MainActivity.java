package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import vn.edu.ueh.baoho.ssharing.R;
import vn.edu.ueh.baoho.ssharing.adapter.BannerSliderAdapter;
import vn.edu.ueh.baoho.ssharing.model.VoucherC2C;
import vn.edu.ueh.baoho.ssharing.model.VoucherMall;
import vn.edu.ueh.baoho.ssharing.viewmodel.VoucherViewModel;

public class MainActivity extends AppCompatActivity {

    private static boolean đãKhởiTạoDữLiệuMẫuXong = false;

    private ViewPager2 bộTrượtBannerMain;
    private LinearLayout khungChỉSốDotBanner;
    private Handler bộLịchTrượtTựĐộng = new Handler(Looper.getMainLooper());
    private Runnable côngViệcTựĐộngTrượtBanner;

    private LinearLayout thẻGianHàngMall, thẻChợC2C;
    private VoucherViewModel bộQuảnLýDữLiệuVoucher;
    private TextView nhãnSốDưXuMànChính;
    private TextView nhãnTiêuĐềKhuVực;
    private LinearLayout khungLướiVoucherTrangChủ;
    private LinearLayout khungVoucherGợiÝBottom;

    private EditText ôNhậpTìmKiếmMànChính;
    private TextView nútTìmKiếmMànChính;
    private Button nútXemTấtCảKhoVoucher;

    private String chuỗiTabĐangChọn = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra phiên đăng nhập bắt buộc
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        boolean đãĐăngNhập = bộLưuTrữVí.getBoolean("IS_LOGGED_IN", false);
        if (!đãĐăngNhập) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        khởiTạoÁnhXạGiaoDiện();
        cậpNhậtTênNgườiDùngMànChính(bộLưuTrữVí);
        khởiTạoViewModel();
        càiĐặtBannerAutoSlider();
        càiĐặtSựKiệnLắngNgheGiaoDiện();
        cậpNhậtGiaoDiệnTab(true); // Mặc định hiển thị SSharing Mall
        cậpNhậtKhungGợiÝDànhChoBạn();
    }

    private static android.graphics.Bitmap giảiMãẢnhThuNhỏAnToàn(String path, int maxDim) {
        if (path == null || path.isEmpty()) return null;
        try {
            android.graphics.BitmapFactory.Options opts = new android.graphics.BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            android.graphics.BitmapFactory.decodeFile(path, opts);
            if (opts.outWidth <= 0 || opts.outHeight <= 0) return null;

            int sample = 1;
            while ((opts.outWidth / sample) > maxDim || (opts.outHeight / sample) > maxDim) {
                sample *= 2;
            }
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = sample;
            opts.inPreferredConfig = android.graphics.Bitmap.Config.RGB_565;
            return android.graphics.BitmapFactory.decodeFile(path, opts);
        } catch (Throwable ignored) {}
        return null;
    }

    private void cậpNhậtTênNgườiDùngMànChính(SharedPreferences bộLưuTrữ) {
        TextView nhãnTênUser = findViewById(R.id.tvUsername);
        ImageView ảnhĐạiDiệnHome = findViewById(R.id.imgAvatar);

        String usernameClean = bộLưuTrữ.getString("CURRENT_USERNAME", "baoho123").toLowerCase().trim();

        if (nhãnTênUser != null) {
            String tênNgườiDùng = bộLưuTrữ.getString("CURRENT_USER_NAME", "Gia Bảo Hồ");
            nhãnTênUser.setText(tênNgườiDùng);
        }

        if (ảnhĐạiDiệnHome != null) {
            // Reset ảnh mặc định ban đầu để xóa bộ nhớ đệm ảnh cũ khi chuyển đổi giữa các tài khoản
            ảnhĐạiDiệnHome.setImageResource(R.drawable.ic_user);
            vn.edu.ueh.baoho.ssharing.utils.SocialHelper.loadUserAvatar(this, ảnhĐạiDiệnHome, usernameClean);
        }

        View badgeRed = findViewById(R.id.viewNotificationBadgeRed);
        if (badgeRed != null) {
            boolean hasUnread = bộLưuTrữ.getBoolean("HAS_UNREAD_NOTIFICATIONS_" + usernameClean, true);
            badgeRed.setVisibility(hasUnread ? View.VISIBLE : View.GONE);
        }

        cậpNhậtSốLượngTinNhắnBongBóngChat(bộLưuTrữ);
    }

    private void cậpNhậtSốLượngTinNhắnBongBóngChat(SharedPreferences bộLưuTrữ) {
        TextView nútBongBóngChatNổi = findViewById(R.id.btnFloatingChatBubble);
        if (nútBongBóngChatNổi == null) return;

        String usernameHiệnTại = bộLưuTrữ.getString("CURRENT_USERNAME", "baoho123");
        String danhSáchPhòngChatTấtCả = bộLưuTrữ.getString("DANH_SACH_PHONG_CHAT_TAT_CA", "");

        int sốTinChưaĐọcThựcTế = 0;

        if (!danhSáchPhòngChatTấtCả.isEmpty()) {
            String[] cácPhòng = danhSáchPhòngChatTấtCả.split(";;;");
            for (String phòng : cácPhòng) {
                if (phòng.trim().isEmpty()) continue;
                String[] mảngTrường = phòng.split("\\|\\|");
                if (mảngTrường.length < 8) continue;

                String idPhòng = mảngTrường[0];
                String buyerUser = mảngTrường[3];
                String sellerName = mảngTrường[5];

                if (usernameHiệnTại.equalsIgnoreCase(buyerUser) || sellerName.toLowerCase().contains(usernameHiệnTại.toLowerCase())) {
                    // Kiểm tra trạng thái đã đọc/chưa đọc của phòng chat này
                    boolean làĐãĐọc = bộLưuTrữ.getBoolean("IS_READ_ROOM_" + idPhòng + "_" + usernameHiệnTại.toLowerCase().trim(), false);
                    if (!làĐãĐọc) {
                        sốTinChưaĐọcThựcTế++;
                    }
                }
            }
        }

        if (sốTinChưaĐọcThựcTế > 0) {
            nútBongBóngChatNổi.setText("💬 Chat (" + sốTinChưaĐọcThựcTế + ") 🔴");
        } else {
            nútBongBóngChatNổi.setText("💬 Chat");
        }
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        bộTrượtBannerMain = findViewById(R.id.bannerViewPager);
        khungChỉSốDotBanner = findViewById(R.id.layoutBannerDots);

        nhãnSốDưXuMànChính = findViewById(R.id.tvCoins);
        thẻGianHàngMall = findViewById(R.id.tabMall);
        thẻChợC2C = findViewById(R.id.tabC2C);
        nhãnTiêuĐềKhuVực = findViewById(R.id.tvHomeSectionTitle);
        khungLướiVoucherTrangChủ = findViewById(R.id.containerVoucherHomeGrid);
        khungVoucherGợiÝBottom = findViewById(R.id.containerSuggestedVouchers);

        ôNhậpTìmKiếmMànChính = findViewById(R.id.etHomeSearch);
        nútTìmKiếmMànChính = findViewById(R.id.btnDoSearchHome);
        nútXemTấtCảKhoVoucher = findViewById(R.id.btnViewFullSearchResults);
    }

    private void càiĐặtBannerAutoSlider() {
        if (bộTrượtBannerMain == null) return;

        List<BannerSliderAdapter.ThôngTinBannerItem> danhSáchBanner = new ArrayList<>();
        danhSáchBanner.add(new BannerSliderAdapter.ThôngTinBannerItem(
                "🔥 SIÊU DEAL SHOPEE FOOD", "Giảm 50% đơn từ 100K khi thanh toán qua SSharing", 120, "🛍", "SHOPEE", "SPFOOD50-SLIDER", Color.parseColor("#7C4DFF"), Color.parseColor("#FF4081"), true, "ShopeeFood Official", "SF",
                "https://cdn-media.sforum.vn/storage/app/media/wp-content/uploads/2024/04/shopee-rewards-thumbnail.jpg", R.drawable.banner_shopeefood
        ));
        danhSáchBanner.add(new BannerSliderAdapter.ThôngTinBannerItem(
                "STARBUCKS COFFEE WEEKEND", "Mua 1 Tặng 1 Trà/Cà phê tất cả chi nhánh", 160, "☕", "STARBUCKS", "SBUX-M1T1-SLIDER", Color.parseColor("#11998E"), Color.parseColor("#38EF7D"), true, "Starbucks Official Store", "SB",
                "", R.drawable.banner_starbucks
        ));
        danhSáchBanner.add(new BannerSliderAdapter.ThôngTinBannerItem(
                "GRABCAR FREESHIP & GIẢM 50K", "Đồng giá 10 Xu - Săn deal khung giờ vàng", 100, "🚗", "GRAB", "GRAB50K-SLIDER", Color.parseColor("#00B4DB"), Color.parseColor("#0083B0"), true, "Grab Vietnam Store", "GR",
                "", R.drawable.banner_grab
        ));
        danhSáchBanner.add(new BannerSliderAdapter.ThôngTinBannerItem(
                "MOMO HOÀN XU 30K", "Nạp tiền ĐT & Data 4G Viettel, Mobifone, Vinaphone", 150, "📱", "MOMO", "MOMO30K-SLIDER", Color.parseColor("#FF416C"), Color.parseColor("#FF4B2B"), true, "MoMo Official Store", "MM",
                "", R.drawable.banner_momo
        ));

        BannerSliderAdapter bộTrượtAdapter = new BannerSliderAdapter(danhSáchBanner, itemBanner ->
                mởChiTiếtVoucher(
                        itemBanner.tiêuĐề,
                        itemBanner.tênThươngHiệu,
                        itemBanner.giáXu,
                        itemBanner.mãBíMật,
                        itemBanner.làChínhHãng,
                        itemBanner.tênNgườiBán,
                        itemBanner.avatarNgườiBán
                )
        );

        bộTrượtBannerMain.setAdapter(bộTrượtAdapter);

        // Tạo dấu Dot chỉ số trang
        tạoDấuDotChỉSốTrang(danhSáchBanner.size(), 0);

        bộTrượtBannerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                cậpNhậtDotChỉSốTrang(danhSáchBanner.size(), position);
            }
        });

        // Tự động trượt mỗi 5 giây
        côngViệcTựĐộngTrượtBanner = new Runnable() {
            @Override
            public void run() {
                if (bộTrượtBannerMain != null && danhSáchBanner.size() > 0) {
                    int vịTríHiệnTại = bộTrượtBannerMain.getCurrentItem();
                    int vịTríMới = (vịTríHiệnTại + 1) % danhSáchBanner.size();
                    bộTrượtBannerMain.setCurrentItem(vịTríMới, true);
                    bộLịchTrượtTựĐộng.postDelayed(this, 5000);
                }
            }
        };

        bộLịchTrượtTựĐộng.postDelayed(côngViệcTựĐộngTrượtBanner, 5000);
    }

    private void tạoDấuDotChỉSốTrang(int tổngSốTrang, int vịTríHiệnTại) {
        if (khungChỉSốDotBanner == null) return;
        khungChỉSốDotBanner.removeAllViews();

        for (int i = 0; i < tổngSốTrang; i++) {
            View dotView = new View(this);
            LinearLayout.LayoutParams thôngSốDot = new LinearLayout.LayoutParams(
                    i == vịTríHiệnTại ? 40 : 16, 12
            );
            thôngSốDot.leftMargin = 8;
            thôngSốDot.rightMargin = 8;
            dotView.setLayoutParams(thôngSốDot);

            if (i == vịTríHiệnTại) {
                dotView.setBackgroundResource(R.drawable.bg_tab_active);
            } else {
                dotView.setBackgroundColor(Color.parseColor("#60FFFFFF"));
            }
            khungChỉSốDotBanner.addView(dotView);
        }
    }

    private void cậpNhậtDotChỉSốTrang(int tổngSốTrang, int vịTríHiệnTại) {
        tạoDấuDotChỉSốTrang(tổngSốTrang, vịTríHiệnTại);
    }

    private void khởiTạoViewModel() {
        bộQuảnLýDữLiệuVoucher = new ViewModelProvider(this).get(VoucherViewModel.class);

        // Tránh vòng lặp vô tận trên UI Thread làm đơ ứng dụng (ANR)
        bộQuảnLýDữLiệuVoucher.getAllVouchers().observe(this, danhSáchVoucher -> {
            if (!đãKhởiTạoDữLiệuMẫuXong && (danhSáchVoucher == null || danhSáchVoucher.isEmpty())) {
                đãKhởiTạoDữLiệuMẫuXong = true;
                khởiTạoDữLiệuMẫu();
            }
        });

        bộQuảnLýDữLiệuVoucher.getSelectedFilter().observe(this, bộLọc -> {
            if ("MALL".equals(bộLọc)) {
                cậpNhậtGiaoDiệnTab(true);
            } else if ("C2C".equals(bộLọc)) {
                cậpNhậtGiaoDiệnTab(false);
            }
        });
    }

    private void càiĐặtSựKiệnLắngNgheGiaoDiện() {
        // HEADER CLICKS
        View ảnhĐạiDiện = findViewById(R.id.imgAvatar);
        View tênNgườiDùng = findViewById(R.id.tvUsername);
        View thẻSốDưXu = findViewById(R.id.btnCoinBadge);
        View nútThôngBáo = findViewById(R.id.btnNotification);

        if (ảnhĐạiDiện != null) ảnhĐạiDiện.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        if (tênNgườiDùng != null) tênNgườiDùng.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        if (thẻSốDưXu != null) thẻSốDưXu.setOnClickListener(v -> startActivity(new Intent(this, WalletActivity.class)));
        if (nútThôngBáo != null) nútThôngBáo.setOnClickListener(v -> startActivity(new Intent(this, NotificationCenterActivity.class)));

        View nútBạnBèHeader = findViewById(R.id.btnSocialFriendsHeader);
        if (nútBạnBèHeader != null) {
            nútBạnBèHeader.setOnClickListener(v -> startActivity(new Intent(this, SocialFriendsActivity.class)));
        }

        // BONG BÓNG CHAT NỔI FLOATING CHAT BUBBLE
        View nútBongBóngChatNổi = findViewById(R.id.btnFloatingChatBubble);
        if (nútBongBóngChatNổi != null) {
            nútBongBóngChatNổi.setOnClickListener(v -> startActivity(new Intent(this, ChatInboxActivity.class)));
        }

        // THANH TÌM KIẾM MÀN HÌNH CHÍNH
        if (nútTìmKiếmMànChính != null) {
            nútTìmKiếmMànChính.setOnClickListener(v -> thựcHiệnMởTrangTìmKiếmMới());
        }
        if (ôNhậpTìmKiếmMànChính != null) {
            ôNhậpTìmKiếmMànChính.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    thựcHiệnMởTrangTìmKiếmMới();
                    return true;
                }
                return false;
            });
        }

        if (nútXemTấtCảKhoVoucher != null) {
            nútXemTấtCảKhoVoucher.setOnClickListener(v -> {
                Intent ýĐịnhMới = new Intent(this, SearchActivity.class);
                startActivity(ýĐịnhMới);
            });
        }

        càiĐặtCụmNútThaoTácNhanh();
    }

    private void càiĐặtCụmNútThaoTácNhanh() {
        // QUICK ACTION HUB (4 NÚT TRÒN)
        View nútTrạm0Đ = findViewById(R.id.btnQuickTram0D);
        View nútGiựtDeal = findViewById(R.id.btnQuickFlashGrab);
        View nútMall = findViewById(R.id.btnQuickMall);
        View nútĐổiXu = findViewById(R.id.btnQuickCoinExchange);

        if (nútTrạm0Đ != null) {
            nútTrạm0Đ.setOnClickListener(v -> {
                Toast.makeText(this, "Mở Trạm Coupon Cộng Đồng 0đ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, CommunityCouponsActivity.class));
            });
        }
        if (nútGiựtDeal != null) {
            nútGiựtDeal.setOnClickListener(v -> {
                Toast.makeText(this, "Mở Sự Kiện Giựt Deal Khung Giờ Vàng", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, FlashGrabActivity.class));
            });
        }
        if (nútMall != null) {
            nútMall.setOnClickListener(v -> {
                Toast.makeText(this, "Mở Gian Hàng SSharing Mall", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, activity_brand_mall.class));
            });
        }
        if (nútĐổiXu != null) {
            nútĐổiXu.setOnClickListener(v -> {
                Toast.makeText(this, "Mở Thu Mua Voucher Lấy Xu Tức Thì", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, InstantBuybackActivity.class));
            });
        }

        View nútXemTấtCả = findViewById(R.id.btnViewAll);
        if (nútXemTấtCả != null) {
            nútXemTấtCả.setOnClickListener(v -> {
                Intent ýĐịnhSearch = new Intent(this, SearchActivity.class);
                startActivity(ýĐịnhSearch);
            });
        }

        // TAB SWITCHER (SSHARING MALL / C2C MARKET)
        if (thẻGianHàngMall != null) {
            thẻGianHàngMall.setOnClickListener(v -> {
                bộQuảnLýDữLiệuVoucher.setFilter("MALL");
                cậpNhậtGiaoDiệnTab(true);
                Toast.makeText(this, "Đang xem SSharing Mall (Thương hiệu chính hãng)", Toast.LENGTH_SHORT).show();
            });
        }

        if (thẻChợC2C != null) {
            thẻChợC2C.setOnClickListener(v -> {
                bộQuảnLýDữLiệuVoucher.setFilter("C2C");
                cậpNhậtGiaoDiệnTab(false);
                Toast.makeText(this, "Đang xem Chợ C2C Market (Giao dịch người dùng)", Toast.LENGTH_SHORT).show();
            });
        }

        // BỘ LỌC DANH MỤC
        View nútẨmThực = findViewById(R.id.btnCategoryAmThuc);
        View nútMuaSắm = findViewById(R.id.btnCategoryMuaSam);
        View nútDiChuyển = findViewById(R.id.btnCategoryDiChuyen);
        View nútGiảiTrí = findViewById(R.id.btnCategoryGiaiTri);

        if (nútẨmThực != null) {
            nútẨmThực.setOnClickListener(v -> {
                cậpNhậtDanhSáchẨmThực();
                Toast.makeText(this, "Đã lọc danh mục Ẩm Thực (2 kết quả). Bấm 'Xem tất cả' để xem trọn kho!", Toast.LENGTH_SHORT).show();
            });
        }
        if (nútMuaSắm != null) {
            nútMuaSắm.setOnClickListener(v -> {
                cậpNhậtDanhSáchMuaSắm();
                Toast.makeText(this, "Đã lọc danh mục Mua Sắm (2 kết quả). Bấm 'Xem tất cả' để xem trọn kho!", Toast.LENGTH_SHORT).show();
            });
        }
        if (nútDiChuyển != null) {
            nútDiChuyển.setOnClickListener(v -> {
                cậpNhậtDanhSáchDiChuyển();
                Toast.makeText(this, "Đã lọc danh mục Di Chuyển (2 kết quả). Bấm 'Xem tất cả' để xem trọn kho!", Toast.LENGTH_SHORT).show();
            });
        }
        if (nútGiảiTrí != null) {
            nútGiảiTrí.setOnClickListener(v -> {
                cậpNhậtDanhSáchGiảiTrí();
                Toast.makeText(this, "Đã lọc danh mục Giải Trí (1 kết quả). Bấm 'Xem tất cả' để xem trọn kho!", Toast.LENGTH_SHORT).show();
            });
        }

        // BOTTOM NAVIGATION
        càiĐặtThanhĐiềuHướngĐáy();
    }

    private void thựcHiệnMởTrangTìmKiếmMới() {
        String từKhóa = ôNhậpTìmKiếmMànChính != null ? ôNhậpTìmKiếmMànChính.getText().toString().trim() : "";
        Intent ýĐịnhMởTìmKiếm = new Intent(this, SearchActivity.class);
        if (!từKhóa.isEmpty()) {
            ýĐịnhMởTìmKiếm.putExtra("EXTRA_SEARCH_QUERY", từKhóa);
        }
        startActivity(ýĐịnhMởTìmKiếm);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bộLịchTrượtTựĐộng != null && côngViệcTựĐộngTrượtBanner != null) {
            bộLịchTrượtTựĐộng.removeCallbacks(côngViệcTựĐộngTrượtBanner);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences bộLưuTrữ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        cậpNhậtSốDưMànChính();
        cậpNhậtTênNgườiDùngMànChính(bộLưuTrữ);
        if (bộLịchTrượtTựĐộng != null && côngViệcTựĐộngTrượtBanner != null) {
            bộLịchTrượtTựĐộng.postDelayed(côngViệcTựĐộngTrượtBanner, 5000);
        }
    }

    private void cậpNhậtSốDưMànChính() {
        SharedPreferences bộLưuTrữ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        int sốDưXu = bộLưuTrữ.getInt("SO_DU_XU_SS", 1250);
        if (nhãnSốDưXuMànChính != null) {
            nhãnSốDưXuMànChính.setText(String.format("%,d", sốDưXu) + " SS");
        }
    }

    private void cậpNhậtGiaoDiệnTab(boolean làTabMallĐượcChọn) {
        if (thẻGianHàngMall == null || thẻChợC2C == null) return;

        String tabMới = làTabMallĐượcChọn ? "MALL" : "C2C";
        if (tabMới.equals(chuỗiTabĐangChọn) && khungLướiVoucherTrangChủ != null && khungLướiVoucherTrangChủ.getChildCount() > 0) {
            return; // Đã tải xong, tránh vẽ lại giao diện thừa làm giật lag
        }
        chuỗiTabĐangChọn = tabMới;

        if (làTabMallĐượcChọn) {
            thẻGianHàngMall.setBackgroundResource(R.drawable.bg_tab_active);
            thẻChợC2C.setBackground(null);
            if (nhãnTiêuĐềKhuVực != null) {
                nhãnTiêuĐềKhuVực.setText("Gian Hàng SSharing Mall (Thương Hiệu)");
            }
            hiểnThịDanhSáchSSharingMall();
        } else {
            thẻChợC2C.setBackgroundResource(R.drawable.bg_tab_active);
            thẻGianHàngMall.setBackground(null);
            if (nhãnTiêuĐềKhuVực != null) {
                nhãnTiêuĐềKhuVực.setText("Chợ C2C Market (Giao Dịch Người Dùng)");
            }
            hiểnThịDanhSáchChợC2CMarket();
        }
    }

    private void hiểnThịDanhSáchSSharingMall() {
        if (khungLướiVoucherTrangChủ == null) return;
        khungLướiVoucherTrangChủ.removeAllViews();

        // 1. Starbucks Official
        thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "STARBUCKS", "Voucher Starbucks Giảm 20K Đơn 50K", 160, "200 SS", "✓ Chính hãng", true, "Starbucks Coffee", "Starbucks Official Store", "SB", "SBUX20K-OCTOBER99");

        // 2. MoMo Official
        thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "MOMO", "Voucher MoMo Hoàn 30K Nạp ĐT / Data 4G", 250, "300 SS", "✓ Chính hãng", true, "MoMo E-Wallet", "MoMo Official Store", "MM", "MOMO30K-OCTOBER77");

        // 3. Lazada Official
        thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "LAZADA", "Voucher Lazada 50K Đơn Từ 150K", 420, "500 SS", "✓ Chính hãng", true, "Lazada Mall", "Lazada Official Store", "LZ", "LAZ50K-OFFICIAL66");

        // 4. Phúc Long Official
        thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "PHÚC LONG", "Voucher Phúc Long 30K Trà Sữa", 200, "250 SS", "✓ Chính hãng", true, "Phúc Long Tea", "Phúc Long Official Store", "PL", "PL30K-OFFICIAL55");
    }

    private void hiểnThịDanhSáchChợC2CMarket() {
        if (khungLướiVoucherTrangChủ == null) return;
        khungLướiVoucherTrangChủ.removeAllViews();

        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String danhSáchC2CChung = bộLưuTrữCụcBộ.getString("DANH_SACH_VOUCHER_C2C_CHUNG", "");
        String danhSáchĐãBánHệThống = bộLưuTrữCụcBộ.getString("DANH_SACH_VOUCHER_DA_BAN_HE_THONG", "");

        if (!danhSáchC2CChung.isEmpty()) {
            String[] cácBàiC2C = danhSáchC2CChung.split(";;");
            for (String bài : cácBàiC2C) {
                if (bài.trim().isEmpty()) continue;
                String[] mảngTrường = bài.split("\\|\\|");
                if (mảngTrường.length < 5) continue;

                String môTả = mảngTrường[1];
                String thươngHiệu = mảngTrường[2];
                String mãVoucher = mảngTrường[3];

                // Nếu mặt hàng đã BÁN xong -> Ẩn khỏi Sàn C2C Market
                if (danhSáchĐãBánHệThống.contains(mãVoucher)) {
                    continue;
                }

                int giáXu = 180;
                try {
                    giáXu = Integer.parseInt(mảngTrường[4]);
                } catch (Exception ignored) {}

                String tênNgườiBán = mảngTrường.length > 7 ? mảngTrường[7] : "Người dùng SSharing";
                String avatarInitials = mảngTrường.length > 8 ? mảngTrường[8] : "SS";
                String usernameNgườiBán = mảngTrường.length > 9 ? mảngTrường[9] : "";

                String usernameHiệnTại = bộLưuTrữCụcBộ.getString("CURRENT_USERNAME", "baoho123");
                String họTênHiệnTại = bộLưuTrữCụcBộ.getString("CURRENT_USER_NAME", "Gia Bảo Hồ");

                // NẾU LÀ VOUCHER DO CHÍNH BẢN THÂN ĐĂNG BÁN -> KHÔNG HIỂN THỊ TRÊN SÀN C2C CỦA CHÍNH MÌNH DÙNG MUA (Chỉ quản lý trong Shop cá nhân!)
                if (usernameHiệnTại.equalsIgnoreCase(usernameNgườiBán) ||
                        (!usernameNgườiBán.isEmpty() && usernameHiệnTại.equalsIgnoreCase(usernameNgườiBán)) ||
                        tênNgườiBán.toLowerCase().contains(usernameHiệnTại.toLowerCase()) ||
                        tênNgườiBán.equalsIgnoreCase(họTênHiệnTại)) {
                    continue;
                }

                // Bỏ qua các bài đăng test rác không hợp lệ
                if (môTả.equalsIgnoreCase("rhdf") || môTả.length() < 3) continue;

                thêmThẻVoucherVàoKhung(
                        khungLướiVoucherTrangChủ,
                        thươngHiệu.toUpperCase(),
                        môTả,
                        giáXu,
                        "C2C Market",
                        "Cộng đồng",
                        false,
                        thươngHiệu,
                        tênNgườiBán,
                        avatarInitials,
                        mãVoucher
                );
            }
        }

        // Các voucher chính chủ do các tài khoản cá nhân thực tế sở hữu và quản lý (Chỉ thêm NẾU CHƯA BỊ MUA)
        if (!danhSáchĐãBánHệThống.contains("SBUX30K-BH"))
            thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "STARBUCKS", "Voucher Starbucks Giảm 30K Đơn 70K", 200, "300 SS", "Cộng đồng", false, "Starbucks Coffee", "Hồ Gia Bảo (@baoho123)", "BH", "SBUX30K-BH");
        if (!danhSáchĐãBánHệThống.contains("GRAB50K-BEO"))
            thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "GRAB", "Voucher GrabCar Giảm 50K Chuyến Đêm", 350, "500 SS", "Cộng đồng", false, "Grab Vietnam", "Gia Beo (@Beo123)", "GB", "GRAB50K-BEO");
        if (!danhSáchĐãBánHệThống.contains("SPFOOD50K-USER22"))
            thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "SHOPEE", "Voucher ShopeeFood Giảm 50K Đơn 100K", 500, "650 SS", "Cộng đồng", false, "ShopeeFood", "Bảo Ngọc (@bao.ngoc)", "BN", "SPFOOD50K-USER22");
        if (!danhSáchĐãBánHệThống.contains("HLM1T1-USER33"))
            thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "HIGHLANDS", "Voucher Highlands Mua 1 Tặng 1 Trà Sen Vàng", 180, "250 SS", "Cộng đồng", false, "Highlands Coffee", "Khánh Trình (@khanh.trinh)", "KT", "HLM1T1-USER33");
        if (!danhSáchĐãBánHệThống.contains("BECAR30K-USER44"))
            thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "BE", "Voucher BeCar Giảm 30K Đơn Từ 60K", 220, "300 SS", "Cộng đồng", false, "Be Group", "Gia Bảo (@gia.bao)", "GB", "BECAR30K-USER44");
    }

    private void cậpNhậtDanhSáchẨmThực() {
        if (khungLướiVoucherTrangChủ == null) return;
        chuỗiTabĐangChọn = "CATEGORY_FOOD";
        khungLướiVoucherTrangChủ.removeAllViews();
        if (nhãnTiêuĐềKhuVực != null) nhãnTiêuĐềKhuVực.setText("Kết Quả Lọc: Ẩm Thực & Đồ Uống");
        thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "STARBUCKS", "Voucher Starbucks Giảm 20K Đơn 50K", 160, "200 SS", "✓ Chính hãng", true, "Starbucks Coffee", "Starbucks Official Store", "SB", "SBUX20K-OCTOBER99");
        thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "HIGHLANDS", "Voucher Highlands Mua 1 Tặng 1 Trà Sen Vàng", 180, "250 SS", "Cộng đồng", false, "Highlands Coffee", "Khánh Trình (@khanh.trinh)", "KT", "HLM1T1-USER33");
    }

    private void cậpNhậtDanhSáchMuaSắm() {
        if (khungLướiVoucherTrangChủ == null) return;
        chuỗiTabĐangChọn = "CATEGORY_SHOP";
        khungLướiVoucherTrangChủ.removeAllViews();
        if (nhãnTiêuĐềKhuVực != null) nhãnTiêuĐềKhuVực.setText("Kết Quả Lọc: Mua Sắm & Siêu Thị");
        thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "LAZADA", "Voucher Lazada 50K Đơn Từ 150K", 420, "500 SS", "✓ Chính hãng", true, "Lazada Mall", "Lazada Official Store", "LZ", "LAZ50K-OFFICIAL66");
        thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "SHOPEE", "Voucher ShopeeFood Giảm 50K Đơn 100K", 500, "650 SS", "Cộng đồng", false, "ShopeeFood", "Bảo Ngọc (@bao.ngoc)", "BN", "SPFOOD50K-USER22");
    }

    private void cậpNhậtDanhSáchDiChuyển() {
        if (khungLướiVoucherTrangChủ == null) return;
        chuỗiTabĐangChọn = "CATEGORY_TRANSPORT";
        khungLướiVoucherTrangChủ.removeAllViews();
        if (nhãnTiêuĐềKhuVực != null) nhãnTiêuĐềKhuVực.setText("Kết Quả Lọc: Di Chuyển & Xe Công Nghệ");
        thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "GRAB", "Giảm 50K chuyến xe GrabCar tiếp theo", 350, "500 SS", "Cộng đồng", false, "Grab Vietnam", "Minh Anh (@minh.anh)", "MA", "GRAB50K-COMMUNITY88");
        thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "BE", "Voucher BeCar Giảm 30K Đơn Từ 60K", 220, "300 SS", "Cộng đồng", false, "Be Group", "Gia Bảo (@gia.bao)", "GB", "BECAR30K-USER44");
    }

    private void cậpNhậtDanhSáchGiảiTrí() {
        if (khungLướiVoucherTrangChủ == null) return;
        chuỗiTabĐangChọn = "CATEGORY_MOVIE";
        khungLướiVoucherTrangChủ.removeAllViews();
        if (nhãnTiêuĐềKhuVực != null) nhãnTiêuĐềKhuVực.setText("Kết Quả Lọc: Giải Trí & Xem Phim");
        thêmThẻVoucherVàoKhung(khungLướiVoucherTrangChủ, "CGV", "Vé Xem Phim CGV 2D Vé Đôi 100K", 280, "350 SS", "✓ Chính hãng", true, "CGV Cinemas", "CGV Official Store", "CGV", "CGV2D-OFFICIAL11");
    }

    private void cậpNhậtKhungGợiÝDànhChoBạn() {
        if (khungVoucherGợiÝBottom == null || khungVoucherGợiÝBottom.getChildCount() > 0) return;

        thêmThẻVoucherVàoKhung(khungVoucherGợiÝBottom, "HIGHLANDS", "Voucher Highlands Coffee Trà Sen Vàng 30K", 180, "250 SS", "Hot Deal", false, "Highlands Coffee", "Khánh Trình (@khanh.trinh)", "KT", "HLM1T1-HOT");
        thêmThẻVoucherVàoKhung(khungVoucherGợiÝBottom, "STARBUCKS", "Giảm 20K cho đơn từ 50K Starbucks", 160, "200 SS", "✓ Gợi ý", true, "Starbucks Coffee", "Starbucks Official Store", "SB", "SBUX20K-HOT");
        thêmThẻVoucherVàoKhung(khungVoucherGợiÝBottom, "GRAB", "Voucher GrabFood FreeShip 30K", 120, "180 SS", "✓ Đề xuất", true, "GrabFood Vietnam", "Grab Official Store", "GR", "GRABFS30K-HOT");
    }

    private void thêmThẻVoucherVàoKhung(LinearLayout containerMụcTiêu, String tênBadge, String tiêuĐề, int giáXu, String giáGốc, String nhãnBadgeHuyHiệu, boolean làChínhHãng, String thươngHiệu, String tênNgườiBán, String avatarNgườiBán, String mãBíMật) {
        CardView thẻCard = new CardView(this);
        thẻCard.setCardBackgroundColor(Color.parseColor("#16141E"));
        thẻCard.setRadius(28);
        thẻCard.setCardElevation(0);
        thẻCard.setClickable(true);
        thẻCard.setFocusable(true);

        LinearLayout.LayoutParams thôngSốThẻ = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốThẻ.bottomMargin = 20;
        thẻCard.setLayoutParams(thôngSốThẻ);

        LinearLayout khungToànBộ = new LinearLayout(this);
        khungToànBộ.setOrientation(LinearLayout.HORIZONTAL);
        khungToànBộ.setPadding(24, 24, 24, 24);

        // Badge Thương Hiệu
        TextView badgeThươngHiệu = new TextView(this);
        badgeThươngHiệu.setText(tênBadge);
        badgeThươngHiệu.setTextColor(Color.WHITE);
        badgeThươngHiệu.setTextSize(11);
        badgeThươngHiệu.setTypeface(null, android.graphics.Typeface.BOLD);
        badgeThươngHiệu.setGravity(Gravity.CENTER);
        badgeThươngHiệu.setBackgroundResource(R.drawable.bg_badge_pink);
        LinearLayout.LayoutParams thôngSốBadge = new LinearLayout.LayoutParams(130, 130);
        badgeThươngHiệu.setLayoutParams(thôngSốBadge);

        // Khung Thông Tin
        LinearLayout khungThôngTin = new LinearLayout(this);
        khungThôngTin.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams thôngSốMôTả = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốMôTả.leftMargin = 20;
        khungThôngTin.setLayoutParams(thôngSốMôTả);

        TextView nhãnHuyHiệu = new TextView(this);
        nhãnHuyHiệu.setText(nhãnBadgeHuyHiệu);
        nhãnHuyHiệu.setTextColor(Color.parseColor(làChínhHãng ? "#6EE7B7" : "#FFD54F"));
        nhãnHuyHiệu.setTextSize(10);
        nhãnHuyHiệu.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView nhãnTiêuĐề = new TextView(this);
        nhãnTiêuĐề.setText(tiêuĐề);
        nhãnTiêuĐề.setTextColor(Color.WHITE);
        nhãnTiêuĐề.setTextSize(13);
        nhãnTiêuĐề.setTypeface(null, android.graphics.Typeface.BOLD);

        LinearLayout khungGiá = new LinearLayout(this);
        khungGiá.setOrientation(LinearLayout.HORIZONTAL);
        khungGiá.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams thôngSốKhungGiá = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốKhungGiá.topMargin = 6;
        khungGiá.setLayoutParams(thôngSốKhungGiá);

        TextView nhãnGiáXu = new TextView(this);
        nhãnGiáXu.setText(String.format("%,d", giáXu) + " SS");
        nhãnGiáXu.setTextColor(Color.parseColor("#00E676"));
        nhãnGiáXu.setTextSize(14);
        nhãnGiáXu.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView nhãnGiáGốc = new TextView(this);
        nhãnGiáGốc.setText(giáGốc);
        nhãnGiáGốc.setTextColor(Color.parseColor("#666666"));
        nhãnGiáGốc.setTextSize(11);
        LinearLayout.LayoutParams thôngSốGiáGốc = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốGiáGốc.leftMargin = 12;
        nhãnGiáGốc.setLayoutParams(thôngSốGiáGốc);

        khungGiá.addView(nhãnGiáXu);
        khungGiá.addView(nhãnGiáGốc);

        TextView nhãnNgườiBán = new TextView(this);
        nhãnNgườiBán.setText(tênNgườiBán);
        nhãnNgườiBán.setTextColor(Color.parseColor("#9E9DA8"));
        nhãnNgườiBán.setTextSize(10);
        LinearLayout.LayoutParams thôngSốNgườiBán = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốNgườiBán.topMargin = 4;
        nhãnNgườiBán.setLayoutParams(thôngSốNgườiBán);

        khungThôngTin.addView(nhãnHuyHiệu);
        khungThôngTin.addView(nhãnTiêuĐề);
        khungThôngTin.addView(khungGiá);
        khungThôngTin.addView(nhãnNgườiBán);

        khungToànBộ.addView(badgeThươngHiệu);
        khungToànBộ.addView(khungThôngTin);

        thẻCard.addView(khungToànBộ);

        thẻCard.setOnClickListener(v -> mởChiTiếtVoucher(
                tiêuĐề, thươngHiệu, giáXu, mãBíMật, làChínhHãng, tênNgườiBán, avatarNgườiBán
        ));

        if (containerMụcTiêu != null) {
            containerMụcTiêu.addView(thẻCard);
        }
    }

    private void mởChiTiếtVoucher(String tiêuĐề, String thươngHiệu, int giáXu, String mãBíMật, boolean làChínhHãng, String tênNgườiBán, String avatarNgườiBán) {
        Intent ýĐịnhChiTiết = new Intent(this, VoucherDetailActivity.class);
        ýĐịnhChiTiết.putExtra("EXTRA_VOUCHER_TITLE", tiêuĐề);
        ýĐịnhChiTiết.putExtra("EXTRA_BRAND_NAME", thươngHiệu);
        ýĐịnhChiTiết.putExtra("EXTRA_PRICE_SS", giáXu);
        ýĐịnhChiTiết.putExtra("EXTRA_SECRET_CODE", mãBíMật);
        ýĐịnhChiTiết.putExtra("EXTRA_IS_OFFICIAL", làChínhHãng);
        ýĐịnhChiTiết.putExtra("EXTRA_SELLER_NAME", tênNgườiBán);
        ýĐịnhChiTiết.putExtra("EXTRA_SELLER_AVATAR", avatarNgườiBán);
        startActivity(ýĐịnhChiTiết);
    }

    private void càiĐặtThanhĐiềuHướngĐáy() {
        View nútTrangChủ = findViewById(R.id.navHome);
        View nútTìmKiếm = findViewById(R.id.navSearch);
        View nútĐăngBán = findViewById(R.id.navSell);
        View nútVíXu = findViewById(R.id.navWallet);
        View nútTrangCáNhân = findViewById(R.id.navProfile);

        if (nútTrangChủ != null) nútTrangChủ.setOnClickListener(v -> Toast.makeText(this, "Trang chủ SSharing", Toast.LENGTH_SHORT).show());
        if (nútTìmKiếm != null) nútTìmKiếm.setOnClickListener(v -> startActivity(new Intent(this, MapStoreLocatorActivity.class)));
        if (nútĐăngBán != null) nútĐăngBán.setOnClickListener(v -> startActivity(new Intent(this, PostVoucherActivity.class)));
        if (nútVíXu != null) nútVíXu.setOnClickListener(v -> startActivity(new Intent(this, WalletActivity.class)));
        if (nútTrangCáNhân != null) nútTrangCáNhân.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void khởiTạoDữLiệuMẫu() {
        VoucherC2C v1 = new VoucherC2C("VC001", "Giảm 50K ShopeeFood Đơn Từ 100K", "ShopeeFood", 50000, 25, "30/12/2026", "USER_01", "SPFOOD50K");
        VoucherMall v2 = new VoucherMall("VM001", "Voucher Phúc Long 30K Trà Sữa", "Phuc Long Coffee & Tea", 30000, 15, "15/11/2026", "BRAND_PHUCLONG", "PL30KSECRET");
        bộQuảnLýDữLiệuVoucher.themVoucher(v1);
        bộQuảnLýDữLiệuVoucher.themVoucher(v2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "1. Kênh Người Bán (Hồ Gia Bảo)");
        menu.add(0, 2, 0, "2. Giao Dịch Escrow Chat (Hồ Gia Bảo)");
        menu.add(0, 3, 0, "3. Hồ Sơ & Tín Nhiệm (Hồ Gia Bảo)");
        menu.add(0, 4, 0, "4. Ví Của Tôi (Bảo Ngọc)");
        menu.add(0, 5, 0, "5. Đăng Bán Voucher (Bảo Ngọc)");
        menu.add(0, 6, 0, "6. Chi Tiết Voucher (Khánh Trình)");
        menu.add(0, 7, 0, "7. Bản Đồ Định Vị (Khánh Trình)");
        menu.add(0, 8, 0, "8. Đăng Nhập / Auth (Trần Gia Bảo)");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent(this, SellerDashboardActivity.class));
                return true;
            case 2:
                startActivity(new Intent(this, EscrowChatActivity.class));
                return true;
            case 3:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            case 4:
                startActivity(new Intent(this, WalletActivity.class));
                return true;
            case 5:
                startActivity(new Intent(this, PostVoucherActivity.class));
                return true;
            case 6:
                startActivity(new Intent(this, VoucherDetailActivity.class));
                return true;
            case 7:
                startActivity(new Intent(this, MapStoreLocatorActivity.class));
                return true;
            case 8:
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
