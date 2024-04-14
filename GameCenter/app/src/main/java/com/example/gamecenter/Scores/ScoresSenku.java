package com.example.gamecenter.Scores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.example.gamecenter.Main.MainActivity;
import com.example.gamecenter.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoresSenku extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores_senku);

        Button menu = findViewById(R.id.btnSenkuMenu);
        Button scoresBtn = findViewById(R.id.btn2048);

        menu.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        scoresBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, Scores2048.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.scoreSenkuRV);

        List<UserTimes> scores = getScores();
        int[] positions = {R.drawable.first, R.drawable.second, R.drawable.third, R.drawable.fourth, R.drawable.fifth};

        ScoresSenkuAdapter scoresSenkuAdapter = new ScoresSenkuAdapter(this, positions, scores);
        recyclerView.setAdapter(scoresSenkuAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<UserTimes> getScores() {

        SharedPreferences prefs = getSharedPreferences("Senku_times", Context.MODE_PRIVATE);

        Map<String, ?> allEntries = prefs.getAll();
        ArrayList<UserTimes> scores = new ArrayList<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            try {
                scores.add(new UserTimes(entry.getKey(), Long.parseLong(entry.getValue().toString())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        scores.sort((o1, o2) -> Long.compare(o1.time, o2.time));
        return scores.subList(0, Math.min(5, scores.size()));
    }

}

class UserTimes {
    String user;
    long time;

    public UserTimes(String user, long time) {
        this.user = user;
        this.time = time;
    }
}