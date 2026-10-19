package vn.edu.ueh.baoho.ssharing.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.ueh.baoho.ssharing.R;

public class EscrowChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escrow_chat);

        // Quay lại
        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        // Sao chép mã voucher
        if (findViewById(R.id.btnCopyVoucherCode) != null) {
            findViewById(R.id.btnCopyVoucherCode).setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("VoucherCode", "SP50K-OCTOBER99");
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(this, "Đã sao chép mã: SP50K-OCTOBER99", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Nút gửi tin nhắn
        if (findViewById(R.id.btn_send_chat) != null) {
            findViewById(R.id.btn_send_chat).setOnClickListener(v -> {
                EditText etInput = findViewById(R.id.et_chat_input);
                if (etInput != null && etInput.getText() != null) {
                    String msg = etInput.getText().toString().trim();
                    if (!msg.isEmpty()) {
                        Toast.makeText(this, "Đã gửi tin nhắn: " + msg, Toast.LENGTH_SHORT).show();
                        etInput.setText("");
                    }
                }
            });
        }

        // Nút xác nhận mã thành công (Giải phóng Xu)
        if (findViewById(R.id.btnConfirmEscrow) != null) {
            findViewById(R.id.btnConfirmEscrow).setOnClickListener(v ->
                    Toast.makeText(this, "Giao dịch thành công! Số Xu ký quỹ đã được chuyển cho người bán.", Toast.LENGTH_LONG).show()
            );
        }

        // Nút báo cáo lỗi / khiếu nại -> Mở DisputeClaimActivity
        if (findViewById(R.id.btnReportEscrow) != null) {
            findViewById(R.id.btnReportEscrow).setOnClickListener(v ->
                    startActivity(new Intent(this, DisputeClaimActivity.class))
            );
        }
    }
}
