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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        int sốDưHiệnTại = bộLưuTrữVí.getInt("SO_DU_XU_SS", 1250);
        int sốDưMới = sốDưHiệnTại + giáTrịXuEscrowĐangXửLý;

        bộLưuTrữVí.edit().putInt("SO_DU_XU_SS", sốDưMới).apply();

        // Cập nhật trạng thái tranh chấp lên Firebase Firestore
        đồngBộPhánQuyếtLênFirebaseFirestore(mãHồSơHiệnTại, "REFUNDED_TO_BUYER", tênTàiKhoảnNgườiMua, giáTrịXuEscrowĐangXửLý);

        Toast.makeText(this, "⚖️ Phán quyết Trọng Tài: Đã chấp thuận hoàn " + giáTrịXuEscrowĐangXửLý + " SS cho Người Mua " + tênTàiKhoảnNgườiMua + "!", Toast.LENGTH_LONG).show();
        finish();
    }

    private void thựcHiệnPhánQuyếtGiảiNgânNgườiBán() {
        // Cập nhật trạng thái tranh chấp lên Firebase Firestore
        đồngBộPhánQuyếtLênFirebaseFirestore(mãHồSơHiệnTại, "RELEASED_TO_SELLER", tênTàiKhoảnNgườiBán, giáTrịXuEscrowĐangXửLý);

        Toast.makeText(this, "⚖️ Phán quyết Trọng Tài: Bác khiếu nại - Đã giải ngân " + giáTrịXuEscrowĐangXửLý + " SS cho Người Bán " + tênTàiKhoảnNgườiBán + "!", Toast.LENGTH_LONG).show();
        finish();
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
