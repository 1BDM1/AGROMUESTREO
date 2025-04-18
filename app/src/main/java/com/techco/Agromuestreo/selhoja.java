package com.techco.Agromuestreo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.techco.Agromuestreo.ui.SpinnerAdapter;
import com.techco.Agromuestreo.ui.SpinnerItem;
import com.techco.Agromuestreo.ui.Tablero;

import java.util.ArrayList;
import java.util.List;

public class selhoja extends AppCompatActivity {

    public Spinner HOJA;
    public String hoja,HOJA2,selectedText;
    public Button EMPEZAR,CLAS;
    public ImageView Ayuda;
    public ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selhoja);

        HOJA = findViewById(R.id.HOJA);
        EMPEZAR = findViewById(R.id.COMENZAR);
        Ayuda = findViewById(R.id.informacionE2);




        CLAS = findViewById(R.id.clasific);

        String [] Hojas = {"Linear lanceolada","Eliptica","Lobulada","Palmeada","Triangular"};

        //adapter = new ArrayAdapter<String>(selhoja.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Hojas);
        //HOJA.setAdapter(adapter);

        List<SpinnerItem> items = new ArrayList<>();
        items.add(new SpinnerItem("Linear lanceolada", R.drawable.linear__0));
        items.add(new SpinnerItem("Eliptica", R.drawable.eliptica_0));
        items.add(new SpinnerItem("Lobulada", R.drawable.lobulada_0));
        items.add(new SpinnerItem("Palmeada", R.drawable.palmeada_0));
        items.add(new SpinnerItem("Triangular", R.drawable.triangular_0));
        // Agrega más opciones según sea necesario

        SpinnerAdapter adapter = new SpinnerAdapter(this, items);
        HOJA.setAdapter(adapter);


// Configurar el Spinner y el adaptador como lo hicimos antes

        HOJA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem selectedItem = (SpinnerItem) parent.getSelectedItem();
                selectedText = selectedItem.getText();
                // Ahora puedes usar 'selectedText' como necesites
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Manejar el caso cuando no se selecciona nada, si es necesario
            }
        });



        EMPEZAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    HOJA2 = selectedText;
                    if (selectedText.equals("Linear lanceolada")){
                        hoja = "linear_";
                    }else if (selectedText.equals("Eliptica")){
                        hoja = "eliptica";
                    }else if (selectedText.equals("Lobulada")){
                        hoja = "lobulada";
                    }else if (selectedText.equals("Palmeada")){
                        hoja = "palmeada";
                    }else if (selectedText.equals("Triangular")){
                        hoja = "triangular";
                    }

                    //Toast.makeText(selhoja.this,hoja,Toast.LENGTH_SHORT).show();

                    Bundle eBundle = new Bundle();
                    eBundle.putString("hoja", hoja);
                    eBundle.putString("hojas" ,HOJA2);
                    Intent i = new Intent(selhoja.this, ENTRENAMIENTO.class);
                    i.putExtras(eBundle);
                    startActivity(i);

                }catch (Exception e){

                    Toast.makeText(selhoja.this,"Error al iniciar",Toast.LENGTH_SHORT).show();
                }
            }
        });


        Ayuda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(selhoja.this);
                LayoutInflater inflater = getLayoutInflater();
                View view  = inflater.inflate(R.layout.new_dialog,null);
                builder.setView(view);
                AlertDialog dialog = builder.create();

                ImageView imagen = view.findViewById(R.id.imagenin2);
                Button continuar = view.findViewById(R.id.Icontinuar2);


                imagen.setImageResource(R.drawable.hojas_ins);



                continuar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();

                    }
                });

                dialog.setCancelable(false);
                dialog.show();
            }

        });



        CLAS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Bundle eBundle = new Bundle();
                    eBundle.putString("hoja", hoja);
                    Intent i = new Intent(selhoja.this, Tablero.class);
                    i.putExtras(eBundle);
                    startActivity(i);
                    selhoja.this.finish();

                }catch (Exception e){

                    Toast.makeText(selhoja.this,"Error al iniciar",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }



}