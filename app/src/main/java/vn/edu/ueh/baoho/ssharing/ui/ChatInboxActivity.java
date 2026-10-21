package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import vn.edu.ueh.baoho.ssharing.R;

public class ChatInboxActivity extends AppCompatActivity {

    private TextView tabĐangGiaoDịch, tabHỗTrợCSKH, tabLịchSửChat;
    private LinearLayout containerDanhSáchĐoạnChat;

    private String tabĐangChọn = "ACTIVE";
    private String usernameHiệnTại = "baoho123";
    private String họTênHiệnTại = "Gia Bảo Hồ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_inbox);

        đọcThôngTinTàiKhoản();
        khởiTạoÁnhXạGiaoDiện();
        càiĐặtSựKiệnLắngNghe();
        cậpNhậtDanhSáchĐoạnChatTheoTab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cậpNhậtDanhSáchĐoạnChatTheoTab();
    }

    private void đọcThôngTinTàiKhoản() {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        usernameHiệnTại = bộLưuTrữCụcBộ.getString("CURRENT_USERNAME", "baoho123");
        họTênHiệnTại = bộLưuTrữCụcBộ.getString("CURRENT_USER_NAME", "Gia Bảo Hồ");
    }

    private String tríchXuấtUsernameSạch(String chuỗiGốc) {
        if (chuỗiGốc == null || chuỗiGốc.trim().isEmpty()) return "";
        if (chuỗiGốc.contains("(@") && chuỗiGốc.contains(")")) {
            int start = chuỗiGốc.indexOf("(@") + 2;
            int end = chuỗiGốc.indexOf(")", start);
            if (end > start) {
                return chuỗiGốc.substring(start, end).toLowerCase().trim();
            }
        }
        return chuỗiGốc.toLowerCase().trim();
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        tabĐangGiaoDịch = findViewById(R.id.tabActiveEscrowChats);
        tabHỗTrợCSKH = findViewById(R.id.tabSupportChats);
        tabLịchSửChat = findViewById(R.id.tabCompletedChats);
        containerDanhSáchĐoạnChat = findViewById(R.id.containerChatConversations);
    }

    private void càiĐặtSựKiệnLắngNghe() {
        if (findViewById(R.id.btnBackChatInbox) != null) {
            findViewById(R.id.btnBackChatInbox).setOnClickListener(v -> finish());
        }

        if (tabĐangGiaoDịch != null) {
            tabĐangGiaoDịch.setOnClickListener(v -> chuyểnTabChatInbox("ACTIVE", tabĐangGiaoDịch));
        }

        if (tabHỗTrợCSKH != null) {
            tabHỗTrợCSKH.setOnClickListener(v -> chuyểnTabChatInbox("SUPPORT", tabHỗTrợCSKH));
        }

        if (tabLịchSửChat != null) {
            tabLịchSửChat.setOnClickListener(v -> chuyểnTabChatInbox("HISTORY", tabLịchSửChat));
        }
    }

    private void chuyểnTabChatInbox(String loạiTab, TextView tabĐượcChọn) {
        tabĐangChọn = loạiTab;

        int màuXám = Color.parseColor("#888888");
        if (tabĐangGiaoDịch != null) { tabĐangGiaoDịch.setBackground(null); tabĐangGiaoDịch.setTextColor(màuXám); }
        if (tabHỗTrợCSKH != null) { tabHỗTrợCSKH.setBackground(null); tabHỗTrợCSKH.setTextColor(màuXám); }
        if (tabLịchSửChat != null) { tabLịchSửChat.setBackground(null); tabLịchSửChat.setTextColor(màuXám); }

        if (tabĐượcChọn != null) {
            tabĐượcChọn.setBackgroundResource(R.drawable.bg_tab_active);
            tabĐượcChọn.setTextColor(Color.WHITE);
        }

        cậpNhậtDanhSáchĐoạnChatTheoTab();
    }

    private void cậpNhậtDanhSáchĐoạnChatTheoTab() {
        if (containerDanhSáchĐoạnChat == null) return;
        containerDanhSáchĐoạnChat.removeAllViews();

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String danhSáchPhòngChatTấtCả = bộLưuTrữVí.getString("DANH_SACH_PHONG_CHAT_TAT_CA", "");
        String danhSáchĐãBánHệThống = bộLưuTrữVí.getString("DANH_SACH_VOUCHER_DA_BAN_HE_THONG", "");

        if ("ACTIVE".equals(tabĐangChọn)) {
            int sốĐoạnChatThựcTế = 0;
            if (!danhSáchPhòngChatTấtCả.isEmpty()) {
                String[] cácPhòng = danhSáchPhòngChatTấtCả.split(";;;");
                for (String phòng : cácPhòng) {
                    if (phòng.trim().isEmpty()) continue;
                    String[] mảngTrường = phòng.split("\\|\\|");
                    if (mảngTrường.length < 8) continue;

                    String idPhòng = mảngTrường[0];
                    String mãCode = mảngTrường[1];
                    String tênVoucher = mảngTrường[2];
                    String buyerUser = mảngTrường[3];
                    String buyerName = mảngTrường[4];
                    String sellerName = mảngTrường[5];
                    String sellerAvatar = mảngTrường[6];
                    int giáXu = 180;
                    try {
                        giáXu = Integer.parseInt(mảngTrường[7]);
                    } catch (Exception ignored) {}

                    String nộiDungCuối = mảngTrường.length > 8 ? mảngTrường[8] : "Giao dịch Escrow mới";
                    String thờiGianCuối = mảngTrường.length > 9 ? mảngTrường[9] : "Vừa xong";

                    // Đọc tin nhắn mới nhất thực tế trong kho phòng chat
                    String khoTinNhắn = bộLưuTrữVí.getString("DANH_SACH_TIN_NHAN_" + idPhòng, "");
                    if (!khoTinNhắn.isEmpty()) {
                        String[] cácTin = khoTinNhắn.split(";;");
                        String tinCuối = cácTin[cácTin.length - 1];
                        String[] mảngTin = tinCuối.split("\\|\\|");
                        if (mảngTin.length >= 5) {
                            nộiDungCuối = mảngTin[3];
                            thờiGianCuối = mảngTin[4];
                        }
                    }

                    // Xác định tên đối tác hiển thị linh hoạt cho 2 đầu tài khoản (Người Mua & Người Bán)
                    String tênĐốiTácHiểnThị = sellerName;
                    String avatarĐốiTácHiểnThị = sellerAvatar;

                    if (usernameHiệnTại.equalsIgnoreCase(buyerUser)) {
                        tênĐốiTácHiểnThị = sellerName;
                        avatarĐốiTácHiểnThị = sellerAvatar;
                    } else {
                        tênĐốiTácHiểnThị = buyerName + " (@" + buyerUser + ")";
                        avatarĐốiTácHiểnThị = buyerUser.substring(0, Math.min(2, buyerUser.length())).toUpperCase();
                    }

                    boolean đãHoànTất = danhSáchĐãBánHệThống.contains(mãCode);
                    String trạngTháiStr = đãHoànTất ? "✓ Đã giải ngân " + giáXu + " SS" : "🔒 Đang khóa " + giáXu + " SS";

                    String usernameThựcĐốiTác = usernameHiệnTại.equalsIgnoreCase(buyerUser) ? sellerName : buyerUser;

                    thêmThẻĐoạnChatHộpThoại(
                            tênĐốiTácHiểnThị,
                            avatarĐốiTácHiểnThị,
                            tênVoucher,
                            trạngTháiStr,
                            nộiDungCuối,
                            thờiGianCuối,
                            mãCode,
                            giáXu,
                            usernameThựcĐốiTác
                    );
                    sốĐoạnChatThựcTế++;
                }
            }

            if (sốĐoạnChatThựcTế == 0) {
                thêmThẻĐoạnChatHộpThoại("Minh Triết (@minh.triet)", "MT", "Voucher GrabCar Giảm 50K", "🔒 Đang khóa 350 SS", "Ok để mình nhập thử vào Shopee xem được không.", "09:39", "GRAB50K-COMMUNITY88", 350, "minh.triet");
                thêmThẻĐoạnChatHộpThoại("Khánh Trình (@khanh.trinh)", "KT", "Voucher Highlands Mua 1 Tặng 1 Trà Sen Vàng", "🔒 Đang khóa 180 SS", "Cảm ơn bạn! Mã voucher mới nhận sáng nay...", "08:24", "HLM1T1-USER33", 180, "khanh.trinh");
                thêmThẻĐoạnChatHộpThoại("Bảo Ngọc (@bao.ngoc)", "BN", "Voucher ShopeeFood Giảm 50K Đơn 100K", "🔒 Đang khóa 500 SS", "Mình đã gửi mã bí mật vào khung chat rồi nhé!", "Hôm qua", "SPFOOD50K-USER22", 500, "bao.ngoc");
            }
        } else if ("SUPPORT".equals(tabĐangChọn)) {
            thêmThẻĐoạnChatHỗTrợCSKH("🎧 Tổng Đài Trọng Tài CSKH SSharing", "CS", "Hệ thống tiếp nhận tranh chấp & giải quyết khiếu nại Escrow 24/7", "🛡️ Trọng tài đã xác minh giao dịch của bạn an toàn 100%", "Vừa xong");
        } else {
            thêmThẻĐoạnChatHộpThoại("Gia Bảo (@gia.bao)", "GB", "Voucher BeCar Giảm 30K Đơn Từ 60K", "✓ Đã giải ngân 220 SS", "Giao dịch đã giải ngân thành công vào ví người bán.", "19/10", "BECAR30K-USER44", 220, "gia.bao");
        }
    }

    private void thêmThẻĐoạnChatHộpThoại(String tênĐốiTác, String avatarText, String tênVoucher, String nhãnTrạngThái, String tinNhắnCuối, String thờiGianText, String mãBíMật, int giáXu, String usernameĐốiTác) {
        CardView thẻCard = new CardView(this);
        thẻCard.setCardBackgroundColor(Color.parseColor("#161327"));
        thẻCard.setRadius(28);
        thẻCard.setCardElevation(0);
        thẻCard.setClickable(true);
        thẻCard.setFocusable(true);

        LinearLayout.LayoutParams thôngSốThẻ = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốThẻ.bottomMargin = 20;
        thẻCard.setLayoutParams(thôngSốThẻ);

        LinearLayout khungToànBộ = new LinearLayout(this);
        khungToànBộ.setOrientation(LinearLayout.HORIZONTAL);
        khungToànBộ.setPadding(24, 24, 24, 24);

        // Avatar Frame: Tự động nạp Ảnh đại diện thật của đối tác nếu có
        android.widget.FrameLayout khungAvatar = new android.widget.FrameLayout(this);
        LinearLayout.LayoutParams thôngSốAvatar = new LinearLayout.LayoutParams(100, 100);
        khungAvatar.setLayoutParams(thôngSốAvatar);

        TextView ôAvatar = new TextView(this);
        ôAvatar.setText(avatarText);
        ôAvatar.setTextColor(Color.WHITE);
        ôAvatar.setTextSize(14);
        ôAvatar.setTypeface(null, android.graphics.Typeface.BOLD);
        ôAvatar.setGravity(Gravity.CENTER);
        ôAvatar.setBackgroundResource(R.drawable.bg_circle_purple);
        android.widget.FrameLayout.LayoutParams thôngSốMatch = new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT
        );
        ôAvatar.setLayoutParams(thôngSốMatch);
        khungAvatar.addView(ôAvatar);

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String usernameClean = tríchXuấtUsernameSạch(usernameĐốiTác);
        String pathAvatar = bộLưuTrữVí.getString("CURRENT_USER_AVATAR_PATH_" + usernameClean, "");

        if (!pathAvatar.isEmpty() && new java.io.File(pathAvatar).exists()) {
            try {
                android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(pathAvatar);
                if (bitmap != null) {
                    android.widget.ImageView ảnhAvatarTarget = new android.widget.ImageView(this);
                    ảnhAvatarTarget.setImageBitmap(bitmap);
                    ảnhAvatarTarget.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
                    ảnhAvatarTarget.setLayoutParams(thôngSốMatch);
                    khungAvatar.addView(ảnhAvatarTarget);
                }
            } catch (Throwable ignored) {}
        }

        // Thông tin Chat
        LinearLayout khungThôngTin = new LinearLayout(this);
        khungThôngTin.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams thôngSốMôTả = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốMôTả.leftMargin = 20;
        khungThôngTin.setLayoutParams(thôngSốMôTả);

        LinearLayout khungHàng1 = new LinearLayout(this);
        khungHàng1.setOrientation(LinearLayout.HORIZONTAL);
        khungHàng1.setGravity(Gravity.CENTER_VERTICAL);

        TextView nhãnTênĐốiTác = new TextView(this);
        nhãnTênĐốiTác.setText(tênĐốiTác);
        nhãnTênĐốiTác.setTextColor(Color.WHITE);
        nhãnTênĐốiTác.setTextSize(14);
        nhãnTênĐốiTác.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams thôngSốTên = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        nhãnTênĐốiTác.setLayoutParams(thôngSốTên);

        TextView nhãnThờiGian = new TextView(this);
        nhãnThờiGian.setText(thờiGianText);
        nhãnThờiGian.setTextColor(Color.parseColor("#888888"));
        nhãnThờiGian.setTextSize(11);

        khungHàng1.addView(nhãnTênĐốiTác);
        khungHàng1.addView(nhãnThờiGian);

        TextView nhãnTiêuĐềVoucher = new TextView(this);
        nhãnTiêuĐềVoucher.setText(tênVoucher);
        nhãnTiêuĐềVoucher.setTextColor(Color.parseColor("#43E5A0"));
        nhãnTiêuĐềVoucher.setTextSize(11);
        nhãnTiêuĐềVoucher.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView nhãnLớpTrạngThái = new TextView(this);
        nhãnLớpTrạngThái.setText(nhãnTrạngThái);
        nhãnLớpTrạngThái.setTextColor(Color.parseColor("#FFD54F"));
        nhãnLớpTrạngThái.setTextSize(11);

        TextView nhãnTinNhắnCuối = new TextView(this);
        nhãnTinNhắnCuối.setText(tinNhắnCuối);
        nhãnTinNhắnCuối.setTextColor(Color.parseColor("#A09CAB"));
        nhãnTinNhắnCuối.setTextSize(12);

        khungThôngTin.addView(khungHàng1);
        khungThôngTin.addView(nhãnTiêuĐềVoucher);
        khungThôngTin.addView(nhãnLớpTrạngThái);
        khungThôngTin.addView(nhãnTinNhắnCuối);

        khungToànBộ.addView(khungAvatar);
        khungToànBộ.addView(khungThôngTin);

        thẻCard.addView(khungToànBộ);

        thẻCard.setOnClickListener(v -> {
            Intent ýĐịnhEscrowChat = new Intent(this, EscrowChatActivity.class);
            ýĐịnhEscrowChat.putExtra("EXTRA_SELLER_NAME", tênĐốiTác);
            ýĐịnhEscrowChat.putExtra("EXTRA_SELLER_AVATAR", avatarText);
            ýĐịnhEscrowChat.putExtra("EXTRA_VOUCHER_TITLE", tênVoucher);
            ýĐịnhEscrowChat.putExtra("EXTRA_SECRET_CODE", mãBíMật);
            ýĐịnhEscrowChat.putExtra("EXTRA_PRICE_SS", giáXu);
            startActivity(ýĐịnhEscrowChat);
        });

        if (containerDanhSáchĐoạnChat != null) {
            containerDanhSáchĐoạnChat.addView(thẻCard);
        }
    }

    private void thêmThẻĐoạnChatHỗTrợCSKH(String tiêuĐềCSKH, String avatarText, String môTảCSKH, String tinNhắnCuối, String thờiGianText) {
        CardView thẻCard = new CardView(this);
        thẻCard.setCardBackgroundColor(Color.parseColor("#161327"));
        thẻCard.setRadius(28);
        thẻCard.setCardElevation(0);
        thẻCard.setClickable(true);
        thẻCard.setFocusable(true);

        LinearLayout.LayoutParams thôngSốThẻ = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốThẻ.bottomMargin = 20;
        thẻCard.setLayoutParams(thôngSốThẻ);

        LinearLayout khungToànBộ = new LinearLayout(this);
        khungToànBộ.setOrientation(LinearLayout.HORIZONTAL);
        khungToànBộ.setPadding(24, 24, 24, 24);

        TextView ôAvatar = new TextView(this);
        ôAvatar.setText(avatarText);
        ôAvatar.setTextColor(Color.WHITE);
        ôAvatar.setTextSize(14);
        ôAvatar.setTypeface(null, android.graphics.Typeface.BOLD);
        ôAvatar.setGravity(Gravity.CENTER);
        ôAvatar.setBackgroundResource(R.drawable.bg_badge_pink);
        LinearLayout.LayoutParams thôngSốAvatar = new LinearLayout.LayoutParams(100, 100);
        ôAvatar.setLayoutParams(thôngSốAvatar);

        LinearLayout khungThôngTin = new LinearLayout(this);
        khungThôngTin.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams thôngSốMôTả = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốMôTả.leftMargin = 20;
        khungThôngTin.setLayoutParams(thôngSốMôTả);

        TextView nhãnTiêuĐề = new TextView(this);
        nhãnTiêuĐề.setText(tiêuĐềCSKH);
        nhãnTiêuĐề.setTextColor(Color.WHITE);
        nhãnTiêuĐề.setTextSize(14);
        nhãnTiêuĐề.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView nhãnMôTả = new TextView(this);
        nhãnMôTả.setText(môTảCSKH);
        nhãnMôTả.setTextColor(Color.parseColor("#43E5A0"));
        nhãnMôTả.setTextSize(11);

        TextView nhãnTinNhắn = new TextView(this);
        nhãnTinNhắn.setText(tinNhắnCuối);
        nhãnTinNhắn.setTextColor(Color.parseColor("#A09CAB"));
        nhãnTinNhắn.setTextSize(12);

        khungThôngTin.addView(nhãnTiêuĐề);
        khungThôngTin.addView(nhãnMôTả);
        khungThôngTin.addView(nhãnTinNhắn);

        khungToànBộ.addView(ôAvatar);
        khungToànBộ.addView(khungThôngTin);

        thẻCard.addView(khungToànBộ);

        thẻCard.setOnClickListener(v -> {
            Toast.makeText(this, "Mở Bàn Làm Việc Trọng Tài CSKH", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, activity_staff_arbiter.class));
        });

        if (containerDanhSáchĐoạnChat != null) {
            containerDanhSáchĐoạnChat.addView(thẻCard);
        }
    }
}
