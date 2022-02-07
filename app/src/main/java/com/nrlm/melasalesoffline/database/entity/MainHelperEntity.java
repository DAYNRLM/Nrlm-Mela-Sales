package com.nrlm.melasalesoffline.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mailHelper")
public class MainHelperEntity {
    @PrimaryKey(autoGenerate = true)
    public int uid;

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

    @ColumnInfo(name = "main_participant_mobile")
    public String main_participant_mobile;

    @ColumnInfo(name = "main_code")
    public String main_code;

    @ColumnInfo(name = "main_name")
    public String main_name;

    @ColumnInfo(name = "invoice_no")
    public String invoice_no;

    @ColumnInfo(name = "ass_mobile")
    public String ass_mobile;

    @ColumnInfo(name = "ass_name")
    public String ass_name;

    public String getStall_no() {
        return stall_no;
    }

    public void setStall_no(String stall_no) {
        this.stall_no = stall_no;
    }


    public String getShg_reg_id() {
        return shg_reg_id;
    }

    public void setShg_reg_id(String shg_reg_id) {
        this.shg_reg_id = shg_reg_id;
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

    public String getState_short_name() {
        return state_short_name;
    }

    public void setState_short_name(String state_short_name) {
        this.state_short_name = state_short_name;
    }

    public String getMain_participant_mobile() {
        return main_participant_mobile;
    }

    public void setMain_participant_mobile(String main_participant_mobile) {
        this.main_participant_mobile = main_participant_mobile;
    }

    public String getMain_code() {
        return main_code;
    }

    public void setMain_code(String main_code) {
        this.main_code = main_code;
    }

    public String getMain_name() {
        return main_name;
    }

    public void setMain_name(String main_name) {
        this.main_name = main_name;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getAss_mobile() {
        return ass_mobile;
    }

    public void setAss_mobile(String ass_mobile) {
        this.ass_mobile = ass_mobile;
    }

    public String getAss_name() {
        return ass_name;
    }

    public void setAss_name(String ass_name) {
        this.ass_name = ass_name;
    }
}
