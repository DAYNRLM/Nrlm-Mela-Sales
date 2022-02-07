package com.nrlm.melasalesoffline.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.ColumnInfo;

import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.activity.BillGenerateActivity;
import com.nrlm.melasalesoffline.database.entity.ProductAddTemptableEntity;
import com.nrlm.melasalesoffline.database.repository.CommonRepository;
import com.nrlm.melasalesoffline.pojo.OnlineBillModle;
import com.nrlm.melasalesoffline.pojo.OnlineProductModle;
import com.nrlm.melasalesoffline.utils.AppSharedPreferences;
import com.nrlm.melasalesoffline.utils.AppUtility;
import com.google.android.material.button.MaterialButton;
import com.nrlm.melasalesoffline.utils.DateFactory;
import com.nrlm.melasalesoffline.utils.PdfUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HistoryCustomAdapter extends RecyclerView.Adapter<HistoryCustomAdapter.MyViewHolder> {
    Context context;
    List<OnlineBillModle> onlineBillDataList;
    CommonRepository commonRepository;
    AppSharedPreferences appSharedPreferences;
    AppUtility appUtility;
    String filePath ="";
    DateFactory dateFactory;

    String billGeneratedDate ="";
    String billGeneratedTime ="";

    public HistoryCustomAdapter(Context context, List<OnlineBillModle> onlineBillDataList) {
        this.context = context;
        this.onlineBillDataList = onlineBillDataList;
        commonRepository =CommonRepository.getInstance(context);
        appSharedPreferences =AppSharedPreferences.getsharedprefInstances(context);
        appUtility=AppUtility.getInstance();
        dateFactory =DateFactory.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myHistoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_history_custom_layout, parent, false);
        return new MyViewHolder(myHistoryView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.payment_mode.setText(onlineBillDataList.get(position).getPaymentMode());
        holder.shg_reg_id.setText(onlineBillDataList.get(position).getShg_reg_id());
        holder.invoice_no.setText(onlineBillDataList.get(position).getInvoice_no());
        holder.billDateTv.setText(onlineBillDataList.get(position).getGenerated_date());

        List<OnlineProductModle> listData = onlineBillDataList.get(position).getOnlineProduct();
        int totalSumPrice =0;

        String s = String.format("%-15s %5s %10s\n", "Item", "Qty", "Price");
        String s1 = s+String.format("%-15s %5s %10s\n", "----", "---", "-----");
        /*String line = String.format("%-15s %5d %10.2f\n", "ffff", "100", "10000");*/

       // holder.productDataTv.setText(s1);

        String data = "<h1>"+context.getString(R.string.product_detail)+ "</h1>";
       // data = data+"<br />"+"Product Name"+"&nbsp;&nbsp;&nbsp;&nbsp;" +"Quantity"+"&nbsp;&nbsp;&nbsp;&nbsp;"+"Unit Price"+"&nbsp;&nbsp;&nbsp;&nbsp;"+"Total Price";
        for(int i=0;i<listData.size();i++){
            int quantity = Integer.parseInt(listData.get(i).getProduct_quantity());
            int price = Integer.parseInt(listData.get(i).getProduct_price());
            holder.billCreatedTv.setText(listData.get(i).getCreated_date());     //setting bill created date
            int tprice =quantity*price;
            totalSumPrice =totalSumPrice+tprice;

            String pname =productName(Integer.parseInt(listData.get(i).getProduct_id()));

            data =data+"<b>"+pname+"</b>"+"&nbsp;&nbsp;&nbsp;&nbsp;" +quantity+context.getString(R.string.no)+"&nbsp;&nbsp;"+price+"Rs./unit"+"&nbsp;&nbsp;" +tprice+context.getString(R.string.rs)+"<br />";

        }
        data = data+"<b>"+context.getString(R.string.total_price)+"</b>"+"-----------------------"+totalSumPrice+" Rs.";
        holder.productDataTv.setText(Html.fromHtml(data));

/*
        for(int i=0;i<listData.size();i++){
            TableRow row= new TableRow(context);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            View customLayout = LayoutInflater.from(context).inflate(R.layout.custom_row, null);
           *//* LinearLayout linearLayout =new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv =new TextView(context);
            TextView tv1 =new TextView(context);
            TextView tv2 =new TextView(context);*//*

           *//* linearLayout.addView(tv);
            tv.setText(listData.get(i).getProduct_id());*//*
            row.addView(customLayout);
            holder.tableLayout.addView(row,i);
        }*/

        holder.downLoadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filePath =appSharedPreferences.getShgFolderPath();
                String finalBillNumber =onlineBillDataList.get(position).getInvoice_no();
                String fileName = filePath+"/"+onlineBillDataList.get(position).getInvoice_no()+".pdf";
                File newFile = new File(fileName);
                if(newFile.exists()){
                    appUtility.showLog("Path file exists",HistoryCustomAdapter.class);
                    appSharedPreferences.setPdfPath(fileName);
                    appSharedPreferences.setInvoiceNumber(finalBillNumber);
                    appSharedPreferences.setSendUrlStatus("done");
                    Intent intent =new Intent(context, BillGenerateActivity.class);
                    context.startActivity(intent);

                }else {
                    List<ProductAddTemptableEntity> tempTable =new ArrayList<>();

                    OnlineBillModle productObject =  onlineBillDataList.get(position);
                    String dateArray[] =productObject.getGenerated_date().split(" ");

                    billGeneratedDate = dateArray[0];
                    billGeneratedTime =dateArray[1];

                    List<OnlineProductModle> onlineProduct =productObject.getOnlineProduct();
                    for(int i=0;i<onlineProduct.size();i++){
                        ProductAddTemptableEntity productAdd =new ProductAddTemptableEntity();
                        productAdd.setShg_code(appSharedPreferences.getShgCode());
                        productAdd.setShg_reg_id(productObject.getShg_reg_id());
                        productAdd.setStall_no(appSharedPreferences.getStallNumber());
                        productAdd.setVillage_code(appSharedPreferences.getVillageCode());
                        productAdd.setState_short_name(appSharedPreferences.getStateShortName());
                        productAdd.setProduct_id(onlineProduct.get(i).getProduct_id());
                        productAdd.setProduct_name(productName(Integer.parseInt(onlineProduct.get(i).getProduct_id())));
                        productAdd.setProduct_desc_id("");
                        productAdd.setProduct_desc_Name("");
                        productAdd.setProduct_quantity(Integer.parseInt(onlineProduct.get(i).getProduct_quantity()));
                        productAdd.setProduct_unit_price(Integer.parseInt(onlineProduct.get(i).getProduct_price()));
                        int totalPrice =Integer.parseInt(onlineProduct.get(i).getProduct_quantity())+Integer.parseInt(onlineProduct.get(i).getProduct_price());
                        productAdd.setProduct_total_price(totalPrice);
                        tempTable.add(productAdd);

                    }

                    PdfUtils.getInstance().createPdfFile(fileName,tempTable,finalBillNumber, billGeneratedDate+ " " +billGeneratedTime);
                    Toast.makeText(context,context.getString(R.string.file_not_available),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String productName(int id){
        String str = commonRepository.getProductName(String.valueOf(id));
        return str;

    }

    @Override
    public int getItemCount() {
        return onlineBillDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView payment_mode,invoice_no,shg_reg_id,productDataTv,billDateTv,billCreatedTv;
        MaterialButton downLoadPdfBtn;
        TableLayout tableLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            payment_mode =itemView.findViewById(R.id.payment_mode);
            invoice_no =itemView.findViewById(R.id.invoice_no);
            shg_reg_id =itemView.findViewById(R.id.shg_reg_id);
            productDataTv =itemView.findViewById(R.id.productDataTv);
            downLoadPdfBtn =itemView.findViewById(R.id.downLoadPdfBtn);
            tableLayout =itemView.findViewById(R.id.tableLayout);
            billDateTv =itemView.findViewById(R.id.billDateTv);
            billCreatedTv=itemView.findViewById(R.id.billCreatedTv);

        }
    }
}
/*  String Header =
                "   ****Super Market****       \n"
                        + "Date: "+"10-10-1991"+"     Time: "+"12:23:2002"+"\n"
                        + "---------------------------------\n"
                        + "Name          Qty    Rate     Amt\n"
                        + "---------------------------------\n";

        String amt  =
                "\n \n \nTotal Amount = "+  "100"   +"\n"
                        + "Tax ="   + "200"    + "\n"
                        + "*********************************\n"
                        + "Thank you. \n";*/