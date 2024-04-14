package com.example.gamecenter.Main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gamecenter.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    // Attributes:
    private List<ListElement> mdata;
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClickListener listener;

    // Constructor:
    public ListAdapter(List<ListElement> mdata, Context context, OnItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.mdata = mdata;
        this.context = context;
        this.listener = listener;
    }


    // Public methods:
    @Override
    public int getItemCount() {
        return mdata.size();
    }
    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
        holder.bindDate(mdata.get(position));
    }
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element, parent, false);
        return new ListAdapter.ViewHolder(view, listener);
    }
    public void setItems(List<ListElement> items) {
        mdata = items;
    }


    // Inner class:
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Attributes:
        ImageView iconImage;
        TextView name;

        // Constructor:
        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.scoreImageView);
            name = itemView.findViewById(R.id.nameTextView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(mdata.get(position));
                }
            });
        }

        // Methods:
        void bindDate(final ListElement item) {
            iconImage.setColorFilter(Color.parseColor(item.color), PorterDuff.Mode.SRC_IN);
            name.setText(item.getName());
        }
    }
}