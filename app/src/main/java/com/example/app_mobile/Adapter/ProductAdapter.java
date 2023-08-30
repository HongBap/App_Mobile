package com.example.app_mobile.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_mobile.Model.Product;
import com.example.app_mobile.R;


import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    List<Product> productList;
    Context context;
    private OnItemClickListener mItemClickListener;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_listview_item,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.catepname.setText(product.getProductName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.cateprice.setText("Price: " + decimalFormat.format(Double.parseDouble(String.valueOf(product.getProductPrice()))) + " $");
        holder.catepriceEvent.setText(decimalFormat.format(Double.parseDouble(String.valueOf(product.getProductPrice()))*1.2) + " $");
        if (product.getEventId() == 1) holder.catepriceEvent.setVisibility(View.VISIBLE);
        else holder.catepriceEvent.setVisibility(View.INVISIBLE);
        holder.catepriceEvent.setPaintFlags(holder.catepriceEvent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (product.getImageUrls().size() != 0)
            Glide.with(context)
                    .load(product.getImageUrls().get(0))
                    .into(holder.catehinhsp);
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mItemClickListener!= null){
                   mItemClickListener.onItemClick(v,holder.getAdapterPosition());
               }
           }
       });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cateprice, catepname,catepriceEvent;
        ImageView catehinhsp;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cateprice = itemView.findViewById(R.id.cateprice);
//          cateprice.setPaintFlags(cateprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
            catepname = itemView.findViewById(R.id.catename);
            catehinhsp = itemView.findViewById(R.id.catehinhsp);
            catepriceEvent = itemView.findViewById(R.id.catepriceEvent);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view,int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }
}
