package com.nrlm.melasalesoffline.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.melasalesoffline.database.entity.MainHelperEntity;

import java.util.List;

@Dao
public interface MainHelperDao {
    @Insert
    void insertAll(MainHelperEntity users);

    @Delete
    void delete(MainHelperEntity user);

    @Query("DELETE  from mailHelper")
    void deleteAll();

    @Query("SELECT * from mailHelper")
    MainHelperEntity getHelperData();


}
