package vn.edu.ueh.baoho.ssharing.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vn.edu.ueh.baoho.ssharing.R;

public class InstantBuybackActivity extends AppCompatActivity {

    private RadioGroup nhómVoucherMẫu;
    private TextView nhãnXuCơBản;
    private TextView nhãnKhấuTrừĐơnTốiThiểu;
    private TextView nhãnPhíSàn;
    private TextView nhãnTổngXuThựcNhận;
    private Button nútXácNhậnĐổiXu;

    private int giáTrịGiảmVND = 50000;
    private int đơnTốiThiểuVND = 200000;
    private int sốXuThựcNhậnTínhToán = 4350;
    private String tênVoucherThuMua = "Voucher Shopee 50.000đ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_buyback);

        khởiTạoÁnhXạGiaoDiện();
        càiĐặtSựKiệnLắngNghe();
        cậpNhậtBảngTínhThuMua();
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        nhómVoucherMẫu = findViewById(R.id.rgSampleVouchers);
        nhãnXuCơBản = findViewById(R.id.tvBaseCoins);
        nhãnKhấuTrừĐơnTốiThiểu = findViewById(R.id.tvMinSpendDeduction);
        nhãnPhíSàn = findViewById(R.id.tvPlatformFeeDeduction);
        nhãnTổngXuThựcNhận = findViewById(R.id.tvTotalNetCoins);
        nútXácNhậnĐổiXu = findViewById(R.id.btnConfirmBuyback);
    }

    private void càiĐặtSựKiệnLắngNghe() {
        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        if (nhómVoucherMẫu != null) {
            nhómVoucherMẫu.setOnCheckedChangeListener((nhóm, mãLựaChọn) -> {
                if (mãLựaChọn == R.id.rbVoucher1) {
                    giáTrịGiảmVND = 50000;
                    đơnTốiThiểuVND = 200000;
                    tênVoucherThuMua = "Voucher Shopee 50.000đ";
                } else if (mãLựaChọn == R.id.rbVoucher2) {
                    giáTrịGiảmVND = 20000;
                    đơnTốiThiểuVND = 60000;
                    tênVoucherThuMua = "Voucher Highlands 20.000đ";
                } else if (mãLựaChọn == R.id.rbVoucher3) {
                    giáTrịGiảmVND = 30000;
                    đơnTốiThiểuVND = 100000;
                    tênVoucherThuMua = "Voucher GrabCar 30.000đ";
                } else if (mãLựaChọn == R.id.rbVoucher4) {
                    giáTrịGiảmVND = 100000;
                    đơnTốiThiểuVND = 500000;
                    tênVoucherThuMua = "Voucher Điện Máy 100.000đ";
                }
                cậpNhậtBảngTínhThuMua();
            });
        }

        if (nútXácNhậnĐổiXu != null) {
            nútXácNhậnĐổiXu.setOnClickListener(v -> thựcHiệnĐổiXuTứcThì());
        }
    }

    /**
     * Thuật toán công thức thu mua Section 5:
     * XuThựcNhận = (GiáTriGiảm / 10) - ((ĐơnTốiThiểu / 10000) * 20) - PhíSàn (5%)
     */
    private void cậpNhậtBảngTínhThuMua() {
        int xuCơBản = giáTrịGiảmVND / 10;
        int khấuTrừĐơnTốiThiểu = (đơnTốiThiểuVND / 10000) * 20;
        int phíSàn5Percent = (int) (xuCơBản * 0.05);

        sốXuThựcNhậnTínhToán = Math.max(0, xuCơBản - khấuTrừĐơnTốiThiểu - phíSàn5Percent);

        if (nhãnXuCơBản != null) {
            nhãnXuCơBản.setText(String.format("%,d", xuCơBản) + " Xu");
        }
        if (nhãnKhấuTrừĐơnTốiThiểu != null) {
            nhãnKhấuTrừĐơnTốiThiểu.setText("-" + String.format("%,d", khấuTrừĐơnTốiThiểu) + " Xu");
        }
        if (nhãnPhíSàn != null) {
            nhãnPhíSàn.setText("-" + String.format("%,d", phíSàn5Percent) + " Xu");
        }
        if (nhãnTổngXuThựcNhận != null) {
            nhãnTổngXuThựcNhận.setText("+" + String.format("%,d", sốXuThựcNhậnTínhToán) + " SS");
        }
    }

    private void thựcHiệnĐổiXuTứcThì() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        int sốDưXuHiệnTại = bộLưuTrữVí.getInt("SO_DU_XU_SS", 1250);
        int sốDưXuMới = sốDưXuHiệnTại + sốXuThựcNhậnTínhToán;
        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        // Ghi lịch sử giao dịch
        String danhSáchGiaoDịchĐãLưu = bộLưuTrữVí.getString("DANH_SACH_GIAO_DICH", "");
        String mãGiaoDịchRandom = "TX-BUYBACK" + (100000 + (int)(Math.random() * 899999));
        String bảnGhiGiaoDịchMới = mãGiaoDịchRandom + "||Đổi Xu Tức Thì (" + tênVoucherThuMua + ")||+\n" + sốXuThựcNhậnTínhToán + " SS||" + thờiGianThực + "||Instant Buyback||Thành công||SSharing thu mua voucher tự động 24/7";
        String danhSáchGiaoDịchMới = bảnGhiGiaoDịchMới + (danhSáchGiaoDịchĐãLưu.isEmpty() ? "" : ";;" + danhSáchGiaoDịchĐãLưu);

        bộLưuTrữVí.edit()
                .putInt("SO_DU_XU_SS", sốDưXuMới)
                .putString("DANH_SACH_GIAO_DICH", danhSáchGiaoDịchMới)
                .apply();

        Toast.makeText(this, "🎉 Đổi xu tức thì thành công! + " + String.format("%,d", sốXuThựcNhậnTínhToán) + " SS đã cộng vào ví của bạn.", Toast.LENGTH_LONG).show();
        finish();
    }
}
