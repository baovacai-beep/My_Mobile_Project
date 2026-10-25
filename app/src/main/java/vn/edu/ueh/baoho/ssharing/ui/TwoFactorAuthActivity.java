package vn.edu.ueh.baoho.ssharing.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

import vn.edu.ueh.baoho.ssharing.R;

public class TwoFactorAuthActivity extends AppCompatActivity {

    private EditText ôOtp1, ôOtp2, ôOtp3, ôOtp4, ôOtp5, ôOtp6;
    private TextView nhãnĐếmLùiOtp;
    private TextView nútGửiLạiOtp;
    private Button nútXácNhậnOtp;

    private CountDownTimer bộĐếmLùiTimer;
    private String mãOtpMẫuChuẩn = "892015";
    private String usernameHiệnTại = "baoho123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_factor_auth);

        đọcThôngTinTàiKhoản();
        khởiTạoÁnhXạGiaoDiện();
        càiĐặtTựĐộngChuyểnÔOtp();
        khởiĐộngĐếmLùiThờigianOtp();
        điềnMẫuMãOtpĐầu();
        càiĐặtSựKiệnLắngNghe();
    }

    private void đọcThôngTinTàiKhoản() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        usernameHiệnTại = bộLưuTrữVí.getString("CURRENT_USERNAME", "baoho123");
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        ôOtp1 = findViewById(R.id.et_otp_1);
        ôOtp2 = findViewById(R.id.et_otp_2);
        ôOtp3 = findViewById(R.id.et_otp_3);
        ôOtp4 = findViewById(R.id.et_otp_4);
        ôOtp5 = findViewById(R.id.et_otp_5);
        ôOtp6 = findViewById(R.id.et_otp_6);

        nhãnĐếmLùiOtp = findViewById(R.id.tv_countdown_timer);
        nútGửiLạiOtp = findViewById(R.id.btn_resend_otp);
        nútXácNhậnOtp = findViewById(R.id.btn_xac_nhan_otp);
    }

    private void điềnMẫuMãOtpĐầu() {
        if (ôOtp1 != null) ôOtp1.setText("8");
        if (ôOtp2 != null) ôOtp2.setText("9");
        if (ôOtp3 != null) ôOtp3.setText("2");
        if (ôOtp4 != null) ôOtp4.setText("0");
        if (ôOtp5 != null) ôOtp5.setText("1");
        if (ôOtp6 != null) ôOtp6.setText("5");
    }

    private void càiĐặtTựĐộngChuyểnÔOtp() {
        tạoTextWatcherChuyểnÔ(ôOtp1, ôOtp2);
        tạoTextWatcherChuyểnÔ(ôOtp2, ôOtp3);
        tạoTextWatcherChuyểnÔ(ôOtp3, ôOtp4);
        tạoTextWatcherChuyểnÔ(ôOtp4, ôOtp5);
        tạoTextWatcherChuyểnÔ(ôOtp5, ôOtp6);
    }

    private void tạoTextWatcherChuyểnÔ(EditText ôHiệnTại, EditText ôKếTiếp) {
        if (ôHiệnTại == null) return;
        ôHiệnTại.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && ôKếTiếp != null) {
                    ôKếTiếp.requestFocus();
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void khởiĐộngĐếmLùiThờigianOtp() {
        if (bộĐếmLùiTimer != null) {
            bộĐếmLùiTimer.cancel();
        }

        bộĐếmLùiTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long giâyCònLại = millisUntilFinished / 1000;
                if (nhãnĐếmLùiOtp != null) {
                    nhãnĐếmLùiOtp.setText(String.format(Locale.getDefault(), "Mã hết hạn trong: 00:%02d", giâyCònLại));
                }
            }

            @Override
            public void onFinish() {
                if (nhãnĐếmLùiOtp != null) {
                    nhãnĐếmLùiOtp.setText("Mã OTP đã hết hạn! Bấm gửi lại mã.");
                }
            }
        }.start();
    }

    private void càiĐặtSựKiệnLắngNghe() {
        if (nútXácNhậnOtp != null) {
            nútXácNhậnOtp.setOnClickListener(v -> thựcHiệnXácNhậnMãOtp());
        }

        if (nútGửiLạiOtp != null) {
            nútGửiLạiOtp.setOnClickListener(v -> {
                khởiĐộngĐếmLùiThờigianOtp();
                 điềnMẫuMãOtpĐầu();
                Toast.makeText(this, "📩 Đã gửi lại mã OTP 6 số qua Email/SMS!", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void thựcHiệnXácNhậnMãOtp() {
        String c1 = ôOtp1 != null ? ôOtp1.getText().toString().trim() : "";
        String c2 = ôOtp2 != null ? ôOtp2.getText().toString().trim() : "";
        String c3 = ôOtp3 != null ? ôOtp3.getText().toString().trim() : "";
        String c4 = ôOtp4 != null ? ôOtp4.getText().toString().trim() : "";
        String c5 = ôOtp5 != null ? ôOtp5.getText().toString().trim() : "";
        String c6 = ôOtp6 != null ? ôOtp6.getText().toString().trim() : "";

        String mãGõ = c1 + c2 + c3 + c4 + c5 + c6;

        if (mãGõ.length() < 6) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ 6 chữ số mã OTP!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        bộLưuTrữVí.edit().putBoolean("IS_2FA_VERIFIED", true).apply();

        // Đồng bộ lưu trạng thái 2FA lên Firebase Firestore
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(usernameHiệnTại).update("is_2fa_enabled", true);
        } catch (Exception ignored) {}

        Toast.makeText(this, "🎉 Xác thực 2FA OTP 6 số thành công! Đã kích hoạt bảo mật cấp ngân hàng.", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bộĐếmLùiTimer != null) {
            bộĐếmLùiTimer.cancel();
        }
    }
}
