package vn.edu.ueh.baoho.ssharing.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.ueh.baoho.ssharing.R;

public class CommunityCouponsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_coupons);

        View btnBack = findViewById(R.id.btnBackCoupons);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        View btnClaim = findViewById(R.id.btnClaim0D1);
        if (btnClaim != null) {
            btnClaim.setOnClickListener(v ->
                    Toast.makeText(this, "Đã lưu mã 0đ vào kho voucher của bạn!", Toast.LENGTH_SHORT).show()
            );
        }
    }
}
