package com.curso.tablas_frecuencia;

public class FrecuenciaSimpleItem {

    private final double dato;
    private final int repeticiones;

    public FrecuenciaSimpleItem(double dato, int repeticiones) {
        this.dato = dato;
        this.repeticiones = repeticiones;
    }

    public double getDato() {
        return dato;
    }

    public int getRepeticiones() {
        return repeticiones;
    }
}