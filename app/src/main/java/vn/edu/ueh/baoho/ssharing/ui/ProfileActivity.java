package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.ueh.baoho.ssharing.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView nhãnHọTênTàiKhoản;
    private TextView nhãnChiTiếtTênEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        khởiTạoÁnhXạGiaoDiện();
        cậpNhậtThôngTinCáNhân();
        càiĐặtSựKiệnLắngNghe();
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        nhãnHọTênTàiKhoản = findViewById(R.id.tvProfileFullName);
        nhãnChiTiếtTênEmail = findViewById(R.id.tvProfileEmailPhone);
    }

    private void cậpNhậtThôngTinCáNhân() {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String họTênUser = bộLưuTrữCụcBộ.getString("CURRENT_USER_NAME", "Gia Bảo Hồ");
        String emailUser = bộLưuTrữCụcBộ.getString("CURRENT_USER_EMAIL", "baoho123@ssharing.vn");
        String usernameUser = bộLưuTrữCụcBộ.getString("CURRENT_USERNAME", "baoho123");

        if (nhãnHọTênTàiKhoản != null) {
            nhãnHọTênTàiKhoản.setText(họTênUser);
        }
        if (nhãnChiTiếtTênEmail != null) {
            nhãnChiTiếtTênEmail.setText("@" + usernameUser + " • " + emailUser + " • Đã eKYC");
        }
    }

    private void càiĐặtSựKiệnLắngNghe() {
        View nútQuayLại = findViewById(R.id.btnBackProfile);
        if (nútQuayLại != null) {
            nútQuayLại.setOnClickListener(v -> finish());
        }

        View nútMenuEkyc = findViewById(R.id.btnMenuEkyc);
        if (nútMenuEkyc != null) {
            nútMenuEkyc.setOnClickListener(v ->
                    startActivity(new Intent(this, EkycVerificationActivity.class))
            );
        }

        View nútMenu2FA = findViewById(R.id.btnMenu2FA);
        if (nútMenu2FA != null) {
            nútMenu2FA.setOnClickListener(v ->
                    startActivity(new Intent(this, TwoFactorAuthActivity.class))
            );
        }

        View nútMenuSeller = findViewById(R.id.btnMenuSeller);
        if (nútMenuSeller != null) {
            nútMenuSeller.setOnClickListener(v ->
                    startActivity(new Intent(this, SellerDashboardActivity.class))
            );
        }

        View nútMenuWallet = findViewById(R.id.btnMenuWallet);
        if (nútMenuWallet != null) {
            nútMenuWallet.setOnClickListener(v ->
                    startActivity(new Intent(this, WalletActivity.class))
            );
        }

        View nútMenuArbiter = findViewById(R.id.btnMenuArbiter);
        if (nútMenuArbiter != null) {
            nútMenuArbiter.setOnClickListener(v ->
                    startActivity(new Intent(this, activity_staff_arbiter.class))
            );
        }

        // Xử lý nút Đăng xuất
        View nútĐăngXuất = findViewById(R.id.btnLogout);
        if (nútĐăngXuất != null) {
            nútĐăngXuất.setOnClickListener(v -> thựcHiệnĐăngXuấtTàiKhoản());
        }
    }

    private void thựcHiệnĐăngXuấtTàiKhoản() {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        bộLưuTrữCụcBộ.edit()
                .putBoolean("IS_LOGGED_IN", false)
                .apply();

        Toast.makeText(this, "Đã đăng xuất tài khoản thành công.", Toast.LENGTH_SHORT).show();
        Intent ýĐịnhChuyển = new Intent(this, LoginActivity.class);
        ýĐịnhChuyển.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(ýĐịnhChuyển);
        finish();
    }
}
