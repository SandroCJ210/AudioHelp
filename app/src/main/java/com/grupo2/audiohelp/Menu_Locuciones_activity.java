package com.grupo2.audiohelp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Menu_Locuciones_activity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_locucion);
        //btnTranscription1

        ImageButton btnLoc = findViewById(R.id.locution_button);
        btnLoc.setOnClickListener(v -> {
            Intent intent = new Intent(Menu_Locuciones_activity.this, Locucion_activity.class);
            startActivity(intent);
        });

        Button btnTrans = findViewById(R.id.btnTranscription1);
        btnTrans.setOnClickListener(v -> {
            Intent intent = new Intent(Menu_Locuciones_activity.this, Menu_Transcripciones_activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        ImageButton btnHistorial = findViewById(R.id.saved_frases_icon);
        btnHistorial.setOnClickListener(w -> {
            Intent intent = new Intent(Menu_Locuciones_activity.this, Gestor_frases_activity.class);
            startActivity(intent);
        });

    }


}
