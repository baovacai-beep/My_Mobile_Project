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

        // Tự động đồng bộ liên kết Follow từ Cloud Firestore trước khi vẽ danh sách
        vn.edu.ueh.baoho.ssharing.utils.SocialHelper.syncFollowStateFromCloud(this, usernameHiệnTại, () -> {
            cậpNhậtThốngKêVàVẽDanhSáchthànhViên("");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        vn.edu.ueh.baoho.ssharing.utils.SocialHelper.syncFollowStateFromCloud(this, usernameHiệnTại, () -> {
            cậpNhậtThốngKêVàVẽDanhSáchthànhViên("");
        });
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

        // 1. Nạp từ bộ nhớ dùng chung SharedPreferences DANH_SACH_TAI_KHOAN_NGUOI_DUNG
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String danhSáchTàiKhoảnHệThống = bộLưuTrữCụcBộ.getString("DANH_SACH_TAI_KHOAN_NGUOI_DUNG", "");

        if (!danhSáchTàiKhoảnHệThống.isEmpty()) {
            String[] cácTàiKhoản = danhSáchTàiKhoảnHệThống.split(";;");
            for (String chuỗiTàiKhoản : cácTàiKhoản) {
                if (chuỗiTàiKhoản.trim().isEmpty()) continue;
                String[] mảngTrường = chuỗiTàiKhoản.split("\\|\\|");
                if (mảngTrường.length < 1) continue;

                String user = mảngTrường[0];
                String họTên = mảngTrường.length > 2 ? mảngTrường[2] : user;
                String initials = "SS";
                if (họTên.length() >= 2) {
                    String[] mảngTừ = họTên.trim().split(" ");
                    if (mảngTừ.length >= 2) {
                        initials = (mảngTừ[0].substring(0, 1) + mảngTừ[mảngTừ.length - 1].substring(0, 1)).toUpperCase();
                    } else {
                        initials = họTên.substring(0, 2).toUpperCase();
                    }
                }

                thêmThànhViênMẫuNếuChưaCó(user, họTên, initials, 96, true);
            }
        }

        // 2. Thêm các tài khoản mẫu mặc định nếu chưa có
        thêmThànhViênMẫuNếuChưaCó("Beo123", "Gia Beo", "GB", 98, true);
        thêmThànhViênMẫuNếuChưaCó("minh.triet", "Minh Triết", "MT", 96, true);
        thêmThànhViênMẫuNếuChưaCó("bao.ngoc", "Bảo Ngọc", "BN", 94, true);
        thêmThànhViênMẫuNếuChưaCó("khanh.trinh", "Khánh Trình", "KT", 92, true);
        thêmThànhViênMẫuNếuChưaCó("gia.bao", "Gia Bảo", "GB", 90, false);
        thêmThànhViênMẫuNếuChưaCó("hoang.long", "Hoàng Long", "HL", 88, false);
    }

    private void thêmThànhViênMẫuNếuChưaCó(String user, String họTên, String initials, int trust, boolean verified) {
        for (ThôngTinThànhViênSocial item : danhSáchTấtCảThànhViên) {
            if (item.username.equalsIgnoreCase(user)) return;
        }
        danhSáchTấtCảThànhViên.add(new ThôngTinThànhViênSocial(user, họTên, initials, trust, verified));
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

        int[] counts = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.calculateFollowingAndMutualCounts(this, usernameHiệnTại);
        int sốLượngĐangTheoDõi = counts[0];
        int sốLượngBạnBè2Chiều = counts[1];

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);

        for (ThôngTinThànhViênSocial thànhViên : danhSáchTấtCảThànhViên) {
            if (thànhViên.username.equalsIgnoreCase(usernameHiệnTại)) continue;

            if (!từKhóa.isEmpty()) {
                boolean khớpUser = thànhViên.username.toLowerCase().contains(từKhóa.toLowerCase());
                boolean khớpTên = thànhViên.họTên.toLowerCase().contains(từKhóa.toLowerCase());
                if (!khớpUser && !khớpTên) continue;
            }

            boolean làĐangTheoDõi = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.isUserFollowing(this, usernameHiệnTại, thànhViên.username);
            boolean làBạnBè2Chiều = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.isMutualFriend(this, usernameHiệnTại, thànhViên.username);

            // Bố Cục Thẻ User 2 Hàng Độc Lập Chống Bị Bóp Méo Chữ Họ Tên
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

            LinearLayout khungThẻToànBộ = new LinearLayout(this);
            khungThẻToànBộ.setOrientation(LinearLayout.VERTICAL);
            khungThẻToànBộ.setPadding(20, 20, 20, 20);

            // Hàng 1: Avatar + Họ tên (Rộng rãi 100% chiều ngang)
            LinearLayout hàng1 = new LinearLayout(this);
            hàng1.setOrientation(LinearLayout.HORIZONTAL);
            hàng1.setGravity(android.view.Gravity.CENTER_VERTICAL);

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

            // Thông tin Username & Họ tên (Chiếm 100% độ rộng còn lại)
            LinearLayout khungMôTả = new LinearLayout(this);
            khungMôTả.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams thôngSốMôTả = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            thôngSốMôTả.leftMargin = 16;
            khungMôTả.setLayoutParams(thôngSốMôTả);

            TextView nhãnTên = new TextView(this);
            nhãnTên.setText(thànhViên.họTên + " " + (thànhViên.làVerified ? "💎" : ""));
            nhãnTên.setTextColor(Color.WHITE);
            nhãnTên.setTextSize(15);
            nhãnTên.setTypeface(null, android.graphics.Typeface.BOLD);
            nhãnTên.setSingleLine(true);
            nhãnTên.setEllipsize(android.text.TextUtils.TruncateAt.END);

            TextView nhãnSub = new TextView(this);
            nhãnSub.setText("@" + thànhViên.username + " • Trust Score: " + thànhViên.điểmTrustScore + " điểm");
            nhãnSub.setTextColor(Color.parseColor("#A09CAB"));
            nhãnSub.setTextSize(11);
            nhãnSub.setSingleLine(true);
            nhãnSub.setEllipsize(android.text.TextUtils.TruncateAt.END);

            khungMôTả.addView(nhãnTên);
            khungMôTả.addView(nhãnSub);

            hàng1.addView(khungAvatar);
            hàng1.addView(khungMôTả);

            // Hàng 2: Cụm Nút Thao Tác 3 Chức Năng (Ngang hàng phẳng đẹp, weight 1.0f đều)
            LinearLayout hàng2Nút = new LinearLayout(this);
            hàng2Nút.setOrientation(LinearLayout.HORIZONTAL);
            hàng2Nút.setGravity(android.view.Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams thôngSốHàng2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            thôngSốHàng2.topMargin = 12;
            hàng2Nút.setLayoutParams(thôngSốHàng2);

            // Nút 1: Nhắn tin
            Button nútNhắnTin = new Button(this);
            LinearLayout.LayoutParams thôngSốNút1 = new LinearLayout.LayoutParams(0, 84, 1.0f);
            thôngSốNút1.rightMargin = 6;
            nútNhắnTin.setLayoutParams(thôngSốNút1);
            nútNhắnTin.setText("💬 Chat");
            nútNhắnTin.setTextColor(Color.WHITE);
            nútNhắnTin.setTextSize(11);
            nútNhắnTin.setPadding(0, 0, 0, 0);
            nútNhắnTin.setBackgroundResource(R.drawable.bg_btn_purple);
            nútNhắnTin.setOnClickListener(v -> {
                String socialRoomCode = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.generateSocialChatRoomCode(usernameHiệnTại, thànhViên.username);
                android.content.Intent ýĐịnhChat = new android.content.Intent(this, EscrowChatActivity.class);
                ýĐịnhChat.putExtra("EXTRA_SELLER_NAME", thànhViên.họTên + " (@" + thànhViên.username + ")");
                ýĐịnhChat.putExtra("EXTRA_SELLER_AVATAR", thànhViên.avatarInitials);
                ýĐịnhChat.putExtra("EXTRA_VOUCHER_TITLE", "Trò chuyện bạn bè cộng đồng");
                ýĐịnhChat.putExtra("EXTRA_SECRET_CODE", socialRoomCode);
                ýĐịnhChat.putExtra("EXTRA_PRICE_SS", 0);
                ýĐịnhChat.putExtra("EXTRA_IS_SOCIAL_CHAT", true);
                startActivity(ýĐịnhChat);
            });

            // Nút 2: Trade
            Button nútBànTrade = new Button(this);
            LinearLayout.LayoutParams thôngSốNút2 = new LinearLayout.LayoutParams(0, 84, 1.0f);
            thôngSốNút2.rightMargin = 6;
            nútBànTrade.setLayoutParams(thôngSốNút2);
            nútBànTrade.setText("Trade");
            nútBànTrade.setTextColor(Color.WHITE);
            nútBànTrade.setTextSize(11);
            nútBànTrade.setPadding(0, 0, 0, 0);
            nútBànTrade.setBackgroundResource(R.drawable.bg_outline_button);
            nútBànTrade.setOnClickListener(v -> {
                android.content.Intent ýĐịnhTrade = new android.content.Intent(this, P2PVoucherTradeActivity.class);
                ýĐịnhTrade.putExtra("EXTRA_TARGET_USERNAME", thànhViên.username);
                startActivity(ýĐịnhTrade);
            });

            // Nút 3: Theo dõi
            Button nútTheoDõi = new Button(this);
            LinearLayout.LayoutParams thôngSốNút3 = new LinearLayout.LayoutParams(0, 84, 1.0f);
            nútTheoDõi.setLayoutParams(thôngSốNút3);
            nútTheoDõi.setTextSize(11);
            nútTheoDõi.setPadding(0, 0, 0, 0);

            if (làBạnBè2Chiều) {
                nútTheoDõi.setText("Bạn Bè");
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

            nútTheoDõi.setOnClickListener(v -> thựcHiệnThayĐổiTrạngTháiTheoDõi(thànhViên));

            hàng2Nút.addView(nútNhắnTin);
            hàng2Nút.addView(nútBànTrade);
            hàng2Nút.addView(nútTheoDõi);

            khungThẻToànBộ.addView(hàng1);
            khungThẻToànBộ.addView(hàng2Nút);

            thẻUser.addView(khungThẻToànBộ);
            containerDanhSáchThànhViên.addView(thẻUser);
        }

        if (nhãnSốĐangTheoDõi != null) nhãnSốĐangTheoDõi.setText(String.valueOf(sốLượngĐangTheoDõi));
        if (nhãnSốBạnBè2Chiều != null) nhãnSốBạnBè2Chiều.setText(String.valueOf(sốLượngBạnBè2Chiều));
    }

    private void thựcHiệnThayĐổiTrạngTháiTheoDõi(ThôngTinThànhViênSocial thànhViên) {
        boolean nowFollowing = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.toggleFollowUser(this, usernameHiệnTại, thànhViên.username);

        if (nowFollowing) {
            boolean làBạnBè2Chiều = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.isMutualFriend(this, usernameHiệnTại, thànhViên.username);
            if (làBạnBè2Chiều) {
                Toast.makeText(this, "🎉 Đã theo dõi @" + thànhViên.username + "! Cả 2 đã trở thành BẠN BÈ 2 CHIỀU!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "🎉 Đã theo dõi @" + thànhViên.username + "!", Toast.LENGTH_SHORT).show();
            }

            // Đẩy thông báo chuông tới đối tác
            SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
            String targetClean = thànhViên.username.toLowerCase().trim();
            String thờiGianThực = new java.text.SimpleDateFormat("HH:mm dd/MM", java.util.Locale.getDefault()).format(new java.util.Date());
            String tbMới = "👤 [THEO DÕI MỚI] Thành viên @" + usernameHiệnTại + " đã bắt đầu theo dõi bạn.||" + thờiGianThực;
            String tbGốc = bộLưuTrữVí.getString("DANH_SACH_THONG_BAO_" + targetClean, "");
            bộLưuTrữVí.edit()
                    .putString("DANH_SACH_THONG_BAO_" + targetClean, tbMới + (tbGốc.isEmpty() ? "" : ";;" + tbGốc))
                    .putBoolean("HAS_UNREAD_NOTIFICATIONS_" + targetClean, true)
                    .apply();
        } else {
            Toast.makeText(this, "Đã bỏ theo dõi @" + thànhViên.username, Toast.LENGTH_SHORT).show();
        }

        // Đồng bộ lên Firebase Firestore
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> data = new HashMap<>();
            data.put("target_user", thànhViên.username);
            data.put("is_following", nowFollowing);
            data.put("updated_at", System.currentTimeMillis());

            db.collection("users").document(usernameHiệnTại).collection("following").document(thànhViên.username).set(data);
        } catch (Exception ignored) {}

        cậpNhậtThốngKêVàVẽDanhSáchthànhViên(ôNhậpTìmKiếmUser != null ? ôNhậpTìmKiếmUser.getText().toString().trim() : "");
    }
}
