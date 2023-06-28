package com.example.app_mobile_phone.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.app_mobile_phone.Model.Order;
import com.example.app_mobile_phone.Model.User;
import com.example.app_mobile_phone.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    User userInfoLogin;
    List<Order> orderList = new ArrayList<>();
    int numCART =0,numPREPARE=0, numCANCEL=0, numSUCCESS=0, numCONFIRM=0,numSHIPPING=0;
    private PieChart pieChart;
    ImageView ivPreviousChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Intent intent = getIntent();
        userInfoLogin = (User) intent.getSerializableExtra("userInfoLogin");
        setControl();
        setEvent();
    }

    private void setControl() {
        pieChart =  findViewById(R.id.pieChart);
        ivPreviousChart = findViewById(R.id.ivPreviousChart);
    }

    private void setEvent() {
        // nhận dữ liệu
        numCART =0;numPREPARE=0; numCANCEL=0; numSUCCESS=0; numCONFIRM=0;numSHIPPING=0;
        Bundle bundle = getIntent().getExtras();
        orderList = (List<Order>) bundle.getSerializable("orderList");
        for (Order order : orderList){
            if (order.getOrderStatus().equals("CART")) numCART +=1;
            else if (order.getOrderStatus().equals("PREPARE")) numPREPARE +=1;
            else if (order.getOrderStatus().equals("SUCCESS")) numSUCCESS +=1;
            else if (order.getOrderStatus().equals("CANCELED")) numCANCEL +=1;
            else if (order.getOrderStatus().equals("CONFIRM")) numCONFIRM +=1;
            else if (order.getOrderStatus().equals("SHIPPING")) numSHIPPING +=1;
        }


        List<PieEntry> entries = new ArrayList<>();
        if (numCART > 0.0) {
            entries.add(new PieEntry(numCART, "CART: " + Float.toString(numCART)));
        }
        if (numPREPARE > 0.0) {
            entries.add(new PieEntry(numPREPARE, "PREPARE: " + Float.toString(numPREPARE)));
        }
        if (numSHIPPING > 0.0) {
            entries.add(new PieEntry(numSHIPPING, "SHIPPING: " + Float.toString(numSHIPPING)));
        }
        if (numSUCCESS > 0.0) {
            entries.add(new PieEntry(numSUCCESS, "SUCCESS: " + Float.toString(numSUCCESS)));
        }
        if (numCONFIRM > 0.0) {
            entries.add(new PieEntry(numCONFIRM, "CONFIRM: " + Float.toString(numCONFIRM)));
        }
        if (numCANCEL > 0.0) {
            entries.add(new PieEntry(numCANCEL, "CANCELED: " + Float.toString(numCANCEL)));
        }


        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.GRAY, Color.WHITE);
        dataSet.setValueTextColor(Color.BLACK);

        PieData pieData = new PieData(dataSet);

        // Đặt dữ liệu vào biểu đồ
        pieChart.setData(pieData);

        // Tùy chỉnh biểu đồ
        pieChart.setCenterText("ORDERS");
        pieChart.setCenterTextSize(10);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setRotationEnabled(true);

        // Cập nhật giao diện
        pieChart.invalidate();

        ivPreviousChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}