package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AgregarFraseActivity extends AppCompatActivity {

    private EditText editTextIdentificador, editTextFrase;
    private Button botonGuardarFrase, botonCancelarFrase;
    private FraseManager fraseManager;

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
        setContentView(R.layout.agregar_frase);

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
}
