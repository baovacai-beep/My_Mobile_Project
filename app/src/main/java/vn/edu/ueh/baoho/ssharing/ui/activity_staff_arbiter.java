package vn.edu.ueh.baoho.ssharing.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.ueh.baoho.ssharing.R;

public class activity_staff_arbiter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_arbiter);

        View btnBack = findViewById(R.id.btnBackStaffArbiter);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        View btnRefundBuyer = findViewById(R.id.btnRefundBuyer);
        if (btnRefundBuyer != null) {
            btnRefundBuyer.setOnClickListener(v -> {
                Toast.makeText(this, "Phán quyết: Đã chấp thuận hoàn xu cho người mua!", Toast.LENGTH_LONG).show();
                finish();
            });
        }

        View btnReleaseSeller = findViewById(R.id.btnReleaseSeller);
        if (btnReleaseSeller != null) {
            btnReleaseSeller.setOnClickListener(v -> {
                Toast.makeText(this, "Phán quyết: Bác khiếu nại - Đã giải ngân cho người bán!", Toast.LENGTH_LONG).show();
                finish();
            });
        }
    }
}