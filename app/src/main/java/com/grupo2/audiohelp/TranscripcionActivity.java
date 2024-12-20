package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TranscripcionActivity extends AppCompatActivity {
    private static final int SPEECH_REQUEST_CODE = 10;
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

                // Mostrar el texto reconocido en el transcriptionBox
                EditText transcriptionBox = findViewById(R.id.transcriptionBox);
                transcriptionBox.setText(recognizedText);

                // Guardar la transcripción
                guardarTranscripcion(recognizedText);
            }
        }
    }

    /**
     * Guarda la transcripción reconocida en un archivo.
     *
     * @param transcriptionText Texto reconocido de la transcripción.
     */
    private void guardarTranscripcion(String transcriptionText) {
        try {
            // Generar un nombre único
            String fileName = "Transcription_" + System.currentTimeMillis() + ".audio";

            // Guardar la transcripción
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(transcriptionText.getBytes());
            fos.close();

            Toast.makeText(this, "Transcripción guardada: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error al guardar la transcripción", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}


