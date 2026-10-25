package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.ueh.baoho.ssharing.R;

public class NotificationCenterActivity extends AppCompatActivity {

    public static class ThôngTinThôngBáoModel {
        public String nộiDung;
        public String thờiGian;
        public String loạiIcon;

        public ThôngTinThôngBáoModel(String nộiDung, String thờiGian, String loạiIcon) {
            this.nộiDung = nộiDung;
            this.thờiGian = thờiGian;
            this.loạiIcon = loạiIcon;
        }
    }

    private LinearLayout containerDanhSáchThôngBáo;
    private String usernameHiệnTại = "baoho123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_center);

        đọcThôngTinTàiKhoản();
        khởiTạoÁnhXạGiaoDiện();
        đánhDấuĐãĐọcToànBộThôngBáo();
        tảiDanhSáchThôngBáoThựcTế();
        càiĐặtSựKiệnLắngNghe();
    }

    private void đọcThôngTinTàiKhoản() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        usernameHiệnTại = bộLưuTrữVí.getString("CURRENT_USERNAME", "baoho123");
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        containerDanhSáchThôngBáo = findViewById(R.id.containerNotificationList);
    }

    private void đánhDấuĐãĐọcToànBộThôngBáo() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String userClean = usernameHiệnTại.toLowerCase().trim();
        bộLưuTrữVí.edit().putBoolean("HAS_UNREAD_NOTIFICATIONS_" + userClean, false).apply();
    }

    private void càiĐặtSựKiệnLắngNghe() {
        View nútQuayLại = findViewById(R.id.btnBackNotifications);
        if (nútQuayLại != null) {
            nútQuayLại.setOnClickListener(v -> finish());
        }

        View nútĐọcHết = findViewById(R.id.btnClearAllNotifications);
        if (nútĐọcHết != null) {
            nútĐọcHết.setOnClickListener(v -> finish());
        }
    }

    private void tảiDanhSáchThôngBáoThựcTế() {
        if (containerDanhSáchThôngBáo == null) return;
        containerDanhSáchThôngBáo.removeAllViews();

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String userClean = usernameHiệnTại.toLowerCase().trim();
        String chuỗiThôngBáoGốc = bộLưuTrữVí.getString("DANH_SACH_THONG_BAO_" + userClean, "");

        List<ThôngTinThôngBáoModel> danhSáchTB = new ArrayList<>();

        if (!chuỗiThôngBáoGốc.isEmpty()) {
            String[] cácTB = chuỗiThôngBáoGốc.split(";;");
            for (String tb : cácTB) {
                if (tb.trim().isEmpty()) continue;
                String[] mảng = tb.split("\\|\\|");
                String nộiDung = mảng[0];
                String thờiGian = mảng.length > 1 ? mảng[1] : "Vừa xong";
                String icon = "📌";

                if (nộiDung.contains("GIAO DỊCH") || nộiDung.contains("MUA")) icon = "🛒";
                else if (nộiDung.contains("THEO DÕI") || nộiDung.contains("BẠN BÈ")) icon = "👤";
                else if (nộiDung.contains("TRADE") || nộiDung.contains("SWAP")) icon = "🤝";
                else if (nộiDung.contains("KHIẾU NẠI") || nộiDung.contains("TRỌNG TÀI")) icon = "⚖️";

                danhSáchTB.add(new ThôngTinThôngBáoModel(nộiDung, thờiGian, icon));
            }
        }

        // Bổ sung các thông báo hệ thống & nhãn hàng nổi bật
        danhSáchTB.add(new ThôngTinThôngBáoModel("🔥 [HOT DEAL 0Đ] Sự kiện Giựt Deal Khung Giờ Vàng 12h:00 đã mở! Giựt ngay voucher Grab 100K chỉ 10 Xu!", "12:00 Hôm nay", "🔥"));
        danhSáchTB.add(new ThôngTinThôngBáoModel("📢 [CỔNG NẠP VIETQR] SSharing chính thức kích hoạt Cổng nạp VietQR Napas247 MBBank STK 0913672861 - Thưởng lên đến +20% Xu!", "09:00 Hôm nay", "🏦"));
        danhSáchTB.add(new ThôngTinThôngBáoModel("🎉 [TÍN NHIỆM KIM CƯƠNG] Tài khoản của bạn đã đạt 96/100 điểm Trust Score - eKYC Verified!", "Hôm qua", "💎"));

        for (ThôngTinThôngBáoModel item : danhSáchTB) {
            CardView thẻTB = new CardView(this);
            thẻTB.setCardBackgroundColor(Color.parseColor("#161327"));
            thẻTB.setRadius(24);
            thẻTB.setCardElevation(0);

            LinearLayout.LayoutParams thôngSốThẻ = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            thôngSốThẻ.bottomMargin = 14;
            thẻTB.setLayoutParams(thôngSốThẻ);

            LinearLayout khungNộiDung = new LinearLayout(this);
            khungNộiDung.setOrientation(LinearLayout.HORIZONTAL);
            khungNộiDung.setPadding(20, 20, 20, 20);
            khungNộiDung.setGravity(android.view.Gravity.CENTER_VERTICAL);

            TextView ôIcon = new TextView(this);
            ôIcon.setText(item.loạiIcon);
            ôIcon.setTextSize(20);
            ôIcon.setGravity(android.view.Gravity.CENTER);

            LinearLayout khungChữ = new LinearLayout(this);
            khungChữ.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams thôngSốChữ = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            thôngSốChữ.leftMargin = 16;
            khungChữ.setLayoutParams(thôngSốChữ);

            TextView nhãnNộiDung = new TextView(this);
            nhãnNộiDung.setText(item.nộiDung);
            nhãnNộiDung.setTextColor(Color.WHITE);
            nhãnNộiDung.setTextSize(13);
            nhãnNộiDung.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView nhãnMốcThờiGian = new TextView(this);
            nhãnMốcThờiGian.setText("⏱ " + item.thờiGian);
            nhãnMốcThờiGian.setTextColor(Color.parseColor("#A09CAB"));
            nhãnMốcThờiGian.setTextSize(10);
            nhãnMốcThờiGian.setPadding(0, 4, 0, 0);

            khungChữ.addView(nhãnNộiDung);
            khungChữ.addView(nhãnMốcThờiGian);

            khungNộiDung.addView(ôIcon);
            khungNộiDung.addView(khungChữ);

            thẻTB.addView(khungNộiDung);

            // Điều hướng tương ứng loại thông báo
            thẻTB.setOnClickListener(v -> {
                if (item.nộiDung.contains("BẠN BÈ") || item.nộiDung.contains("THEO DÕI")) {
                    startActivity(new Intent(this, SocialFriendsActivity.class));
                } else if (item.nộiDung.contains("TRADE") || item.nộiDung.contains("SWAP")) {
                    startActivity(new Intent(this, P2PVoucherTradeActivity.class));
                } else if (item.nộiDung.contains("DEAL") || item.nộiDung.contains("HOT")) {
                    startActivity(new Intent(this, FlashGrabActivity.class));
                } else if (item.nộiDung.contains("VIETQR") || item.nộiDung.contains("NẠP")) {
                    startActivity(new Intent(this, CoinTopupActivity.class));
                } else {
                    startActivity(new Intent(this, ChatInboxActivity.class));
                }
            });

            containerDanhSáchThôngBáo.addView(thẻTB);
        }
    }
}
