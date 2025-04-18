package com.techco.Agromuestreo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class ExpLVAdapter extends BaseExpandableListAdapter {

    final ArrayList<String> ListPreguntas;
    final Map<String,ArrayList<String>> mapChild;
    final Context context;

    public ExpLVAdapter(ArrayList<String> listPreguntas, Map<String, ArrayList<String>> mapChild, Context context) {
        this.ListPreguntas = listPreguntas;
        this.mapChild = mapChild;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return ListPreguntas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mapChild.get(ListPreguntas.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return ListPreguntas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mapChild.get(ListPreguntas.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String tituloCategoria = (String) getGroup(i);
        view = LayoutInflater.from(context).inflate(R.layout.preguntas,null);
        TextView pregunta = view.findViewById(R.id.pregunta);
        pregunta.setText(tituloCategoria);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String item = (String) getChild(i,i1);
        view = LayoutInflater.from(context).inflate(R.layout.respuestas,null);
        TextView respuestas = view.findViewById(R.id.respuesta);
        respuestas.setText(item);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
