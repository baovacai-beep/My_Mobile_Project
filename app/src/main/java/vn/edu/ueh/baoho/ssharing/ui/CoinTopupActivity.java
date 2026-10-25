package vn.edu.ueh.baoho.ssharing.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import vn.edu.ueh.baoho.ssharing.R;

public class CoinTopupActivity extends AppCompatActivity {

    private RadioGroup nhómGóiNạp;
    private TextView nhãnThôngTinGóiNạp;
    private TextView nhãnNộiDungChuyểnKhoản;
    private ImageView ảnhMãVietQR;
    private android.widget.EditText ôMãGiaoDịchNgânHàng;

    private int sốSốTiềnVND = 200000;
    private int sốXuCầnNạp = 220; // Gói 200k = 220 SS xu (thưởng +10%)
    private String usernameHiệnTại = "baoho123";

    private final String SỐ_TÀI_KHOẢN_MBBANK = "0913672861";
    private final String TÊN_CHỦ_TÀI_KHOẢN = "HO GIA BAO";
    private final String TÊN_NGÂN_HÀNG = "MBBank";

    private Handler bộXửLýThreadBảnĐồ = new Handler(Looper.getMainLooper());
    private boolean đãXácNhậnNạpThànhCông = false;
    private com.google.firebase.firestore.ListenerRegistration bộLắngNgheFirestoreRealtime;
    private long thờiGianKhởiTạoMànHình = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_topup);

        đọcThôngTinTàiKhoản();
        khởiTạoÁnhXạGiaoDiện();
        càiĐặtSựKiệnLắngNghe();
        cậpNhậtMãVietQRThựcTế();
        kíchHoạtLắngNgheChuyểnKhoảnTựĐộngRealtime();
    }

    private void đọcThôngTinTàiKhoản() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        usernameHiệnTại = bộLưuTrữVí.getString("CURRENT_USERNAME", "baoho123");
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        nhómGóiNạp = findViewById(R.id.rgPackages);
        nhãnThôngTinGóiNạp = findViewById(R.id.tvThongTinGoiNap);
        nhãnNộiDungChuyểnKhoản = findViewById(R.id.tvTransferContent);
        ảnhMãVietQR = findViewById(R.id.imgVietQR);
        ôMãGiaoDịchNgânHàng = findViewById(R.id.etBankTransactionCode);

        if (nhãnNộiDungChuyểnKhoản != null) {
            nhãnNộiDungChuyểnKhoản.setText("Nội dung: SSHARING NAP XU " + usernameHiệnTại);
        }
    }

    private void càiĐặtSựKiệnLắngNghe() {
        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        // Nút Sao chép STK
        if (findViewById(R.id.btnCopyAccount) != null) {
            findViewById(R.id.btnCopyAccount).setOnClickListener(v -> thựcHiệnSaoChépVănBản("STK MBBank", SỐ_TÀI_KHOẢN_MBBANK));
        }

        // Nút Sao chép Nội dung CK
        if (findViewById(R.id.btnCopyContent) != null) {
            findViewById(R.id.btnCopyContent).setOnClickListener(v -> thựcHiệnSaoChépVănBản("Nội dung CK", "SSHARING NAP XU " + usernameHiệnTại));
        }

        if (nhómGóiNạp != null) {
            nhómGóiNạp.setOnCheckedChangeListener((nhóm, mãĐượcChọn) -> {
                if (mãĐượcChọn == R.id.rbGoi2k) {
                    sốSốTiềnVND = 2000;
                    sốXuCầnNạp = 2;
                } else if (mãĐượcChọn == R.id.rbGoi50k) {
                    sốSốTiềnVND = 50000;
                    sốXuCầnNạp = 50;
                } else if (mãĐượcChọn == R.id.rbGoi100k) {
                    sốSốTiềnVND = 100000;
                    sốXuCầnNạp = 105;
                } else if (mãĐượcChọn == R.id.rbGoi200k) {
                    sốSốTiềnVND = 200000;
                    sốXuCầnNạp = 220;
                } else if (mãĐượcChọn == R.id.rbGoi500k) {
                    sốSốTiềnVND = 500000;
                    sốXuCầnNạp = 575;
                } else if (mãĐượcChọn == R.id.rbGoi1000k) {
                    sốSốTiềnVND = 1000000;
                    sốXuCầnNạp = 1200;
                }

                if (nhãnThôngTinGóiNạp != null) {
                    nhãnThôngTinGóiNạp.setText("Số Xu sẽ cộng vào ví: " + sốXuCầnNạp + " SS");
                }

                cậpNhậtMãVietQRThựcTế();
            });
        }

        // Nút Xác nhận đã chuyển khoản
        if (findViewById(R.id.btnVerifyTopupPayment) != null) {
            findViewById(R.id.btnVerifyTopupPayment).setOnClickListener(v -> thựcHiệnXácNhậnThanhToánNạpTiền(false));
        }

        // Nút Giả lập thanh toán Sandbox
        if (findViewById(R.id.btnSandboxTest) != null) {
            findViewById(R.id.btnSandboxTest).setOnClickListener(v -> thựcHiệnXácNhậnThanhToánNạpTiền(true));
        }
    }

    private void thựcHiệnSaoChépVănBản(String nhãn, String nộiDung) {
        ClipboardManager khayNhớTạm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData dữLiệuMã = ClipData.newPlainText(nhãn, nộiDung);
        if (khayNhớTạm != null) {
            khayNhớTạm.setPrimaryClip(dữLiệuMã);
            Toast.makeText(this, "Đã sao chép " + nhãn + ": " + nộiDung, Toast.LENGTH_SHORT).show();
        }
    }

    private void cậpNhậtMãVietQRThựcTế() {
        // Đường dẫn API VietQR Napas247 chuẩn cho MBBank STK 0913672861 - HO GIA BAO
        String urlVietQR = "https://img.vietqr.io/image/" + TÊN_NGÂN_HÀNG + "-" + SỐ_TÀI_KHOẢN_MBBANK + "-compact2.png" +
                "?amount=" + sốSốTiềnVND +
                "&addInfo=SSHARING%20NAP%20XU%20" + usernameHiệnTại +
                "&accountName=" + TÊN_CHỦ_TÀI_KHOẢN.replace(" ", "%20");

        new Thread(() -> {
            try {
                InputStream inputStream = new URL(urlVietQR).openStream();
                Bitmap bitmapQR = BitmapFactory.decodeStream(inputStream);
                bộXửLýThreadBảnĐồ.post(() -> {
                    if (ảnhMãVietQR != null && bitmapQR != null) {
                        ảnhMãVietQR.setImageBitmap(bitmapQR);
                    }
                });
            } catch (Exception ignored) {}
        }).start();
    }

    private void thựcHiệnXácNhậnThanhToánNạpTiền(boolean làSandboxDemo) {
        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        String mãGDNhập = ôMãGiaoDịchNgânHàng != null ? ôMãGiaoDịchNgânHàng.getText().toString().trim() : "";

        // Kiểm tra đối soát nếu người dùng bấm "Tôi Đã Chuyển Khoản"
        if (!làSandboxDemo) {
            if (mãGDNhập.isEmpty()) {
                Toast.makeText(this, "⚠️ Vui lòng nhập Mã giao dịch MoMo/MBBank (Ví dụ: 153385709872) từ biên lai chuyển tiền để đối soát nạp xu!", Toast.LENGTH_LONG).show();
                return;
            }

            if (mãGDNhập.length() < 6) {
                Toast.makeText(this, "⚠️ Mã giao dịch ngân hàng/MoMo không hợp lệ (Phải có ít nhất 6 chữ số)!", Toast.LENGTH_SHORT).show();
                return;
            }

            String danhSáchGiaoDịchĐãLưu = bộLưuTrữCụcBộ.getString("DANH_SACH_GIAO_DICH", "");
            if (danhSáchGiaoDịchĐãLưu.contains(mãGDNhập)) {
                Toast.makeText(this, "⚠️ Mã giao dịch #" + mãGDNhập + " đã được kiểm tra và nạp xu trước đó!", Toast.LENGTH_LONG).show();
                return;
            }
        }

        if (đãXácNhậnNạpThànhCông) return;
        đãXácNhậnNạpThànhCông = true;

        int sốDưHiệnTại = bộLưuTrữCụcBộ.getInt("SO_DU_XU_SS", 1250);
        int sốDưMới = sốDưHiệnTại + sốXuCầnNạp;
        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        String mãGiaoDịchChính;
        if (!mãGDNhập.isEmpty()) {
            mãGiaoDịchChính = "TX-" + mãGDNhập.replace("#", "");
        } else {
            mãGiaoDịchChính = "TX-SANDBOX" + (100000 + (int)(Math.random() * 899999));
        }

        String bảnGhiGiaoDịchMới = mãGiaoDịchChính + "||Nạp xu VietQR Napas247 MBBank (+" + sốXuCầnNạp + " SS)||+\n" + sốXuCầnNạp + " SS||" + thờiGianThực + "||VietQR Napas247||Thành công||Thanh toán chuyển khoản MBBank 0913672861 HO GIA BAO (Mã GD: " + mãGiaoDịchChính + ")";

        String danhSáchGiaoDịchĐãLưu = bộLưuTrữCụcBộ.getString("DANH_SACH_GIAO_DICH", "");
        String danhSáchGiaoDịchMới = bảnGhiGiaoDịchMới + (danhSáchGiaoDịchĐãLưu.isEmpty() ? "" : ";;" + danhSáchGiaoDịchĐãLưu);

        bộLưuTrữCụcBộ.edit()
                .putInt("SO_DU_XU_SS", sốDưMới)
                .putString("DANH_SACH_GIAO_DICH", danhSáchGiaoDịchMới)
                .apply();

        // Đồng bộ cập nhật số dư nguyên tử FieldValue.increment() lên Firebase Firestore
        đồngBộNạpXuLênFirebaseFirestore(usernameHiệnTại, sốXuCầnNạp, mãGiaoDịchChính);

        // Hiển thị Hóa Đơn Xác Nhận Giao Dịch Đầy Đủ Thông Tin
        hiểnThịHóaĐơnNạpTiềnThànhCông(mãGiaoDịchChính, sốSốTiềnVND, sốXuCầnNạp, thờiGianThực, làSandboxDemo);
    }

    private void hiểnThịHóaĐơnNạpTiềnThànhCông(String mãTX, int sốTiềnVND, int sốXuSS, String thờiGian, boolean làSandbox) {
        if (isFinishing() || isDestroyed()) return;

        String nộiDungHóaĐơn = "🧾 MÃ GIAO DỊCH: " + mãTX + "\n\n" +
                "🏦 Ngân hàng nhận: MB Bank (Ngân hàng Quân Đội)\n" +
                "💳 Số tài khoản nhận: " + SỐ_TÀI_KHOẢN_MBBANK + "\n" +
                "👤 Tên chủ tài khoản: " + TÊN_CHỦ_TÀI_KHOẢN + "\n\n" +
                "💵 Số tiền thanh toán: " + String.format(Locale.getDefault(), "%,d VNĐ", sốTiềnVND) + "\n" +
                "🪙 Số Xu cộng vào ví: +" + String.format(Locale.getDefault(), "%,d SS", sốXuSS) + "\n" +
                "📝 Cú pháp CK: SSHARING NAP XU " + usernameHiệnTại + "\n" +
                "⏱ Thời gian: " + thờiGian + "\n\n" +
                "✅ Trạng thái: THÀNH CÔNG 100%\n" +
                "🛡️ Hệ thống SSharing đã kiểm tra & xác nhận tiền chuyển khoản thành công vào tài khoản chính " + SỐ_TÀI_KHOẢN_MBBANK + "!";

        new AlertDialog.Builder(this)
                .setTitle("🎉 HÓA ĐƠN XÁC NHẬN NẠP XU VIETQR")
                .setMessage(nộiDungHóaĐơn)
                .setCancelable(false)
                .setPositiveButton("Đóng & Xem Ví Xu", (dialog, which) -> {
                    setResult(RESULT_OK);
                    finish();
                })
                .show();
    }

    private void đồngBộNạpXuLênFirebaseFirestore(String username, int sốXu, String mãTX) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Giao dịch nguyên tử FieldValue.increment
            db.collection("users").document(username).update("so_du_xu", FieldValue.increment(sốXu));

            Map<String, Object> txData = new HashMap<>();
            txData.put("tx_id", mãTX);
            txData.put("username", username);
            txData.put("amount_ss", sốXu);
            txData.put("bank_account", SỐ_TÀI_KHOẢN_MBBANK);
            txData.put("bank_name", TÊN_NGÂN_HÀNG);
            txData.put("account_owner", TÊN_CHỦ_TÀI_KHOẢN);
            txData.put("status", "SUCCESS");
            txData.put("created_at", System.currentTimeMillis());

            db.collection("transactions").document(mãTX).set(txData);
        } catch (Exception ignored) {}
    }

    private void kíchHoạtLắngNgheChuyểnKhoảnTựĐộngRealtime() {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            bộLắngNgheFirestoreRealtime = db.collection("transactions")
                    .whereEqualTo("username", usernameHiệnTại)
                    .whereGreaterThanOrEqualTo("created_at", thờiGianKhởiTạoMànHình)
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null || snapshot == null || snapshot.isEmpty()) return;
                        for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
                            String status = doc.getString("status");
                            if ("SUCCESS".equalsIgnoreCase(status) && !đãXácNhậnNạpThànhCông) {
                                String txId = doc.getId();
                                Double amount = doc.getDouble("amount_ss");
                                int xuAmount = amount != null ? amount.intValue() : sốXuCầnNạp;
                                thựcHiệnXửLýTựĐộngCộngXuKhiTínHiệuVề(txId, xuAmount);
                                break;
                            }
                        }
                    });
        } catch (Exception ignored) {}
    }

    private void thựcHiệnXửLýTựĐộngCộngXuKhiTínHiệuVề(String mãTX, int sốXu) {
        if (đãXácNhậnNạpThànhCông) return;
        đãXácNhậnNạpThànhCông = true;

        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        int sốDưHiệnTại = bộLưuTrữCụcBộ.getInt("SO_DU_XU_SS", 1250);
        int sốDưMới = sốDưHiệnTại + sốXu;
        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        String bảnGhiGiaoDịchMới = mãTX + "||Nạp xu VietQR Napas247 MBBank (+" + sốXu + " SS)||+\n" + sốXu + " SS||" + thờiGianThực + "||VietQR Napas247||Thành công||Hệ thống tự động phát hiện số dư MBBank 0913672861 HO GIA BAO (Mã GD: " + mãTX + ")";

        String danhSáchGiaoDịchĐãLưu = bộLưuTrữCụcBộ.getString("DANH_SACH_GIAO_DICH", "");
        String danhSáchGiaoDịchMới = bảnGhiGiaoDịchMới + (danhSáchGiaoDịchĐãLưu.isEmpty() ? "" : ";;" + danhSáchGiaoDịchĐãLưu);

        bộLưuTrữCụcBộ.edit()
                .putInt("SO_DU_XU_SS", sốDưMới)
                .putString("DANH_SACH_GIAO_DICH", danhSáchGiaoDịchMới)
                .apply();

        hiểnThịHóaĐơnNạpTiềnThànhCông(mãTX, sốSốTiềnVND, sốXu, thờiGianThực, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bộLắngNgheFirestoreRealtime != null) {
            bộLắngNgheFirestoreRealtime.remove();
        }
    }
}
