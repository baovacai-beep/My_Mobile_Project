package vn.edu.ueh.baoho.ssharing;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FlashGrabActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_grab);

        View btnBack = findViewById(R.id.btnBackFlash);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        View btnGrab = findViewById(R.id.btnGrabNow);
        if (btnGrab != null) {
            btnGrab.setOnClickListener(v ->
                    Toast.makeText(this, "Chúc mừng bạn đã giựt thành công Deal giờ vàng!", Toast.LENGTH_LONG).show()
            );
        }
    }
}