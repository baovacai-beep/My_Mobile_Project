package vn.edu.ueh.baoho.ssharing;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WalletActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        if (findViewById(R.id.btnDeposit) != null) {
            findViewById(R.id.btnDeposit).setOnClickListener(v ->
                    Toast.makeText(this, "Tính năng nạp xu VietQR đang mở rộng...", Toast.LENGTH_SHORT).show()
            );
        }
    }
}
