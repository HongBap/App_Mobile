package com.example.app_mobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.app_mobile.Adapter.OrderDetailAdapter;
import com.example.app_mobile.Model.Order;
import com.example.app_mobile.Model.Product;
import com.example.app_mobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {
    TextView tvOderIDDetail,tvStatusDetail,tvTotalsDetail,tvTimeOrderDetail;
    ListView lvOrderDetail;
    List<Product> productList = new ArrayList<>();
    ImageView imgPreviousDetail;
    Order order;
    OrderDetailAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setControl();
        setEvent();
    }

    private void setEvent() {
        Bundle bundle = getIntent().getExtras();
        productList = (List<Product>) bundle.getSerializable("productList");
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("keyOrder");
        tvOderIDDetail.setText(String.valueOf(order.getOrderId()));
        tvStatusDetail.setText(order.getOrderStatus());
        tvTimeOrderDetail.setText(order.getOrderTime().substring(0,10));
        int total = 0;
        for (Map.Entry<Long, Integer> entry : order.getOrderDetails().entrySet()) {
            Long productId = entry.getKey();
            Integer amount = entry.getValue();
            // Do something with productId and amount
            for (Product product : productList) {
                System.out.print("ok");
                if (productId == product.getProductId()) {
                    total += product.getProductPrice() * amount;
                    System.out.println("Product: " + product.getProductId() + "Amount = " + amount
                            + "Total = " + total);
                    break;
                }
            }
            System.out.println();
        }
        tvTotalsDetail.setText(String.valueOf(total));
        adapter = new OrderDetailAdapter(OrderDetailActivity.this , order.getOrderDetails(),productList);
        lvOrderDetail.setAdapter(adapter);
        imgPreviousDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    private void setControl() {
        tvOderIDDetail = findViewById(R.id.tvOderIDDetail);
        tvStatusDetail = findViewById(R.id.tvStatusDetail);
        tvTotalsDetail = findViewById(R.id.tvTotalsDetail);
        lvOrderDetail = findViewById(R.id.lvOrderDetail);
        imgPreviousDetail = findViewById(R.id.imgPreviousDetail);
        tvTimeOrderDetail = findViewById(R.id.tvTimeOrderDetail);
    }
}