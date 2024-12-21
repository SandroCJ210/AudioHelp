package com.grupo2.audiohelp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class GestorTranscripcionesActivity extends AppCompatActivity {

    private LinearLayout transcriptionLayout;
    private boolean sortByName = true; // Default sorting method

    private SharedPreferences sharedPreferences;

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
        setContentView(R.layout.gestor_transcripciones);

        // Aplica la fuente guardada
        aplicarFuenteGuardada();

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
            Button transcriptionButton = itemView.findViewById(R.id.transcription_title);
            ImageButton optionsButton = itemView.findViewById(R.id.btnOpcionesTranscripcion2);

            // Set the title and
            String displayName = file.getName().replace(".audio", "");
            transcriptionButton.setText(file.getName() + " (Size: " + file.length() + " bytes)");

            // Set a click listener for the options button
            optionsButton.setOnClickListener(v -> {
                // Call deleteTranscription when the options button is clicked
                deleteTranscription(displayName);
                displaySortedTranscriptions();
            });

            transcriptionButton.setOnClickListener(v -> {
                Intent intent = new Intent(GestorTranscripcionesActivity.this, TranscripcionActivity.class);
                String text = obtenerTranscripcion(file.getName());
                intent.putExtra("transcripcion",text);
                startActivity(intent);
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
     * @param fileName The name of the transcription.
     * @return A byte array containing the audio data, or null if the file is not found.
     * @throws IOException If an error occurs during file reading.
     */
    private String obtenerTranscripcion(String fileName) {
        try {
            // Abrir el archivo desde el almacenamiento interno
            FileInputStream fis = openFileInput(fileName);

            // Leer el contenido del archivo
            StringBuilder stringBuilder = new StringBuilder();
            int content;
            while ((content = fis.read()) != -1) {
                stringBuilder.append((char) content);
            }

            // Cerrar el archivo
            fis.close();

            // Retornar el contenido como texto
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al leer la transcripción", Toast.LENGTH_SHORT).show();
            return null; // Retorna null si ocurre un error
        }
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