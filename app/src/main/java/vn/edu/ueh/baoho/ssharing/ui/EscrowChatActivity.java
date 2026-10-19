package vn.edu.ueh.baoho.ssharing.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vn.edu.ueh.baoho.ssharing.R;

public class EscrowChatActivity extends AppCompatActivity {

    private LinearLayout khungDanhSáchTinNhắn;
    private ScrollView khungCuộnChat;
    private EditText ôNhậpTinNhắn;
    private Button nútXácNhậnEscrow;
    private Button nútBáoCáoLỗiEscrow;
    private TextView nhãnTrạngTháiEscrow;
    private TextView nhãnTênĐốiTác;
    private TextView nhãnAvatarĐốiTác;
    private TextView nhãnTiêuĐềVoucher;
    private TextView nhãnMãBíMậtVoucher;
    private TextView nhãnThôngBáoHệThốngĐầu;

    private Handler bộĐiềuPhốiĐộTrễ = new Handler(Looper.getMainLooper());

    private String tênNgườiBán = "Minh Triết";
    private String avatarNgườiBán = "MT";
    private String tênVoucher = "Voucher GrabCar Giảm 50K";
    private String mãBíMậtVoucher = "GRAB50K-COMMUNITY88";
    private int giáXuEscrow = 350;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escrow_chat);

        khởiTạoÁnhXạGiaoDiện();
        đọcDữLiệuGiaoDịchTruyềnTới();
        càiĐặtSựKiệnLắngNghe();
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        khungDanhSáchTinNhắn = findViewById(R.id.containerChatMessages);
        khungCuộnChat = findViewById(R.id.scrollChatArea);
        ôNhậpTinNhắn = findViewById(R.id.et_chat_input);
        nútXácNhậnEscrow = findViewById(R.id.btnConfirmEscrow);
        nútBáoCáoLỗiEscrow = findViewById(R.id.btnReportEscrow);
        nhãnTrạngTháiEscrow = findViewById(R.id.tv_escrow_holding_amount);
        nhãnTênĐốiTác = findViewById(R.id.tv_partner_name);
        nhãnAvatarĐốiTác = findViewById(R.id.tvAvatarUser);
        nhãnTiêuĐềVoucher = findViewById(R.id.tvVoucherTitle);
        nhãnMãBíMậtVoucher = findViewById(R.id.tvSecretCodeDisplay);
        nhãnThôngBáoHệThốngĐầu = findViewById(R.id.tvSystemNotice);
    }

    private void đọcDữLiệuGiaoDịchTruyềnTới() {
        Intent dữLiệuÝĐịnh = getIntent();
        if (dữLiệuÝĐịnh != null) {
            if (dữLiệuÝĐịnh.hasExtra("EXTRA_SELLER_NAME")) {
                tênNgườiBán = dữLiệuÝĐịnh.getStringExtra("EXTRA_SELLER_NAME");
            }
            if (dữLiệuÝĐịnh.hasExtra("EXTRA_SELLER_AVATAR")) {
                avatarNgườiBán = dữLiệuÝĐịnh.getStringExtra("EXTRA_SELLER_AVATAR");
            }
            if (dữLiệuÝĐịnh.hasExtra("EXTRA_VOUCHER_TITLE")) {
                tênVoucher = dữLiệuÝĐịnh.getStringExtra("EXTRA_VOUCHER_TITLE");
            }
            if (dữLiệuÝĐịnh.hasExtra("EXTRA_SECRET_CODE")) {
                mãBíMậtVoucher = dữLiệuÝĐịnh.getStringExtra("EXTRA_SECRET_CODE");
            }
            if (dữLiệuÝĐịnh.hasExtra("EXTRA_PRICE_SS")) {
                giáXuEscrow = dữLiệuÝĐịnh.getIntExtra("EXTRA_PRICE_SS", 350);
            }
        }

        if (nhãnTênĐốiTác != null) nhãnTênĐốiTác.setText(tênNgườiBán);
        if (nhãnAvatarĐốiTác != null) nhãnAvatarĐốiTác.setText(avatarNgườiBán);
        if (nhãnTiêuĐềVoucher != null) nhãnTiêuĐềVoucher.setText(tênVoucher);
        if (nhãnMãBíMậtVoucher != null) nhãnMãBíMậtVoucher.setText("Mã bí mật: " + mãBíMậtVoucher);
        if (nhãnTrạngTháiEscrow != null) {
            nhãnTrạngTháiEscrow.setText("Đang tạm giữ: " + String.format("%,d", giáXuEscrow) + " SS");
        }
        if (nhãnThôngBáoHệThốngĐầu != null) {
            nhãnThôngBáoHệThốngĐầu.setText("🛡️ Hệ thống: Người mua đã khóa " + giáXuEscrow + " SS vào ví Escrow. Bạn có 60 phút để kiểm tra mã trước khi giải ngân cho " + tênNgườiBán + ".");
        }
    }

    private void càiĐặtSựKiệnLắngNghe() {
        // Nút Quay lại
        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(vương -> finish());
        }

        // Nút Sao chép mã
        if (findViewById(R.id.btnCopyVoucherCode) != null) {
            findViewById(R.id.btnCopyVoucherCode).setOnClickListener(vương -> {
                ClipboardManager khayNhớTạm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData dữLiệuMã = ClipData.newPlainText("MãVoucher", mãBíMậtVoucher);
                if (khayNhớTạm != null) {
                    khayNhớTạm.setPrimaryClip(dữLiệuMã);
                    Toast.makeText(this, "Đã sao chép mã: " + mãBíMậtVoucher, Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Nút Gửi tin nhắn
        if (findViewById(R.id.btn_send_chat) != null) {
            findViewById(R.id.btn_send_chat).setOnClickListener(vương -> thựcHiệnGửiTinNhắnNgườimua());
        }

        // Nút Xác nhận giải phóng Escrow
        if (nútXácNhậnEscrow != null) {
            nútXácNhậnEscrow.setOnClickListener(vương -> xácNhậnGiảiPhóngEscrow());
        }

        // Nút Báo cáo lỗi / Khiếu nại
        if (nútBáoCáoLỗiEscrow != null) {
            nútBáoCáoLỗiEscrow.setOnClickListener(vương -> {
                Toast.makeText(this, "Chuyển tới Trung Tâm Tiếp Nhận Khiếu Nại Ký Quỹ...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, DisputeClaimActivity.class));
            });
        }
    }

    private void thựcHiệnGửiTinNhắnNgườimua() {
        if (ôNhậpTinNhắn == null) return;
        String nộiDung = ôNhậpTinNhắn.getText().toString().trim();
        if (nộiDung.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung tin nhắn!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo Bong bóng chat Người mua (phải)
        LinearLayout bongBóngNgườiMua = new LinearLayout(this);
        bongBóngNgườiMua.setOrientation(LinearLayout.VERTICAL);
        bongBóngNgườiMua.setBackgroundResource(R.drawable.bg_bubble_buyer);
        bongBóngNgườiMua.setPadding(24, 24, 24, 24);

        LinearLayout.LayoutParams thôngSốLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốLayout.gravity = Gravity.END;
        thôngSốLayout.topMargin = 16;
        bongBóngNgườiMua.setLayoutParams(thôngSốLayout);

        TextView nhãnNộiDung = new TextView(this);
        nhãnNộiDung.setText(nộiDung);
        nhãnNộiDung.setTextColor(Color.WHITE);
        nhãnNộiDung.setTextSize(13);

        TextView nhãnThờiGian = new TextView(this);
        String thờiGianHiệnTại = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        nhãnThờiGian.setText(thờiGianHiệnTại + " ✓✓");
        nhãnThờiGian.setTextColor(Color.parseColor("#D1C4E9"));
        nhãnThờiGian.setTextSize(10);
        LinearLayout.LayoutParams thôngSốThờiGian = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốThờiGian.gravity = Gravity.END;
        thôngSốThờiGian.topMargin = 6;
        nhãnThờiGian.setLayoutParams(thôngSốThờiGian);

        bongBóngNgườiMua.addView(nhãnNộiDung);
        bongBóngNgườiMua.addView(nhãnThờiGian);

        if (khungDanhSáchTinNhắn != null) {
            khungDanhSáchTinNhắn.addView(bongBóngNgườiMua);
        }

        ôNhậpTinNhắn.setText("");
        cuộnXuốngĐáyChat();

        // Tự động phản hồi từ Người bán tương ứng sau 1.5 giây
        bộĐiềuPhốiĐộTrễ.postDelayed(this::tựĐộngPhảnHồiNgườiBán, 1500);
    }

    private void tựĐộngPhảnHồiNgườiBán() {
        LinearLayout khungHàngNgườiBán = new LinearLayout(this);
        khungHàngNgườiBán.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams thôngSốHàng = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốHàng.topMargin = 16;
        khungHàngNgườiBán.setLayoutParams(thôngSốHàng);

        // Avatar Người bán
        TextView ảnhĐạiDiện = new TextView(this);
        ảnhĐạiDiện.setText(avatarNgườiBán);
        ảnhĐạiDiện.setTextColor(Color.WHITE);
        ảnhĐạiDiện.setTextSize(12);
        ảnhĐạiDiện.setGravity(Gravity.CENTER);
        ảnhĐạiDiện.setBackgroundResource(R.drawable.bg_circle_dark_purple);
        LinearLayout.LayoutParams thôngSốAvatar = new LinearLayout.LayoutParams(64, 64);
        ảnhĐạiDiện.setLayoutParams(thôngSốAvatar);

        // Bong bóng Người bán
        LinearLayout bongBóngNgườiBán = new LinearLayout(this);
        bongBóngNgườiBán.setOrientation(LinearLayout.VERTICAL);
        bongBóngNgườiBán.setBackgroundResource(R.drawable.bg_bubble_seller);
        bongBóngNgườiBán.setPadding(24, 24, 24, 24);
        LinearLayout.LayoutParams thôngSốBongBóng = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốBongBóng.leftMargin = 16;
        bongBóngNgườiBán.setLayoutParams(thôngSốBongBóng);

        TextView nhãnNộiDung = new TextView(this);
        nhãnNộiDung.setText("Cảm ơn bạn đã mua " + tênVoucher + "! Bạn nhập mã " + mãBíMậtVoucher + " vào app áp dụng ngay nhé 😊");
        nhãnNộiDung.setTextColor(Color.WHITE);
        nhãnNộiDung.setTextSize(13);

        TextView nhãnThờiGian = new TextView(this);
        String thờiGianHiệnTại = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        nhãnThờiGian.setText(thờiGianHiệnTại);
        nhãnThờiGian.setTextColor(Color.parseColor("#78718C"));
        nhãnThờiGian.setTextSize(10);
        LinearLayout.LayoutParams thôngSốThờiGian = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốThờiGian.gravity = Gravity.END;
        thôngSốThờiGian.topMargin = 6;
        nhãnThờiGian.setLayoutParams(thôngSốThờiGian);

        bongBóngNgườiBán.addView(nhãnNộiDung);
        bongBóngNgườiBán.addView(nhãnThờiGian);

        khungHàngNgườiBán.addView(ảnhĐạiDiện);
        khungHàngNgườiBán.addView(bongBóngNgườiBán);

        if (khungDanhSáchTinNhắn != null) {
            khungDanhSáchTinNhắn.addView(khungHàngNgườiBán);
        }
        cuộnXuốngĐáyChat();
    }

    private void xácNhậnGiảiPhóngEscrow() {
        new AlertDialog.Builder(this)
                .setTitle("Xác Nhận Giải Phong Xu Escrow")
                .setMessage("Bạn đã kiểm tra và xác nhận mã " + tênVoucher + " hoạt động tốt? Hệ thống sẽ giải phóng " + giáXuEscrow + " SS cho " + tênNgườiBán + ".")
                .setPositiveButton("Giải phóng ngay", (hộpThoại, vịTrí) -> thựcHiệnHoànTấtEscrow())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void thựcHiệnHoànTấtEscrow() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        int sốDưHiệnTại = bộLưuTrữVí.getInt("SO_DU_XU_SS", 1250);
        int sốDưMới = Math.max(0, sốDưHiệnTại - giáXuEscrow);
        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        if (nhãnTrạngTháiEscrow != null) {
            nhãnTrạngTháiEscrow.setText("✓ Đã giải phóng: " + giáXuEscrow + " SS cho " + tênNgườiBán);
            nhãnTrạngTháiEscrow.setTextColor(Color.parseColor("#00E676"));
        }

        // 1. Thêm vào Ví Voucher
        String danhSáchVoucherĐãLưu = bộLưuTrữVí.getString("DANH_SACH_VOUCHER_DA_LUU", "");
        String voucherMớiThêm = mãBíMậtVoucher + "::" + tênVoucher + "::HSD: 31 T12 2026::C2C";
        String danhSáchVoucherMới = voucherMớiThêm + (danhSáchVoucherĐãLưu.isEmpty() ? "" : ";;" + danhSáchVoucherĐãLưu);

        // 2. Thêm vào Lịch Sử Giao Dịch
        String danhSáchGiaoDịchĐãLưu = bộLưuTrữVí.getString("DANH_SACH_GIAO_DICH", "");
        String mãGiaoDịchRandom = "TX-ESC" + (100000 + (int)(Math.random() * 899999));
        String bảnGhiGiaoDịchMới = mãGiaoDịchRandom + "||Giải phóng Escrow " + tênVoucher + "||-" + giáXuEscrow + " SS||" + thờiGianThực + "||Thanh toán Escrow||Thành công||Giải phóng " + giáXuEscrow + " SS cho người bán " + tênNgườiBán;
        String danhSáchGiaoDịchMới = bảnGhiGiaoDịchMới + (danhSáchGiaoDịchĐãLưu.isEmpty() ? "" : ";;" + danhSáchGiaoDịchĐãLưu);

        bộLưuTrữVí.edit()
                .putInt("SO_DU_XU_SS", sốDưMới)
                .putString("DANH_SACH_VOUCHER_DA_LUU", danhSáchVoucherMới)
                .putString("DANH_SACH_GIAO_DICH", danhSáchGiaoDịchMới)
                .apply();

        // Thêm thông báo hệ thống vào Chat
        TextView thôngBáoHệThống = new TextView(this);
        thôngBáoHệThống.setText("🛡️ Hệ thống: Giao dịch hoàn tất thành công! " + giáXuEscrow + " SS đã được giải ngân cho " + tênNgườiBán + ".");
        thôngBáoHệThống.setTextColor(Color.parseColor("#6EE7B7"));
        thôngBáoHệThống.setBackgroundResource(R.drawable.bg_card_dark);
        thôngBáoHệThống.setPadding(20, 20, 20, 20);
        thôngBáoHệThống.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams thôngSốThôngBáo = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốThôngBáo.topMargin = 20;
        thôngBáoHệThống.setLayoutParams(thôngSốThôngBáo);

        if (khungDanhSáchTinNhắn != null) {
            khungDanhSáchTinNhắn.addView(thôngBáoHệThống);
        }
        cuộnXuốngĐáyChat();

        if (nútXácNhậnEscrow != null) {
            nútXácNhậnEscrow.setEnabled(false);
            nútXácNhậnEscrow.setText("✓ Đã Giải Phóng Xu");
        }
        if (nútBáoCáoLỗiEscrow != null) {
            nútBáoCáoLỗiEscrow.setVisibility(Button.GONE);
        }

        Toast.makeText(this, "🎉 Giải phóng Escrow " + giáXuEscrow + " SS thành công!", Toast.LENGTH_LONG).show();
    }

    private void cuộnXuốngĐáyChat() {
        if (khungCuộnChat != null) {
            khungCuộnChat.post(() -> khungCuộnChat.fullScroll(ScrollView.FOCUS_DOWN));
        }
    }
}
