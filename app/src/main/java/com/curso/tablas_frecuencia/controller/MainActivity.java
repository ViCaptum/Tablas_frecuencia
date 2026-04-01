package com.curso.tablas_frecuencia.controller;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.tablas_frecuencia.R;
import com.curso.tablas_frecuencia.adapter.FrecuenciaSimpleAdapter;
import com.curso.tablas_frecuencia.adapter.FrequencyAdapter;
import com.curso.tablas_frecuencia.model.EstadisticaUtils;
import com.curso.tablas_frecuencia.model.FrecuenciaItem;
import com.curso.tablas_frecuencia.model.FrecuenciaSimpleItem;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private EditText etNumbersInput;
    private TextView tvN, tvMin, tvMax, tvRange, tvClasses, tvWidth;
    private TextView tvMedia, tvMediana, tvModa;
    private TextView tvSortedNumbers;
    private RecyclerView rvFrequencyTable;
    private RecyclerView rvFrecuenciaSimple;
    private FrequencyAdapter adapter;
    private FrecuenciaSimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        findViewById(R.id.btnCalculate).setOnClickListener(v -> procesarInformacion());
    }

    private void initViews() {
        etNumbersInput = findViewById(R.id.etNumbersInput);

        // NUEVOS IDs (IMPORTANTE)
        tvN = findViewById(R.id.tvN);
        tvMin = findViewById(R.id.tvMin);
        tvMax = findViewById(R.id.tvMax);
        tvRange = findViewById(R.id.tvRange);
        tvClasses = findViewById(R.id.tvClasses);
        tvWidth = findViewById(R.id.tvWidth);

        tvMedia = findViewById(R.id.tvMedia);
        tvMediana = findViewById(R.id.tvMediana);
        tvModa = findViewById(R.id.tvModa);

        tvSortedNumbers = findViewById(R.id.tvSortedNumbers);

        rvFrequencyTable = findViewById(R.id.rvFrequencyTable);
        rvFrecuenciaSimple = findViewById(R.id.rvFrecuenciaSimple);

        rvFrequencyTable.setLayoutManager(new LinearLayoutManager(this));
        rvFrecuenciaSimple.setLayoutManager(new LinearLayoutManager(this));
    }

    private void procesarInformacion() {

        String input = etNumbersInput.getText().toString().trim();

        if (input.isEmpty()) {
            Toast.makeText(this, "Ingrese datos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            List<Double> datos = obtenerListaDesdeString(input);
            Collections.sort(datos);

            int n = datos.size();

            double min = EstadisticaUtils.getMin(datos);
            double max = EstadisticaUtils.getMax(datos);
            double rango = EstadisticaUtils.getRango(max, min);
            int k = EstadisticaUtils.getNumeroClases(n);
            double amplitud = EstadisticaUtils.getAmplitud(rango, k);

            mostrarDatosOrdenados(datos);
            actualizarResumen(n, min, max, rango, k, amplitud);

            double media = EstadisticaUtils.calcularMedia(datos);
            double mediana = EstadisticaUtils.calcularMediana(datos);
            List<Double> modaList = EstadisticaUtils.calcularModa(datos);

            tvMedia.setText("Media: " + formatDecimal(media));
            tvMediana.setText("Mediana: " + formatDecimal(mediana));
            tvModa.setText("Moda: " + formatearModa(modaList));

            List<FrecuenciaItem> tabla = generarListaTabla(datos, min, k, amplitud);
            adapter = new FrequencyAdapter(tabla);
            rvFrequencyTable.setAdapter(adapter);

            Map<Double, Integer> frecuencia = EstadisticaUtils.calcularFrecuenciaSimple(datos);

            List<FrecuenciaSimpleItem> listaSimple = new ArrayList<>();
            for (Map.Entry<Double, Integer> entry : frecuencia.entrySet()) {
                listaSimple.add(new FrecuenciaSimpleItem(entry.getKey(), entry.getValue()));
            }

            simpleAdapter = new FrecuenciaSimpleAdapter(listaSimple);
            rvFrecuenciaSimple.setAdapter(simpleAdapter);

            etNumbersInput.setText("");

        } catch (Exception e) {
            Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Double> obtenerListaDesdeString(String input) {
        List<Double> lista = new ArrayList<>();
        String[] partes = input.split("\\s+");

        for (String p : partes) {
            if (!p.isEmpty()) lista.add(Double.parseDouble(p));
        }

        return lista;
    }

    private void mostrarDatosOrdenados(List<Double> lista) {

        int n = lista.size();
        int columnas = (int) Math.ceil(Math.sqrt(n));

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < n; i++) {
            sb.append(String.format(Locale.getDefault(), "%-6s", formatDecimal(lista.get(i))));

            if ((i + 1) % columnas == 0) {
                sb.append("\n");
            } else {
                sb.append("   ");
            }
        }

        tvSortedNumbers.setText(sb.toString());
    }

    private void actualizarResumen(int n, double min, double max, double r, int k, double a) {
        tvN.setText("n: " + n);
        tvMin.setText("Min: " + formatDecimal(min));
        tvMax.setText("Max: " + formatDecimal(max));
        tvRange.setText("Rango: " + formatDecimal(r));
        tvClasses.setText("Clases: " + k);
        tvWidth.setText("Amplitud: " + formatDecimal(a));
    }

    private String formatDecimal(double d) {
        if (d == (long) d) {
            return String.valueOf((long) d);
        }
        return String.format(Locale.getDefault(), "%.2f", d);
    }

    private String formatearModa(List<Double> modas) {
        if (modas.isEmpty()) return "No hay";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < modas.size(); i++) {
            sb.append(formatDecimal(modas.get(i)));
            if (i < modas.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    private List<FrecuenciaItem> generarListaTabla(List<Double> datos, double min, int k, double a) {

        List<FrecuenciaItem> lista = new ArrayList<>();

        double li = min;
        int fac = 0;
        double frac = 0;
        int n = datos.size();

        for (int i = 0; i < k; i++) {

            double ls = li + a;
            int fa = 0;

            for (double d : datos) {
                if (i < k - 1) {
                    if (d >= li && d < ls) fa++;
                } else {
                    if (d >= li && d <= ls) fa++;
                }
            }

            fac += fa;
            double fr = (double) fa / n;
            frac += fr;

            lista.add(new FrecuenciaItem(
                    li, ls,
                    fa,
                    fac,
                    fr * 100,
                    frac * 100
            ));

            li = ls;
        }
        return lista;
    }
}