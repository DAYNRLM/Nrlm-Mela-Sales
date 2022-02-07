package com.nrlm.melasalesoffline.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "productDetail")
public class ProductEntity {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "village_code")
    public String village_code;

    @ColumnInfo(name = "shg_code")
    public String shg_code;

    @ColumnInfo(name = "stall_no")
    public String stall_no;

    @ColumnInfo(name = "state_short_name")
    public String state_short_name;

    @ColumnInfo(name = "shg_reg_id")
    public String shg_reg_id;


    @ColumnInfo(name = "product_name")
    public String product_name;

    @ColumnInfo(name = "subcategory_name")
    public String subcategory_name;

    @ColumnInfo(name = "category_name")
    public String category_name;

    @ColumnInfo(name = "product_id")
    public String product_id;

    @ColumnInfo(name = "category_id")
    public String category_id;

    @ColumnInfo(name = "subcategory_id")
    public String subcategory_id;

    @ColumnInfo(name = "available_quantity")
    public String available_quantity;

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

    public String getShg_reg_id() {
        return shg_reg_id;
    }

    public void setShg_reg_id(String shg_reg_id) {
        this.shg_reg_id = shg_reg_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(String subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public String getAvailable_quantity() {
        return available_quantity;
    }

    public void setAvailable_quantity(String available_quantity) {
        this.available_quantity = available_quantity;
    }
}
