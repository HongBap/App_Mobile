package com.example.app_mobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

public class CheapActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    List<Product> productList = new ArrayList<>();
    List<Feature> featureList = new ArrayList<>();
    User userInfoLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheap);
        setControl();
        setEvent();
        ActionBar();
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
        productList = (List<Product>) bundle.getSerializable("productList");
        featureList = (List<Feature>) bundle.getSerializable("keyfeatureList");
        Intent intent = getIntent();
        userInfoLogin = (User) intent.getSerializableExtra("userInfoLogin");


        productAdapter = new ProductAdapter(productList, getApplicationContext());
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
                intent.putExtras(bundle);
                System.out.println("FeatureList = " + featureList.size());
                System.out.println("ProductList = " + productList.size());
                startActivity(intent);
            }
        });
    }


    private void setControl() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview_cp);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

}