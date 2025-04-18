package com.techco.Agromuestreo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.series.DataPoint;
import com.techco.Agromuestreo.Entidad.Registro;
import com.techco.Agromuestreo.Entidad.registro2;
import com.techco.Agromuestreo.Utilidades.CorrelationCalculator;
import com.techco.Agromuestreo.Utilidades.Entreno;
import com.techco.Agromuestreo.Utilidades.Utilidades;

import org.w3c.dom.Text;

public class ENTRENAMIENTO extends AppCompatActivity {


    //public ListView list;
    public ArrayAdapter<String> miA2;
    public ArrayList<String> arreglos = new ArrayList();
    public String numero,verdadero,a,AA,OR= "General";

    public ArrayList<Integer> availableNumbers = new ArrayList();
    public int drawableId;
    ConexionSQliteHelper con;
    registro2  registro;
    Context context = this;
    public Integer esta = 0,TIPO = 0;
    public ImageView imagen,ayuda;
    public EditText N;
    public FirebaseAuth mAuth;
    public String lista,lista2;
    public String fecha,nombre;
    DatabaseReference mRootReference;
    public  String id;
    public ArrayList<Double> arreglo = new ArrayList();
    public ArrayList<Double> arreglo2 = new ArrayList();
public ArrayList<Double> miarreglo = new ArrayList<>();
    public ArrayList<Double> miarreglo2 = new ArrayList<>();
    ArrayList<registro2> ListaRegistro = null;
public Double varianza_r,varian_c,sum,pre,med_r,med_c,Cb = 0.0,sistematico = 0.0,ubicacion= 0.0 ,r=0.0,Pc=0.0,PCS = 0.0;
    public Double PCLL = 0.0,PCE = 0.0,PCL = 0.0,PCP = 0.0,PCT = 0.0,PCG = 0.0;
public String clasificacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento);


        mRootReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        id = currentUser.getUid();

        ayuda = findViewById(R.id.informacionE3);
        N = findViewById(R.id.nivel_infeccion);
        //list = findViewById(R.id.lw);
        imagen = findViewById(R.id.hojas);

        Bundle eBundle = this.getIntent().getExtras();
        a = eBundle.getString("hoja");
        AA = eBundle.getString("hojas");

        inicializarListaValores();

        drawableId = getRandomDrawable(context, a,arreglo2,arreglo,availableNumbers);
        imagen.setImageResource(drawableId);
        nombre = currentUser.getDisplayName();




        N.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEND) {



                    if (Double.valueOf(String.valueOf(N.getText())) > 100 || Double.valueOf(String.valueOf(N.getText())) < 0 ){

                        Toast.makeText(context,"Solo valores entre 0 y 100",Toast.LENGTH_SHORT).show();

                    }else{


                        if (miarreglo.size() >= 20) {

                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ENTRENAMIENTO.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View view = inflater.inflate(R.layout.activity_instrucciones, null);
                            builder.setView(view);
                            AlertDialog dialog = builder.create();
                            TextView text = view.findViewById(R.id.instrucciones);
                            ImageView imagen = view.findViewById(R.id.imagenin);
                            TextView text2 = view.findViewById(R.id.valores);
                            Button continuar = view.findViewById(R.id.Icontinuar);
                            imagen.setImageResource(drawableId);
                            text.setText("ENTRENAMIENTO");
                            text2.setText("Valor real:" + " " + String.valueOf(arreglo2.get(arreglo2.size() - 1)));
                            continuar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    dialog.dismiss();
                                    pasar();
                                }
                            });
                            dialog.setCancelable(false);
                            dialog.show();

                        }//else del arreglo
                    } //else de tamaño



                    handled = true;
                }
                return handled;
            }
        });

        ayuda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ENTRENAMIENTO.this);
                LayoutInflater inflater = getLayoutInflater();
                View view  = inflater.inflate(R.layout.activity_instrucciones,null);
                builder.setView(view);
                AlertDialog dialog = builder.create();

                ImageView imagen = view.findViewById(R.id.imagenin);
                Button continuar = view.findViewById(R.id.Icontinuar);
                TextView text2 = view.findViewById(R.id.valores);

                imagen.setImageResource(R.drawable.instrucciones_entrenamiento);
                text2.setText("");


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

