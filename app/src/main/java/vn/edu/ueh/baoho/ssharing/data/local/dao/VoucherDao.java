package vn.edu.ueh.baoho.ssharing.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import vn.edu.ueh.baoho.ssharing.data.local.entity.VoucherEntity;

@Dao
public interface VoucherDao { //

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void chenDanhSachVoucher(List<VoucherEntity> danhSachVoucher); //

    @Query("SELECT * FROM voucher_entity WHERE trang_thai = 'SAN_SANG' ORDER BY ngay_het_han ASC")
    LiveData<List<VoucherEntity>> layTatCaVoucherKhaDung(); //[cite: 8]

    @Query("SELECT * FROM voucher_entity WHERE la_coupon_cong_dong = 1 AND trang_thai = 'SAN_SANG'")
    LiveData<List<VoucherEntity>> layVoucherCongDong0Xu(); //[cite: 8]

    @Query("SELECT * FROM voucher_entity WHERE danh_muc = :tenDanhMuc AND trang_thai = 'SAN_SANG'")
    LiveData<List<VoucherEntity>> layVoucherTheoDanhMuc(String tenDanhMuc); //[cite: 8]

    @Query("DELETE FROM voucher_entity")
    void xoaToanBoCache(); //[cite: 8]
}