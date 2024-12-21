package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class AjustesActivity extends AppCompatActivity {
    private SwitchCompat switchTheme;
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
        setContentView(R.layout.ajustes);

        // Aplica la fuente guardada
        aplicarFuenteGuardada();

        Log.i("MENSAJE", "Activity Ajustes creada");

        // Configurar el Switch
        switchTheme = findViewById(R.id.switch_tema);
        if (switchTheme == null) {
            Log.e("MENSAJE", "El Switch no se encontró en el layout");
        } else {
            Log.d("MENSAJE", "El Switch fue encontrado correctamente");
        }

        switchTheme.setChecked(isDarkMode);
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isDarkMode", isChecked);
            editor.apply();

            // Reiniciar actividad para aplicar el tema
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        });

        // Configurar el botón de cambiar fuente
        Button btnCambiarFuente = findViewById(R.id.buttonCambiarFuente);
        btnCambiarFuente.setOnClickListener(v -> mostrarSelectorDeFuentes());

        // Configuración del botón de atrás
        ImageButton imageButtonAtras = findViewById(R.id.ButtonAtras);
        imageButtonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar de regreso a MenuOpcionesActivity
                Intent intent = new Intent(AjustesActivity.this, MenuOpcionesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void mostrarSelectorDeFuentes() {
        // Lista de nombres de fuentes y sus rutas
        final String[] fuentes = {"Caskaydia Mono", "Comic Neue", "Monserrat", "Roboto"};
        final String[] rutas = {"res/font/caskaydia_mono_nerd_font.ttf", "res/font/comic_neue.ttf", "res/font/montserrat.ttf","res/font/roboto.ttf"};

        new AlertDialog.Builder(this)
                .setTitle("Selecciona una fuente")
                .setItems(fuentes, (dialog, which) -> {
                    // Guarda la fuente seleccionada
                    guardarFuenteSeleccionada(rutas[which]);

                    // Aplica la fuente seleccionada
                    aplicarFuente(rutas[which]);

                    Toast.makeText(this, "Fuente cambiada", Toast.LENGTH_SHORT).show();
                })
                .show();
    }


    private void guardarFuenteSeleccionada(String rutaFuente) {
        // Extraer solo el nombre del archivo (sin la ruta ni la extensión)
        String nombreFuente = rutaFuente.substring(rutaFuente.lastIndexOf("/") + 1, rutaFuente.lastIndexOf("."));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FuenteSeleccionada", nombreFuente); // Guardar solo el nombre
        editor.apply();
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

