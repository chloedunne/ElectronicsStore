package com.example.electronicsstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.electronicsstore.adapters.RecyclerAdapterProduct;
import com.example.electronicsstore.objects.Order;

import java.util.ArrayList;

public class ViewOrderActivity extends AppCompatActivity {

    private Order order;
    private TextView orderNumber, orderAddress, orderTotal;
    private RecyclerView recyclerView;
    private RecyclerAdapterProduct adapter;
    private ArrayList productList;
    private RecyclerAdapterProduct.RecyclerViewClickListener clickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");
        productList = order.getProducts();

        orderNumber = findViewById(R.id.orderNumber);
        orderAddress = findViewById(R.id.orderAddress);
        orderTotal = findViewById(R.id.orderTotal);
        recyclerView = findViewById(R.id.rcvOrderDetails);

        orderNumber.setText("Order Number: " + order.getOrderNum());
        orderAddress.setText("Shipping Address: " + order.getProfile().getAddress());
        orderTotal.setText("Order Total: " + String.valueOf(order.getTotal()));

        adapter = new RecyclerAdapterProduct(productList, clickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewOrderActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }




}