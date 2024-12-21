package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import java.io.IOException;

public class MenuTranscripcionesActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Cargar tema antes de llamar a super.onCreate()
        sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            setTheme(R.style.DarkTheme); // Tema oscuro
        } else {
            setTheme(R.style.LightTheme); // Tema claro
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_transcripcion);

        // Aplica la fuente guardada
        aplicarFuenteGuardada();

        ImageButton btnTrans = findViewById(R.id.locution_button);
        btnTrans.setOnClickListener(v -> {
            Intent intent = new Intent(MenuTranscripcionesActivity.this, TranscripcionActivity.class);
            startActivity(intent);
        });

        ImageButton btnHistorial = findViewById(R.id.saved_transcriptions_icon);
        btnHistorial.setOnClickListener(w -> {
            Intent intent = new Intent(MenuTranscripcionesActivity.this, GestorTranscripcionesActivity.class);
            startActivity(intent);
        });
        Button btnMenu = findViewById(R.id.BtnOpciones);
        btnMenu.setOnClickListener(w -> {
            Intent intent = new Intent(MenuTranscripcionesActivity.this, MenuOpcionesActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        Button BtonLocucion = findViewById(R.id.BtnLocucion1);
        BtonLocucion.setOnClickListener(w -> {
            Intent intent = new Intent(MenuTranscripcionesActivity.this, MenuLocucionesActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void aplicarFuente(String rutaFuente) {
        try {
            // Extraer solo el nombre del archivo de la ruta completa
            String nombreFuente = rutaFuente.substring(rutaFuente.lastIndexOf("/") + 1, rutaFuente.lastIndexOf("."));

            // Cargar la fuente desde res/font
            Typeface typeface = ResourcesCompat.getFont(this, getResources().getIdentifier(nombreFuente, "font", getPackageName()));

            // Aplica la fuente seleccionada a todos los TextViews de la actividad
            View rootView = findViewById(android.R.id.content);
            aplicarFuenteEnVistas(rootView, typeface);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar la fuente", Toast.LENGTH_SHORT).show();
        }
    }


    private void aplicarFuenteEnVistas(View view, Typeface typeface) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                aplicarFuenteEnVistas(group.getChildAt(i), typeface);
            }
        }
    }

    private void aplicarFuenteGuardada() {
        try {
            // Lee la fuente guardada desde SharedPreferences
            String fuenteGuardada = sharedPreferences.getString("FuenteSeleccionada", "caskaydia_mono_nerd_font"); // Ajusta el nombre de la fuente

            // Aplica la fuente
            Typeface typeface = ResourcesCompat.getFont(this, getResources().getIdentifier(fuenteGuardada, "font", getPackageName()));
            aplicarFuenteEnVistas(findViewById(android.R.id.content), typeface);
        } catch (Exception e) {
            e.printStackTrace();
            // Si hay un error, aplica la fuente predeterminada
            Typeface defaultTypeface = ResourcesCompat.getFont(this, R.font.caskaydia_mono_nerd_font);
            aplicarFuenteEnVistas(findViewById(android.R.id.content), defaultTypeface);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Aplica la fuente guardada al inicio
        aplicarFuenteGuardada();
    }
}