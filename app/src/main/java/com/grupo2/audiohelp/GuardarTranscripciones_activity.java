package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GuardarTranscripciones_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestor_transcripciones);

        Button BtonAgregar = findViewById(R.id.boton_AñadirTrans);
        BtonAgregar.setOnClickListener(w -> {
            Intent intent = new Intent(GuardarTranscripciones_activity.this, Menu_Transcripciones_activity.class);
            startActivity(intent);
        });
    }
}
