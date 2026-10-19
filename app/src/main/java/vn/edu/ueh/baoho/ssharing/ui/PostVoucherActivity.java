package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.ueh.baoho.ssharing.R;

public class PostVoucherActivity extends AppCompatActivity {

    private AutoCompleteTextView ôThươngHiệu;
    private EditText ôMãVoucher, ôGiảmGiá, ôHạnDùng, ôGiáBánSS, ôMôTảChiTiết;
    private RadioGroup nhómLoạiHìnhĐăng;
    private RadioButton nútChọnTặngCộngĐồng;
    private Button nútGửiBàiĐăng;

    private boolean làChếĐộChỉnhSửa = false;
    private String mãBàiĐăngCầnSửa = "";

    private final String[] danhSáchThươngHiệuGợiÝ = {
            "Shopee", "Grab", "Lazada", "Tiki", "TikTok Shop",
            "Highlands Coffee", "Starbucks Coffee", "Phúc Long", "MoMo",
            "CGV Cinemas", "Be Group", "Xanh SM", "Galaxy Cinema", "Spotify", "ShopeeFood"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_voucher);

        khởiTạoÁnhXạGiaoDiện();
        càiĐặtThươngHiệuAutoComplete();
        kiểmTraChếĐộChỉnhSửa();
        càiĐặtSựKiệnLắngNghe();
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        ôThươngHiệu = findViewById(R.id.etBrand);
        ôMãVoucher = findViewById(R.id.etCode);
        ôGiảmGiá = findViewById(R.id.etDiscount);
        ôHạnDùng = findViewById(R.id.etExpiry);
        ôGiáBánSS = findViewById(R.id.etSellingPrice);
        ôMôTảChiTiết = findViewById(R.id.etDescription);
        nhómLoạiHìnhĐăng = findViewById(R.id.rg_sell_type);
        nútChọnTặngCộngĐồng = findViewById(R.id.rb_donate_community);
        nútGửiBàiĐăng = findViewById(R.id.btnSubmit);
    }

    private void càiĐặtThươngHiệuAutoComplete() {
        if (ôThươngHiệu != null) {
            ArrayAdapter<String> bộĐiềuPhốiThươngHiệu = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_dropdown_item_1line,
                    danhSáchThươngHiệuGợiÝ
            );
            ôThươngHiệu.setAdapter(bộĐiềuPhốiThươngHiệu);
            ôThươngHiệu.setOnClickListener(v -> ôThươngHiệu.showDropDown());
            ôThươngHiệu.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) ôThươngHiệu.showDropDown();
            });
        }
    }

    private void kiểmTraChếĐộChỉnhSửa() {
        Intent dữLiệuĐầuVào = getIntent();
        if (dữLiệuĐầuVào != null && dữLiệuĐầuVào.getBooleanExtra("IS_EDIT", false)) {
            làChếĐộChỉnhSửa = true;
            mãBàiĐăngCầnSửa = dữLiệuĐầuVào.getStringExtra("VOUCHER_ID");

            String thươngHiệuSửa = dữLiệuĐầuVào.getStringExtra("BRAND");
            String mãSửa = dữLiệuĐầuVào.getStringExtra("CODE");
            String giáSửa = dữLiệuĐầuVào.getStringExtra("PRICE");
            String môTảSửa = dữLiệuĐầuVào.getStringExtra("DESC");

            if (ôThươngHiệu != null && thươngHiệuSửa != null) ôThươngHiệu.setText(thươngHiệuSửa);
            if (ôMãVoucher != null && mãSửa != null) ôMãVoucher.setText(mãSửa);
            if (ôGiáBánSS != null && giáSửa != null) ôGiáBánSS.setText(giáSửa);
            if (ôMôTảChiTiết != null && môTảSửa != null) ôMôTảChiTiết.setText(môTảSửa);

            if (nútGửiBàiĐăng != null) nútGửiBàiĐăng.setText("Cập nhật bài đăng ngay");
        }
    }

    private void càiĐặtSựKiệnLắngNghe() {
        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(vương -> finish());
        }

        if (nhómLoạiHìnhĐăng != null) {
            nhómLoạiHìnhĐăng.setOnCheckedChangeListener((nhóm, mãNútĐượcChọn) -> {
                if (mãNútĐượcChọn == R.id.rb_donate_community) {
                    if (ôGiáBánSS != null) {
                        ôGiáBánSS.setText("0");
                        ôGiáBánSS.setEnabled(false);
                    }
                } else {
                    if (ôGiáBánSS != null) {
                        ôGiáBánSS.setEnabled(true);
                    }
                }
            });
        }

        if (nútGửiBàiĐăng != null) {
            nútGửiBàiĐăng.setOnClickListener(vương -> thựcHiệnLưuBàiĐăngVoucher());
        }
    }

    private void thựcHiệnLưuBàiĐăngVoucher() {
        String thươngHiệu = ôThươngHiệu != null ? ôThươngHiệu.getText().toString().trim() : "Shopee";
        String mãVoucher = ôMãVoucher != null ? ôMãVoucher.getText().toString().trim() : "SP50K";
        String giáBánChuỗi = ôGiáBánSS != null ? ôGiáBánSS.getText().toString().trim() : "1500";
        String hạnDùng = ôHạnDùng != null ? ôHạnDùng.getText().toString().trim() : "31/12/2026";
        String môTả = ôMôTảChiTiết != null && !ôMôTảChiTiết.getText().toString().trim().isEmpty()
                ? ôMôTảChiTiết.getText().toString().trim()
                : "Giảm 50K đơn từ 250K - Điện tử";

        if (mãVoucher.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã voucher!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String danhSáchĐăngBánGốc = bộLưuTrữCụcBộ.getString("DANH_SACH_VOUCHER_DANG_BAN", "");

        String mãBàiĐăng = làChếĐộChỉnhSửa ? mãBàiĐăngCầnSửa : "POS_" + System.currentTimeMillis();
        String bảnGhiMới = mãBàiĐăng + "||" + môTả + "||" + thươngHiệu + "||" + mãVoucher + "||" + giáBánChuỗi + "||" + hạnDùng + "||" + môTả;

        StringBuilder danhSáchMới = new StringBuilder();

        if (làChếĐộChỉnhSửa) {
            // Thay thế bài đăng cũ
            String[] cácBài = danhSáchĐăngBánGốc.split(";;");
            for (String bài : cácBài) {
                if (bài.trim().isEmpty()) continue;
                if (bài.startsWith(mãBàiĐăngCầnSửa)) {
                    if (danhSáchMới.length() > 0) danhSáchMới.append(";;");
                    danhSáchMới.append(bảnGhiMới);
                } else {
                    if (danhSáchMới.length() > 0) danhSáchMới.append(";;");
                    danhSáchMới.append(bài);
                }
            }
            Toast.makeText(this, "Đã cập nhật bài đăng voucher thành công!", Toast.LENGTH_LONG).show();
        } else {
            // Thêm bài đăng mới lên đầu
            if (danhSáchĐăngBánGốc.isEmpty()) {
                danhSáchMới.append(bảnGhiMới);
            } else {
                danhSáchMới.append(bảnGhiMới).append(";;").append(danhSáchĐăngBánGốc);
            }
            Toast.makeText(this, "Đã đăng bán Voucher thành công! Bài đăng đang hiển thị trên sàn.", Toast.LENGTH_LONG).show();
        }

        bộLưuTrữCụcBộ.edit()
                .putString("DANH_SACH_VOUCHER_DANG_BAN", danhSáchMới.toString())
                .apply();

        startActivity(new Intent(this, SellerDashboardActivity.class));
        finish();
    }
}
