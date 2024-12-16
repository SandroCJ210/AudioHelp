package com.grupo2.audiohelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GestorFrasesActivity extends AppCompatActivity {
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
        setContentView(R.layout.gestor_frases);


        RecyclerView recyclerView = findViewById(R.id.recyclerViewFrases);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Frase> listaFrases = new ArrayList<>();
        FrasesAdapter adapter = new FrasesAdapter(listaFrases);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Obtener frases desde Firebase
        db.collection("users").document(uid).collection("phrases")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String titulo = doc.getString("id");
                        String texto = doc.getString("texto");
                        listaFrases.add(new Frase(titulo, texto)); // Asegúrate de tener el constructor de Frase
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(GestorFrasesActivity.this, "Error al cargar frases", Toast.LENGTH_SHORT).show();
                });


        ImageButton botonAgregarFrase = findViewById(R.id.botonAgregarFrase);
        // Configurar el evento OnClickListener
        botonAgregarFrase.setOnClickListener(v -> {
            Intent intent = new Intent(GestorFrasesActivity.this, AgregarFraseActivity.class);
            startActivity(intent);
        });


        ImageButton btnCerrar = findViewById(R.id.buttonClose);
        btnCerrar.setOnClickListener(v -> {
            Intent intent = new Intent(GestorFrasesActivity.this, MenuLocucionesActivity.class);
            startActivity(intent);
        });
    }
}