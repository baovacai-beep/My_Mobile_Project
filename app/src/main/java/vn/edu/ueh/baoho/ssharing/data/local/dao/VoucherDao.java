package vn.edu.ueh.baoho.ssharing.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import vn.edu.ueh.baoho.ssharing.data.local.entity.VoucherEntity;

@Dao
public interface VoucherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVoucher(VoucherEntity voucher);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VoucherEntity> vouchers);

    @Update
    void updateVoucher(VoucherEntity voucher);

    @Delete
    void deleteVoucher(VoucherEntity voucher);

    @Query("SELECT * FROM vouchers ORDER BY id DESC")
    LiveData<List<VoucherEntity>> getAllVouchersLiveData();

    @Query("SELECT * FROM vouchers WHERE phanLoai = :loai ORDER BY id DESC")
    LiveData<List<VoucherEntity>> getVouchersByCategory(String loai);

    @Query("SELECT * FROM vouchers WHERE id = :id LIMIT 1")
    VoucherEntity getVoucherById(String id);
}