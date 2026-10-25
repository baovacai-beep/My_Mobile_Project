package vn.edu.ueh.baoho.ssharing.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.edu.ueh.baoho.ssharing.R;

public class SocialFriendsActivity extends AppCompatActivity {

    public static class ThôngTinThànhViênSocial {
        public String username;
        public String họTên;
        public String avatarInitials;
        public int điểmTrustScore;
        public boolean làVerified;

        public ThôngTinThànhViênSocial(String username, String họTên, String avatarInitials, int điểmTrustScore, boolean làVerified) {
            this.username = username;
            this.họTên = họTên;
            this.avatarInitials = avatarInitials;
            this.điểmTrustScore = điểmTrustScore;
            this.làVerified = làVerified;
        }
    }

    private EditText ôNhậpTìmKiếmUser;
    private TextView nhãnSốĐangTheoDõi;
    private TextView nhãnSốBạnBè2Chiều;
    private LinearLayout containerDanhSáchThànhViên;

    private String usernameHiệnTại = "baoho123";
    private List<ThôngTinThànhViênSocial> danhSáchTấtCảThànhViên = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_friends);

        đọcThôngTinTàiKhoản();
        khởiTạoÁnhXạGiaoDiện();
        khởiTạoKhoThànhViênMẫu();
        càiĐặtSựKiệnLắngNghe();
        cậpNhậtThốngKêVàVẽDanhSáchthànhViên("");
    }

    private void đọcThôngTinTàiKhoản() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        usernameHiệnTại = bộLưuTrữVí.getString("CURRENT_USERNAME", "baoho123");
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        ôNhậpTìmKiếmUser = findViewById(R.id.etSearchUser);
        nhãnSốĐangTheoDõi = findViewById(R.id.tvFollowingCount);
        nhãnSốBạnBè2Chiều = findViewById(R.id.tvMutualFriendsCount);
        containerDanhSáchThànhViên = findViewById(R.id.containerUserList);
    }

    private void khởiTạoKhoThànhViênMẫu() {
        danhSáchTấtCảThànhViên.clear();
        danhSáchTấtCảThànhViên.add(new ThôngTinThànhViênSocial("Beo123", "Gia Beo", "GB", 98, true));
        danhSáchTấtCảThànhViên.add(new ThôngTinThànhViênSocial("minh.triet", "Minh Triết", "MT", 96, true));
        danhSáchTấtCảThànhViên.add(new ThôngTinThànhViênSocial("bao.ngoc", "Bảo Ngọc", "BN", 94, true));
        danhSáchTấtCảThànhViên.add(new ThôngTinThànhViênSocial("khanh.trinh", "Khánh Trình", "KT", 92, true));
        danhSáchTấtCảThànhViên.add(new ThôngTinThànhViênSocial("gia.bao", "Gia Bảo", "GB", 90, false));
        danhSáchTấtCảThànhViên.add(new ThôngTinThànhViênSocial("hoang.long", "Hoàng Long", "HL", 88, false));
    }

    private void càiĐặtSựKiệnLắngNghe() {
        View nútQuayLại = findViewById(R.id.btnBackSocialFriends);
        if (nútQuayLại != null) {
            nútQuayLại.setOnClickListener(v -> finish());
        }

        if (ôNhậpTìmKiếmUser != null) {
            ôNhậpTìmKiếmUser.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    cậpNhậtThốngKêVàVẽDanhSáchthànhViên(s.toString().trim());
                }
                @Override public void afterTextChanged(Editable s) {}
            });
        }

        View nútBấmTìm = findViewById(R.id.btnDoSearchUser);
        if (nútBấmTìm != null && ôNhậpTìmKiếmUser != null) {
            nútBấmTìm.setOnClickListener(v ->
                    cậpNhậtThốngKêVàVẽDanhSáchthànhViên(ôNhậpTìmKiếmUser.getText().toString().trim())
            );
        }
    }

    private void cậpNhậtThốngKêVàVẽDanhSáchthànhViên(String từKhóa) {
        if (containerDanhSáchThànhViên == null) return;
        containerDanhSáchThànhViên.removeAllViews();

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String chuỗiĐangTheoDõi = bộLưuTrữVí.getString("FOLLOWING_LIST_" + usernameHiệnTại.toLowerCase().trim(), "Beo123;;minh.triet");

        int sốLượngĐangTheoDõi = 0;
        int sốLượngBạnBè2Chiều = 0;

        for (ThôngTinThànhViênSocial thànhViên : danhSáchTấtCảThànhViên) {
            if (thànhViên.username.equalsIgnoreCase(usernameHiệnTại)) continue;

            if (!từKhóa.isEmpty()) {
                boolean khớpUser = thànhViên.username.toLowerCase().contains(từKhóa.toLowerCase());
                boolean khớpTên = thànhViên.họTên.toLowerCase().contains(từKhóa.toLowerCase());
                if (!khớpUser && !khớpTên) continue;
            }

            boolean làĐangTheoDõi = chuỗiĐangTheoDõi.contains(thànhViên.username.toLowerCase());
            // Giả lập đối tác cũng theo dõi lại mình -> Trở thành Bạn Bè 2 chiều
            boolean làBạnBè2Chiều = làĐangTheoDõi && ("Beo123".equalsIgnoreCase(thànhViên.username) || "minh.triet".equalsIgnoreCase(thànhViên.username));

            if (làĐangTheoDõi) sốLượngĐangTheoDõi++;
            if (làBạnBè2Chiều) sốLượngBạnBè2Chiều++;

            // Vẽ Card Thành Viên
            CardView thẻUser = new CardView(this);
            thẻUser.setCardBackgroundColor(Color.parseColor("#161327"));
            thẻUser.setRadius(24);
            thẻUser.setCardElevation(0);

            LinearLayout.LayoutParams thôngSốThẻ = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            thôngSốThẻ.bottomMargin = 16;
            thẻUser.setLayoutParams(thôngSốThẻ);

            LinearLayout hàngToànBộ = new LinearLayout(this);
            hàngToànBộ.setOrientation(LinearLayout.HORIZONTAL);
            hàngToànBộ.setPadding(20, 20, 20, 20);
            hàngToànBộ.setGravity(android.view.Gravity.CENTER_VERTICAL);

            // Frame Avatar
            android.widget.FrameLayout khungAvatar = new android.widget.FrameLayout(this);
            LinearLayout.LayoutParams thôngSốAvatar = new LinearLayout.LayoutParams(88, 88);
            khungAvatar.setLayoutParams(thôngSốAvatar);

            TextView ôAvatarText = new TextView(this);
            ôAvatarText.setText(thànhViên.avatarInitials);
            ôAvatarText.setTextColor(Color.WHITE);
            ôAvatarText.setTextSize(13);
            ôAvatarText.setTypeface(null, android.graphics.Typeface.BOLD);
            ôAvatarText.setGravity(android.view.Gravity.CENTER);
            ôAvatarText.setBackgroundResource(R.drawable.bg_circle_purple);
            android.widget.FrameLayout.LayoutParams thôngSốMatch = new android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT
            );
            ôAvatarText.setLayoutParams(thôngSốMatch);
            khungAvatar.addView(ôAvatarText);

            String pathAvatar = bộLưuTrữVí.getString("CURRENT_USER_AVATAR_PATH_" + thànhViên.username.toLowerCase().trim(), "");
            if (!pathAvatar.isEmpty() && new File(pathAvatar).exists()) {
                try {
                    android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(pathAvatar);
                    if (bitmap != null) {
                        ImageView imgUser = new ImageView(this);
                        imgUser.setImageBitmap(bitmap);
                        imgUser.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imgUser.setLayoutParams(thôngSốMatch);
                        khungAvatar.addView(imgUser);
                    }
                } catch (Throwable ignored) {}
            }

            // Thông tin Username & Họ tên
            LinearLayout khungMôTả = new LinearLayout(this);
            khungMôTả.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams thôngSốMôTả = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            thôngSốMôTả.leftMargin = 16;
            thôngSốMôTả.rightMargin = 10;
            khungMôTả.setLayoutParams(thôngSốMôTả);

            TextView nhãnTên = new TextView(this);
            nhãnTên.setText(thànhViên.họTên + " " + (thànhViên.làVerified ? "💎" : ""));
            nhãnTên.setTextColor(Color.WHITE);
            nhãnTên.setTextSize(14);
            nhãnTên.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView nhãnSub = new TextView(this);
            nhãnSub.setText("@" + thànhViên.username + " • Trust: " + thànhViên.điểmTrustScore + " điểm");
            nhãnSub.setTextColor(Color.parseColor("#A09CAB"));
            nhãnSub.setTextSize(11);

            khungMôTả.addView(nhãnTên);
            khungMôTả.addView(nhãnSub);

            // Cụm Nút Thao Tác 3 Chức Năng: Chat, Trade & Theo Dõi/Kết Bạn
            LinearLayout cụmNútThaoTác = new LinearLayout(this);
            cụmNútThaoTác.setOrientation(LinearLayout.HORIZONTAL);
            cụmNútThaoTác.setGravity(android.view.Gravity.CENTER_VERTICAL);

            // Nút 1: Nhắn tin trực tiếp
            Button nútNhắnTin = new Button(this);
            LinearLayout.LayoutParams thôngSốMởChat = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    84
            );
            thôngSốMởChat.rightMargin = 8;
            nútNhắnTin.setLayoutParams(thôngSốMởChat);
            nútNhắnTin.setText("💬 Chat");
            nútNhắnTin.setTextColor(Color.WHITE);
            nútNhắnTin.setTextSize(10);
            nútNhắnTin.setPadding(12, 0, 12, 0);
            nútNhắnTin.setBackgroundResource(R.drawable.bg_btn_purple);
            nútNhắnTin.setOnClickListener(v -> {
                android.content.Intent ýĐịnhChat = new android.content.Intent(this, EscrowChatActivity.class);
                ýĐịnhChat.putExtra("EXTRA_SELLER_NAME", thànhViên.họTên + " (@" + thànhViên.username + ")");
                ýĐịnhChat.putExtra("EXTRA_SELLER_AVATAR", thànhViên.avatarInitials);
                ýĐịnhChat.putExtra("EXTRA_VOUCHER_TITLE", "Trò chuyện giao dịch P2P cộng đồng");
                ýĐịnhChat.putExtra("EXTRA_SECRET_CODE", "CHAT-" + thànhViên.username.toUpperCase());
                ýĐịnhChat.putExtra("EXTRA_PRICE_SS", 0);
                startActivity(ýĐịnhChat);
            });

            // Nút 2: Bàn Trade Voucher P2P
            Button nútBànTrade = new Button(this);
            LinearLayout.LayoutParams thôngSốMởTrade = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    84
            );
            thôngSốMởTrade.rightMargin = 8;
            nútBànTrade.setLayoutParams(thôngSốMởTrade);
            nútBànTrade.setText("🤝 Trade");
            nútBànTrade.setTextColor(Color.WHITE);
            nútBànTrade.setTextSize(10);
            nútBànTrade.setPadding(12, 0, 12, 0);
            nútBànTrade.setBackgroundResource(R.drawable.bg_outline_button);
            nútBànTrade.setOnClickListener(v -> {
                android.content.Intent ýĐịnhTrade = new android.content.Intent(this, P2PVoucherTradeActivity.class);
                ýĐịnhTrade.putExtra("EXTRA_TARGET_USERNAME", thànhViên.username);
                startActivity(ýĐịnhTrade);
            });

            // Nút 3: Theo Dõi / Bạn Bè
            Button nútTheoDõi = new Button(this);
            LinearLayout.LayoutParams thôngSốNút = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    84
            );
            nútTheoDõi.setLayoutParams(thôngSốNút);
            nútTheoDõi.setTextSize(10);
            nútTheoDõi.setPadding(12, 0, 12, 0);

            if (làBạnBè2Chiều) {
                nútTheoDõi.setText("🤝 Bạn Bè");
                nútTheoDõi.setTextColor(Color.parseColor("#00E676"));
                nútTheoDõi.setBackgroundResource(R.drawable.bg_card_dark);
            } else if (làĐangTheoDõi) {
                nútTheoDõi.setText("✓ Đang Theo Dõi");
                nútTheoDõi.setTextColor(Color.WHITE);
                nútTheoDõi.setBackgroundResource(R.drawable.bg_card_dark);
            } else {
                nútTheoDõi.setText("+ Theo Dõi");
                nútTheoDõi.setTextColor(Color.WHITE);
                nútTheoDõi.setBackgroundResource(R.drawable.bg_btn_purple);
            }

            nútTheoDõi.setOnClickListener(v -> thựcHiệnThayĐổiTrạngTháiTheoDõi(thànhViên, làĐangTheoDõi, chuỗiĐangTheoDõi));

            cụmNútThaoTác.addView(nútNhắnTin);
            cụmNútThaoTác.addView(nútBànTrade);
            cụmNútThaoTác.addView(nútTheoDõi);

            hàngToànBộ.addView(khungAvatar);
            hàngToànBộ.addView(khungMôTả);
            hàngToànBộ.addView(cụmNútThaoTác);

            thẻUser.addView(hàngToànBộ);
            containerDanhSáchThànhViên.addView(thẻUser);
        }

        if (nhãnSốĐangTheoDõi != null) nhãnSốĐangTheoDõi.setText(String.valueOf(sốLượngĐangTheoDõi));
        if (nhãnSốBạnBè2Chiều != null) nhãnSốBạnBè2Chiều.setText(String.valueOf(sốLượngBạnBè2Chiều));
    }

    private void thựcHiệnThayĐổiTrạngTháiTheoDõi(ThôngTinThànhViênSocial thànhViên, boolean đangTheoDõi, String chuỗiGốc) {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String chuỗiMới;

        if (đangTheoDõi) {
            chuỗiMới = chuỗiGốc.replace(thànhViên.username.toLowerCase(), "").replace(";;;;", ";;");
            Toast.makeText(this, "Đã bỏ theo dõi @" + thànhViên.username, Toast.LENGTH_SHORT).show();
        } else {
            chuỗiMới = chuỗiGốc + ";;" + thànhViên.username.toLowerCase();
            Toast.makeText(this, "🎉 Đã theo dõi @" + thànhViên.username + "! Cả 2 đã trở thành Bạn Bè!", Toast.LENGTH_SHORT).show();
        }

        bộLưuTrữVí.edit().putString("FOLLOWING_LIST_" + usernameHiệnTại.toLowerCase().trim(), chuỗiMới).apply();

        // Đồng bộ lên Firebase Firestore
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> data = new HashMap<>();
            data.put("target_user", thànhViên.username);
            data.put("is_following", !đangTheoDõi);
            data.put("updated_at", System.currentTimeMillis());

            db.collection("users").document(usernameHiệnTại).collection("following").document(thànhViên.username).set(data);
        } catch (Exception ignored) {}

        cậpNhậtThốngKêVàVẽDanhSáchthànhViên(ôNhậpTìmKiếmUser != null ? ôNhậpTìmKiếmUser.getText().toString().trim() : "");
    }
}
