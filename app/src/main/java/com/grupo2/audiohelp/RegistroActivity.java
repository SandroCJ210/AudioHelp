package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth autorizador;
    private EditText registroEmail,registroPassword;
    private Button registroBoton;
    private TextView loginRedirect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta);

        autorizador = FirebaseAuth.getInstance();
        registroEmail = findViewById(R.id.registro_email);
        registroPassword = findViewById(R.id.registro_password);
        registroBoton = findViewById(R.id.registro_btn);
        loginRedirect = findViewById(R.id.loginRedirect);

        registroBoton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String user = registroEmail.getText().toString().trim();
                String password = registroPassword.getText().toString().trim();

                if(user.isEmpty()){
                    registroEmail.setError("Ingresa un Email valido.");
                }

                if(password.isEmpty()){
                    registroEmail.setError("Ingrese una contraseña valida.");
                } else{
                    autorizador.createUserWithEmailAndPassword(user, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task){
                            if(task.isSuccessful()){
                                Toast.makeText(RegistroActivity.this, "Registro completado.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                            } else{
                                Toast.makeText(RegistroActivity.this, "Registro fallido.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        });

        loginRedirect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
            }
        });
    }
}