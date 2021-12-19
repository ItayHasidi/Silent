package com.example.silent_ver_1.ui.premium;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.silent_ver_1.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Activity activity;
    ArrayList<com.example.silent_ver_1.ui.premium.ContactModel>arrayList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MainAdapter(Activity activity, ArrayList<com.example.silent_ver_1.ui.premium.ContactModel> arrayList){
        this.activity = activity;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        com.example.silent_ver_1.ui.premium.ContactModel model = arrayList.get(position);
        Log.i(TAG, "model: contact "+holder.tvName);
        holder.tvName.setText(model.getName());
        holder.tvNumber.setText(model.getNumber());
    }
    public void onClickContact(View view){
//        Toast.makeText(view.getContext(), "clicked on contact: "/*+tvName+" , "+tvNumber*/, Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvNumber;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
