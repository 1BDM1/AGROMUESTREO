package com.techco.Agromuestreo.ui;

public class SpinnerItem {
    private String text;
    private int imageResId; // Identificador de recursos de la imagen

    public SpinnerItem(String text, int imageResId) {
        this.text = text;
        this.imageResId = imageResId;
    }

    public String getText() {
        return text;
    }

    public int getImageResId() {
        return imageResId;
    }
}
