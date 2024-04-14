package com.example.gamecenter.Scores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamecenter.R;

import java.util.List;

public class Scores2048Adapter extends RecyclerView.Adapter<Scores2048Adapter.MyViewHolder> {

    Context context;
    int[] positions;
    List<UserScore> scores;

    public Scores2048Adapter(Context context, int[] positions, List<UserScore> scores) {
        this.context = context;
        this.positions = positions;
        this.scores = scores;
    }

    @NonNull
    @Override
    public Scores2048Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.score_element, parent, false);
        return new Scores2048Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Scores2048Adapter.MyViewHolder holder, int position) {
        holder.primary.setText(scores.get(position).user);
        holder.secondary.setText(String.valueOf(scores.get(position).score));
        holder.imageView.setImageResource(positions[position]);
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView primary, secondary;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.scoreImageView);
            primary = itemView.findViewById(R.id.primaryTextView);
            secondary = itemView.findViewById(R.id.secondaryTextView);
        }
    }
}