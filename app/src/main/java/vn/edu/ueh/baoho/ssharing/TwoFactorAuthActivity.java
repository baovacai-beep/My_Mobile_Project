package vn.edu.ueh.baoho.ssharing;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TwoFactorAuthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_factor_auth);

        findViewById(R.id.btn_xac_nhan_otp).setOnClickListener(v ->
                Toast.makeText(this, "Xác thực 2FA thành công!", Toast.LENGTH_SHORT).show()
        );
    }
}