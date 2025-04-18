package com.techco.Agromuestreo.ui.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.techco.Agromuestreo.EnfermedadSe;
import com.techco.Agromuestreo.R;
import com.techco.Agromuestreo.selhoja;




public class ENFERMEDAD_ACTIVITY extends AppCompatActivity {


    public ImageView duda;
    public Button ENTRENO, MUESTREO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_enfermedad);

        duda = findViewById(R.id.informacionE);
        ENTRENO = findViewById(R.id.ENTRENAR);
        MUESTREO = findViewById(R.id.MUESTREO);


        duda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder2 = new AlertDialog.Builder(ENFERMEDAD_ACTIVITY.this);
                builder2.setTitle("Selección");
                builder2.setMessage("Se puede acceder al entrenamiento para el muestreo de enfermedades, para así estar mejor cualificado. Dicha calificación obtenida en el entrenamiento aparecera en el reporte final en cada muestreo.");
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

                    Intent i = new Intent(ENFERMEDAD_ACTIVITY.this, EnfermedadSe.class);
                    startActivity(i);


                }catch (Exception e){

                    Toast.makeText(ENFERMEDAD_ACTIVITY.this,"Error al iniciar",Toast.LENGTH_SHORT).show();
                }

            }
        });


        ENTRENO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Intent i = new Intent(ENFERMEDAD_ACTIVITY.this, selhoja.class);
                    startActivity(i);

                }catch (Exception e){

                    Toast.makeText(ENFERMEDAD_ACTIVITY.this,"Error al iniciar",Toast.LENGTH_SHORT).show();
                }

            }
        });






    } //Oncreate
}
