package com.nrlm.melasalesoffline.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "billSyncData")
public class BillSyncEntity {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "invoice_number")
    public String invoice_number;

    @ColumnInfo(name = "bill_generated_date")
    public String bill_generated_date;

    @ColumnInfo(name = "bill_generated_time")
    public String bill_generated_time;

    @ColumnInfo(name = "bill_created_date")
    public String bill_created_date;

    @ColumnInfo(name = "shg_reg_id")
    public String shg_reg_id;

    @ColumnInfo(name = "shg_code")
    public String shg_code;

    @ColumnInfo(name = "product_Qty")
    public String product_Qty;

    @ColumnInfo(name = "product_Id")
    public String product_Id;

    @ColumnInfo(name = "mela_Id")
    public String mela_Id;

    @ColumnInfo(name = "unit_Price")
    public String unit_Price;

    @ColumnInfo(name = "status_flag")
    public String status_flag;

    @ColumnInfo(name = "product_desc_Id")
    public String product_desc_Id;

    @ColumnInfo(name = "login_mobile_no")
    public String login_mobile_no;

    @ColumnInfo(name = "unique_bill_number")
    public String unique_bill_number;

    @ColumnInfo(name = "imei_number")
    public String imei_number;

    @ColumnInfo(name = "device_info")
    public String device_info;


    @ColumnInfo(name = "total_amount")
    public int total_amount;

    @ColumnInfo(name = "generated_bill_no")
    public int generated_bill_no;


    public String getImei_number() {
        return imei_number;
    }

    public void setImei_number(String imei_number) {
        this.imei_number = imei_number;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getBill_generated_date() {
        return bill_generated_date;
    }

    public void setBill_generated_date(String bill_generated_date) {
        this.bill_generated_date = bill_generated_date;
    }

    public String getBill_generated_time() {
        return bill_generated_time;
    }

    public void setBill_generated_time(String bill_generated_time) {
        this.bill_generated_time = bill_generated_time;
    }

    public String getBill_created_date() {
        return bill_created_date;
    }

    public void setBill_created_date(String bill_created_date) {
        this.bill_created_date = bill_created_date;
    }

    public String getShg_reg_id() {
        return shg_reg_id;
    }

    public void setShg_reg_id(String shg_reg_id) {
        this.shg_reg_id = shg_reg_id;
    }

    public String getShg_code() {
        return shg_code;
    }

    public void setShg_code(String shg_code) {
        this.shg_code = shg_code;
    }

    public String getProduct_Qty() {
        return product_Qty;
    }

    public void setProduct_Qty(String product_Qty) {
        this.product_Qty = product_Qty;
    }

    public String getProduct_Id() {
        return product_Id;
    }

    public void setProduct_Id(String product_Id) {
        this.product_Id = product_Id;
    }

    public String getMela_Id() {
        return mela_Id;
    }

    public void setMela_Id(String mela_Id) {
        this.mela_Id = mela_Id;
    }

    public String getUnit_Price() {
        return unit_Price;
    }

    public void setUnit_Price(String unit_Price) {
        this.unit_Price = unit_Price;
    }

    public String getStatus_flag() {
        return status_flag;
    }

    public void setStatus_flag(String status_flag) {
        this.status_flag = status_flag;
    }

    public String getProduct_desc_Id() {
        return product_desc_Id;
    }

    public void setProduct_desc_Id(String product_desc_Id) {
        this.product_desc_Id = product_desc_Id;
    }

    public String getLogin_mobile_no() {
        return login_mobile_no;
    }

    public void setLogin_mobile_no(String login_mobile_no) {
        this.login_mobile_no = login_mobile_no;
    }

    public String getUnique_bill_number() {
        return unique_bill_number;
    }

    public void setUnique_bill_number(String unique_bill_number) {
        this.unique_bill_number = unique_bill_number;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public int getGenerated_bill_no() {
        return generated_bill_no;
    }

    public void setGenerated_bill_no(int generated_bill_no) {
        this.generated_bill_no = generated_bill_no;
    }
}
