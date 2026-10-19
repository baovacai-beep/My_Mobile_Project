package vn.edu.ueh.baoho.ssharing.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import vn.edu.ueh.baoho.ssharing.data.local.dao.UserDao;
import vn.edu.ueh.baoho.ssharing.data.local.dao.VoucherDao;
import vn.edu.ueh.baoho.ssharing.data.local.entity.NguoiDungEntity;
import vn.edu.ueh.baoho.ssharing.data.local.entity.ViTienEntity;
import vn.edu.ueh.baoho.ssharing.data.local.entity.VoucherEntity;

@Database(entities = {VoucherEntity.class, NguoiDungEntity.class, ViTienEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract VoucherDao voucherDao();
    public abstract UserDao userDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "ssharing_local_db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}