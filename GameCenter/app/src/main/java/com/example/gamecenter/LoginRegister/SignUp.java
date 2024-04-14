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

public class SignUp extends AppCompatActivity {

    // Attributes:
    EditText userField;
    EditText passwordField;
    EditText passwordConfirmField;
    MaterialButton login;
    MaterialButton register;

    // Run:
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Variable assignation:
        userField = findViewById(R.id.user_field);
        passwordField = findViewById(R.id.password_field);
        passwordConfirmField = findViewById(R.id.passwordConfirm_field);

        login = findViewById(R.id.login_Button);
        register = findViewById(R.id.register_Button);


        // Listeners:
        login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
        });

        register.setOnClickListener(v -> {

            // Extracción de credenciales:
            String user = String.valueOf(userField.getText());
            String password = String.valueOf(passwordField.getText());
            String passwordConfirm = String.valueOf(passwordConfirmField.getText());

            // Manejo de campos vacíos:
            if (TextUtils.isEmpty(user)) {
                Toast.makeText(SignUp.this, "User field is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(SignUp.this, "Password field is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(passwordConfirm)) {
                Toast.makeText(SignUp.this, "Confirm password field is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardado de credenciales:
            SharedPreferences sharedPreferences = getSharedPreferences("Users", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (password.equals(passwordConfirm)) {

                editor.putString(user, password);
                editor.apply();

                editor.putString("ActualUser", user);
                editor.apply();

                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(SignUp.this, "Passwords don't coincide", Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }
}