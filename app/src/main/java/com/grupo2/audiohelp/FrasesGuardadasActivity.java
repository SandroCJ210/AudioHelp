package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class FrasesGuardadasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frases_guardadas);

        ImageButton btnCerrar = findViewById(R.id.buttonClose);
        btnCerrar.setOnClickListener(v -> {
            Intent intent = new Intent(FrasesGuardadasActivity.this, LocucionActivity.class);
            startActivity(intent);
        });
    }
}