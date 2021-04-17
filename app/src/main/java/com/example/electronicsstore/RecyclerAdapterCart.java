package com.example.electronicsstore;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapterCart extends RecyclerView.Adapter<RecyclerAdapterCart.MyViewHolder> {

    private ArrayList<Product> productList;
    private RecyclerAdapterCart.RecyclerViewClickListener listener;

    public RecyclerAdapterCart(ArrayList<Product> productList, RecyclerAdapterCart.RecyclerViewClickListener listener){
        this.productList = productList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView productNameTxt, brandNameTxt, priceTxt;
        public ImageView productImg;


        public MyViewHolder(View itemView) {
            super(itemView);

            productNameTxt = itemView.findViewById(R.id.nameTextViewCart);
            brandNameTxt = itemView.findViewById(R.id.brandTextViewCart);
            priceTxt = itemView.findViewById(R.id.priceTextViewCart);
            productImg = itemView.findViewById(R.id.imageViewCart);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }


    public void onBindViewHolder(MyViewHolder holder, int position) {

        Product currentProduct = productList.get(position);

        StorageReference productReference = FirebaseStorage.getInstance().getReference().child(currentProduct.getId() + ".jpg");

        holder.productNameTxt.setText(currentProduct.getTitle());
        holder.brandNameTxt.setText(currentProduct.getManufacturer());
        holder.priceTxt.setText("€"+String.valueOf(currentProduct.getPrice()));

        productReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.productImg);
            }
        });
    }

    public int getItemCount() {
        return productList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }

}
