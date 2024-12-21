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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth autorizador;
    private EditText registroEmail, registroPassword, registroNombre;
    private Button registroBoton;
    private TextView loginRedirect;

    private FirebaseFirestore db;

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
        setContentView(R.layout.crear_cuenta);

        // Aplica la fuente guardada
        aplicarFuenteGuardada();

        autorizador = FirebaseAuth.getInstance();
        registroNombre = findViewById(R.id.registro_nombre);
        registroEmail = findViewById(R.id.registro_email);
        registroPassword = findViewById(R.id.registro_password);
        registroBoton = findViewById(R.id.registro_btn);
        loginRedirect = findViewById(R.id.loginRedirect);

        db = FirebaseFirestore.getInstance();

        registroBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = registroNombre.getText().toString().trim();
                String user = registroEmail.getText().toString().trim();
                String password = registroPassword.getText().toString().trim();

                if (nombre.isEmpty()) {
                    registroNombre.setError("Ingresa tu nombre.");
                    return;
                }
                if (user.isEmpty()) {
                    registroEmail.setError("Ingresa tu Email.");
                    return;
                }
                if (password.isEmpty()) {
                    registroPassword.setError("Ingrese una contraseña.");
                    return;
                }

                // Crear usuario con Firebase Auth
                autorizador.createUserWithEmailAndPassword(user, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistroActivity.this, "Registro completado.", Toast.LENGTH_SHORT).show();

                            // Crear base de datos de usuario en Firestore
                            String uid = autorizador.getCurrentUser().getUid();
                            createUserInFirestore(uid, nombre, user);  // Pasar nombre y email aquí

                            startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(RegistroActivity.this, "Registro fallido: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
            }
        });
    }

    // Metodo para crear el usuario en Firestore
    private void createUserInFirestore(String uid, String nombre, String email) {
        DocumentReference userDocRef = db.collection("users").document(uid);

        // Crear usuario con un mapa de datos
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("nombre", nombre);
        userMap.put("email", email);
        userMap.put("createdAt", FieldValue.serverTimestamp());

        // Guardar los datos del usuario en Firestore
        userDocRef.set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegistroActivity.this, "Base de datos del usuario creada.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegistroActivity.this, "Error al crear base de datos.", Toast.LENGTH_SHORT).show();
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
