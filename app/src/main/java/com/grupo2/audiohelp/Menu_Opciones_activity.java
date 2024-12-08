package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Menu_Opciones_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cargar tema desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            setTheme(R.style.DarkTheme);  // Aplicar tema oscuro
        } else {
            setTheme(R.style.LightTheme);  // Aplicar tema claro
        }

        setContentView(R.layout.menu_opciones);

        LinearLayout btnIniciarSesion = findViewById(R.id.boton_cuenta);
        LinearLayout btnConfiguracion = findViewById(R.id.boton_config);
        Button btnRetroceder = findViewById(R.id.BtnTranscription3);
        LinearLayout btnTutorial = findViewById(R.id.boton_tutorial);

        // Opciones ----> Inicio sesión
        btnIniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(Menu_Opciones_activity.this, loginActivity.class);
            startActivity(intent);
        });

        // Opciones ----> Ajustes
        btnConfiguracion.setOnClickListener(v -> {
            Intent intent = new Intent(Menu_Opciones_activity.this, AjustesActivity.class);
            startActivity(intent);
        });

        // Opciones ----> Menu de Transcripciones
        btnRetroceder.setOnClickListener(v -> {
            Intent intent = new Intent(Menu_Opciones_activity.this, MenuTranscripcionesActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });


        btnTutorial.setOnClickListener(v -> {
            String url = "https://youtu.be/k10xBs6--Q8?si=F0GusH0jYyVGM9ll&t=209";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });


    }



}