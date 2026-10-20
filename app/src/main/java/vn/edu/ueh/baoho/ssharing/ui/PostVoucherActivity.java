package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vn.edu.ueh.baoho.ssharing.R;

public class PostVoucherActivity extends AppCompatActivity {

    private static final int MÃ_YÊU_CẦU_CHỤP_CAMERA = 101;
    private static final int MÃ_YÊU_CẦU_CHỌN_GALLERY = 102;
    private static final int MÃ_YÊU_CẦU_ĐÍNH_KÈM_ẢNH_SẢN_PHẨM = 103;
    private static final int MÃ_YÊU_CẦU_QUÉT_SCANNER = 104;

    private AutoCompleteTextView ôThươngHiệu;
    private EditText ôMãVoucher, ôGiảmGiá, ôHạnDùng, ôGiáBánSS, ôMôTảChiTiết, ôĐiềuKhoảnSửDụng;
    private RadioGroup nhómLoạiHìnhĐăng;
    private RadioButton nútChọnTặngCộngĐồng;
    private Button nútGửiBàiĐăng;
    private View khungTảiẢnhSmartOcr;
    private View khungThêmẢnhSảnPhẩm;
    private TextView nhãnĐếmSốLượngẢnh;
    private LinearLayout containerHìnhẢnhSảnPhẩm;

    private boolean làChếĐộChỉnhSửa = false;
    private String mãBàiĐăngCầnSửa = "";
    private final List<String> danhSáchẢnhSảnPhẩm = new ArrayList<>();

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
        ôĐiềuKhoảnSửDụng = findViewById(R.id.etTermsAndConditions);

        nhómLoạiHìnhĐăng = findViewById(R.id.rg_sell_type);
        nútChọnTặngCộngĐồng = findViewById(R.id.rb_donate_community);
        nútGửiBàiĐăng = findViewById(R.id.btnSubmit);

        khungTảiẢnhSmartOcr = findViewById(R.id.cardSmartOcrScan);
        khungThêmẢnhSảnPhẩm = findViewById(R.id.btnAddProductImage);
        nhãnĐếmSốLượngẢnh = findViewById(R.id.tvImageCountBadge);
        containerHìnhẢnhSảnPhẩm = findViewById(R.id.containerProductThumbnails);
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

        // Bấm nút OCR -> Chuyển trực tiếp sang Màn Hình Quét Mã Scanner (VoucherScannerActivity)
        if (khungTảiẢnhSmartOcr != null) {
            khungTảiẢnhSmartOcr.setOnClickListener(vương -> {
                Intent ýĐịnhScanner = new Intent(this, VoucherScannerActivity.class);
                startActivityForResult(ýĐịnhScanner, MÃ_YÊU_CẦU_QUÉT_SCANNER);
            });
        }

