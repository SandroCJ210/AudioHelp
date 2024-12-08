package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class loginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        Button BtonLocucion = findViewById(R.id.Registrarse);
        BtonLocucion.setOnClickListener(w -> {
            Intent intent = new Intent(loginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        ImageButton btnRetroceder = findViewById(R.id.imageButton);
        btnRetroceder.setOnClickListener(v -> {
            Intent intent = new Intent(loginActivity.this, Menu_Opciones_activity.class);
            startActivity(intent);
        });

    }

}