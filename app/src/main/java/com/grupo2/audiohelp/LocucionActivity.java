package com.grupo2.audiohelp;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.speech.tts.UtteranceProgressListener;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LocucionActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private EditText locutionBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locucion);

        // Inicializar EditText
        locutionBox = findViewById(R.id.locutionBox);

        // Botón de reproducción
        ImageButton playButton = findViewById(R.id.playLocution);

        // Botón de retroceso
        ImageButton retrocederButton = findViewById(R.id.Retroceder);

        // Botón de frases guardadas
        ImageButton botonFrasesGuardadas = findViewById(R.id.botonFrasesGuardadas);

        // Boton de borrar locución
        ImageButton borrarLocucion = findViewById(R.id.borrarLocucion);

        // Titulo
        TextView titleLocution = findViewById(R.id.titleLocution);

        // Inicializar Text-to-Speech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setSpeechRate(1.0f);
                textToSpeech.setPitch(1.0f);

                // Habilitar procesamiento de rangos (si es compatible)
                Bundle params = new Bundle();
                params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "TTS_SPEAKING");
            }
        });

        // Configurar botón de locución
        playButton.setOnClickListener(v -> {
            String text = locutionBox.getText().toString();
            if (text.isEmpty()) {
                Toast.makeText(this, "Escribe algo para reproducir", Toast.LENGTH_SHORT).show();
            } else {
                // Reproducir texto como voz
                titleLocution.setTextColor(Color.parseColor("#90EE90")); // Cambia color de titulo
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TTS_SPEAKING");
            }
        });

        // Configurar botón de retroceso
        retrocederButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocucionActivity.this, MenuLocucionesActivity.class);
            startActivity(intent);
            // Opcional: Finaliza el activity actual para no volver con el botón de retroceso
            finish();
        });

        // Botón para ver frases
        botonFrasesGuardadas.setOnClickListener(v -> {
            // Crear un Intent para ir a GestorFrasesActivity
            Intent intent = new Intent(LocucionActivity.this, GestorFrasesActivity.class);
            startActivity(intent);
        });

        borrarLocucion.setOnClickListener(v -> {
            locutionBox.setText("");
        });

        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) { }

            @Override
            public void onDone(String utteranceId) {
                if (utteranceId.equals("TTS_SPEAKING")) {
                    runOnUiThread(() -> {
                        SpannableString spannable = new SpannableString(locutionBox.getText().toString());
                        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        locutionBox.setText(spannable);
                        // Restaurar el estilo original del título
                        titleLocution.setTextColor(getResources().getColor(R.color.primaryText)); // Restaurar color original
                        titleLocution.setTextSize(35); // Tamaño original
                        titleLocution.setTypeface(null, Typeface.NORMAL); // Restaurar a normal
                    });
                }
            }

            @Override
            public void onError(String utteranceId) { }

            @Override
            public void onRangeStart(String utteranceId, int start, int end, int frame) {
                if (utteranceId.equals("TTS_SPEAKING")) {
                    // Cambiar color, tamaño y negrita mientras se habla
                    runOnUiThread(() -> {
                        titleLocution.setTextColor(Color.parseColor("#90EE90")); // Cambiar a verde claro
                        titleLocution.setTextSize(40); // Aumentar tamaño de texto
                        titleLocution.setTypeface(null, Typeface.BOLD); // Poner en negrita
                    });
                    String text = locutionBox.getText().toString();
                    int wordIndex = getWordIndex(text, start); // Obtiene el índice de la palabra actual
                    highlightWord(text, wordIndex);
                }
            }
        });
    }

    // Metodo para obtener el índice de la palabra actual basándose en la posición "start"
    private int getWordIndex(String text, int start) {
        String[] words = text.split(" |\n");
        int charCount = 0;
        for (int i = 0; i < words.length; i++) {
            charCount += words[i].length() + 1; // +1 para el espacio o salto de línea
            if (start < charCount) {
                return i; // Retorna el índice de la palabra actual
            }
        }
        return -1; // Si no encuentra un índice válido
    }

    private void highlightWord(String text, int wordIndex) {
        // Dividir el texto en palabras usando espacio y salto de línea
        SpannableString spannable = new SpannableString(text);
        String[] words = text.split(" |\n");
        int start = 0;
        for (int i = 0; i < words.length; i++) {
            int end = start + words[i].length();
            // Si la palabra es la actual, la resalta en verde
            if (i == wordIndex) {
                spannable.setSpan(new ForegroundColorSpan(Color.GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                // Restaura el color blanco para las demás palabras
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            start = end + 1; // Avanzar el índice para la próxima palabra (agregar 1 para el espacio o salto de línea)
        }
        // Actualiza el texto del EditText en el hilo principal
        runOnUiThread(() -> locutionBox.setText(spannable));
    }

    @Override
    protected void onDestroy() {
        // Liberar recursos de Text-to-Speech
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}