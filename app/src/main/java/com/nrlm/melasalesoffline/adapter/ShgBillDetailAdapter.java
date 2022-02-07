package com.nrlm.melasalesoffline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.pojo.ShgBillModel;

import java.util.List;

public class ShgBillDetailAdapter extends RecyclerView.Adapter<ShgBillDetailAdapter.MyViewHolder> {
    List<ShgBillModel> billListData;
    Context context;


    public ShgBillDetailAdapter(List<ShgBillModel> billListData, Context context) {
        this.billListData = billListData;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myHistoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_bill_data_layout, parent, false);
        return new ShgBillDetailAdapter.MyViewHolder(myHistoryView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_shgregName.setText(billListData.get(position).getShgName() +" ("+billListData.get(position).getShgRegId()+")");

        holder.tv_totalAmount.setText("Total Sell Amount: " +billListData.get(position).getTotalPrice()+" Rs.");
        holder.tv_totalInvoice.setText("Total Invoice Created:  " +billListData.get(position).getSumInvoiceNo());


    }

    @Override
    public int getItemCount() {
        return billListData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_shgregName,tv_shgregId,tv_totalAmount,tv_totalInvoice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_shgregName =itemView.findViewById(R.id.tv_shgregName);
            tv_totalAmount =itemView.findViewById(R.id.tv_totalAmount);
            tv_totalInvoice =itemView.findViewById(R.id.tv_totalInvoice);
        }
    }
}
