package com.techco.Agromuestreo.Entidad;

import com.techco.Agromuestreo.databinding.ActivitySelhojaBinding;

public class registro2 {

    public String Id;
    public String Clasificacion;
    public String Hoja;
    public Double PC;
    public String Fecha;

    public registro2(String id, String clasificacion, String hoja, Double pc, String fecha){
        this.Id = id;
        this.Clasificacion = clasificacion;
        this.Hoja = hoja;
        this.PC = pc;
        this.Fecha = fecha;
    }

    public String getId(){ return Id;}
    public void setId (String id){Id = id;}

    public String getClasificacion(){return Clasificacion;}
    public void setClasificacion (String clasificacion){Clasificacion = clasificacion;}

    public String getHoja(){return Hoja;}
    public void setHoja (String hoja){Hoja = hoja;}

    public Double getPC(){return PC;}
    public void setPC(Double pc){PC = pc;}

    public String getFecha(){return Fecha;}
    public void setFecha (String fecha){Fecha = fecha;}

    public registro2(){}


}
