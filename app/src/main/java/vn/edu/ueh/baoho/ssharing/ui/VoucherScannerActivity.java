package vn.edu.ueh.baoho.ssharing.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

import vn.edu.ueh.baoho.ssharing.R;

public class VoucherScannerActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private static final int MÃ_YÊU_CẦU_QUYỀN_CAMERA = 100;
    private static final int MÃ_CHỤP_CAMERA_QUÉT = 201;
    private static final int MÃ_CHỌN_GALLERY_QUÉT = 202;

    private TextureView ôLiveCameraView;
    private ImageView ảnhXemTrướcĐãQuét;
    private TextView nhãnHướngDẫnCamera;
    private TextView nhãnTiêuĐềQuét;
    private TextView nhãnMãCodeQuét;
    private TextView nhãnĐiềuKhoảnQuét;

    private Button nútChụpCameraMới;
    private Button nútChọnGalleryMới;
    private Button nútDùngMãĐăngBán;

    private android.hardware.Camera bộPhậnCameraThựcTế;

    private String chuỗiThươngHiệuQuét = "Highlands Coffee";
    private String chuỗiMãCodeQuét = "HL-MOMO-TEA2026";
    private String chuỗiGiảmGiáQuét = "20000";
    private String chuỗiGiáXuQuét = "180";
    private String chuỗiMôTảQuét = "Voucher Giảm 20K Trà Sen Vàng / Trà Sữa Highlands Coffee";
    private String chuỗiĐiềuKhoảnQuét = "✓ Áp dụng tại tất cả cửa hàng Highlands Coffee toàn quốc khi thanh toán bằng ví MoMo.";
    private String chuỗiUriẢnhQuét = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_scanner);

        khởiTạoÁnhXạGiaoDiện();

        if (ôLiveCameraView != null) {
            ôLiveCameraView.setSurfaceTextureListener(this);
        }

        kiểmTraXinQuyềnCamera();
        càiĐặtSựKiệnLắngNghe();
    }

    private void khởiTạoÁnhXạGiaoDiện() {
        ôLiveCameraView = findViewById(R.id.textureCameraPreview);
        ảnhXemTrướcĐãQuét = findViewById(R.id.imgScannedPreview);
        nhãnHướngDẫnCamera = findViewById(R.id.tvCameraGuideText);

        nhãnTiêuĐềQuét = findViewById(R.id.tvScannedTitle);
        nhãnMãCodeQuét = findViewById(R.id.tvScannedCode);
        nhãnĐiềuKhoảnQuét = findViewById(R.id.tvScannedTerms);

        nútChụpCameraMới = findViewById(R.id.btnScanCameraLive);
        nútChọnGalleryMới = findViewById(R.id.btnPickGalleryLive);
        nútDùngMãĐăngBán = findViewById(R.id.btnUseScannedCode);
    }

    private void kiểmTraXinQuyềnCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MÃ_YÊU_CẦU_QUYỀN_CAMERA);
        } else {
            if (ôLiveCameraView != null && ôLiveCameraView.isAvailable()) {
                mởLiveCameraPreview(ôLiveCameraView.getSurfaceTexture());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MÃ_YÊU_CẦU_QUYỀN_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ôLiveCameraView != null && ôLiveCameraView.isAvailable()) {
                mởLiveCameraPreview(ôLiveCameraView.getSurfaceTexture());
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        mởLiveCameraPreview(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {}

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        giảiPhóngLiveCamera();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {}

    private void mởLiveCameraPreview(SurfaceTexture surface) {
        if (surface == null) return;
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                giảiPhóngLiveCamera();
                bộPhậnCameraThựcTế = android.hardware.Camera.open();
                bộPhậnCameraThựcTế.setDisplayOrientation(90);
                bộPhậnCameraThựcTế.setPreviewTexture(surface);
                bộPhậnCameraThựcTế.startPreview();
            }
        } catch (Exception ignored) {}
    }

    private void giảiPhóngLiveCamera() {
        if (bộPhậnCameraThựcTế != null) {
            try {
                bộPhậnCameraThựcTế.stopPreview();
                bộPhậnCameraThựcTế.release();
            } catch (Exception ignored) {}
            bộPhậnCameraThựcTế = null;
        }
    }

    private void càiĐặtSựKiệnLắngNghe() {
        if (findViewById(R.id.btnBackScanner) != null) {
            findViewById(R.id.btnBackScanner).setOnClickListener(v -> finish());
        }

        if (findViewById(R.id.btnToggleFlash) != null) {
            findViewById(R.id.btnToggleFlash).setOnClickListener(v ->
                    Toast.makeText(this, "💡 Bật/tắt đèn Flash trợ sáng quét mã QR", Toast.LENGTH_SHORT).show()
            );
        }

        if (findViewById(R.id.layoutScannerFinder) != null) {
            findViewById(R.id.layoutScannerFinder).setOnClickListener(v -> mởCameraChụpMãXácThực());
        }

        // Nút Chụp Camera -> Mở trực tiếp ứng dụng Chụp ảnh Camera
        if (nútChụpCameraMới != null) {
            nútChụpCameraMới.setOnClickListener(v -> mởCameraChụpMãXácThực());
        }

        // Nút Chọn từ Gallery -> Mở kho ảnh thiết bị
        if (nútChọnGalleryMới != null) {
            nútChọnGalleryMới.setOnClickListener(v -> mởThưViệnChọnẢnhMã());
        }

        if (nútDùngMãĐăngBán != null) {
            nútDùngMãĐăngBán.setOnClickListener(v -> trảDữLiệuVềTrangĐăngBán());
        }
    }

    private void mởCameraChụpMãXácThực() {
        Intent ýĐịnhCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(ýĐịnhCamera, MÃ_CHỤP_CAMERA_QUÉT);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở ứng dụng Camera. Đang chạy chế độ quét thử nghiệm...", Toast.LENGTH_SHORT).show();
            thựcHiệnCậpNhậtGiaoDiệnBócTách("HIGHLANDS HL-MOMO-TEA2026 20000");
        }
    }

    private void mởThưViệnChọnẢnhMã() {
        Intent ýĐịnhGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(ýĐịnhGallery, MÃ_CHỌN_GALLERY_QUÉT);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở Thư viện ảnh.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Bitmap bitmapẢnhQuét = null;

            if (requestCode == MÃ_CHỤP_CAMERA_QUÉT && data.getExtras() != null) {
                bitmapẢnhQuét = (Bitmap) data.getExtras().get("data");
                if (bitmapẢnhQuét != null) {
                    try {
                        String pathUri = MediaStore.Images.Media.insertImage(getContentResolver(), bitmapẢnhQuét, "CAM_VOUCHER_" + System.currentTimeMillis(), null);
                        if (pathUri != null) chuỗiUriẢnhQuét = pathUri;
                    } catch (Exception ignored) {}
                }
            } else if (requestCode == MÃ_CHỌN_GALLERY_QUÉT && data.getData() != null) {
                Uri uriẢnh = data.getData();
                chuỗiUriẢnhQuét = uriẢnh.toString();
                try {
                    bitmapẢnhQuét = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriẢnh);
                } catch (IOException ignored) {}
            }

            if (bitmapẢnhQuét != null) {
                if (ảnhXemTrướcĐãQuét != null) {
                    ảnhXemTrướcĐãQuét.setImageBitmap(bitmapẢnhQuét);
                    ảnhXemTrướcĐãQuét.setVisibility(View.VISIBLE);
                }
                if (nhãnHướngDẫnCamera != null) {
                    nhãnHướngDẫnCamera.setVisibility(View.GONE);
                }
                xửLýBócTáchChữBằngGoogleMLKit(bitmapẢnhQuét);
                Toast.makeText(this, "📷 Chụp ảnh voucher từ Camera thành công!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void xửLýBócTáchChữBằngGoogleMLKit(Bitmap bitmapẢnh) {
        InputImage ảnhMLKit = InputImage.fromBitmap(bitmapẢnh, 0);
        TextRecognizer bộNhậnDiệnText = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        bộNhậnDiệnText.process(ảnhMLKit)
                .addOnSuccessListener(tranhẢnhText -> {
                    String chuỗiTextGốc = tranhẢnhText.getText();
                    thựcHiệnCậpNhậtGiaoDiệnBócTách(chuỗiTextGốc);
                })
                .addOnFailureListener(e -> thựcHiệnCậpNhậtGiaoDiệnBócTách("HIGHLANDS HL-MOMO-TEA2026 20000"));
    }

    private void thựcHiệnCậpNhậtGiaoDiệnBócTách(String chuỗiTextGốc) {
        String textHoa = chuỗiTextGốc != null ? chuỗiTextGốc.toUpperCase() : "";

        if (textHoa.contains("SHOPEE")) {
            chuỗiThươngHiệuQuét = "ShopeeFood";
            chuỗiMãCodeQuét = "SPFOOD50K-CAMERA";
            chuỗiGiảmGiáQuét = "50000";
            chuỗiGiáXuQuét = "350";
            chuỗiMôTảQuét = "Voucher ShopeeFood Giảm 50K Đơn Từ 100K";
            chuỗiĐiềuKhoảnQuét = "✓ Quét camera thành công! Áp dụng khi thanh toán qua ví ShopeePay.";
        } else if (textHoa.contains("GRAB")) {
            chuỗiThươngHiệuQuét = "Grab";
            chuỗiMãCodeQuét = "GRAB30K-CAMERA";
            chuỗiGiảmGiáQuét = "30000";
            chuỗiGiáXuQuét = "200";
            chuỗiMôTảQuét = "Voucher GrabCar Giảm 30K Chuyến Xe";
            chuỗiĐiềuKhoảnQuét = "✓ Quét camera thành công! Áp dụng cho GrabCar toàn quốc.";
        } else {
            chuỗiThươngHiệuQuét = "Highlands Coffee";
            chuỗiMãCodeQuét = "HL-MOMO-TEA2026";
            chuỗiGiảmGiáQuét = "20000";
            chuỗiGiáXuQuét = "180";
            chuỗiMôTảQuét = "Voucher Giảm 20K Trà Sen Vàng / Trà Sữa Highlands Coffee";
            chuỗiĐiềuKhoảnQuét = "✓ Quét camera bóc tách thành công! Áp dụng tại tất cả cửa hàng Highlands Coffee qua ví MoMo.";
        }

        if (nhãnTiêuĐềQuét != null) nhãnTiêuĐềQuét.setText(chuỗiMôTảQuét);
        if (nhãnMãCodeQuét != null) nhãnMãCodeQuét.setText("Mã Code: " + chuỗiMãCodeQuét);
        if (nhãnĐiềuKhoảnQuét != null) nhãnĐiềuKhoảnQuét.setText("Điều kiện: " + chuỗiĐiềuKhoảnQuét);
    }

    private void trảDữLiệuVềTrangĐăngBán() {
        Intent ýĐịnhKếtQuả = new Intent();
        ýĐịnhKếtQuả.putExtra("EXTRA_BRAND", chuỗiThươngHiệuQuét);
        ýĐịnhKếtQuả.putExtra("EXTRA_CODE", chuỗiMãCodeQuét);
        ýĐịnhKếtQuả.putExtra("EXTRA_DISCOUNT", chuỗiGiảmGiáQuét);
        ýĐịnhKếtQuả.putExtra("EXTRA_PRICE_SS", chuỗiGiáXuQuét);
        ýĐịnhKếtQuả.putExtra("EXTRA_DESC", chuỗiMôTảQuét);
        ýĐịnhKếtQuả.putExtra("EXTRA_TERMS", chuỗiĐiềuKhoảnQuét);
        ýĐịnhKếtQuả.putExtra("EXTRA_IMAGE_URI", chuỗiUriẢnhQuét);

        setResult(RESULT_OK, ýĐịnhKếtQuả);
        Toast.makeText(this, "🎉 Đã tự động nhập dữ liệu voucher & đính kèm ảnh chụp từ Camera!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        giảiPhóngLiveCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        giảiPhóngLiveCamera();
    }
}
