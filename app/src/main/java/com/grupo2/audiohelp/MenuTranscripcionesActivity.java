package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MenuTranscripcionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_transcripcion);

        ImageButton btnTrans = findViewById(R.id.locution_button);
        btnTrans.setOnClickListener(v -> {
            Intent intent = new Intent(MenuTranscripcionesActivity.this, TranscripcionActivity.class);
            startActivity(intent);
        });
        ImageButton btnHistorial = findViewById(R.id.saved_transcriptions_icon);
        btnHistorial.setOnClickListener(w -> {
            Intent intent = new Intent(MenuTranscripcionesActivity.this, GestorTranscripcionesActivity.class);
            startActivity(intent);
        });
        Button btnMenu = findViewById(R.id.BtnOpciones);
        btnMenu.setOnClickListener(w -> {
            Intent intent = new Intent(MenuTranscripcionesActivity.this, Menu_Opciones_activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        Button BtonLocucion = findViewById(R.id.BtnLocucion1);
        BtonLocucion.setOnClickListener(w -> {
            Intent intent = new Intent(MenuTranscripcionesActivity.this, MenuLocucionesActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}