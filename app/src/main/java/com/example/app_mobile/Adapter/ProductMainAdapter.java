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

public class ProductMainAdapter extends RecyclerView.Adapter<ProductMainAdapter.MyViewHolder> {
    Context context;
    List<Product> array;
    private ProductAdapter.OnItemClickListener mItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view,int position);
    }
    public void setOnItemClickListener(ProductAdapter.OnItemClickListener listener) {
        mItemClickListener = listener;
    }
    public ProductMainAdapter(Context context, List<Product> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_moi,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = array.get(position);
        holder.txtten.setText(product.getProductName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtgia.setText("Price: " + decimalFormat.format(Double.parseDouble(String.valueOf(product.getProductPrice()))) + "$");
        holder.txtgiaEvent.setText("Price: " + decimalFormat.format(Double.parseDouble(String.valueOf(product.getProductPrice()))*1.2) + "$");
        if (product.getEventId() == 1) holder.txtgiaEvent.setVisibility(View.VISIBLE);
        else holder.txtgiaEvent.setVisibility(View.INVISIBLE);
        holder.txtgiaEvent.setPaintFlags(holder.txtgiaEvent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (product.getImageUrls().size() !=0) {
            Glide.with(context)
                    .load(product.getImageUrls().get(0))
                    .into(holder.imghinhanh);
        }
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
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtgia, txtten,txtgiaEvent;
        ImageView imghinhanh;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtgia = itemView.findViewById(R.id.itemsp_gia);
            txtgiaEvent = itemView.findViewById(R.id.itemsp_giaEvent);
            txtten = itemView.findViewById(R.id.itemsp_ten);
            imghinhanh = itemView.findViewById(R.id.itemsp_image);
        }
    }
}
