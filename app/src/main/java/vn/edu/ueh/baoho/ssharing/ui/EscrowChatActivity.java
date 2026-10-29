package vn.edu.ueh.baoho.ssharing.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vn.edu.ueh.baoho.ssharing.R;

public class EscrowChatActivity extends AppCompatActivity {

    private static final int MÃ_YÊU_CẦU_GỬI_ẢNH_CHAT = 302;

    private LinearLayout khungDanhSáchTinNhắn;
    private ScrollView khungCuộnChat;
    private EditText ôNhậpTinNhắn;
    private Button nútXácNhậnEscrow;
    private Button nútBáoCáoLỗiEscrow;
    private TextView nhãnTrạngTháiEscrow;
    private TextView nhãnĐếmLùiEscrow;
    private TextView nhãnTênĐốiTác;
    private TextView nhãnAvatarĐốiTác;
    private TextView nhãnTiêuĐềVoucher;
    private TextView nhãnMãBíMậtVoucher;
    private TextView nhãnThôngBáoHệThốngĐầu;

    private Handler bộCậpNhậtChatRealtime = new Handler(Looper.getMainLooper());
    private Runnable côngViệcTảiTinNhắnTựĐộng;
    private android.os.CountDownTimer bộĐếmLùiEscrowTimer;
    private long tổngSốGiâyĐếmLùi = 86400000L; // 24 giờ (1 ngày) = 86,400,000 ms

    private String tênNgườiBán = "Minh Triết";
    private String avatarNgườiBán = "MT";
    private String tênVoucher = "Voucher GrabCar Giảm 50K";
    private String mãBíMậtVoucher = "GRAB50K-COMMUNITY88";
    private int giáXuEscrow = 350;

