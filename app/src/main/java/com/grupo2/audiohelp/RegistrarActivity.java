package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta);

        ImageButton btnRetroceder = findViewById(R.id.imageButton);
        btnRetroceder.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrarActivity.this, InicioSesionActivity.class);
            startActivity(intent);
        });

    }

}