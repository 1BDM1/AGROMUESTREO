package com.techco.Agromuestreo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techco.Agromuestreo.Entidad.Registro;

import java.util.ArrayList;
import java.util.Collections;

public class Adaptador3<T> extends BaseAdapter {

    ArrayList<T> miArreglo ;
    Context context;

    public Adaptador3(ArrayList<T> miArreglo, Context context) {
        this.miArreglo = miArreglo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return miArreglo.size();
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

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.respuestas,null);

        datos=view.findViewById(R.id.respuesta);
        datos.setText(miArreglo.get(i).toString());



        return view;
    }
}