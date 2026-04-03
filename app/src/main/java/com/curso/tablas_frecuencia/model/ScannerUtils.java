package com.curso.tablas_frecuencia.model;

import com.google.mlkit.vision.text.Text;

public class ScannerUtils {

    public static String extraerNumeros(Text visionText) {
        
        StringBuilder resultado = new StringBuilder();
        for (Text.TextBlock block : visionText.getTextBlocks()) {
            for (Text.Line line : block.getLines()) {

                String lineText = line.getText();
                String limpio = lineText.replaceAll("[^0-9.\\s]", " ");

                if (!limpio.trim().isEmpty()) {
                    resultado.append(limpio).append(" ");
                }
            }
        }
        return resultado.toString().trim().replaceAll("\\s+", " ");
    }
}