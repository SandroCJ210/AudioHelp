package com.grupo2.audiohelp;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.types.Transcript;
import java.io.File;
import java.nio.file.*;
import java.nio.file.Paths;

public class SpeechToText {
    static AssemblyAI aai;
    String path;

    public static void main (String[] args){
        aai = AssemblyAI.builder().apiKey("98eb3744b7a5422e8147fdc8aab6f94c").build();
    }

    public SpeechToText(String path) {
        this.path = path;
    }

    public String Transcription(String path){
        try {
            if (path == null || path.isEmpty()) {
                throw new IllegalArgumentException("La ruta del archivo de audio es inválida");
            }

            File file = new File(path);
            Transcript transcript = aai.transcripts().transcribe(file);

            if (transcript != null && transcript.getText() != null) {
                return "" + transcript.getText();
            } else {
                return "No se pudo transcribir el audio.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al transcribir el audio: " + e.getMessage();
        }
    }
}

/*
*


    static AssemblyAI aai;
    String path;

    public static void main (String[] args){
        aai = AssemblyAI.builder().apiKey("98eb3744b7a5422e8147fdc8aab6f94c").build();
    }

    public SpeechToText(String path) {
        this.path = path;
    }

    public String Transcription(){
        try {
            if (path == null || path.isEmpty()) {
                throw new IllegalArgumentException("La ruta del archivo de audio es inválida");
            }

            File file = new File(path);
            Transcript transcript = aai.transcripts().transcribe(file);

            if (transcript != null && transcript.getText() != null) {
                return "" + transcript.getText();
            } else {
                return "No se pudo transcribir el audio.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al transcribir el audio: " + e.getMessage();
        }
    }
* */

/*
*
    private static Path filePath; // Ahora usamos Path para manejar la ruta
    private static String transcripcion; // Almacenamos la transcripción aquí

    // Constructor
    @SuppressLint("NewApi")
    public SpeechToText(String fileName) {
        this.filePath = Paths.get(fileName); // Convertimos el nombre de archivo en un Path
    }


    public String get(){
        return transcripcion;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void transcribeModelSelection() throws Exception {
        byte[] content = Files.readAllBytes(filePath);

        try (SpeechClient speech = SpeechClient.create()) {
            // Configure request with WAV audio and Spanish language
            RecognitionConfig recConfig =
                    RecognitionConfig.newBuilder()
                            // Suponiendo que el audio WAV está en formato LINEAR16
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setLanguageCode("es-ES")  // Cambiado a español (España)
                            .setSampleRateHertz(16000) // Asegúrate de que el archivo WAV tenga esta tasa de muestreo
                            .setModel("default")       // Se puede usar el modelo predeterminado para audio general
                            .build();

            RecognitionAudio recognitionAudio =
                    RecognitionAudio.newBuilder().setContent(ByteString.copyFrom(content)).build();

            // Realiza la transcripción
            RecognizeResponse recognizeResponse = speech.recognize(recConfig, recognitionAudio);

            // Imprime el primer resultado de la transcripción
            SpeechRecognitionResult result = recognizeResponse.getResultsList().get(0);
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            transcripcion = alternative.getTranscript();
            System.out.printf("Transcripción: %s\n", alternative.getTranscript());
        }
    }

* */