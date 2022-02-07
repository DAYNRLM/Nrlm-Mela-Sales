package com.nrlm.melasalesoffline.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.melasalesoffline.database.entity.ProductAddTemptableEntity;

import java.util.List;

@Dao
public interface ProductAddTempDao {

    @Insert
    void insertAll(ProductAddTemptableEntity users);

    @Delete
    void delete(ProductAddTemptableEntity user);

    @Query("DELETE  from productAddTemp")
    void deleteAll();

    @Query("SELECT * from productAddTemp")
    List<ProductAddTemptableEntity> getAllTempData();

    @Query("DELETE  from productAddTemp WHERE u_id =:uid")
    void deleteBasedOnUid(int uid);
}
