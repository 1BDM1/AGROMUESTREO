package com.techco.Agromuestreo.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.internal.RootTelemetryConfigManager;
import com.techco.Agromuestreo.EnfermedadSe;
import com.techco.Agromuestreo.Grafica;
import com.techco.Agromuestreo.R;
import com.techco.Agromuestreo.selhoja;


public class Enfermedad extends Fragment {

    public ImageView duda;
    public Button ENTRENO, MUESTREO;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_enfermedad, container, false);

        duda = root.findViewById(R.id.informacionE);
        ENTRENO = root.findViewById(R.id.ENTRENAR);
        MUESTREO = root.findViewById(R.id.MUESTREO);


        duda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                builder2.setTitle("Selección");
                builder2.setMessage("Selecciona una opcion");
                builder2.setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                    }

                });

                Dialog dialog2 = builder2.create();
                dialog2.setCancelable(false);
                dialog2.show();


            }

        });

        MUESTREO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            try {

                Intent i = new Intent(getActivity(), EnfermedadSe.class);
                startActivity(i);


            }catch (Exception e){

                Toast.makeText(getActivity(),"Error al iniciar",Toast.LENGTH_SHORT).show();
             }

            }
        });


        ENTRENO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Intent i = new Intent(getActivity(), selhoja.class);
                    startActivity(i);

                }catch (Exception e){

                    Toast.makeText(getActivity(),"Error al iniciar",Toast.LENGTH_SHORT).show();
                }

            }
        });




        return root;
    }





}