package com.techco.Agromuestreo.Entidad;

import java.sql.Date;

public class Registro {

    private String fecha;
    private String latitud,longuitud;
    private String Cultivo,Plaga;
    private Double Densidad;
    private String Capturas;
    private String Area;
    private Double PRECISION;
    private String TIPO;
    private String ETAPA;

    //Densidad no ha sido usado


  /*  public Registro(String fecha, String latitud, String longuitud, Double densidad) {
        this.fecha = fecha;
        this.latitud = latitud;
        this.longuitud = longuitud;
        Densidad = densidad;
    }*/

    public Registro(String fecha, String latitud, String longuitud, String cultivo, String plaga, Double densidad, String capturas, String area, Double pres, String tipo, String etapa) {
        this.fecha = fecha;
        this.latitud = latitud;
        this.longuitud = longuitud;
        Cultivo = cultivo;
        Plaga = plaga;
        Densidad = densidad;
        Capturas = capturas;
        Area = area;
        PRECISION = pres;
        TIPO = tipo;
        ETAPA = etapa;
    }

    public String getETAPA(){
        return ETAPA;
    }

    public  void setETAPA(String etapa){ ETAPA = etapa;}

    public String getTIPO(){
        return TIPO;
    }

    public  void setTIPO(String tipo){
        TIPO = tipo;
    }

    public Double getPRECISION(){
        return PRECISION;
    }

    public  void setPRECISION(Double pres){
        PRECISION = pres;
    }

    public String getArea(){
        return Area;
    }

    public  void setArea(String area){
        Area = area;
    }

    public String getCapturas() {
        return Capturas;
    }

    public void setCapturas(String capturas) {
        Capturas = capturas;
    }

    public String getCultivo() {
        return Cultivo;
    }

    public void setCultivo(String cultivo) {
        Cultivo = cultivo;
    }

    public String getPlaga() {
        return Plaga;
    }

    public void setPlaga(String plaga) {
        Plaga = plaga;
    }

    public Registro(){

    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLonguitud() {
        return longuitud;
    }

    public void setLonguitud(String longuitud) {
        this.longuitud = longuitud;
    }



    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Double getDensidad() {
        return Densidad;
    }

    public void setDensidad(Double densidad) {
        Densidad = densidad;
    }
}