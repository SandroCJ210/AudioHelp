package com.grupo2.audiohelp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class TranscriptionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_transcripcion);

        ImageButton btnTrans = findViewById(R.id.locution_button);
        btnTrans.setOnClickListener(v -> {
            Intent intent = new Intent(TranscriptionActivity.this,TranscriptionActivity_2.class);
            startActivity(intent);
        });
        ImageButton btnHistorial = findViewById(R.id.saved_transcriptions_icon);
        btnHistorial.setOnClickListener(w -> {
            Intent intent = new Intent(TranscriptionActivity.this,TransGuardadaActivity.class);
            startActivity(intent);
        });
        ImageButton btnMenu = findViewById(R.id.menu_icon);
        btnMenu.setOnClickListener(w -> {
            Intent intent = new Intent(TranscriptionActivity.this,OpcionesActivity.class);
            startActivity(intent);
        });
        Button BtonLocucion = findViewById(R.id.BtnLocucion1);
        BtonLocucion.setOnClickListener(w -> {
            Intent intent = new Intent(TranscriptionActivity.this,LocucionActivity.class);
            startActivity(intent);
        });
    }
}
