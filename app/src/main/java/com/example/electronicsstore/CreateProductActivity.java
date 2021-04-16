package com.example.electronicsstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateProductActivity extends AppCompatActivity {

    EditText nameET, manufacturerET, priceET, stockET;
    Spinner spinner;
    Button createProduct;
    DatabaseReference productRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        nameET = findViewById(R.id.nameET);
        manufacturerET = findViewById(R.id.manufacturerET);
        priceET = findViewById(R.id.priceET);
        stockET = findViewById(R.id.stockET);
        spinner = findViewById(R.id.spinner);
        createProduct = findViewById(R.id.createProduct);

        productRef = FirebaseDatabase.getInstance().getReference("Products");

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(CreateProductActivity.this, R.array.category_array,
                android.R.layout.simple_spinner_item);

        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(staticAdapter);

        createProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString().trim();
                String manufacturer = manufacturerET.getText().toString().trim();
                double price = Double.parseDouble(priceET.getText().toString().trim());
                int stock = Integer.parseInt(stockET.getText().toString().trim());
                String category = spinner.getSelectedItem().toString();
                String keyId = productRef.push().getKey();

                Product product = new Product(name, manufacturer, category, keyId, price, stock);

                productRef.child(keyId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CreateProductActivity.this, "Product created sucessfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(CreateProductActivity.this, ViewProductsActivity.class);
                        startActivity(i);
                    }
                });
            }
        });


    }
}