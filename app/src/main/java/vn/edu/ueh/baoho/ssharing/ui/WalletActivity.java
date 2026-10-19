package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.ueh.baoho.ssharing.R;

public class WalletActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        View imgAvatar = findViewById(R.id.imgAvatar);
        if (imgAvatar != null) {
            imgAvatar.setOnClickListener(v ->
                    startActivity(new Intent(this, ProfileActivity.class))
            );
        }

        if (findViewById(R.id.btnDeposit) != null) {
            findViewById(R.id.btnDeposit).setOnClickListener(v -> {
                Toast.makeText(this, "Chuyển sang màn hình Nạp xu VietQR...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, CoinTopupActivity.class));
            });
        }
    }
}