package vn.edu.ueh.baoho.ssharing.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vn.edu.ueh.baoho.ssharing.R;

public class CoinTopupActivity extends AppCompatActivity {

    private RadioGroup nhómGóiNạp;
    private TextView nhãnThôngTinGóiNạp;
    private int sốXuCầnNạp = 220; // Mặc định gói 200k = 220 SS xu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_topup);

        nhómGóiNạp = findViewById(R.id.rgPackages);
        nhãnThôngTinGóiNạp = findViewById(R.id.tvThongTinGoiNap);

        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(vương -> finish());
        }

        if (nhómGóiNạp != null) {
            nhómGóiNạp.setOnCheckedChangeListener((nhóm, mãĐượcChọn) -> {
                if (mãĐượcChọn == R.id.rbGoi50k) {
                    sốXuCầnNạp = 50;
                } else if (mãĐượcChọn == R.id.rbGoi100k) {
                    sốXuCầnNạp = 105;
                } else if (mãĐượcChọn == R.id.rbGoi200k) {
                    sốXuCầnNạp = 220;
                } else if (mãĐượcChọn == R.id.rbGoi500k) {
                    sốXuCầnNạp = 575;
                } else if (mãĐượcChọn == R.id.rbGoi1000k) {
                    sốXuCầnNạp = 1200;
                }

                if (nhãnThôngTinGóiNạp != null) {
                    nhãnThôngTinGóiNạp.setText("Số Xu sẽ cộng vào ví: " + sốXuCầnNạp + " SS");
                }
            });
        }

        if (findViewById(R.id.btnSandboxTest) != null) {
            findViewById(R.id.btnSandboxTest).setOnClickListener(vương -> thựcHiệnGiảLậpNạpTiền());
        }
    }

    private void thựcHiệnGiảLậpNạpTiền() {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        int sốDưHiệnTại = bộLưuTrữCụcBộ.getInt("SO_DU_XU_SS", 1250);
        int sốDưMới = sốDưHiệnTại + sốXuCầnNạp;
        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        // Tạo mã giao dịch nạp ngẫu nhiên
        String mãGiaoDịchRandom = "TX-N" + (100000 + (int)(Math.random() * 899999));
        String bảnGhiGiaoDịchMới = mãGiaoDịchRandom + "||Nạp xu qua VietQR Napas247 (+" + sốXuCầnNạp + " SS)||+\n" + sốXuCầnNạp + " SS||" + thờiGianThực + "||Nạp tiền VietQR||Thành công||Nạp tiền qua cổng thanh toán tự động VietQR Napas247";

        String danhSáchGiaoDịchĐãLưu = bộLưuTrữCụcBộ.getString("DANH_SACH_GIAO_DICH", "");
        String danhSáchGiaoDịchMới = bảnGhiGiaoDịchMới + (danhSáchGiaoDịchĐãLưu.isEmpty() ? "" : ";;" + danhSáchGiaoDịchĐãLưu);

        bộLưuTrữCụcBộ.edit()
                .putInt("SO_DU_XU_SS", sốDưMới)
                .putString("DANH_SACH_GIAO_DICH", danhSáchGiaoDịchMới)
                .apply();

        Toast.makeText(this, "Nạp thành công +" + sốXuCầnNạp + " SS! Số dư mới: " + String.format("%,d", sốDưMới) + " SS", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }
}
