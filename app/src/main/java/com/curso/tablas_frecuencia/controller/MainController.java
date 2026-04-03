package com.curso.tablas_frecuencia.controller;

import com.curso.tablas_frecuencia.controller.logic.InputParser;
import com.curso.tablas_frecuencia.controller.logic.TableGenerator;
import com.curso.tablas_frecuencia.model.FrecuenciaItem;
import com.curso.tablas_frecuencia.model.FrecuenciaSimpleItem;
import java.util.List;

public class MainController {
    private final InputParser parser;
    private final TableGenerator tableGenerator;

    public MainController() {
        this.parser = new InputParser();
        this.tableGenerator = new TableGenerator();
    }

    public List<Double> obtenerDatosProcesados(String input) {
        return parser.stringToDoubleList(input);
    }

    public List<FrecuenciaItem> obtenerListaAgrupada(List<Double> datos, double min, int k, double a) {
        return tableGenerator.generarTablaAgrupada(datos, min, k, a);
    }

    public List<FrecuenciaSimpleItem> obtenerListaSimple(List<Double> datos) {
        return tableGenerator.generarTablaSimple(datos);
    }
}
