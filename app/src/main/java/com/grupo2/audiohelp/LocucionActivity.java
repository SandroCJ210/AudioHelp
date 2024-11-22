package com.grupo2.audiohelp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class LocucionActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locution_main);
        //btnTranscription1

        ImageButton btnLoc = findViewById(R.id.locution_button);
        btnLoc.setOnClickListener(v -> {
            Intent intent = new Intent(LocucionActivity.this,LocucionActivity_2.class);
            startActivity(intent);
        });

        Button btnTrans = findViewById(R.id.btnTranscription1);
        btnTrans.setOnClickListener(v -> {
            Intent intent = new Intent(LocucionActivity.this,TranscriptionActivity.class);
            startActivity(intent);
        });
        ImageButton btnMenu = findViewById(R.id.menu_icon);
        btnMenu.setOnClickListener(w -> {
            Intent intent = new Intent(LocucionActivity.this,OpcionesActivity.class);
            startActivity(intent);
        });
        ImageButton btnHistorial = findViewById(R.id.saved_frases_icon);
        btnHistorial.setOnClickListener(w -> {
            Intent intent = new Intent(LocucionActivity.this,FrasesGuardadasActivity.class);
            startActivity(intent);
        });

    }


}
