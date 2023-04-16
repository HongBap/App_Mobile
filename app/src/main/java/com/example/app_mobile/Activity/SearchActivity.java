package com.example.app_mobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_mobile.Adapter.ProductAdapter;
import com.example.app_mobile.Model.Feature;
import com.example.app_mobile.Model.Product;
import com.example.app_mobile.Model.User;
import com.example.app_mobile.R;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText edtsearch;
    ProductAdapter productAdapter;
    List<Product> productList = new ArrayList<>();
    List spm = new ArrayList();
    List<Feature> featureList = new ArrayList<>();
    User userInfoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        userInfoLogin = (User) intent.getSerializableExtra("userInfoLogin");
        setControl();
        setEvent();
        ActionToolBar();
//        getListProductAPI();
//        getListFeatureAPI();
    }
    private void setEvent() {
        // nhận dữ liệu
        Bundle bundle = getIntent().getExtras();
        productList = (List<Product>) bundle.getSerializable("productList");
        featureList = (List<Feature>) bundle.getSerializable("keyfeatureList");
        for(int i=0 ; i<=5;i++ ){
            spm.add(productList.get(i));
        }
        productAdapter = new ProductAdapter(spm,getApplicationContext());
        recyclerView.setAdapter(productAdapter);
    }

    //    private void getListFeatureAPI() {
//        ApiService.apiService.featureListData().enqueue(new Callback<List<Feature>>() {
//            @Override
//            public void onResponse(Call<List<Feature>> call, Response<List<Feature>> response) {
//                featureList = response.body();
//                System.out.println("FeatureList call API ok");
//            }
//            @Override
//            public void onFailure(Call<List<Feature>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Call API Errol  " + t, Toast.LENGTH_SHORT).show();
//                System.out.println("FeatureList Call API Errol  " + t);
//            }
//        });
//    }
//    private void getListProductAPI() {
//        ApiService.apiService.productListData().enqueue(new Callback<List<Product>>() {
//            @Override
//            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                productList = response.body();
//                System.out.println("ProductList Call API ok");
//                for(int i=0 ; i<=5;i++ ){
//                    spm.add(productList.get(i));
//                }
//                productAdapter = new ProductAdapter(spm,getApplicationContext());
//                recyclerView.setAdapter(productAdapter);
//            }
//            @Override
//            public void onFailure(Call<List<Product>> call, Throwable t) {
//                Toast.makeText(SearchActivity.this , "Call API Errol  " + t, Toast.LENGTH_SHORT).show();
//                System.out.println("ProductList Call API Errol  " + t);
//
//            }
//        });
//    }
    public List<Product> searchProduct(String keyword) {
        List<Product> result = new ArrayList<>();
        for (Product product : productList) {
            if (product.getProductName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(product);
            }
        }
        return result;
    }
    private void setControl() {

        edtsearch = findViewById(R.id.edtsearch);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview_search);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Product> searchlist = searchProduct(s.toString());
                productAdapter = new ProductAdapter(searchlist,getApplicationContext());
                recyclerView.setAdapter(productAdapter);
                productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Product product = productList.get(position);
                        Intent intent = new Intent(getApplicationContext(), ProductDetail.class);
                        intent.putExtra("keyProduct", product);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("keyfeatureList", (Serializable) featureList);
                        intent.putExtra("userInfoLogin", userInfoLogin);
                        System.out.println("FeatureList = " + featureList.size());
                        System.out.println("ProductList = " + productList.size());
                        intent.putExtras(bundle);
                        getApplicationContext().startActivity(intent);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}