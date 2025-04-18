package com.techco.Agromuestreo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.techco.Agromuestreo.Entidad.Registro;
import com.techco.Agromuestreo.Utilidades.Utilidades;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Grafica2 extends AppCompatActivity implements LocationListener{

    Double D = 0.25;
    Double a ;
    Double b ;
    Double t_n;
    public int NUMERO;
    Double E = 0.25,E2 = 0.15;
    EditText N;
    String fecha;
    double units;
    double units2;
    double units3;
    double me2,s2, me1,s;
    double unitsdos;
    public int check;
    double sum = 0, muestras = 0,sum2 = 0;
    int numero;
    TextView texto1,Seleccion;
    double T = 1, densidad;
    public  String longuitud,latitud,cultivo,plaga,area,muestreo,plg;

    //seguir muestreando
    int rem = 0,caso1=0,caso2=0,caso3 = 0,caso4 = 0,dbl=0;
    double pre = 0.0,para = 0.0;

    String NOMBRE_DIRECTORIO = "Agromuestreo";
    String NOMBRE_DOCUMENTO = "MUESTREO.pdf";

    String criterio;



    EditText txt2;

    private GraphView funcion;
    public ListView list;
    public ListView parametros; //
    //private BaseArray<?> miArreglo;
    public ArrayList<Integer> miArreglo = new ArrayList<>();
    public ArrayList<Integer> miArreglouno = new ArrayList<>();
    public ArrayList<Integer> miArreglodos = new ArrayList<>();
    public ArrayList<Integer> miArreglo3 = new ArrayList<>();
    public ArrayList<Integer> miArreglodoble = new ArrayList<>();
    public ArrayList<Double> miArreglo2 = new ArrayList<Double>();
    public ArrayAdapter<Integer> miA;
    public ArrayAdapter<String> miA2;
    public ImageView Ayuda;
    public ArrayList<String> arreglo = new ArrayList();
    public ArrayList<String> arreglo2 = new ArrayList(); //parametros en pantalla
    public ArrayAdapter<String> parametros_adapter; //adaptador parametros
    Button guardar,datos_dentro;
    public Double me;

    //Para consultar y actualizar base de datos

    ConexionSQliteHelper con;
    ArrayList<String> Listainformacion = null;
    ArrayList<Registro> ListaRegistro = null;

    //Para la ubicacion

    public LocationManager ubicacion;
    public LocationManager locationManager;

    //Para graficar

    public LineGraphSeries<DataPoint> series;
    public LineGraphSeries<DataPoint> series2;
    public  int pasar = 0 ;

    //Subir datos

    public String lista;
    public FirebaseAuth mAuth;

    //PARA SUBIR DATOS A REALTIME DATA BASE//

    DatabaseReference mRootReference;
    public  String id;
    public String etapa, etapa_do, etapa_uno,clasificacion;

    //ESTADISTICOS

    Double media;
    Double varianza;
    Double rvm;
    Double se;
    Double kuno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);



        Utils.getDatabase();
        con = new ConexionSQliteHelper(this, "db_Registro", null, 1);

        Ayuda = findViewById(R.id.AYUDA);
        list = findViewById(R.id.lw);
        texto1 = findViewById(R.id.suma);
        parametros = findViewById(R.id.parametros);
        N = findViewById(R.id.Numero);
        funcion = findViewById(R.id.Grafica);
        guardar = findViewById(R.id.guardar1);
        Seleccion = findViewById(R.id.PLAGA);
        datos_dentro = findViewById(R.id.Detallesbtn);


        funcion.getViewport().setScrollable(false);
        funcion.getViewport().setScrollableY(false);
        funcion.getViewport().setScalable(false);
        funcion.getViewport().setScalableY(false);
        funcion.getViewport().setXAxisBoundsManual(true);
        funcion.getViewport().setYAxisBoundsManual(true);
        funcion.getViewport().setMaxY(300);

        N.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

        datos_dentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Grafica.this,"correcto",Toast.LENGTH_SHORT).show();
               datos_dialogo();

            }
        });

        //ActionBar actionBar = getActionBar();
        //actionBar.hide();

        //OBTENER LOS VALORES DE LA PLAGA Y EL CULTIVO DESDE ACTIVIDADES ANTERIORES//
        Bundle eBundle = this.getIntent().getExtras();
        cultivo = eBundle.getString("cultivo");
        plaga = eBundle.getString("plaga");
        area = eBundle.getString("area");
        check = eBundle.getInt("Doble_M");
        muestreo  = "Muestreo secuencial";
        plg = eBundle.getString("plaga2");
        etapa_uno =  eBundle.getString("etapa1");
        etapa_do =  eBundle.getString("etapa2");
        etapa =  eBundle.getString("etapa1"); //SOLO PARA ENTRAR EN EL IF DEL TIPO DE ENTRADA EN EL EDITTEXT N, NO TIENE OTRA FUNCION
        clasificacion = "SEMILLA (APRENDIZ)";



        //N.setText(muestreo);

        //UBICACION //

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
            },2);
        }

        mRootReference = FirebaseDatabase.getInstance().getReference();

        Ayuda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(Grafica2.this);
                LayoutInflater inflater = getLayoutInflater();
                View view  = inflater.inflate(R.layout.activity_instrucciones,null);
                builder.setView(view);
                AlertDialog dialog = builder.create();

                ImageView imagen = view.findViewById(R.id.imagenin);
                Button continuar = view.findViewById(R.id.Icontinuar);

                imagen.setImageResource(R.drawable.instrucciones_2);

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


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        id = currentUser.getUid();

        texto1.setText("Bienvenido");

        //Parametro(cultivo,plaga,etapa);

        Seleccion.setText(plaga);


        numero = (int)(Math.random()*30+1);

        N.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEND) {

                    //Toast.makeText(Grafica2.this,N.getText().toString(),Toast.LENGTH_SHORT);
                    pasar2();

                    handled = true;
                }
                return handled;
            }
        } );

        //Permisos para generar los pdfs

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},1000);
        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  texto1.setText(String.valueOf(pasar));
                try {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Grafica2.this);
                    builder.setTitle("Atención");
                    builder.setMessage("Se han ingresado pocos datos");
                    builder.setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {


                        }
                    });
                    Dialog dialog = builder.create();



                    if( miArreglo.size() > 20) {

                            obtenerubicacion();
                            dialogo_doble(miArreglouno,miArreglodos);


                    }else{
                        dialog.show();

                    }

                }catch (Exception e){

                    Toast.makeText(Grafica2.this, "Ingrese mas datos para guardar",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void dialogo_doble(ArrayList<Integer> arr,ArrayList<Integer> brr){

        DecimalFormat format = new DecimalFormat("#.00");
        ArrayList<Integer> de;
        de = miArreglo;

        obtenerubicacion();

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        Dialog dialog2 = builder2.create();
        builder2.setTitle("¡ATENCIÓN!");
        builder2.setMessage("¿Está seguro de que desea guardar?");
        Dialog finalDialog = dialog2;
        builder2.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {



                datos_md(arr);
                Relacion_VM2(arr,sum);
                miArreglo = arr;
                etapa = etapa_uno;
                finalDialog.dismiss();
                consulta();
                Crear_pdf();
                onPause();
                Guardar();

                datos_md(brr);
                Relacion_VM2(brr,sum2);
                miArreglo = brr;
                etapa = etapa_do;
                consulta();
                Crear_pdf();
                onPause();
                Guardar();


                Intent i = new Intent(Grafica2.this, MainActivity.class);
                startActivity(i);
                Grafica2.this.finish();

            }


        });
        builder2.setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                finalDialog.dismiss();
                onPause();
                miArreglo = de;

            }


        });


        dialog2 = builder2.create();
        dialog2.setCancelable(false);
        dialog2.show();
    }



    public void pasar2(){
        try {
            DecimalFormat format = new DecimalFormat("#.00");
            DecimalFormat format2 = new DecimalFormat("#");
            Integer nu = Integer.parseInt(N.getText().toString());
            N.setText("");


            numero = (int)(Math.random()*30+1);
            miArreglo.add(nu);
            if (miArreglo.size() % 2 != 0){
                Seleccion.setText(plaga);
                sum = 0;
                miArreglouno.add(nu);
                DataPoint[] dataPoints = new DataPoint[miArreglouno.size()];
                for (int i = 0; i < miArreglouno.size(); i++) {
                    //T = T_n(i + 1);
                    sum = sum + miArreglouno.get(i);
                    if (units*units>0){
                        texto1.setText("Suma:" + "  " + format2.format(sum) + "\n" + "Densidad:" + "  " + format.format(sum / (i + 1) ) + "\n" + "Tamaño muestra:"+ " " + Math.round(units) + "\n" + "Muestreo Secuencial" );
                    }else {
                        texto1.setText("Suma:" + "  " + format2.format(sum2) + "\n" + "Densidad:" + "  " + format.format(sum2 / (i + 1))  + "\n" + "Muestreo Secuencial");
                    }
                    dataPoints[i] = new DataPoint((i + 1), sum);

                }
                Relacion_VM2(miArreglouno,sum);
                list.setAdapter(new Adaptador3(miArreglouno, this));

                muestras = miArreglodos.size();
                densidad = sum / muestras;

                me1 = sum / miArreglouno.size();
                s = Math.sqrt(varianza);
                Double a = Math.sqrt(miArreglouno.size());
                Double b = a * me1;
                pre = s / b;

                arreglo.clear();
                arreglo.add("Pasos:" + " " + numero);
                arreglo.add("Precisión: " + " " + format.format((1 - pre
                ) * 100) + " " + "%");
                arreglo.add("R V M:" + " \t " + format.format(rvm));
                arreglo.add("E E:" + " \t " + format.format(se) );

                miA2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);
                parametros.setAdapter(miA2);

                series = new LineGraphSeries<>(dataPoints);
                series.setColor(Color.GREEN);
                funcion.getViewport().setMaxX(miArreglouno.size() + 2);
                funcion.getViewport().setMaxY(Math.max(sum,sum2) + 5);
                funcion.addSeries(series);

            }else{

                Seleccion.setText(plg);
                sum2 = 0;
                miArreglodos.add(nu);
                DataPoint[] dataPoints2 = new DataPoint[miArreglodos.size()];
                for (int i = 0; i < miArreglodos.size(); i++) {

                    sum2 = sum2 + miArreglodos.get(i);
                    if (units*units>0){
                        texto1.setText("Suma:" + "  " + format2.format(sum) + "\n" + "Densidad:" + "  " + format.format(sum / (i + 1) ) + "\n" + "Tamaño muestra:"+ " " + Math.round(units) + "\n" + "Muestreo Secuencial" );
                    }else {
                        texto1.setText("Suma:" + "  " + format2.format(sum2) + "\n" + "Densidad:" + "  " + format.format(sum2 / (i + 1)) + "\n" + "Muestreo Secuencial");
                    }
                    dataPoints2[i] = new DataPoint((i + 1), sum2);
                }
                me2 = sum2 / miArreglodos.size();
                s2 = Math.sqrt(varianza);
                Double a = Math.sqrt(miArreglodos.size());
                Double b = a * me2;
                pre = s2 / b;
                Relacion_VM2(miArreglodos,sum2);
                list.setAdapter(new Adaptador3(miArreglodos, this));
                muestras = miArreglodos.size();
                densidad = sum2 / muestras;
                arreglo.clear();
                arreglo.add("Pasos:" + " " + numero);
                arreglo.add("Precisión: " + " " + format.format((1 - pre
                ) * 100) + " " + "%");
                arreglo.add("R V M:" + " \t " + format.format(rvm));
                arreglo.add("E E:" + " \t " + format.format(se) );



                series = new LineGraphSeries<>(dataPoints2);
                series.setColor(Color.BLACK);
                funcion.getViewport().setMaxX(miArreglodos.size() + 2);
                funcion.getViewport().setMaxY(Math.max(sum,sum2) + 5);
                funcion.addSeries(series);


                miA2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);
                parametros.setAdapter(miA2);
            }


            double sum3 = sum + sum2;

            if (units3 >= units2) {
                miArreglodoble = miArreglouno;
            }else {
                miArreglodoble = miArreglodos;
            }


            if (miArreglo.size() == 50 & sum3 != 0){

                units3 = Math.pow(s / (E * me1), 2);
                units2 = Math.pow(s2 / (E * me2), 2);


                units = Math.max(units3,units2);

                if(units <= 5){
                    units = units + 25;
                }else if(units <= 15 & units > 5){
                    units = units + 20;
                }
                //texto1.setText(String.valueOf(units));
                graficar_v();
                graficar_v2();
                //Toast.makeText(this,String.valueOf(units),Toast.LENGTH_SHORT).show();


                if (units <= 25) {

                    dialogo_personalizado2(pre);
                    //rem = 1;
                    caso1 = 1;

                }

                if (units > 25 & units <= 30) {


                    //rem = 1;
                    caso2 = 1;

                }

                if (units > 30) {

                    //rem = 1;
                    caso3 = 1;

                }
                Toast.makeText(this, String.valueOf(caso1)+String.valueOf(caso2)+String.valueOf(caso3), Toast.LENGTH_SHORT).show();

            }else if(miArreglo.size() == 50 & sum3 == 0 ){
                caso4 = 1;
//                Toast.makeText(this, String.valueOf(caso1)+String.valueOf(caso2)+String.valueOf(caso3), Toast.LENGTH_SHORT).show();
            }

                Toast.makeText(this, String.valueOf(miArreglodoble.size()), Toast.LENGTH_SHORT).show();

            if (caso1 == 1){
                if (miArreglodoble.size() == 30){

                    dialogo_personalizado2(pre);
                }
            }

            if (caso2 == 1) {

                unitsdos = Math.round(units);
                if (miArreglodoble.size() == units){

                    dialogo_personalizado2(pre);
                }
            }
            if (caso3 == 1){

                if (miArreglodoble.size() == 30){

                    dialogo_personalizado2(pre);

                }

                unitsdos = Math.round(units);
                if (miArreglodoble.size() == Math.round(units)){

                    dialogo_personalizado2(pre);
                }
            }
            if (caso4 == 1){

                if (miArreglodoble.size() == 100){
                    dialogo_personalizado2(pre);
                }
            }




       }catch (Exception E){

            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        }

    }

    public void dialogo_personalizado2(double parametro){

        para = parametro;
        para = (1-para)*100;
        ArrayList<Integer> de;


        AlertDialog.Builder builder = new AlertDialog.Builder(Grafica2.this);
        LayoutInflater inflater = getLayoutInflater();
        DecimalFormat format = new DecimalFormat("#.00");
        para = Double.parseDouble(format.format((para)));
        muestras = miArreglo.size();
        densidad = Double.parseDouble(format.format(sum / muestras));
        obtenerubicacion();

        View view  = inflater.inflate(R.layout.alert_dialog2,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        TextView resultados = view.findViewById(R.id.resultados);
        Button btneliminar =  view.findViewById(R.id.eliminar);
        Button btnguardar = view.findViewById(R.id.guardar);
        //la precision de:" +" " + String.valueOf(format.format(para))+ " " +"%" +
        resultados.setText("Se ha alcanzado el tamaño de muestra calculado"+ "\n" + "Puede continuar muestreando para mejorar la precision");

        if(rvm < 0.70){
            criterio = ("N/A");
        }if (rvm >= 0.70 & rvm <= 1.3){
            criterio = ("Población al azar");
        }if (rvm > 1.3 & rvm <= 2.0){
            criterio = ("Población ligeramente agregada");
        }if (rvm > 2.0 & rvm <= 4.0){
            criterio = ("Población con agregación fuerte");
        }if (rvm > 4){
            criterio = ("Población muy fuertemente agregada");
        }


        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                onPause();
            }
        });

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    datos_md(miArreglouno);
                    Relacion_VM2(miArreglouno,sum);
                    miArreglo = miArreglouno;
                    etapa = etapa_uno;
                    consulta();
                    Crear_pdf();
                    onPause();
                    Guardar();

                    datos_md(miArreglodos);
                    Relacion_VM2(miArreglodos,sum2);
                    miArreglo = miArreglodos;
                    etapa = etapa_do;
                    consulta();
                    Crear_pdf();
                    onPause();
                    Guardar();

                    dialog.dismiss();
                    Intent i = new Intent(Grafica2.this, MainActivity.class);
                    startActivity(i);
                    Grafica2.this.finish();




            }
        });

        //  miA2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);
        //resultados.setAdapter(miA2);

    }

    public byte[] toByteArray(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void Crear_pdf(){
        Document documento = new Document();
        try {

            ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            @SuppressLint("MissingPermission") Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double lon = loc.getLongitude();
            double lat = loc.getLatitude();

            // Convertir los grados decimales a grados enteros
            int latitudGrados = (int) lat;
            int longitudGrados = (int) lon;

// Convertir la parte decimal de los grados a minutos
            double latitudMinutosDecimal = (lat - latitudGrados) * 60;
            double longitudMinutosDecimal = (lon - longitudGrados) * 60;

// Convertir los minutos decimales a minutos enteros
            int latitudMinutos = (int) latitudMinutosDecimal;
            int longitudMinutos = (int) longitudMinutosDecimal;

// Convertir la parte decimal de los minutos a segundos
            double latitudSegundos = (latitudMinutosDecimal - latitudMinutos) * 60;
            double longitudSegundos = (longitudMinutosDecimal - longitudMinutos) * 60;

// Formatear la latitud y la longitud con los símbolos correspondientes
            latitud = String.format("%d° %d' %.2f\" %s", Math.abs(latitudGrados), Math.abs(latitudMinutos), Math.abs(latitudSegundos), latitudGrados < 0 ? "S" : "N");
            longuitud = String.format("%d° %d' %.2f\" %s", Math.abs(longitudGrados), Math.abs(longitudMinutos), Math.abs(longitudSegundos), longitudGrados < 0 ? "O" : "E");

            String tp;
            if (muestreo.equals("Muestreo proporción")){
                tp = "Proporción";
            }else{
                tp = "Densidad";
            }

            DecimalFormat format = new DecimalFormat("#.00");
            lista = miArreglo.toString().replace("[","").replace("]","");
            Calendar calendario = Calendar.getInstance();
            fecha = DateFormat.getDateInstance().format(calendario.getTime());
            NUMERO = ListaRegistro.size() + 1;
            File file = CrearFichero("Muestreo" +  " " + String.valueOf(NUMERO) + ".pdf");
            FileOutputStream ficheroPDF = new FileOutputStream(file.getAbsolutePath());

            Image image = Image.getInstance(toByteArray(Grafica2.this.getResources().getDrawable(R.drawable.agromneg)));
            image.scalePercent(10);
            image.setAlignment(Element.ALIGN_CENTER);

            if (image.isImgTemplate() || image.isImgRaw()) {
                //Toast.makeText(this,"SI",Toast.LENGTH_SHORT).show();
            } else {
                // hacer algo si la imagen no es válida
            }

            //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.statistics);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //bm.compress(Bitmap.CompressFormat.PNG, 10, stream);
            Image img = null;
            byte[] byteArray = stream.toByteArray();
            try {
                img = Image.getInstance(byteArray);
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            PdfWriter writer =  PdfWriter.getInstance(documento,ficheroPDF);

            //PdfWriter writer = PdfWriter.getInstance(documento,ficheroPDF);
            //PdfWriter writer =  PdfWriter.getInstance(documento,ficheroPDF);

            if (etapa.equals("Enfermedad")){

                documento.open();
                documento.add(image);
                //documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("                                                        " + "RESUMEN DEL MUESTREO" + "                                                          "));
                //documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("                                                                      " + fecha));
                // documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("-------------------------------------------" + "Los datos del muestreo son los siguientes:" + "-------------------------------"));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph(                                                     "Nivel del muetreador:" + " " + clasificacion));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Longitud:" + " " + longuitud + "                                                                           " + "Latitud:" + latitud));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Cultivo evaluada:" + "     " + cultivo));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Enfermedad evaluada:" + "     " + plaga));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Area evaluada:" + "     " + area));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph(tp +" "+"estimada:" + "     " + format.format(densidad)));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Datos registrados:" + "\n" + "\n" + String.valueOf(lista)));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Relacion varianza media:" + "     " + (format.format(rvm))));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Error estandar:" + "     " + (format.format(se))));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Precisión Alcanzada" + "     " + format.format( (1 - pre)*100) ));


            }else {

                documento.open();
                documento.add(image);
                //documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("                                                        " + "RESUMEN DEL MUESTREO" + "                                                          "));
                //documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("                                                                      " + fecha));
                // documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("-------------------------------------------" + "Los datos del muestreo son los siguientes:" + "-------------------------------"));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Longitud:" + " " + longuitud + "                                                                           " + "Latitud:" + latitud));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Cultivo muestreado:" + "     " + cultivo));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Plaga muestreada:" + "     " + plaga));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Estado biológico:" + "     " + etapa));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Area muestreada:" + "     " + area));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph(tp + " " + "estimada:" + "     " + format.format(densidad)));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Datos muestreados:" + "\n" + "\n" + String.valueOf(lista)));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Relacion varianza media:" + "     " + (format.format(rvm))));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Error estandar:" + "     " + (format.format(se))));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph(criterio));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Precisión Alcanzada" + "     " + format.format((1 - pre) * 100)));
            }
        }catch (DocumentException e ){


            Toast.makeText(this, (CharSequence) e,Toast.LENGTH_SHORT).show();
        }catch (IOException e){

            Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show();

        }

        finally {

            documento.close();

        }
    }

    public File CrearFichero(String nombreFichero) {

        File ruta = getRuta();
        File fichero = null;

        if(ruta != null){

            fichero = new File(ruta,nombreFichero);
            while (fichero.exists()) {
                NUMERO++;
                nombreFichero = "Muestreo" + " " + String.valueOf(NUMERO) + ".pdf";
                fichero = new File(ruta, nombreFichero);
            }

        }

        return fichero;
    }

    public File getRuta(){
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), NOMBRE_DIRECTORIO);
            if (ruta != null){
                // Crear la carpeta si no existe
                ruta.mkdir();
            }
        }
        return  ruta;
    }



    public void datos_md(ArrayList<Integer> a){
        sum = 0;
        for (int i = 0; i < a.size(); i++) {

            sum = sum + a.get(i);
        }
        Relacion_VM2(a,sum);
        muestras = a.size();
        densidad = sum / muestras;
        double me = sum / a.size();
        double s = Math.sqrt(varianza);
        Double c = Math.sqrt(a.size());
        Double b = c * me;
        pre = s / b;

        if (miArreglodos.equals(a)){
            plaga = plg;
        }

    }

    public void Relacion_VM2(ArrayList<Integer> a,double s){

        Double sum2 = 0.0;
        miArreglo2.clear();
        varianza = 0.0;
        media = s/a.size();
        for (int i=0; i < a.size();i++){
            miArreglo3.add(a.get(i));
            miArreglo2.add((a.get(i) - media)*(a.get(i) - media));
            sum2 = sum2 + miArreglo2.get(i);
        }
        //txt2.setText(miArreglo3.toString());
        varianza = sum2/(a.size() - 1);

        rvm = varianza/media;

        Double n = Math.sqrt(a.size());
        Double de = Math.sqrt(varianza);
        se = de/n; //error estandar

        double k1 = varianza - media;
        double k2 = varianza/a.size();
        double y2 = Math.pow(media,2);
        kuno = k1/(y2 - k2);

        if(rvm < 0.70){
            criterio = ("N/A");
        }if (rvm >= 0.70 & rvm <= 1.3){
            criterio = ("Población al azar");
        }if (rvm > 1.3 & rvm <= 2.0){
            criterio = ("Población ligeramente agregada");
        }if (rvm > 2.0 & rvm <= 4.0){
            criterio = ("Población con agregación fuerte");
        }if (rvm > 4){
            criterio = ("Población muy fuertemente agregada");
        }

    }

    public void editar(View view) {
        try {
            DecimalFormat format = new DecimalFormat("#.00");
            DecimalFormat format2 = new DecimalFormat("#");

            funcion.removeAllSeries();

                if (miArreglo.size() % 2 == 0){
                    miArreglouno.remove(miArreglouno.size() - 1);
                }else{
                    miArreglodos.remove(miArreglodos.size() - 1);
                }

                Toast.makeText(this, "Se ha eliminado el ultimo elemento", Toast.LENGTH_SHORT).show();
                miArreglo.remove(miArreglo.size() - 1);
                double sum3 = sum + sum2;

                if (miArreglo.size() >= 50 & sum3 != 0){
                    graficar_v();
                    graficar_v2();
                }



        } catch (Exception e) {
            Toast.makeText(this, "No hay datos para eliminar", Toast.LENGTH_SHORT).show();
        }


    }

    public void graficar_v(){
        DataPoint[] dataPoints = new DataPoint[2];

        dataPoints[0] = new DataPoint(30, 0);
        dataPoints[1] = new DataPoint(30,1050);

        series2 = new LineGraphSeries<>(dataPoints);
        series2.setColor(Color.YELLOW);
        funcion.addSeries(series2);
    }

    public void graficar_v2(){
        DataPoint[] dataPoints = new DataPoint[2];
        double units3 = Math.round(units);

        dataPoints[0] = new DataPoint(units3, 0);
        dataPoints[1] = new DataPoint(units3,1050);

        series2 = new LineGraphSeries<>(dataPoints);
        series2.setColor(Color.RED);
        funcion.addSeries(series2);
    }

    @Override
    public void onLocationChanged( Location location) {

        try {
            Geocoder geocoder = new Geocoder(Grafica2.this, Locale.getDefault());
            List<Address> direccion  = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),2);

            latitud = String.valueOf(direccion.get(0).getLatitude());
            longuitud = String.valueOf(direccion.get(1).getLongitude());
            if (latitud == null){
                ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                @SuppressLint("MissingPermission") Location loc = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                latitud = String.valueOf(loc.getLatitude());
            }
            if (longuitud == null){
                ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                @SuppressLint("MissingPermission") Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                longuitud = String.valueOf(loc.getLongitude());
            }

        }catch (Exception e){

        }
    }

    private  void subir_datos(){
        SQLiteDatabase db = con.getReadableDatabase();

        Registro registro ;
        ListaRegistro = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_REGISTRO ,null);

        while (cursor.moveToNext()){

            registro = new Registro();
            registro.setFecha(cursor.getString(2));
            registro.setCultivo(cursor.getString(0));
            registro.setPlaga(cursor.getString(1));
            registro.setDensidad(cursor.getDouble(3));
            registro.setLatitud(cursor.getString(4));
            registro.setLonguitud(cursor.getString(5));
            registro.setArea(cursor.getString(6));
            registro.setCapturas(cursor.getString(8));
            registro.setTIPO(cursor.getString(9));
            registro.setETAPA(cursor.getString(10));

            ListaRegistro.add(registro);
        }
        cursor.close();
        db.close();


        String fecha = ListaRegistro.get(ListaRegistro.size() - 1).getFecha();
        String plaga =ListaRegistro.get(ListaRegistro.size() - 1).getPlaga();

        //
        //
        String cultivo = ListaRegistro.get(ListaRegistro.size() - 1).getCultivo();
        Double densidad = ListaRegistro.get(ListaRegistro.size() - 1).getDensidad();
        String longitud = ListaRegistro.get(ListaRegistro.size() - 1).getLonguitud();
        String latitud = ListaRegistro.get(ListaRegistro.size() - 1).getLatitud();
        String captura = ListaRegistro.get(ListaRegistro.size() - 1).getCapturas();
        String area = ListaRegistro.get(ListaRegistro.size() - 1).getArea();
        String tipo = ListaRegistro.get(ListaRegistro.size() - 1).getTIPO();


        if (check == 1){
            etapa = "N/A";
        }

        Map<String,Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("fecha",fecha);
        hopperUpdates.put("plaga",plaga);
        hopperUpdates.put("cultivo",cultivo);
        hopperUpdates.put("densidad",densidad);
        hopperUpdates.put("longitud",longitud);
        hopperUpdates.put("latitud",latitud);
        hopperUpdates.put("muestra",area);
        hopperUpdates.put("captura",captura);
        hopperUpdates.put("etapa",etapa);
        hopperUpdates.put("tipo",tipo);

        String uniqueKey = String.valueOf(System.currentTimeMillis());
        mRootReference.child("Usuario").child(id).child(uniqueKey).setValue(hopperUpdates);



    }

    public void Guardar( ) {
//
        //if (miArreglo.size() > 5){
        // try {
        DecimalFormat format = new DecimalFormat("#.00");
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //fecha = DateFormat.getDateInstance().format(calendario.getTime())   ;
        fecha = dateFormat.format(calendario.getTime());
        //fecha = "2023-04-14";


        muestras = miArreglo.size();

        densidad = Double.parseDouble(format.format(sum / muestras));
        format.format(densidad);
        lista = miArreglo.toString().replace("[", "").replace("]", "");
        //lista = convertArrayToString(miArreglo);



            ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            @SuppressLint("MissingPermission") Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double lon = loc.getLongitude();
            double lat = loc.getLatitude();

            // Convertir los grados decimales a grados enteros
            int latitudGrados = (int) lat;
            int longitudGrados = (int) lon;

// Convertir la parte decimal de los grados a minutos
            double latitudMinutosDecimal = (lat - latitudGrados) * 60;
            double longitudMinutosDecimal = (lon - longitudGrados) * 60;

// Convertir los minutos decimales a minutos enteros
            int latitudMinutos = (int) latitudMinutosDecimal;
            int longitudMinutos = (int) longitudMinutosDecimal;

// Convertir la parte decimal de los minutos a segundos
            double latitudSegundos = (latitudMinutosDecimal - latitudMinutos) * 60;
            double longitudSegundos = (longitudMinutosDecimal - longitudMinutos) * 60;

// Formatear la latitud y la longitud con los símbolos correspondientes
            latitud = String.format("%d° %d' %.2f\" %s", Math.abs(latitudGrados), Math.abs(latitudMinutos), Math.abs(latitudSegundos), latitudGrados < 0 ? "S" : "N");
            longuitud = String.format("%d° %d' %.2f\" %s", Math.abs(longitudGrados), Math.abs(longitudMinutos), Math.abs(longitudSegundos), longitudGrados < 0 ? "O" : "E");


            DecimalFormat format3 = new DecimalFormat("#.00");
            con = new ConexionSQliteHelper(Grafica2.this, "db_Registro", null, 1);

            SQLiteDatabase db = con.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Utilidades.CAMPO_CULTIVO, cultivo); // + "Y" + String.valueOf(para));
            values.put(Utilidades.CAMPO_PLAGA, plaga);
            values.put(Utilidades.CAMPO_FECHA, fecha);
            values.put(Utilidades.CAMPO_DENSIDAD, densidad);
            values.put(Utilidades.CAMPO_LATITUD, latitud);
            values.put(Utilidades.CAMPO_LONGUITUD, longuitud);
            values.put(Utilidades.CAMPO_TIPO, muestreo);
            values.put(Utilidades.CAMPO_DATOS, lista);
            values.put(Utilidades.CAMPO_MUESTRA, area);
            values.put(Utilidades.CAMPO_ETAPA,etapa);
            values.put(Utilidades.CAMPO_PRE, format3.format((1-pre)*100) );

            Long id = db.insert(Utilidades.TABLA_REGISTRO, Utilidades.CAMPO_LONGUITUD, values);

            Toast.makeText(getApplicationContext(), "Registro exitoso" + " " + id, Toast.LENGTH_SHORT).show();

            db.close();
            subir_datos();





    }
    @SuppressLint("MissingPermission")
    public void obtenerubicacion(){
        try {
            locationManager = (LocationManager) Grafica2.this.getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 100, this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates( this);
        }
    }

    @Override
    public void onLocationChanged( List<Location> locations) {

    }

    @Override
    public void onFlushComplete(int requestCode) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled( String provider) {

    }

    @Override
    public void onProviderDisabled( String provider) {

    }

    public void consulta(){
        try{
            SQLiteDatabase db = con.getReadableDatabase();

            Registro registro ;
            ListaRegistro = new ArrayList<>();

            Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_REGISTRO,null);

            while (cursor.moveToNext()){

                registro = new Registro();
                registro.setCapturas(cursor.getString(6));

                ListaRegistro.add(registro);
            }
            cursor.close();
            db.close();
            //obtenerlista();
        }catch (Exception e){
            Toast.makeText(this,"Error en la consulta",Toast.LENGTH_SHORT   ).show();
        }
    }

    public void datos_dialogo(){

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        lista = miArreglo.toString().replace("[","").replace("]","");
        Dialog dialog2 = builder2.create();
        builder2.setTitle("DATOS");
        builder2.setMessage(lista);
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



}



