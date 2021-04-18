package com.example.electronicsstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electronicsstore.objects.Product;
import com.example.electronicsstore.objects.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ProductActivity extends AppCompatActivity {

    private Product product;
    private ImageView productImage;
    private TextView productText, manufacturerText, priceText, stockText;
    private Button addToCart, createReview, adjustStock;
    private DatabaseReference cartRef, userRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Intent i = getIntent();
        product = (Product) i.getSerializableExtra("product");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("Profiles");
        cartRef = FirebaseDatabase.getInstance().getReference("Cart");

        productImage = findViewById(R.id.productImage1);
        priceText = findViewById(R.id.priceText);
        stockText = findViewById(R.id.stockText);
        manufacturerText = findViewById(R.id.manufacturerText);
        productText = findViewById(R.id.productText);

        addToCart = findViewById(R.id.addToCart);
        createReview = findViewById(R.id.createReview);
        adjustStock = findViewById(R.id.adjustStock);

        StorageReference productReference = FirebaseStorage.getInstance().getReference().child(product.getId() + ".jpg");

        productReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(productImage);
            }
        });
        productText.setText(product.getTitle());
        manufacturerText.setText(product.getManufacturer());
        stockText.setText("Stock level: " + String.valueOf(product.getStock()));
        priceText.setText("Price: " + String.valueOf(product.getPrice()));

        createReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductActivity.this, CreateReviewActivity.class);
                i.putExtra("product", (Serializable) product);
                startActivity(i);
            }
        });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    cartRef.child(product.getId()).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ProductActivity.this, product.getTitle() + " added to Cart", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ProductActivity.this, ViewProductsActivity.class);
                            startActivity(i);
                        }
                    });
                }
        });

        adjustStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Profile current = snapshot.getValue(Profile.class);
                        if(current.getAdmin()){
                            Intent i = new Intent(ProductActivity.this, CreateProductActivity.class);
                            startActivity(i);
                        }
                        else
                            Toast.makeText(ProductActivity.this, "Admin access only", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                final EditText input = new EditText(ProductActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                builder.setTitle("Adjust Stock");
                builder.setMessage("Current stock is " + String.valueOf(product.getStock()) + ". Please type in updated stock level:" );
                builder.setView(input);
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int newStock = Integer.parseInt(input.getText().toString());
                        updateStockLevel(newStock);
                        Toast.makeText(ProductActivity.this, "Stock updated", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void updateStockLevel(int newStock){

        FirebaseDatabase.getInstance().getReference("Products").child(product.getId()).child("stock").setValue(newStock);
    }
}