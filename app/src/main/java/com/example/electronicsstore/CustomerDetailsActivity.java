package com.example.electronicsstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.electronicsstore.adapters.RecyclerAdapter;
import com.example.electronicsstore.adapters.RecyclerAdapterCustomer;
import com.example.electronicsstore.adapters.RecyclerAdapterOrder;
import com.example.electronicsstore.objects.Order;
import com.example.electronicsstore.objects.Product;
import com.example.electronicsstore.objects.Profile;
import com.example.electronicsstore.objects.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerDetailsActivity extends AppCompatActivity {

    private Profile profile;
    private RecyclerAdapterOrder.RecyclerViewClickListener clickListener;

    private TextView name, email, phone, address, carddetails, password;
    private RecyclerView recyclerView;
    private RecyclerAdapterOrder adapter;
    private ArrayList<Order> orderList = new ArrayList<Order>();
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        Intent intent = getIntent();
        profile = (Profile) intent.getSerializableExtra("profile");

        recyclerView = findViewById(R.id.orderHistoryRCV);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        carddetails = findViewById(R.id.carddetails);
        password = findViewById(R.id.password);

        setOnClickListener();
        adapter = new RecyclerAdapterOrder(orderList, clickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerDetailsActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        name.setText("Name: " + profile.getFirstname() + " " + profile.getLastname());
        email.setText("Email: " + profile.getEmail());
        phone.setText("Phone: " + profile.getPhone());
        address.setText("Address: " + profile.getAddress());
        password.setText("Password: " + profile.getPassword());
        if(profile.getCardDetails()!=null){
            carddetails.setText("Card Details: " + String.valueOf(profile.getCardDetails().getCardNumber()) + " "
            + String.valueOf(profile.getCardDetails().getExpiry()) + " " + String.valueOf(profile.getCardDetails().getCcv()));
        }
        else
            carddetails.setText("Card Details: not yet applied");



        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()){
                    Order o = child.getValue(Order.class);
                    if(o.getProfile().getEmail().equalsIgnoreCase(profile.getEmail())){
                        orderList.add(o);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void setOnClickListener() {
        clickListener = new RecyclerAdapterOrder.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Order order = orderList.get(position);

                Intent i = new Intent(CustomerDetailsActivity.this, ViewOrderActivity.class);
                i.putExtra("order", (Serializable) order);
                startActivity(i);

            }
        };
    }
}