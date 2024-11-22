package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class OpcionesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opciones);

        LinearLayout btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion);
        LinearLayout btnConfiguracion = findViewById(R.id.btn_configuracion);

        btnIniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(OpcionesActivity.this,InicioSesionActivity.class);
            startActivity(intent);
        });

        btnConfiguracion.setOnClickListener(v -> {
            Intent intent = new Intent(OpcionesActivity.this,AjustesMain.class);
            startActivity(intent);
        });

        ImageButton btnRetroceder = findViewById(R.id.imageButton4);
        btnRetroceder.setOnClickListener(v -> {
            Intent intent = new Intent(OpcionesActivity.this, TranscriptionActivity.class);
            startActivity(intent);
        });

    }



}