package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuCuentaActivity extends AppCompatActivity {
    private Button btnVolver;
    private FirebaseAuth autorizador;
    private FirebaseUser currentUser;
    private Button btnUpdateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuenta);

        autorizador = FirebaseAuth.getInstance();
        currentUser = autorizador.getCurrentUser();

        // Mostrar información del usuario
        if (currentUser != null) {
            String email = currentUser.getEmail();
            EditText etEmail = findViewById(R.id.etEmail);
            etEmail.setText(email);
        }

        // Implementar lógica para actualizar datos del perfil
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnUpdateProfile.setOnClickListener(v -> {
            // lógica de actualización de datos
        });

        btnVolver = findViewById(R.id.salir_cuenta_1);
        btnVolver.setOnClickListener(v -> {
            Intent intent = new Intent(MenuCuentaActivity.this, MenuOpcionesActivity.class);
            startActivity(intent);
        });
    }
}
