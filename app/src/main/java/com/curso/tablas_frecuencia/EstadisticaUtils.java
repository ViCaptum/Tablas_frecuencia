package com.curso.tablas_frecuencia;

import java.util.*;

public class EstadisticaUtils {

    public static double getMin(List<Double> datos) {
        validarLista(datos);
        return Collections.min(datos);
    }

    public static double getMax(List<Double> datos) {
        validarLista(datos);
        return Collections.max(datos);
    }

    public static double getRango(double max, double min) {
        return max - min;
    }

    public static int getNumeroClases(int n) {
        if (n <= 0) return 0;
        double k = 1 + (3.322 * Math.log10(n));
        return (int) Math.ceil(k);
    }

    public static double getAmplitud(double rango, int k) {
        if (k == 0) return 0;
        return Math.ceil(rango / k);
    }

    public static double calcularMedia(List<Double> datos) {
        validarLista(datos);
        double suma = 0;

        for (double d : datos) {
            suma += d;
        }

        return suma / datos.size();
    }

    public static double calcularMediana(List<Double> datos) {
        validarLista(datos);

        List<Double> copia = new ArrayList<>(datos);
        Collections.sort(copia);

        int n = copia.size();

        if (n % 2 == 0) {
            return (copia.get(n / 2 - 1) + copia.get(n / 2)) / 2.0;
        } else {
            return copia.get(n / 2);
        }
    }

    public static List<Double> calcularModa(List<Double> datos) {
        validarLista(datos);

        Map<Double, Integer> frecuencia = new HashMap<>();

        for (double d : datos) {
            frecuencia.put(d, frecuencia.getOrDefault(d, 0) + 1);
        }

        int maxFrecuencia = Collections.max(frecuencia.values());
        List<Double> modas = new ArrayList<>();

        for (Map.Entry<Double, Integer> entry : frecuencia.entrySet()) {
            if (entry.getValue() == maxFrecuencia && maxFrecuencia > 1) {
                modas.add(entry.getKey());
            }
        }

        Collections.sort(modas);
        return modas;
    }

    public static Map<Double, Integer> calcularFrecuenciaSimple(List<Double> datos) {
        validarLista(datos);

        Map<Double, Integer> frecuencia = new TreeMap<>(); // ordenado

        for (double d : datos) {
            frecuencia.put(d, frecuencia.getOrDefault(d, 0) + 1);
        }

        return frecuencia;
    }

    private static void validarLista(List<Double> datos) {
        if (datos == null || datos.isEmpty()) {
            throw new IllegalArgumentException("La lista de datos está vacía");
        }
    }
}