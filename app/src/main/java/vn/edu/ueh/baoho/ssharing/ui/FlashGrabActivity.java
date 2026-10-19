package vn.edu.ueh.baoho.ssharing.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vn.edu.ueh.baoho.ssharing.R;

public class FlashGrabActivity extends AppCompatActivity {

    private TextView nhãnGiờĐếmNgược, nhãnPhútĐếmNgược, nhãnGiâyĐếmNgược;
    private CountDownTimer bộĐếmNgượcThờiGian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_grab);

        nhãnGiờĐếmNgược = findViewById(R.id.tvCountdownHours);
        nhãnPhútĐếmNgược = findViewById(R.id.tvCountdownMinutes);
        nhãnGiâyĐếmNgược = findViewById(R.id.tvCountdownSeconds);

        View nútQuayLại = findViewById(R.id.btnBackFlash);
        if (nútQuayLại != null) nútQuayLại.setOnClickListener(vương -> finish());

        càiĐặtSựKiệnGiựtDeal();
        bắtĐầuĐếmNgượcTrựcTiếp();
    }

    private void càiĐặtSựKiệnGiựtDeal() {
        // Deal 1: Grab 100K (10 Xu)
        Button nútGiựtDeal1 = findViewById(R.id.btnGrabNow);
        if (nútGiựtDeal1 != null) {
            nútGiựtDeal1.setOnClickListener(vương ->
                    thựcHiệnGiựtDeal(nútGiựtDeal1, "Voucher GrabCar 100.000đ", 10, "GRAB100K-FLASH", "GRAB")
            );
        }

        // Deal 2: Shopee 100K (15 Xu)
        Button nútGiựtDeal2 = findViewById(R.id.btnGrab2);
        if (nútGiựtDeal2 != null) {
            nútGiựtDeal2.setOnClickListener(vương ->
                    thựcHiệnGiựtDeal(nútGiựtDeal2, "Voucher Shopee 100K Giảm 50%", 15, "SHOPEE100K-FLASH", "SHOPEE")
            );
        }

        // Deal 3: Highlands 30K (5 Xu)
        Button nútGiựtDeal3 = findViewById(R.id.btnGrab3);
        if (nútGiựtDeal3 != null) {
            nútGiựtDeal3.setOnClickListener(vương ->
                    thựcHiệnGiựtDeal(nútGiựtDeal3, "Voucher Highlands Trà Sen Vàng 30K", 5, "HIGHLANDS30K-FLASH", "HIGHLANDS")
            );
        }

        // Deal 4: MoMo 50K (10 Xu)
        Button nútGiựtDeal4 = findViewById(R.id.btnGrab4);
        if (nútGiựtDeal4 != null) {
            nútGiựtDeal4.setOnClickListener(vương ->
                    thựcHiệnGiựtDeal(nútGiựtDeal4, "Voucher MoMo Hoàn 50K Nạp ĐT", 10, "MOMO50K-FLASH", "MOMO")
            );
        }
    }

    private void thựcHiệnGiựtDeal(Button nútGiựt, String tênDeal, int giáXu, String mãBíMật, String loạiIcon) {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        int sốDưXuHiệnTại = bộLưuTrữCụcBộ.getInt("SO_DU_XU_SS", 1250);

        if (sốDưXuHiệnTại < giáXu) {
            Toast.makeText(this, "Không đủ Xu để giựt deal này! Bạn cần có ít nhất " + giáXu + " SS.", Toast.LENGTH_LONG).show();
            return;
        }

        int sốDưMới = sốDưXuHiệnTại - giáXu;
        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        // 1. Lưu voucher đã giựt vào danh sách Ví Voucher
        String danhSáchVoucherĐãLưu = bộLưuTrữCụcBộ.getString("DANH_SACH_VOUCHER_DA_LUU", "");
        String voucherMớiThêm = mãBíMật + "::" + tênDeal + "::HSD: 31 T12 2026::" + loạiIcon;
        String danhSáchVoucherMới = voucherMớiThêm + (danhSáchVoucherĐãLưu.isEmpty() ? "" : ";;" + danhSáchVoucherĐãLưu);

        // 2. Thêm bản ghi giao dịch mới vào Danh sách Lịch Sử
        String danhSáchGiaoDịchĐãLưu = bộLưuTrữCụcBộ.getString("DANH_SACH_GIAO_DICH", "");
        String mãGiaoDịchRandom = "TX-FG" + (100000 + (int)(Math.random() * 899999));
        String bảnGhiGiaoDịchMới = mãGiaoDịchRandom + "||Giựt Deal " + tênDeal + "||-" + giáXu + " SS||" + thờiGianThực + "||Giựt Deal Flash Grab||Thành công||Đã sử dụng " + giáXu + " SS để giựt mã " + mãBíMật;
        String danhSáchGiaoDịchMới = bảnGhiGiaoDịchMới + (danhSáchGiaoDịchĐãLưu.isEmpty() ? "" : ";;" + danhSáchGiaoDịchĐãLưu);

        // 3. Cập nhật SharedPreferences
        bộLưuTrữCụcBộ.edit()
                .putInt("SO_DU_XU_SS", sốDưMới)
                .putString("DANH_SACH_VOUCHER_DA_LUU", danhSáchVoucherMới)
                .putString("DANH_SACH_GIAO_DICH", danhSáchGiaoDịchMới)
                .apply();

        // 4. Cập nhật nút
        if (nútGiựt != null) {
            nútGiựt.setEnabled(false);
            nútGiựt.setText("✓ ĐÃ GIỰT DEAL THÀNH CÔNG");
        }

        Toast.makeText(this, "🎉 Chúc mừng bạn đã giựt thành công " + tênDeal + "! Voucher đã lưu vào Ví của bạn.", Toast.LENGTH_LONG).show();
    }

    private void bắtĐầuĐếmNgượcTrựcTiếp() {
        long tổngThờiGianMilliGiây = (2 * 3600 + 45 * 60 + 12) * 1000L;

        bộĐếmNgượcThờiGian = new CountDownTimer(tổngThờiGianMilliGiây, 1000) {
            @Override
            public void onTick(long thờiGianCònLạiMilliGiây) {
                long tổngSốGiây = thờiGianCònLạiMilliGiây / 1000;
                long giờ = tổngSốGiây / 3600;
                long phút = (tổngSốGiây % 3600) / 60;
                long giây = tổngSốGiây % 60;

                if (nhãnGiờĐếmNgược != null) {
                    nhãnGiờĐếmNgược.setText(String.format(Locale.getDefault(), "%02d", giờ));
                }
                if (nhãnPhútĐếmNgược != null) {
                    nhãnPhútĐếmNgược.setText(String.format(Locale.getDefault(), "%02d", phút));
                }
                if (nhãnGiâyĐếmNgược != null) {
                    nhãnGiâyĐếmNgược.setText(String.format(Locale.getDefault(), "%02d", giây));
                }
            }

            @Override
            public void onFinish() {
                if (nhãnGiờĐếmNgược != null) nhãnGiờĐếmNgược.setText("00");
                if (nhãnPhútĐếmNgược != null) nhãnPhútĐếmNgược.setText("00");
                if (nhãnGiâyĐếmNgược != null) nhãnGiâyĐếmNgược.setText("00");
                Toast.makeText(FlashGrabActivity.this, "Sự kiện Giựt Deal đã kết thúc!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bộĐếmNgượcThờiGian != null) {
            bộĐếmNgượcThờiGian.cancel();
        }
    }
}
