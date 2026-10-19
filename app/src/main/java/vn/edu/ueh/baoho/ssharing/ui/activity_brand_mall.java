package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.ueh.baoho.ssharing.R;

public class activity_brand_mall extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_mall);

        View btnBack = findViewById(R.id.btnBackBrandMall);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        View cardBrandVoucher1 = findViewById(R.id.cardBrandVoucher1);
        if (cardBrandVoucher1 != null) {
            cardBrandVoucher1.setOnClickListener(v -> {
                Toast.makeText(this, "Xem chi tiết voucher Mall", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, VoucherDetailActivity.class));
            });
        }
    }
}