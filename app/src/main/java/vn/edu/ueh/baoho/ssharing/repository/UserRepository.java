package vn.edu.ueh.baoho.ssharing.repository;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.edu.ueh.baoho.ssharing.data.local.AppDatabase;
import vn.edu.ueh.baoho.ssharing.data.local.dao.UserDao;
import vn.edu.ueh.baoho.ssharing.data.local.entity.NguoiDungEntity;

/**
 * UserRepository triển khai Single Source of Truth cho đối tượng Người Dùng
 */
public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;
    private final FirebaseFirestore firestore;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.userDao = db.userDao();
        this.executorService = Executors.newFixedThreadPool(2);
        this.firestore = FirebaseFirestore.getInstance();
    }

    public LiveData<NguoiDungEntity> getUserById(String userId) {
        // Tự động thử đồng bộ từ Cloud Firestore khi đọc người dùng
        syncUserFromFirestore(userId);
        return userDao.getUserById(userId);
    }

    public void syncUserFromFirestore(String userId) {
        if (userId == null || userId.isEmpty()) return;
        firestore.collection("nguoi_dung")
                .document(userId)
                .get()
                .addOnSuccessListener((DocumentSnapshot doc) -> {
                    if (doc.exists()) {
                        String name = doc.getString("ho_ten");
                        String email = doc.getString("email");
                        String phone = doc.getString("so_dien_thoai");
                        Long trust = doc.getLong("diem_tin_nhiem");
                        String role = doc.getString("vai_tro");
                        Boolean ekyc = doc.getBoolean("da_xac_thuc_ekyc");

                        NguoiDungEntity entity = new NguoiDungEntity(
                                userId,
                                name != null ? name : "Người dùng SSharing",
                                email != null ? email : "",
                                phone != null ? phone : "",
                                trust != null ? trust.intValue() : 95,
                                role != null ? role : "VERIFIED",
                                ekyc != null && ekyc
                        );
                        executorService.execute(() -> userDao.insertUser(entity));
                    }
                })
                .addOnFailureListener(e -> Log.w("UserRepository", "Offline Mode - Sử dụng dữ liệu hồ sơ đệm từ Room SQLite Cache", e));
    }

    public void insertUser(NguoiDungEntity user) {
        executorService.execute(() -> userDao.insertUser(user));
    }
}