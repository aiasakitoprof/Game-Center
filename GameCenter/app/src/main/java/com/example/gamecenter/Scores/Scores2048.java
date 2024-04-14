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

public class Scores2048 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores2048);

        Button menu = findViewById(R.id.btn2048Menu);
        Button scoresBtn = findViewById(R.id.btnSenku);

        menu.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        scoresBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScoresSenku.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.score2048RV);

        List<UserScore> scores = getScores();
        int[] positions = {R.drawable.first, R.drawable.second, R.drawable.third, R.drawable.fourth, R.drawable.fifth};

        Scores2048Adapter scores2048Adapter = new Scores2048Adapter(this, positions, scores);
        recyclerView.setAdapter(scores2048Adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<UserScore> getScores() {

        SharedPreferences prefs = getSharedPreferences("2048_Scores", Context.MODE_PRIVATE);

        Map<String, ?> allEntries = prefs.getAll();
        ArrayList<UserScore> scores = new ArrayList<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            try {
                scores.add(new UserScore(entry.getKey(), Integer.parseInt(entry.getValue().toString())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        scores.sort((o1, o2) -> Integer.compare(o2.score, o1.score));
        return scores.subList(0, Math.min(5, scores.size()));
    }
}

class UserScore {
    String user;
    int score;

    public UserScore(String user, int score) {
        this.user = user;
        this.score = score;
    }
}