//Termina on create

        consulta();

    }

    private double entrenamiento_v(ArrayList<Double> miarreglo){
        sum = 0.0;
        miarreglo2.clear();
        Double sum2 = 0.0;
        pre = 0.0;
        double varianza = 0.0;
        Integer muestras = miarreglo.size();
        for (int i = 0; i < miarreglo.size(); i++) {
            sum = sum + miarreglo.get(i);
        }
        Double media = sum / muestras;
        for (int i = 0; i < miarreglo.size(); i++) {
            miarreglo2.add((miarreglo.get(i) - media)*(miarreglo.get(i) - media));
            sum2 = sum2 + miarreglo2.get(i);
        }
        varianza = sum2/(muestras - 1);
    return varianza;
    }

    private double medias(ArrayList<Double> arr){
        Integer muestras = arr.size();
        Double sum = 0.0;
        for (int i = 0; i < arr.size(); i++) {
             sum = sum + arr.get(i);
        }
       Double media = sum/muestras;
        return media;
    }

    public  void pasar(){
            miarreglo.add(Double.valueOf(String.valueOf(N.getText())));
            N.setText("");
            if (miarreglo.size() >= 20){
                Toast.makeText(context, "Muestreo terminado", Toast.LENGTH_SHORT).show();
                indicadores();
                Mensaje(Pc,r);
//datos_dialogo();

            }else {
                drawableId = getRandomDrawable(context, a, arreglo2, arreglo,availableNumbers);
                imagen.setImageResource(drawableId);

            }
    }

    public void indicadores (){
        DecimalFormat format = new DecimalFormat("#.00");
        varian_c = Math.sqrt(entrenamiento_v(miarreglo)); //SE CALCULA LA DESVIACIÓN ESTANDAR
        varianza_r = Math.sqrt(entrenamiento_v(arreglo2));
        med_c = medias(miarreglo);
        med_r = medias(arreglo2);
        sistematico = varian_c/varianza_r;
        Double a = (med_c - med_r);
        Double b = varianza_r*varian_c;
        Double c = Math.sqrt(b);
        ubicacion = a/c;
        Double d = 1/sistematico;
        Double e = Math.pow(ubicacion,2);
        Cb = 2/(sistematico + d + e);
        //X Y
        r = CorrelationCalculator.calculateCorrelation(arreglo2,miarreglo);
        //Toast.makeText(context, "CALC:"+ String.valueOf(varian_c)+ " " + "REAL" +String.valueOf(varianza_r), Toast.LENGTH_LONG).show();
        Pc = r*Cb;
    }

    public void datos_dialogo(){

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        lista = miarreglo.toString().replace("[","").replace("]","");
        lista2 = arreglo2.toString().replace("[","").replace("]","");
        String lista3 = lista + "*" + lista2 + "*" + Pc;
        Dialog dialog2 = builder2.create();
        builder2.setTitle("DATOS");
        builder2.setMessage(lista3);
        Dialog finalDialog = dialog2;
        builder2.setPositiveButton("Continuar muestreo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                finalDialog.dismiss();
                //lista = NULL;


            }


        });

        dialog2 = builder2.create();
        dialog2.setCancelable(false);
        dialog2.show();
    }

    private void inicializarListaValores() {
        // Añade los valores al ArrayList
       // double[] valores = {0, 1.65, 1.65, 4.38, 4.38, 7.58, 7.58, 12.71, 16.34, 19.09, 23.28, 25.44, 29, 33.5, 35.85, 37.22, 40.14, 43.95, 48.19, 52.4, 55.64, 59.72, 62.9, 65.95, 68.27};


            double[] valor_triangular =  {0, 1.65, 1.65, 4.38, 4.38, 7.58, 7.58, 12.71, 16.34, 19.09, 23.28, 25.44, 29, 33.5, 35.85, 37.22, 40.14, 43.95, 48.19, 52.4, 55.64, 59.72, 62.9, 65.95, 68.27,
                    0.38,0.82,0.96,2.10,2.92,4.35,5.94,7.59,9.11,12.45,18.59,21.26,25.91,28.33,29.97,34.71,37.41,41.51,43.05,45.56,47.77,49.83,52.03,55.54,60.45,
                    0.14,0.92,1.17,2.34,2.99,3.62,5.48,7.00,10.07,13.14,15.60,18.25,22.26,24.27,29.36,33.61,35.12,37.84,39.22,41.89,43.08,46.43,49.86,51.36,56.04,
                    0.43,0.89,1.14,2.60,4.16,6.16,8.00,9.90,12.20,14.62,16.68,18.14,21.11,24.33,27.26,29.11,31.25,33.10,35.33,37.10,39.00,43.05,46.87,49.92,52.11,
                    0.32,0.92,1.36,2.44,3.26,5.61,7.71,10.05,12.72,15.81,17.01,19.19,21.32,23.93,26.09,30.25,31.48,34.13,38.64,41.10,44.35,47.03,49.71,52.14,56.67};

       double[] valor_palmeada = {0, 1.65, 1.65, 4.38, 4.38, 7.58, 7.58, 12.71, 16.34, 19.09, 23.28, 25.44, 29, 33.5, 35.85, 37.22, 40.14, 43.95, 48.19, 52.4, 55.64, 59.72, 62.9, 65.95, 68.27,
            0.35,0.75,1.00,1.71,2.58,4.28,5.70,7.31,10.16,12.11,14.61,16.04,18.10,21.29,22.65,24.42,26.93,28.27,30.49,34.71,37.63,40.81,43.66,45.94,51.82,
0.37,0.66,1.87,3.14,5.68,8.08,9.21,10.46,13.18,15.34,17.00,19.64,21.42,23.15,26.54,28.60,30.84,32.35,35.36,38.45,40.35,42.02,44.51,49.08,55.35,
0.65,0.95,1.20,3.06,5.00,7.12,9.44,11.44,13.27,15.77,17.66,19.21,21.00,23.61,24.44,25.35,27.00,29.15,32.84,34.52,36.12,39.89,43.83,48.94,51.20,
0.78,1.19,3.45,4.20,5.16,7.50,9.03,11.10,13.95,16.16,18.53,20.00,22.40,24.32,27.21,28.83,31.66,35.67,39.01,40.65,42.80,44.19,46.00,52.91,55.42};

       double[] valor_lobulada = {
               0, 1.65, 1.65, 4.38, 4.38, 7.58, 7.58, 12.71, 16.34, 19.09, 23.28, 25.44, 29, 33.5, 35.85, 37.22, 40.14, 43.95, 48.19, 52.4, 55.64, 59.72, 62.9, 65.95, 68.27,
       0.82,1.61,2.07,3.70,5.59,7.69,9.00,10.74,12.38,15.27,17.72,20.47,22.00,24.65,26.93,28.10,30.00,32.58,34.20,36.03,38.53,41.65,46.20,48.66,52.61,
0.38,1.26,2.50,3.03,5.15,7.96,9.18,10.75,12.00,13.62,16.12,18.50,19.47,22.71,25.84,27.92,28.03,30.79,32.88,35.00,37.12,40.17,42.30,46.60,48.60,
1.04,1.88,2.52,3.94,5.40,7.60,9.21,12.32,14.19,17.21,21.05,22.46,25.75,27.00,28.88,31.99,33.32,36.20,39.88,41.82,43.06,45.00,48.40,52.53,54.22,
0.65,1.19,2.34,4.09,6.12,8.29,9.13,11.69,13.56,15.67,18.12,20.32,23.25,24.30,26.00,27.64,30.44,33.71,36.01,37.81,41.12,44.28,46.96,49.73,53.48
       };

       double[] valor_eliptica = {
               0, 1.65, 1.65, 4.38, 4.38, 7.58, 7.58, 12.71, 16.34, 19.09, 23.28, 25.44, 29, 33.5, 35.85, 37.22, 40.14, 43.95, 48.19, 52.4, 55.64, 59.72, 62.9, 65.95, 68.27,
               0.55,1.15,2.46,4.48,6.10,8.68,10.85,12.04,14.36,16.28,18.28,20.62,22.11,24.00,26.95,28.60,30.82,32.10,34.17,36.04,38.34,40.15,44.50,52.01,58.90,
               1.47,2.94,4.00,5.97,7.30,9.99,12.61,14.20,16.62,17.40,20.65,22.96,24.61,27.42,29.69,33.22,35.75,38.81,41.27,44.39,46.37,49.58,52.05,56.04,57.17,
               0.65,1.00,3.20,5.73,7.71,9.96,11.64,13.37,15.00,17.77,19.35,21.58,23.95,25.67,27.76,29.05,31.15,33.04,35.01,37.95,40.03,43.18,48.35,55.60,59.42,
               0.83,1.02,2.35,5.00,7.92,9.56,10.12,12.37,14.50,15.96,17.00,18.04,20.82,23.16,24.89,26.00,28.97,33.09,35.32,38.83,43.80,45.14,47.36,51.65,54.83
       };

       double[] valor_linear = {
               0, 1.65, 1.65, 4.38, 4.38, 7.58, 7.58, 12.71, 16.34, 19.09, 23.28, 25.44, 29, 33.5, 35.85, 37.22, 40.14, 43.95, 48.19, 52.4, 55.64, 59.72, 62.9, 65.95, 68.27,
               1.47,2.12,3.95,6.21,7.58,10.09,11.26,14.00,16.25,19.57,21.02,23.63,25.11,27.41,29.62,32.23,35.41,37.23,40.90,42.46,45.37,47.80,52.25,56.75,60.50,
               1.00,1.50,2.36,4.45,6.89,8.00,10.29,12.12,14.05,16.48,19.47,20.48,21.40,23.00,25.07,27.62,29.55,31.71,33.40,36.10,40.61,45.88,50.72,53.71,56.21,
               1.37,2.47,4.55,6.70,8.03,10.59,12.77,15.41,18.26,20.00,22.10,24.87,27.54,29.50,32.77,34.24,35.00,38.07,40.35,43.31,46.08,50.11,51.89,54.37,56.92,
               1.00,1.73,2.07,3.10,4.83,7.80,10.63,11.79,14.51,15.89,17.54,19.13,20.48,23.26,25.72,27.09,28.51,30.97,32.00,33.50,36.04,38.10,40.37,43.53,45.07
       };

       if (a.equals("triangular")){
                for (double valor : valor_triangular) {
                    arreglo.add(valor);
                }
       }else if (a.equals("palmeada")) {
           for (double valor : valor_palmeada) {
               arreglo.add(valor);
           }
       }else if (a.equals("lobulada")) {
           for (double valor : valor_lobulada) {
               arreglo.add(valor);
           }
       }else if (a.equals("eliptica")) {
           for (double valor : valor_eliptica) {
               arreglo.add(valor);
           }
       }else if (a.equals("linear_")) {
           for (double valor : valor_linear) {
               arreglo.add(valor);
           }
       }

        //for (double valor : valores) {
        //    arreglo.add(valor);
       // }


    }

    //drawableId = getRandomDrawable(context, a,arreglo2,arreglo,availableNumbers);

    public static int getRandomDrawable(Context context,String prefix,ArrayList<Double> arr,ArrayList<Double> in,ArrayList<Integer> num) {
            Random random = new Random();
            String drawableName = null;
            int randomNumber;

            //Toast.makeText(context,String.valueOf(in.size()),Toast.LENGTH_SHORT).show();
            // Genera un número aleatorio entre 0 y 24

        do {
            randomNumber = random.nextInt(in.size());
        } while (num.contains(randomNumber));

        arr.add(Double.valueOf(in.get(randomNumber)));
        num.add(randomNumber);


            // Construye el nombre del recurso drawable
             drawableName = prefix + "_" + randomNumber;
            // Toast.makeText(context,drawableName + " " + in.get(randomNumber),Toast.LENGTH_SHORT).show();

            int drawableId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
            return drawableId;

        }

        public void Mensaje (Double A,Double B){
            DecimalFormat format = new DecimalFormat("#0.00");
            AlertDialog.Builder builder = new AlertDialog.Builder(ENTRENAMIENTO.this);
            LayoutInflater inflater = getLayoutInflater();
            View view  = inflater.inflate(R.layout.activity_instrucciones,null);
            builder.setView(view);
            AlertDialog dialog = builder.create();

            clasific();

            TextView text3 = view.findViewById(R.id.instrucciones);
            ImageView imagen = view.findViewById(R.id.imagenin);
            Button continuar = view.findViewById(R.id.Icontinuar);
            TextView text2 = view.findViewById(R.id.valores);

            if (esta == 0){
                imagen.setImageResource(R.drawable.uno);
                text3.setText("NIVEL: SEMILLA (APRENDIZ)");
                clasificacion = "SEMILLA (APRENDIZ)";
            }else if(esta == 1){
                imagen.setImageResource(R.drawable.dos);
                text3.setText("NIVEL:BROTE (PRINCIPIANTE)");
                clasificacion = "BROTE (PRINCIPIANTE)";
            }else if(esta == 2){
                imagen.setImageResource(R.drawable.tres);
                text3.setText("NIVEL: PLÁNTULA (INTERMEDIO)");
                clasificacion = "PLÁNTULA (INTERMEDIO)";
            }else if(esta == 3){
                imagen.setImageResource(R.drawable.cuatro);
                text3.setText("NIVEL:ÁRBOL FRONDOSO (AVANZADO)");
                clasificacion = "ÁRBOL FRONDOSO (AVANZADO)";
            }else if(esta == 4){
                imagen.setImageResource(R.drawable.cinco);
                text3.setText("NIVEL: BOSQUE SABIO (EXPERTO)");
                clasificacion = "BOSQUE SABIO (EXPERTO)";
            }

            text2.setText("Pc:" + " " + String.valueOf(format.format(A)) + " " );


           // text2.setText();

            continuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    guardar();
                    ENTRENAMIENTO.this.finish();

                }
            });

            dialog.setCancelable(false);
            dialog.show();
        }


        public void guardar(){


        consulta();

        if (Pc > PCS){
            //Toast.makeText(this,"pc>pcs",Toast.LENGTH_SHORT   ).show();

            BASE_DATOS(AA,Pc);
            consulta_general();
            BASE_DATOS("General",PCG);
            subir_datos();
            Intent i = new Intent(ENTRENAMIENTO.this, MainActivity.class);
            startActivity(i);

        }else {

            Intent i = new Intent(ENTRENAMIENTO.this, MainActivity.class);
            startActivity(i);

        }
        }

        public void clasific(){
        if (Pc <= 0.85){
            esta = 0;
        }else if(Pc > 0.85 & Pc <= 0.90){
         esta = 1;
        }else if(Pc > 90 & Pc <= 0.95){
           esta = 2;
        }else if(Pc > 0.95 & Pc <= 0.97){
            esta = 3;
        }else if(Pc > 0.97){
            esta = 4;
        }

        }

        public void BASE_DATOS(String TIPO, Double IND){

        try {
            DecimalFormat format3 = new DecimalFormat("#.00");
            Entreno entrenoDb = new Entreno(this,"my_database", null, 1);
            SQLiteDatabase db = entrenoDb.getWritableDatabase();
            Calendar calendario = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fecha = dateFormat.format(calendario.getTime());

            ContentValues values = new ContentValues();
            values.put(Entreno.COLUMN_ID, String.valueOf(nombre));
            values.put(Entreno.CLASIFICACION, clasificacion);
            values.put(Entreno.HOJA, TIPO);
            values.put(Entreno.PC, format3.format(IND));
            values.put(Entreno.FECHA, fecha);


            Long id = db.insert(Entreno.TABLE_NAME, Entreno.CLASIFICACION, values);

if (TIPO.equals("General")){
    Toast.makeText(getApplicationContext(), "Registro exitoso" + " " + id, Toast.LENGTH_SHORT).show();
}
            db.close();


        }catch (Exception e){

        }

        }

    public void consulta(){
        try{

            SQLiteDatabase db = new Entreno(this, "my_database", null, 1).getReadableDatabase();
            ListaRegistro = new ArrayList<>();

         //   Cursor cursor = db.rawQuery(" SELECT MAX(" + Entreno.PC +") FROM " + Entreno.TABLE_NAME + " WHERE " + Entreno.HOJA + " = ?", new String[] {AA});

            Cursor cursor = db.rawQuery(" SELECT * FROM " + Entreno.TABLE_NAME + " WHERE " + Entreno.HOJA + " = ?", new String[] {AA});


            while (cursor.moveToNext()){
                registro = new registro2();
                registro.setHoja(cursor.getString(2));
                registro.setPC(cursor.getDouble(3));
                ListaRegistro.add(registro);
            }

            cursor.close();
            db.close();

           // Toast.makeText(this,String.valueOf(ListaRegistro.get(0).getPC()),Toast.LENGTH_SHORT   ).show();


            if (ListaRegistro.isEmpty()) {

                PCS=0.0;

            } else {

                PCS = ListaRegistro.get(ListaRegistro.size() - 1).getPC();

            }

            cursor.close();
            db.close();

        }catch (Exception e){
           Toast.makeText(this,"Error en la consulta",Toast.LENGTH_SHORT).show();

    }
        //Cursor cursor = db.rawQuery("SELECT * FROM " + Entreno.TABLE_NAME + " WHERE " + Entreno.HOJA + " = ?", new String[] {OR});

        //Cursor cursor = db.rawQuery("SELECT MAX(" + Entreno.PC + ")" + "," + Entreno.HOJA+ " FROM " + Entreno.TABLE_NAME,null);

    }

    public void consulta_general(){
        try{


           /*
            SQLiteDatabase db = new Entreno(this, "my_database", null, 1).getReadableDatabase();
            ListaRegistro = new ArrayList<>();

            Cursor cursor = db.rawQuery("SELECT * FROM "+ Entreno.TABLE_NAME,null);

            while (cursor.moveToNext()){
                registro = new registro2();
                registro.setHoja(cursor.getString(2));
                registro.setPC(cursor.getDouble(3));
                ListaRegistro.add(registro);
            }

            cursor.close();
            db.close();
            */

            SQLiteDatabase db = new Entreno(this, "my_database", null, 1).getReadableDatabase();
            ListaRegistro = new ArrayList<>();

            Cursor cursor = db.rawQuery("SELECT " + Entreno.HOJA + ", MAX(" + Entreno.PC + ") FROM " + Entreno.TABLE_NAME + " GROUP BY " + Entreno.HOJA, null);
            while (cursor.moveToNext()) {
                registro2 registro = new registro2();
                registro.setHoja(cursor.getString(0));
                registro.setPC(cursor.getDouble(1));
                ListaRegistro.add(registro);
            }
            cursor.close();
            db.close();

            for (int i = 0; i < ListaRegistro.size(); i++){
                if (ListaRegistro.get(i).getHoja().equals("Linear lanceolada")){
                    PCLL = ListaRegistro.get(i).getPC();
                }else if (ListaRegistro.get(i).getHoja().equals("Eliptica")){
                    PCE = ListaRegistro.get(i).getPC();
                }else if (ListaRegistro.get(i).getHoja().equals("Lobulada")){
                    PCL = ListaRegistro.get(i).getPC();
                }else if (ListaRegistro.get(i).getHoja().equals("Palmeada")){
                    PCP = ListaRegistro.get(i).getPC();
                }else if (ListaRegistro.get(i).getHoja().equals("Triangular")){
                    PCT = ListaRegistro.get(i).getPC();
                }else if (ListaRegistro.get(i).getHoja().equals("General")){
                    PCG = ListaRegistro.get(i).getPC();
                }
            }

     /*       for (int i = 0; i < ListaRegistro.size(); i++){
                if (ListaRegistro.get(i).getHoja().equals(AA)){
                    PCLL = ListaRegistro.get(i).getPC();
                }else if (ListaRegistro.get(i).getHoja().equals(AA)){
                    PCE = ListaRegistro.get(i).getPC();
                }else if (ListaRegistro.get(i).getHoja().equals(AA)){
                    PCL = ListaRegistro.get(i).getPC();
                }else if (ListaRegistro.get(i).getHoja().equals(AA)){
                    PCP = ListaRegistro.get(i).getPC();
                }else if (ListaRegistro.get(i).getHoja().equals(AA)){
                    PCT = ListaRegistro.get(i).getPC();
                }else if (ListaRegistro.get(i).getHoja().equals(AA)){
                    PCG = ListaRegistro.get(i).getPC();
                }
            }
            */

            if (ListaRegistro.isEmpty()) {

                PCG=0.0;

            } else {

                PCG = (PCLL + PCE + PCL + PCP + PCT)/5 ;

            }

            cursor.close();
            db.close();

        }catch (Exception e){
            Toast.makeText(this,"Error en la consulta",Toast.LENGTH_SHORT   ).show();

        }
    }


    public void PC(){
        if (AA.equals("Linear lanceolada")){
            TIPO = 0;
        }else if (AA.equals("Eliptica")){
            TIPO = 1;
        }else if (AA.equals("Lobulada")){
            TIPO = 2;
        }else if (AA.equals("Palmeada")){
            TIPO = 3;
        }else if (AA.equals("Triangular")){
            TIPO = 4;
        }
    }

    private  void subir_datos(){

        /*
ContentValues values = new ContentValues();
            values.put(Entreno.COLUMN_ID, String.valueOf(nombre));
            values.put(Entreno.CLASIFICACION, clasificacion);
            values.put(Entreno.HOJA, TIPO);
            values.put(Entreno.PC, format3.format(IND));
            values.put(Entreno.FECHA, fecha);

        String fecha = fecha;
        String plaga =ListaRegistro.get(ListaRegistro.size() - 1).getPlaga();
        String cultivo = ListaRegistro.get(ListaRegistro.size() - 1).getCultivo();
        Double densidad = ListaRegistro.get(ListaRegistro.size() - 1).getDensidad();
        String longuitud = ListaRegistro.get(ListaRegistro.size() - 1).getLonguitud();
        String latitud = ListaRegistro.get(ListaRegistro.size() - 1).getLatitud();
        String captura = ListaRegistro.get(ListaRegistro.size() - 1).getCapturas();
        String area = ListaRegistro.get(ListaRegistro.size() - 1).getArea();
        String tipo = ListaRegistro.get(ListaRegistro.size() - 1).getTIPO();

*/

        String captura = String.valueOf(AA);

        Map<String,Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("fecha",fecha);
        hopperUpdates.put("Usuario",String.valueOf(nombre));
        hopperUpdates.put("Hoja",captura);
        hopperUpdates.put("Indicador",Pc);
        hopperUpdates.put("etapa",clasificacion);


        mRootReference.child("Usuario").child(id).child(captura).setValue(hopperUpdates);



    }

}