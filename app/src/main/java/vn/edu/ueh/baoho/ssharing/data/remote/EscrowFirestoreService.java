package vn.edu.ueh.baoho.ssharing.data.remote;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

public class EscrowFirestoreService {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Thực hiện giao dịch nguyên tử (Firestore Transaction) để giữ xu ký quỹ của người mua
     */
    public void giuTienKyQuyEscrow(String maDonHang, String idNguoiMua, String idNguoiBan, long soXuThanhToan, EscrowCallback callback) {
        DocumentReference refViMua = db.collection("wallets").document(idNguoiMua);
        DocumentReference refViBan = db.collection("wallets").document(idNguoiBan);
        DocumentReference refDonHang = db.collection("escrow_orders").document(maDonHang);

        db.runTransaction((Transaction.Function<Void>) transaction -> {
                    // 1. Kiểm tra số dư ví người mua
                    long soDuKhaDungMua = transaction.get(refViMua).getLong("soDuXuKhaDung");
                    if (soDuKhaDungMua < soXuThanhToan) {
                        throw new RuntimeException("Số dư xu khả dụng không đủ để ký quỹ giao dịch đảm bảo!");
                    }

                    // 2. Trừ ví khả dụng người mua, cộng dồn vào đóng băng Escrow
                    transaction.update(refViMua, "soDuXuKhaDung", soDuKhaDungMua - soXuThanhToan);
                    long soDuEscrowHienTai = transaction.get(refViMua).getLong("soDuXuDongBangEscrow");
                    transaction.update(refViMua, "soDuXuDongBangEscrow", soDuEscrowHienTai + soXuThanhToan);

                    // 3. Tạo bản ghi đơn hàng Escrow kèm mốc thời gian đếm ngược 60 phút
                    long thoiGianHetHan = System.currentTimeMillis() + (60 * 60 * 1000);
                    java.util.Map<String, Object> orderData = new java.util.HashMap<>();
                    orderData.put("maDonHang", maDonHang);
                    orderData.put("idNguoiMua", idNguoiMua);
                    orderData.put("idNguoiBan", idNguoiBan);
                    orderData.put("soXu", soXuThanhToan);
                    orderData.put("trangThai", "DANG_GIU_QUY_ESCROW");
                    orderData.put("thoiGianHetHan", thoiGianHetHan);
                    transaction.set(refDonHang, orderData);

                    return null;
                }).addOnSuccessListener(aVoid -> callback.onSuccess("Đã khóa xu ký quỹ an toàn qua Escrow Engine!"))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public interface EscrowCallback {
        void onSuccess(String message);
        void onError(String error);
    }
}