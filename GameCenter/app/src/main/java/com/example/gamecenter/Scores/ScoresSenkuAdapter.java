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

public class ScoresSenkuAdapter extends RecyclerView.Adapter<ScoresSenkuAdapter.MyViewHolder> {

    Context context;
    int[] positions;
    List<UserTimes> times;

    public ScoresSenkuAdapter(Context context, int[] positions, List<UserTimes> times) {
        this.context = context;
        this.positions = positions;
        this.times = times;
    }

    @NonNull
    @Override
    public ScoresSenkuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.score_element, parent, false);
        return new ScoresSenkuAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoresSenkuAdapter.MyViewHolder holder, int position) {
        holder.primary.setText(times.get(position).user);
        holder.secondary.setText(convertMillisToMinutesSeconds(times.get(position).time));
        holder.imageView.setImageResource(positions[position]);
    }

    @Override
    public int getItemCount() {
        return times.size();
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

    public String convertMillisToMinutesSeconds(long millis) {
        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}