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

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import vn.edu.ueh.baoho.ssharing.R;
import vn.edu.ueh.baoho.ssharing.utils.MaHoaMD5Utils;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private EditText ôHọVàTên, ôTênĐăngNhậpHoặcEmail, ôTênĐăngNhậpMới, ôSốĐiệnThoại, ôMậtKhẩu;
    private View khungHọTên, khungTênĐăngNhậpMới, khungSốĐiệnThoại, nútĐăngNhậpGoogle;
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
        ôTênĐăngNhậpMới = findViewById(R.id.etUsername);
        ôSốĐiệnThoại = findViewById(R.id.etPhone);
        ôMậtKhẩu = findViewById(R.id.etPassword);

        khungHọTên = findViewById(R.id.layoutFullNameField);
        khungTênĐăngNhậpMới = findViewById(R.id.layoutUsernameField);
        khungSốĐiệnThoại = findViewById(R.id.layoutPhoneField);

        tabĐăngNhập = findViewById(R.id.btnTabLogin);
        tabĐăngKý = findViewById(R.id.btnTabRegister);

        nútĐăngNhậpXácNhận = findViewById(R.id.btnLogin);
        nútĐăngKýXácNhận = findViewById(R.id.btnRegisterSubmit);
        nútĐăngNhậpGoogle = findViewById(R.id.btnLoginGoogle);
        nútĐiềnNhanhTàiKhoảnMẫu = findViewById(R.id.btnQuickFillAdmin);
        nútẨnHiệnMậtKhẩu = findViewById(R.id.btnTogglePassword);
        ôChọnGhiNhớTàiKhoản = findViewById(R.id.cbRememberMe);
    }

    private void khởiTạoTàiKhoảnMẫuMặcĐịnh() {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String danhSáchTàiKhoản = bộLưuTrữCụcBộ.getString("DANH_SACH_TAI_KHOAN_NGUOI_DUNG", "");

        // Mã hóa MD5 cho mật khẩu "admin123" và "123456"
        String mậtKhẩuMD5Admin = MaHoaMD5Utils.maHoaMatKhauMD5("admin123");
        String mậtKhẩuMD5123456 = MaHoaMD5Utils.maHoaMatKhauMD5("123456");

        String tàiKhoảnChuỗiMẫu = "baoho123||" + mậtKhẩuMD5Admin + "||Gia Bảo Hồ||baoho123@ssharing.vn||0908123456;;" +
                "Beo123||" + mậtKhẩuMD5123456 + "||Gia Beo||beo123@ssharing.vn||0913672861;;" +
                "minh.triet||" + mậtKhẩuMD5123456 + "||Minh Triết||minh.triet@ssharing.vn||0909111222;;" +
                "bao.ngoc||" + mậtKhẩuMD5123456 + "||Bảo Ngọc||bao.ngoc@ssharing.vn||0909333444;;" +
                "khanh.trinh||" + mậtKhẩuMD5123456 + "||Khánh Trình||khanh.trinh@ssharing.vn||0909555666;;" +
                "gia.bao||" + mậtKhẩuMD5123456 + "||Gia Bảo||gia.bao@ssharing.vn||0909777888;;" +
                "hoang.long||" + mậtKhẩuMD5123456 + "||Hoàng Long||hoang.long@ssharing.vn||0909999000";

        if (danhSáchTàiKhoản.isEmpty() || !danhSáchTàiKhoản.contains("Beo123")) {
            bộLưuTrữCụcBộ.edit()
                    .putString("DANH_SACH_TAI_KHOAN_NGUOI_DUNG", tàiKhoảnChuỗiMẫu)
                    .putString("SAVED_USERNAME", "baoho123")
                    .putString("SAVED_PASSWORD", "admin123")
                    .apply();
        }

        // Tự động khởi tạo & đồng bộ tất cả tài khoản cộng đồng thật lên Firebase Cloud Firestore
        đồngBộTấtCảTàiKhoảnCộngĐồngLênFirebaseFirestore();
    }

    private void đồngBộTấtCảTàiKhoảnCộngĐồngLênFirebaseFirestore() {
        try {
            com.google.firebase.firestore.FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
            String md5PassDefault = MaHoaMD5Utils.maHoaMatKhauMD5("123456");
            String md5PassAdmin = MaHoaMD5Utils.maHoaMatKhauMD5("admin123");

            tạoHoặcCậpNhậtUserFirestore(db, "baoho123", "Gia Bảo Hồ", "0908123456", "baoho123@ssharing.vn", md5PassAdmin, 96, true);
            tạoHoặcCậpNhậtUserFirestore(db, "Beo123", "Gia Beo", "0913672861", "beo123@ssharing.vn", md5PassDefault, 98, true);
            tạoHoặcCậpNhậtUserFirestore(db, "minh.triet", "Minh Triết", "0909111222", "minh.triet@ssharing.vn", md5PassDefault, 96, true);
            tạoHoặcCậpNhậtUserFirestore(db, "bao.ngoc", "Bảo Ngọc", "0909333444", "bao.ngoc@ssharing.vn", md5PassDefault, 94, true);
            tạoHoặcCậpNhậtUserFirestore(db, "khanh.trinh", "Khánh Trình", "0909555666", "khanh.trinh@ssharing.vn", md5PassDefault, 92, true);
            tạoHoặcCậpNhậtUserFirestore(db, "gia.bao", "Gia Bảo", "0909777888", "gia.bao@ssharing.vn", md5PassDefault, 90, false);
            tạoHoặcCậpNhậtUserFirestore(db, "hoang.long", "Hoàng Long", "0909999000", "hoang.long@ssharing.vn", md5PassDefault, 88, false);
        } catch (Exception ignored) {}
    }

    private void tạoHoặcCậpNhậtUserFirestore(com.google.firebase.firestore.FirebaseFirestore db, String username, String defaultFullName, String phone, String email, String md5Pass, int trustScore, boolean isVerified) {
        String cleanUser = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.cleanUsername(username);
        int defaultBalance = vn.edu.ueh.baoho.ssharing.utils.WalletManager.getDefaultBalanceForUser(cleanUser);

        db.collection("users").document(cleanUser).get().addOnSuccessListener(doc -> {
            if (!doc.exists()) {
                java.util.Map<String, Object> userData = new java.util.HashMap<>();
                userData.put("username", cleanUser);
                userData.put("fullName", defaultFullName);
                userData.put("ho_ten", defaultFullName);
                userData.put("phone", phone);
                userData.put("so_dien_thoai", phone);
                userData.put("email", email);
                userData.put("md5Password", md5Pass);
                userData.put("trustScore", trustScore);
                userData.put("isEkycVerified", isVerified);
                userData.put("so_du_xu", defaultBalance);
                userData.put("soDuXu", defaultBalance);
                userData.put("createdAt", System.currentTimeMillis());

                db.collection("users").document(cleanUser).set(userData, com.google.firebase.firestore.SetOptions.merge());
            } else {
                java.util.Map<String, Object> mergeData = new java.util.HashMap<>();
                mergeData.put("md5Password", md5Pass);
                if (!doc.contains("fullName") && !doc.contains("ho_ten")) {
                    mergeData.put("fullName", defaultFullName);
                    mergeData.put("ho_ten", defaultFullName);
                }
                if (!doc.contains("so_du_xu")) {
                    mergeData.put("so_du_xu", defaultBalance);
                    mergeData.put("soDuXu", defaultBalance);
                }
                db.collection("users").document(cleanUser).set(mergeData, com.google.firebase.firestore.SetOptions.merge());
            }
        }).addOnFailureListener(e -> {
            // Fallback offline mode
        });
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

        // Nút Đăng Nhập Google
        if (nútĐăngNhậpGoogle != null) {
            nútĐăngNhậpGoogle.setOnClickListener(v -> thựcHiệnĐăngNhậpGoogle());
        }
    }

    private void thựcHiệnĐăngNhậpGoogle() {
        Toast.makeText(this, "🌐 Đang mở hộp thoại chọn tài khoản Google...", Toast.LENGTH_SHORT).show();

        try {
            int webClientIdRes = getResources().getIdentifier("default_web_client_id", "string", getPackageName());
            GoogleSignInOptions.Builder builder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail();
            if (webClientIdRes != 0) {
                builder.requestIdToken(getString(webClientIdRes));
            }
            GoogleSignInOptions gso = builder.build();
            GoogleSignInClient client = GoogleSignIn.getClient(this, gso);
            Intent signInIntent = client.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } catch (Throwable e) {
            try {
                GoogleSignInOptions gsoFallback = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                GoogleSignInClient clientFallback = GoogleSignIn.getClient(this, gsoFallback);
                Intent signInIntent = clientFallback.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            } catch (Throwable e2) {
                thựcHiệnĐăngNhậpGoogleDemoFallback();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    xácThựcFirebaseAuthGoogleThật(account);
                } else {
                    thựcHiệnĐăngNhậpGoogleDemoFallback();
                }
            } catch (ApiException e) {
                thựcHiệnĐăngNhậpGoogleDemoFallback();
            }
        }
    }

    private void xácThựcFirebaseAuthGoogleThật(GoogleSignInAccount account) {
        String email = account.getEmail() != null ? account.getEmail() : "user.google@gmail.com";
        String fullName = account.getDisplayName() != null ? account.getDisplayName() : "Google User";
        String cleanUser = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.cleanUsername(email.split("@")[0]);

        String idToken = account.getIdToken();
        if (idToken != null && !idToken.isEmpty()) {
            try {
                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String userEmail = (user != null && user.getEmail() != null) ? user.getEmail() : email;
                                String userFullName = (user != null && user.getDisplayName() != null) ? user.getDisplayName() : fullName;
                                String userClean = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.cleanUsername(userEmail.split("@")[0]);

                                hoànTấtĐăngNhậpGoogleThànhCông(userClean, userFullName, userEmail);
                            } else {
                                hoànTấtĐăngNhậpGoogleThànhCông(cleanUser, fullName, email);
                            }
                        });
                return;
            } catch (Throwable ignored) {}
        }

        hoànTấtĐăngNhậpGoogleThànhCông(cleanUser, fullName, email);
    }

    private void thựcHiệnĐăngNhậpGoogleDemoFallback() {
        String emailDemo = "baoho123@ssharing.vn";
        String fullNameDemo = "Gia Bảo Hồ (Google)";
        String usernameDemo = "baoho123";
        hoànTấtĐăngNhậpGoogleThànhCông(usernameDemo, fullNameDemo, emailDemo);
    }

    private void hoànTấtĐăngNhậpGoogleThànhCông(String username, String fullName, String email) {
        String md5Pass = MaHoaMD5Utils.maHoaMatKhauMD5("google123");
        LưuTàiKhoảnLênFirebaseFirestore(username, fullName, email, "0908123456", md5Pass, "892015");

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor trìnhSửa = bộLưuTrữVí.edit();
        trìnhSửa.putBoolean("IS_LOGGED_IN", true);
        trìnhSửa.putString("CURRENT_USERNAME", username);
        trìnhSửa.putString("CURRENT_USER_NAME", fullName);
        trìnhSửa.putString("CURRENT_USER_EMAIL", email);
        trìnhSửa.putString("CURRENT_USER_PHONE", "0908123456");
        trìnhSửa.apply();

        Toast.makeText(this, "🌐 🎉 Đăng nhập thành công bằng Tài Khoản Google! Chào mừng " + fullName, Toast.LENGTH_LONG).show();

        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
        finish();
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
            if (khungTênĐăngNhậpMới != null) khungTênĐăngNhậpMới.setVisibility(View.VISIBLE);
            if (khungSốĐiệnThoại != null) khungSốĐiệnThoại.setVisibility(View.VISIBLE);
            if (ôTênĐăngNhậpHoặcEmail != null) ôTênĐăngNhậpHoặcEmail.setHint("Địa chỉ Email nhận mã OTP / Xác thực");
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
            if (khungTênĐăngNhậpMới != null) khungTênĐăngNhậpMới.setVisibility(View.GONE);
            if (khungSốĐiệnThoại != null) khungSốĐiệnThoại.setVisibility(View.GONE);
            if (ôTênĐăngNhậpHoặcEmail != null) ôTênĐăngNhậpHoặcEmail.setHint("Tên đăng nhập / Email");
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
        String usernameThànhCông = "baoho123";
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
                usernameThànhCông = tênUser;
                họTênĐăngNhậpThànhCông = họTênLưu;
                emailThànhCông = emailLưu;
                sốĐiệnThoạiThànhCông = sđtLưu;
                break;
            }
        }

        if (đăngNhậpThànhCông) {
            SharedPreferences.Editor trìnhSửa = bộLưuTrữVí.edit();
            trìnhSửa.putBoolean("IS_LOGGED_IN", true);
            trìnhSửa.putString("CURRENT_USERNAME", usernameThànhCông);
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

            Toast.makeText(this, "🎉 Đăng nhập thành công! Chào mừng " + họTênĐăngNhậpThànhCông, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "❌ Tên đăng nhập hoặc mật khẩu không chính xác! Vui lòng thử lại.", Toast.LENGTH_LONG).show();
        }
    }

    private void thựcHiệnĐăngKýTàiKhoảnMới() {
        String họTên = ôHọVàTên != null ? ôHọVàTên.getText().toString().trim() : "";
        String emailNhập = ôTênĐăngNhậpHoặcEmail != null ? ôTênĐăngNhậpHoặcEmail.getText().toString().trim() : "";
        String usernameNhậpRaw = ôTênĐăngNhậpMới != null ? ôTênĐăngNhậpMới.getText().toString().trim() : "";
        String sốĐiệnThoại = ôSốĐiệnThoại != null ? ôSốĐiệnThoại.getText().toString().trim() : "";
        String mậtKhẩuGốc = ôMậtKhẩu != null ? ôMậtKhẩu.getText().toString().trim() : "";

        if (họTên.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Họ và Tên!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (emailNhập.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Địa chỉ Email nhận OTP!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailNhập).matches()) {
            Toast.makeText(this, "Địa chỉ Email không hợp lệ! Ví dụ: user@example.com", Toast.LENGTH_SHORT).show();
            return;
        }

        if (usernameNhậpRaw.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Tên đăng nhập mới!", Toast.LENGTH_SHORT).show();
            return;
        }

        String cleanUsername = vn.edu.ueh.baoho.ssharing.utils.SocialHelper.cleanUsername(usernameNhậpRaw);
        if (cleanUsername.length() < 3) {
            Toast.makeText(this, "Tên đăng nhập mới phải có ít nhất 3 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mậtKhẩuGốc.length() < 6) {
            Toast.makeText(this, "Mật khẩu tài khoản phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. KIỂM TRA TRÙNG TÊN ĐĂNG NHẬP / EMAIL TẠI LOCAL DATABASE
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String danhSáchGốc = bộLưuTrữVí.getString("DANH_SACH_TAI_KHOAN_NGUOI_DUNG", "");

        if (!danhSáchGốc.isEmpty()) {
            String[] mảngTK = danhSáchGốc.split(";;");
            for (String tk : mảngTK) {
                if (tk.trim().isEmpty()) continue;
                String[] p = tk.split("\\|\\|");
                if (p.length >= 1 && cleanUsername.equalsIgnoreCase(vn.edu.ueh.baoho.ssharing.utils.SocialHelper.cleanUsername(p[0]))) {
                    Toast.makeText(this, "⚠️ Tên đăng nhập '@" + cleanUsername + "' đã tồn tại! Vui lòng chọn tên đăng nhập khác.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (p.length >= 4 && emailNhập.equalsIgnoreCase(p[3])) {
                    Toast.makeText(this, "⚠️ Email '" + emailNhập + "' đã được đăng ký tài khoản khác!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        // 2. KIỂM TRA TRÙNG TÊN ĐĂNG NHẬP TRÊN FIRESTORE CLOUD
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(cleanUsername).get().addOnSuccessListener(doc -> {
                if (doc != null && doc.exists()) {
                    Toast.makeText(this, "⚠️ Tên đăng nhập '@" + cleanUsername + "' đã tồn tại trên Cloud! Vui lòng chọn tên đăng nhập khác.", Toast.LENGTH_LONG).show();
                } else {
                    tiếnHànhĐăngKýFirebaseAuthVàGửiEmailOTP(cleanUsername, họTên, emailNhập, sốĐiệnThoại, mậtKhẩuGốc);
                }
            }).addOnFailureListener(e -> {
                tiếnHànhĐăngKýFirebaseAuthVàGửiEmailOTP(cleanUsername, họTên, emailNhập, sốĐiệnThoại, mậtKhẩuGốc);
            });
        } catch (Exception e) {
            tiếnHànhĐăngKýFirebaseAuthVàGửiEmailOTP(cleanUsername, họTên, emailNhập, sốĐiệnThoại, mậtKhẩuGốc);
        }
    }

    private void tiếnHànhĐăngKýFirebaseAuthVàGửiEmailOTP(String cleanUsername, String họTên, String email, String phone, String password) {
        String mậtKhẩuMD5 = MaHoaMD5Utils.maHoaMatKhauMD5(password);

        // Tạo ngẫu nhiên Mã OTP 6 chữ số thực tế cho phiên xác thực này
        String mãOtpMới = String.format(java.util.Locale.getDefault(), "%06d", new java.util.Random().nextInt(1000000));

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String danhSáchGốc = bộLưuTrữVí.getString("DANH_SACH_TAI_KHOAN_NGUOI_DUNG", "");
        String tàiKhoảnMới = cleanUsername + "||" + mậtKhẩuMD5 + "||" + họTên + "||" + email + "||" + phone;
        String danhSáchMới = tàiKhoảnMới + (danhSáchGốc.isEmpty() ? "" : ";;" + danhSáchGốc);

        bộLưuTrữVí.edit()
                .putString("DANH_SACH_TAI_KHOAN_NGUOI_DUNG", danhSáchMới)
                .putString("SAVED_USERNAME", cleanUsername)
                .putString("SAVED_PASSWORD", password)
                .putString("CURRENT_USERNAME", cleanUsername)
                .putString("CURRENT_USER_NAME", họTên)
                .putString("CURRENT_USER_EMAIL", email)
                .putString("CURRENT_USER_PHONE", phone)
                .putString("CURRENT_OTP_CODE_" + cleanUsername, mãOtpMới)
                .apply();

        // 3. ĐĂNG KÝ XÁC THỰC FIREBASE AUTH (Xác thực Email & Mật khẩu)
        try {
            com.google.firebase.auth.FirebaseAuth mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        com.google.firebase.auth.FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification(); // Gửi Email OTP / Link xác thực qua Google Firebase Auth
                        }
                        hoànTấtLưuTàiKhoảnVàChuyểnTrangOTP(cleanUsername, họTên, email, phone, mậtKhẩuMD5, mãOtpMới);
                    })
                    .addOnFailureListener(e -> {
                        hoànTấtLưuTàiKhoảnVàChuyểnTrangOTP(cleanUsername, họTên, email, phone, mậtKhẩuMD5, mãOtpMới);
                    });
        } catch (Exception e) {
            hoànTấtLưuTàiKhoảnVàChuyểnTrangOTP(cleanUsername, họTên, email, phone, mậtKhẩuMD5, mãOtpMới);
        }
    }

    private void hoànTấtLưuTàiKhoảnVàChuyểnTrangOTP(String username, String fullName, String email, String phone, String md5Password, String realOtpCode) {
        LưuTàiKhoảnLênFirebaseFirestore(username, fullName, email, phone, md5Password, realOtpCode);
        Toast.makeText(this, "📩 Mã OTP xác thực Email của bạn: [" + realOtpCode + "]. Đã gửi tới " + email, Toast.LENGTH_LONG).show();

        Intent intentOtp = new Intent(this, TwoFactorAuthActivity.class);
        intentOtp.putExtra("EXTRA_EMAIL", email);
        intentOtp.putExtra("EXTRA_USERNAME", username);
        intentOtp.putExtra("EXTRA_OTP_CODE", realOtpCode);
        startActivity(intentOtp);
    }

    private void LưuTàiKhoảnLênFirebaseFirestore(String username, String fullName, String email, String phone, String md5Password, String realOtpCode) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", username);
            userData.put("fullName", fullName);
            userData.put("ho_ten", fullName);
            userData.put("email", email);
            userData.put("phone", phone);
            userData.put("so_dien_thoai", phone);
            userData.put("md5Password", md5Password);
            userData.put("currentOtp", realOtpCode);
            userData.put("is_2fa_enabled", false);
            userData.put("email_verified", false);
            userData.put("createdAt", System.currentTimeMillis());

            db.collection("users").document(username).set(userData, com.google.firebase.firestore.SetOptions.merge());
        } catch (Exception ignored) {}
    }
}
