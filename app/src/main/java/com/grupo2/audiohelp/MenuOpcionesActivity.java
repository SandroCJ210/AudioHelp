package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuOpcionesActivity extends AppCompatActivity {
    private LinearLayout btnIniciarSesion;
    private LinearLayout btnConfiguracion;
    private Button btnRetroceder;
    private LinearLayout btnTutorial;
    private FirebaseAuth autorizador;
    private FirebaseUser currentUser;

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cargar tema desde SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            setTheme(R.style.DarkTheme);  // Aplicar tema oscuro
        } else {
            setTheme(R.style.LightTheme);  // Aplicar tema claro
        }

        setContentView(R.layout.menu_opciones);

        // Aplica la fuente guardada
        aplicarFuenteGuardada();

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