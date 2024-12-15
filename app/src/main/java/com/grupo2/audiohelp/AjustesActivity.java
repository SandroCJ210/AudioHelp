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
}

