package com.example.app_mobile.Adapter;

import android.content.Context;
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
        holder.cateprice.setText("Giá: " + decimalFormat.format(Double.parseDouble(String.valueOf(product.getProductPrice()))) + "$");
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

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mItemClickListener != null) {
//                    mItemClickListener.onItemClick(position);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cateprice, catepname;
        ImageView catehinhsp;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cateprice = itemView.findViewById(R.id.cateprice);
            catepname = itemView.findViewById(R.id.catename);
            catehinhsp = itemView.findViewById(R.id.catehinhsp);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view,int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

//

//    List<Product> productList;
//    Context context;
//    private OnItemClickListener mItemClickListener;
//
//    public ProductAdapter(List<Product> productList, Context context, List<Feature> featureList) {
//        this.productList = productList;
//        this.context = context;
//    }
//    @NonNull
//    @Override
//    public int getCount() {
//        return productList.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return productList.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return productList.get(i).getProductId();
//    }
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        ViewHolder viewHolder = null;
//        if (convertView == null){
//            convertView = LayoutInflater.from(context).inflate(R.layout.product_listview_item,null);
//            viewHolder = new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        }
//        else{
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        Product product = this.productList.get(position);
//        ///Ánh xạ
//        //// setText - setImg
//        viewHolder.tvName.setText(product.getProductName());
//        viewHolder.tvPrice.setText("Price: "+String.valueOf(product.getProductPrice())+ "$");
//        if (product.getProductId() < 35) {
//            Glide.with(context)
//                    .load(product.getImageUrls().get(0))
//                    .into(viewHolder.ivIMG);
//        }
//
//        return convertView;
//    }
//
//    public class ViewHolder{
//        TextView tvName,tvPrice,tvDes;
//        ImageView ivIMG;
//        public ViewHolder(View itemView){
//
//            tvName = ((TextView) itemView.findViewById(R.id.catename));
//            ivIMG = (ImageView) itemView.findViewById(R.id.catehinhsp);
//            tvPrice = ((TextView) itemView.findViewById(R.id.cateprice));
//        }
//    }


}
