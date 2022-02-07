package com.nrlm.melasalesoffline.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nrlm.melasalesoffline.database.entity.BillSyncEntity;

import java.util.List;

@Dao
public interface BillSyncDao {

    @Insert
    void insertAll(BillSyncEntity billSync);

    @Delete
    void delete(BillSyncEntity billSync);

    @Query("DELETE  from billSyncData")
    void deleteAll();

    @Query("SELECT * from billSyncData")
    List<BillSyncEntity> getAllBillData();

    @Query("SELECT * from billSyncData WHERE status_flag =:flag")
    List<BillSyncEntity> getAllFlagBillData(String flag);

    @Query("UPDATE billSyncData SET status_flag =:flag WHERE invoice_number =:invoice")
    void updateFlageStatus(String flag,String invoice);




}
