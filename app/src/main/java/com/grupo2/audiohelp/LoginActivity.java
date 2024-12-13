package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth autorizador;
    private EditText loginEmail, loginPassword;
    private TextView registroRedirect;
    private Button loginBoton;
    private ImageButton btnRetroceder;


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
        setContentView(R.layout.inicio_sesion);

        btnRetroceder = findViewById(R.id.regresarOpciones);
        autorizador = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginBoton = findViewById(R.id.login_btn);
        registroRedirect = findViewById(R.id.registroRedirect);

        loginBoton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(!password.isEmpty()){
                        autorizador.signInWithEmailAndPassword(email,password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(LoginActivity.this, "Ingreso correcto.", Toast.LENGTH_SHORT).show();

                                        // Obtener el UID del usuario logueado
                                        String uid = autorizador.getCurrentUser().getUid();

                                        // Acceder a la colección de usuarios en Firestore
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("users").document(uid)
                                                .get()
                                                .addOnSuccessListener(documentSnapshot -> {
                                                    if (documentSnapshot.exists()) {
                                                        // Obtener el nombre del usuario
                                                        String nombre = documentSnapshot.getString("nombre");

                                                        // Pasar el nombre a la siguiente actividad
                                                        Intent intent = new Intent(LoginActivity.this, MenuOpcionesActivity.class);
                                                        intent.putExtra("nombre_usuario", nombre);  // Pasar el nombre
                                                        startActivity(intent);
                                                        finish();  // Finalizar LoginActivity
                                                    } else {
                                                        Toast.makeText(LoginActivity.this, "Usuario no encontrado en la base de datos.", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(LoginActivity.this, "Error al obtener los datos del usuario.", Toast.LENGTH_SHORT).show();
                                                });
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "Inicio de sesión fallido.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else{
                        loginPassword.setError("La contraseña no puede estar vacía.");
                    }
                }else if(email.isEmpty()){
                    loginEmail.setError("El Email no puede estar vacío.");
                } else{
                    loginEmail.setError("Ingrese un Email válido.");
                }
            }
        });

        registroRedirect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(LoginActivity.this,RegistroActivity.class));
            }
        });

        btnRetroceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(LoginActivity.this, MenuOpcionesActivity.class));
            }
        });

    }

}