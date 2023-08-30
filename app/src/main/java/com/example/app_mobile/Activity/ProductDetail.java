package com.example.app_mobile.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.app_mobile.Adapter.RateAdapter;
import com.example.app_mobile.Model.Feature;
import com.example.app_mobile.Model.Order;
import com.example.app_mobile.Model.Product;
import com.example.app_mobile.Model.Rate;
import com.example.app_mobile.Model.User;
import com.example.app_mobile.R;
import com.example.app_mobile.Retrofit.ApiService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetail extends AppCompatActivity {

    Integer amount = 1;
//    String username = "mynth30";
//    String email = "mynth30@gmail.com";
    ListView lvBinhLuan;
    String[] numbers = {"1", "2", "3", "4", "5"};
    RateAdapter adapterRate;
    LinearLayout llTinhNang, llBinhLuan;
    ImageView ivImage, ivImageDetail1, ivImageDetail2 , ivIconCartProductDetail;
    EditText edtMyBinhLuan;
    ListView lvMyBinhLuan;
    Button btnChiTiet, btnTinhNang, btnAddtoCartProductDetail, btnBinhLuan, btnImage, btnGuiBinhLuan, btnDanhGia;
    Button btnCongAmount, btnTruAmount, btnPrevious;
    TextView tvPriceProductDetailEvent,tvProductName, tvPrice, tvChiTiet, tvBrand, tvCamera, tvRam, tvRom,tvNumberInCartProductDetail;
    TextView tvStart1, tvStart2, tvStart3, tvStart4, tvStart5, tvBinhLuan, tvDanhGia, tvAmount;
    Product product;
    User userInfoLogin;
    private ArrayAdapter<Integer> arrayAdapter;
    List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Intent intent = getIntent();
        userInfoLogin = (User) intent.getSerializableExtra("userInfoLogin");
        setControl();
        setEvent();
    }

    private void setEvent() {
        // lấy dữ liệu khi click vào product
        product = (Product) getIntent().getSerializableExtra("keyProduct");
        Bundle bundle = getIntent().getExtras();
        List<Feature> featureList = (List<Feature>) bundle.getSerializable("keyfeatureList");
        /// gọi API hiển thị
        getOrderStatusCARTtoIcon();
        getGetRateFromProductId();
        getListOrdersfromUserId();
        /////
        tvProductName.setText(product.getProductName());
        tvProductName.setTag(product.getProductRemain());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvPrice.setText(String.valueOf(product.getProductPrice()) + " $");
        tvPriceProductDetailEvent.setText(decimalFormat.format(Double.parseDouble(String.valueOf( product.getProductPrice()*1.2))) + " $");
        tvPriceProductDetailEvent.setPaintFlags(tvPriceProductDetailEvent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (product.getEventId() == 1) tvPriceProductDetailEvent.setVisibility(View.VISIBLE);
        else tvPriceProductDetailEvent.setVisibility(View.INVISIBLE);
        tvChiTiet.setText(product.getProductDescription());
        if (product.getImageUrls().size() != 0) {
            Glide.with(ProductDetail.this)
                    .load(product.getImageUrls().get(0))
                    .into(ivImage);
            ivImage.setTag(product.getImageUrls().get(0));
            Glide.with(ProductDetail.this)
                    .load(product.getImageUrls().get(0))
                    .into(ivImageDetail1);
            ivImageDetail1.setTag(product.getImageUrls().get(0));
            Glide.with(ProductDetail.this)
                    .load(product.getImageUrls().get(1))
                    .into(ivImageDetail2);
            ivImageDetail2.setTag(product.getImageUrls().get(1));
        }
        for (Integer n : product.getFeatureIds())
            for (Feature feature: featureList){
                if (n == feature.getFeatureFeatureId()){
                    if (feature.getFeatureTypeId() == 1){
                        tvBrand.setText("Brand: " +feature.getFeatureSpecific());
                    }else if (feature.getFeatureTypeId() == 2){
                        tvCamera.setText("Camera: " + feature.getFeatureSpecific());
                    }
                    else if (feature.getFeatureTypeId() == 4){
                        tvRam.setText("Ram: " + feature.getFeatureSpecific());
                    }else if (feature.getFeatureTypeId() == 7){
                        tvRom.setText("ROM: "+ feature.getFeatureSpecific());
                    }
                    break;
                }
            }
        tvAmount.setText(String.valueOf(amount));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_myrateitem, numbers);
        lvMyBinhLuan.setAdapter(adapter);
        btnAddtoCartProductDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ok = false;
                for (Order ordertmp : orderList){
                    if (ordertmp.getOrderStatus().equals("CART")) {
                        Set<Map.Entry<Long, Integer>> entries = ordertmp.getOrderDetails().entrySet();
                        Map.Entry<Long, Integer> firstEntry = entries.iterator().next();
                        Long productId = firstEntry.getKey();
                        int quantity = firstEntry.getValue();
                        if (productId == product.getProductId()){
                            Map<Long, Integer> mapping = new HashMap<>();
                            mapping.put((long) product.getProductId(),quantity+amount);
                            ordertmp.setOrderDetails(mapping);
                            ok = true;
                            PutOrderByOrderId(Math.toIntExact(ordertmp.getOrderId()),ordertmp,userInfoLogin.getToken());
                        }
                    }
                }
                if (!ok){
                    Order order = new Order();
                    order.setUserId((long) userInfoLogin.getId());
                    order.setOrderAddress("");
                    order.setOrderStatus("CART");
                    Map<Long, Integer> map = new HashMap<>();
                    map.put((long) product.getProductId(),amount);
                    order.setOrderDetails(map);
                    System.out.println("ORDER NEW : "+order);
                    PostNewOrder(order);
                }
            }
        });
        lvMyBinhLuan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedNumber = numbers[position];
                tvDanhGia.setText("Đánh giá:    " + selectedNumber + " sao");
                tvDanhGia.setTag(selectedNumber);
            }
        });
        btnTruAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount > 1) {
                    amount--;
                    tvAmount.setText(String.valueOf(amount));
                }
            }
        });

        btnCongAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount < Integer.valueOf(tvProductName.getTag().toString())) {
                    amount++;
                    tvAmount.setText(String.valueOf(amount));
                }
            }
        });

        btnGuiBinhLuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtMyBinhLuan.getText().length() == 0) {
                    Toast.makeText(ProductDetail.this, "Comments are not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tvDanhGia.getTag() == null) {
                    Toast.makeText(ProductDetail.this, "Rating stars are not empty\n", Toast.LENGTH_SHORT).show();
                    return;
                }
                Rate rate = new Rate();
                rate.setRateComment(edtMyBinhLuan.getText().toString());
                rate.setRatePoint(Integer.valueOf(tvDanhGia.getTag().toString()));
                rate.setProductProductId((long) product.getProductId());
                rate.setUserEmail(userInfoLogin.getEmail());
                rate.setUserName(userInfoLogin.getUsername());
                rate.setUserId((long) userInfoLogin.getId());
                if(btnGuiBinhLuan.getTag()=="comment"){
                    PutRate(rate);
                }
                else if(btnGuiBinhLuan.getTag()=="nocomment"){
                    getPostRate(rate);
                }
            }
        });
        btnChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvChiTiet.getVisibility() == View.GONE) {
                    tvChiTiet.setVisibility(View.VISIBLE);
                } else {
                    tvChiTiet.setVisibility(View.GONE);
                }
            }
        });

        btnTinhNang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llTinhNang.getVisibility() == View.GONE) {
                    llTinhNang.setVisibility(View.VISIBLE);
                } else {
                    llTinhNang.setVisibility(View.GONE);
                }
            }
        });
        btnBinhLuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lvBinhLuan.getVisibility() == View.GONE) {
                    lvBinhLuan.setVisibility(View.VISIBLE);
                } else {
                    lvBinhLuan.setVisibility(View.GONE);
                }
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ivImage.getTag().equals(ivImageDetail1.getTag())) {
                    Glide.with(view)
                            .load(ivImageDetail2.getTag())
                            .into(ivImage);
                    ivImage.setTag(ivImageDetail2.getTag());
                } else {
                    Glide.with(view)
                            .load(ivImageDetail1.getTag())
                            .into(ivImage);
                    ivImage.setTag(ivImageDetail1.getTag());
                }
            }
        });
        btnBinhLuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llBinhLuan.getVisibility() == View.GONE) {
                    llBinhLuan.setVisibility(View.VISIBLE);
                } else {
                    llBinhLuan.setVisibility(View.GONE);
                }
            }
        });
        btnDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lvMyBinhLuan.getVisibility() == View.GONE) {
                    lvMyBinhLuan.setVisibility(View.VISIBLE);
                } else {
                    lvMyBinhLuan.setVisibility(View.GONE);
                }
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                btnPrevious.setHighlightColor(1000);
            }
        });
        ivIconCartProductDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                cart.putExtra("userInfoLogin", userInfoLogin);
                startActivity(cart);
            }
        });
    }
    private void setControl() {
        btnPrevious = findViewById(R.id.btn_previousProductDetail);
        ivImage = findViewById(R.id.ivImageProductDetail);
        ivImageDetail1 = findViewById(R.id.ivImageDetail1ProductDetail);
        ivImageDetail2 = findViewById(R.id.ivImageDetail2ProductDetail);
        btnChiTiet = findViewById(R.id.btnChiTietProductDetail);
        btnTinhNang = findViewById(R.id.btnTinhNangProductDetail);
        btnAddtoCartProductDetail = findViewById(R.id.btnAddtoCartProductDetail);
        btnBinhLuan = findViewById(R.id.btnBinhLuanProductDetail);
        tvProductName = findViewById(R.id.tvProductNameProductDetail);
        tvPrice = findViewById(R.id.tvPriceProductDetail);
        tvChiTiet = findViewById(R.id.tvChiTietProductDetail);
        tvBrand = findViewById(R.id.tvBrandProductDetail);
        tvCamera = findViewById(R.id.tvCameraProductDetail);
        tvRam = findViewById(R.id.tvRamProductDetail);
        tvRom = findViewById(R.id.tvRomProductDetail);
        llTinhNang = findViewById(R.id.llTinhNangProductDetail);
        lvBinhLuan = findViewById(R.id.lvBinhLuanProductDetail);
        btnImage = findViewById(R.id.btnImageProductDetail);
        tvStart1 = findViewById(R.id.tvStart1);
        tvStart2 = findViewById(R.id.tvStart2);
        tvStart3 = findViewById(R.id.tvStart3);
        tvStart4 = findViewById(R.id.tvStart4);
        tvStart5 = findViewById(R.id.tvStart5);
        tvBinhLuan = findViewById(R.id.tvBinhLuan);
        btnGuiBinhLuan = findViewById(R.id.btnGuiBinhLuanProductDetail);
        lvMyBinhLuan = findViewById(R.id.lvMyBinhLuanProductDetail);
        edtMyBinhLuan = findViewById(R.id.edtMyBinhLuanProductDetail);
        tvDanhGia = findViewById(R.id.tvDanhGiaProductDetail);
        btnDanhGia = findViewById(R.id.btnDanhGiaProductDetail);
        llBinhLuan = findViewById(R.id.llBinhLuanProductDetail);
        btnCongAmount = findViewById(R.id.btnCongAmountProductDetail);
        btnTruAmount = findViewById(R.id.btnTruAmountProductDetail);
        tvAmount = findViewById(R.id.tvAmountProductDetail);
        ivIconCartProductDetail = findViewById(R.id.ivIconCartProductDetail);
        tvNumberInCartProductDetail = findViewById(R.id.tvNumberInCartProductDetail);
        tvPriceProductDetailEvent = findViewById(R.id.tvPriceProductDetailEvent);
    }
    private void getPostRate(Rate rate){
        ApiService.apiService.PostRate(rate , userInfoLogin.getToken()).enqueue(new Callback<Rate>() {
            @Override
            public void onResponse(Call<Rate> call, Response<Rate> response) {
                getGetRateFromProductId();
                System.out.println("getPostRate thành công");
                edtMyBinhLuan.setText("");
                lvMyBinhLuan.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(), "getPostRate Call API ok  ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Rate> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "getPostRate Call API Errol  " + t, Toast.LENGTH_SHORT).show();
                System.out.println("getPostRate thất bại");
            }
        });
    }
    private void PutRate(Rate rate){
        ApiService.apiService.PutRate(rate, (long) product.getProductId(), (long) userInfoLogin.getId()).enqueue(new Callback<Rate>() {
            @Override
            public void onResponse(Call<Rate> call, Response<Rate> response) {
                System.out.println("PutRate thành công");
                edtMyBinhLuan.setText("");
                lvMyBinhLuan.setVisibility(View.GONE);
                getGetRateFromProductId();
//                Toast.makeText(getApplicationContext(), "PutRate Call API ok  ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Rate> call, Throwable t) {
                System.out.println("PutRate Errol");
//                Toast.makeText(getApplicationContext(), "PutRate Call API Errol  " + t, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void PostNewOrder(Order order){
        ApiService.apiService.PostOrder(order , userInfoLogin.getToken()).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                getListOrdersfromUserId();
                System.out.println("PostOrder thành công");
                AddProductToCartConfirmationDialog();
//                Toast.makeText(getApplicationContext(), "PostOrder Call API ok  ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                System.out.println("PostOrder Errol");
//                Toast.makeText(getApplicationContext(), "PostOrder Call API Errol  " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getOrderStatusCARTtoIcon(){
        ApiService.apiService.getOrdersfromUserId((long) userInfoLogin.getId(), userInfoLogin.getToken()).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                int num = 0;
                for (Order order: response.body())
                    if (order.getOrderStatus().equals("CART")) num ++;
                /// hiển thị số lượng product có trong giỏ hàng lên icon
                tvNumberInCartProductDetail.setText(String.valueOf(num));
            }
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
            }
        });
    }

    private void getListOrdersfromUserId(){
        ApiService.apiService.getOrdersfromUserId((long) userInfoLogin.getId(), userInfoLogin.getToken()).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                orderList = response.body();
                int num = 0;
                for (Order order: orderList){
                    if (order.getOrderStatus().equals("SUCCESS")) {
                        Set<Long> productIds = order.getOrderDetails().keySet();
                        for (Long id : productIds) {
                            if (id == product.getProductId()) {
                                edtMyBinhLuan.setVisibility(View.VISIBLE);
                                tvDanhGia.setVisibility(View.VISIBLE);
                                btnDanhGia.setVisibility(View.VISIBLE);
                                btnGuiBinhLuan.setVisibility(View.VISIBLE);
                                System.out.println("trong đơn hàng người đang sử dụng có product id đang xem OrderList Cal API");
                                break;
                            }
                        }
                        if (edtMyBinhLuan.getVisibility() == View.VISIBLE) {
                            break;
                        }
                    }
                    if (order.getOrderStatus().equals("CART")) num ++;
                }
                /// hiển thị số lượng product có trong giỏ hàng lên icon
                tvNumberInCartProductDetail.setText(String.valueOf(num));
//                Toast.makeText(getApplicationContext(), "Call API ok  ", Toast.LENGTH_SHORT).show();
                System.out.println("OrderList Call API ok");
            }
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Call API Errol  " + t, Toast.LENGTH_SHORT).show();
                System.out.println("OrderList Call API Errol  " + t);
            }
        });
    }
    private void getGetRateFromProductId(){
        ApiService.apiService.getRateFromProductId((long) product.getProductId() , userInfoLogin.getToken()).enqueue(new Callback<List<Rate>>() {
            @Override
            public void onResponse(Call<List<Rate>> call, Response<List<Rate>> response) {
                adapterRate = new RateAdapter(getApplicationContext(), response.body());
                lvBinhLuan.setAdapter(adapterRate);
                ViewGroup.LayoutParams params = lvBinhLuan.getLayoutParams();
                params.height = 200*response.body().size(); // đặt chiều cao của view là 100px
                lvBinhLuan.setLayoutParams(params);
//                Toast.makeText(getApplicationContext(), "RateList Call API ok", Toast.LENGTH_SHORT).show();
                System.out.println("RateList call API ok--- " + response.body().size());
                for (Rate rate: response.body()){
                    btnGuiBinhLuan.setTag("nocomment");
                    System.out.println("ko có comment của người đang dùng trong sp này RateList call API ok--- ");
                    System.out.println(rate.getUserId() + " == " + userInfoLogin.getId());
                    if(rate.getUserId().equals(userInfoLogin.getId())){
                        btnGuiBinhLuan.setTag("comment");
                        System.out.println("có comment của người đang dùng trong sp này RateList call API ok--- ");
                        break;
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Rate>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Call API Errol  " + t, Toast.LENGTH_SHORT).show();
                System.out.println("RateList Call API Errol  " + t);
            }
        });
    }
    private void PutOrderByOrderId(int orderID, Order order , String token){
        ApiService.apiService.PutOrderByOrderID(orderID, order, token).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                    System.out.println("PutOrder Success: " + response.code());
                AddProductToCartConfirmationDialog();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                System.out.println("PutOrder ERROL: " + t);
            }
        });
    }
    private void AddProductToCartConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetail.this);
        builder.setMessage("Product added to cart successfully");
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Đóng cửa sổ cảnh báo và không làm gì cả
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}