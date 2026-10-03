package vn.edu.ueh.baoho.ssharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hiển thị giao diện Trang chủ của Trần Gia Bảo
        setContentView(R.layout.activity_main);

        // Tự động kết nối an toàn với các ID thường gặp trên thanh điều hướng (nếu có)
        // Cơ chế getIdentifier giúp không bao giờ bị lỗi biên dịch đỏ dù layout có hay chưa có ID
        bindClickSafe("nav_profile", ProfileActivity.class);
        bindClickSafe("btn_profile", ProfileActivity.class);
        bindClickSafe("iv_user", ProfileActivity.class);
        bindClickSafe("nav_wallet", WalletActivity.class);
        bindClickSafe("btn_wallet", WalletActivity.class);
        bindClickSafe("iv_wallet", WalletActivity.class);
    }

    // Hàm hỗ trợ gán sự kiện an toàn: Có ID thì nhận sự kiện, chưa có ID thì tự bỏ qua (không văng app)
    private void bindClickSafe(String viewIdName, Class<?> targetActivity) {
        int resId = getResources().getIdentifier(viewIdName, "id", getPackageName());
        if (resId != 0) {
            View view = findViewById(resId);
            if (view != null) {
                view.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, targetActivity)));
            }
        }
    }

    // Tạo Menu 3 chấm góc phải trên cùng để kiểm thử và demo đầy đủ toàn bộ màn hình của nhóm
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "1. Kênh Người Bán (Gia Bảo)");
        menu.add(0, 2, 0, "2. Giao Dịch Escrow Chat (Gia Bảo)");
        menu.add(0, 3, 0, "3. Hồ Sơ & Tín Nhiệm (Gia Bảo)");
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