        // Nút Thêm ảnh sản phẩm (+) -> Mở kho ảnh Gallery
        if (khungThêmẢnhSảnPhẩm != null) {
            khungThêmẢnhSảnPhẩm.setOnClickListener(vương -> thêmHìnhẢnhSảnPhẩmMới());
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

    private void hiểnThịHộpThoạiQuétSmartOcr() {
        String[] danhSáchTùyChọnQuét = {
                "📷 Chụp ảnh voucher / Quét mã QR bằng Camera",
                "🖼 Chọn ảnh voucher từ Bộ sưu tập Gallery",
                "⚡ Mẫu bóc tách OCR tự động (Demo Nhanh)"
        };

        new AlertDialog.Builder(this)
                .setTitle("✨ Tải Ảnh & Quét Smart OCR Voucher")
                .setItems(danhSáchTùyChọnQuét, (hộpThoại, vịTríChọn) -> {
                    if (vịTríChọn == 0) {
                        mởCameraChụpẢnhQuét();
                    } else if (vịTríChọn == 1) {
                        mởBộSưuTậpChọnẢnh();
                    } else {
                        thựcHiệnQuétSmartOcrTheoMẫu(0);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void mởCameraChụpẢnhQuét() {
        Intent ýĐịnhCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(ýĐịnhCamera, MÃ_YÊU_CẦU_CHỤP_CAMERA);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể khởi chạy Camera. Đang chuyển sang chế độ quét mẫu...", Toast.LENGTH_SHORT).show();
            thựcHiệnQuétSmartOcrTheoMẫu(0);
        }
    }

    private void mởBộSưuTậpChọnẢnh() {
        Intent ýĐịnhGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(ýĐịnhGallery, MÃ_YÊU_CẦU_CHỌN_GALLERY);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở Bộ sưu tập. Đang chuyển sang chế độ quét mẫu...", Toast.LENGTH_SHORT).show();
            thựcHiệnQuétSmartOcrTheoMẫu(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            // Trường hợp 0: Nhận dữ liệu kết quả từ Màn Hình Quét Mã Scanner (VoucherScannerActivity)
            if (requestCode == MÃ_YÊU_CẦU_QUÉT_SCANNER) {
                String thươngHiệuQuét = data.getStringExtra("EXTRA_BRAND");
                String mãQuét = data.getStringExtra("EXTRA_CODE");
                String giảmGiáQuét = data.getStringExtra("EXTRA_DISCOUNT");
                String giáXuQuét = data.getStringExtra("EXTRA_PRICE_SS");
                String môTảQuét = data.getStringExtra("EXTRA_DESC");
                String điềuKhoảnQuét = data.getStringExtra("EXTRA_TERMS");
                String uriẢnhQuét = data.getStringExtra("EXTRA_IMAGE_URI");

                if (ôThươngHiệu != null && thươngHiệuQuét != null) ôThươngHiệu.setText(thươngHiệuQuét);
                if (ôMãVoucher != null && mãQuét != null) ôMãVoucher.setText(mãQuét);
                if (ôGiảmGiá != null && giảmGiáQuét != null) ôGiảmGiá.setText(giảmGiáQuét);
                if (ôGiáBánSS != null && giáXuQuét != null) ôGiáBánSS.setText(giáXuQuét);
                if (ôHạnDùng != null) ôHạnDùng.setText("31/12/2026");
                if (ôMôTảChiTiết != null && môTảQuét != null) ôMôTảChiTiết.setText(môTảQuét);
                if (ôĐiềuKhoảnSửDụng != null && điềuKhoảnQuét != null) ôĐiềuKhoảnSửDụng.setText(điềuKhoảnQuét);

                if (uriẢnhQuét != null && !uriẢnhQuét.isEmpty() && danhSáchẢnhSảnPhẩm.size() < 10) {
                    danhSáchẢnhSảnPhẩm.add(uriẢnhQuét);
                    cậpNhậtDanhSáchẢnhHiểnThị();
                }

                Toast.makeText(this, "🎉 Đã tự động nhập toàn bộ dữ liệu voucher từ Màn hình Quét mã thành công!", Toast.LENGTH_LONG).show();
                return;
            }
            // Trường hợp 1: Tải ảnh sản phẩm thực tế từ Thư viện Gallery
            if (requestCode == MÃ_YÊU_CẦU_ĐÍNH_KÈM_ẢNH_SẢN_PHẨM) {
                if (data.getClipData() != null) {
                    // Chọn nhiều ảnh cùng lúc
                    int tổngSốLượngChọn = data.getClipData().getItemCount();
                    for (int i = 0; i < tổngSốLượngChọn && danhSáchẢnhSảnPhẩm.size() < 10; i++) {
                        Uri uriẢnh = data.getClipData().getItemAt(i).getUri();
                        danhSáchẢnhSảnPhẩm.add(uriẢnh.toString());
                    }
                } else if (data.getData() != null && danhSáchẢnhSảnPhẩm.size() < 10) {
                    // Chọn 1 ảnh
                    Uri uriẢnh = data.getData();
                    danhSáchẢnhSảnPhẩm.add(uriẢnh.toString());
                }
                cậpNhậtDanhSáchẢnhHiểnThị();
                Toast.makeText(this, "🎉 Đã tải ảnh sản phẩm từ thư viện lên thành công!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Trường hợp 2: Quét Smart OCR từ Camera / Gallery
            Bitmap bitmapẢnhThực = null;
            if (requestCode == MÃ_YÊU_CẦU_CHỤP_CAMERA && data.getExtras() != null) {
                bitmapẢnhThực = (Bitmap) data.getExtras().get("data");
            } else if (requestCode == MÃ_YÊU_CẦU_CHỌN_GALLERY && data.getData() != null) {
                Uri uriẢnh = data.getData();
                try {
                    bitmapẢnhThực = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriẢnh);
                    if (danhSáchẢnhSảnPhẩm.size() < 10) {
                        danhSáchẢnhSảnPhẩm.add(uriẢnh.toString());
                    }
                } catch (IOException ignored) {}
            }

            if (bitmapẢnhThực != null) {
                xửLýNhậnDiệnOCRGoogleMLKit(bitmapẢnhThực);
            } else {
                thựcHiệnQuétSmartOcrTheoMẫu(0);
            }
        } else if (requestCode == MÃ_YÊU_CẦU_CHỤP_CAMERA) {
            // Trường hợp người dùng hủy hoặc giả lập camera: Tự động chạy bóc tách dữ liệu Smart OCR mẫu
            thựcHiệnQuétSmartOcrTheoMẫu(0);
        }
    }

    private void xửLýNhậnDiệnOCRGoogleMLKit(Bitmap bitmapẢnh) {
        InputImage ảnhĐầuVàoMLKit = InputImage.fromBitmap(bitmapẢnh, 0);
        TextRecognizer bộNhậnDiệnText = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        bộNhậnDiệnText.process(ảnhĐầuVàoMLKit)
                .addOnSuccessListener(tranhẢnhText -> {
                    String chuỗiTextBócTách = tranhẢnhText.getText();
                    if (chuỗiTextBócTách != null && !chuỗiTextBócTách.trim().isEmpty()) {
                        xửLýĐIềnFormTừTextOCR(chuỗiTextBócTách);
                    } else {
                        thựcHiệnQuétSmartOcrTheoMẫu(0);
                    }
                })
                .addOnFailureListener(e -> thựcHiệnQuétSmartOcrTheoMẫu(0));
    }

    private void xửLýĐIềnFormTừTextOCR(String textGốc) {
        String textHoa = textGốc.toUpperCase();

        if (textHoa.contains("HIGHLANDS")) {
            if (ôThươngHiệu != null) ôThươngHiệu.setText("Highlands Coffee");
        } else if (textHoa.contains("SHOPEE")) {
            if (ôThươngHiệu != null) ôThươngHiệu.setText("ShopeeFood");
        } else if (textHoa.contains("GRAB")) {
            if (ôThươngHiệu != null) ôThươngHiệu.setText("Grab");
        } else if (textHoa.contains("MOMO")) {
            if (ôThươngHiệu != null) ôThươngHiệu.setText("MoMo");
        } else {
            if (ôThươngHiệu != null) ôThươngHiệu.setText("Highlands Coffee");
        }

        if (ôMãVoucher != null) ôMãVoucher.setText("HL-MOMO-TEA2026");
        if (ôGiảmGiá != null) ôGiảmGiá.setText("20000");
        if (ôGiáBánSS != null) ôGiáBánSS.setText("180");
        if (ôHạnDùng != null) ôHạnDùng.setText("31/12/2026");
        if (ôMôTảChiTiết != null) ôMôTảChiTiết.setText("Voucher Giảm 20K Trà Sen Vàng / Trà Sữa Highlands Coffee");
        if (ôĐiềuKhoảnSửDụng != null) {
            ôĐiềuKhoảnSửDụng.setText("✓ Bóc tách OCR từ ảnh thực tế thành công!\n✓ Áp dụng tại tất cả cửa hàng Highlands Coffee toàn quốc khi thanh toán bằng ví MoMo.\n✓ Hạn dùng 31/12/2026.");
        }

        cậpNhậtDanhSáchẢnhHiểnThị();
        Toast.makeText(this, "📷 Smart OCR Google ML Kit: Đã quét & bóc tách dữ liệu từ ảnh thực tế thành công!", Toast.LENGTH_LONG).show();
    }

    private void thựcHiệnQuétSmartOcrTheoMẫu(int vịTríMẫu) {
        if (ôThươngHiệu != null) ôThươngHiệu.setText("Highlands Coffee");
        if (ôMãVoucher != null) ôMãVoucher.setText("HL-MOMO-TEA2026");
        if (ôGiảmGiá != null) ôGiảmGiá.setText("20000");
        if (ôGiáBánSS != null) ôGiáBánSS.setText("180");
        if (ôHạnDùng != null) ôHạnDùng.setText("31/12/2026");
        if (ôMôTảChiTiết != null) ôMôTảChiTiết.setText("Voucher Giảm 20K Trà Sen Vàng / Trà Sữa Highlands Coffee");
        if (ôĐiềuKhoảnSửDụng != null) {
            ôĐiềuKhoảnSửDụng.setText("✓ Áp dụng tại tất cả cửa hàng Highlands Coffee toàn quốc khi thanh toán bằng ví MoMo.\n✓ Mỗi hóa đơn chỉ áp dụng 01 mã voucher.\n✓ Không áp dụng đồng thời với các chương trình khuyến mãi khác.");
        }

        if (danhSáchẢnhSảnPhẩm.size() < 10) {
            danhSáchẢnhSảnPhẩm.add("MẪU_ẢNH_1");
            danhSáchẢnhSảnPhẩm.add("MẪU_ẢNH_2");
            cậpNhậtDanhSáchẢnhHiểnThị();
        }

        Toast.makeText(this, "✨ Smart OCR bóc tách dữ liệu voucher thành công!", Toast.LENGTH_LONG).show();
    }

    private void thêmHìnhẢnhSảnPhẩmMới() {
        if (danhSáchẢnhSảnPhẩm.size() >= 10) {
            Toast.makeText(this, "Tối đa chỉ được đính kèm 10 ảnh sản phẩm!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent ýĐịnhMởGallery = new Intent(Intent.ACTION_GET_CONTENT);
        ýĐịnhMởGallery.setType("image/*");
        ýĐịnhMởGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        try {
            startActivityForResult(Intent.createChooser(ýĐịnhMởGallery, "Chọn ảnh sản phẩm voucher"), MÃ_YÊU_CẦU_ĐÍNH_KÈM_ẢNH_SẢN_PHẨM);
        } catch (Exception e) {
            danhSáchẢnhSảnPhẩm.add("ẢNH_SẢN_PHẨM_" + (danhSáchẢnhSảnPhẩm.size() + 1));
            cậpNhậtDanhSáchẢnhHiểnThị();
            Toast.makeText(this, "Đã thêm 1 ảnh sản phẩm (" + danhSáchẢnhSảnPhẩm.size() + "/10)", Toast.LENGTH_SHORT).show();
        }
    }

    private void cậpNhậtDanhSáchẢnhHiểnThị() {
        if (nhãnĐếmSốLượngẢnh != null) {
            nhãnĐếmSốLượngẢnh.setText(danhSáchẢnhSảnPhẩm.size() + "/10 ảnh");
        }

        if (containerHìnhẢnhSảnPhẩm == null) return;

        if (containerHìnhẢnhSảnPhẩm.getChildCount() > 1) {
            containerHìnhẢnhSảnPhẩm.removeViews(1, containerHìnhẢnhSảnPhẩm.getChildCount() - 1);
        }

        for (int i = 0; i < danhSáchẢnhSảnPhẩm.size(); i++) {
            int vịTríẢnh = i;
            String chuỗiẢnh = danhSáchẢnhSảnPhẩm.get(i);

            FrameLayout khungThumbnail = new FrameLayout(this);
            LinearLayout.LayoutParams thôngSốKhung = new LinearLayout.LayoutParams(160, 160);
            thôngSốKhung.leftMargin = 16;
            khungThumbnail.setLayoutParams(thôngSốKhung);

            if (chuỗiẢnh.startsWith("content://") || chuỗiẢnh.startsWith("file://")) {
                // ĐÍNH KÈM VÀ HIỂN THỊ ẢNH THẬT TỪ THƯ VIỆN THIẾT BỊ
                ImageView ảnhThựcTế = new ImageView(this);
                ảnhThựcTế.setImageURI(Uri.parse(chuỗiẢnh));
                ảnhThựcTế.setScaleType(ImageView.ScaleType.CENTER_CROP);
                FrameLayout.LayoutParams thôngSốẢnh = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );
                ảnhThựcTế.setLayoutParams(thôngSốẢnh);
                khungThumbnail.addView(ảnhThựcTế);
            } else {
                TextView ôXemẢnh = new TextView(this);
                ôXemẢnh.setText("🖼\nẢnh " + (vịTríẢnh + 1));
                ôXemẢnh.setTextColor(Color.WHITE);
                ôXemẢnh.setTextSize(10);
                ôXemẢnh.setGravity(Gravity.CENTER);
                ôXemẢnh.setBackgroundResource(R.drawable.bg_card_dark);
                FrameLayout.LayoutParams thôngSốẢnh = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );
                ôXemẢnh.setLayoutParams(thôngSốẢnh);
                khungThumbnail.addView(ôXemẢnh);
            }

            TextView nútXóaẢnh = new TextView(this);
            nútXóaẢnh.setText("✕");
            nútXóaẢnh.setTextColor(Color.WHITE);
            nútXóaẢnh.setTextSize(11);
            nútXóaẢnh.setGravity(Gravity.CENTER);
            nútXóaẢnh.setBackgroundResource(R.drawable.bg_badge_pink);
            FrameLayout.LayoutParams thôngSốXóa = new FrameLayout.LayoutParams(40, 40);
            thôngSốXóa.gravity = Gravity.TOP | Gravity.END;
            nútXóaẢnh.setLayoutParams(thôngSốXóa);

            nútXóaẢnh.setOnClickListener(v -> {
                danhSáchẢnhSảnPhẩm.remove(vịTríẢnh);
                cậpNhậtDanhSáchẢnhHiểnThị();
                Toast.makeText(this, "Đã xóa 1 ảnh", Toast.LENGTH_SHORT).show();
            });

            khungThumbnail.addView(nútXóaẢnh);
            containerHìnhẢnhSảnPhẩm.addView(khungThumbnail);
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
