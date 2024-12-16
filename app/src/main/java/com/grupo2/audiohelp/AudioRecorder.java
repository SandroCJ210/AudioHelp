package com.grupo2.audiohelp;

import android.media.MediaRecorder;
import android.os.Environment;
import java.io.File;
import java.io.IOException;

public class AudioRecorder {
    private MediaRecorder recorder;
    private String filePath;

    public AudioRecorder() {
        // El archivo de audio se guardará en la memoria interna del dispositivo
        File directory = new File(Environment.getExternalStorageDirectory(), "AudioHelp");
        if (!directory.exists()) {
            directory.mkdir(); // Crear directorio si no existe
        }

        filePath = directory.getAbsolutePath() + "/recording.mp3"; // Ruta del archivo de audio
    }

    public void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(filePath);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void deleteRecording() {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete(); // Borrar archivo después de la transcripción
        }
    }


}
