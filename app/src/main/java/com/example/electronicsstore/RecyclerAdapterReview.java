package com.example.electronicsstore;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapterReview extends RecyclerView.Adapter<RecyclerAdapterReview.MyViewHolder> {

    private ArrayList<Review> reviewList;
    private RecyclerViewClickListener listener;

    public RecyclerAdapterReview(ArrayList<Review> reviewList, RecyclerViewClickListener listener){
        this.reviewList = reviewList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView productTextView;
        public TextView brandTextView;
        public TextView shadeNameTextView;
        public ImageView reviewImage;
        public RatingBar ratingBar;

        public MyViewHolder(View itemView) {
            super(itemView);

            productTextView = itemView.findViewById(R.id.textViewProductNameRCV);
            brandTextView = itemView.findViewById(R.id.textViewBrandNameRCV);
            reviewImage = itemView.findViewById(R.id.imageViewReviewRCV);
            ratingBar = itemView.findViewById(R.id.ratingBarReview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }


    public void onBindViewHolder(MyViewHolder holder, int position) {
        Review currentReview = reviewList.get(position);

        StorageReference productReference = FirebaseStorage.getInstance().getReference().child(currentReview.getProduct().getId() + ".jpg");

        holder.productTextView.setText(currentReview.getProduct().getTitle());
        holder.brandTextView.setText(currentReview.getProduct().getManufacturer());
        holder.ratingBar.setRating((float) currentReview.getReview());
        productReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.reviewImage);
            }
        });
    }

    public int getItemCount() {
        return reviewList.size();
    }


public interface RecyclerViewClickListener{
        void onClick(View v, int position);
}

}