package vn.edu.ueh.baoho.ssharing.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import vn.edu.ueh.baoho.ssharing.data.local.dao.VoucherDao;
import vn.edu.ueh.baoho.ssharing.data.local.entity.NguoiDungEntity;
import vn.edu.ueh.baoho.ssharing.data.local.entity.ViTienEntity;
import vn.edu.ueh.baoho.ssharing.data.local.entity.VoucherEntity;

@Database(entities = {NguoiDungEntity.class, ViTienEntity.class, VoucherEntity.class}, version = 1, exportSchema = false) //[cite: 8]
public abstract class AppDatabase extends RoomDatabase { //[cite: 8]

    private static volatile AppDatabase INSTANCE; //[cite: 8]

    public abstract VoucherDao voucherDao(); //[cite: 8]

    public static AppDatabase getDatabase(final Context context) { //[cite: 8]
        if (INSTANCE == null) { //[cite: 8]
            synchronized (AppDatabase.class) { //[cite: 8]
                if (INSTANCE == null) { //[cite: 8]
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), //[cite: 8]
                                    AppDatabase.class, "ssharing_local_db") //[cite: 8]
                            .fallbackToDestructiveMigration() //[cite: 8]
                            .build(); //[cite: 8]
                }
            }
        }
        return INSTANCE; //[cite: 8]
    }
}