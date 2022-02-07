package com.nrlm.melasalesoffline.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ShgAssign")
public class ShgDetailEntity {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "village_code")
    public String village_code;

    @ColumnInfo(name = "shg_code")
    public String shg_code;

    @ColumnInfo(name = "stall_no")
    public String stall_no;

    @ColumnInfo(name = "group_name")
    public String group_name;

    @ColumnInfo(name = "shg_reg_id")
    public String shg_reg_id;

    @ColumnInfo(name = "mela_from")
    public String mela_from;

    @ColumnInfo(name = "mela_to")
    public String mela_to;

    @ColumnInfo(name = "state_short_name")
    public String state_short_name;

    public String getMela_to() {
        return mela_to;
    }

    public void setMela_to(String mela_to) {
        this.mela_to = mela_to;
    }

    public String getMela_from() {
        return mela_from;
    }

    public void setMela_from(String mela_from) {
        this.mela_from = mela_from;
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

    public String getStall_no() {
        return stall_no;
    }

    public void setStall_no(String stall_no) {
        this.stall_no = stall_no;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getShg_reg_id() {
        return shg_reg_id;
    }

    public void setShg_reg_id(String shg_reg_id) {
        this.shg_reg_id = shg_reg_id;
    }

    public String getState_short_name() {
        return state_short_name;
    }

    public void setState_short_name(String state_short_name) {
        this.state_short_name = state_short_name;
    }
}
