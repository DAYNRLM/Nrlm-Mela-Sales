package com.nrlm.melasalesoffline.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.melasalesoffline.database.entity.ProductDescriptionEntity;

import java.util.List;

@Dao
public interface ProductDecsDao {

    @Insert
    void insertAll(ProductDescriptionEntity users);

    @Delete
    void delete(ProductDescriptionEntity user);

    @Query("DELETE  from productDescriptionData")
    void deleteAll();

    @Query("SELECT * from productDescriptionData")
    List<ProductDescriptionEntity> getAllShgDescData();


    @Query("SELECT * FROM productDescriptionData  where  product_id = :productId")
    List<ProductDescriptionEntity> getAllProductIdDesc(String productId);

    @Query("SELECT * FROM productDescriptionData where product_id = :productId and product_desc_id=:productDecId")
    ProductDescriptionEntity getSpecificProductSpec(String productId,String productDecId);

}
