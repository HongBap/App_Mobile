package com.example.app_mobile_phone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_mobile_phone.Adapter.ProductAdapter;
import com.example.app_mobile_phone.Adapter.ProductSaleAdapter;
import com.example.app_mobile_phone.Model.Feature;
import com.example.app_mobile_phone.Model.Product;
import com.example.app_mobile_phone.Model.User;
import com.example.app_mobile_phone.R;
import com.example.app_mobile_phone.Retrofit.ApiService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleProductActivity extends AppCompatActivity {
    User userInfoLogin;
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewSaleProduct;
    DrawerLayout drawerLayout;
    ProductSaleAdapter productSaleAdapter;
    List<Product> productSaleList = new ArrayList<>();
    List<Feature> featureList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_product);
        Intent intent = getIntent();
        userInfoLogin = (User) intent.getSerializableExtra("userInfoLogin");
        setControl();
        setEvent();
        ActionBar();
        ActionViewFlipper();
        getListFeatureAPI();
        getListProductAPI();
    }

    private void getListProductAPI() {
        ApiService.apiService.getProductListSale(1).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                productSaleList = response.body();
                List<Product> productListTmp = new ArrayList<>();
                productListTmp.addAll(productSaleList);
                System.out.println("ProductList Call API ok");

                productSaleAdapter = new ProductSaleAdapter(getApplicationContext(), productSaleList);
                recyclerViewSaleProduct.setAdapter(productSaleAdapter);
                productSaleAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Product product = productSaleList.get(position);
                        Intent intent = new Intent(getApplicationContext(), ProductDetail.class);
                        intent.putExtra("keyProduct", product);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("keyfeatureList", (Serializable) featureList);
                        intent.putExtra("userInfoLogin", userInfoLogin);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                productSaleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(SaleProductActivity.this, "Call API Errol  " + t, Toast.LENGTH_SHORT).show();
                System.out.println("ProductList Call API Errol  " + t);

            }
        });
    }

    private void getListFeatureAPI() {
        ApiService.apiService.featureListData().enqueue(new Callback<List<Feature>>() {
            @Override
            public void onResponse(Call<List<Feature>> call, Response<List<Feature>> response) {
                featureList = response.body();
                System.out.println("FeatureList call API ok");
            }
            @Override
            public void onFailure(Call<List<Feature>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Call API Errol  " + t, Toast.LENGTH_SHORT).show();
                System.out.println("FeatureList Call API Errol  " + t);
            }
        });
    }

    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://s3.cloud.cmctelecom.vn/tinhte1/2014/10/2624433_hinh-minh-hoa-tinhte.jpg");
        mangquangcao.add("https://s3.cloud.cmctelecom.vn/tinhte1/2018/09/4429992_GIAM1TR.png");
        mangquangcao.add("https://cdn.tgdd.vn/Files/2020/06/11/1262208/sale_800x450.jpg");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void setEvent() {
        // nhận dữ liệu
        Bundle bundle = getIntent().getExtras();
        productSaleList = (List<Product>) bundle.getSerializable("productSaleList");
        featureList = (List<Feature>) bundle.getSerializable("keyfeatureList");
        Intent intent = getIntent();
        userInfoLogin = (User) intent.getSerializableExtra("userInfoLogin");


        productSaleAdapter = new ProductSaleAdapter(getApplicationContext(), productSaleList);
        recyclerViewSaleProduct.setAdapter(productSaleAdapter);
        productSaleAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Product product = productSaleList.get(position);
                Intent intent = new Intent(getApplicationContext(), ProductDetail.class);
                intent.putExtra("keyProduct", product);
                Bundle bundle = new Bundle();
                bundle.putSerializable("keyfeatureList", (Serializable) featureList);
                intent.putExtra("userInfoLogin", userInfoLogin);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar);
        viewFlipper = findViewById(R.id.viewlipper);
        recyclerViewSaleProduct = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewSaleProduct.setLayoutManager(layoutManager);
        recyclerViewSaleProduct.setHasFixedSize(true);
        drawerLayout = findViewById(R.id.drawerlayout);
    }
}