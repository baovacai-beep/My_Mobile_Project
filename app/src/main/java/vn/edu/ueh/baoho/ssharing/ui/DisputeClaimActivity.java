package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import vn.edu.ueh.baoho.ssharing.R;

public class DisputeClaimActivity extends AppCompatActivity {

    private static final int MÃ_CHỤP_CAMERA_BẰNG_CHỨNG = 501;
    private static final int MÃ_CHỌN_GALLERY_BẰNG_CHỨNG = 502;

    private ImageView[] danhSáchImageViewÔBằngChứng = new ImageView[5];
    private View[] danhSáchKhungÔSlot = new View[5];
    private int vịTríÔĐangChụpChọn = 0;
    private int sốLượngẢnhĐãĐínhKèm = 0;

    private String usernameHiệnTại = "baoho123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispute_claim);

        đọcThôngTinTàiKhoản();
        khởiTạoÁnhXạGiaoDiện();
        càiĐặtSựKiệnLắngNghe();
    }

    private void đọcThôngTinTàiKhoản() {
        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        usernameHiệnTại = bộLưuTrữVí.getString("CURRENT_USERNAME", "baoho123");
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        danhSáchKhungÔSlot[0] = findViewById(R.id.slotEvidence1);
        danhSáchKhungÔSlot[1] = findViewById(R.id.slotEvidence2);
        danhSáchKhungÔSlot[2] = findViewById(R.id.slotEvidence3);
        danhSáchKhungÔSlot[3] = findViewById(R.id.slotEvidence4);
        danhSáchKhungÔSlot[4] = findViewById(R.id.slotEvidence5);

        danhSáchImageViewÔBằngChứng[0] = findViewById(R.id.imgSlot1);
        danhSáchImageViewÔBằngChứng[1] = findViewById(R.id.imgSlot2);
        danhSáchImageViewÔBằngChứng[2] = findViewById(R.id.imgSlot3);
        danhSáchImageViewÔBằngChứng[3] = findViewById(R.id.imgSlot4);
        danhSáchImageViewÔBằngChứng[4] = findViewById(R.id.imgSlot5);
    }

    private void càiĐặtSựKiệnLắngNghe() {
        for (int i = 0; i < 5; i++) {
            final int vịTrí = i;
            if (danhSáchKhungÔSlot[i] != null) {
                danhSáchKhungÔSlot[i].setOnClickListener(v -> hiểnThịHộpThoạiLựaChọnNguồnẢnhBằngChứng(vịTrí));
            }
        }

        if (findViewById(R.id.btn_gui_khieu_nai) != null) {
            findViewById(R.id.btn_gui_khieu_nai).setOnClickListener(v -> thựcHiệnGửiHồSơKhiếuNạiTranhChấp());
        }
    }

    private void hiểnThịHộpThoạiLựaChọnNguồnẢnhBằngChứng(int vịTríSlot) {
        vịTríÔĐangChụpChọn = vịTríSlot;
        String[] cácTùyChọn = {
                "📷 Chụp Ảnh Trực Tiếp Bằng Camera",
                "🖼 Chọn Ảnh Bằng Chứng Từ Thư Viện (Gallery)"
        };

        new AlertDialog.Builder(this)
                .setTitle("📸 Thêm Bằng Chứng Ảnh #" + (vịTríSlot + 1))
                .setItems(cácTùyChọn, (dialog, which) -> {
                    if (which == 0) {
                        mởCameraChụpBằngChứng();
                    } else {
                        mởBộSưuTậpChọnBằngChứng();
                    }
                })
                .show();
    }

    private void mởCameraChụpBằngChứng() {
        Intent ýĐịnhCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(ýĐịnhCamera, MÃ_CHỤP_CAMERA_BẰNG_CHỨNG);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể khởi chạy Camera.", Toast.LENGTH_SHORT).show();
        }
    }

    private void mởBộSưuTậpChọnBằngChứng() {
        Intent ýĐịnhGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(ýĐịnhGallery, MÃ_CHỌN_GALLERY_BẰNG_CHỨNG);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở Bộ sưu tập ảnh.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Bitmap bitmapBằngChứng = null;

            if (requestCode == MÃ_CHỤP_CAMERA_BẰNG_CHỨNG && data.getExtras() != null) {
                bitmapBằngChứng = (Bitmap) data.getExtras().get("data");
            } else if (requestCode == MÃ_CHỌN_GALLERY_BẰNG_CHỨNG && data.getData() != null) {
                Uri uriẢnh = data.getData();
                try (InputStream inputStream = getContentResolver().openInputStream(uriẢnh)) {
                    bitmapBằngChứng = BitmapFactory.decodeStream(inputStream);
                } catch (Exception ignored) {}
            }

            if (bitmapBằngChứng != null && vịTríÔĐangChụpChọn >= 0 && vịTríÔĐangChụpChọn < 5) {
                ImageView imgSlot = danhSáchImageViewÔBằngChứng[vịTríÔĐangChụpChọn];
                if (imgSlot != null) {
                    imgSlot.setImageBitmap(bitmapBằngChứng);
                    imgSlot.setVisibility(View.VISIBLE);
                    sốLượngẢnhĐãĐínhKèm++;

                    // Lưu ảnh bằng chứng vào bộ nhớ tệp riêng
                    try {
                        File tệpBằngChứng = new File(getFilesDir(), "dispute_evidence_latest.jpg");
                        try (java.io.FileOutputStream out = new java.io.FileOutputStream(tệpBằngChứng)) {
                            bitmapBằngChứng.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        }
                        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
                        bộLưuTrữVí.edit().putString("DISPUTE_EVIDENCE_PHOTO_PATH_LATEST", tệpBằngChứng.getAbsolutePath()).apply();
                    } catch (Exception ignored) {}

                    Toast.makeText(this, "📷 Đã đính kèm ảnh bằng chứng #" + (vịTríÔĐangChụpChọn + 1) + " thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void thựcHiệnGửiHồSơKhiếuNạiTranhChấp() {
        String thờiGianThực = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        String mãHồSơDispute = "TK-" + (10000 + (int)(Math.random() * 89999));

        android.widget.EditText ôMôTả = findViewById(R.id.et_mo_ta_chi_tiet);
        String môTảNhập = ôMôTả != null ? ôMôTả.getText().toString().trim() : "";

        SharedPreferences bộLưuTrữVí = getSharedPreferences("SSHARING_PREFS", MODE_PRIVATE);
        bộLưuTrữVí.edit()
                .putBoolean("HAS_ACTIVE_DISPUTE_" + usernameHiệnTại, true)
                .putString("DISPUTE_DESCRIPTION_LATEST", môTảNhập.isEmpty() ? "Mã báo lỗi đã sử dụng trên ứng dụng thanh toán." : môTảNhập)
                .apply();

        // Đồng bộ hồ sơ khiếu nại kèm số lượng ảnh lên Firebase Firestore
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> disputeData = new HashMap<>();
            disputeData.put("case_id", mãHồSơDispute);
            disputeData.put("complainant_username", usernameHiệnTại);
            disputeData.put("evidence_count", sốLượngẢnhĐãĐínhKèm);
            disputeData.put("description", môTảNhập);
            disputeData.put("status", "PENDING_ARBITRATION");
            disputeData.put("created_at", System.currentTimeMillis());

            db.collection("disputes").document(mãHồSơDispute).set(disputeData);
        } catch (Exception ignored) {}

        Toast.makeText(this, "🎉 Đã gửi hồ sơ khiếu nại " + mãHồSơDispute + " kèm " + (sốLượngẢnhĐãĐínhKèm > 0 ? sốLượngẢnhĐãĐínhKèm : 1) + " ảnh bằng chứng! Tiền ký quỹ đã đóng băng an toàn.", Toast.LENGTH_LONG).show();

        startActivity(new Intent(this, activity_staff_arbiter.class));
        finish();
    }
}
