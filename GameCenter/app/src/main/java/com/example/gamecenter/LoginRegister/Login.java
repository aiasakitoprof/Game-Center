package com.example.gamecenter.LoginRegister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.Main.MainActivity;
import com.example.gamecenter.R;
import com.google.android.material.button.MaterialButton;

public class Login extends AppCompatActivity {

    // Attributes:
    EditText userField;
    EditText passwordField;
    MaterialButton login;
    MaterialButton register;

    // Run:
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Variable assignation:
        userField = findViewById(R.id.user_field);
        passwordField = findViewById(R.id.password_field);

        login = findViewById(R.id.login_Button);
        register = findViewById(R.id.register_Button);


        // Listeners:
        login.setOnClickListener(v -> {

            // Extracción de credenciales:
            String user = String.valueOf(userField.getText());
            String password = String.valueOf(passwordField.getText());

            // Manejo de campos vacíos:
            if (TextUtils.isEmpty(user)) {
                Toast.makeText(Login.this, "User field is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(Login.this, "Password field is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Extracción y validación de credenciales:
            SharedPreferences sharedPreferences = getSharedPreferences("Users", MODE_PRIVATE);
            String savedPassword = sharedPreferences.getString(user, null);

            if (password.equals(savedPassword)) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ActualUser", user);
                editor.apply();

                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(Login.this, "Wrong password or user", Toast.LENGTH_SHORT).show();
                return;
            }


        });

        register.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });
    }
}