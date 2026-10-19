package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import vn.edu.ueh.baoho.ssharing.R;
import vn.edu.ueh.baoho.ssharing.model.VoucherC2C;
import vn.edu.ueh.baoho.ssharing.model.VoucherMall;
import vn.edu.ueh.baoho.ssharing.viewmodel.VoucherViewModel;

public class MainActivity extends AppCompatActivity {

    private LinearLayout tabMall, tabC2C;
    private VoucherViewModel voucherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // =========================================================================
        // 1. KHỞI TẠO VIEWMODEL & LẮNG NGHE LIVEDATA (CHUẨN MVVM - SỐNG SÓT QUA XOAY MÀN HÌNH)
        // =========================================================================
        voucherViewModel = new ViewModelProvider(this).get(VoucherViewModel.class);

        // Lắng nghe dữ liệu Voucher từ Room Database
        voucherViewModel.getAllVouchers().observe(this, vouchers -> {
            if (vouchers == null || vouchers.isEmpty()) {
                // Tự động nạp dữ liệu mẫu ban đầu nếu cơ sở dữ liệu Room đang trống
                khoiTaoDuLieuMau();
            }
        });

        // Lắng nghe trạng thái Tab đang chọn để khôi phục giao diện đúng tab khi xoay ngang/dọc
        voucherViewModel.getSelectedFilter().observe(this, filter -> {
            if ("MALL".equals(filter)) {
                capNhatGiaoDienTab(true);
            } else if ("C2C".equals(filter)) {
                capNhatGiaoDienTab(false);
            }
        });

        // =========================================================================
        // 2. HEADER CLICKS
        // =========================================================================
        View imgAvatar = findViewById(R.id.imgAvatar);
        View tvUsername = findViewById(R.id.tvUsername);
        View btnCoinBadge = findViewById(R.id.btnCoinBadge);
        View btnNotification = findViewById(R.id.btnNotification);

        if (imgAvatar != null) {
            imgAvatar.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        }
        if (tvUsername != null) {
            tvUsername.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        }
        if (btnCoinBadge != null) {
            btnCoinBadge.setOnClickListener(v -> startActivity(new Intent(this, WalletActivity.class)));
        }
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> Toast.makeText(this, "Không có thông báo mới", Toast.LENGTH_SHORT).show());
        }

        // =========================================================================
        // 3. ÁNH XẠ VÀ XỬ LÝ SỰ KIỆN QUICK ACTION HUB (4 NÚT TRÒN)
        // =========================================================================
        View btnQuickTram0D = findViewById(R.id.btnQuickTram0D);
        View btnQuickFlashGrab = findViewById(R.id.btnQuickFlashGrab);
        View btnQuickMall = findViewById(R.id.btnQuickMall);
        View btnQuickCoinExchange = findViewById(R.id.btnQuickCoinExchange);

        if (btnQuickTram0D != null) {
            btnQuickTram0D.setOnClickListener(v -> {
                Toast.makeText(this, "Mở Trạm 0đ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, CommunityCouponsActivity.class));
            });
        }

        if (btnQuickFlashGrab != null) {
            btnQuickFlashGrab.setOnClickListener(v -> {
                Toast.makeText(this, "Mở Giựt Deal Flash Grab", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, FlashGrabActivity.class));
            });
        }

        if (btnQuickMall != null) {
            btnQuickMall.setOnClickListener(v -> {
                Toast.makeText(this, "Mở SSharing Mall", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, activity_brand_mall.class));
            });
        }

        if (btnQuickCoinExchange != null) {
            btnQuickCoinExchange.setOnClickListener(v -> {
                Toast.makeText(this, "Mở Đổi Voucher Lấy Xu", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, InstantBuybackActivity.class));
            });
        }

        // View All button
        View btnViewAll = findViewById(R.id.btnViewAll);
        if (btnViewAll != null) {
            btnViewAll.setOnClickListener(v -> startActivity(new Intent(this, CommunityCouponsActivity.class)));
        }

        // =========================================================================
        // 4. ÁNH XẠ VÀ XỬ LÝ SỰ KIỆN TAB SWITCHER (MALL / C2C MARKET)
        // =========================================================================
        tabMall = findViewById(R.id.tabMall);
        tabC2C = findViewById(R.id.tabC2C);

        if (tabMall != null) {
            tabMall.setOnClickListener(v -> {
                voucherViewModel.setFilter("MALL");
                Toast.makeText(this, "Đang xem SSharing Mall", Toast.LENGTH_SHORT).show();
            });
        }

        if (tabC2C != null) {
            tabC2C.setOnClickListener(v -> {
                voucherViewModel.setFilter("C2C");
                Toast.makeText(this, "Đang xem C2C Market", Toast.LENGTH_SHORT).show();
            });
        }

        // =========================================================================
        // 5. ÁNH XẠ VÀ XỬ LÝ SỰ KIỆN BOTTOM NAVIGATION BAR
        // =========================================================================
        View navHome = findViewById(R.id.navHome);
        View navSearch = findViewById(R.id.navSearch);
        View navSell = findViewById(R.id.navSell);
        View navWallet = findViewById(R.id.navWallet);
        View navProfile = findViewById(R.id.navProfile);

        if (navHome != null) {
            navHome.setOnClickListener(v ->
                    Toast.makeText(this, "Trang chủ", Toast.LENGTH_SHORT).show()
            );
        }

        if (navSearch != null) {
            navSearch.setOnClickListener(v ->
                    startActivity(new Intent(this, MapStoreLocatorActivity.class))
            );
        }

        if (navSell != null) {
            navSell.setOnClickListener(v ->
                    startActivity(new Intent(this, PostVoucherActivity.class))
            );
        }

        if (navWallet != null) {
            navWallet.setOnClickListener(v ->
                    startActivity(new Intent(this, WalletActivity.class))
            );
        }

        if (navProfile != null) {
            navProfile.setOnClickListener(v ->
                    startActivity(new Intent(this, ProfileActivity.class))
            );
        }
    }

    /**
     * Đồng bộ màu nền active cho Tab Switcher
     */
    private void capNhatGiaoDienTab(boolean isMallSelected) {
        if (tabMall == null || tabC2C == null) return;
        if (isMallSelected) {
            tabMall.setBackgroundResource(R.drawable.bg_tab_active);
            tabC2C.setBackground(null);
        } else {
            tabC2C.setBackgroundResource(R.drawable.bg_tab_active);
            tabMall.setBackground(null);
        }
    }

    /**
     * Khởi tạo bản ghi mẫu ban đầu vào Room thông qua ViewModel
     */
    private void khoiTaoDuLieuMau() {
        VoucherC2C v1 = new VoucherC2C(
                "VC001",
                "Giảm 50K ShopeeFood Đơn Từ 100K",
                "ShopeeFood",
                50000,
                25,
                "30/12/2026",
                "USER_01",
                "SPFOOD50K"
        );
        VoucherMall v2 = new VoucherMall(
                "VM001",
                "Voucher Phúc Long 30K Trà Sữa",
                "Phuc Long Coffee & Tea",
                30000,
                15,
                "15/11/2026",
                "BRAND_PHUCLONG",
                "PL30KSECRET"
        );
        voucherViewModel.themVoucher(v1);
        voucherViewModel.themVoucher(v2);
    }

    // =========================================================================
    // 6. MENU 3 CHẤM ĐỂ TEST ĐIỀU HƯỚNG TẤT CẢ MÀN HÌNH
    // =========================================================================
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