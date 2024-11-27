package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Locucion_activity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locucion);

        ImageButton btnCerrar = findViewById(R.id.cerrar_transcripcion);
        btnCerrar.setOnClickListener(v -> {
            Intent intent = new Intent(Locucion_activity.this, Menu_Locuciones_activity.class);
            startActivity(intent);
        });

        ImageButton btnFrasesGuardadas = findViewById(R.id.imageButton2);
        btnFrasesGuardadas.setOnClickListener(v -> {
            Intent intent = new Intent(Locucion_activity.this, Gestor_frases_activity.class);
            startActivity(intent);
        });

    }
}
