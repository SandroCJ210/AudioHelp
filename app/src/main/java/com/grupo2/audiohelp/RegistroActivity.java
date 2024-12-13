package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta);

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
}
