package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TranscripcionActivity extends AppCompatActivity {
    private static final int SPEECH_REQUEST_CODE = 10;

    private SharedPreferences sharedPreferences;
    String recognizedText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Cargar tema antes de llamar a super.onCreate()
        sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            setTheme(R.style.DarkTheme); // Tema oscuro
        } else {
            setTheme(R.style.LightTheme); // Tema claro
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.transcripcion);

        // Aplica la fuente guardada
        aplicarFuenteGuardada();

        recognizedText = getIntent().getStringExtra("transcripcion");
        if(recognizedText != null){
            EditText transcriptionBox = findViewById(R.id.transcriptionBox);
            transcriptionBox.setText(recognizedText);
        }else startSpeechToText();


        ImageButton btnCerrar = findViewById(R.id.Retroceder);
        btnCerrar.setOnClickListener(v -> {
            Intent IT = new Intent(TranscripcionActivity.this, MenuTranscripcionesActivity.class);
            startActivity(IT);
        });

        ImageButton eraseButton = findViewById(R.id.Borrar_Trans);
        eraseButton.setOnClickListener(v -> {
            EditText transcriptionBox = findViewById(R.id.transcriptionBox);
            transcriptionBox.setText("");
            startSpeechToText();
        });

        ImageButton saveButton = findViewById(R.id.Guardar_Trans);
        saveButton.setOnClickListener(v -> {
            guardarTranscripcion(recognizedText);
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
                recognizedText = results.get(0); // Primer resultado reconocido

                // Mostrar el texto reconocido en el transcriptionBox
                EditText transcriptionBox = findViewById(R.id.transcriptionBox);
                transcriptionBox.setText(recognizedText);

                // Guardar la transcripción
                //guardarTranscripcion(recognizedText);
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

    private void aplicarFuente(String rutaFuente) {
        try {
            // Extraer solo el nombre del archivo de la ruta completa
            String nombreFuente = rutaFuente.substring(rutaFuente.lastIndexOf("/") + 1, rutaFuente.lastIndexOf("."));

            // Cargar la fuente desde res/font
            Typeface typeface = ResourcesCompat.getFont(this, getResources().getIdentifier(nombreFuente, "font", getPackageName()));

            // Aplica la fuente seleccionada a todos los TextViews de la actividad
            View rootView = findViewById(android.R.id.content);
            aplicarFuenteEnVistas(rootView, typeface);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar la fuente", Toast.LENGTH_SHORT).show();
        }
    }


    private void aplicarFuenteEnVistas(View view, Typeface typeface) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                aplicarFuenteEnVistas(group.getChildAt(i), typeface);
            }
        }
    }

    private void aplicarFuenteGuardada() {
        try {
            // Lee la fuente guardada desde SharedPreferences
            String fuenteGuardada = sharedPreferences.getString("FuenteSeleccionada", "caskaydia_mono_nerd_font"); // Ajusta el nombre de la fuente

            // Aplica la fuente
            Typeface typeface = ResourcesCompat.getFont(this, getResources().getIdentifier(fuenteGuardada, "font", getPackageName()));
            aplicarFuenteEnVistas(findViewById(android.R.id.content), typeface);
        } catch (Exception e) {
            e.printStackTrace();
            // Si hay un error, aplica la fuente predeterminada
            Typeface defaultTypeface = ResourcesCompat.getFont(this, R.font.caskaydia_mono_nerd_font);
            aplicarFuenteEnVistas(findViewById(android.R.id.content), defaultTypeface);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Aplica la fuente guardada al inicio
        aplicarFuenteGuardada();
    }
}


