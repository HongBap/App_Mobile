package com.example.app_mobile.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.app_mobile.Model.Order;
import com.example.app_mobile.Model.Product;
import com.example.app_mobile.Model.User;
import com.example.app_mobile.R;
import com.example.app_mobile.Retrofit.ApiService;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends BaseAdapter {

    Context context;
    List<Order> orderList;
    List<Product> productList;
    User userInfoLogin;
    public OrderAdapter(Context context, List<Order> orderList, List<Product> productList, User userInfoLogin) {
        this.context = context;
        this.orderList = orderList;
        this.productList = productList;
        this.userInfoLogin = userInfoLogin;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return orderList.get(position).getOrderId();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        System.out.println("ok " + position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_order_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //Lấy thông tin đơn hàng
        Order order = orderList.get(position);
        viewHolder.tvOderID.setText(String.valueOf(order.getOrderId()));
        viewHolder.tvNameUserOder.setText(userInfoLogin.getFullName());
        viewHolder.tvPhone.setText(order.getOrderPhone());
        viewHolder.tvAddress.setText(order.getOrderAddress());
        viewHolder.tvStatus.setText(order.getOrderStatus());
        viewHolder.tvTimeOrder.setText(order.getOrderTime().substring(0,10));
        viewHolder.tvNumProductOrder.setText(String.valueOf(order.getOrderDetails().size()));
        int total = 0;
        for (Map.Entry<Long, Integer> entry : order.getOrderDetails().entrySet()) {
            Long productId = entry.getKey();
            Integer amount = entry.getValue();
            // Do something with productId and amount

            for (Product product : productList) {
                if (productId == product.getProductId()) {
                    total += product.getProductPrice() * amount;
                    System.out.println("Product: " + product.getProductId() + "Amount = " + amount
                            + "Total = " + total);
                    break;
                }
            }
        }
        viewHolder.tvTotalPriceOrder.setText(String.valueOf(total));

        // Lấy đối tượng TextView và Button
//        TextView tvStatus = viewHolder.tvStatus;
        Button btnHuyDon = viewHolder.btnHuyDon;

        if (order.getOrderStatus().equals("PREPARE")) {
            //hiện button
            btnHuyDon.setVisibility(View.VISIBLE);
        } else {
            //ẩn button
            btnHuyDon.setVisibility(View.GONE);
        }

        //sự kiện Button
        btnHuyDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Order Cancellation Confirmation");
                builder.setMessage("You want to cancel your order?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý hủy đơn hàng
                        order.setOrderStatus("CANCELED");
                        PutOrderByOrderID(Math.toIntExact(order.getOrderId()),order,userInfoLogin.getToken());
                        notifyDataSetChanged();
                        Toast.makeText(context, "Hủy Đơn Hàng Thành Công", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView tvNameUserOder, tvPhone, tvAddress,tvTimeOrder,
                tvNumProductOrder, tvTotalPriceOrder, tvStatus, tvOderID;
        Button btnHuyDon;

        public ViewHolder(View view) {
            tvNameUserOder = view.findViewById(R.id.tvNameUserOder);
            tvOderID = view.findViewById(R.id.tvOderID);
            tvPhone = view.findViewById(R.id.tvPhone);
            tvAddress = view.findViewById(R.id.tvAddress);
            tvNumProductOrder = view.findViewById(R.id.tvNumProductOrder);
            tvTotalPriceOrder = view.findViewById(R.id.tvTotalPriceOrder);
            tvStatus = view.findViewById(R.id.tvStatus);
            btnHuyDon = view.findViewById(R.id.btnHuyDon);
            tvTimeOrder = view.findViewById(R.id.tvTimeOrder);
        }
    }
    private void PutOrderByOrderID(int orderID , Order order, String token){
        ApiService.apiService.PutOrderByOrderID(orderID, order,token).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful())
                    System.out.println("Canceled Success");
                else System.out.println("Canceled ERROL");
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                System.out.println("Canceled ERROL" + t);

            }
        });
    }
}
