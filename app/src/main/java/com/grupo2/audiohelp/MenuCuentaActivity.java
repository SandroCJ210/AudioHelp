package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuCuentaActivity extends AppCompatActivity {

    private ImageButton btnVolver;
    private Button btnCerrarSesion;
    private Button btnCambiarContrasena;
    private FirebaseAuth autorizador;
    private FirebaseUser currentUser;
    private TextView tvNombreUsuario;
    private EditText etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuenta);

        autorizador = FirebaseAuth.getInstance();
        currentUser = autorizador.getCurrentUser();

        // Referencia a los elementos de la interfaz
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        EditText etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etpass); // Campo para la nueva contraseña
        btnVolver = findViewById(R.id.btnVolver);
        btnCambiarContrasena = findViewById(R.id.btnCambiarContrasena);

        // Verificar si el usuario está autenticado
        if (currentUser == null) {
            Toast.makeText(MenuCuentaActivity.this, "Tu cuenta ha sido eliminada o no existe.", Toast.LENGTH_SHORT).show();
            autorizador.signOut();
            startActivity(new Intent(MenuCuentaActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Mostrar el correo electrónico
        String email = currentUser.getEmail();
        if (email != null) {
            etEmail.setText(email);
        } else {
            etEmail.setText("Correo no disponible");
        }

        // Obtener el nombre del usuario desde Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = currentUser.getUid();

        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombre = documentSnapshot.getString("nombre");
                        if (nombre != null && !nombre.isEmpty()) {
                            tvNombreUsuario.setText(nombre);
                        } else {
                            tvNombreUsuario.setText("Nombre no disponible");
                        }
                    } else {
                        tvNombreUsuario.setText("Usuario no encontrado");
                    }
                })
                .addOnFailureListener(e -> {
                    tvNombreUsuario.setText("Error al obtener el nombre.");
                });

        // Botón para cambiar la contraseña
        btnCambiarContrasena.setOnClickListener(v -> {
            String nuevaContrasena = etPass.getText().toString().trim();

            if (TextUtils.isEmpty(nuevaContrasena)) {
                Toast.makeText(MenuCuentaActivity.this, "Por favor, ingresa una nueva contraseña.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nuevaContrasena.length() < 6) {
                Toast.makeText(MenuCuentaActivity.this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mostrar un cuadro de diálogo para pedir la contraseña actual
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Reautenticación necesaria");
            builder.setMessage("Ingresa tu contraseña actual para cambiar la contraseña.");
            final EditText inputPassword = new EditText(this);
            inputPassword.setHint("Contraseña actual");
            builder.setView(inputPassword);

            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                String passwordActual = inputPassword.getText().toString().trim();
                if (TextUtils.isEmpty(passwordActual)) {
                    Toast.makeText(MenuCuentaActivity.this, "Debes ingresar tu contraseña actual.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Reautenticación con el correo del usuario y su contraseña actual
                AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), passwordActual);
                currentUser.reauthenticate(credential)
                        .addOnSuccessListener(unused -> {
                            // Si la reautenticación es exitosa, cambiar la contraseña
                            currentUser.updatePassword(nuevaContrasena)
                                    .addOnSuccessListener(unused1 -> {
                                        Toast.makeText(MenuCuentaActivity.this, "Contraseña actualizada exitosamente.", Toast.LENGTH_SHORT).show();
                                        etPass.setText(""); // Limpiar el campo después de actualizar la contraseña
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(MenuCuentaActivity.this, "Error al actualizar la contraseña: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(MenuCuentaActivity.this, "Reautenticación fallida: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.show();
        });

        // Botón para cerrar sesión
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(v -> {
            autorizador.signOut();
            Toast.makeText(MenuCuentaActivity.this, "Sesión cerrada.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MenuCuentaActivity.this, LoginActivity.class));
            finish();
        });

        // Botón para volver al menú anterior
        btnVolver.setOnClickListener(v -> {
            Intent intent = new Intent(MenuCuentaActivity.this, MenuOpcionesActivity.class);
            startActivity(intent);
        });
    }
}
