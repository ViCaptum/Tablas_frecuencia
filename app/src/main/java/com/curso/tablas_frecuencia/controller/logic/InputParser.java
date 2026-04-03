package com.curso.tablas_frecuencia.controller.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InputParser {
    public List<Double> stringToDoubleList(String input) {
        List<Double> lista = new ArrayList<>();
        if (input == null || input.isEmpty()) return lista;

        String[] partes = input.split("\\s+");
        for (String p : partes) {
            if (!p.isEmpty()) {
                try {
                    lista.add(Double.parseDouble(p));
                } catch (NumberFormatException ignored) {}
            }
        }
        Collections.sort(lista);
        return lista;
    }
}