    private String usernameHiệnTại = "baoho123";
    private String họTênHiệnTại = "Gia Bảo Hồ";
    private String idPhòngChat = "CHAT_ROOM_DEFAULT";
    private boolean làChatTròChuyệnXãHội = false;
    private boolean làPhòngĐãKhóaThựcTế = false;
    private boolean làBànPhímĐangMở = false;
    private com.google.firebase.firestore.ListenerRegistration listenerFirestoreChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escrow_chat);

        đọcThôngTinTàiKhoảnĐangĐăngNhập();
        khởiTạoÁnhXạGiaoDiện();
        đọcDữLiệuGiaoDịchTruyềnTới();
        càiĐặtSựKiệnLắngNghe();
        càiĐặtLắngNgheBànPhímẨnHiệnNútThaoTác();
        khởiĐộngTảiTinNhắnRealtime();
        kíchHoạtLắngNgheTinNhắnFirestoreRealtime();
    }

    private void đọcThôngTinTàiKhoảnĐangĐăngNhập() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        usernameHiệnTại = bộLưuTrữVí.getString("CURRENT_USERNAME", "baoho123");
        họTênHiệnTại = bộLưuTrữVí.getString("CURRENT_USER_NAME", "Gia Bảo Hồ");
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

    public static String tạoMãPhòngChatBạnBèĐốiXứng(String u1Input, String u2Input) {
        String u1 = u1Input != null ? u1Input.toLowerCase().trim() : "";
        String u2 = u2Input != null ? u2Input.toLowerCase().trim() : "";

        if (u1.compareTo(u2) < 0) {
            return "P2P_" + u1 + "_" + u2;
        } else {
            return "P2P_" + u2 + "_" + u1;
        }
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        khungDanhSáchTinNhắn = findViewById(R.id.containerChatMessages);
        khungCuộnChat = findViewById(R.id.scrollChatArea);
        ôNhậpTinNhắn = findViewById(R.id.et_chat_input);
        nútXácNhậnEscrow = findViewById(R.id.btnConfirmEscrow);
        nútBáoCáoLỗiEscrow = findViewById(R.id.btnReportEscrow);
        nhãnTrạngTháiEscrow = findViewById(R.id.tv_escrow_holding_amount);
        nhãnĐếmLùiEscrow = findViewById(R.id.tv_escrow_countdown_timer);
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

        String partnerClean = tríchXuấtUsernameSạch(tênNgườiBán);
        this.làChatTròChuyệnXãHội = (dữLiệuÝĐịnh != null && dữLiệuÝĐịnh.getBooleanExtra("EXTRA_IS_SOCIAL_CHAT", false))
                || (giáXuEscrow == 0 && (mãBíMậtVoucher.startsWith("CHAT-") || mãBíMậtVoucher.startsWith("P2P_") || "HOTLINE-CSKH-SUPPORT".equals(mãBíMậtVoucher)));

        // CHUẨN HÓA MÃ PHÒNG CHAT ĐỐI XỨNG BẠN BÈ/NGƯỜI LẠ P2P AN TOÀN
        if (làChatTròChuyệnXãHội && !"HOTLINE-CSKH-SUPPORT".equals(mãBíMậtVoucher) && !partnerClean.isEmpty()) {
            mãBíMậtVoucher = tạoMãPhòngChatBạnBèĐốiXứng(usernameHiệnTại, partnerClean);
        }

        idPhòngChat = "CHAT_ROOM_" + mãBíMậtVoucher;

        // Đánh dấu người dùng hiện tại ĐÃ ĐỌC phòng chat này
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        bộLưuTrữVí.edit().putBoolean("IS_READ_ROOM_" + idPhòngChat + "_" + usernameHiệnTại.toLowerCase().trim(), true).apply();

        // Chống tự nhắn tin giao dịch Ký quỹ với chính bản thân (ngoại trừ Kênh Hỗ Trợ CSKH Hotline)
        boolean làKênhHỗTrợCSKH = "HOTLINE-CSKH-SUPPORT".equals(mãBíMậtVoucher) || giáXuEscrow == 0;
        if (!làKênhHỗTrợCSKH && tênNgườiBán != null && (tênNgườiBán.toLowerCase().contains(usernameHiệnTại.toLowerCase()) || tênNgườiBán.equalsIgnoreCase(họTênHiệnTại))) {
            Toast.makeText(this, "⚠️ Bạn không thể tự mở phòng Chat Ký quỹ với chính mình!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (nhãnTênĐốiTác != null) nhãnTênĐốiTác.setText(tênNgườiBán);

        // Tự động tải tên đầy đủ mới nhất của Đối tác từ Cloud Firestore lên Header phòng Chat
        final String finalPartnerClean = partnerClean;
        if (!finalPartnerClean.isEmpty()) {
            vn.edu.ueh.baoho.ssharing.utils.SocialHelper.getUserFullNameFromCloud(this, finalPartnerClean, latestName -> {
                if (latestName != null && !latestName.trim().isEmpty() && nhãnTênĐốiTác != null) {
                    nhãnTênĐốiTác.setText(latestName + " (@" + finalPartnerClean + ")");
                }
            });
        }
        if (nhãnAvatarĐốiTác != null) nhãnAvatarĐốiTác.setText(avatarNgườiBán);
        if (nhãnTiêuĐềVoucher != null) nhãnTiêuĐềVoucher.setText(tênVoucher);
        if (nhãnMãBíMậtVoucher != null) nhãnMãBíMậtVoucher.setText("Mã bí mật: " + mãBíMậtVoucher);

        // Nạp ảnh đại diện thật của đối tác lên Header phòng Chat nếu có
        ImageView imgPartnerHeader = findViewById(R.id.imgPartnerAvatarHeader);
        if (imgPartnerHeader != null) {
            vn.edu.ueh.baoho.ssharing.utils.SocialHelper.loadUserAvatar(this, imgPartnerHeader, partnerClean);
        }

        // Đọc & Cập nhật chỉ số Tín Nhiệm Uy Tín thật của đối tác
        TextView nhãnUyTínĐốiTác = findViewById(R.id.tv_partner_trust);
        if (nhãnUyTínĐốiTác != null) {
            nhãnUyTínĐốiTác.setText("96% Uy tín  ★ 4.8 (eKYC)");
        }

        // KÍCH HOẠT VÀ ĐĂNG KÝ NGAY PHÒNG CHAT VÀO HỘP THOẠI CHUNG NGAY KHI MỞ MÀN HÌNH (KỂ CẢ CHƯA GỬI TIN NHẮN)
        String thờiGianKhởiTạo = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String nộiDungBanĐầu = làChatTròChuyệnXãHội ? "Trò chuyện bạn bè cộng đồng" : "🔒 Đã khóa " + giáXuEscrow + " SS vào ví Escrow";
        đăngKýPhòngChatVàoDanhSáchChung(idPhòngChat, mãBíMậtVoucher, tênVoucher, usernameHiệnTại, họTênHiệnTại, tênNgườiBán, avatarNgườiBán, giáXuEscrow, nộiDungBanĐầu, thờiGianKhởiTạo);

        if (làChatTròChuyệnXãHội) {
            View cardEscrowStatus = findViewById(R.id.cardEscrowStatus);
            View cardVoucherInfo = findViewById(R.id.cardVoucherInfo);
            if (cardEscrowStatus != null) cardEscrowStatus.setVisibility(View.GONE);
            if (cardVoucherInfo != null) cardVoucherInfo.setVisibility(View.GONE);
            if (nhãnThôngBáoHệThốngĐầu != null) nhãnThôngBáoHệThốngĐầu.setVisibility(View.GONE);
            if (nútXácNhậnEscrow != null) nútXácNhậnEscrow.setVisibility(View.GONE);
            if (nútBáoCáoLỗiEscrow != null) nútBáoCáoLỗiEscrow.setVisibility(View.GONE);
            if (ôNhậpTinNhắn != null) {
                ôNhậpTinNhắn.setEnabled(true);
                ôNhậpTinNhắn.setHint("Nhập tin nhắn trò chuyện...");
            }
            View btnSend = findViewById(R.id.btn_send_chat);
            View btnAttach = findViewById(R.id.btn_attach_image_chat);
            if (btnSend != null) btnSend.setVisibility(View.VISIBLE);
            if (btnAttach != null) btnAttach.setVisibility(View.VISIBLE);
        } else {
            // Trường hợp Mua Bán / Trao Đổi Voucher Ký Quỹ Escrow Thực Tế
            String danhSáchĐãBánHệThống = bộLưuTrữVí.getString("DANH_SACH_VOUCHER_DA_BAN_HE_THONG", "");
            boolean làPhòngĐãKhóa = bộLưuTrữVí.getBoolean("IS_LOCKED_ROOM_" + idPhòngChat, false)
                    || (giáXuEscrow > 0 && vn.edu.ueh.baoho.ssharing.utils.SocialHelper.isVoucherCodeExactSold(danhSáchĐãBánHệThống, mãBíMậtVoucher));
            this.làPhòngĐãKhóaThựcTế = làPhòngĐãKhóa;

            if (làPhòngĐãKhóa) {
                if (nhãnTrạngTháiEscrow != null) {
                    nhãnTrạngTháiEscrow.setText("⚖️ Giao dịch đã kết thúc & hoàn tất");
                    nhãnTrạngTháiEscrow.setTextColor(Color.parseColor("#00E676"));
                }
                if (nhãnĐếmLùiEscrow != null) {
                    nhãnĐếmLùiEscrow.setText("✓ Khóa lưu trữ tự động sau 3 ngày");
                }
                if (nútXácNhậnEscrow != null) {
                    nútXácNhậnEscrow.setEnabled(false);
                    nútXácNhậnEscrow.setText("✓ Giao Dịch Đã Hoàn Tất (Đã Khóa)");
                }
                if (nútBáoCáoLỗiEscrow != null) {
                    nútBáoCáoLỗiEscrow.setVisibility(View.GONE);
                }
                if (ôNhậpTinNhắn != null) {
                    ôNhậpTinNhắn.setEnabled(false);
                    ôNhậpTinNhắn.setHint("🔒 Đoạn chat giao dịch đã hoàn tất và tự động khóa.");
                }
                View btnSend = findViewById(R.id.btn_send_chat);
                View btnAttach = findViewById(R.id.btn_attach_image_chat);
                if (btnSend != null) btnSend.setVisibility(View.GONE);
                if (btnAttach != null) btnAttach.setVisibility(View.GONE);
            } else {
                // ĐOẠN CHAT ĐANG TRONG TIẾN TRÌNH GIAO DỊCH ESCROW SỐNG (BẬT MỞ 100%)
                partnerClean = tríchXuấtUsernameSạch(tênNgườiBán);
                boolean làNgườiBánCủaGiaoDịch = usernameHiệnTại.equalsIgnoreCase(partnerClean)
                        || (tênNgườiBán != null && tênNgườiBán.toLowerCase().contains(usernameHiệnTại.toLowerCase()));

                if (nhãnTrạngTháiEscrow != null) {
                    nhãnTrạngTháiEscrow.setText("🛡️ Đang tạm giữ Escrow: " + String.format("%,d", giáXuEscrow) + " SS");
                    nhãnTrạngTháiEscrow.setTextColor(Color.parseColor("#FFD54F"));
                }

                if (ôNhậpTinNhắn != null) {
                    ôNhậpTinNhắn.setEnabled(true);
                    ôNhậpTinNhắn.setHint("Nhập tin nhắn trao đổi...");
                }
                View btnSend = findViewById(R.id.btn_send_chat);
                View btnAttach = findViewById(R.id.btn_attach_image_chat);
                if (btnSend != null) btnSend.setVisibility(View.VISIBLE);
                if (btnAttach != null) btnAttach.setVisibility(View.VISIBLE);

                if (làNgườiBánCủaGiaoDịch) {
                    if (nútXácNhậnEscrow != null) {
                        nútXácNhậnEscrow.setEnabled(true);
                        nútXácNhậnEscrow.setText("💸 Hoàn Xu Cho Khách Hàng (Tự Nguyện)");
                        nútXácNhậnEscrow.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FFD54F")));
                        nútXácNhậnEscrow.setTextColor(Color.parseColor("#120E22"));
                    }
                    if (nútBáoCáoLỗiEscrow != null) {
                        nútBáoCáoLỗiEscrow.setVisibility(View.VISIBLE);
                        nútBáoCáoLỗiEscrow.setText("⚖️ Nộp Bằng Chứng Trọng Tài CSKH");
                        nútBáoCáoLỗiEscrow.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#815BF5")));
                        nútBáoCáoLỗiEscrow.setTextColor(Color.WHITE);
                    }
                    if (nhãnThôngBáoHệThốngĐầu != null) {
                        nhãnThôngBáoHệThốngĐầu.setText("🛡️ Hệ thống: Người mua đang tạm giữ " + giáXuEscrow + " SS vào ví Escrow. Người mua có 24 giờ để kiểm tra mã trước khi giải ngân.");
                    }
                } else {
                    if (nútXácNhậnEscrow != null) {
                        nútXácNhậnEscrow.setEnabled(true);
                        nútXácNhậnEscrow.setText("✓ Xác Nhận Nhận Voucher & Giải Ngân Xu");
                        nútXácNhậnEscrow.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#00E676")));
                        nútXácNhậnEscrow.setTextColor(Color.WHITE);
                    }
                    if (nútBáoCáoLỗiEscrow != null) {
                        nútBáoCáoLỗiEscrow.setVisibility(View.VISIBLE);
                        nútBáoCáoLỗiEscrow.setText("⚠️ Báo Lỗi / Khiếu Nại Tranh Chấp");
                        nútBáoCáoLỗiEscrow.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF5252")));
                        nútBáoCáoLỗiEscrow.setTextColor(Color.WHITE);
                    }
                    if (nhãnThôngBáoHệThốngĐầu != null) {
                        nhãnThôngBáoHệThốngĐầu.setText("🛡️ Hệ thống: Bạn đã khóa " + giáXuEscrow + " SS vào ví Escrow. Bạn có 24 giờ để kiểm tra mã trước khi tiền giải ngân cho " + tênNgườiBán + ".");
                    }
                }
                khởiĐộngBộĐếmLùiEscrow24GiờThựcTế();
            }
        }
    }

    private void khởiĐộngBộĐếmLùiEscrow24GiờThựcTế() {
        if (bộĐếmLùiEscrowTimer != null) {
            bộĐếmLùiEscrowTimer.cancel();
        }

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String keyThờiGianBắtĐầu = "ESCROW_START_TIME_" + idPhòngChat;
        long thờiGianBắtĐầu = bộLưuTrữVí.getLong(keyThờiGianBắtĐầu, 0L);

        if (thờiGianBắtĐầu == 0L) {
            thờiGianBắtĐầu = System.currentTimeMillis();
            bộLưuTrữVí.edit().putLong(keyThờiGianBắtĐầu, thờiGianBắtĐầu).apply();
        }

        long tổngThờiGian24GiờMs = 24 * 60 * 60 * 1000L; // 24 Giờ = 86,400,000 ms
        long thờiGianHiệnTại = System.currentTimeMillis();
        long thờiGianCònLạiMs = (thờiGianBắtĐầu + tổngThờiGian24GiờMs) - thờiGianHiệnTại;

        if (thờiGianCònLạiMs <= 0) {
            if (nhãnĐếmLùiEscrow != null) {
                nhãnĐếmLùiEscrow.setText("⏱ Hết 24 giờ - Tự động giải phóng xu");
            }
            thựcHiệnHoànTấtEscrow();
            return;
        }

        bộĐếmLùiEscrowTimer = new android.os.CountDownTimer(thờiGianCònLạiMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long tổngSốGiâyCònLại = millisUntilFinished / 1000;
                long sốGiờ = tổngSốGiâyCònLại / 3600;
                long sốPhút = (tổngSốGiâyCònLại % 3600) / 60;
                long sốGiây = tổngSốGiâyCònLại % 60;

                if (nhãnĐếmLùiEscrow != null) {
                    nhãnĐếmLùiEscrow.setText(String.format(Locale.getDefault(), "⏱ Còn lại %02dg %02dp %02ds để thử mã (24 giờ)", sốGiờ, sốPhút, sốGiây));
                }
            }

            @Override
            public void onFinish() {
                if (nhãnĐếmLùiEscrow != null) {
                    nhãnĐếmLùiEscrow.setText("⏱ Hết 24 giờ - Tự động giải phóng xu");
                }
                thựcHiệnHoànTấtEscrow();
            }
        }.start();
    }

    private void càiĐặtLắngNgheBànPhímẨnHiệnNútThaoTác() {
        View rootView = getWindow().getDecorView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            android.graphics.Rect r = new android.graphics.Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;

            boolean isKeyboardNowOpen = keypadHeight > screenHeight * 0.15; // > 15% màn hình = Bàn phím đang mở

            if (isKeyboardNowOpen != làBànPhímĐangMở) {
                làBànPhímĐangMở = isKeyboardNowOpen;
                if (làBànPhímĐangMở) {
                    if (nútXácNhậnEscrow != null) nútXácNhậnEscrow.setVisibility(View.GONE);
                    if (nútBáoCáoLỗiEscrow != null) nútBáoCáoLỗiEscrow.setVisibility(View.GONE);
                } else {
                    if (!làPhòngĐãKhóaThựcTế && !làChatTròChuyệnXãHội) {
                        if (nútXácNhậnEscrow != null) nútXácNhậnEscrow.setVisibility(View.VISIBLE);
                        if (nútBáoCáoLỗiEscrow != null) nútBáoCáoLỗiEscrow.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void moBanPhimInput() {
        if (ôNhậpTinNhắn == null) return;

        if (nútXácNhậnEscrow != null) nútXácNhậnEscrow.setVisibility(View.GONE);
        if (nútBáoCáoLỗiEscrow != null) nútBáoCáoLỗiEscrow.setVisibility(View.GONE);

        ôNhậpTinNhắn.setFocusable(true);
        ôNhậpTinNhắn.setFocusableInTouchMode(true);
        ôNhậpTinNhắn.requestFocus();
        ôNhậpTinNhắn.postDelayed(() -> {
            try {
                android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(ôNhậpTinNhắn, android.view.inputmethod.InputMethodManager.SHOW_FORCED);
                }
            } catch (Throwable ignored) {}
        }, 100);
    }

    private void càiĐặtSựKiệnLắngNghe() {
        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        if (findViewById(R.id.btnCopyVoucherCode) != null) {
            findViewById(R.id.btnCopyVoucherCode).setOnClickListener(v -> {
                ClipboardManager khayNhớTạm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData dữLiệuMã = ClipData.newPlainText("MãVoucher", mãBíMậtVoucher);
                if (khayNhớTạm != null) {
                    khayNhớTạm.setPrimaryClip(dữLiệuMã);
                    Toast.makeText(this, "Đã sao chép mã: " + mãBíMậtVoucher, Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Ô nhập tin nhắn Chat - Tự động kích hoạt Bàn phím Soft Keyboard linh hoạt
        if (ôNhậpTinNhắn != null) {
            ôNhậpTinNhắn.setFocusable(true);
            ôNhậpTinNhắn.setFocusableInTouchMode(true);
            ôNhậpTinNhắn.setOnClickListener(v -> moBanPhimInput());
            ôNhậpTinNhắn.setOnTouchListener((v, event) -> {
                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    v.performClick();
                    moBanPhimInput();
                }
                return false;
            });
            ôNhậpTinNhắn.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    moBanPhimInput();
                }
            });
        }

        // Nút Đính kèm ảnh trong Chat
        if (findViewById(R.id.btn_attach_image_chat) != null) {
            findViewById(R.id.btn_attach_image_chat).setOnClickListener(v -> mởBộSưuTậpGửiẢnhChat());
        }

        // Nút Gửi tin nhắn thực tế P2P
        if (findViewById(R.id.btn_send_chat) != null) {
            findViewById(R.id.btn_send_chat).setOnClickListener(v -> thựcHiệnGửiTinNhắnP2PThựcTế());
        }

        if (nútXácNhậnEscrow != null) {
            nútXácNhậnEscrow.setOnClickListener(v -> xácNhậnGiảiPhóngEscrow());
        }

        if (nútBáoCáoLỗiEscrow != null) {
            nútBáoCáoLỗiEscrow.setOnClickListener(v -> {
                Toast.makeText(this, "Chuyển tới Trung Tâm Tiếp Nhận Khiếu Nại Ký Quỹ...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, DisputeClaimActivity.class));
            });
        }
    }

    private void mởBộSưuTậpGửiẢnhChat() {
        Intent ýĐịnhChọnẢnh = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(ýĐịnhChọnẢnh, MÃ_YÊU_CẦU_GỬI_ẢNH_CHAT);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở Bộ sưu tập ảnh.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MÃ_YÊU_CẦU_GỬI_ẢNH_CHAT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uriẢnhGửi = data.getData();
            File tệpẢnhChat = new File(getFilesDir(), "chat_img_" + System.currentTimeMillis() + ".jpg");

            try (InputStream inputStream = getContentResolver().openInputStream(uriẢnhGửi);
                 FileOutputStream outputStream = new FileOutputStream(tệpẢnhChat)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                thựcHiệnGửiGóiNộiDungTinNhắn("IMG:" + tệpẢnhChat.getAbsolutePath());
                Toast.makeText(this, "📷 Đã gửi hình ảnh trong phòng chat!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Không thể gửi ảnh trong phòng chat.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void khởiĐộngTảiTinNhắnRealtime() {
        côngViệcTảiTinNhắnTựĐộng = new Runnable() {
            @Override
            public void run() {
                cậpNhậtHiểnThịDanhSáchTinNhắnRealtime();
                bộCậpNhậtChatRealtime.postDelayed(this, 2000);
            }
        };
        bộCậpNhậtChatRealtime.post(côngViệcTảiTinNhắnTựĐộng);
    }

    public static class ChatMessageItem {
        public String senderUsername;
        public String senderName;
        public String senderAvatar;
        public String messageText;
        public String timeStr;
        public long timestamp;

        public ChatMessageItem(String senderUsername, String senderName, String senderAvatar, String messageText, String timeStr, long timestamp) {
            this.senderUsername = senderUsername != null ? senderUsername : "";
            this.senderName = senderName != null ? senderName : "";
            this.senderAvatar = senderAvatar != null ? senderAvatar : "SS";
            this.messageText = messageText != null ? messageText : "";
            this.timeStr = timeStr != null ? timeStr : "Vừa xong";
            this.timestamp = timestamp > 0 ? timestamp : System.currentTimeMillis();
        }
    }

    public static java.util.List<ChatMessageItem> parseMessageList(String rawData) {
        java.util.List<ChatMessageItem> list = new java.util.ArrayList<>();
        if (rawData == null || rawData.trim().isEmpty()) return list;

        String trimmed = rawData.trim();
        if (trimmed.startsWith("[")) {
            try {
                org.json.JSONArray array = new org.json.JSONArray(trimmed);
                for (int i = 0; i < array.length(); i++) {
                    org.json.JSONObject obj = array.getJSONObject(i);
                    list.add(new ChatMessageItem(
                            obj.optString("u", ""),
                            obj.optString("n", ""),
                            obj.optString("a", "SS"),
                            obj.optString("m", ""),
                            obj.optString("t", "Vừa xong"),
                            obj.optLong("ts", System.currentTimeMillis())
                    ));
                }
                return list;
            } catch (Exception ignored) {}
        }

        String[] items = trimmed.split(";;");
        for (String item : items) {
            if (item.trim().isEmpty()) continue;
            String[] fields = item.split("\\|\\|");
            if (fields.length >= 4) {
                String u = fields[0];
                String n = fields[1];
                String a = fields[2];
                String m = fields[3];
                String t = fields.length > 4 ? fields[4] : "Vừa xong";
                list.add(new ChatMessageItem(u, n, a, m, t, System.currentTimeMillis()));
            }
        }
        return list;
    }

    public static String serializeMessageList(java.util.List<ChatMessageItem> list) {
        if (list == null || list.isEmpty()) return "[]";
        try {
            org.json.JSONArray array = new org.json.JSONArray();
            for (ChatMessageItem item : list) {
                org.json.JSONObject obj = new org.json.JSONObject();
                obj.put("u", item.senderUsername);
                obj.put("n", item.senderName);
                obj.put("a", item.senderAvatar);
                obj.put("m", item.messageText);
                obj.put("t", item.timeStr);
                obj.put("ts", item.timestamp);
                array.put(obj);
            }
            return array.toString();
        } catch (Exception e) {
            return "[]";
        }
    }

    private void kíchHoạtLắngNgheTinNhắnFirestoreRealtime() {
        try {
            com.google.firebase.firestore.FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
            listenerFirestoreChat = db.collection("chat_rooms")
                    .document(idPhòngChat)
                    .collection("messages")
                    .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.ASCENDING)
                    .addSnapshotListener((snapshots, e) -> {
                        if (e != null || snapshots == null || snapshots.isEmpty()) return;

                        List<ChatMessageItem> cloudList = new ArrayList<>();
                        for (com.google.firebase.firestore.DocumentSnapshot doc : snapshots.getDocuments()) {
                            String senderUser = doc.getString("sender_username");
                            String senderName = doc.getString("sender_name");
                            String senderAvatar = doc.getString("sender_avatar");
                            String msgText = doc.getString("message_text");
                            String timeStr = doc.getString("time_str");
                            Long tsObj = doc.getLong("timestamp");

                            if (senderUser != null && msgText != null && !msgText.trim().isEmpty()) {
                                cloudList.add(new ChatMessageItem(
                                        senderUser,
                                        senderName != null ? senderName : senderUser,
                                        senderAvatar != null ? senderAvatar : "SS",
                                        msgText,
                                        timeStr != null ? timeStr : "Vừa xong",
                                        tsObj != null ? tsObj : System.currentTimeMillis()
                                ));
                            }
                        }

                        if (!cloudList.isEmpty()) {
                            String serialized = serializeMessageList(cloudList);
                            SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
                            bộLưuTrữVí.edit().putString("DANH_SACH_TIN_NHAN_" + idPhòngChat, serialized).apply();

                            runOnUiThread(() -> {
                                cậpNhậtHiểnThịDanhSáchTinNhắnRealtime();
                                cuộnXuốngĐáyChat();
                            });
                        }
                    });
        } catch (Exception ignored) {}
    }

    private void thựcHiệnGửiTinNhắnP2PThựcTế() {
        if (ôNhậpTinNhắn == null) return;
        String nộiDungMới = ôNhậpTinNhắn.getText().toString().trim();
        if (nộiDungMới.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung tin nhắn!", Toast.LENGTH_SHORT).show();
            return;
        }

        thựcHiệnGửiGóiNộiDungTinNhắn(nộiDungMới);
        ôNhậpTinNhắn.setText("");
    }

    private void thựcHiệnGửiGóiNộiDungTinNhắn(String nộiDungGói) {
        String thờiGianThực = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String avatarInitials = "GB";
        if (họTênHiệnTại != null && họTênHiệnTại.length() >= 2) {
            String[] mảngTừ = họTênHiệnTại.trim().split(" ");
            if (mảngTừ.length >= 2) {
                avatarInitials = (mảngTừ[0].substring(0, 1) + mảngTừ[mảngTừ.length - 1].substring(0, 1)).toUpperCase();
            } else {
                avatarInitials = họTênHiệnTại.substring(0, 2).toUpperCase();
            }
        }

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String khoTinNhắnGốc = bộLưuTrữVí.getString("DANH_SACH_TIN_NHAN_" + idPhòngChat, "");
        List<ChatMessageItem> listMsgs = parseMessageList(khoTinNhắnGốc);

        ChatMessageItem newMsg = new ChatMessageItem(
                usernameHiệnTại,
                họTênHiệnTại,
                avatarInitials,
                nộiDungGói,
                thờiGianThực,
                System.currentTimeMillis()
        );
        listMsgs.add(newMsg);

        String serialized = serializeMessageList(listMsgs);
        bộLưuTrữVí.edit().putString("DANH_SACH_TIN_NHAN_" + idPhòngChat, serialized).apply();

        String hiểnThịHiệuỨngInbox = nộiDungGói.startsWith("IMG:") ? "📷 [Hình ảnh]" : nộiDungGói;

        // Đăng ký/cập nhật phòng chat vào danh sách Hộp Thoại Inbox dùng chung
        đăngKýPhòngChatVàoDanhSáchChung(idPhòngChat, mãBíMậtVoucher, tênVoucher, usernameHiệnTại, họTênHiệnTại, tênNgườiBán, avatarNgườiBán, giáXuEscrow, hiểnThịHiệuỨngInbox, thờiGianThực);

        // Đánh dấu CHƯA ĐỌC cho đối tác nhận tin nhắn
        String partnerClean = tríchXuấtUsernameSạch(tênNgườiBán);
        if (!partnerClean.isEmpty()) {
            bộLưuTrữVí.edit().putBoolean("IS_READ_ROOM_" + idPhòngChat + "_" + partnerClean, false).apply();
        }

        // Đồng bộ tin nhắn mới lên Firebase Firestore cho phòng chat P2P
        đồngBộTinNhắnLênFirebaseFirestore(idPhòngChat, usernameHiệnTại, họTênHiệnTại, avatarInitials, nộiDungGói, thờiGianThực);

        cậpNhậtHiểnThịDanhSáchTinNhắnRealtime();
        cuộnXuốngĐáyChat();
    }

    private void đăngKýPhòngChatVàoDanhSáchChung(String idPhòng, String mãCode, String tênVoucher, String buyerUsername, String buyerName, String sellerName, String sellerAvatar, int giáXu, String nộiDungTin, String thờiGian) {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String danhSáchGốc = bộLưuTrữVí.getString("DANH_SACH_PHONG_CHAT_TAT_CA", "");

        String bảnGhiMới = idPhòng + "||" + mãCode + "||" + tênVoucher + "||" + buyerUsername + "||" + buyerName + "||" + sellerName + "||" + sellerAvatar + "||" + giáXu + "||" + nộiDungTin + "||" + thờiGian;

        StringBuilder danhSáchMới = new StringBuilder();
        danhSáchMới.append(bảnGhiMới);

        if (!danhSáchGốc.isEmpty()) {
            String[] cácPhòng = danhSáchGốc.split(";;;");
            for (String phòng : cácPhòng) {
                if (phòng.trim().isEmpty() || phòng.startsWith(idPhòng)) continue;
                danhSáchMới.append(";;;").append(phòng);
            }
        }

        bộLưuTrữVí.edit().putString("DANH_SACH_PHONG_CHAT_TAT_CA", danhSáchMới.toString()).apply();

        // ĐỒNG BỘ INBOX LÊN CLOUD FIRESTORE CHO CẢ 2 BÊN (BUYER VÀ SELLER/TARGET USER)
        try {
            com.google.firebase.firestore.FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
            Map<String, Object> roomData = new HashMap<>();
            roomData.put("idPhong", idPhòng);
            roomData.put("maCode", mãCode);
            roomData.put("tenVoucher", tênVoucher);
            roomData.put("buyerUsername", buyerUsername);
            roomData.put("buyerName", buyerName);
            roomData.put("sellerName", sellerName);
            roomData.put("sellerAvatar", sellerAvatar);
            roomData.put("giaXu", giáXu);
            roomData.put("noiDungTin", nộiDungTin);
            roomData.put("thoiGian", thờiGian);
            roomData.put("updatedAt", System.currentTimeMillis());

            // Đẩy vào Cloud Inbox cho User A
            String cleanBuyer = buyerUsername.toLowerCase().trim();
            if (!cleanBuyer.isEmpty()) {
                db.collection("users").document(cleanBuyer).collection("chat_inbox").document(idPhòng).set(roomData);
            }

            // Đẩy vào Cloud Inbox cho User B (Target User / Seller)
            String cleanPartner = tríchXuấtUsernameSạch(sellerName);
            if (!cleanPartner.isEmpty()) {
                db.collection("users").document(cleanPartner).collection("chat_inbox").document(idPhòng).set(roomData);
            }
        } catch (Exception ignored) {}
    }

    private void đồngBộTinNhắnLênFirebaseFirestore(String idPhòng, String username, String họTên, String avatar, String nộiDung, String thờiGian) {
        try {
            com.google.firebase.firestore.FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
            Map<String, Object> msgData = new HashMap<>();
            msgData.put("sender_username", username);
            msgData.put("sender_name", họTên);
            msgData.put("sender_avatar", avatar);
            msgData.put("message_text", nộiDung);
            msgData.put("time_str", thờiGian);
            msgData.put("timestamp", System.currentTimeMillis());

            db.collection("chat_rooms").document(idPhòng).collection("messages").add(msgData);
        } catch (Exception ignored) {}
    }

    private final Map<String, Bitmap> bộNhớĐệmBitmapChat = new HashMap<>();
    private String chuỗiTinNhắnCũĐãVẽ = null;

    private Bitmap lấyBitmapTừBộNhớĐệmChat(String đườngDẫn) {
        if (đườngDẫn == null || đườngDẫn.isEmpty()) return null;
        if (bộNhớĐệmBitmapChat.containsKey(đườngDẫn)) {
            return bộNhớĐệmBitmapChat.get(đườngDẫn);
        }
        try {
            Bitmap bm = BitmapFactory.decodeFile(đườngDẫn);
            if (bm != null) {
                bộNhớĐệmBitmapChat.put(đườngDẫn, bm);
                return bm;
            }
        } catch (Throwable ignored) {}
        return null;
    }

    private void cậpNhậtHiểnThịDanhSáchTinNhắnRealtime() {
        if (khungDanhSáchTinNhắn == null) return;

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String khoTinNhắnPhòngChat = bộLưuTrữVí.getString("DANH_SACH_TIN_NHAN_" + idPhòngChat, "");

        if (chuỗiTinNhắnCũĐãVẽ != null && khoTinNhắnPhòngChat.equals(chuỗiTinNhắnCũĐãVẽ) && !khoTinNhắnPhòngChat.isEmpty()) {
            return;
        }

        // Tự động khởi tạo tin nhắn chào mừng cho phòng chat Escrow nếu kho trống
        if (khoTinNhắnPhòngChat.trim().isEmpty() && !làChatTròChuyệnXãHội) {
            String timeShort = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            String partnerClean = tríchXuấtUsernameSạch(tênNgườiBán);
            List<ChatMessageItem> initialList = new ArrayList<>();
            initialList.add(new ChatMessageItem(
                    "sys", "Hệ thống", "SS",
                    "🛡️ Hệ thống: Người mua @" + usernameHiệnTại + " đã khóa " + giáXuEscrow + " SS vào ví Escrow. Hai bên có 24 giờ để kiểm tra và trao đổi mã.",
                    timeShort, System.currentTimeMillis() - 1000
            ));
            initialList.add(new ChatMessageItem(
                    usernameHiệnTại, họTênHiệnTại, "NM",
                    "Xin chào @" + partnerClean + ", tôi vừa mở giao dịch Escrow để mua voucher '" + tênVoucher + "'. Bạn vui lòng gửi mã/hướng dẫn sử dụng nhé!",
                    timeShort, System.currentTimeMillis()
            ));
            khoTinNhắnPhòngChat = serializeMessageList(initialList);
            bộLưuTrữVí.edit().putString("DANH_SACH_TIN_NHAN_" + idPhòngChat, khoTinNhắnPhòngChat).apply();
        }

        chuỗiTinNhắnCũĐãVẽ = khoTinNhắnPhòngChat;

        List<ChatMessageItem> list = parseMessageList(khoTinNhắnPhòngChat);

        // Dọn sạch hoàn toàn danh sách tin nhắn cũ trước khi vẽ lại
        khungDanhSáchTinNhắn.removeAllViews();

        if (list.isEmpty()) {
            return;
        }

        for (ChatMessageItem item : list) {
            String ngườiGửiUsername = item.senderUsername;
            String avatarText = item.senderAvatar;
            String nộiDungGửi = item.messageText;
            String thờiGianGửi = item.timeStr;

            if (nộiDungGửi == null || nộiDungGửi.trim().isEmpty()) continue;

            if ("sys".equalsIgnoreCase(ngườiGửiUsername) || "system".equalsIgnoreCase(ngườiGửiUsername)) {
                if (!làChatTròChuyệnXãHội) {
                    thêmBongBóngThôngBáoHệThốngMiddle(nộiDungGửi);
                }
            } else if (ngườiGửiUsername.equalsIgnoreCase(usernameHiệnTại)) {
                // Bong bóng chat bên PHẢI
                if (nộiDungGửi.startsWith("IMG:")) {
                    thêmBongBóngHìnhẢnhBênPhải(nộiDungGửi.substring(4), thờiGianGửi);
                } else {
                    thêmBongBóngTinNhắnBênPhải(nộiDungGửi, thờiGianGửi);
                }
            } else {
                // Bong bóng chat bên TRÁI (Đối tác P2P nhắn)
                if (nộiDungGửi.startsWith("IMG:")) {
                    thêmBongBóngHìnhẢnhBênTrái(ngườiGửiUsername, avatarText, nộiDungGửi.substring(4), thờiGianGửi);
                } else {
                    thêmBongBóngTinNhắnBênTrái(ngườiGửiUsername, avatarText, nộiDungGửi, thờiGianGửi);
                }
            }
        }
        cuộnXuốngĐáyChat();
    }

    private void thêmBongBóngThôngBáoHệThốngMiddle(String nộiDung) {
        TextView tvSystem = new TextView(this);
        tvSystem.setText(nộiDung);
        tvSystem.setTextColor(Color.parseColor("#E0E0E0"));
        tvSystem.setTextSize(11);
        tvSystem.setGravity(Gravity.CENTER);
        tvSystem.setBackgroundResource(R.drawable.bg_card_dark);
        tvSystem.setPadding(24, 16, 24, 16);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 16;
        params.bottomMargin = 8;
        tvSystem.setLayoutParams(params);

        if (khungDanhSáchTinNhắn != null) {
            khungDanhSáchTinNhắn.addView(tvSystem);
        }
    }

    private void thêmBongBóngTinNhắnBênPhải(String nộiDung, String thờiGian) {
        LinearLayout bongBóngRight = new LinearLayout(this);
        bongBóngRight.setOrientation(LinearLayout.VERTICAL);
        bongBóngRight.setBackgroundResource(R.drawable.bg_bubble_buyer);
        bongBóngRight.setPadding(24, 20, 24, 20);

        LinearLayout.LayoutParams thôngSốLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốLayout.gravity = Gravity.END;
        thôngSốLayout.topMargin = 16;
        bongBóngRight.setLayoutParams(thôngSốLayout);

        TextView nhãnNộiDung = new TextView(this);
        nhãnNộiDung.setText(nộiDung);
        nhãnNộiDung.setTextColor(Color.WHITE);
        nhãnNộiDung.setTextSize(13);

        TextView nhãnThờiGian = new TextView(this);
        nhãnThờiGian.setText(thờiGian + " ✓✓");
        nhãnThờiGian.setTextColor(Color.parseColor("#D1C4E9"));
        nhãnThờiGian.setTextSize(10);
        LinearLayout.LayoutParams thôngSốThờiGian = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốThờiGian.gravity = Gravity.END;
        thôngSốThờiGian.topMargin = 4;
        nhãnThờiGian.setLayoutParams(thôngSốThờiGian);

        bongBóngRight.addView(nhãnNộiDung);
        bongBóngRight.addView(nhãnThờiGian);

        if (khungDanhSáchTinNhắn != null) {
            khungDanhSáchTinNhắn.addView(bongBóngRight);
        }
    }

    private void thêmBongBóngHìnhẢnhBênPhải(String đườngDẫnẢnh, String thờiGian) {
        LinearLayout bongBóngRight = new LinearLayout(this);
        bongBóngRight.setOrientation(LinearLayout.VERTICAL);
        bongBóngRight.setBackgroundResource(R.drawable.bg_bubble_buyer);
        bongBóngRight.setPadding(16, 16, 16, 16);

        LinearLayout.LayoutParams thôngSốLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốLayout.gravity = Gravity.END;
        thôngSốLayout.topMargin = 16;
        bongBóngRight.setLayoutParams(thôngSốLayout);

        ImageView ảnhHiểnThị = new ImageView(this);
        Bitmap bitmap = lấyBitmapTừBộNhớĐệmChat(đườngDẫnẢnh);
        if (bitmap != null) {
            ảnhHiểnThị.setImageBitmap(bitmap);
        }
        ảnhHiểnThị.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams thôngSốẢnh = new LinearLayout.LayoutParams(400, 400);
        ảnhHiểnThị.setLayoutParams(thôngSốẢnh);
        ảnhHiểnThị.setOnClickListener(v -> mởXemHìnhẢnhĐầyMànHình(đườngDẫnẢnh));

        TextView nhãnThờiGian = new TextView(this);
        nhãnThờiGian.setText(thờiGian + " ✓✓");
        nhãnThờiGian.setTextColor(Color.parseColor("#D1C4E9"));
        nhãnThờiGian.setTextSize(10);
        LinearLayout.LayoutParams thôngSốThờiGian = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốThờiGian.gravity = Gravity.END;
        thôngSốThờiGian.topMargin = 4;
        nhãnThờiGian.setLayoutParams(thôngSốThờiGian);

        bongBóngRight.addView(ảnhHiểnThị);
        bongBóngRight.addView(nhãnThờiGian);

        if (khungDanhSáchTinNhắn != null) {
            khungDanhSáchTinNhắn.addView(bongBóngRight);
        }
    }

    private void thêmBongBóngTinNhắnBênTrái(String ngườiGửiUsername, String avatarText, String nộiDung, String thờiGian) {
        LinearLayout khungHàngBênTrái = new LinearLayout(this);
        khungHàngBênTrái.setOrientation(LinearLayout.HORIZONTAL);
        khungHàngBênTrái.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams thôngSốHàng = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốHàng.topMargin = 16;
        khungHàngBênTrái.setLayoutParams(thôngSốHàng);

        // Khung Avatar Đối Tác
        android.widget.FrameLayout khungAvatar = new android.widget.FrameLayout(this);
        LinearLayout.LayoutParams thôngSốAvatar = new LinearLayout.LayoutParams(72, 72);
        khungAvatar.setLayoutParams(thôngSốAvatar);

        TextView ôAvatar = new TextView(this);
        ôAvatar.setText(avatarText);
        ôAvatar.setTextColor(Color.WHITE);
        ôAvatar.setTextSize(11);
        ôAvatar.setGravity(Gravity.CENTER);
        ôAvatar.setBackgroundResource(R.drawable.bg_circle_dark_purple);
        android.widget.FrameLayout.LayoutParams thôngSốMatch = new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT
        );
        ôAvatar.setLayoutParams(thôngSốMatch);
        khungAvatar.addView(ôAvatar);

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String senderClean = tríchXuấtUsernameSạch(ngườiGửiUsername);
        String pathAvatar = bộLưuTrữVí.getString("CURRENT_USER_AVATAR_PATH_" + senderClean, "");
        if (!pathAvatar.isEmpty() && new File(pathAvatar).exists()) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(pathAvatar);
                if (bitmap != null) {
                    ImageView imgAvatarUser = new ImageView(this);
                    imgAvatarUser.setImageBitmap(bitmap);
                    imgAvatarUser.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imgAvatarUser.setLayoutParams(thôngSốMatch);
                    khungAvatar.addView(imgAvatarUser);
                }
            } catch (Throwable ignored) {}
        }

        LinearLayout bongBóngLeft = new LinearLayout(this);
        bongBóngLeft.setOrientation(LinearLayout.VERTICAL);
        bongBóngLeft.setBackgroundResource(R.drawable.bg_bubble_seller);
        bongBóngLeft.setPadding(24, 20, 24, 20);
        LinearLayout.LayoutParams thôngSốBongBóng = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốBongBóng.leftMargin = 16;
        bongBóngLeft.setLayoutParams(thôngSốBongBóng);

        TextView nhãnNộiDung = new TextView(this);
        nhãnNộiDung.setText(nộiDung);
        nhãnNộiDung.setTextColor(Color.WHITE);
        nhãnNộiDung.setTextSize(13);

        TextView nhãnThờiGian = new TextView(this);
        nhãnThờiGian.setText(thờiGian);
        nhãnThờiGian.setTextColor(Color.parseColor("#78718C"));
        nhãnThờiGian.setTextSize(10);
        LinearLayout.LayoutParams thôngSốThờiGian = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốThờiGian.gravity = Gravity.END;
        thôngSốThờiGian.topMargin = 4;
        nhãnThờiGian.setLayoutParams(thôngSốThờiGian);

        bongBóngLeft.addView(nhãnNộiDung);
        bongBóngLeft.addView(nhãnThờiGian);

        khungHàngBênTrái.addView(khungAvatar);
        khungHàngBênTrái.addView(bongBóngLeft);

        if (khungDanhSáchTinNhắn != null) {
            khungDanhSáchTinNhắn.addView(khungHàngBênTrái);
        }
    }

    private void thêmBongBóngHìnhẢnhBênTrái(String ngườiGửiUsername, String avatarText, String đườngDẫnẢnh, String thờiGian) {
        LinearLayout khungHàngBênTrái = new LinearLayout(this);
        khungHàngBênTrái.setOrientation(LinearLayout.HORIZONTAL);
        khungHàngBênTrái.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams thôngSốHàng = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốHàng.topMargin = 16;
        khungHàngBênTrái.setLayoutParams(thôngSốHàng);

        android.widget.FrameLayout khungAvatar = new android.widget.FrameLayout(this);
        LinearLayout.LayoutParams thôngSốAvatar = new LinearLayout.LayoutParams(72, 72);
        khungAvatar.setLayoutParams(thôngSốAvatar);

        TextView ôAvatar = new TextView(this);
        ôAvatar.setText(avatarText);
        ôAvatar.setTextColor(Color.WHITE);
        ôAvatar.setTextSize(11);
        ôAvatar.setGravity(Gravity.CENTER);
        ôAvatar.setBackgroundResource(R.drawable.bg_circle_dark_purple);
        android.widget.FrameLayout.LayoutParams thôngSốMatch = new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT
        );
        ôAvatar.setLayoutParams(thôngSốMatch);
        khungAvatar.addView(ôAvatar);

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String senderClean = tríchXuấtUsernameSạch(ngườiGửiUsername);
        String pathAvatar = bộLưuTrữVí.getString("CURRENT_USER_AVATAR_PATH_" + senderClean, "");
        if (!pathAvatar.isEmpty() && new File(pathAvatar).exists()) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(pathAvatar);
                if (bitmap != null) {
                    ImageView imgAvatarUser = new ImageView(this);
                    imgAvatarUser.setImageBitmap(bitmap);
                    imgAvatarUser.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imgAvatarUser.setLayoutParams(thôngSốMatch);
                    khungAvatar.addView(imgAvatarUser);
                }
            } catch (Throwable ignored) {}
        }

        LinearLayout bongBóngLeft = new LinearLayout(this);
        bongBóngLeft.setOrientation(LinearLayout.VERTICAL);
        bongBóngLeft.setBackgroundResource(R.drawable.bg_bubble_seller);
        bongBóngLeft.setPadding(16, 16, 16, 16);
        LinearLayout.LayoutParams thôngSốBongBóng = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốBongBóng.leftMargin = 16;
        bongBóngLeft.setLayoutParams(thôngSốBongBóng);

        ImageView ảnhHiểnThị = new ImageView(this);
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(đườngDẫnẢnh);
            if (bitmap != null) {
                ảnhHiểnThị.setImageBitmap(bitmap);
            }
        } catch (Throwable ignored) {}
        ảnhHiểnThị.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams thôngSốẢnh = new LinearLayout.LayoutParams(400, 400);
        ảnhHiểnThị.setLayoutParams(thôngSốẢnh);
        ảnhHiểnThị.setOnClickListener(v -> mởXemHìnhẢnhĐầyMànHình(đườngDẫnẢnh));

        TextView nhãnThờiGian = new TextView(this);
        nhãnThờiGian.setText(thờiGian);
        nhãnThờiGian.setTextColor(Color.parseColor("#78718C"));
        nhãnThờiGian.setTextSize(10);
        LinearLayout.LayoutParams thôngSốThờiGian = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        thôngSốThờiGian.gravity = Gravity.END;
        thôngSốThờiGian.topMargin = 4;
        nhãnThờiGian.setLayoutParams(thôngSốThờiGian);

        bongBóngLeft.addView(ảnhHiểnThị);
        bongBóngLeft.addView(nhãnThờiGian);

        khungHàngBênTrái.addView(khungAvatar);
        khungHàngBênTrái.addView(bongBóngLeft);

        if (khungDanhSáchTinNhắn != null) {
            khungDanhSáchTinNhắn.addView(khungHàngBênTrái);
        }
    }

    private void mởXemHìnhẢnhĐầyMànHình(String đườngDẫnẢnh) {
        ImageView ảnhTo = new ImageView(this);
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(đườngDẫnẢnh);
            if (bitmap != null) {
                ảnhTo.setImageBitmap(bitmap);
            }
        } catch (Throwable ignored) {}
        ảnhTo.setAdjustViewBounds(true);
        ảnhTo.setPadding(30, 30, 30, 30);

        new AlertDialog.Builder(this)
                .setTitle("🖼 Xem Ảnh Trong Phòng Chat")
                .setView(ảnhTo)
                .setPositiveButton("Đóng", null)
                .show();
    }

    private void xácNhậnGiảiPhóngEscrow() {
        String partnerClean = tríchXuấtUsernameSạch(tênNgườiBán);
        boolean làNgườiBánCủaGiaoDịch = usernameHiệnTại.equalsIgnoreCase(partnerClean)
                || (tênNgườiBán != null && tênNgườiBán.toLowerCase().contains(usernameHiệnTại.toLowerCase()));

        if (làNgườiBánCủaGiaoDịch) {
            new AlertDialog.Builder(this)
                    .setTitle("💸 Xác Nhận Hoàn Xu Tự Nguyện")
                    .setMessage("Bạn có chắc chắn muốn tự nguyện hoàn " + giáXuEscrow + " SS lại cho người mua?")
                    .setPositiveButton("Hoàn xu ngay", (dialog, which) -> thựcHiệnTựNguyệnHoànXuTừngNgườiBán())
                    .setNegativeButton("Hủy", null)
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Xác Nhận Giải Phóng Xu Escrow")
                    .setMessage("Bạn đã kiểm tra và xác nhận mã " + tênVoucher + " hoạt động tốt? Hệ thống sẽ giải phóng " + giáXuEscrow + " SS cho " + tênNgườiBán + ".")
                    .setPositiveButton("Giải phóng ngay", (hộpThoại, vịTrí) -> thựcHiệnHoànTấtEscrow())
                    .setNegativeButton("Hủy", null)
                    .show();
        }
    }

    private void thựcHiệnTựNguyệnHoànXuTừngNgườiBán() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);

        String danhSáchĐãBánGốc = bộLưuTrữVí.getString("DANH_SACH_VOUCHER_DA_BAN_HE_THONG", "");
        bộLưuTrữVí.edit()
                .putString("DANH_SACH_VOUCHER_DA_BAN_HE_THONG", mãBíMậtVoucher + ";;" + danhSáchĐãBánGốc)
                .putBoolean("IS_LOCKED_ROOM_" + idPhòngChat, true)
                .apply();

        thựcHiệnGửiGóiNộiDungTinNhắn("🛡️ Hệ thống: Người bán @" + usernameHiệnTại + " đã tự nguyện hoàn " + giáXuEscrow + " SS lại cho Người mua. Giao dịch đã khép lại!");

        if (ôNhậpTinNhắn != null) {
            ôNhậpTinNhắn.setEnabled(false);
            ôNhậpTinNhắn.setHint("🔒 Đoạn chat giao dịch đã hoàn tất và tự động khóa lưu trữ sau 1 ngày.");
        }
        View btnSend = findViewById(R.id.btn_send_chat);
        View btnAttach = findViewById(R.id.btn_attach_image_chat);
        if (btnSend != null) btnSend.setVisibility(View.GONE);
        if (btnAttach != null) btnAttach.setVisibility(View.GONE);

        if (nútXácNhậnEscrow != null) {
            nútXácNhậnEscrow.setEnabled(false);
            nútXácNhậnEscrow.setText("✓ Đã Hoàn Xu Cho Người Mua");
        }
        if (nútBáoCáoLỗiEscrow != null) {
            nútBáoCáoLỗiEscrow.setVisibility(View.GONE);
        }

        Toast.makeText(this, "💸 Đã tự nguyện hoàn " + giáXuEscrow + " SS lại cho Người Mua thành công!", Toast.LENGTH_LONG).show();
    }

    private void thựcHiệnHoànTấtEscrow() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String currentUser = vn.edu.ueh.baoho.ssharing.utils.WalletManager.getCurrentUsername(this);
        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        if (nhãnTrạngTháiEscrow != null) {
            nhãnTrạngTháiEscrow.setText("✓ Đã giải phóng: " + giáXuEscrow + " SS cho " + tênNgườiBán);
            nhãnTrạngTháiEscrow.setTextColor(Color.parseColor("#00E676"));
        }

        // 1. Thêm vào Ví Voucher của Người Mua
        String voucherMớiThêm = mãBíMậtVoucher + "::" + tênVoucher + "::HSD: 31 T12 2026::C2C";

        // 2. Thêm vào Lịch Sử Giao Dịch
        String mãGiaoDịchRandom = "TX-ESC" + (100000 + (int)(Math.random() * 899999));
        String bảnGhiGiaoDịchMới = mãGiaoDịchRandom + "||Giải phóng Escrow " + tênVoucher + "||-" + giáXuEscrow + " SS||" + thờiGianThực + "||Thanh toán Escrow||Thành công||Giải phóng " + giáXuEscrow + " SS cho người bán " + tênNgườiBán;

        // 3. Đánh dấu Voucher đã BÁN xong & Đơn hàng Escrow đã hoàn tất khóa
        String danhSáchĐãBánGốc = bộLưuTrữVí.getString("DANH_SACH_VOUCHER_DA_BAN_HE_THONG", "");
        String danhSáchĐãBánMới = mãBíMậtVoucher + ";;" + danhSáchĐãBánGốc;
        bộLưuTrữVí.edit()
                .putString("DANH_SACH_VOUCHER_DA_BAN_HE_THONG", danhSáchĐãBánMới)
                .putBoolean("IS_LOCKED_ROOM_" + idPhòngChat, true)
                .apply();

        // Cập nhật ví và lịch sử riêng từng tài khoản
        vn.edu.ueh.baoho.ssharing.utils.WalletManager.addBalance(this, currentUser, -giáXuEscrow);
        vn.edu.ueh.baoho.ssharing.utils.WalletManager.addSavedVoucherRecord(this, currentUser, voucherMớiThêm, mãBíMậtVoucher, tênVoucher);
        vn.edu.ueh.baoho.ssharing.utils.WalletManager.addTransactionRecord(this, currentUser, bảnGhiGiaoDịchMới, mãGiaoDịchRandom, "Giải phóng Escrow " + tênVoucher, -giáXuEscrow, "ESCROW_RELEASE", "SUCCESS", "Giải phóng " + giáXuEscrow + " SS cho " + tênNgườiBán);

        // Cộng tiền cho Ví Người Bán
        String sellerUser = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.cleanUsername(tênNgườiBán);
        if (!sellerUser.isEmpty()) {
            String bảnGhiBán = mãGiaoDịchRandom + "||Bán Voucher C2C " + tênVoucher + "||+" + giáXuEscrow + " SS||" + thờiGianThực + "||Nhận tiền Escrow||Thành công||Nhận " + giáXuEscrow + " SS từ người mua " + currentUser;
            vn.edu.ueh.baoho.ssharing.utils.WalletManager.addBalance(this, sellerUser, +giáXuEscrow);
            vn.edu.ueh.baoho.ssharing.utils.WalletManager.addTransactionRecord(this, sellerUser, bảnGhiBán, mãGiaoDịchRandom, "Bán Voucher C2C " + tênVoucher, giáXuEscrow, "ESCROW_INCOME", "SUCCESS", "Nhận tiền bán voucher từ " + currentUser);
        }

        // Thêm tin nhắn hệ thống xác nhận giải phóng xu
        String thờiGianTinSys = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        thựcHiệnGửiGóiNộiDungTinNhắn("🛡️ Hệ thống: Giao dịch hoàn tất thành công! " + giáXuEscrow + " SS đã giải ngân cho người bán " + tênNgườiBán + ".");

        cậpNhậtHiểnThịDanhSáchTinNhắnRealtime();
        cuộnXuốngĐáyChat();

        if (nútXácNhậnEscrow != null) {
            nútXácNhậnEscrow.setEnabled(false);
            nútXácNhậnEscrow.setText("✓ Đã Giải Phóng Xu (Giao Dịch Hoàn Tất)");
        }
        if (nútBáoCáoLỗiEscrow != null) {
            nútBáoCáoLỗiEscrow.setVisibility(View.GONE);
        }

        Toast.makeText(this, "🎉 Giải phóng Escrow " + giáXuEscrow + " SS thành công!", Toast.LENGTH_LONG).show();
    }

    private void cuộnXuốngĐáyChat() {
        if (khungCuộnChat != null) {
            khungCuộnChat.post(() -> khungCuộnChat.fullScroll(ScrollView.FOCUS_DOWN));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bộCậpNhậtChatRealtime != null && côngViệcTảiTinNhắnTựĐộng != null) {
            bộCậpNhậtChatRealtime.removeCallbacks(côngViệcTảiTinNhắnTựĐộng);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bộCậpNhậtChatRealtime != null) {
            bộCậpNhậtChatRealtime.removeCallbacksAndMessages(null);
        }
        if (bộĐếmLùiEscrowTimer != null) {
            bộĐếmLùiEscrowTimer.cancel();
        }
        if (listenerFirestoreChat != null) {
            listenerFirestoreChat.remove();
        }
    }
}
