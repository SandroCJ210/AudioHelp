package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Ajustes_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes);

        Button BtonGuardar = findViewById(R.id.saveConfigButton);
        BtonGuardar.setOnClickListener(w -> {
            Intent intent = new Intent(Ajustes_activity.this, Menu_Opciones_activity.class);
            startActivity(intent);
        });
    }
}