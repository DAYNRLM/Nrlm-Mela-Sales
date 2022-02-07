package com.nrlm.melasalesoffline.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "productAddTemp")
public class ProductAddTemptableEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "u_id")
    public int uid;

    public int getUid() {
        return uid;
    }

    @ColumnInfo(name = "village_code")
    public String village_code;

    @ColumnInfo(name = "shg_code")
    public String shg_code;

    @ColumnInfo(name = "shg_reg_id")
    public String shg_reg_id;

    @ColumnInfo(name = "stall_no")
    public String stall_no;

    @ColumnInfo(name = "state_short_name")
    public String state_short_name;

    @ColumnInfo(name = "product_id")
    public String product_id;

    @ColumnInfo(name = "product_name")
    public String product_name;

    @ColumnInfo(name = "product_desc_id")
    public String product_desc_id;

    @ColumnInfo(name = "product_desc_Name")
    public String product_desc_Name;

    @ColumnInfo(name = "product_quantity")
    public int product_quantity;

    @ColumnInfo(name = "product_unit_price")
    public int product_unit_price;

    @ColumnInfo(name = "product_total_price")
    public int product_total_price;

    public int getProduct_total_price() {
        return product_total_price;
    }

    public void setProduct_total_price(int product_total_price) {
        this.product_total_price = product_total_price;
    }

    public String getVillage_code() {
        return village_code;
    }

    public void setVillage_code(String village_code) {
        this.village_code = village_code;
    }

    public String getShg_code() {
        return shg_code;
    }

    public void setShg_code(String shg_code) {
        this.shg_code = shg_code;
    }

    public String getShg_reg_id() {
        return shg_reg_id;
    }

    public void setShg_reg_id(String shg_reg_id) {
        this.shg_reg_id = shg_reg_id;
    }

    public String getStall_no() {
        return stall_no;
    }

    public void setStall_no(String stall_no) {
        this.stall_no = stall_no;
    }

    public String getState_short_name() {
        return state_short_name;
    }

    public void setState_short_name(String state_short_name) {
        this.state_short_name = state_short_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_desc_id() {
        return product_desc_id;
    }

    public void setProduct_desc_id(String product_desc_id) {
        this.product_desc_id = product_desc_id;
    }

    public String getProduct_desc_Name() {
        return product_desc_Name;
    }

    public void setProduct_desc_Name(String product_desc_Name) {
        this.product_desc_Name = product_desc_Name;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public int getProduct_unit_price() {
        return product_unit_price;
    }

    public void setProduct_unit_price(int product_unit_price) {
        this.product_unit_price = product_unit_price;
    }
}
