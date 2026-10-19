package vn.edu.ueh.baoho.ssharing.repository;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.edu.ueh.baoho.ssharing.data.local.AppDatabase;
import vn.edu.ueh.baoho.ssharing.data.local.dao.VoucherDao;
import vn.edu.ueh.baoho.ssharing.data.local.entity.VoucherEntity;
import vn.edu.ueh.baoho.ssharing.model.Voucher;

/**
 * Tầng Repository đóng vai trò SINGLE SOURCE OF TRUTH (Chân lý duy nhất)
 * Thực hiện Offline Synchronization:
 * - Khi có mạng: Tải từ Firestore -> Ghi đè Cache vào Room SQLite.
 * - Khi ngắt mạng (Offline): Tự động truy vấn hiển thị từ Room SQLite mà không báo lỗi.
 */
public class VoucherRepository {
    private final VoucherDao voucherDao;
    private final LiveData<List<VoucherEntity>> allVouchers;
    private final ExecutorService executorService;
    private final FirebaseFirestore firestore;

    public VoucherRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.voucherDao = db.voucherDao();
        this.allVouchers = voucherDao.getAllVouchersLiveData();
        this.executorService = Executors.newFixedThreadPool(2);
        this.firestore = FirebaseFirestore.getInstance();

        // Tự động kích hoạt đồng bộ ngoại tuyến từ đám mây khi khởi tạo
        refreshVouchersFromFirestore();
    }

    /**
     * Tầng UI chỉ lắng nghe LiveData trực tiếp từ Room SQLite (Single Source of Truth)
     */
    public LiveData<List<VoucherEntity>> getAllVouchers() {
        return allVouchers;
    }

    public LiveData<List<VoucherEntity>> getVouchersByCategory(String category) {
        return voucherDao.getVouchersByCategory(category);
    }

    /**
     * Đồng bộ ngoại tuyến: Tải dữ liệu từ Firebase Firestore và ghi đè đệm (cache) vào Room
     */
    public void refreshVouchersFromFirestore() {
        firestore.collection("voucher")
                .get()
                .addOnSuccessListener(querySnapshot -> executorService.execute(() -> {
                    List<VoucherEntity> entities = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String id = doc.getId();
                        String tieuDe = doc.getString("tieu_de_voucher");
                        String thuongHieu = doc.getString("ten_thuong_hieu");
                        Double giaGoc = doc.getDouble("gia_tri_giam_vnd");
                        Double giaXu = doc.getDouble("gia_ban_xu");
                        String hanDung = doc.getString("ngay_het_han");
                        String idDang = doc.getString("ma_nguoi_dang");
                        String maCode = doc.getString("ma_code_bi_mat");
                        String phanLoai = doc.getString("danh_muc");
                        Boolean daBan = doc.getBoolean("la_coupon_cong_dong");

                        if (tieuDe != null) {
                            entities.add(new VoucherEntity(
                                    id,
                                    tieuDe,
                                    thuongHieu != null ? thuongHieu : "SSharing",
                                    giaGoc != null ? giaGoc : 0.0,
                                    giaXu != null ? giaXu : 0.0,
                                    hanDung != null ? hanDung : "31/12/2026",
                                    idDang != null ? idDang : "ADMIN",
                                    maCode != null ? maCode : "SECRET",
                                    phanLoai != null ? phanLoai : "C2C",
                                    daBan != null && daBan
                            ));
                        }
                    }
                    if (!entities.isEmpty()) {
                        voucherDao.insertAll(entities);
                        Log.d("VoucherRepository", "Đã đồng bộ " + entities.size() + " voucher từ Firestore vào Room SQLite Cache");
                    }
                }))
                .addOnFailureListener(e -> Log.w("VoucherRepository", "Ngắt kết nối mạng (Offline Mode) - Đang truy vấn trực tiếp từ Room SQLite Cache local", e));
    }

    public void insert(VoucherEntity voucher) {
        executorService.execute(() -> voucherDao.insertVoucher(voucher));
    }

    public void insertDomainVoucher(Voucher voucher) {
        VoucherEntity entity = new VoucherEntity(
                voucher.getId(),
                voucher.getTieuDe(),
                voucher.getTenThuongHieu(),
                voucher.getGiaGoc(),
                voucher.getGiaBanXu(),
                voucher.getHanSuDung(),
                voucher.getIdNguoiDang(),
                voucher.xemMaCodeDaMoKhoa(true),
                voucher.layPhanLoaiVoucher(),
                voucher.isDaBan()
        );
        insert(entity);
    }
}