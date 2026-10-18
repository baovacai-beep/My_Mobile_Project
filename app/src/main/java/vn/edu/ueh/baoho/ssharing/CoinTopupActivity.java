package vn.edu.ueh.baoho.ssharing;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CoinTopupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_topup);

        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        if (findViewById(R.id.btnSandboxTest) != null) {
            findViewById(R.id.btnSandboxTest).setOnClickListener(v ->
                    Toast.makeText(this, "Giả lập thanh toán VietQR thành công!", Toast.LENGTH_SHORT).show()
            );
        }
    }
}