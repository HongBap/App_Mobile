package com.example.app_mobile.Activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_mobile.Adapter.ProductAdapter;
import com.example.app_mobile.Adapter.ProductMainAdapter;
import com.example.app_mobile.Model.Feature;
import com.example.app_mobile.Model.Order;
import com.example.app_mobile.Model.OrderDetailView;
import com.example.app_mobile.Model.Product;
import com.example.app_mobile.Model.User;
import com.example.app_mobile.R;
import com.example.app_mobile.Retrofit.ApiService;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private static final int HOME_A = 1;
    int currentActivity = HOME_A;
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewHome;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    List<Product> productList;
    ProductAdapter productAdapter;
    ListView listViewhome;
    List<Product> spm = new ArrayList();
    ProductMainAdapter productMainAdapter;
    List<Product> productSaleList = new ArrayList<>();
    List<Product> product_cheap_List = new ArrayList<>();
    List<Product> produc_average_List = new ArrayList<>();
    List<Product> product_expensive_List = new ArrayList<>();
    List<OrderDetailView> orderDetailViewList = new ArrayList<>();
    List<Feature> featureList = new ArrayList<>();
    ImageView imageViewsearch;
    User userInfoLogin;
    List<Order> orderList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        userInfoLogin = (User) intent.getSerializableExtra("userInfoLogin");
        setControl();
        ActionViewFlipper();
        getListProductAPI();
        getListFeatureAPI();
        getListOrderAPI();
        getOrdersfromUserId();
        ActionBar();
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

    private void getListProductAPI() {
        ApiService.apiService.productListData().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                productList = response.body();
                List<Product> productsListTmp = new ArrayList<>();
                productsListTmp.addAll(productList);
                System.out.println("ProductList Call API ok");
                for (Product product : productsListTmp) {
                    if (product.getCateId() == 1) product_cheap_List.add(product);
                    else if (product.getCateId() == 2) produc_average_List.add(product);
                    else if (product.getCateId() == 3) product_expensive_List.add(product);
                }
                Collections.sort(productsListTmp, new Comparator<Product>() {
                    @Override
                    public int compare(Product p1, Product p2) {
                        return p2.getProductCreateDate().compareTo(p1.getProductCreateDate());
                    }
                });
                for (int i=0; i<6; i++){
                    spm.add(productsListTmp.get(i));
                }
                productMainAdapter = new ProductMainAdapter(getApplicationContext(), spm);
                recyclerViewHome.setAdapter(productMainAdapter);
                productMainAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Product product = spm.get(position);
                        Intent intent = new Intent(getApplicationContext(), ProductDetail.class);
                        intent.putExtra("keyProduct", product);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("keyfeatureList", (Serializable) featureList);
                        intent.putExtra("userInfoLogin", userInfoLogin);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                productMainAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Call API Errol  " + t, Toast.LENGTH_SHORT).show();
                System.out.println("ProductList Call API Errol  " + t);

            }
        });
    }

    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://images.fpt.shop/unsafe/filters:quality(5)/fptshop.com.vn/uploads/images/2015/Tin-Tuc/a11.png");
        mangquangcao.add("https://cdn.tgdd.vn/Files/2020/06/11/1262208/sale_800x450.jpg");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
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
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                Bundle bundle = new Bundle();
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent trangchu = new Intent(getApplicationContext(), HomeActivity.class);
                        trangchu.putExtra("userInfoLogin", userInfoLogin);
                        startActivity(trangchu);
                        break;
                    case R.id.mn_Cheap:
                        bundle.putSerializable("productList", (Serializable) product_cheap_List);
                        bundle.putSerializable("keyfeatureList", (Serializable) featureList);
                        Intent cheaproduct = new Intent(getApplicationContext(), CheapActivity.class);
                        cheaproduct.putExtras(bundle);
                        cheaproduct.putExtra("userInfoLogin", userInfoLogin);
                        startActivity(cheaproduct);
                        break;
                    case R.id.mn_Average:
                        bundle.putSerializable("productList", (Serializable) produc_average_List);
                        bundle.putSerializable("keyfeatureList", (Serializable) featureList);
                        Intent average = new Intent(getApplicationContext(), AverageActivity.class);
                        average.putExtras(bundle);
                        average.putExtra("userInfoLogin", userInfoLogin);
                        startActivity(average);
                        break;
                    case R.id.mn_Expensive:
                        bundle.putSerializable("productList", (Serializable) product_expensive_List);
                        bundle.putSerializable("keyfeatureList", (Serializable) featureList);
                        Intent expensive = new Intent(getApplicationContext(), ExpensiveActivity.class);
                        expensive.putExtras(bundle);
                        expensive.putExtra("userInfoLogin", userInfoLogin);
                        startActivity(expensive);
                        break;
                    case R.id.mn_Cart:
                        Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                        cart.putExtra("userInfoLogin", userInfoLogin);
                        startActivity(cart);
                        break;
                    case R.id.mn_Order:
                        bundle.putSerializable("orderList", (Serializable) orderList);
                        bundle.putSerializable("productList", (Serializable) productList);
                        Intent order = new Intent(getApplicationContext(), OrderActivity.class);
                        order.putExtras(bundle);
                        order.putExtra("userInfoLogin", userInfoLogin);
                        startActivity(order);
                        break;
                    case R.id.mn_logOut:
                        Intent logout = new Intent(getApplicationContext(),Login.class);
                        startActivity(logout);
                        break;
                    case R.id.mn_productSale:
                        bundle.putSerializable("productSaleList", (Serializable) productSaleList);
                        bundle.putSerializable("keyfeatureList", (Serializable) featureList);
                        Intent productSale = new Intent(getApplicationContext(), SaleProductActivity.class);
                        productSale.putExtras(bundle);
                        productSale.putExtra("userInfoLogin", userInfoLogin);
                        startActivity(productSale);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbarhome);
        viewFlipper = findViewById(R.id.viewlipper);
        recyclerViewHome = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setHasFixedSize(true);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);
        imageViewsearch = findViewById(R.id.imgsearch);
        imageViewsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("productList", (Serializable) product_expensive_List);
                bundle.putSerializable("keyfeatureList", (Serializable) featureList);
                intent.putExtras(bundle);
                intent.putExtra("userInfoLogin", userInfoLogin);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            ExitConfirmationDialog();
        }
    }
    private void ExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Exit Program?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Đóng cửa sổ cảnh báo và không làm gì cả
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getListOrderAPI() {
        ApiService.apiService.getOrderDetailViews(userInfoLogin.getId(), userInfoLogin.getToken()).enqueue(new Callback<List<OrderDetailView>>() {
            @Override
            public void onResponse(Call<List<OrderDetailView>> call, Response<List<OrderDetailView>> response) {
                orderDetailViewList = response.body();
                System.out.println("Code call API success!" + response.code());
                System.out.println("getListOrderAPI call API success!");
            }

            @Override
            public void onFailure(Call<List<OrderDetailView>> call, Throwable t) {
                System.out.println("getListOrderAPI call API error " + t);
            }
        });
    }
    private void getOrdersfromUserId() {
        ApiService.apiService.getOrdersfromUserId((long) userInfoLogin.getId() , userInfoLogin.getToken()).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.code() == 200) {
                    orderList = response.body();
//                    Toast.makeText(getApplicationContext(), "OrderList Call API ok  ", Toast.LENGTH_SHORT).show();
                    System.out.println("OrderList Call API ok");
                }
            }
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "OrderList Call API Errol  " + t, Toast.LENGTH_SHORT).show();
                System.out.println("OrderList Call API Errol  " + t);
            }
        });
    }

}