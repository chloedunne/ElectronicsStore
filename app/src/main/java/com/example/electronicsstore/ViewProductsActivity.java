package com.example.electronicsstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewProductsActivity extends AppCompatActivity {

    private Button addProductButton;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Product> productList = new ArrayList<Product>();
    private RecyclerAdapter.RecyclerViewClickListener clickListener;
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        addProductButton = findViewById(R.id.addButton);
        recyclerView = findViewById(R.id.productRCV);
        productRef = FirebaseDatabase.getInstance().getReference("Products");

        setOnClickListener();
        adapter = new RecyclerAdapter(productList, clickListener);
        recyclerView.setLayoutManager(new GridLayoutManager(ViewProductsActivity.this, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        loadProducts();

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //createProductActivity

            }
        });

    }

    public void loadProducts(){
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Product product = child.getValue(Product.class);
                    productList.add(product);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProductsActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setOnClickListener() {
        clickListener = new RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Product product = productList.get(position);

               // Intent i = new Intent(ViewProductsActivity.this, ProductActivity.class);
                //i.putExtra("product", (Serializable) product);
                //startActivity(i);

            }
        };
    }
}