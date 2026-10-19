package vn.edu.ueh.baoho.ssharing.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import vn.edu.ueh.baoho.ssharing.data.local.entity.NguoiDungEntity;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(NguoiDungEntity user);

    @Update
    void updateUser(NguoiDungEntity user);

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    LiveData<NguoiDungEntity> getUserById(String userId);

    @Query("SELECT * FROM users WHERE soDienThoai = :sdt LIMIT 1")
    NguoiDungEntity getUserByPhone(String sdt);
}