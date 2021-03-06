package com.example.electronicsstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicsstore.adapters.RecyclerAdapterCart;
import com.example.electronicsstore.objects.Order;
import com.example.electronicsstore.objects.Product;
import com.example.electronicsstore.objects.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Button proceedToCheckout;
    private RecyclerAdapterCart.RecyclerViewClickListener clickListener;
    private ArrayList<Product> productList = new ArrayList<Product>();
    private RecyclerAdapterCart adapter;
    private DatabaseReference dbRef, userRef;
    private double total;
    private TextView totalTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        recyclerView = findViewById(R.id.cartRCV);
        totalTextView = findViewById(R.id.totalPriceTextView);
        dbRef = FirebaseDatabase.getInstance().getReference("Cart");
        userRef = FirebaseDatabase.getInstance().getReference("Profiles");
        proceedToCheckout = findViewById(R.id.checkoutButton);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Product p = child.getValue(Product.class);
                    productList.add(p);
                    total = total + p.getPrice();
                    totalTextView.setText("Total: " + String.valueOf(total));
                }
                setOnClickListener();
                adapter = new RecyclerAdapterCart(productList, clickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });


        proceedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Profile current = snapshot.getValue(Profile.class);
                        Order order = new Order(productList, current, total, "");
                        Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                        intent.putExtra("order", (Serializable) order);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });

    }

    private void setOnClickListener() {
        clickListener = new RecyclerAdapterCart.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Product product = productList.get(position);
                Intent i = new Intent(CartActivity.this, ProductActivity.class);
                i.putExtra("product", (Serializable) product);
                startActivity(i);

            }
        };
    }
}
