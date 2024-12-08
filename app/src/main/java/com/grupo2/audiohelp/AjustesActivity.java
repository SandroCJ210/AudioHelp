package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.widget.ImageButton;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class AjustesActivity extends AppCompatActivity {
    private SwitchCompat switchTheme;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MENSAJE", "onCreate ejecutado");
        // Cargar tema desde SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.ajustes);
        Log.i("MENSAJE", "TODO BIEN POR AHORA");
        // Configurar el Switch
        switchTheme = findViewById(R.id.switch_tema);
        if (switchTheme == null) {
            Log.e("MENSAJE", "El Switch no se encontró en el layout");
        } else {
            Log.d("MENSAJE", "El Switch fue encontrado correctamente");
        }
        switchTheme.setChecked(isDarkMode);
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor = sharedPreferences.edit();
            editor.putBoolean("isDarkMode", isChecked);
            editor.apply();
            // Reiniciar actividad para aplicar el tema
            recreate();
        });

        // Referencia al ImageButton
        ImageButton imageButtonAtras = findViewById(R.id.ButtonAtras);

        // Configuración del OnClickListener para el ImageButton
        imageButtonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un Intent para regresar al MenuOpcionesActivity
                Intent intent = new Intent(AjustesActivity.this, Menu_Opciones_activity.class);
                startActivity(intent);  // Inicia la actividad MenuOpcionesActivity
                finish();  // Finaliza la actividad actual (AjustesActivity)
            }
        });


    }
}

