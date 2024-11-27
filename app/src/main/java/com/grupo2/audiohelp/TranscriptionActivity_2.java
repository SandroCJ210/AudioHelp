package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class TranscriptionActivity_2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transcripcion);

        ImageButton btnLoc = findViewById(R.id.frases_guardadas);
        btnLoc.setOnClickListener(v -> {
            Intent intent = new Intent(TranscriptionActivity_2.this, LocucionActivity_2.class);
            startActivity(intent);
        });

        ImageButton btnCerrar = findViewById(R.id.cerrar_transcripcion);
        btnCerrar.setOnClickListener(v -> {
            Intent intent = new Intent(TranscriptionActivity_2.this, TranscriptionActivity.class);
            startActivity(intent);
        });
    }
}
