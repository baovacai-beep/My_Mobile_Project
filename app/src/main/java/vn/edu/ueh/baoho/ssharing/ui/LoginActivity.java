package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import vn.edu.ueh.baoho.ssharing.R;
import vn.edu.ueh.baoho.ssharing.utils.MaHoaMD5Utils;

public class LoginActivity extends AppCompatActivity {

    private EditText ôHọVàTên, ôTênĐăngNhậpHoặcEmail, ôSốĐiệnThoại, ôMậtKhẩu;
    private View khungHọTên, khungSốĐiệnThoại;
    private TextView tabĐăngNhập, tabĐăngKý;
    private Button nútĐăngNhậpXácNhận, nútĐăngKýXácNhận;
    private TextView nútĐiềnNhanhTàiKhoảnMẫu;
    private ImageView nútẨnHiệnMậtKhẩu;
    private CheckBox ôChọnGhiNhớTàiKhoản;

    private boolean làMậtKhẩuĐangHiện = false;
    private boolean làChếĐộĐăngKý = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ui);

        khởiTạoTàiKhoảnMẫuMặcĐịnh();
        khởiTạoÁnhXạGiaoDiện();
        tựĐộngĐiềnTàiKhoảnĐãGhiNhớTrướcĐó();
        càiĐặtSựKiệnLắngNghe();
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        ôHọVàTên = findViewById(R.id.etFullName);
        ôTênĐăngNhậpHoặcEmail = findViewById(R.id.etEmail);
        ôSốĐiệnThoại = findViewById(R.id.etPhone);
        ôMậtKhẩu = findViewById(R.id.etPassword);

        khungHọTên = findViewById(R.id.layoutFullNameField);
        khungSốĐiệnThoại = findViewById(R.id.layoutPhoneField);

        tabĐăngNhập = findViewById(R.id.btnTabLogin);
        tabĐăngKý = findViewById(R.id.btnTabRegister);

        nútĐăngNhậpXácNhận = findViewById(R.id.btnLogin);
        nútĐăngKýXácNhận = findViewById(R.id.btnRegisterSubmit);
        nútĐiềnNhanhTàiKhoảnMẫu = findViewById(R.id.btnQuickFillAdmin);
        nútẨnHiệnMậtKhẩu = findViewById(R.id.btnTogglePassword);
        ôChọnGhiNhớTàiKhoản = findViewById(R.id.cbRememberMe);
    }

    private void khởiTạoTàiKhoảnMẫuMặcĐịnh() {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String danhSáchTàiKhoản = bộLưuTrữCụcBộ.getString("DANH_SACH_TAI_KHOAN_NGUOI_DUNG", "");

        // Mã hóa MD5 cho mật khẩu "admin123"
        String mậtKhẩuMD5Admin = MaHoaMD5Utils.maHoaMatKhauMD5("admin123");
        String tàiKhoảnMẫuAdmin = "baoho123||" + mậtKhẩuMD5Admin + "||Gia Bảo Hồ||baoho123@ssharing.vn||0908123456";

        if (danhSáchTàiKhoản.isEmpty() || !danhSáchTàiKhoản.contains("baoho123")) {
            String danhSáchMới = tàiKhoảnMẫuAdmin + (danhSáchTàiKhoản.isEmpty() ? "" : ";;" + danhSáchTàiKhoản);
            bộLưuTrữCụcBộ.edit()
                    .putString("DANH_SACH_TAI_KHOAN_NGUOI_DUNG", danhSáchMới)
                    .putString("SAVED_USERNAME", "baoho123")
                    .putString("SAVED_PASSWORD", "admin123")
                    .apply();
        }
    }

    private void tựĐộngĐiềnTàiKhoảnĐãGhiNhớTrướcĐó() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String tênĐãLưu = bộLưuTrữVí.getString("SAVED_USERNAME", "baoho123");
        String mậtKhẩuĐãLưu = bộLưuTrữVí.getString("SAVED_PASSWORD", "admin123");

        if (ôTênĐăngNhậpHoặcEmail != null && tênĐãLưu != null && !tênĐãLưu.isEmpty()) {
            ôTênĐăngNhậpHoặcEmail.setText(tênĐãLưu);
        }
        if (ôMậtKhẩu != null && mậtKhẩuĐãLưu != null && !mậtKhẩuĐãLưu.isEmpty()) {
            ôMậtKhẩu.setText(mậtKhẩuĐãLưu);
        }
    }

    private void càiĐặtSựKiệnLắngNghe() {
        // Chuyển Tab Đăng Nhập / Đăng Ký
        if (tabĐăngNhập != null) {
            tabĐăngNhập.setOnClickListener(v -> chuyểnChếĐộĐăngNhập(false));
        }

        if (tabĐăngKý != null) {
            tabĐăngKý.setOnClickListener(v -> chuyểnChếĐộĐăngNhập(true));
        }

        // Điền nhanh tài khoản baoho123 / admin123
        if (nútĐiềnNhanhTàiKhoảnMẫu != null) {
            nútĐiềnNhanhTàiKhoảnMẫu.setOnClickListener(v -> {
                if (ôTênĐăngNhậpHoặcEmail != null) ôTênĐăngNhậpHoặcEmail.setText("baoho123");
                if (ôMậtKhẩu != null) ôMậtKhẩu.setText("admin123");
                Toast.makeText(this, "Đã điền tài khoản mẫu baoho123!", Toast.LENGTH_SHORT).show();
            });
        }

        // Toggle ẩn/hiện mật khẩu
        if (nútẨnHiệnMậtKhẩu != null && ôMậtKhẩu != null) {
            nútẨnHiệnMậtKhẩu.setOnClickListener(v -> {
                làMậtKhẩuĐangHiện = !làMậtKhẩuĐangHiện;
                if (làMậtKhẩuĐangHiện) {
                    ôMậtKhẩu.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    ôMậtKhẩu.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                ôMậtKhẩu.setSelection(ôMậtKhẩu.getText().length());
            });
        }

        // Quên mật khẩu
        if (findViewById(R.id.tvForgotPassword) != null) {
            findViewById(R.id.tvForgotPassword).setOnClickListener(v ->
                    startActivity(new Intent(this, TwoFactorAuthActivity.class))
            );
        }

        // Nút Đăng Nhập
        if (nútĐăngNhậpXácNhận != null) {
            nútĐăngNhậpXácNhận.setOnClickListener(v -> thựcHiệnĐăngNhậpTàiKhoản());
        }

        // Nút Đăng Ký
        if (nútĐăngKýXácNhận != null) {
            nútĐăngKýXácNhận.setOnClickListener(v -> thựcHiệnĐăngKýTàiKhoảnMới());
        }
    }

    private void chuyểnChếĐộĐăngNhập(boolean làĐăngKý) {
        làChếĐộĐăngKý = làĐăngKý;
        if (làĐăngKý) {
            if (tabĐăngKý != null) {
                tabĐăngKý.setBackgroundResource(R.drawable.bg_tab_active);
                tabĐăngKý.setTextColor(Color.WHITE);
            }
            if (tabĐăngNhập != null) {
                tabĐăngNhập.setBackground(null);
                tabĐăngNhập.setTextColor(Color.parseColor("#888888"));
            }

            if (khungHọTên != null) khungHọTên.setVisibility(View.VISIBLE);
            if (khungSốĐiệnThoại != null) khungSốĐiệnThoại.setVisibility(View.VISIBLE);
            if (nútĐăngNhậpXácNhận != null) nútĐăngNhậpXácNhận.setVisibility(View.GONE);
            if (nútĐăngKýXácNhận != null) nútĐăngKýXácNhận.setVisibility(View.VISIBLE);
        } else {
            if (tabĐăngNhập != null) {
                tabĐăngNhập.setBackgroundResource(R.drawable.bg_tab_active);
                tabĐăngNhập.setTextColor(Color.WHITE);
            }
            if (tabĐăngKý != null) {
                tabĐăngKý.setBackground(null);
                tabĐăngKý.setTextColor(Color.parseColor("#888888"));
            }

            if (khungHọTên != null) khungHọTên.setVisibility(View.GONE);
            if (khungSốĐiệnThoại != null) khungSốĐiệnThoại.setVisibility(View.GONE);
            if (nútĐăngNhậpXácNhận != null) nútĐăngNhậpXácNhận.setVisibility(View.VISIBLE);
            if (nútĐăngKýXácNhận != null) nútĐăngKýXácNhận.setVisibility(View.GONE);
        }
    }

    private void thựcHiệnĐăngNhậpTàiKhoản() {
        String tênĐăngNhậpNhập = ôTênĐăngNhậpHoặcEmail != null ? ôTênĐăngNhậpHoặcEmail.getText().toString().trim() : "";
        String mậtKhẩuGốcNhập = ôMậtKhẩu != null ? ôMậtKhẩu.getText().toString().trim() : "";

        if (tênĐăngNhậpNhập.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Tên đăng nhập hoặc Email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mậtKhẩuGốcNhập.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mã hóa MD5 mật khẩu vừa nhập để đối soát
        String mậtKhẩuMD5Nhập = MaHoaMD5Utils.maHoaMatKhauMD5(mậtKhẩuGốcNhập);

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String danhSáchChuỗiTàiKhoản = bộLưuTrữVí.getString("DANH_SACH_TAI_KHOAN_NGUOI_DUNG", "");

        boolean đăngNhậpThànhCông = false;
        String họTênĐăngNhậpThànhCông = "Gia Bảo Hồ";
        String emailThànhCông = tênĐăngNhậpNhập;
        String sốĐiệnThoạiThànhCông = "0908123456";

        String[] cácTàiKhoản = danhSáchChuỗiTàiKhoản.split(";;");
        for (String chuỗiTàiKhoản : cácTàiKhoản) {
            if (chuỗiTàiKhoản.trim().isEmpty()) continue;
            String[] mảngTrường = chuỗiTàiKhoản.split("\\|\\|");
            if (mảngTrường.length < 2) continue;

            String tênUser = mảngTrường[0];
            String mậtKhẩuMD5Lưu = mảngTrường[1];
            String họTênLưu = mảngTrường.length > 2 ? mảngTrường[2] : tênUser;
            String emailLưu = mảngTrường.length > 3 ? mảngTrường[3] : tênUser;
            String sđtLưu = mảngTrường.length > 4 ? mảngTrường[4] : "0908123456";

            // So sánh Tên đăng nhập/Email/SĐT và Mật khẩu mã hóa MD5
            if ((tênĐăngNhậpNhập.equalsIgnoreCase(tênUser) || tênĐăngNhậpNhập.equalsIgnoreCase(emailLưu) || tênĐăngNhậpNhập.equals(sđtLưu))
                    && mậtKhẩuMD5Nhập.equals(mậtKhẩuMD5Lưu)) {
                đăngNhậpThànhCông = true;
                họTênĐăngNhậpThànhCông = họTênLưu;
                emailThànhCông = emailLưu;
                sốĐiệnThoạiThànhCông = sđtLưu;
                break;
            }
        }

        if (đăngNhậpThànhCông) {
            SharedPreferences.Editor trìnhSửa = bộLưuTrữVí.edit();
            trìnhSửa.putBoolean("IS_LOGGED_IN", true);
            trìnhSửa.putString("CURRENT_USERNAME", tênĐăngNhậpNhập);
            trìnhSửa.putString("CURRENT_USER_NAME", họTênĐăngNhậpThànhCông);
            trìnhSửa.putString("CURRENT_USER_EMAIL", emailThànhCông);
            trìnhSửa.putString("CURRENT_USER_PHONE", sốĐiệnThoạiThànhCông);

            // Ghi nhớ thông tin tài khoản & mật khẩu nếu có chọn Checkbox
            if (ôChọnGhiNhớTàiKhoản != null && ôChọnGhiNhớTàiKhoản.isChecked()) {
                trìnhSửa.putString("SAVED_USERNAME", tênĐăngNhậpNhập);
                trìnhSửa.putString("SAVED_PASSWORD", mậtKhẩuGốcNhập);
            } else {
                trìnhSửa.remove("SAVED_USERNAME");
                trìnhSửa.remove("SAVED_PASSWORD");
            }
            trìnhSửa.apply();

            Toast.makeText(this, "🎉 Đăng nhập thành công! Chào mừng " + họTênĐăngNhậpThànhCông, Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "❌ Tên đăng nhập hoặc mật khẩu không chính xác! Vui lòng thử lại.", Toast.LENGTH_LONG).show();
        }
    }

    private void thựcHiệnĐăngKýTàiKhoảnMới() {
        String họTên = ôHọVàTên != null ? ôHọVàTên.getText().toString().trim() : "";
        String tênĐăngNhậpHoặcEmail = ôTênĐăngNhậpHoặcEmail != null ? ôTênĐăngNhậpHoặcEmail.getText().toString().trim() : "";
        String sốĐiệnThoại = ôSốĐiệnThoại != null ? ôSốĐiệnThoại.getText().toString().trim() : "";
        String mậtKhẩuGốc = ôMậtKhẩu != null ? ôMậtKhẩu.getText().toString().trim() : "";

        if (họTên.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Họ và Tên!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tênĐăngNhậpHoặcEmail.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Tên đăng nhập hoặc Email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sốĐiệnThoại.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Số điện thoại xác thực!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mậtKhẩuGốc.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mã hóa MD5 cho mật khẩu tài khoản mới
        String mậtKhẩuMD5 = MaHoaMD5Utils.maHoaMatKhauMD5(mậtKhẩuGốc);

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String danhSáchGốc = bộLưuTrữVí.getString("DANH_SACH_TAI_KHOAN_NGUOI_DUNG", "");

        String tàiKhoảnMới = tênĐăngNhậpHoặcEmail + "||" + mậtKhẩuMD5 + "||" + họTên + "||" + tênĐăngNhậpHoặcEmail + "||" + sốĐiệnThoại;
        String danhSáchMới = tàiKhoảnMới + (danhSáchGốc.isEmpty() ? "" : ";;" + danhSáchGốc);

        bộLưuTrữVí.edit()
                .putString("DANH_SACH_TAI_KHOAN_NGUOI_DUNG", danhSáchMới)
                .putString("SAVED_USERNAME", tênĐăngNhậpHoặcEmail)
                .putString("SAVED_PASSWORD", mậtKhẩuGốc)
                .apply();

        // Đồng bộ lưu tài khoản lên Firebase Firestore
        LưuTàiKhoảnLênFirebaseFirestore(tênĐăngNhậpHoặcEmail, họTên, sốĐiệnThoại, mậtKhẩuMD5);

        Toast.makeText(this, "🎉 Đăng ký tài khoản thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show();
        chuyểnChếĐộĐăngNhập(false);
    }

    private void LưuTàiKhoảnLênFirebaseFirestore(String username, String fullName, String phone, String md5Password) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", username);
            userData.put("fullName", fullName);
            userData.put("phone", phone);
            userData.put("md5Password", md5Password);
            userData.put("createdAt", System.currentTimeMillis());

            db.collection("users").document(username).set(userData);
        } catch (Exception ignored) {}
    }
}
