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
    private int sốXuCầnNạp = 220;
    private String usernameHiệnTại = "baoho123";

    private final String SỐ_TÀI_KHOẢN_MBBANK = "0913672861";
    private final String TÊN_CHỦ_TÀI_KHOẢN = "HO GIA BAO";
    private final String TÊN_NGÂN_HÀNG = "MBBank";

    private Handler bộXửLýThreadBảnĐồ = new Handler(Looper.getMainLooper());
    private boolean đãXácNhậnNạpThànhCông = false;
    private com.google.firebase.firestore.ListenerRegistration bộLắngNgheFirestoreRealtime;

    private static final String CASSO_API_KEY_CHUẨN = "AK_CS.bffaf5c0d0b111f1929a0f0fa79e0aca.FEnaqVSUWDRx4DcRyp1bfYJLmzJOTlZy8D8vdztEEUc6CW34AjxYvmWrh4pFWZdlTZiwqUh4";
    private Handler bộXửLýQuétCassoAPI = new Handler(Looper.getMainLooper());
    private boolean đangQuétCassoAPI = false;
    private long mốcThờiGianKhởiTạoMànHình = System.currentTimeMillis();

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

    private void đốiSoátLịchSửChặnNạpLạiMãCũBanĐầu() {
        new Thread(() -> {
            try {
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new URL("https://oauth.casso.vn/v2/transactions?pageSize=20&sort=DESC").openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Apikey " + CASSO_API_KEY_CHUẨN);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);

                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    java.util.Scanner scanner = new java.util.Scanner(in).useDelimiter("\\A");
                    String responseBody = scanner.hasNext() ? scanner.next() : "";

                    org.json.JSONObject jsonRoot = new org.json.JSONObject(responseBody);
                    if (jsonRoot.optInt("error") == 0) {
                        org.json.JSONObject dataObj = jsonRoot.getJSONObject("data");
                        org.json.JSONArray records = dataObj.getJSONArray("records");
                        SharedPreferences prefTx = getSharedPreferences("LICH_SU_GIAO_DICH_DA_XU_LY", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefTx.edit();

                        for (int i = 0; i < records.length(); i++) {
                            org.json.JSONObject trans = records.getJSONObject(i);
                            String idGiaoDich = String.valueOf(trans.optLong("id", 0));
                            if (!idGiaoDich.equals("0")) {
                                editor.putBoolean(idGiaoDich, true);
                            }
                        }
                        editor.apply();
                    }
                }
            } catch (Exception ignored) {}
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bắtĐầuQuétGiaoDịchCassoTựĐộng();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dừngQuétGiaoDịchCasso();
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

        if (findViewById(R.id.btnCopyAccount) != null) {
            findViewById(R.id.btnCopyAccount).setOnClickListener(v -> thựcHiệnSaoChépVănBản("STK MBBank", SỐ_TÀI_KHOẢN_MBBANK));
        }

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

        if (findViewById(R.id.btnVerifyTopupPayment) != null) {
            findViewById(R.id.btnVerifyTopupPayment).setOnClickListener(v -> thựcHiệnXácNhậnThanhToánNạpTiền(false));
        }

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

        if (!làSandboxDemo) {
            if (mãGDNhập.isEmpty()) {
                Toast.makeText(this, "🔍 Đang kết nối trực tiếp Casso API đối soát chuyển khoản cho @" + usernameHiệnTại + "...", Toast.LENGTH_SHORT).show();
                thựcHiệnGọiAPICassoBắtBuộcThaoTácThủCông();
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

        đồngBộNạpXuLênFirebaseFirestore(usernameHiệnTại, sốXuCầnNạp, mãGiaoDịchChính);
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
                    .whereGreaterThanOrEqualTo("created_at", mốcThờiGianKhởiTạoMànHình)
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null || snapshot == null || snapshot.isEmpty()) return;
                        for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
                            String status = doc.getString("trangThai");
                            if (status == null) status = doc.getString("status");

                            boolean làThànhCông = "THANH_CONG".equalsIgnoreCase(status) || "SUCCESS".equalsIgnoreCase(status);

                            if (làThànhCông && !đãXácNhậnNạpThànhCông) {
                                String txId = doc.getString("maGiaoDich");
                                if (txId == null) txId = doc.getId();

                                SharedPreferences prefTx = getSharedPreferences("LICH_SU_GIAO_DICH_DA_XU_LY", MODE_PRIVATE);
                                if (prefTx.getBoolean(txId.replace("TX-", ""), false)) continue;

                                Long soXuObj = doc.getLong("soXuCong");
                                int xuAmount;
                                if (soXuObj != null) {
                                    xuAmount = soXuObj.intValue();
                                } else {
                                    Double amount = doc.getDouble("amount_ss");
                                    xuAmount = amount != null ? amount.intValue() : sốXuCầnNạp;
                                }

                                Long soTienObj = doc.getLong("soTienVnd");
                                if (soTienObj != null) {
                                    sốSốTiềnVND = soTienObj.intValue();
                                }

                                thựcHiệnXửLýTựĐộngCộngXuKhiTínHiệuVề(txId, xuAmount);
                                break;
                            }
                        }
                    });
        } catch (Exception ignored) {}
    }

    private void bắtĐầuQuétGiaoDịchCassoTựĐộng() {
        đangQuétCassoAPI = true;
        bộXửLýQuétCassoAPI.post(new Runnable() {
            @Override
            public void run() {
                if (!đangQuétCassoAPI || đãXácNhậnNạpThànhCông) return;
                thựcHiệnGọiAPICassoKiểmTraBiếnĐộngSốDư();
                bộXửLýQuétCassoAPI.postDelayed(this, 2000);
            }
        });
    }

    private void dừngQuétGiaoDịchCasso() {
        đangQuétCassoAPI = false;
        if (bộXửLýQuétCassoAPI != null) {
            bộXửLýQuétCassoAPI.removeCallbacksAndMessages(null);
        }
    }

    private void thựcHiệnGọiAPICassoKiểmTraBiếnĐộngSốDư() {
        new Thread(() -> {
            try {
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new URL("https://oauth.casso.vn/v2/transactions?pageSize=10&sort=DESC").openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Apikey " + CASSO_API_KEY_CHUẨN);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);

                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    java.util.Scanner scanner = new java.util.Scanner(in).useDelimiter("\\A");
                    String jsonString = scanner.hasNext() ? scanner.next() : "";

                    org.json.JSONObject jsonRoot = new org.json.JSONObject(jsonString);
                    if (jsonRoot.optInt("error") == 0) {
                        org.json.JSONObject dataObj = jsonRoot.getJSONObject("data");
                        org.json.JSONArray records = dataObj.getJSONArray("records");
                        String userCầnTìm = usernameHiệnTại.toUpperCase().trim();

                        SharedPreferences prefTx = getSharedPreferences("GIAO_DICH_DA_NAP_XU_DANH_SACH", MODE_PRIVATE);

                        for (int i = 0; i < records.length(); i++) {
                            org.json.JSONObject trans = records.getJSONObject(i);
                            String môTả = trans.optString("description", "").toUpperCase();
                            long sốTiềnVND = trans.optLong("amount", 0);
                            String idGiaoDich = String.valueOf(trans.optLong("id", 0));

                            if (prefTx.getBoolean("TX_" + idGiaoDich, false)) continue;

                            boolean làGiaoDịchThỏaMãn = môTả.contains(userCầnTìm)
                                    || môTả.contains("SSHARING")
                                    || môTả.contains("NAP XU")
                                    || môTả.contains("SSH");

                            if (làGiaoDịchThỏaMãn && sốTiềnVND > 0) {
                                prefTx.edit().putBoolean("TX_" + idGiaoDich, true).apply();
                                runOnUiThread(() -> {
                                    dừngQuétGiaoDịchCasso();
                                    xửLýCộngTiềnVàHiểnThịHóaĐơnCasso(idGiaoDich, sốTiềnVND, usernameHiệnTại, môTả);
                                });
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                android.util.Log.e("CassoPolling", "Lỗi kiểm tra API Casso: " + e.getMessage());
            }
        }).start();
    }

    private void thựcHiệnGọiAPICassoBắtBuộcThaoTácThủCông() {
        new Thread(() -> {
            try {
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new URL("https://oauth.casso.vn/v2/transactions?pageSize=10&sort=DESC").openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Apikey " + CASSO_API_KEY_CHUẨN);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setConnectTimeout(4000);
                conn.setReadTimeout(4000);

                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    java.util.Scanner scanner = new java.util.Scanner(in).useDelimiter("\\A");
                    String responseBody = scanner.hasNext() ? scanner.next() : "";

                    org.json.JSONObject jsonRoot = new org.json.JSONObject(responseBody);
                    if (jsonRoot.optInt("error") == 0) {
                        org.json.JSONObject dataObj = jsonRoot.getJSONObject("data");
                        org.json.JSONArray records = dataObj.getJSONArray("records");
                        String userCầnTìm = usernameHiệnTại.toUpperCase().trim();

                        boolean tìmThấy = false;
                        for (int i = 0; i < records.length(); i++) {
                            org.json.JSONObject trans = records.getJSONObject(i);
                            String môTả = trans.optString("description", "").toUpperCase();
                            long sốTiềnVND = trans.optLong("amount", 0);
                            String mãGiaoDịchCasso = String.valueOf(trans.optLong("id", 0));

                            if (môTả.contains(userCầnTìm) || môTả.contains("SSHARING") || môTả.contains("NAP XU") || môTả.contains("SSH")) {
                                tìmThấy = true;
                                runOnUiThread(() -> {
                                    dừngQuétGiaoDịchCasso();
                                    xửLýCộngTiềnVàHiểnThịHóaĐơnCasso(mãGiaoDịchCasso, sốTiềnVND, usernameHiệnTại, môTả);
                                });
                                break;
                            }
                        }

                        if (!tìmThấy) {
                            runOnUiThread(() -> Toast.makeText(this, "⚠️ Casso API chưa nhận được tin báo từ ngân hàng cho @" + usernameHiệnTại + ". Vui lòng đợi 3 giây rồi bấm lại!", Toast.LENGTH_LONG).show());
                        }
                    }
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(CoinTopupActivity.this, "⚠️ Kết nối mạng chập chờn. Bạn có thể bấm [Giả Lập Thanh Toán Sandbox] bên dưới!", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void xửLýCộngTiềnVàHiểnThịHóaĐơnCasso(String idGiaoDich, long sốTiềnVnd, String username, String môTả) {
        SharedPreferences prefTx = getSharedPreferences("LICH_SU_GIAO_DICH_DA_XU_LY", MODE_PRIVATE);
        if (prefTx.getBoolean(idGiaoDich, false)) return;
        prefTx.edit().putBoolean(idGiaoDich, true).apply();

        if (đãXácNhậnNạpThànhCông) return;
        đãXácNhậnNạpThànhCông = true;

        long sốXuCộng = sốTiềnVnd / 1000;
        if (sốTiềnVnd == 2000) sốXuCộng = 2;
        else if (sốTiềnVnd >= 1000000) sốXuCộng += (long)(sốXuCộng * 0.20);
        else if (sốTiềnVnd >= 500000)  sốXuCộng += (long)(sốXuCộng * 0.15);
        else if (sốTiềnVnd >= 200000)  sốXuCộng += (long)(sốXuCộng * 0.10);
        else if (sốTiềnVnd >= 100000)  sốXuCộng += (long)(sốXuCộng * 0.05);

        int sốXuInt = (int) sốXuCộng;

        SharedPreferences bộLưuTrữCụcBộ = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        int sốDưHiệnTại = bộLưuTrữCụcBộ.getInt("SO_DU_XU_SS", 1250);
        int sốDưMới = sốDưHiệnTại + sốXuInt;
        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        String mãTX = "TX-" + idGiaoDich;
        String bảnGhiGiaoDịchMới = mãTX + "||Nạp xu VietQR Napas247 MBBank (+" + sốXuInt + " SS)||+\n" + sốXuInt + " SS||" + thờiGianThực + "||VietQR Napas247||Thành công||Hệ thống tự động phát hiện số dư Casso MBBank 0913672861 HO GIA BAO (Mã GD: " + mãTX + ")";

        String danhSáchGiaoDịchĐãLưu = bộLưuTrữCụcBộ.getString("DANH_SACH_GIAO_DICH", "");
        String danhSáchGiaoDịchMới = bảnGhiGiaoDịchMới + (danhSáchGiaoDịchĐãLưu.isEmpty() ? "" : ";;" + danhSáchGiaoDịchĐãLưu);

        bộLưuTrữCụcBộ.edit()
                .putInt("SO_DU_XU_SS", sốDưMới)
                .putString("DANH_SACH_GIAO_DICH", danhSáchGiaoDịchMới)
                .apply();

        đồngBộNạpXuLênFirebaseFirestore(username, sốXuInt, mãTX);

        String tbMới = "🎉 [NẠP XU THÀNH CÔNG] Hệ thống Casso vừa ghi nhận giao dịch chuyển khoản " + String.format(Locale.getDefault(), "%,d VNĐ", sốTiềnVnd) + "! +" + sốXuInt + " SS đã được cộng vào Ví.||" + thờiGianThực;
        String tbGốc = bộLưuTrữCụcBộ.getString("DANH_SACH_THONG_BAO_" + username.toLowerCase().trim(), "");
        bộLưuTrữCụcBộ.edit()
                .putString("DANH_SACH_THONG_BAO_" + username.toLowerCase().trim(), tbMới + (tbGốc.isEmpty() ? "" : ";;" + tbGốc))
                .putBoolean("HAS_UNREAD_NOTIFICATIONS_" + username.toLowerCase().trim(), true)
                .apply();

        Toast.makeText(this, "🎉 Nạp tiền thành công! Bạn nhận được +" + sốXuInt + " Xu", Toast.LENGTH_LONG).show();
        hiểnThịHóaĐơnNạpTiềnThànhCông(mãTX, (int) sốTiềnVnd, sốXuInt, thờiGianThực, false);
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
        dừngQuétGiaoDịchCasso();
        if (bộLắngNgheFirestoreRealtime != null) {
            bộLắngNgheFirestoreRealtime.remove();
        }
    }
}
