package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TransGuardadaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestor_transcripciones);

        Button BtonAgregar = findViewById(R.id.newTranscriptionButton);
        BtonAgregar.setOnClickListener(w -> {
            Intent intent = new Intent(TransGuardadaActivity.this,TranscriptionActivity.class);
            startActivity(intent);
        });
    }
}