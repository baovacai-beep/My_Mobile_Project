package vn.edu.ueh.baoho.ssharing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class VoucherDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_detail);

        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        if (findViewById(R.id.btnViewMap) != null) {
            findViewById(R.id.btnViewMap).setOnClickListener(v ->
                    startActivity(new Intent(this, MapStoreLocatorActivity.class))
            );
        }

        if (findViewById(R.id.btnBuy) != null) {
            findViewById(R.id.btnBuy).setOnClickListener(v -> {
                Toast.makeText(this, "Đã mua voucher thành công! Mở phòng Escrow Chat...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, EscrowChatActivity.class));
            });
        }
    }
}
