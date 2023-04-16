package com.example.app_mobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mobile.Model.Order;
import com.example.app_mobile.Model.User;
import com.example.app_mobile.R;
import com.example.app_mobile.Retrofit.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    TextView tvUsername,  tvPrice, tvDiscount, tvDeliveryFee, tvTotalPrice;
    RadioButton rbPaymentWithCard, rbPaymentOnDelivery;
    Button btnClose, btnConfirm;
    EditText edtCoupon ,edtPhoneNumber, edtAddress;
    ImageView ivRefreshCoupon;
    String COUPONSHOP = "lezada"; //coupon discount 10%
    String FREESHIP = "freeship";
    Order order;
    User userInfoLogin;
    Map<Integer, Integer> orderIds = new HashMap<>();
    //List<AmountProduct> orderDetails = new ArrayList<AmountProduct>();
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setControl();
        setEvent();
    }


    private void setEvent() {
        Intent intent = getIntent();
        int orderId = intent.getIntExtra("orderId", -1);
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String address = intent.getStringExtra("address");
        order = (Order) intent.getSerializableExtra("order");
        orderIds = (Map<Integer, Integer>) intent.getSerializableExtra("mainOrderIds");
        userInfoLogin = (User) intent.getSerializableExtra("userInfoLogin");

        int price = intent.getIntExtra("price", 0);
        tvPrice.setText(String.valueOf(price)+ ".00$" );
        tvUsername.setText(userInfoLogin.getUsername());
//        tvPhoneNumber.setText(phoneNumber);
//        tvAddress.setText(address);
        //all price before use coupon
        int deliveryPrice = 2;
        tvDeliveryFee.setText("+" + String.valueOf(deliveryPrice) + ".00$");
        int totalPrice = price + deliveryPrice;
        tvTotalPrice.setText(totalPrice + ".00$");
        //set price after use coupon
        ivRefreshCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //discount 10%
                String coupon = edtCoupon.getText().toString().trim();
                System.out.println(coupon);
                int discount = getDiscount(coupon, price);
                int deliveryPrice = getDeliveryPrice(coupon);
                if (discount != 0 || deliveryPrice == 0) {
                    int totalPrice = price - discount + deliveryPrice;
                    tvTotalPrice.setText(String.valueOf(totalPrice + ".00$"));
                    tvDiscount.setText("-" + String.valueOf(discount) + ".00$");
                    tvDeliveryFee.setText("+" + String.valueOf(deliveryPrice) + ".00$");
                } else {
                    Toast.makeText(PaymentActivity.this, "This coupon is not exist!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //radio box payment with visa
        rbPaymentWithCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rbPaymentWithCard.isChecked()) {
                    Toast.makeText(PaymentActivity.this, "The system is maintenance!", Toast.LENGTH_SHORT).show();
                    rbPaymentWithCard.setVisibility(View.GONE);
                }
            }
        });

        //confirm buy
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPhoneNumber.getText().toString().equals("")){
                    edtPhoneNumber.setError("Please enter the phone number");
                    return;
                }
                if (edtAddress.getText().toString().equals("")){
                    edtAddress.setError("Please enter your address");
                    return;
                }
                if (!rbPaymentOnDelivery.isChecked()) {
                    Toast.makeText(PaymentActivity.this, "Please choose payment method!", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("order confirmation successful!");
//                    Toast.makeText(PaymentActivity.this, "order confirmation successful!", Toast.LENGTH_SHORT).show();
                    order.setOrderAddress(edtAddress.getText().toString());
                    order.setOrderPhone(edtPhoneNumber.getText().toString());
                    createOrder();
                    System.out.println("orderId: " + String.valueOf(orderId));
                    orderIds.forEach((key, value) -> {
                        deleteOrder(value);
                    });
                    quitPayment();
                }

            }
        });
        //close and return previous page
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private int getDiscount(String coupon, int price) {
        if (coupon.toLowerCase().equals(COUPONSHOP)) {
            int discount = (int) (price * 0.1);
            return discount;
        }
        return 0;
    }

    private int getDeliveryPrice(String coupon) {
        if(coupon.toLowerCase().equals(FREESHIP)){
            return  0;
        }
        return 2;
    }


    private void setControl() {
        tvUsername = findViewById(R.id.tvUsername);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtAddress = findViewById(R.id.edtAddress);
        tvPrice = findViewById(R.id.tvPrice);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvDeliveryFee = findViewById(R.id.tvDeliveryFee);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        rbPaymentWithCard = findViewById(R.id.rbPaymentWithCard);
        rbPaymentOnDelivery = findViewById(R.id.rbPaymentOnDelivery);
        btnClose = findViewById(R.id.btnClose);
        btnConfirm = findViewById(R.id.btnConfirm);
        edtCoupon = findViewById(R.id.edtCoupon);
        ivRefreshCoupon = findViewById(R.id.ivRefreshCoupon);
    }

    private void createOrder() {
        ApiService.apiService.PostOrder(order , userInfoLogin.getToken()).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    // Handle the successful response from the server
                    System.out.println("Order created successfully");
//                    Toast.makeText(PaymentActivity.this, "Order created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the error response from the server
                    System.out.println("Order created ERROL");
//                    Toast.makeText(PaymentActivity.this, "Failed to create order", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                System.out.println("Order created ERROL");
//                Toast.makeText(PaymentActivity.this, "Failed to create order", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void quitPayment() {
        Intent trangchu = new Intent(getApplicationContext(), HomeActivity.class);
        trangchu.putExtra("userInfoLogin", userInfoLogin);
        startActivity(trangchu);
    }

    private void deleteOrder(int orderId) {
        ApiService.apiService.DeleteOder(orderId , userInfoLogin.getToken()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle the successful response from the server
                    System.out.println("Order deleted successfully");
//                    Toast.makeText(PaymentActivity.this, "Order deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the error response from the server
                    System.out.println("Order deleted ERROL");

//                    Toast.makeText(PaymentActivity.this, "Failed to delete order", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle the network failure
                System.out.println("Order deleted ERROL");

//                Toast.makeText(PaymentActivity.this, "Failed to delete order: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}