package com.grupo2.audiohelp;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FraseManager {

    private final FirebaseFirestore db;
    private final String userId;

    // Constructor
    public FraseManager() {
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    /**
     * Clase interna que representa una frase.
     */
    public static class Frase {
        private String id;
        private String texto;

        // Constructor vacío necesario para Firestore
        public Frase() {}

        public Frase(String id, String texto) {
            this.id = id;
            this.texto = texto;
        }

        // Getters y setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTexto() {
            return texto;
        }

        public void setTexto(String texto) {
            this.texto = texto;
        }
    }

    /**
     * Método para agregar una frase a la base de datos.
     *
     * @param id    Identificador único de la frase.
     * @param texto Texto de la frase.
     * @return Task para manejar resultados (éxito o fallo).
     */
    public Task<Void> agregarFrase(String id, String texto) {
        DocumentReference nuevaFraseRef = db.collection("users")
                .document(userId)
                .collection("phrases")
                .document(id);

        Frase nuevaFrase = new Frase(id, texto);
        return nuevaFraseRef.set(nuevaFrase);
    }
}
