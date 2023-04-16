package com.example.app_mobile.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.app_mobile.Adapter.CartAdapter;
import com.example.app_mobile.Model.Order;
import com.example.app_mobile.Model.OrderDetailView;
import com.example.app_mobile.Model.User;
import com.example.app_mobile.R;
import com.example.app_mobile.Retrofit.ApiService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartActivity extends AppCompatActivity {

    ListView lvOrders;
    TextView tvTotals;
    ImageView ivBuyNow , ivPreviousCart;
    List<OrderDetailView> orderDetailViewList = new ArrayList<>();
    List<OrderDetailView> listTmp = new ArrayList<>();
    Map<Long, Integer> orderDetails = new HashMap<>();
    Map<Integer, Integer> orderIds = new HashMap<>();
    CartAdapter adapter;
    User userInfoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Intent intent = getIntent();
        userInfoLogin = (User) intent.getSerializableExtra("userInfoLogin");
        setControl();
        getListOrderAPI();
    }
    private void setEvent() {
        for (int i = 0; i < listTmp.size(); i++) {
            if (listTmp.get(i).getStatus().equals("CART")) {
                orderDetailViewList.add(listTmp.get(i));
                System.out.println(i + " H ");
            }
        }
//        orderDetailViewList
        adapter = new CartAdapter(orderDetailViewList, CartActivity.this , userInfoLogin);
        lvOrders.setAdapter(adapter);
        //
        Intent intent_data = getIntent();
        String data_intent = intent_data.getStringExtra("data");
        tvTotals.setText(data_intent);

        //
        ivPreviousCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailView orderDetailView = orderDetailViewList.get(0);
                String username = orderDetailView.getFirstName() + " " + orderDetailView.getLastName();
                if (username == null || username == "") {
                    username = "anonymous";
                }
                String phoneNumber = orderDetailView.getPhone();
                if (phoneNumber == null || phoneNumber == "" || phoneNumber.trim().length() < 1) {
                    phoneNumber = "(please update your phone number!)";
                }
                String address = orderDetailView.getAddress();
                if (address.length() < 1) {
                    address = "(please update your address!)";
                }
                int orderId = orderDetailView.getOrderId();
//                String price = String.valueOf(orderDetailView.getPrice());
                String total = tvTotals.getText().toString().trim();
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                //intent.putExtra("orderId", orderId);
                intent.putExtra("userId", userInfoLogin.getId());
                intent.putExtra("username", username);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("address", address);
                intent.putExtra("userInfoLogin", userInfoLogin);


                Order order = new Order();
                order.setUserId((long) userInfoLogin.getId());
                order.setOrderPhone(phoneNumber);
                order.setOrderAddress(address);
                order.setOrderStatus("PREPARE");
                order.setOrderDetails(orderDetails);
                intent.putExtra("order", order);
                intent.putExtra("mainOrderIds", (Serializable) orderIds);

//                updateOrderAPI(order);

                if (total == "") {
                    Toast.makeText(CartActivity.this, "You didn't choose item!", Toast.LENGTH_SHORT).show();
                } else {
                    intent.putExtra("price", Integer.parseInt(total));
                    startActivity(intent);
                }
            }
        });
    }
    private void setControl() {
        lvOrders = findViewById(R.id.lvOrders);
        tvTotals = findViewById(R.id.tvTotals);
        ivBuyNow = findViewById(R.id.ivBuyNow);
        ivPreviousCart = findViewById(R.id.ivPreviousCart);
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String total = intent.getStringExtra("data");
            tvTotals.setText(total);
            orderDetails = (Map<Long, Integer>) intent.getSerializableExtra("orderDetails");
            orderIds = (Map<Integer, Integer>) intent.getSerializableExtra("orderIds");
            orderDetails.forEach((key, value) -> {
                System.out.println(key + " - " + value);
            });
            System.out.println("----");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("send2Main"));
//        getListOrderAPI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }
    private void getListOrderAPI() {
        ApiService.apiService.getOrderDetailViews(userInfoLogin.getId(),userInfoLogin.getToken()).enqueue(new Callback<List<OrderDetailView>>() {
            @Override
            public void onResponse(Call<List<OrderDetailView>> call, Response<List<OrderDetailView>> response) {
                listTmp = response.body();
                setEvent();
            }
            @Override
            public void onFailure(Call<List<OrderDetailView>> call, Throwable t) {
//                Toast.makeText(CartActivity.this, "Can't get data!" + t, Toast.LENGTH_SHORT).show();
                System.out.println("getListOrderAPI call API error " + t);
            }
        });
    }
}