package com.example.app_mobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.app_mobile.Model.Product;
import com.example.app_mobile.R;

import java.util.List;
import java.util.Map;

public class OrderDetailAdapter extends BaseAdapter {

    Context context;
    Map<Long, Integer> orderDetails; // productID và Amount
    List<Product> productList;

    public OrderDetailAdapter(Context context, Map<Long, Integer> orderDetails, List<Product> productList) {
        this.context = context;
        this.orderDetails = orderDetails;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return orderDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return orderDetails.keySet().toArray()[position];
    }

    @Override
    public long getItemId(int position) {
        return (long) getItem(position);
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        System.out.println("ok " + position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_order_detail_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Lấy thông tin sản phẩm dựa trên position
        Long productId = (Long) orderDetails.keySet().toArray()[position];
        Integer amount = orderDetails.get(productId);
        Product product = null;
        for (Product p : productList){
            if (p.getProductId() == productId){
                product = p;
                break;
            }
        }
        if (product != null){
            viewHolder.tvNameOrderDetailItem.setText(product.getProductName());
            viewHolder.tvPriceOrderDetailItem.setText(String.valueOf(product.getProductPrice()));
            viewHolder.tvAmountOrderDetailItem.setText(String.valueOf(amount));

            Glide.with(context)
                    .load(product.getImageUrls().get(0))
                    .into(viewHolder.ivImageOrderDetailItem);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvNameOrderDetailItem,tvPriceOrderDetailItem,tvAmountOrderDetailItem;
        ImageView ivImageOrderDetailItem;

        public ViewHolder(View view) {
            tvNameOrderDetailItem = view.findViewById(R.id.tvNameOrderDetailItem);
            tvPriceOrderDetailItem = view.findViewById(R.id.tvPriceOrderDetailItem);
            tvAmountOrderDetailItem = view.findViewById(R.id.tvAmountOrderDetailItem);
            ivImageOrderDetailItem = view.findViewById(R.id.ivImageOrderDetailItem);



        }
    }
}
