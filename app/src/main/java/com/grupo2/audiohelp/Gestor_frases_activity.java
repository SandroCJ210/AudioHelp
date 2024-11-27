package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Gestor_frases_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestor_frases);

        ImageButton btnCerrar = findViewById(R.id.buttonClose);
        btnCerrar.setOnClickListener(v -> {
            Intent intent = new Intent(Gestor_frases_activity.this, Menu_Locuciones_activity.class);
            startActivity(intent);
        });
    }
}