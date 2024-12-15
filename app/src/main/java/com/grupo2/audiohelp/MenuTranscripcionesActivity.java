package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MenuTranscripcionesActivity extends AppCompatActivity {
    private boolean isRecording = false;
    private MediaRecorder recorder;
    private String audioFilePath;
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
        setContentView(R.layout.menu_transcripcion);

        ImageButton btnTrans = findViewById(R.id.locution_button);
        btnTrans.setOnClickListener(v -> {
            if (isRecording) {
                stopRecording();
                goToTranscriptionActivity();
            } else {
                startRecording();
            }
        });
        ImageButton btnHistorial = findViewById(R.id.saved_transcriptions_icon);
        btnHistorial.setOnClickListener(w -> {
            Intent intent = new Intent(MenuTranscripcionesActivity.this, GestorTranscripcionesActivity.class);
            startActivity(intent);
        });
        Button btnMenu = findViewById(R.id.BtnOpciones);
        btnMenu.setOnClickListener(w -> {
            Intent intent = new Intent(MenuTranscripcionesActivity.this, MenuOpcionesActivity.class);
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

    private void startRecording() {
        audioFilePath = getExternalFilesDir(null) + "/audio_AudioHelp.wav";
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audioFilePath);

        try {
            recorder.prepare();
            recorder.start();
            isRecording = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (isRecording) {
            recorder.stop();
            recorder.release();
            isRecording = false;
        }
    }
    private void goToTranscriptionActivity(){
        Intent intent = new Intent(this, TranscripcionActivity.class);
        intent.putExtra("audioFilePath", audioFilePath);
        startActivity(intent);
    }

}