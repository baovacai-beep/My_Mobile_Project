package vn.edu.ueh.baoho.ssharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hiển thị giao diện Trang chủ của Trần Gia Bảo
        setContentView(R.layout.activity_main);
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