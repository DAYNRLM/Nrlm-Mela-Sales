package com.nrlm.melasalesoffline.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "productDescriptionData")
public class ProductDescriptionEntity {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "product_id")
    public String product_id;

    @ColumnInfo(name = "unit")
    public String unit;

    @ColumnInfo(name = "product_type")
    public String product_type;

    @ColumnInfo(name = "product_desc_id")
    public String product_desc_id;

    @ColumnInfo(name = "unit_price")
    public String unit_price;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getProduct_desc_id() {
        return product_desc_id;
    }

    public void setProduct_desc_id(String product_desc_id) {
        this.product_desc_id = product_desc_id;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }
}
