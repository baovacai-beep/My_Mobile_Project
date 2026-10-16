package vn.edu.ueh.baoho.ssharing;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EkycVerificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekyc_verification);

        findViewById(R.id.btn_chup_anh_cccd).setOnClickListener(v ->
                Toast.makeText(this, "Đang khởi động Camera AI quét CCCD...", Toast.LENGTH_SHORT).show()
        );
    }
}