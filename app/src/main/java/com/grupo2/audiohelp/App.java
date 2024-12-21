package com.grupo2.audiohelp;

import android.app.Application;
import android.graphics.Typeface;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Aplica la fuente globalmente
        String fuenteGuardada = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE)
                .getString("FuenteSeleccionada", "caskaydia_mono_nerd_font.ttf"); // Fuente predeterminada

        // Llama al método para aplicar la fuente
        aplicarFuenteGlobal(fuenteGuardada);
    }

    private void aplicarFuenteGlobal(String rutaFuente) {
        try {
            // Extraer solo el nombre del archivo de la ruta completa
            String nombreFuente = rutaFuente.substring(rutaFuente.lastIndexOf("/") + 1, rutaFuente.lastIndexOf("."));

            // Cargar la fuente desde res/font
            Typeface typeface = ResourcesCompat.getFont(this, getResources().getIdentifier(nombreFuente, "font", getPackageName()));

            // Aplicar la fuente a todos los TextViews globalmente
            TextView globalTextView = new TextView(this);
            globalTextView.setTypeface(typeface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
