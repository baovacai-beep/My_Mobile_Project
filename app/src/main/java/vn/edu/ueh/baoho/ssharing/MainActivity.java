package vn.edu.ueh.baoho.ssharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout tabMall, tabC2C;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. ÁNH XẠ VÀ XỬ LÝ SỰ KIỆN QUICK ACTION HUB (4 NÚT TRÒN)
        View btnQuickTram0D = findViewById(R.id.btnQuickTram0D);
        View btnQuickFlashGrab = findViewById(R.id.btnQuickFlashGrab);
        View btnQuickMall = findViewById(R.id.btnQuickMall);
        View btnQuickCoinExchange = findViewById(R.id.btnQuickCoinExchange);

        if (btnQuickTram0D != null) {
            btnQuickTram0D.setOnClickListener(v ->
                    Toast.makeText(this, "Mở Trạm 0đ", Toast.LENGTH_SHORT).show()
            );
        }

        if (btnQuickFlashGrab != null) {
            btnQuickFlashGrab.setOnClickListener(v ->
                    Toast.makeText(this, "Mở Giựt Deal Flash Grab", Toast.LENGTH_SHORT).show()
            );
        }

        if (btnQuickMall != null) {
            btnQuickMall.setOnClickListener(v ->
                    Toast.makeText(this, "Mở SSharing Mall", Toast.LENGTH_SHORT).show()
            );
        }

        if (btnQuickCoinExchange != null) {
            btnQuickCoinExchange.setOnClickListener(v ->
                    Toast.makeText(this, "Mở Đổi Xu", Toast.LENGTH_SHORT).show()
            );
        }

        // 2. ÁNH XẠ VÀ XỬ LÝ SỰ KIỆN TAB SWITCHER (MALL / C2C MARKET)
        tabMall = findViewById(R.id.tabMall);
        tabC2C = findViewById(R.id.tabC2C);

        if (tabMall != null) {
            tabMall.setOnClickListener(v -> {
                tabMall.setBackgroundResource(R.drawable.bg_tab_active);
                if (tabC2C != null) tabC2C.setBackground(null);
                Toast.makeText(this, "Đang xem SSharing Mall", Toast.LENGTH_SHORT).show();
            });
        }

        if (tabC2C != null) {
            tabC2C.setOnClickListener(v -> {
                tabC2C.setBackgroundResource(R.drawable.bg_tab_active);
                if (tabMall != null) tabMall.setBackground(null);
                Toast.makeText(this, "Đang xem C2C Market", Toast.LENGTH_SHORT).show();
            });
        }

        // 3. ÁNH XẠ VÀ XỬ LÝ SỰ KIỆN BOTTOM NAVIGATION BAR
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

    // Menu 3 chấm góc phải trên cùng để chuyển nhanh và test tất cả màn hình của nhóm
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