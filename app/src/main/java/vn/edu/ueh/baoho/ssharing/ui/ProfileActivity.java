package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.ueh.baoho.ssharing.R;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        View btnBack = findViewById(R.id.btnBackProfile);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        View btnMenuEkyc = findViewById(R.id.btnMenuEkyc);
        if (btnMenuEkyc != null) {
            btnMenuEkyc.setOnClickListener(v ->
                    startActivity(new Intent(this, EkycVerificationActivity.class))
            );
        }

        View btnMenu2FA = findViewById(R.id.btnMenu2FA);
        if (btnMenu2FA != null) {
            btnMenu2FA.setOnClickListener(v ->
                    startActivity(new Intent(this, TwoFactorAuthActivity.class))
            );
        }

        View btnMenuSeller = findViewById(R.id.btnMenuSeller);
        if (btnMenuSeller != null) {
            btnMenuSeller.setOnClickListener(v ->
                    startActivity(new Intent(this, SellerDashboardActivity.class))
            );
        }

        View btnMenuWallet = findViewById(R.id.btnMenuWallet);
        if (btnMenuWallet != null) {
            btnMenuWallet.setOnClickListener(v ->
                    startActivity(new Intent(this, WalletActivity.class))
            );
        }

        View btnMenuArbiter = findViewById(R.id.btnMenuArbiter);
        if (btnMenuArbiter != null) {
            btnMenuArbiter.setOnClickListener(v ->
                    startActivity(new Intent(this, activity_staff_arbiter.class))
            );
        }

        if (findViewById(R.id.btnLogout) != null) {
            findViewById(R.id.btnLogout).setOnClickListener(v -> {
                Toast.makeText(this, "Đã đăng xuất tài khoản", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}