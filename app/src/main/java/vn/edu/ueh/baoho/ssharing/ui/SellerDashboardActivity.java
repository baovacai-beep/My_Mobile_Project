package vn.edu.ueh.baoho.ssharing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.ueh.baoho.ssharing.R;

public class SellerDashboardActivity extends AppCompatActivity {

    private TextView tabDangBan, tabChoXacNhan, tabDaBan, tabHetHan;
    private TextView tvPageIndicator, tvTotalRevenue, tvSoDuKhaDung;
    private LinearLayout itemVoucherShopee, itemVoucherHighlands;
    private int countDangBan = 8;
    private int currentPage = 1;
    private final int totalPages = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        // Ánh xạ views
        tabDangBan = findViewById(R.id.tab_dang_ban);
        tabChoXacNhan = findViewById(R.id.tab_cho_xac_nhan);
        tabDaBan = findViewById(R.id.tab_da_ban);
        tabHetHan = findViewById(R.id.tab_het_han);

        itemVoucherShopee = findViewById(R.id.item_voucher_shopee);
        itemVoucherHighlands = findViewById(R.id.item_voucher_highlands);

        tvPageIndicator = findViewById(R.id.tv_page_indicator);
        tvTotalRevenue = findViewById(R.id.tv_total_revenue);
        tvSoDuKhaDung = findViewById(R.id.tv_seller_so_du_kha_dung);

        View btnBack = findViewById(R.id.btnBackShop);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // =========================================================================
        // 1. XỬ LÝ CHUYỂN TAB TRẠNG THÁI
        // =========================================================================
        if (tabDangBan != null) {
            tabDangBan.setOnClickListener(v -> selectTab(1));
        }
        if (tabChoXacNhan != null) {
            tabChoXacNhan.setOnClickListener(v -> selectTab(2));
        }
        if (tabDaBan != null) {
            tabDaBan.setOnClickListener(v -> selectTab(3));
        }
        if (tabHetHan != null) {
            tabHetHan.setOnClickListener(v -> selectTab(4));
        }

        // =========================================================================
        // 2. XỬ LÝ CHỨC NĂNG NÚT BẤM TRONG THẺ VOUCHER
        // =========================================================================

