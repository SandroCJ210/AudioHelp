package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class AgregarFraseActivity extends AppCompatActivity {

    private EditText editTextIdentificador, editTextFrase;
    private Button botonGuardarFrase, botonCancelarFrase;
    private FraseManager fraseManager;

    private  SharedPreferences sharedPreferences;

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
        setContentView(R.layout.agregar_frase);

        // Aplica la fuente guardada
        aplicarFuenteGuardada();

        // Inicializar FraseManager
        fraseManager = new FraseManager();

        // Enlazar vistas
        editTextIdentificador = findViewById(R.id.editTextIdentificador);
        editTextFrase = findViewById(R.id.editTextFrase);
        botonGuardarFrase = findViewById(R.id.botonGuardarFrase);
        botonCancelarFrase = findViewById(R.id.botonCancelarFrase);

        // Manejar evento de guardar
        botonGuardarFrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String identificador = editTextIdentificador.getText().toString().trim();
                String textoFrase = editTextFrase.getText().toString().trim();

                if (identificador.isEmpty() || textoFrase.isEmpty()) {
                    Toast.makeText(AgregarFraseActivity.this, "Completa todos los campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                fraseManager.agregarFrase(identificador, textoFrase)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(AgregarFraseActivity.this, "Frase guardada correctamente.", Toast.LENGTH_SHORT).show();

                            // Volver al GestorFrasesActivity
                            Intent intent = new Intent(AgregarFraseActivity.this, GestorFrasesActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            finish(); // Cierra la actividad
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(AgregarFraseActivity.this, "Error al guardar la frase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // Manejar evento de cancelar
        botonCancelarFrase.setOnClickListener(view -> finish());
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
