package com.curso.tablas_frecuencia.controller.logic;

import com.curso.tablas_frecuencia.model.FrecuenciaItem;
import com.curso.tablas_frecuencia.model.FrecuenciaSimpleItem;
import com.curso.tablas_frecuencia.model.EstadisticaUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableGenerator {

    public List<FrecuenciaItem> generarTablaAgrupada(List<Double> datos, double min, int k, double a) {
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
            lista.add(new FrecuenciaItem(li, ls, fa, fac, fr * 100, frac * 100));
            li = ls;
        }
        return lista;
    }

    public List<FrecuenciaSimpleItem> generarTablaSimple(List<Double> datos) {
        Map<Double, Integer> frecuencia = EstadisticaUtils.calcularFrecuenciaSimple(datos);
        List<FrecuenciaSimpleItem> listaSimple = new ArrayList<>();
        for (Map.Entry<Double, Integer> entry : frecuencia.entrySet()) {
            listaSimple.add(new FrecuenciaSimpleItem(entry.getKey(), entry.getValue()));
        }
        return listaSimple;
    }
}