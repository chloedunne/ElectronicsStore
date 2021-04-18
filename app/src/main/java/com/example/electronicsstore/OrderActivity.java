package com.example.electronicsstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electronicsstore.adapters.RecyclerAdapterProduct;
import com.example.electronicsstore.objects.CardDetails;
import com.example.electronicsstore.objects.Order;
import com.example.electronicsstore.objects.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private Order order;
    private EditText cardNumber , month, year, ccv;
    private ArrayList<Product> productList;
    private RecyclerAdapterProduct adapter;
    private RecyclerView recyclerView;
    private Button payButton;
    private DatabaseReference orderRef, profileRef, productRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView totalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        productList = order.getProducts();

        cardNumber = findViewById(R.id.cardNumberET);
        month = findViewById(R.id.monthET);
        year = findViewById(R.id.yearET);
        ccv = findViewById(R.id.ccvET);
        recyclerView = findViewById(R.id.orderProductRCV);
        payButton = findViewById(R.id.placeOrder);
        totalTextView = findViewById(R.id.orderTotal);

        totalTextView.setText("Total: " + String.valueOf(order.getTotal()));

        orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        productRef = FirebaseDatabase.getInstance().getReference("Products");
        profileRef = FirebaseDatabase.getInstance().getReference("Profiles").child(user.getUid()).child("cardDetails");

        adapter = new RecyclerAdapterProduct(productList, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if(order.getProfile().getCardDetails() != null){
            cardNumber.setText(String.valueOf(order.getProfile().getCardDetails().getCardNumber()));
            int monthDigits = Integer.parseInt(Integer.toString(order.getProfile().getCardDetails().getExpiry()).substring(0, 2));
            int yearDigits = Integer.parseInt(Integer.toString(order.getProfile().getCardDetails().getExpiry()).substring(1, 3));
            month.setText(String.valueOf(monthDigits));
            year.setText(String.valueOf(yearDigits));
            ccv.setText(String.valueOf(order.getProfile().getCardDetails().getCcv()));
        }


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cardNumber.getText().length() != 16){
                    cardNumber.setError("Card number must be 16 digits");
                    cardNumber.requestFocus();
                    return;
                }else if(month.getText().length() != 2){
                    month.setError("Error");
                    month.requestFocus();
                    return;
                }
                else if(year.getText().length() != 2){
                    year.setError("Error");
                    year.requestFocus();
                    return;
                }
                else if(ccv.getText().length() != 3){
                    year.setError("CCV must be 3 digits long");
                    year.requestFocus();
                    return;
                }else{
                    long cardNum = Long.valueOf(cardNumber.getText().toString());
                    String expiry = month.getText().toString() + year.getText().toString();
                    int expiryNum = Integer.valueOf(expiry);
                    int ccvNum = Integer.valueOf(ccv.getText().toString());

                    CardDetails cd = new CardDetails(cardNum, ccvNum, expiryNum);

                    profileRef.setValue(cd).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    order.getProfile().setCardDetails(cd);
                }

                if(order.getProfile().getCardDetails()!= null){

                    for(Product p : productList){
                        int stock = p.getStock();
                        p.setStock(stock -1);
                        updateStock(p.getId(), stock -1);

                    }

                String keyId = orderRef.push().getKey();
                order.setOrderNum(keyId);
                orderRef.child(keyId).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( Task<Void> task) {
                        Toast.makeText(OrderActivity.this, "Order placed sucessfully, Order number: " + keyId, Toast.LENGTH_LONG).show();
                        FirebaseDatabase.getInstance().getReference("Cart").removeValue();
                    }
                });
            }else
                    Toast.makeText(OrderActivity.this, "Please enter correct card details", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateStock(String id, int newStockLevel){

        productRef.child(id).child("stock").setValue(newStockLevel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

}