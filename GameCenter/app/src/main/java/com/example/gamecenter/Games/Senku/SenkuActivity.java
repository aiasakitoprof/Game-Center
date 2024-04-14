package com.example.gamecenter.Games.Senku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.Main.MainActivity;
import com.example.gamecenter.R;

public class SenkuActivity extends AppCompatActivity {

    // Attributes:
    SenkuGame senkuGame;

    // Run:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senku);

        Button menu = findViewById(R.id.senkuMenu);
        menu.setOnClickListener(v -> {
            Intent intent = new Intent(SenkuActivity.this, MainActivity.class);
            startActivity(intent);
        });


        this.senkuGame = new SenkuGame(this);
    }
}