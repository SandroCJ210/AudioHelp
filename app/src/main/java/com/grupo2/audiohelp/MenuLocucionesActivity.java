package com.grupo2.audiohelp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MenuLocucionesActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Cargar tema antes de llamar a super.onCreate()
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            setTheme(R.style.DarkTheme); // Tema oscuro
        } else {
            setTheme(R.style.LightTheme); // Tema claro
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_locucion);
        //btnTranscription1

        ImageButton btnLoc = findViewById(R.id.locution_button);

        btnLoc.setOnClickListener(v -> {
            Intent intent = new Intent(MenuLocucionesActivity.this, LocucionActivity.class);
            startActivity(intent);
        });

        Button btnTrans = findViewById(R.id.btnTranscription1);
        btnTrans.setOnClickListener(v -> {
            Intent intent = new Intent(MenuLocucionesActivity.this, MenuTranscripcionesActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        ImageButton btnHistorial = findViewById(R.id.saved_frases_icon);
        btnHistorial.setOnClickListener(w -> {
            Intent intent = new Intent(MenuLocucionesActivity.this, GestorFrasesActivity.class);
            startActivity(intent);
        });

    }


}
