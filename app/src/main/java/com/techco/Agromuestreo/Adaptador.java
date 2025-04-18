package com.techco.Agromuestreo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techco.Agromuestreo.Entidad.Registro;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {

    ArrayList<Registro> ListaRegistro;
    Context context;
   /* String  fecha;
    String  plaga;
    String  cultivo;
    String  Densidad;
*/

    public Adaptador(Context context,ArrayList<Registro> listaRegistro) {
        ListaRegistro = listaRegistro;
        this.context = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView fecha;
        TextView cultivo;
        TextView plaga;
        TextView densidad;

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.historial_lista,null);

        fecha = (TextView) view.findViewById(R.id.fecha);
        cultivo = (TextView) view.findViewById(R.id.cultivo);
        plaga = (TextView) view.findViewById(R.id.plaga);
        densidad = (TextView) view.findViewById(R.id.densidad);

        fecha.setText(ListaRegistro.get(i).getFecha());
        plaga.setText(ListaRegistro.get(i).getPlaga());
        cultivo.setText(ListaRegistro.get(i).getCultivo());
        densidad.setText("Densidad:" + "  "+ ListaRegistro.get(i).getDensidad().toString());




        return view;
    }

    @Override
    public int getCount() {
        return ListaRegistro.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


}
