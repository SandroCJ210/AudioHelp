package com.grupo2.audiohelp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class GestorTranscripcionesActivity extends AppCompatActivity {

    private LinearLayout transcriptionLayout;
    private boolean sortByName = true; // Default sorting method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestor_transcripciones);

        transcriptionLayout = findViewById(R.id.transcription_layout);
        Button BtonAgregar = findViewById(R.id.boton_AñadirTrans);
        BtonAgregar.setOnClickListener(w -> {
            Intent intent = new Intent(GestorTranscripcionesActivity.this, MenuTranscripcionesActivity.class);
            startActivity(intent);

        });

        Button sortByNameButton = findViewById(R.id.sort_button);

        sortByNameButton.setOnClickListener(v -> {
            // Toggle sorting method
            sortByName = true;
            displaySortedTranscriptions();
        });

        Button saveAudioButton = findViewById(R.id.save_audio_button);
        saveAudioButton.setOnClickListener(v -> {
            try {
                // Create dummy audio data for testing
                String name = "TestAudio";
                byte[] audioData = "This is dummy audio data".getBytes();
                Transcription transcription = new Transcription(name, audioData);
                saveTranscription(transcription);
                Log.d("SaveAudio", "Audio saved successfully: " + name);

                // Refresh the UI
                displaySortedTranscriptions();
            } catch (IOException e) {
                Log.e("SaveAudio", "Error saving audio", e);
            }
        });

        Button sortBySizeButton = findViewById(R.id.sort_button2);

        sortBySizeButton.setOnClickListener(v -> {
            // Toggle sorting method
            sortByName = false;
            displaySortedTranscriptions();
        });
        displaySortedTranscriptions();
    }

    private void displaySortedTranscriptions() {
        File directory = getFilesDir();
        Log.d("GestorActivity", getFilesDir().toString());
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".audio"));
        if (files == null || files.length == 0) return;

        List<File> fileList = new ArrayList<>();
        Collections.addAll(fileList, files);

        // Sort files based on the current sorting method
        if (sortByName) {
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    return file1.getName().compareTo(file2.getName());
                }
            });
        } else {
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    return Long.compare(file1.length(), file2.length());
                }
            });
        }

        // Add sorted transcriptions to the layout
        transcriptionLayout.removeAllViews();
        Log.d("GestorActivity", getFilesDir().toString());
        for (File file : fileList) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View itemView = inflater.inflate(R.layout.transcription_item, transcriptionLayout, false);

            // Customize the content
            TextView textView = itemView.findViewById(R.id.transcription_title);
            ImageButton optionsButton = itemView.findViewById(R.id.btnOpcionesTranscripcion2);

            // Set the title and
            String displayName = file.getName().replace(".audio", "");
            textView.setText(file.getName() + " (Size: " + file.length() + " bytes)");

            // Set a click listener for the options button
            optionsButton.setOnClickListener(v -> {
                // Call deleteTranscription when the options button is clicked
                deleteTranscription(displayName);
                displaySortedTranscriptions();
            });

            // Add the item to the parent layout
            transcriptionLayout.addView(itemView);
        }
    }

    private void deleteTranscription(String name) {
        String fileName = name + ".audio";
        File file = new File(getFilesDir(), fileName);
        if (file.exists()) {
            if (file.delete()) {
                Log.d("DeleteTranscription", "File deleted: " + fileName);
            } else {
                Log.e("DeleteTranscription", "Failed to delete file: " + fileName);
            }
        } else {
            Log.d("DeleteTranscription", "File not found: " + fileName);
        }
    }
    public void saveTranscription(Transcription transcription) throws IOException {
        String fileName = transcription.getName() + ".audio";
        FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
        fos.write(transcription.getAudio());
        fos.close();
    }

    /**
     * Retrieves the audio data of a transcription by its name.
     *
     * @param name The name of the transcription.
     * @return A byte array containing the audio data, or null if the file is not found.
     * @throws IOException If an error occurs during file reading.
     */
    public byte[] getAudioByName(String name) throws IOException {
        String fileName = name + ".audio";
        File file = new File(getFilesDir(), fileName);

        if (!file.exists()) {
            return null; // File does not exist
        }

        FileInputStream fis = new FileInputStream(file);
        byte[] audioData = new byte[(int) file.length()];
        fis.read(audioData);
        fis.close();
        return audioData;
    }

    /**
     * Data class representing a Transcription.
     */
    public static class Transcription {
        private String name;
        private byte[] audio;

        public Transcription(String name, byte[] audio) {
            this.name = name;
            this.audio = audio;
        }

        public String getName() {
            return name;
        }

        public byte[] getAudio() {
            return audio;
        }
    }
}