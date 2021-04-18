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

public class RecyclerAdapterCustomer extends RecyclerView.Adapter<RecyclerAdapterCustomer.MyViewHolder> {

    private ArrayList<Profile> customerList;
    private RecyclerAdapterCustomer.RecyclerViewClickListener listener;

    public RecyclerAdapterCustomer(ArrayList<Profile> customerList, RecyclerAdapterCustomer.RecyclerViewClickListener listener){
        this.customerList = customerList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView customerNameText, emailText;

        public MyViewHolder(View itemView) {
            super(itemView);

            customerNameText = itemView.findViewById(R.id.nameTextViewCustomer);
            emailText = itemView.findViewById(R.id.emailTextViewCustomer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }


    public void onBindViewHolder(MyViewHolder holder, int position) {

        Profile currentProfile = customerList.get(position);

        holder.customerNameText.setText(currentProfile.getFirstname() + " " + currentProfile.getLastname());
        holder.emailText.setText(currentProfile.getEmail());
    }

    public int getItemCount() {
        return customerList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }

}
