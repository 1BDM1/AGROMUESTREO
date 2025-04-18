package com.techco.Agromuestreo.Utilidades;

public class Utilidades {

    public static final String TABLA_REGISTRO= "Registro";
    public static final String CAMPO_FECHA= "Fecha";
    public static final String CAMPO_DENSIDAD= "Densidad";
    public static final String CAMPO_LATITUD= "latitud";
    public static final String CAMPO_LONGUITUD= "longuitud";
    public static final String CAMPO_CULTIVO= "cultivo";
    public static final String CAMPO_PLAGA= "plaga";
    public static final String CAMPO_DATOS= "datos";
    public static final String CAMPO_MUESTRA= "muestra";
    public static final String CAMPO_PRE= "precision";
    public static final String CAMPO_TIPO = "tipo";
    public static final String CAMPO_ETAPA = "etapa";



    public static final String CREAR_TABLA= "CREATE TABLE " +TABLA_REGISTRO+ " ( "+CAMPO_CULTIVO+" STRING, "+CAMPO_PLAGA+" STRING, "+CAMPO_FECHA+" DATE, "+CAMPO_DENSIDAD+" DOUBLE, "+CAMPO_LATITUD+" STRING, "+CAMPO_LONGUITUD+" STRING, "+CAMPO_MUESTRA+" STRING, "+CAMPO_PRE+" DOUBLE, "+CAMPO_DATOS+" STRING, "+CAMPO_TIPO+" STRING, "+CAMPO_ETAPA+" STRING) ";

}
