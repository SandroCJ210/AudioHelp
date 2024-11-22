package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AjustesMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes);

        Button BtonGuardar = findViewById(R.id.btnGuardarConfiguracion);
        BtonGuardar.setOnClickListener(w -> {
            Intent intent = new Intent(AjustesMain.this,OpcionesActivity.class);
            startActivity(intent);
        });
    }
}