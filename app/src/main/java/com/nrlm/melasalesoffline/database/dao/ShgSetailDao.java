package com.nrlm.melasalesoffline.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.melasalesoffline.database.entity.ShgDetailEntity;

import java.util.List;

@Dao
public interface ShgSetailDao {
    @Insert
    void insertAll(ShgDetailEntity users);

    @Delete
    void delete(ShgDetailEntity user);

    @Query("DELETE  from ShgAssign")
    void deleteAll();


    @Query("SELECT * from ShgAssign")
    List<ShgDetailEntity> getAllShgData();

    @Query("SELECT * from ShgAssign WHERE shg_reg_id =:shgRegId ")
    ShgDetailEntity getShgName(String shgRegId);

}
