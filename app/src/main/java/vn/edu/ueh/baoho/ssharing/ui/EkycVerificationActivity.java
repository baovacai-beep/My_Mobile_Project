package vn.edu.ueh.baoho.ssharing.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.ueh.baoho.ssharing.R;

public class EkycVerificationActivity extends AppCompatActivity {

    private TextView tvExtractedCccd, tvExtractedHoTen;
    private int currentStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekyc_verification);

        tvExtractedCccd = findViewById(R.id.tv_extracted_so_cccd);
        tvExtractedHoTen = findViewById(R.id.tv_extracted_ho_ten);

        Button btnChupAnh = findViewById(R.id.btn_chup_anh_cccd);
        if (btnChupAnh != null) {
            btnChupAnh.setOnClickListener(v -> {
                if (currentStep == 1) {
                    Toast.makeText(this, "ML Kit: Quét mặt trước CCCD thành công!", Toast.LENGTH_SHORT).show();
                    if (tvExtractedCccd != null) tvExtractedCccd.setText("Số CCCD: 079206001234");
                    if (tvExtractedHoTen != null) tvExtractedHoTen.setText("Họ và tên: HỒ GIA BẢO");
                    currentStep = 2;
                } else if (currentStep == 2) {
                    Toast.makeText(this, "ML Kit: Quét mặt sau CCCD thành công!", Toast.LENGTH_SHORT).show();
                    currentStep = 3;
                } else {
                    Toast.makeText(this, "Face Detection: Quét sinh trắc học khuôn mặt thành công!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        Button btnTiepTuc = findViewById(R.id.btn_tiep_tuc_ekyc);
        if (btnTiepTuc != null) {
            btnTiepTuc.setOnClickListener(v -> {
                Toast.makeText(this, "Xác thực eKYC hoàn tất! Bạn đã nhận Huy hiệu Tín nhiệm Verified User (+20 điểm)", Toast.LENGTH_LONG).show();
                finish();
            });
        }
    }
}