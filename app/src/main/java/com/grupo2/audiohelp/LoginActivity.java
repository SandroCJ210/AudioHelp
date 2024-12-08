package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        Button BtonLocucion = findViewById(R.id.Registrarse);
        BtonLocucion.setOnClickListener(w -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        ImageButton btnRetroceder = findViewById(R.id.imageButton);
        btnRetroceder.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Menu_Opciones_activity.class);
            startActivity(intent);
        });

    }

}