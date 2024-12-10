package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class MenuCuentaActivity extends AppCompatActivity {

    private Button btnVolver;
    private Button btnCerrarSesion;
    private FirebaseAuth autorizador;
    private FirebaseUser currentUser;
    private Button btnUpdateProfile;
    private TextView tvNombreUsuario;  // Este será el TextView donde mostrarás el nombre

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuenta);

        autorizador = FirebaseAuth.getInstance();
        currentUser = autorizador.getCurrentUser();

        // Referencia a los elementos de la interfaz
        tvNombreUsuario = findViewById(R.id.etName);  // Asegúrate de tener un TextView con este ID
        EditText etEmail = findViewById(R.id.etEmail);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnVolver = findViewById(R.id.salir_cuenta_1);

        // Mostrar el correo electrónico
        if (currentUser != null) {
            String email = currentUser.getEmail();
            etEmail.setText(email);

            // Obtener el nombre del usuario desde Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String uid = currentUser.getUid();

            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nombre = documentSnapshot.getString("nombre");
                            // Mostrar el nombre del usuario en el TextView
                            tvNombreUsuario.setText(nombre); // Puedes personalizar el mensaje
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Manejar el error si no se encuentra el documento
                        tvNombreUsuario.setText("Error al obtener el nombre.");
                    });
        }

        // Lógica para actualizar datos del perfil
        btnUpdateProfile.setOnClickListener(v -> {
            // Aquí agregarás la lógica de actualización de datos del perfil
        });

        // Botón para cerrar sesión
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(v -> {
            // Cerrar sesión de Firebase
            autorizador.signOut();
            // Mensaje de confirmación
            Toast.makeText(MenuCuentaActivity.this, "Sesión cerrada.", Toast.LENGTH_SHORT).show();
            // Redirigir a la pantalla de login
            startActivity(new Intent(MenuCuentaActivity.this, LoginActivity.class));
            finish(); // Finaliza la actividad actual
        });

        // Volver al menú anterior
        btnVolver.setOnClickListener(v -> {
            Intent intent = new Intent(MenuCuentaActivity.this, MenuOpcionesActivity.class);
            startActivity(intent);
        });
    }
}
