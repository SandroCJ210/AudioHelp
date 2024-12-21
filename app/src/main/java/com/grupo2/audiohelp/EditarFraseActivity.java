package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarFraseActivity extends AppCompatActivity {

    private EditText editTextIdentificador, editTextFrase;
    private Button botonGuardarFrase, botonCancelarFrase;
    private FirebaseFirestore db;
    private String fraseId; // Para identificar qué frase editar

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
        setContentView(R.layout.editar_frase);

        // Aplica la fuente guardada
        aplicarFuenteGuardada();

        // Obtener referencias de los elementos de la UI
        editTextIdentificador = findViewById(R.id.editTextIdentificador);
        editTextFrase = findViewById(R.id.editTextFrase);
        botonGuardarFrase = findViewById(R.id.botonGuardarFrase);
        botonCancelarFrase = findViewById(R.id.botonCancelarFrase);

        // Obtener los datos pasados desde el adaptador
        fraseId = getIntent().getStringExtra("FRASE_TITULO"); // Identificador único
        String texto = getIntent().getStringExtra("FRASE_TEXTO"); // Texto de la frase

        // Configurar los valores iniciales en los campos para que el usario edite
        editTextIdentificador.setText(fraseId);
        editTextFrase.setText(texto);

        // Configurar acciones de los botones
        botonGuardarFrase.setOnClickListener(v -> guardarFrase());
        botonCancelarFrase.setOnClickListener(v -> finish()); // Cerrar la actividad al cancelar
    }

    private void guardarFrase() {


        String nuevoTitulo = editTextIdentificador.getText().toString().trim();
        String nuevoTexto = editTextFrase.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (nuevoTitulo.isEmpty() || nuevoTexto.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Instancia de Firestore
        db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Referencia al documento antiguo usando el fraseId
        DocumentReference docRefAntiguo = db.collection("users")
                .document(userId)  // Reemplaza "user_id" con el ID de usuario adecuado
                .collection("phrases")
                .document(fraseId);  // ID del documento antiguo

        // Eliminar el documento antiguo
        docRefAntiguo.delete()
                .addOnSuccessListener(aVoid -> {
                    // Documento antiguo eliminado exitosamente, crear el nuevo documento
                    Map<String, Object> nuevaFrase = new HashMap<>();
                    nuevaFrase.put("id", nuevoTitulo);
                    nuevaFrase.put("texto", nuevoTexto);  // Nuevo texto de la frase

                    // Crear el nuevo documento con el nuevo ID
                    db.collection("users")
                            .document(userId)  // Reemplaza "user_id" con el ID de usuario adecuado
                            .collection("phrases")
                            .document(nuevoTitulo)  // El nuevo ID del documento
                            .set(nuevaFrase)
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(this, "Frase actualizada exitosamente", Toast.LENGTH_SHORT).show();
                                // Volver al GestorFrasesActivity
                                Intent intent = new Intent(EditarFraseActivity.this, GestorFrasesActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                finish();  // Cerrar la actividad después de guardar
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error al guardar la frase", Toast.LENGTH_SHORT).show();
                                Log.w("Firestore", "Error al crear el nuevo documento", e);
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al eliminar el documento antiguo", Toast.LENGTH_SHORT).show();
                    Log.w("Firestore", "Error al eliminar el documento", e);
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