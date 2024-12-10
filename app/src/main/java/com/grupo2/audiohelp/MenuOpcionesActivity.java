package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuOpcionesActivity extends AppCompatActivity {
    private LinearLayout btnIniciarSesion;
    private LinearLayout btnConfiguracion;
    private Button btnRetroceder;
    private LinearLayout btnTutorial;
    private FirebaseAuth autorizador;
    private FirebaseUser currentUser;
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

        btnIniciarSesion = findViewById(R.id.boton_cuenta);
        btnConfiguracion = findViewById(R.id.boton_config);
        btnRetroceder = findViewById(R.id.BtnTranscription3);
        btnTutorial = findViewById(R.id.boton_tutorial);

        autorizador = FirebaseAuth.getInstance();
        currentUser = autorizador.getCurrentUser();

        btnIniciarSesion.setOnClickListener(v -> {
            if (currentUser != null) {
                // Usuario autenticado: Ir a cuenta.xml
                Intent intent = new Intent(MenuOpcionesActivity.this, MenuCuentaActivity.class);
                startActivity(intent);
            } else {
                // Usuario no autenticado: Ir a registrar.xml
                Intent intent = new Intent(MenuOpcionesActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        // Opciones ----> Inicio sesión
        /*btnIniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(MenuOpcionesActivity.this, LoginActivity.class);
            startActivity(intent);
        });*/

        // Opciones ----> Ajustes
        btnConfiguracion.setOnClickListener(v -> {
            Intent intent = new Intent(MenuOpcionesActivity.this, AjustesActivity.class);
            startActivity(intent);
        });

        // Opciones ----> Menu de Transcripciones
        btnRetroceder.setOnClickListener(v -> {
            Intent intent = new Intent(MenuOpcionesActivity.this, MenuTranscripcionesActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Opciones ----> Tutorial
        btnTutorial.setOnClickListener(v -> {
            String url = "https://youtu.be/k10xBs6--Q8?si=F0GusH0jYyVGM9ll&t=209";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });


    }



}