package com.example.app_mobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mobile.Adapter.OrderAdapter;
import com.example.app_mobile.Model.Order;
import com.example.app_mobile.Model.Product;
import com.example.app_mobile.Model.User;
import com.example.app_mobile.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    ListView lvDanhSach;
    OrderAdapter adapter;
    ImageView ivPreviousOder,ivchart;
    User userInfoLogin;
    Spinner spinnerFilterOrder;
    List<Order> orderList = new ArrayList<>();
    List<Product> productList = new ArrayList<>();
    List<Order> orderList_tmp = new ArrayList<>();
    List<Order> filteredOrders = new ArrayList<>();
    String[] orderStatuses = {"ALL", "PREPARE" , "SUCCESS", "CANCELED"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setControl();
        setEvent();
    }

    private void setEvent() {
        Bundle bundle = getIntent().getExtras();
        orderList_tmp = (List<Order>) bundle.getSerializable("orderList");
        productList = (List<Product>) bundle.getSerializable("productList");
        Intent intent = getIntent();
        userInfoLogin = (User) intent.getSerializableExtra("userInfoLogin");
        // lọc bỏ CART
        for (Order order : orderList_tmp) {
            if (!order.getOrderStatus().equals("CART")) {
                orderList.add(order);
            }
        }

        /// set Adapter cho Spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, orderStatuses);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterOrder.setAdapter(adapterSpinner);
        spinnerFilterOrder.setSelection(0);
        filteredOrders.addAll(orderList);
        spinnerFilterOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = orderStatuses[position];
                filteredOrders.clear();
                if (selectedStatus.equals("ALL")) {
                    filteredOrders.addAll(orderList);
                } else {
                    for (Order order : orderList) {
                        if (order.getOrderStatus().equals(selectedStatus)) {
                            filteredOrders.add(order);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //set Adapter cho orderList
        adapter = new OrderAdapter(OrderActivity.this, filteredOrders, productList, userInfoLogin);
        lvDanhSach.setAdapter(adapter);
        lvDanhSach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Order order = filteredOrders.get(i);
                Intent intent = new Intent(OrderActivity.this, OrderDetailActivity.class);
                intent.putExtra("keyOrder", order);
                Bundle bundle = new Bundle();
                bundle.putSerializable("productList", (Serializable) productList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ivPreviousOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivchart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderList", (Serializable) orderList);
                Intent internChart = new Intent(getApplicationContext(), ChartActivity.class);
                internChart.putExtras(bundle);
                startActivity(internChart);
            }
        });
    }
    private void setControl() {
        lvDanhSach = findViewById(R.id.lvDanhSach);
        spinnerFilterOrder = findViewById(R.id.spinnerFilterOrder);
        ivPreviousOder = findViewById(R.id.ivPreviousOder);
        ivchart = findViewById(R.id.ivchart);
    }

}