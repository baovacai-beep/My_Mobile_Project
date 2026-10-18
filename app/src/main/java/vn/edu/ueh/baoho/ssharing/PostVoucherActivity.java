package vn.edu.ueh.baoho.ssharing;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PostVoucherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_voucher);

        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        if (findViewById(R.id.btnSubmit) != null) {
            findViewById(R.id.btnSubmit).setOnClickListener(v -> {
                Toast.makeText(this, "Đăng bán voucher thành công!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}