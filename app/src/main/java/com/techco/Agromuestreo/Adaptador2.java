package com.techco.Agromuestreo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.techco.Agromuestreo.Entidad.Registro;

import java.util.ArrayList;

public class Adaptador2 extends BaseAdapter {

    ArrayList<Registro> ListaRegistro ;
    Context context;

    public Adaptador2(ArrayList<Registro> listaRegistro, Context context) {
        ListaRegistro = listaRegistro;
        this.context = context;
    }

    @Override
    public int getCount() {
        return ListaRegistro.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView datos;
        Button reporte;

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.historial_lista2,null);

        datos=view.findViewById(R.id.datos);
        reporte = (Button) view.findViewById(R.id.generar2);
        datos.setText(ListaRegistro.get(i).getCapturas());





        return view;
    }
}
