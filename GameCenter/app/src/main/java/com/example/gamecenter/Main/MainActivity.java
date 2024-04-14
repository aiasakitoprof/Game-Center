package com.example.gamecenter.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.gamecenter.Games.G2048.G2048Activity;
import com.example.gamecenter.Games.Senku.SenkuActivity;
import com.example.gamecenter.LoginRegister.Login;
import com.example.gamecenter.R;
import com.example.gamecenter.Scores.Scores2048;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<ListElement> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        createScores();

        Button scores = findViewById(R.id.scores_button);

        scores.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Scores2048.class);
            startActivity(intent);
        });
    }

    public void init() {

        elements = new ArrayList<>();
        elements.add(new ListElement("#f0e92b", "2048"));
        elements.add(new ListElement("#088a08", "Senku"));

        ListAdapter listAdapter = new ListAdapter(elements, this, item -> {
            Toast.makeText(MainActivity.this, "Clicked: " + item.getName(), Toast.LENGTH_SHORT).show();
            if (item.getName().equals("Senku")) {
                Intent intent = new Intent(MainActivity.this, SenkuActivity.class);
                startActivity(intent);
            } else if (item.getName().equals("2048")) {
                Intent intent = new Intent(MainActivity.this, G2048Activity.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.score2048RV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    private void createScores() {

        SharedPreferences g2048 = getSharedPreferences("2048_Scores", MODE_PRIVATE);
        SharedPreferences.Editor editor = g2048.edit();
        editor.clear();
        editor.putInt("Alpha", 10000);
        editor.putInt("Beta", 8000);
        editor.putInt("Charlie", 6000);
        editor.putInt("Delta", 4000);
        editor.apply();

        SharedPreferences senku = getSharedPreferences("Senku_times", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = senku.edit();
        editor2.clear();
        editor2.putInt("Alpha", 120000);
        editor2.putInt("Beta", 180000);
        editor2.putInt("Charlie", 240000);
        editor2.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}