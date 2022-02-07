package com.nrlm.melasalesoffline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.database.entity.ProductAddTemptableEntity;
import com.nrlm.melasalesoffline.database.repository.CommonRepository;
import com.nrlm.melasalesoffline.intrface.GetTotalPrice;

import java.util.List;

public class ProductAddCustomAdapter extends RecyclerView.Adapter<ProductAddCustomAdapter.MyViewHolder> {
    List<ProductAddTemptableEntity> productTempData;
    Context context;
    CommonRepository commonRepository;
    GetTotalPrice getTotalPrice =null;
    TextView availableQty;
    String availableQtyy;

    public ProductAddCustomAdapter(List<ProductAddTemptableEntity> productTempData, Context context,GetTotalPrice getTotalPrice,TextView availableqty) {
        this.productTempData = productTempData;
        this.context = context;
        this.getTotalPrice =getTotalPrice;
        this.availableQty=availableqty;

        commonRepository =CommonRepository.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_product_custom_layout, parent, false);
        return new ProductAddCustomAdapter.MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.pNameTv.setText(productTempData.get(position).getProduct_name());
        holder.quantityTv.setText(String.valueOf(productTempData.get(position).getProduct_quantity()));
        holder.pPriceTv.setText(String.valueOf(productTempData.get(position).getProduct_unit_price())+" Rs.");
        holder.tPriceTv.setText(String.valueOf(productTempData.get(position).getProduct_total_price())+" Rs.");

        holder.imageDeletItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int getEntredQuantity = productTempData.get(position).getProduct_quantity();
                int uniqId =productTempData.get(position).getUid();
                String pId =productTempData.get(position).getProduct_id();

                updateQuantityInMainTable(pId,getEntredQuantity);
                deleteItemFromDb(uniqId);
                if(getTotalPrice!=null){
                    String totalSumAmount = String.valueOf(commonRepository.getTotalPriceSumFromTemp());
                    getTotalPrice.notifyPrice(context.getString(R.string.total_sum),totalSumAmount);
                }

                productTempData.remove(position);
                notifyDataSetChanged();
            }
        });

    }



    private void deleteItemFromDb(int uniqId) {
        commonRepository.deletTempData(uniqId);
    }

    private void updateQuantityInMainTable(String pid, int quantity ) {
        int getQuantity = Integer.parseInt(commonRepository.getQuantity(pid));
        int updatedQuantity =getQuantity+quantity;
        availableQty.setText(""+updatedQuantity);  //added by manish
        commonRepository.updateAvalableQuantity(pid, String.valueOf(updatedQuantity));
    }

    @Override
    public int getItemCount() {
        return productTempData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pNameTv,quantityTv,pPriceTv,tPriceTv;
        ImageView imageDeletItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pNameTv = itemView.findViewById(R.id.pNameTv);
            quantityTv = itemView.findViewById(R.id.quantityTv);
            pPriceTv = itemView.findViewById(R.id.pPriceTv);
            tPriceTv = itemView.findViewById(R.id.tPriceTv);
            imageDeletItem = itemView.findViewById(R.id.imageDeletItem);
        }
    }
}
