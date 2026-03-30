package com.curso.tablas_frecuencia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class FrecuenciaSimpleAdapter extends RecyclerView.Adapter<FrecuenciaSimpleAdapter.ViewHolder> {

    private final List<FrecuenciaSimpleItem> lista;

    public FrecuenciaSimpleAdapter(List<FrecuenciaSimpleItem> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_frecuencia_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FrecuenciaSimpleItem item = lista.get(position);

        holder.tvDato.setText(formatearNumero(item.getDato()));
        holder.tvRepeticiones.setText(String.valueOf(item.getRepeticiones()));
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    private String formatearNumero(double d) {
        if (d == (long) d) {
            return String.valueOf((long) d);
        }
        return String.format(Locale.getDefault(), "%.2f", d);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDato, tvRepeticiones;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDato = itemView.findViewById(R.id.tvDato);
            tvRepeticiones = itemView.findViewById(R.id.tvRepeticiones);
        }
    }
}