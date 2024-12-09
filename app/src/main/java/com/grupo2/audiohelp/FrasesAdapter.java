package com.grupo2.audiohelp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FrasesAdapter extends RecyclerView.Adapter<FrasesAdapter.FraseViewHolder> {

    private List<Frase> listaFrases;

    public FrasesAdapter(List<Frase> listaFrases) {
        this.listaFrases = listaFrases;
    }

    @NonNull
    @Override
    public FraseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_frase, parent, false);
        return new FraseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FraseViewHolder holder, int position) {
        Frase frase = listaFrases.get(position);
        holder.tvTituloFrase.setText(frase.getTitulo());
    }

    @Override
    public int getItemCount() {
        return listaFrases.size();
    }

    public static class FraseViewHolder extends RecyclerView.ViewHolder {
        TextView tvTituloFrase;
        ImageButton btnOpciones;

        public FraseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTituloFrase = itemView.findViewById(R.id.tvTituloFrase);
            btnOpciones = itemView.findViewById(R.id.btnOpciones);
        }
    }
}
