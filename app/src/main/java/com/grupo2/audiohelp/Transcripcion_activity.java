package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Transcripcion_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transcripcion);

        ImageButton btnCerrar = findViewById(R.id.cerrar_transcripcion);
        btnCerrar.setOnClickListener(v -> {
            Intent intent = new Intent(Transcripcion_activity.this, Menu_Transcripciones_activity.class);
            startActivity(intent);
        });
    }
}
