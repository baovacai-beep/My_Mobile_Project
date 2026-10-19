package vn.edu.ueh.baoho.ssharing.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.ueh.baoho.ssharing.R;

public class TwoFactorAuthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_factor_auth);

        if (findViewById(R.id.btn_xac_nhan_otp) != null) {
            findViewById(R.id.btn_xac_nhan_otp).setOnClickListener(v -> {
                Toast.makeText(this, "Xác thực 2FA thành công!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }

        if (findViewById(R.id.btn_resend_otp) != null) {
            findViewById(R.id.btn_resend_otp).setOnClickListener(v ->
                    Toast.makeText(this, "Đã gửi lại mã OTP 6 số qua Email/SMS!", Toast.LENGTH_SHORT).show()
            );
        }
    }
}