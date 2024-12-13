package com.grupo2.audiohelp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

        // Hacer clic en el ítem para abrir LocucionActivity
        holder.itemView.setOnClickListener(v -> {
            // Crear un Intent para abrir LocucionActivity
            Intent intent = new Intent(holder.itemView.getContext(), LocucionActivity.class);
            intent.putExtra("FRASE_TITULO", frase.getTitulo());  // Pasar el título de la frase
            intent.putExtra("FRASE_TEXTO", frase.getTexto());    // Pasar el texto de la frase
            holder.itemView.getContext().startActivity(intent);  // Lanzar la actividad
        });

        holder.btnOpciones.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.btnOpciones);
            popup.inflate(R.menu.menu_frase_item); // Asegúrate de que este archivo XML existe
            popup.show();

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_editar) {
                    // Iniciar la actividad EditarFraseActivity
                    Intent intent = new Intent(holder.itemView.getContext(), EditarFraseActivity.class);
                    intent.putExtra("FRASE_TITULO", frase.getTitulo()); // Pasar el título de la frase
                    intent.putExtra("FRASE_TEXTO", frase.getTexto());  // Pasar el texto de la frase
                    holder.itemView.getContext().startActivity(intent); // Lanzar la actividad

                    return true;
                } else if (item.getItemId() == R.id.menu_eliminar) {
                    eliminarFrase(holder.itemView.getContext(), frase);
                    return true;
                } else if (item.getItemId() == R.id.menu_reproducir){
                    Intent intent = new Intent(holder.itemView.getContext(), LocucionActivity.class);
                    intent.putExtra("FRASE_TITULO", frase.getTitulo());  // Pasar el título de la frase
                    intent.putExtra("FRASE_TEXTO", frase.getTexto());    // Pasar el texto de la frase
                    holder.itemView.getContext().startActivity(intent);  // Lanzar la actividad
                    return true;
                } else {
                    return false;
                }
            });


        });

    }

    private void eliminarFrase(Context context, Frase frase) {
        FraseManager fraseManager = new FraseManager();

        // Llamar al método de eliminación del FraseManager
        fraseManager.eliminarFrase(frase.getTitulo())
                .addOnSuccessListener(aVoid -> {
                    // Eliminar la frase de la lista y actualizar el RecyclerView
                    listaFrases.remove(frase);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Frase eliminada exitosamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Mostrar un mensaje en caso de error
                    Toast.makeText(context, "Error al eliminar frase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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