        // Nút Chỉnh sửa
        View btnEditVoucher1 = findViewById(R.id.btn_edit_voucher_1);
        if (btnEditVoucher1 != null) {
            btnEditVoucher1.setOnClickListener(v -> {
                Toast.makeText(this, "Mở form chỉnh sửa voucher...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, PostVoucherActivity.class);
                intent.putExtra("IS_EDIT", true);
                intent.putExtra("VOUCHER_TITLE", "Giảm 50K đơn từ 250K - Điện tử");
                startActivity(intent);
            });
        }

        // Nút Gỡ bài / Xóa
        View btnDeleteVoucher1 = findViewById(R.id.btn_delete_voucher_1);
        if (btnDeleteVoucher1 != null) {
            btnDeleteVoucher1.setOnClickListener(v -> showDeleteConfirmationDialog());
        }

        // Click vào thẻ để mở Escrow Chat
        if (itemVoucherShopee != null) {
            itemVoucherShopee.setOnClickListener(v -> {
                Toast.makeText(this, "Xem phòng giao dịch Ký quỹ Escrow...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, EscrowChatActivity.class));
            });
        }

        if (itemVoucherHighlands != null) {
            itemVoucherHighlands.setOnClickListener(v -> {
                Toast.makeText(this, "Xem chi tiết người mua chờ xác nhận...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, EscrowChatActivity.class));
            });
        }

        // =========================================================================
        // 3. XỬ LÝ PHÂN TRANG (PAGINATION)
        // =========================================================================
        View btnPrevPage = findViewById(R.id.btn_prev_page);
        View btnNextPage = findViewById(R.id.btn_next_page);

        if (btnPrevPage != null) {
            btnPrevPage.setOnClickListener(v -> {
                if (currentPage > 1) {
                    currentPage--;
                    updatePageDisplay();
                } else {
                    Toast.makeText(this, "Đang ở trang đầu tiên", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnNextPage != null) {
            btnNextPage.setOnClickListener(v -> {
                if (currentPage < totalPages) {
                    currentPage++;
                    updatePageDisplay();
                } else {
                    Toast.makeText(this, "Đang ở trang cuối cùng", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Chuyển đổi tab hiển thị danh sách voucher theo trạng thái
     */
    private void selectTab(int tabIndex) {
        resetTabStyles();

        if (tabIndex == 1) {
            // Đang bán
            if (tabDangBan != null) tabDangBan.setBackgroundResource(R.drawable.bg_tab_active);
            if (tabDangBan != null) tabDangBan.setTextColor(0xFFFFFFFF);
            if (itemVoucherShopee != null) itemVoucherShopee.setVisibility(View.VISIBLE);
            if (itemVoucherHighlands != null) itemVoucherHighlands.setVisibility(View.GONE);
            Toast.makeText(this, "Hiển thị danh sách Voucher đang bán", Toast.LENGTH_SHORT).show();
        } else if (tabIndex == 2) {
            // Chờ xác nhận
            if (tabChoXacNhan != null) tabChoXacNhan.setBackgroundResource(R.drawable.bg_tab_active);
            if (tabChoXacNhan != null) tabChoXacNhan.setTextColor(0xFFFFFFFF);
            if (itemVoucherShopee != null) itemVoucherShopee.setVisibility(View.GONE);
            if (itemVoucherHighlands != null) itemVoucherHighlands.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Hiển thị danh sách Voucher đang giữ xu Escrow chờ duyệt", Toast.LENGTH_SHORT).show();
        } else if (tabIndex == 3) {
            // Đã bán
            if (tabDaBan != null) tabDaBan.setBackgroundResource(R.drawable.bg_tab_active);
            if (tabDaBan != null) tabDaBan.setTextColor(0xFFFFFFFF);
            if (itemVoucherShopee != null) itemVoucherShopee.setVisibility(View.GONE);
            if (itemVoucherHighlands != null) itemVoucherHighlands.setVisibility(View.GONE);
            Toast.makeText(this, "Hiển thị 45 Voucher đã bán thành công!", Toast.LENGTH_SHORT).show();
        } else if (tabIndex == 4) {
            // Hết hạn
            if (tabHetHan != null) tabHetHan.setBackgroundResource(R.drawable.bg_tab_active);
            if (tabHetHan != null) tabHetHan.setTextColor(0xFFFFFFFF);
            if (itemVoucherShopee != null) itemVoucherShopee.setVisibility(View.GONE);
            if (itemVoucherHighlands != null) itemVoucherHighlands.setVisibility(View.GONE);
            Toast.makeText(this, "Hiển thị 1 Voucher đã hết hạn sử dụng", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetTabStyles() {
        int textGray = 0xFF9E9AA9;
        if (tabDangBan != null) {
            tabDangBan.setBackground(null);
            tabDangBan.setTextColor(textGray);
        }
        if (tabChoXacNhan != null) {
            tabChoXacNhan.setBackground(null);
            tabChoXacNhan.setTextColor(textGray);
        }
        if (tabDaBan != null) {
            tabDaBan.setBackground(null);
            tabDaBan.setTextColor(textGray);
        }
        if (tabHetHan != null) {
            tabHetHan.setBackground(null);
            tabHetHan.setTextColor(textGray);
        }
    }

    /**
     * Hộp thoại xác nhận gỡ bài đăng
     */
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận gỡ bài đăng")
                .setMessage("Bạn có chắc chắn muốn gỡ voucher 'Giảm 50K Shopee' khỏi sàn giao dịch?")
                .setPositiveButton("Gỡ bài", (dialog, which) -> {
                    if (itemVoucherShopee != null) {
                        itemVoucherShopee.setVisibility(View.GONE);
                    }
                    countDangBan = Math.max(0, countDangBan - 1);
                    if (tabDangBan != null) {
                        tabDangBan.setText("Đang bán (" + countDangBan + ")");
                    }
                    Toast.makeText(this, "Đã gỡ bài đăng voucher thành công!", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void updatePageDisplay() {
        if (tvPageIndicator != null) {
            tvPageIndicator.setText(currentPage + " / " + totalPages);
        }
        Toast.makeText(this, "Đang ở Trang " + currentPage, Toast.LENGTH_SHORT).show();
    }
}