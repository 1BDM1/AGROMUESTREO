package com.techco.Agromuestreo.Utilidades;
import java.util.ArrayList;

public class CorrelationCalculator {
    public static double calculateCorrelation(ArrayList<Double> xValues, ArrayList<Double> yValues) {
        int n = xValues.size();
        double sumXY = 0;
        double sumX = 0;
        double sumY = 0;
        double sumXSquare = 0;
        double sumYSquare = 0;

        for (int i = 0; i < n; i++) {
            double x = xValues.get(i);
            double y = yValues.get(i);
            sumXY += x * y;
            sumX += x;
            sumY += y;
            sumXSquare += x * x;
            sumYSquare += y * y;
        }

        double numerator = n * sumXY - sumX * sumY;
        double denominatorX = Math.sqrt(n * sumXSquare - sumX * sumX);
        double denominatorY = Math.sqrt(n * sumYSquare - sumY * sumY);

        return numerator / (denominatorX * denominatorY);
    }

    public static void main(String[] args) {
        ArrayList<Double> xValues = new ArrayList<>();
        ArrayList<Double> yValues = new ArrayList<>();

        // Agrega tus datos a los ArrayList (xValues e yValues)

        double correlation = calculateCorrelation(xValues, yValues);
        //System.out.println("Coeficiente de correlación (r): " + correlation);
    }
}