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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewCustomersActivity extends AppCompatActivity {

    private RecyclerAdapterCustomer.RecyclerViewClickListener clickListener;
    private ArrayList<Profile> customerList = new ArrayList<Profile>();
    private RecyclerAdapterCustomer adapter;
    private RecyclerView recyclerView;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customers);

        userRef = FirebaseDatabase.getInstance().getReference("Profiles");
        recyclerView = findViewById(R.id.customerRCV);

        setOnClickListener();
        adapter = new RecyclerAdapterCustomer(customerList, clickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewCustomersActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        loadProfiles();

    }

    public void loadProfiles() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Profile profile = child.getValue(Profile.class);
                    customerList.add(profile);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewCustomersActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setOnClickListener() {
        clickListener = new RecyclerAdapterCustomer.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Profile profile = customerList.get(position);

                Intent i = new Intent(ViewCustomersActivity.this, ProductActivity.class);
                i.putExtra("profile", (Serializable) profile);
                startActivity(i);

            }
        };
    }
}