package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class LocucionActivity_2 extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locution_main_2);

        ImageButton btnCerrar = findViewById(R.id.closeLocution);
        btnCerrar.setOnClickListener(v -> {
            Intent intent = new Intent(LocucionActivity_2.this, LocucionActivity.class);
            startActivity(intent);
        });

        ImageButton btnFrasesGuardadas = findViewById(R.id.imageButton2);
        btnFrasesGuardadas.setOnClickListener(v -> {
            Intent intent = new Intent(LocucionActivity_2.this, FrasesGuardadasActivity.class);
            startActivity(intent);
        });

    }
}
