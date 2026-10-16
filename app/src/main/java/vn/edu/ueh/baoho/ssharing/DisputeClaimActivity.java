package vn.edu.ueh.baoho.ssharing;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DisputeClaimActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispute_claim);

        findViewById(R.id.btn_gui_khieu_nai).setOnClickListener(v -> {
            Toast.makeText(this, "Đã gửi hồ sơ khiếu nại! Tiền ký quỹ đang được bảo vệ.", Toast.LENGTH_LONG).show();
            finish();
        });
    }
}