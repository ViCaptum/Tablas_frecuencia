package com.curso.tablas_frecuencia.model;

public class FrecuenciaItem {

    private final double limiteInferior;
    private final double limiteSuperior;
    private final int frecuenciaAbsoluta;
    private final int frecuenciaAcumulada;
    private final double frecuenciaRelativa;
    private final double frecuenciaRelativaAcumulada;

    public FrecuenciaItem(double limiteInferior,
                          double limiteSuperior,
                          int frecuenciaAbsoluta,
                          int frecuenciaAcumulada,
                          double frecuenciaRelativa,
                          double frecuenciaRelativaAcumulada) {

        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
        this.frecuenciaAbsoluta = frecuenciaAbsoluta;
        this.frecuenciaAcumulada = frecuenciaAcumulada;
        this.frecuenciaRelativa = frecuenciaRelativa;
        this.frecuenciaRelativaAcumulada = frecuenciaRelativaAcumulada;
    }

    public double getLimiteInferior() {
        return limiteInferior;
    }

    public double getLimiteSuperior() {
        return limiteSuperior;
    }

    public int getFrecuenciaAbsoluta() {
        return frecuenciaAbsoluta;
    }

    public int getFrecuenciaAcumulada() {
        return frecuenciaAcumulada;
    }

    public double getFrecuenciaRelativa() {
        return frecuenciaRelativa;
    }

    public double getFrecuenciaRelativaAcumulada() {
        return frecuenciaRelativaAcumulada;
    }

    public String getIntervaloString() {
        return String.format("%.0f - %.0f", limiteInferior, limiteSuperior);
    }
}