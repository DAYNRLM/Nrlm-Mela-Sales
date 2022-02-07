package com.nrlm.melasalesoffline.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.melasalesoffline.database.entity.ProductEntity;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insertAll(ProductEntity users);

    @Delete
    void delete(ProductEntity user);

    @Query("DELETE  from productDetail")
    void deleteAll();

    @Query("SELECT * from productDetail")
    List<ProductEntity> getAllShgData();

    @Query("SELECT * from productDetail WHERE shg_reg_id =:shgRegId")
    List<ProductEntity> getShgData(String shgRegId);

    @Query("UPDATE productDetail SET available_quantity =:difference WHERE product_id =:pId ")
    void updateQuantity(String pId,String difference);

    @Query("SELECT * FROM productDetail  WHERE product_id =:pId ")
    ProductEntity getQuantity(String pId);



}
