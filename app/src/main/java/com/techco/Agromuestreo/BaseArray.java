package com.techco.Agromuestreo;

import java.util.ArrayList;

public class BaseArray<T> {
    private ArrayList<T> arrayList;

    public BaseArray() {
        arrayList = new ArrayList<>();
    }

    public void add(T value) {
        arrayList.add(value);
    }

    public ArrayList<T> getArrayList() {
        return arrayList;
    }
}

// Clase para manejar enteros
class IntegerArray extends BaseArray<Integer> {
    public IntegerArray() {
        super();
    }
}

// Clase para manejar decimales
class DoubleArray extends BaseArray<Double> {
    public DoubleArray() {
        super();
    }
}
