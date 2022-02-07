package com.nrlm.melasalesoffline.pojo;

import java.util.List;

public class OnlineBillModle {
    String bill_id;
    String invoice_no;
    String shg_reg_id;
    String mela_id;
    String generated_date;
    String paymentMode;
    List<OnlineProductModle> onlineProduct;

    public String getGenerated_date() {
        return generated_date;
    }

    public void setGenerated_date(String generated_date) {
        this.generated_date = generated_date;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getShg_reg_id() {
        return shg_reg_id;
    }

    public void setShg_reg_id(String shg_reg_id) {
        this.shg_reg_id = shg_reg_id;
    }

    public String getMela_id() {
        return mela_id;
    }

    public void setMela_id(String mela_id) {
        this.mela_id = mela_id;
    }

    public List<OnlineProductModle> getOnlineProduct() {
        return onlineProduct;
    }

    public void setOnlineProduct(List<OnlineProductModle> onlineProduct) {
        this.onlineProduct = onlineProduct;
    }
}
