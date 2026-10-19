package vn.edu.ueh.baoho.ssharing.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.ueh.baoho.ssharing.R;

public class InstantBuybackActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_buyback);

        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        if (findViewById(R.id.btnConfirmBuyback) != null) {
            findViewById(R.id.btnConfirmBuyback).setOnClickListener(v -> {
                Toast.makeText(this, "Đổi xu tức thì thành công!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}