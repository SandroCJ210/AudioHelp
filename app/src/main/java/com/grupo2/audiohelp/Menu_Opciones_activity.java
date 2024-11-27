package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Menu_Opciones_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_opciones);

        LinearLayout btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion);
        LinearLayout btnConfiguracion = findViewById(R.id.btn_configuracion);

        btnIniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(Menu_Opciones_activity.this, Iniciar_Sesion_activity.class);
            startActivity(intent);
        });

        btnConfiguracion.setOnClickListener(v -> {
            Intent intent = new Intent(Menu_Opciones_activity.this, Ajustes_activity.class);
            startActivity(intent);
        });

        Button btnRetroceder = findViewById(R.id.BtnTranscription3);
        btnRetroceder.setOnClickListener(v -> {
            Intent intent = new Intent(Menu_Opciones_activity.this, Menu_Transcripciones_activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

    }



}