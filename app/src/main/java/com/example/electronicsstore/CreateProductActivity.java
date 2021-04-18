package com.example.electronicsstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.electronicsstore.objects.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateProductActivity extends AppCompatActivity {

    private EditText nameET, manufacturerET, priceET, stockET;
    private Spinner spinner;
    private Button createProduct;
    private DatabaseReference productRef;
    private ImageView productImg;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

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
        productImg = findViewById(R.id.addImage);

        productRef = FirebaseDatabase.getInstance().getReference("Products");

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(CreateProductActivity.this, R.array.category_array,
                android.R.layout.simple_spinner_item);

        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(staticAdapter);

        productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });

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
                uploadPicture(keyId);

                productRef.child(keyId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });
    }

    public void selectPicture(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data.getData() != null){
            imageUri = data.getData();
            productImg.setImageURI(imageUri);
        }
    }

    public void  uploadPicture(String productId) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image");
        progressDialog.show();

        StorageReference imgRef = storageRef.child(productId + ".jpg");

        imgRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_SHORT).show();
                        Toast.makeText(CreateProductActivity.this, "Product created sucessfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(CreateProductActivity.this, ViewProductsActivity.class);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(CreateProductActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                progressDialog.setMessage(progressPercent + "%");
            }
        });
    }
    }
