package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class TranscripcionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transcripcion);

        ImageButton btnCerrar = findViewById(R.id.Retroceder);
        btnCerrar.setOnClickListener(v -> {
            Intent intent = new Intent(TranscripcionActivity.this, MenuTranscripcionesActivity.class);
            startActivity(intent);
        });
    }
}
