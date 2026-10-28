package vn.edu.ueh.baoho.ssharing.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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

public class activity_staff_arbiter extends AppCompatActivity {

    private Spinner ôChọnHồSơTranhChấp;
    private TextView nhãnChiTiếtTranhChấpHeader;
    private TextView nhãnTênCácBênTranhChấp;
    private TextView nhãnMốcThờiGianKhiếuNại;
    private TextView nhãnLờiKhaiNgườiMua;
    private TextView nhãnLờiKhaiNgườiBán;
    private Button nútChấpThuậnHoànTiềnNgườiMua;
    private Button nútBácKhiếuNạiGiảiNgânNgườiBán;

    private int giáTrịXuEscrowĐangXửLý = 220;
    private String tênTàiKhoảnNgườiMua = "baoho123";
    private String tênTàiKhoảnNgườiBán = "Beo123";
    private String mãHồSơHiệnTại = "TK-98012";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_arbiter);

        khởiTạoÁnhXạGiaoDiện();
        khởiTạoDanhSáchHồSơTranhChấp();
        càiĐặtSựKiệnLắngNghe();
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        ôChọnHồSơTranhChấp = findViewById(R.id.spDisputeCases);
        nhãnChiTiếtTranhChấpHeader = findViewById(R.id.tvCaseDetailHeader);
        nhãnTênCácBênTranhChấp = findViewById(R.id.tvCaseDetailParties);
        nhãnMốcThờiGianKhiếuNại = findViewById(R.id.tvCaseDetailTime);
        nhãnLờiKhaiNgườiMua = findViewById(R.id.tvBuyerStatement);
        nhãnLờiKhaiNgườiBán = findViewById(R.id.tvSellerStatement);
        nútChấpThuậnHoànTiềnNgườiMua = findViewById(R.id.btnRefundBuyer);
        nútBácKhiếuNạiGiảiNgânNgườiBán = findViewById(R.id.btnReleaseSeller);
    }

    private void khởiTạoDanhSáchHồSơTranhChấp() {
        List<String> danhSáchCaTranhChấp = new ArrayList<>();
        danhSáchCaTranhChấp.add("[TK-98012] Voucher BeCar - Mua: baoho123 - Bán: Beo123 (🔒 220 SS)");
        danhSáchCaTranhChấp.add("[TK-55102] Voucher ShopeeFood - Mua: minh.triet - Bán: bao.ngoc (🔒 500 SS)");
        danhSáchCaTranhChấp.add("[TK-33109] Voucher GrabCar - Mua: khanh.trinh - Bán: baoho123 (🔒 350 SS)");

        ArrayAdapter<String> bộĐiềuPhốiDanhSách = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSáchCaTranhChấp);
        bộĐiềuPhốiDanhSách.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (ôChọnHồSơTranhChấp != null) {
            ôChọnHồSơTranhChấp.setAdapter(bộĐiềuPhốiDanhSách);
            ôChọnHồSơTranhChấp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    cậpNhậtDữLiệuHồSơTranhChấpTheoVịTrí(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
    }

    private void cậpNhậtDữLiệuHồSơTranhChấpTheoVịTrí(int vịTrí) {
        if (vịTrí == 0) {
            mãHồSơHiệnTại = "TK-98012";
            tênTàiKhoảnNgườiMua = "baoho123";
            tênTàiKhoảnNgườiBán = "Beo123";
            giáTrịXuEscrowĐangXửLý = 220;

            if (nhãnChiTiếtTranhChấpHeader != null) nhãnChiTiếtTranhChấpHeader.setText("📌 Mã Hồ Sơ: [TK-98012] | 🔒 Ký quỹ Escrow: 220 SS");
            if (nhãnTênCácBênTranhChấp != null) nhãnTênCácBênTranhChấp.setText("👤 Mua: baoho123 (Hồ Gia Bảo)  ➜  🏪 Bán: Beo123 (Gia Beo)");
            if (nhãnMốcThờiGianKhiếuNại != null) nhãnMốcThờiGianKhiếuNại.setText("⏱ Thời gian khiếu nại: 25/10/2026 15:45:10");
            if (nhãnLờiKhaiNgườiMua != null) nhãnLờiKhaiNgườiMua.setText("💬 baoho123: Mã BeCar báo đã được sử dụng từ trước. Nhờ Trọng tài hoàn xu cho mình!");
            if (nhãnLờiKhaiNgườiBán != null) nhãnLờiKhaiNgườiBán.setText("💬 Beo123: Mã BECAR30K-USER44 mới mua sáng nay chưa dùng. Nhờ Trọng tài kiểm tra!");
        } else if (vịTrí == 1) {
            mãHồSơHiệnTại = "TK-55102";
            tênTàiKhoảnNgườiMua = "minh.triet";
            tênTàiKhoảnNgườiBán = "bao.ngoc";
            giáTrịXuEscrowĐangXửLý = 500;

            if (nhãnChiTiếtTranhChấpHeader != null) nhãnChiTiếtTranhChấpHeader.setText("📌 Mã Hồ Sơ: [TK-55102] | 🔒 Ký quỹ Escrow: 500 SS");
            if (nhãnTênCácBênTranhChấp != null) nhãnTênCácBênTranhChấp.setText("👤 Mua: minh.triet (Minh Triết)  ➜  🏪 Bán: bao.ngoc (Bảo Ngọc)");
            if (nhãnMốcThờiGianKhiếuNại != null) nhãnMốcThờiGianKhiếuNại.setText("⏱ Thời gian khiếu nại: 25/10/2026 14:10:05");
            if (nhãnLờiKhaiNgườiMua != null) nhãnLờiKhaiNgườiMua.setText("💬 minh.triet: Mã ShopeeFood báo hết lượt áp dụng. Yêu cầu hoàn tiền!");
            if (nhãnLờiKhaiNgườiBán != null) nhãnLờiKhaiNgườiBán.setText("💬 bao.ngoc: Bạn kiểm tra lại hạn dùng khung giờ vàng giúp mình.");
        } else {
            mãHồSơHiệnTại = "TK-33109";
            tênTàiKhoảnNgườiMua = "khanh.trinh";
            tênTàiKhoảnNgườiBán = "baoho123";
            giáTrịXuEscrowĐangXửLý = 350;

            if (nhãnChiTiếtTranhChấpHeader != null) nhãnChiTiếtTranhChấpHeader.setText("📌 Mã Hồ Sơ: [TK-33109] | 🔒 Ký quỹ Escrow: 350 SS");
            if (nhãnTênCácBênTranhChấp != null) nhãnTênCácBênTranhChấp.setText("👤 Mua: khanh.trinh (Khánh Trình)  ➜  🏪 Bán: baoho123 (Hồ Gia Bảo)");
            if (nhãnMốcThờiGianKhiếuNại != null) nhãnMốcThờiGianKhiếuNại.setText("⏱ Thời gian khiếu nại: 25/10/2026 12:20:18");
            if (nhãnLờiKhaiNgườiMua != null) nhãnLờiKhaiNgườiMua.setText("💬 khanh.trinh: Nhập mã Grab báo sai ký tự bí mật.");
            if (nhãnLờiKhaiNgườiBán != null) nhãnLờiKhaiNgườiBán.setText("💬 baoho123: Đã gửi lại ảnh chụp mã gốc chính xác trong khung chat.");
        }

        if (nútChấpThuậnHoànTiềnNgườiMua != null) nútChấpThuậnHoànTiềnNgườiMua.setText("Chấp Thuận - Hoàn Tiền " + giáTrịXuEscrowĐangXửLý + " SS Cho " + tênTàiKhoảnNgườiMua);
        if (nútBácKhiếuNạiGiảiNgânNgườiBán != null) nútBácKhiếuNạiGiảiNgânNgườiBán.setText("Bác Khiếu Nại - Giải Ngân " + giáTrịXuEscrowĐangXửLý + " SS Cho " + tênTàiKhoảnNgườiBán);

        // Nạp ảnh bằng chứng và mô tả khiếu nại thực tế do Người mua tải lên
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String pathẢnhMới = bộLưuTrữVí.getString("DISPUTE_EVIDENCE_PHOTO_PATH_LATEST", "");
        String môTảMới = bộLưuTrữVí.getString("DISPUTE_DESCRIPTION_LATEST", "");

        android.widget.ImageView imgBuyerEvidence = findViewById(R.id.imgBuyerEvidence);
        if (imgBuyerEvidence != null && !pathẢnhMới.isEmpty() && new java.io.File(pathẢnhMới).exists()) {
            try {
                android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(pathẢnhMới);
                if (bitmap != null) {
                    imgBuyerEvidence.setImageBitmap(bitmap);
                    imgBuyerEvidence.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
                }
            } catch (Throwable ignored) {}
        }

        if (vịTrí == 0 && !môTảMới.isEmpty() && nhãnLờiKhaiNgườiMua != null) {
            nhãnLờiKhaiNgườiMua.setText("💬 " + tênTàiKhoảnNgườiMua + ": " + môTảMới);
        }
    }

    private void càiĐặtSựKiệnLắngNghe() {
        View nútQuayLại = findViewById(R.id.btnBackStaffArbiter);
        if (nútQuayLại != null) {
            nútQuayLại.setOnClickListener(v -> finish());
        }

        if (nútChấpThuậnHoànTiềnNgườiMua != null) {
            nútChấpThuậnHoànTiềnNgườiMua.setOnClickListener(v -> thựcHiệnPhánQuyếtHoànTiềnNgườiMua());
        }

        if (nútBácKhiếuNạiGiảiNgânNgườiBán != null) {
            nútBácKhiếuNạiGiảiNgânNgườiBán.setOnClickListener(v -> thựcHiệnPhánQuyếtGiảiNgânNgườiBán());
        }
    }

    private void thựcHiệnPhánQuyếtHoànTiềnNgườiMua() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);

        // Đánh dấu giao dịch đã hoàn tất phán quyết -> Chuyển vào Lịch Sử Chat & Khóa đoạn chat lại
        đánhDấuKhóaGiaoDịchSauPhánQuyết(bộLưuTrữVí);

        // Hoàn xu cho tài khoản riêng của Người Mua
        vn.edu.ueh.baoho.ssharing.utils.WalletManager.addBalance(this, tênTàiKhoảnNgườiMua, giáTrịXuEscrowĐangXửLý);

        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        String bảnGhiRefund = mãHồSơHiệnTại + "||Phán quyết Trọng tài: Hoàn tiền||+" + giáTrịXuEscrowĐangXửLý + " SS||" + thờiGianThực + "||Trọng tài Phán quyết||Thành công||Trọng tài hoàn " + giáTrịXuEscrowĐangXửLý + " SS cho hồ sơ " + mãHồSơHiệnTại;
        vn.edu.ueh.baoho.ssharing.utils.WalletManager.addTransactionRecord(this, tênTàiKhoảnNgườiMua, bảnGhiRefund, mãHồSơHiệnTại, "Hoàn tiền Trọng Tài", giáTrịXuEscrowĐangXửLý, "REFUND", "SUCCESS", "Hoàn xu do thắng khiếu nại");

        // Gửi thông báo tới Chuông Hệ Thống của cả 2 bên (Người mua thắng & Người bán bị trừ điểm)
        gửiThôngBáoVềChuôngCho2Bên(tênTàiKhoảnNgườiMua, tênTàiKhoảnNgườiBán, mãHồSơHiệnTại, true, giáTrịXuEscrowĐangXửLý);

        // Cập nhật trạng thái tranh chấp lên Firebase Firestore
        đồngBộPhánQuyếtLênFirebaseFirestore(mãHồSơHiệnTại, "REFUNDED_TO_BUYER", tênTàiKhoảnNgườiMua, giáTrịXuEscrowĐangXửLý);

        Toast.makeText(this, "⚖️ Phán quyết Trọng Tài: Đã chấp thuận hoàn " + giáTrịXuEscrowĐangXửLý + " SS cho Người Mua " + tênTàiKhoảnNgườiMua + "!", Toast.LENGTH_LONG).show();
        finish();
    }

    private void thựcHiệnPhánQuyếtGiảiNgânNgườiBán() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);

        // Đánh dấu giao dịch đã hoàn tất phán quyết -> Chuyển vào Lịch Sử Chat & Khóa đoạn chat lại
        đánhDấuKhóaGiaoDịchSauPhánQuyết(bộLưuTrữVí);

        // Giải ngân xu vào tài khoản riêng của Người Bán
        vn.edu.ueh.baoho.ssharing.utils.WalletManager.addBalance(this, tênTàiKhoảnNgườiBán, giáTrịXuEscrowĐangXửLý);

        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        String bảnGhiRelease = mãHồSơHiệnTại + "||Phán quyết Trọng tài: Giải ngân người bán||+" + giáTrịXuEscrowĐangXửLý + " SS||" + thờiGianThực + "||Trọng tài Phán quyết||Thành công||Trọng tài giải ngân " + giáTrịXuEscrowĐangXửLý + " SS cho hồ sơ " + mãHồSơHiệnTại;
        vn.edu.ueh.baoho.ssharing.utils.WalletManager.addTransactionRecord(this, tênTàiKhoảnNgườiBán, bảnGhiRelease, mãHồSơHiệnTại, "Giải ngân Trọng Tài", giáTrịXuEscrowĐangXửLý, "RELEASE", "SUCCESS", "Giải ngân xu do bác khiếu nại");

        // Gửi thông báo tới Chuông Hệ Thống của cả 2 bên (Bác khiếu nại & Giải ngân người bán)
        gửiThôngBáoVềChuôngCho2Bên(tênTàiKhoảnNgườiMua, tênTàiKhoảnNgườiBán, mãHồSơHiệnTại, false, giáTrịXuEscrowĐangXửLý);

        // Cập nhật trạng thái tranh chấp lên Firebase Firestore
        đồngBộPhánQuyếtLênFirebaseFirestore(mãHồSơHiệnTại, "RELEASED_TO_SELLER", tênTàiKhoảnNgườiBán, giáTrịXuEscrowĐangXửLý);

        Toast.makeText(this, "⚖️ Phán quyết Trọng Tài: Bác khiếu nại - Đã giải ngân " + giáTrịXuEscrowĐangXửLý + " SS cho Người Bán " + tênTàiKhoảnNgườiBán + "!", Toast.LENGTH_LONG).show();
        finish();
    }

    private void gửiThôngBáoVềChuôngCho2Bên(String buyer, String seller, String mãHS, boolean ngườiMuaThắng, int sốXu) {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String thờiGianThực = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());

        String thôngBáoBuyer;
        String thôngBáoSeller;

        if (ngườiMuaThắng) {
            thôngBáoBuyer = "🎉 [THẮNG KHIẾU NẠI] Trọng tài SSharing đã chấp thuận khiếu nại " + mãHS + "! +" + sốXu + " SS đã được hoàn trả 100% vào Ví của bạn.||" + thờiGianThực;
            thôngBáoSeller = "⚠️ [THẤT BẠI KHIẾU NẠI] Trọng tài kết luận mã " + mãHS + " không hợp lệ/gian dối. Khoản tiền " + sốXu + " SS đã bị trừ hoàn lại cho Người mua. Tài khoản của bạn bị trừ -10 điểm Trust Score!||" + thờiGianThực;
        } else {
            thôngBáoBuyer = "❌ [BÁC KHIẾU NẠI] Trọng tài kết luận mã " + mãHS + " hợp lệ. Khoản Ký quỹ " + sốXu + " SS đã được giải ngân cho Người bán.||" + thờiGianThực;
            thôngBáoSeller = "🎉 [THẮNG KHIẾU NẠI] Trọng tài đã bác khiếu nại của Người mua! +" + sốXu + " SS đã được giải ngân cộng trực tiếp vào Ví của bạn.||" + thờiGianThực;
        }

        String tbBuyerGốc = bộLưuTrữVí.getString("DANH_SACH_THONG_BAO_" + buyer.toLowerCase().trim(), "");
        String tbSellerGốc = bộLưuTrữVí.getString("DANH_SACH_THONG_BAO_" + seller.toLowerCase().trim(), "");

        bộLưuTrữVí.edit()
                .putString("DANH_SACH_THONG_BAO_" + buyer.toLowerCase().trim(), thôngBáoBuyer + (tbBuyerGốc.isEmpty() ? "" : ";;" + tbBuyerGốc))
                .putBoolean("HAS_UNREAD_NOTIFICATIONS_" + buyer.toLowerCase().trim(), true)
                .putString("DANH_SACH_THONG_BAO_" + seller.toLowerCase().trim(), thôngBáoSeller + (tbSellerGốc.isEmpty() ? "" : ";;" + tbSellerGốc))
                .putBoolean("HAS_UNREAD_NOTIFICATIONS_" + seller.toLowerCase().trim(), true)
                .apply();
    }

    private void đánhDấuKhóaGiaoDịchSauPhánQuyết(SharedPreferences bộLưuTrữVí) {
        String mãCodeHoànTất = "BECAR30K-USER44";
        if (mãHồSơHiệnTại.contains("55102")) mãCodeHoànTất = "SPFOOD50K-USER22";
        else if (mãHồSơHiệnTại.contains("33109")) mãCodeHoànTất = "GRAB50K-COMMUNITY88";

        String danhSáchĐãBánGốc = bộLưuTrữVí.getString("DANH_SACH_VOUCHER_DA_BAN_HE_THONG", "");
        String danhSáchĐãBánMới = mãCodeHoànTất + ";;" + danhSáchĐãBánGốc;
        bộLưuTrữVí.edit().putString("DANH_SACH_VOUCHER_DA_BAN_HE_THONG", danhSáchĐãBánMới).apply();
    }

    private void đồngBộPhánQuyếtLênFirebaseFirestore(String mãHS, String quyếtĐịnh, String ngườiNhận, int sốXu) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> disputeData = new HashMap<>();
            disputeData.put("case_id", mãHS);
            disputeData.put("decision", quyếtĐịnh);
            disputeData.put("beneficiary", ngườiNhận);
            disputeData.put("amount_ss", sốXu);
            disputeData.put("arbiter_username", "Gia Bảo Hồ (@baoho123)");
            disputeData.put("updated_at", System.currentTimeMillis());

            db.collection("disputes").document(mãHS).set(disputeData);
        } catch (Exception ignored) {}
    }
}
