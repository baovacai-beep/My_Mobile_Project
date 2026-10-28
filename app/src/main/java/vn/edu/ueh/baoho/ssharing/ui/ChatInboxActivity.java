package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import vn.edu.ueh.baoho.ssharing.R;

public class ChatInboxActivity extends AppCompatActivity {

    private TextView tabĐangGiaoDịch, tabBạnBèChat, tabNgườiLạChat, tabHỗTrợCSKH, tabLịchSửChat;
    private LinearLayout containerDanhSáchĐoạnChat;

    private String tabĐangChọn = "ACTIVE";
    private String usernameHiệnTại = "baoho123";
    private com.google.firebase.firestore.ListenerRegistration listenerFirestoreInbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_inbox);

        đọcThôngTinTàiKhoản();
        khởiTạoÁnhXạGiaoDiện();
        càiĐặtSựKiệnLắngNghe();
        cậpNhậtDanhSáchĐoạnChatTheoTab();
        kíchHoạtLắngNgheInboxRealtimeFirestore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vn.edu.ueh.baoho.ssharing.utils.SocialHelper.syncFollowStateFromCloud(this, usernameHiệnTại, this::cậpNhậtDanhSáchĐoạnChatTheoTab);
    }

    private void đọcThôngTinTàiKhoản() {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        usernameHiệnTại = bộLưuTrữCụcBộ.getString("CURRENT_USERNAME", "baoho123");
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
        tabBạnBèChat = findViewById(R.id.tabSocialFriendChats);
        tabNgườiLạChat = findViewById(R.id.tabStrangerChats);
        tabHỗTrợCSKH = findViewById(R.id.tabSupportChats);
        tabLịchSửChat = findViewById(R.id.tabCompletedChats);
        containerDanhSáchĐoạnChat = findViewById(R.id.containerChatConversations);
    }

    private void càiĐặtSựKiệnLắngNghe() {
        if (findViewById(R.id.btnBackChatInbox) != null) {
            findViewById(R.id.btnBackChatInbox).setOnClickListener(v -> finish());
        }

        View nútBànTradeP2P = findViewById(R.id.btnOpenP2PTrade);
        if (nútBànTradeP2P != null) {
            nútBànTradeP2P.setOnClickListener(v ->
                    startActivity(new Intent(this, P2PVoucherTradeActivity.class))
            );
        }

        if (tabĐangGiaoDịch != null) {
            tabĐangGiaoDịch.setOnClickListener(v -> chuyểnTabChatInbox("ACTIVE", tabĐangGiaoDịch));
        }

        if (tabBạnBèChat != null) {
            tabBạnBèChat.setOnClickListener(v -> chuyểnTabChatInbox("SOCIAL", tabBạnBèChat));
        }

        if (tabNgườiLạChat != null) {
            tabNgườiLạChat.setOnClickListener(v -> chuyểnTabChatInbox("STRANGER", tabNgườiLạChat));
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
        if (tabBạnBèChat != null) { tabBạnBèChat.setBackground(null); tabBạnBèChat.setTextColor(màuXám); }
        if (tabNgườiLạChat != null) { tabNgườiLạChat.setBackground(null); tabNgườiLạChat.setTextColor(màuXám); }
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

        java.util.Set<String> cácĐốiTácĐãHiểnThịSet = new java.util.HashSet<>();

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

                boolean đãHoànTất = danhSáchĐãBánHệThống.contains(mãCode);
                boolean làChatSocial = giáXu == 0 || mãCode.startsWith("CHAT-") || mãCode.startsWith("SOCIAL_") || mãCode.startsWith("P2P_");

                // Kiểm tra bạn bè 2 chiều (Mutual Friends) vs Người lạ (Strangers)
                String usernameThựcĐốiTác = usernameHiệnTại.equalsIgnoreCase(buyerUser) ? sellerName : buyerUser;
                String partnerClean = tríchXuấtUsernameSạch(usernameThựcĐốiTác);

                // Khử trùng lặp: Mỗi đối tác chỉ hiển thị 1 thẻ hội thoại gần nhất duy nhất
                if (!partnerClean.isEmpty()) {
                    if (cácĐốiTácĐãHiểnThịSet.contains(partnerClean)) continue;
                }

                boolean làBạnBè2Chiều = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.isMutualFriend(this, usernameHiệnTại, partnerClean);

                // Lọc chính xác Rạch Ròi theo từng Tab
                if ("ACTIVE".equals(tabĐangChọn)) {
                    if (đãHoànTất || làChatSocial) continue;
                } else if ("SOCIAL".equals(tabĐangChọn)) {
                    if (!làChatSocial || !làBạnBè2Chiều) continue;
                } else if ("STRANGER".equals(tabĐangChọn)) {
                    if (!làChatSocial || làBạnBè2Chiều) continue;
                } else if ("HISTORY".equals(tabĐangChọn)) {
                    if (!đãHoànTất) continue;
                } else if ("SUPPORT".equals(tabĐangChọn)) {
                    continue;
                }

                if (!partnerClean.isEmpty()) {
                    cácĐốiTácĐãHiểnThịSet.add(partnerClean);
                }

                // Đọc tin nhắn mới nhất
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

                String tênĐốiTácHiểnThị = sellerName;
                String avatarĐốiTácHiểnThị = sellerAvatar;

                if (usernameHiệnTại.equalsIgnoreCase(buyerUser)) {
                    tênĐốiTácHiểnThị = sellerName;
                    avatarĐốiTácHiểnThị = sellerAvatar;
                } else {
                    tênĐốiTácHiểnThị = buyerName + " (@" + buyerUser + ")";
                    avatarĐốiTácHiểnThị = buyerUser.substring(0, Math.min(2, buyerUser.length())).toUpperCase();
                }

                String trạngTháiStr = làChatSocial ? (làBạnBè2Chiều ? "💬 Bạn bè 2 chiều" : "👤 Tin nhắn từ Người Lạ") : (đãHoànTất ? "🔒 Đã hoàn tất - Khóa lưu trữ tự động sau 3 ngày" : "🔒 Đang khóa " + giáXu + " SS");

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
            }
        }

        if ("SUPPORT".equals(tabĐangChọn)) {
            thêmThẻĐoạnChatHỗTrợCSKH("🎧 Tổng Đài Trọng Tài CSKH SSharing", "CS", "Hệ thống tiếp nhận tranh chấp & giải quyết khiếu nại Escrow 24/7", "🛡️ Trọng tài đã xác minh giao dịch của bạn an toàn 100%", "Vừa xong");
        } else if ("HISTORY".equals(tabĐangChọn) && containerDanhSáchĐoạnChat.getChildCount() == 0) {
            thêmThẻĐoạnChatHộpThoại("Gia Bảo (@gia.bao)", "GB", "Voucher BeCar Giảm 30K Đơn Từ 60K", "🔒 Đã hoàn tất - Khóa lưu trữ tự động sau 3 ngày", "Giao dịch đã giải ngân thành công vào ví người bán.", "19/10", "BECAR30K-USER44", 220, "gia.bao");
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

        String usernameClean = tríchXuấtUsernameSạch(usernameĐốiTác);
        android.widget.ImageView ảnhAvatarTarget = new android.widget.ImageView(this);
        ảnhAvatarTarget.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
        ảnhAvatarTarget.setLayoutParams(thôngSốMatch);
        khungAvatar.addView(ảnhAvatarTarget);
        vn.edu.ueh.baoho.ssharing.utils.SocialHelper.loadUserAvatar(this, ảnhAvatarTarget, usernameClean);

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
            String idPhòng = "CHAT_ROOM_" + mãBíMật;
            SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
            bộLưuTrữVí.edit().putBoolean("IS_READ_ROOM_" + idPhòng + "_" + usernameHiệnTại.toLowerCase().trim(), true).apply();

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
            Intent ýĐịnhChatCSKH = new Intent(this, EscrowChatActivity.class);
            ýĐịnhChatCSKH.putExtra("EXTRA_SELLER_NAME", "Gia Bảo Hồ (@baoho123)");
            ýĐịnhChatCSKH.putExtra("EXTRA_SELLER_AVATAR", "CS");
            ýĐịnhChatCSKH.putExtra("EXTRA_VOUCHER_TITLE", "Tổng Đài Hỗ Trợ Khách Hàng & Trọng Tài SSharing");
            ýĐịnhChatCSKH.putExtra("EXTRA_SECRET_CODE", "HOTLINE-CSKH-SUPPORT");
            ýĐịnhChatCSKH.putExtra("EXTRA_PRICE_SS", 0);
            startActivity(ýĐịnhChatCSKH);
        });

        if (containerDanhSáchĐoạnChat != null) {
            containerDanhSáchĐoạnChat.addView(thẻCard);
        }
    }

    private void kíchHoạtLắngNgheInboxRealtimeFirestore() {
        try {
            com.google.firebase.firestore.FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
            listenerFirestoreInbox = db.collection("users")
                    .document(usernameHiệnTại.toLowerCase().trim())
                    .collection("chat_inbox")
                    .orderBy("updatedAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .addSnapshotListener((snapshots, e) -> {
                        if (e != null || snapshots == null) return;
                        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);

                        for (com.google.firebase.firestore.DocumentSnapshot doc : snapshots.getDocuments()) {
                            String idPhong = doc.getString("idPhong");
                            String maCode = doc.getString("maCode");
                            String tenVoucher = doc.getString("tenVoucher");
                            String buyerUsername = doc.getString("buyerUsername");
                            String buyerName = doc.getString("buyerName");
                            String sellerName = doc.getString("sellerName");
                            String sellerAvatar = doc.getString("sellerAvatar");
                            Long giaXuObj = doc.getLong("giaXu");
                            int giaXu = giaXuObj != null ? giaXuObj.intValue() : 0;
                            String noiDungTin = doc.getString("noiDungTin");
                            String thoiGian = doc.getString("thoiGian");

                            if (idPhong == null) continue;
                            if (noiDungTin == null) noiDungTin = "Giao dịch mới";
                            if (thoiGian == null) thoiGian = "Vừa xong";

                            String banGhiMoi = idPhong + "||" + maCode + "||" + tenVoucher + "||" + buyerUsername + "||" + buyerName + "||" + sellerName + "||" + sellerAvatar + "||" + giaXu + "||" + noiDungTin + "||" + thoiGian;

                            String danhSachGoc = bộLưuTrữVí.getString("DANH_SACH_PHONG_CHAT_TAT_CA", "");
                            StringBuilder danhSachMoi = new StringBuilder();
                            danhSachMoi.append(banGhiMoi);

                            if (!danhSachGoc.isEmpty()) {
                                String[] cacPhong = danhSachGoc.split(";;;");
                                for (String phong : cacPhong) {
                                    if (phong.trim().isEmpty() || phong.startsWith(idPhong)) continue;
                                    danhSachMoi.append(";;;").append(phong);
                                }
                            }
                            bộLưuTrữVí.edit().putString("DANH_SACH_PHONG_CHAT_TAT_CA", danhSachMoi.toString()).apply();
                        }

                        runOnUiThread(this::cậpNhậtDanhSáchĐoạnChatTheoTab);
                    });
        } catch (Exception ignored) {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerFirestoreInbox != null) {
            listenerFirestoreInbox.remove();
        }
    }
}
