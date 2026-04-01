package com.curso.tablas_frecuencia.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.tablas_frecuencia.R;
import com.curso.tablas_frecuencia.model.FrecuenciaItem;

import java.util.List;
import java.util.Locale;

public class FrequencyAdapter extends RecyclerView.Adapter<FrequencyAdapter.FrequencyViewHolder> {

    private final List<FrecuenciaItem> listaFrecuencias;

    public FrequencyAdapter(List<FrecuenciaItem> listaFrecuencias) {
        this.listaFrecuencias = listaFrecuencias;
    }

    @NonNull
    @Override
    public FrequencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_frequency, parent, false);
        return new FrequencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FrequencyViewHolder holder, int position) {
        FrecuenciaItem item = listaFrecuencias.get(position);

        holder.tvInterval.setText(item.getIntervaloString());
        holder.tvFA.setText(String.valueOf(item.getFrecuenciaAbsoluta()));
        holder.tvFAcum.setText(String.valueOf(item.getFrecuenciaAcumulada()));

        holder.tvFR.setText(formatPercent(item.getFrecuenciaRelativa()));
        holder.tvFRAcum.setText(formatPercent(item.getFrecuenciaRelativaAcumulada()));
    }

    @Override
    public int getItemCount() {
        return listaFrecuencias != null ? listaFrecuencias.size() : 0;
    }

    private String formatPercent(double value) {
        return String.format(Locale.getDefault(), "%.2f%%", value);
    }

    static class FrequencyViewHolder extends RecyclerView.ViewHolder {

        TextView tvInterval, tvFA, tvFAcum, tvFR, tvFRAcum;

        public FrequencyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInterval = itemView.findViewById(R.id.tvInterval);
            tvFA = itemView.findViewById(R.id.tvFA);
            tvFAcum = itemView.findViewById(R.id.tvFAcum);
            tvFR = itemView.findViewById(R.id.tvFR);
            tvFRAcum = itemView.findViewById(R.id.tvFRAcum);
        }
    }
}