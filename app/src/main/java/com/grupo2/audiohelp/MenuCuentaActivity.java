package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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
        setContentView(R.layout.cuenta);

        // Aplica la fuente guardada
        aplicarFuenteGuardada();

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
