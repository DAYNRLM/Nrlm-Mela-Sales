package com.nrlm.melasalesoffline.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.nrlm.melasalesoffline.database.dao.BillSyncDao;
import com.nrlm.melasalesoffline.database.dao.MainHelperDao;
import com.nrlm.melasalesoffline.database.dao.ProductAddTempDao;
import com.nrlm.melasalesoffline.database.dao.ProductDao;
import com.nrlm.melasalesoffline.database.dao.ProductDecsDao;
import com.nrlm.melasalesoffline.database.dao.ShgSetailDao;
import com.nrlm.melasalesoffline.database.entity.BillSyncEntity;
import com.nrlm.melasalesoffline.database.entity.MainHelperEntity;
import com.nrlm.melasalesoffline.database.entity.ProductAddTemptableEntity;
import com.nrlm.melasalesoffline.database.entity.ProductDescriptionEntity;
import com.nrlm.melasalesoffline.database.entity.ProductEntity;
import com.nrlm.melasalesoffline.database.entity.ShgDetailEntity;

@Database(entities = {ShgDetailEntity.class, MainHelperEntity.class,
        ProductDescriptionEntity.class, ProductEntity.class, ProductAddTemptableEntity.class, BillSyncEntity.class}
        , version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract ShgSetailDao shgDetailDao();
    public abstract MainHelperDao mainHelperDao();
    public abstract ProductDao productDao();
    public abstract ProductDecsDao productDescriptionDao();
    public abstract ProductAddTempDao productTempDao();
    public abstract BillSyncDao billSyncDao();

    public static volatile AppDataBase DBINSTANCE;

    public static AppDataBase getDatabase(final Context context){
        if (DBINSTANCE==null){
            synchronized (AppDataBase.class){
                if (DBINSTANCE==null){
                    DBINSTANCE= Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,"mela_sales").allowMainThreadQueries().build();
                }
            }
        }
        return DBINSTANCE;
    }

   /* public static volatile AppDataBase DBINSTANCE;
    private static final int NUMBER_OF_THREADS = 5;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDataBase getDatabase(final Context context) {
        if (DBINSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (DBINSTANCE == null) {
                    DBINSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "shg_transactions").allowMainThreadQueries().build();
                }
            }
        }
        return DBINSTANCE;
    }*/
}
