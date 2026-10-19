package vn.edu.ueh.baoho.ssharing.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import vn.edu.ueh.baoho.ssharing.R;

public class FlashGrabActivity extends AppCompatActivity {

    private TextView tvCountdownHours, tvCountdownMinutes, tvCountdownSeconds;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_grab);

        tvCountdownHours = findViewById(R.id.tvCountdownHours);
        tvCountdownMinutes = findViewById(R.id.tvCountdownMinutes);
        tvCountdownSeconds = findViewById(R.id.tvCountdownSeconds);

        View btnBack = findViewById(R.id.btnBackFlash);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        View btnGrab = findViewById(R.id.btnGrabNow);
        if (btnGrab != null) {
            btnGrab.setOnClickListener(v ->
                    Toast.makeText(this, "Chúc mừng bạn đã giựt thành công Deal giờ vàng!", Toast.LENGTH_LONG).show()
            );
        }

        startLiveCountdown();
    }

    /**
     * Bắt đầu đồng hồ đếm ngược thời gian thực (2 giờ 45 phút 12 giây)
     */
    private void startLiveCountdown() {
        // 2 giờ 45 phút 12 giây = 9,912,000 milliseconds
        long durationMillis = (2 * 3600 + 45 * 60 + 12) * 1000L;

        countDownTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long totalSeconds = millisUntilFinished / 1000;
                long hours = totalSeconds / 3600;
                long minutes = (totalSeconds % 3600) / 60;
                long seconds = totalSeconds % 60;

                if (tvCountdownHours != null) {
                    tvCountdownHours.setText(String.format(Locale.getDefault(), "%02d", hours));
                }
                if (tvCountdownMinutes != null) {
                    tvCountdownMinutes.setText(String.format(Locale.getDefault(), "%02d", minutes));
                }
                if (tvCountdownSeconds != null) {
                    tvCountdownSeconds.setText(String.format(Locale.getDefault(), "%02d", seconds));
                }
            }

            @Override
            public void onFinish() {
                if (tvCountdownHours != null) tvCountdownHours.setText("00");
                if (tvCountdownMinutes != null) tvCountdownMinutes.setText("00");
                if (tvCountdownSeconds != null) tvCountdownSeconds.setText("00");
                Toast.makeText(FlashGrabActivity.this, "Sự kiện Giựt Deal đã kết thúc!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}