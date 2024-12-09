package com.grupo2.audiohelp;
public class Frase {
    private String titulo;
    private String texto;

    public Frase(String titulo, String texto) {
        this.titulo = titulo;
        this.texto = texto;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTexto() {
        return texto;
    }
}
