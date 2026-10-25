package vn.edu.ueh.baoho.ssharing.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vn.edu.ueh.baoho.ssharing.R;

public class P2PVoucherTradeActivity extends AppCompatActivity {

    private Spinner ôChọnĐốiTácTrade;
    private TextView nhãnTrạngTháiPhiênTrade;
    private TextView nhãnDanhSáchOfferCủaTôi;
    private TextView nhãnDanhSáchOfferĐốiTác;
    private TextView nhãnKhóaOfferCủaTôi;
    private TextView nhãnKhóaOfferĐốiTác;
    private LinearLayout containerDanhSáchVoucherKhoTôi;
    private Button nútKhóaOfferCủaTôi;
    private Button nútXácNhậnTraoĐổiMã;

    private boolean làĐãKhóaOfferCủaTôi = false;
    private boolean làĐốiTácĐãKhóaOffer = true;
    private String tênĐốiTácHiệnTại = "Beo123";
    private String usernameHiệnTại = "baoho123";

    private List<String> danhSáchVoucherTôiChọnOffer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2p_voucher_trade);

        đọcThôngTinTàiKhoản();
        khởiTạoÁnhXạGiaoDiện();
        khởiTạoDanhSáchĐốiTác();
        tảiKhoVoucherTrongVíCủaTôi();
        càiĐặtSựKiệnLắngNghe();
    }

    private void đọcThôngTinTàiKhoản() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        usernameHiệnTại = bộLưuTrữVí.getString("CURRENT_USERNAME", "baoho123");
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        ôChọnĐốiTácTrade = findViewById(R.id.spTradePartner);
        nhãnTrạngTháiPhiênTrade = findViewById(R.id.tvTradeSessionStatus);
        nhãnDanhSáchOfferCủaTôi = findViewById(R.id.tvMyOfferedVouchers);
        nhãnDanhSáchOfferĐốiTác = findViewById(R.id.tvPartnerOfferedVouchers);
        nhãnKhóaOfferCủaTôi = findViewById(R.id.tvMyOfferLockStatus);
        nhãnKhóaOfferĐốiTác = findViewById(R.id.tvPartnerOfferLockStatus);
        containerDanhSáchVoucherKhoTôi = findViewById(R.id.containerMyInventoryTradeItems);
        nútKhóaOfferCủaTôi = findViewById(R.id.btnLockMyOffer);
        nútXácNhậnTraoĐổiMã = findViewById(R.id.btnConfirmTradeSwap);
    }

    private void khởiTạoDanhSáchĐốiTác() {
        List<String> danhSáchĐốiTác = new ArrayList<>();
        danhSáchĐốiTác.add("Beo123 (Gia Beo)");
        danhSáchĐốiTác.add("minh.triet (Minh Triết)");
        danhSáchĐốiTác.add("bao.ngoc (Bảo Ngọc)");
        danhSáchĐốiTác.add("khanh.trinh (Khánh Trình)");

        ArrayAdapter<String> bộĐiềuPhối = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSáchĐốiTác);
        bộĐiềuPhối.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (ôChọnĐốiTácTrade != null) {
            ôChọnĐốiTácTrade.setAdapter(bộĐiềuPhối);
            ôChọnĐốiTácTrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tênĐốiTácHiệnTại = danhSáchĐốiTác.get(position);
                    if (nhãnTrạngTháiPhiênTrade != null) {
                        nhãnTrạngTháiPhiênTrade.setText("🟢 Trạng thái: Đã kết nối bàn trao đổi trực tiếp với " + tênĐốiTácHiệnTại);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
    }

    private void càiĐặtSựKiệnLắngNghe() {
        View nútQuayLại = findViewById(R.id.btnBackP2PTrade);
        if (nútQuayLại != null) {
            nútQuayLại.setOnClickListener(v -> finish());
        }

        View nútMờiTrade = findViewById(R.id.btnSendTradeInvite);
        if (nútMờiTrade != null) {
            nútMờiTrade.setOnClickListener(v -> {
                SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
                String partnerClean = tênĐốiTácHiệnTại.toLowerCase().replaceAll("[^a-z0-9]", "").trim();
                if (partnerClean.isEmpty()) partnerClean = "beo123";

                String thờiGianThực = new SimpleDateFormat("HH:mm dd/MM", Locale.getDefault()).format(new Date());
                String tbMới = "🤝 [BÀN TRADE P2P] Thành viên @" + usernameHiệnTại + " đã gửi lời mời tham gia Bàn Trao Đổi Voucher trực tiếp!||" + thờiGianThực;
                String tbGốc = bộLưuTrữVí.getString("DANH_SACH_THONG_BAO_" + partnerClean, "");
                bộLưuTrữVí.edit()
                        .putString("DANH_SACH_THONG_BAO_" + partnerClean, tbMới + (tbGốc.isEmpty() ? "" : ";;" + tbGốc))
                        .putBoolean("HAS_UNREAD_NOTIFICATIONS_" + partnerClean, true)
                        .apply();

                // Đăng ký thẻ Giao Dịch vào ChatInbox dưới tab Giao Dịch
                String idPhòng = "CHAT_ROOM_SWAP_" + System.currentTimeMillis();
                String bảnGhiBànTrade = idPhòng + "||TRADE-INVITE||Thương lượng Bàn Trade Voucher||" + usernameHiệnTại + "||Gia Bảo Hồ||" + tênĐốiTácHiệnTại + "||TR||0||🔒 Yêu cầu trao đổi Voucher P2P đang chờ xác nhận||" + thờiGianThực;
                String phòngGốc = bộLưuTrữVí.getString("DANH_SACH_PHONG_CHAT_TAT_CA", "");
                bộLưuTrữVí.edit().putString("DANH_SACH_PHONG_CHAT_TAT_CA", bảnGhiBànTrade + (phòngGốc.isEmpty() ? "" : ";;;" + phòngGốc)).apply();

                new AlertDialog.Builder(this)
                        .setTitle("📩 Gửi Lời Mời Bàn Trade P2P")
                        .setMessage("Đã gửi thông báo lời mời Bàn Trade Voucher tới " + tênĐốiTácHiệnTại + "! Thẻ [Yêu cầu trao đổi] đã xuất hiện ở Hộp Thoại Chat Giao Dịch và Chuông Báo của đối tác.")
                        .setPositiveButton("Bắt đầu Trade", null)
                        .setNegativeButton("Từ Chối Trade", (dialog, which) -> finish())
                        .show();
            });
        }

        if (nútKhóaOfferCủaTôi != null) {
            nútKhóaOfferCủaTôi.setOnClickListener(v -> thựcHiệnKhóaOfferCủaTôi());
        }

        if (nútXácNhậnTraoĐổiMã != null) {
            nútXácNhậnTraoĐổiMã.setOnClickListener(v -> thựcHiệnXácNhậnTraoĐổiVoucherAtomicSwap());
        }
    }

    private void tảiKhoVoucherTrongVíCủaTôi() {
        if (containerDanhSáchVoucherKhoTôi == null) return;
        containerDanhSáchVoucherKhoTôi.removeAllViews();

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String danhSáchVoucherGốc = bộLưuTrữVí.getString("DANH_SACH_VOUCHER_DA_LUU", "");

        if (danhSáchVoucherGốc.isEmpty()) {
            danhSáchVoucherGốc = "SBUX20K-OCTOBER99::Voucher Starbucks Giảm 20K::HSD: 31 T12 2026::BRAND;;" +
                    "BECAR30K-USER44::Voucher BeCar Giảm 30K::HSD: 31 T12 2026::C2C;;" +
                    "SPFOOD50K-USER22::Voucher ShopeeFood Giảm 50K::HSD: 31 T12 2026::FOOD";
        }

        String[] cácVoucher = danhSáchVoucherGốc.split(";;");
        for (String chuỗiVoucher : cácVoucher) {
            if (chuỗiVoucher.trim().isEmpty()) continue;
            String[] mảngTrường = chuỗiVoucher.split("::");
            if (mảngTrường.length < 2) continue;

            String mãCode = mảngTrường[0];
            String tênVoucher = mảngTrường[1];

            // Tạo Checkbox chọn Voucher cho vào bàn Trade
            LinearLayout hàngItem = new LinearLayout(this);
            hàngItem.setOrientation(LinearLayout.HORIZONTAL);
            hàngItem.setPadding(12, 12, 12, 12);
            hàngItem.setBackgroundResource(R.drawable.bg_card_dark);
            LinearLayout.LayoutParams thôngSốHàng = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            thôngSốHàng.bottomMargin = 10;
            hàngItem.setLayoutParams(thôngSốHàng);

            CheckBox checkBoxVoucher = new CheckBox(this);
            checkBoxVoucher.setText("🎟 " + tênVoucher + " (Mã: " + mãCode + ")");
            checkBoxVoucher.setTextColor(Color.WHITE);
            checkBoxVoucher.setTextSize(12);

            checkBoxVoucher.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    danhSáchVoucherTôiChọnOffer.add(tênVoucher);
                } else {
                    danhSáchVoucherTôiChọnOffer.remove(tênVoucher);
                }
                cậpNhậtHiểnThịTrựcTiếpBànTradeCủaTôi();
            });

            hàngItem.addView(checkBoxVoucher);
            containerDanhSáchVoucherKhoTôi.addView(hàngItem);
        }
    }

    private void cậpNhậtHiểnThịTrựcTiếpBànTradeCủaTôi() {
        if (nhãnDanhSáchOfferCủaTôi == null) return;
        if (danhSáchVoucherTôiChọnOffer.isEmpty()) {
            nhãnDanhSáchOfferCủaTôi.setText("Chưa chọn voucher nào để offer.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < danhSáchVoucherTôiChọnOffer.size(); i++) {
            sb.append("🎟 ").append(i + 1).append(". ").append(danhSáchVoucherTôiChọnOffer.get(i)).append(" (x1)\n");
        }
        nhãnDanhSáchOfferCủaTôi.setText(sb.toString().trim());
    }

    private void thựcHiệnKhóaOfferCủaTôi() {
        if (danhSáchVoucherTôiChọnOffer.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất 01 voucher từ ví của bạn để khóa offer!", Toast.LENGTH_SHORT).show();
            return;
        }

        làĐãKhóaOfferCủaTôi = true;
        if (nhãnKhóaOfferCủaTôi != null) {
            nhãnKhóaOfferCủaTôi.setText("🔒 Đã khóa offer");
            nhãnKhóaOfferCủaTôi.setTextColor(Color.parseColor("#43E5A0"));
        }
        Toast.makeText(this, "🔒 Đã khóa offer voucher của bạn trên Bàn Trade!", Toast.LENGTH_SHORT).show();
    }

    private void thựcHiệnXácNhậnTraoĐổiVoucherAtomicSwap() {
        if (!làĐãKhóaOfferCủaTôi) {
            Toast.makeText(this, "⚠️ Vui lòng bấm [Khóa Offer Voucher Của Bạn] trước khi xác nhận trao đổi!", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        String danhSáchVoucherĐãLưu = bộLưuTrữVí.getString("DANH_SACH_VOUCHER_DA_LUU", "");

        // Khấu trừ danh sách voucher bạn đã đem ra trao đổi khỏi Ví của bạn
        StringBuilder danhSáchVoucherSauKhấuTrừ = new StringBuilder();
        if (!danhSáchVoucherĐãLưu.isEmpty()) {
            String[] mảngVí = danhSáchVoucherĐãLưu.split(";;");
            for (String v : mảngVí) {
                if (v.trim().isEmpty()) continue;
                String[] t = v.split("::");
                if (t.length >= 2) {
                    String tênMón = t[1];
                    if (!danhSáchVoucherTôiChọnOffer.contains(tênMón)) {
                        if (danhSáchVoucherSauKhấuTrừ.length() > 0) danhSáchVoucherSauKhấuTrừ.append(";;");
                        danhSáchVoucherSauKhấuTrừ.append(v);
                    }
                }
            }
        }

        // Thêm Voucher mới nhận được từ đối tác vào Ví
        String voucherMớiThêm = "GRAB50K-SWAP::Voucher GrabCar Giảm 50K::HSD: 31 T12 2026::GRAB;;" +
                "HLM1T1-SWAP::Voucher Highlands Mua 1 Tặng 1::HSD: 31 T12 2026::FOOD";

        String danhSáchVoucherMớiHoànTất = voucherMớiThêm + (danhSáchVoucherSauKhấuTrừ.length() == 0 ? "" : ";;" + danhSáchVoucherSauKhấuTrừ.toString());

        // Mô tả chi tiết hoán đổi món đồ cho Lịch sử
        StringBuilder sbCho = new StringBuilder();
        for (String m : danhSáchVoucherTôiChọnOffer) {
            sbCho.append(m).append(", ");
        }
        String chuỗiMónCho = sbCho.toString();
        if (chuỗiMónCho.endsWith(", ")) chuỗiMónCho = chuỗiMónCho.substring(0, chuỗiMónCho.length() - 2);

        String môTảChiTiếtLịchSử = "Hoán đổi thành công: Đã cho " + (chuỗiMónCho.isEmpty() ? "Voucher" : chuỗiMónCho) + " ➜ Nhận Voucher GrabCar Giảm 50K & Highlands Mua 1 Tặng 1 từ " + tênĐốiTácHiệnTại;

        // Ghi nhật ký giao dịch vào Ví
        String danhSáchGiaoDịchĐãLưu = bộLưuTrữVí.getString("DANH_SACH_GIAO_DICH", "");
        String mãGiaoDịchRandom = "TX-SWAP" + (100000 + (int)(Math.random() * 899999));
        String bảnGhiGiaoDịchMới = mãGiaoDịchRandom + "||Trao đổi Voucher P2P với " + tênĐốiTácHiệnTại + "||0 SS||" + thờiGianThực + "||Trao đổi P2P Swap||Thành công||" + môTảChiTiếtLịchSử;
        String danhSáchGiaoDịchMới = bảnGhiGiaoDịchMới + (danhSáchGiaoDịchĐãLưu.isEmpty() ? "" : ";;" + danhSáchGiaoDịchĐãLưu);

        bộLưuTrữVí.edit()
                .putString("DANH_SACH_VOUCHER_DA_LUU", danhSáchVoucherMớiHoànTất)
                .putString("DANH_SACH_GIAO_DICH", danhSáchGiaoDịchMới)
                .apply();

        // 3. Đồng bộ giao dịch trao đổi Atomic Swap lên Firebase Firestore
        đồngBộHoánĐổiVoucherLênFirebaseFirestore(mãGiaoDịchRandom, usernameHiệnTại, tênĐốiTácHiệnTại);

        String nộiDungHóaĐơn = "🧾 MÃ HỒ SƠ SWAP: " + mãGiaoDịchRandom + "\n\n" +
                "👤 Người trao đổi A: " + usernameHiệnTại + "\n" +
                "👤 Người trao đổi B: " + tênĐốiTácHiệnTại + "\n\n" +
                "🎁 Voucher Bạn Nhận Được:\n" +
                "   • 🎟 Voucher GrabCar Giảm 50K (x1)\n" +
                "   • 🎟 Voucher Highlands Mua 1 Tặng 1 (x1)\n\n" +
                "⏱ Thời gian: " + thờiGianThực + "\n" +
                "✅ Trạng thái: TRAO ĐỔI THÀNH CÔNG 100%\n" +
                "🛡️ Hệ thống SSharing đã chuyển mã voucher mới vào Ví Voucher của bạn!";

        new AlertDialog.Builder(this)
                .setTitle("🎉 HÓA ĐƠN TRAO ĐỔI VOUCHER P2P THÀNH CÔNG")
                .setMessage(nộiDungHóaĐơn)
                .setCancelable(false)
                .setPositiveButton("💬 Mở Escrow Chat Voucher", (dialog, which) -> {
                    android.content.Intent ýĐịnhEscrow = new android.content.Intent(this, EscrowChatActivity.class);
                    ýĐịnhEscrow.putExtra("EXTRA_SELLER_NAME", tênĐốiTácHiệnTại);
                    ýĐịnhEscrow.putExtra("EXTRA_SELLER_AVATAR", "SW");
                    ýĐịnhEscrow.putExtra("EXTRA_VOUCHER_TITLE", "Voucher GrabCar Giảm 50K & Highlands M1T1");
                    ýĐịnhEscrow.putExtra("EXTRA_SECRET_CODE", "GRAB50K-SWAP");
                    ýĐịnhEscrow.putExtra("EXTRA_PRICE_SS", 0);
                    ýĐịnhEscrow.putExtra("EXTRA_IS_SOCIAL_CHAT", false);
                    startActivity(ýĐịnhEscrow);
                    finish();
                })
                .setNegativeButton("Xem Ví Xu", (dialog, which) -> finish())
                .show();
    }

    private void đồngBộHoánĐổiVoucherLênFirebaseFirestore(String mãTX, String userA, String userB) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> tradeData = new HashMap<>();
            tradeData.put("trade_id", mãTX);
            tradeData.put("user_a", userA);
            tradeData.put("user_b", userB);
            tradeData.put("status", "SWAP_SUCCESS");
            tradeData.put("created_at", System.currentTimeMillis());

            db.collection("p2p_trades").document(mãTX).set(tradeData);
        } catch (Exception ignored) {}
    }
}
