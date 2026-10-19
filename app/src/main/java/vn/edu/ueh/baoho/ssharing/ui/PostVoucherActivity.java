package vn.edu.ueh.baoho.ssharing.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import vn.edu.ueh.baoho.ssharing.R;
import vn.edu.ueh.baoho.ssharing.model.VoucherC2C;
import vn.edu.ueh.baoho.ssharing.model.VoucherCongDong;
import vn.edu.ueh.baoho.ssharing.viewmodel.VoucherViewModel;

public class PostVoucherActivity extends AppCompatActivity {

    private EditText etBrand, etCode, etDiscount, etExpiry, etSellingPrice, etDescription;
    private RadioGroup rgSellType;
    private RadioButton rbDonateCommunity;
    private VoucherViewModel voucherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_voucher);

        voucherViewModel = new ViewModelProvider(this).get(VoucherViewModel.class);

        etBrand = findViewById(R.id.etBrand);
        etCode = findViewById(R.id.etCode);
        etDiscount = findViewById(R.id.etDiscount);
        etExpiry = findViewById(R.id.etExpiry);
        etSellingPrice = findViewById(R.id.etSellingPrice);
        etDescription = findViewById(R.id.etDescription);
        rgSellType = findViewById(R.id.rg_sell_type);
        rbDonateCommunity = findViewById(R.id.rb_donate_community);

        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        // Tự động điều chỉnh khi chọn Tặng 0 Xu
        if (rgSellType != null) {
            rgSellType.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rb_donate_community) {
                    if (etSellingPrice != null) {
                        etSellingPrice.setText("0");
                        etSellingPrice.setEnabled(false);
                    }
                } else {
                    if (etSellingPrice != null) {
                        etSellingPrice.setEnabled(true);
                    }
                }
            });
        }

        if (findViewById(R.id.btnSubmit) != null) {
            findViewById(R.id.btnSubmit).setOnClickListener(v -> {
                String brand = etBrand != null ? etBrand.getText().toString().trim() : "";
                String code = etCode != null ? etCode.getText().toString().trim() : "";
                String discountStr = etDiscount != null ? etDiscount.getText().toString().trim() : "0";
                String priceStr = etSellingPrice != null ? etSellingPrice.getText().toString().trim() : "0";
                String expiry = etExpiry != null ? etExpiry.getText().toString().trim() : "31/12/2026";
                String title = etDescription != null && !etDescription.getText().toString().trim().isEmpty()
                        ? etDescription.getText().toString().trim()
                        : "Voucher " + (brand.isEmpty() ? "Ưu Đãi" : brand);

                if (code.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập mã voucher!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double discount = 0;
                double price = 0;
                try {
                    discount = Double.parseDouble(discountStr);
                    price = Double.parseDouble(priceStr);
                } catch (Exception ignored) {}

                String newId = "VCH_" + System.currentTimeMillis();

                if (rbDonateCommunity != null && rbDonateCommunity.isChecked()) {
                    VoucherCongDong vCongDong = new VoucherCongDong(
                            newId, title, brand.isEmpty() ? "Cộng Đồng" : brand,
                            discount, expiry, "CURRENT_USER", code
                    );
                    voucherViewModel.themVoucher(vCongDong);
                    Toast.makeText(this, "Đã đăng tặng Voucher 0đ vào Trạm Cộng Đồng thành công!", Toast.LENGTH_LONG).show();
                } else {
                    VoucherC2C vC2C = new VoucherC2C(
                            newId, title, brand.isEmpty() ? "Thương Hiệu" : brand,
                            discount, price, expiry, "CURRENT_USER", code
                    );
                    voucherViewModel.themVoucher(vC2C);
                    Toast.makeText(this, "Đăng bán Voucher lấy Xu thành công! Mã đã được lưu vào Room DB.", Toast.LENGTH_LONG).show();
                }

                finish();
            });
        }
    }
}