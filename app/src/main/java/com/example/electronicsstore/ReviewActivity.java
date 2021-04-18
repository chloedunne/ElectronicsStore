package com.example.electronicsstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.electronicsstore.objects.Profile;
import com.example.electronicsstore.objects.Review;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class ReviewActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference userReference;
    private Review review;
    private Profile currentProfile;
    private TextView usernameTextViewReview, descriptionTextView, productText, productBrandText;
    private RatingBar ratingBarReviewReview;
    private ImageView productImage1;
    private StorageReference productReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Intent intent = getIntent();
        review = (Review) intent.getSerializableExtra("review");

        productText = findViewById(R.id.productText);
        productBrandText = findViewById(R.id.productBrandText);
        usernameTextViewReview = findViewById(R.id.usernameTextViewReview);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        ratingBarReviewReview = findViewById(R.id.ratingBarReviewReview);
        productImage1 = findViewById(R.id.productImage1);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userReference =  FirebaseDatabase.getInstance().getReference("Profiles").child(user.getUid());
        productReference = FirebaseStorage.getInstance().getReference().child(review.getProduct().getId() + ".jpg");

        productReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(productImage1);
            }
        });

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentProfile = snapshot.getValue(Profile.class);
                usernameTextViewReview.setText("Reviewed by " + currentProfile.getFirstname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        productText.setText(review.getProduct().getTitle());
        productBrandText.setText(review.getProduct().getManufacturer());
        descriptionTextView.setText(review.getDescription());
        ratingBarReviewReview.setRating((float) review.getReview());



    }

}