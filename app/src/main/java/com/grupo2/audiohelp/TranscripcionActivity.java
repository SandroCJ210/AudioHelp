package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TranscripcionActivity extends AppCompatActivity {
    private static final int SPEECH_REQUEST_CODE = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transcripcion);
        startSpeechToText();


        ImageButton btnCerrar = findViewById(R.id.Retroceder);
        btnCerrar.setOnClickListener(v -> {
            Intent IT = new Intent(TranscripcionActivity.this, MenuTranscripcionesActivity.class);
            startActivity(IT);
        });
    }
    private void startSpeechToText(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            startActivityForResult(intent,SPEECH_REQUEST_CODE);
        }else{
            Toast.makeText(this,"El reconocimiento de voz no está disponible.",Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Obtén los resultados del reconocimiento de voz
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String recognizedText = results.get(0); // Primer resultado reconocido
                Toast.makeText(this, "Texto reconocido: " + recognizedText, Toast.LENGTH_LONG).show();
                // Aquí puedes manejar el texto reconocido, por ejemplo, mostrarlo en una vista o guardarlo
            }
        }
    }
}


