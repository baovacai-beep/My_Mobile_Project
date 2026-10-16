package vn.edu.ueh.baoho.ssharing;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SellerDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        if (findViewById(R.id.btnBackShop) != null) {
            findViewById(R.id.btnBackShop).setOnClickListener(v -> finish());
        }
    }
}
