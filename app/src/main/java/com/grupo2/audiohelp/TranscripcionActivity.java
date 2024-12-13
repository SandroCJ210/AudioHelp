package com.grupo2.audiohelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class TranscripcionActivity extends AppCompatActivity {
    private EditText transcriptionBox;
    private String audioFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transcripcion);

        transcriptionBox = findViewById(R.id.transcriptionBox);
        audioFilePath = getIntent().getStringExtra("audioFilePath");
        TranscribeAudio(audioFilePath);

        ImageButton btnCerrar = findViewById(R.id.Retroceder);
        btnCerrar.setOnClickListener(v -> {
            Intent intent = new Intent(TranscripcionActivity.this, MenuTranscripcionesActivity.class);
            startActivity(intent);
        });
    }

    private void TranscribeAudio(String audioFilePath) {
        SpeechToText speechToText = new SpeechToText(audioFilePath);
        String transcripcion = speechToText.Transcription(audioFilePath);
        transcriptionBox.setText(transcripcion);
    }

    private void deleteAudioFile(String audioFilePath) {
        File file = new File(audioFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

}
