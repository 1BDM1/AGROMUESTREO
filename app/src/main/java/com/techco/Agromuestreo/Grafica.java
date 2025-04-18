package com.techco.Agromuestreo;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.app.ActionBar;


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
import android.graphics.BitmapFactory;
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
import android.os.Handler;
import android.text.InputType;
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
import android.widget.Toast;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Header;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfWriter;
import com.techco.Agromuestreo.Entidad.Registro;
import com.techco.Agromuestreo.Utilidades.Utilidades;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Grafica extends AppCompatActivity implements LocationListener{

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
    public ArrayList<Double> miArreglo = new ArrayList<>();
    public ArrayList<Double> miArreglouno = new ArrayList<>();
    public ArrayList<Double> miArreglodos = new ArrayList<>();
    public ArrayList<Double> miArreglo3 = new ArrayList<>();
    public ArrayList<Double> miArreglodoble = new ArrayList<>();
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
        //siguiente = findViewById(R.id.Siguiente);
        //txt2 = findViewById(R.id.Numero);
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
        //check = eBundle.getInt("MD");
        check = eBundle.getInt("Doble_M");
        muestreo = eBundle.getString("muestreo");


        if (check == 1 ){
            //String clt = eBundle.getString("cl");
            //plg = eBundle.getString("pl");
            plg = eBundle.getString("plaga2");
            muestreo  = "Muestreo secuencial";


            etapa_uno =  eBundle.getString("etapa1");
            etapa_do =  eBundle.getString("etapa2");
            etapa =  eBundle.getString("etapa1"); //SOLO PARA ENTRAR EN EL IF DEL TIPO DE ENTRADA EN EL EDITTEXT N, NO TIENE OTRA FUNCION
            //Toast.makeText(this,etapa_uno + " " + etapa_do , Toast.LENGTH_SHORT).show();
        }else{
            etapa = eBundle.getString("etapa");
        }

        if (etapa.equals("Enfermedad")){

            N.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);



                    clasificacion = eBundle.getString("nivel");

        }else{


            N.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
            ArrayList<Integer> miArreglo = new ArrayList<>();
            clasificacion = "SEMILLA (APRENDIZ)";


        }

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


                AlertDialog.Builder builder = new AlertDialog.Builder(Grafica.this);
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

        Parametro(cultivo,plaga,etapa);

        if (pasar == 1  ){
            grafica();

        }
        if (check == 1){
            Seleccion.setText(plaga);
        }

        numero = (int)(Math.random()*30+1);

        N.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEND) {

if(check == 0){
                    if (pasar == 1 && muestreo.equals("Muestreo secuencial")) {

                        grafica();
                        Pasar();

                    } else {

                        if (miArreglo.size() <= 25) {

                            RVM();

                        } else {

                            RVM2();

                        }

                    }

                }else{

    pasar2();
}
                    handled = true;
                }
                return handled;
            }
        });

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
          if( miArreglo.size() > 10 & check == 0| check == 1 & miArreglo.size() > 10) {
              if (check == 0) {

                  obtenerubicacion();
                  dialogo();

              }else if(check == 1 ){

                  obtenerubicacion();
                  dialogo_doble(miArreglouno,miArreglodos);

              }
          }else{
              Toast.makeText(Grafica.this,"Ingrese mas datos para guardar",Toast.LENGTH_SHORT).show();
          }

      }catch (Exception e){

          //Toast.makeText(this, "Error al guardar",Toast.LENGTH_SHORT).show();
      }
            }
        });


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

            Image image = Image.getInstance(toByteArray(Grafica.this.getResources().getDrawable(R.drawable.agromneg)));
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





    public double T_n(int n) {
        Parametro(cultivo,plaga,etapa);
        t_n = Math.exp(Math.log(Math.pow(D, 2) / a) / (b - 2) + ((b - 1) / (b - 2)) * Math.log(n));

        return (t_n);
    }

    public void dialogo_personalizado2(double parametro){

        para = parametro;
        para = (1-para)*100;
        ArrayList<Integer> de;


        AlertDialog.Builder builder = new AlertDialog.Builder(Grafica.this);
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
        //arreglo.add("Precision alcanzada: " + " " + format.format(pre));
        //arreglo.add("Relacion Varianza Media:" + " \t " + format.format(rvm));
        //arreglo.add("Error estandar:" + " \t " + format.format(se) );
        //arreglo.add("Indice de agregación de Kuno:" + "  \t  " + format.format(kuno));
        // arreglo.add(String.valueOf(varianza));
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

                if (check == 1){

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
                    Intent i = new Intent(Grafica.this, MainActivity.class);
                    startActivity(i);
                   Grafica.this.finish();

                }else {

                    Consulta(view);
                    Crear_pdf();
                    dialog.dismiss();
                    onPause();
                    Guardar();
                }



            }
        });

      //  miA2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);
        //resultados.setAdapter(miA2);

    }

    //Grafica.this.finish();

    public void datos_md(ArrayList<Double> a){
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


    public void dialogo_doble(ArrayList<Double> arr,ArrayList<Double> brr){

        DecimalFormat format = new DecimalFormat("#.00");
        ArrayList<Double> de;
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


                Intent i = new Intent(Grafica.this, MainActivity.class);
                startActivity(i);
                Grafica.this.finish();

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


    public void dialogo(){

        DecimalFormat format = new DecimalFormat("#.00");

        obtenerubicacion();

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        Dialog dialog2 = builder2.create();
        builder2.setTitle("¡ATENCIÓN!");
        builder2.setMessage("¿Está seguro de que desea guardar?");
        Dialog finalDialog = dialog2;
        builder2.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                finalDialog.dismiss();
                consulta();
                Crear_pdf();
                onPause();
                Guardar();

            }


        });
        builder2.setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                finalDialog.dismiss();
                onPause();

            }


        });


        dialog2 = builder2.create();
        dialog2.setCancelable(false);
        dialog2.show();
    }

    public void Relacion_VM2(ArrayList<Double> a,double s){

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

    public void pasar2(){
        try {
            DecimalFormat format = new DecimalFormat("#.00");
            DecimalFormat format2 = new DecimalFormat("#");
            double nu = Double.parseDouble(N.getText().toString());


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
                funcion.getViewport().setMaxY(Math.max(sum,sum2) + 3);
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
                funcion.getViewport().setMaxY(Math.max(sum,sum2) + 3);
                funcion.addSeries(series);


                miA2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);
                parametros.setAdapter(miA2);
            }


            //texto1.setText(String.valueOf(units3));
            if (units3 > units2) {

                miArreglodoble = miArreglouno;
                //texto1.setText(String.valueOf(miArreglodoble.size()));
            }else {

                miArreglodoble = miArreglodos;
                //texto1.setText(String.valueOf(miArreglodoble.size()));
            }


            N.setText("");
            double sum3 = sum + sum2;

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
            }else if(miArreglo.size() == 50 & sum3 == 0 ){
                caso4 = 1;
                Toast.makeText(this, "Para arreglo 100", Toast.LENGTH_SHORT).show();
            }

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

    public void Pasar( ) {
       try {
           DecimalFormat format = new DecimalFormat("#.00");
           DecimalFormat format2 = new DecimalFormat("#");
            Double nu = Double.parseDouble(N.getText().toString());
            sum = 0;

            miArreglo.add(nu);
            N.setText("");
           numero = (int)(Math.random()*30+1);

            DataPoint[] dataPoints = new DataPoint[miArreglo.size()];
                for (int i = 0; i < miArreglo.size(); i++) {
                    T = T_n(i + 1);
                    sum = sum + miArreglo.get(i);
                    texto1.setText("Suma:" + "  " + format2.format(sum) + "\n" + "Densidad:" + "  " + format.format(sum / (i + 1)) + "\n" + "Muestreo Secuencial");
                    dataPoints[i] = new DataPoint((i + 1), sum);

                }
                list.setAdapter(new Adaptador3(miArreglo, this));


                Relacion_VM();

                if (sum >= T) {
                    dialogo_personalizado(sum,T);
                    //dialogo(sum, T);
                }
                series = new LineGraphSeries<>(dataPoints);
                series.setColor(Color.BLACK);
                funcion.getViewport().setMaxX(miArreglo.size() + 2);
                funcion.getViewport().setMaxY(sum + 5);
                funcion.addSeries(series);

           muestras = miArreglo.size();
           densidad = sum / muestras;

           Relacion_VM();


           arreglo.clear();
           arreglo.add("Pasos:" + " " + numero);
           arreglo.add("R V M:" + " \t " + format.format(rvm));
           arreglo.add("E E:" + " \t " + format.format(se) );
          // arreglo.add("I Kuno:" + "  \t  " + format.format(kuno));
           //arreglo.add("Precision: " + " " + format.format((1 - pre
          // ) * 100) + " " + "%");
           // arreglo.add(String.valueOf(varianza));


           miA2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);
           parametros.setAdapter(miA2);




       } catch (Exception e) {

            Toast.makeText(this, "No ha ingresado ninguna cantidad", Toast.LENGTH_SHORT).show();
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

    public void RVM(){
            try {
                DecimalFormat format = new DecimalFormat("#.00");
                DecimalFormat format2 = new DecimalFormat("#");
                Double s = null;

                Double nu = Double.parseDouble(N.getText().toString());
                int ad = nu.intValue();
                if (muestreo.equals("Muestreo proporción") &  ad == 1 | muestreo.equals("Muestreo proporción") &  ad == 0 | muestreo.equals("Muestreo secuencial")) {
                sum = 0;
                Double sum2 = 0.0;
                pre = 0.0;
                varianza = 0.0;
                miArreglo.add(nu);
                N.setText("");
                numero = (int) (Math.random() * 30 + 1);

                muestras = miArreglo.size();
                DataPoint[] dataPoints = new DataPoint[miArreglo.size()];
                for (int i = 0; i < miArreglo.size(); i++) {

                    sum = sum + miArreglo.get(i);
                    if (muestreo.equals("Muestreo proporción") &  nu == 1 | muestreo.equals("Muestreo proporción") &  nu == 0){
                        texto1.setText("Suma:" + "  " + format2.format(sum) + "\n" + "Proporción:" + "  " + format.format(sum / (i + 1)) + "\n" + "Muestreo Proporción" );
                    }else if (muestreo.equals("Muestreo secuencial")){
                        texto1.setText("Suma:" + "  " + format2.format(sum) + "\n" + "Densidad:" + "  " + format.format(sum / (i + 1)) + "\n" + "Muestreo Secuencial");
                    }

                    dataPoints[i] = new DataPoint((i + 1), sum);
                    densidad = sum / muestras;
                    miArreglo2.add((miArreglo.get(i) - densidad) * (miArreglo.get(i) - densidad));
                    sum2 = sum2 + miArreglo2.get(i);

                }

                Relacion_VM();
                //varianza = sum2 / (miArreglo2.size() - 1);
                    if (muestreo.equals("Muestreo secuencial")) {
                        me = sum / miArreglo.size();
                        s = Math.sqrt(varianza);
                        Double a = Math.sqrt(miArreglo.size());
                        Double b = a * me;
                        pre = s / b;
                    }
                    if (muestreo.equals("Muestreo proporción")) {

                        me = sum / miArreglo.size();
                        pre = 1.95*(Math.sqrt(me*(1-me)))/Math.sqrt(miArreglo.size());
                    }


                arreglo.clear();
                arreglo.add("Pasos:" + " " + numero);
                arreglo.add("Precision: " + " " + format.format((1 - pre
                    ) * 100) + " " + "%");
                arreglo.add("R V M:" + " \t " + format.format(rvm));
                arreglo.add("EE:" + " \t " + format.format(se));
               // arreglo.add("I  Kuno:" + "  \t  " + format.format(kuno));

                //arreglo.add("N" + " " + format.format(units));

                list.setAdapter(new Adaptador3(miArreglo, this));
                miA2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);
                parametros.setAdapter(miA2);
                miA2.notifyDataSetChanged();
                series = new LineGraphSeries<>(dataPoints);
                series.setColor(Color.BLACK);
                funcion.getViewport().setMaxX(miArreglo.size() + 2);
                funcion.getViewport().setMaxY(sum + 5);
                funcion.addSeries(series);


                if (miArreglo.size() == 25 & sum != 0) {
                    //texto1.setText(String.valueOf(sum));

                    //Toast.makeText(this,"Diferente de 25",Toast.LENGTH_SHORT).show();
                    if (muestreo.equals("Muestreo secuencial")) {

                        units = Math.pow(s / (E * me), 2);
                        if(units <= 5){
                            units = units + 25;
                        }else if(units <= 15 & units > 5){
                            units = units + 20;
                        }

                    }
                    if (muestreo.equals("Muestreo proporción")) {

                        units = (Math.pow(1.96, 2)*me*(1-me)) /  Math.pow(E2,2);
                        //texto1.setText(String.valueOf(units));
                        if(units <= 5){
                            units = units + 25;
                        }else if(units <= 15 & units > 5){
                            units = units + 20;
                        }
                    }
                    graficar_v();
                    graficar_v2();

if(units < 15) {

    units = units + 20;
}



                        if (units <= 25) {


                            dialogo_personalizado2(E);
                            rem = 1;
                            caso1 = 1;

                        }

                        if (units > 25 & units <= 30) {


                            rem = 1;
                            caso2 = 1;

                        }

                        if (units > 30) {

                            rem = 1;
                            caso3 = 1;

                        }


                    }
                    if (miArreglo.size() == 25 & sum == 0) {

                        caso4 = 1;
                        Toast.makeText(this, "Para arreglo 100", Toast.LENGTH_SHORT).show();

                    }


            }


          } catch (Exception e) {

                Toast.makeText(this, "No ha ingresado ninguna cantidad", Toast.LENGTH_SHORT).show();
         }


    }

    public void RVM2(){

            try {
                //Toast.makeText(this,String.valueOf(units),Toast.LENGTH_SHORT).show();

                DecimalFormat format = new DecimalFormat("#.00");
                DecimalFormat format2 = new DecimalFormat("#");
                Double nu = Double.parseDouble(N.getText().toString());
                int ad = nu.intValue();
                if (muestreo.equals("Muestreo proporción") &  ad == 1 | muestreo.equals("Muestreo proporción") &  ad == 0 | muestreo.equals("Muestreo secuencial")) {


                    sum = 0;
                    Double sum2 = 0.0;
                    pre = 0.0;
                    //Double me = 0.0;
                    miArreglo.add(nu);
                    N.setText("");
                    numero = (int) (Math.random() * 30 + 1);

                    muestras = miArreglo.size();
                    DataPoint[] dataPoints = new DataPoint[miArreglo.size()];
                    for (int i = 0; i < miArreglo.size(); i++) {

                        sum = sum + miArreglo.get(i);

                        if (muestreo.equals("Muestreo proporción")) {
                            texto1.setText("Suma:" + "  " + format2.format(sum) + "\n" + "Proporción:" + "  " + format.format(sum / (i + 1)) + "\n" + "Tamaño muestra:" + " " + Math.round(units));
                        } else if (muestreo.equals("Muestreo secuencial")) {
                            texto1.setText("Suma:" + "  " + format2.format(sum) + "\n" + "Densidad:" + "  " + format.format(sum / (i + 1)) + "\n" + "Tamaño muestra:" + " " + Math.round(units));
                        }
                        dataPoints[i] = new DataPoint((i + 1), sum);
                        densidad = sum / muestras;
                        miArreglo2.add((miArreglo.get(i) - densidad) * (miArreglo.get(i) - densidad));
                        sum2 = sum2 + miArreglo2.get(i);

                    }
                    Relacion_VM();

                    //varianza = sum2 / (miArreglo2.size() - 1);
                    //me = sum / miArreglo.size();
                    //units = Math.pow(se / (E * me), 2);
                    //double s = Math.sqrt(varianza);
                    //Double a = Math.sqrt(miArreglo.size());
                    //Double b = a * me;
                    //pre = s / b;

                    if (muestreo.equals("Muestreo secuencial")) {
                        me = sum / miArreglo.size();
                        double s = Math.sqrt(varianza);
                        Double a = Math.sqrt(miArreglo.size());
                        Double b = a * me;
                        pre = s / b;
                    }
                    if (muestreo.equals("Muestreo proporción")) {
                        me = sum / miArreglo.size();

                        pre = 1.95*(Math.sqrt(me*(1-me)))/Math.sqrt(miArreglo.size());
                    }


                    arreglo.clear();
                    arreglo.add("Pasos:" + " " + numero);
                    arreglo.add("Precision: " + " " + format.format((1 - pre) * 100) + " " + "%");
                    arreglo.add("R V M:" + " \t " + format.format(rvm));
                    //arreglo.add("varianza:" + " " + format.format(varianza));
                    arreglo.add("EE:" + " \t " + format.format(se));
                    //arreglo.add("I Kuno:" + "  \t  " + format.format(kuno));

                    //arreglo.add("N" + " " + format.format(units));

                    list.setAdapter(new Adaptador3(miArreglo, this));
                    miA2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);

                    parametros.setAdapter(miA2);
                    miA2.notifyDataSetChanged();
                    series = new LineGraphSeries<>(dataPoints);
                    series.setColor(Color.BLACK);
                    funcion.getViewport().setMaxX(miArreglo.size() + 2);
                    funcion.getViewport().setMaxY(sum + 5);
                    funcion.addSeries(series);

                    if (caso1 == 1) {
                        if (miArreglo.size() == 30) {

                            dialogo_personalizado2(pre);
                        }
                    }

                    if (caso2 == 1) {

                        units2 = Math.round(units);
                        if (miArreglo.size() == units2) {

                            dialogo_personalizado2(E);
                        }
                    }
                    if (caso3 == 1) {

                        if (miArreglo.size() == 30) {

                            dialogo_personalizado2(pre);

                        }

                        units2 = Math.round(units);
                        if (miArreglo.size() == Math.round(units)) {

                            dialogo_personalizado2(E);
                        }
                    }
                    if (caso4 == 1) {

                        if (miArreglo.size() == 100) {
                            dialogo_personalizado2(pre);
                        }
                    }

                }

            } catch (Exception e) {

                Toast.makeText(this, "No ha ingresado ninguna cantidad", Toast.LENGTH_SHORT).show();
            }

    }
/*
    public void proporcion(){
        DecimalFormat format = new DecimalFormat("#.00");
        DecimalFormat format2 = new DecimalFormat("#");

        int nu = Integer.parseInt(N.getText().toString());
        sum = 0;
        Double sum2 = 0.0;
        pre = 0.0;
        Double me = 0.0;
        miArreglo.add(nu);
        N.setText("");
        numero = (int)(Math.random()*30+1);

        muestras = miArreglo.size();
        DataPoint[] dataPoints = new DataPoint[miArreglo.size()];
        for (int i = 0; i < miArreglo.size(); i++) {

            sum = sum + miArreglo.get(i);
            texto1.setText("Suma:" + "  " + format2.format(sum) + "\n" + "Densidad:" + "  " + format.format(sum / (i + 1)));
            dataPoints[i] = new DataPoint((i + 1), sum);
            densidad = sum / muestras;
            miArreglo2.add((miArreglo.get(i) - densidad) * (miArreglo.get(i) - densidad));
            sum2 = sum2 + miArreglo2.get(i);

        }
        Relacion_VM();

        //varianza = sum2 / (miArreglo2.size() - 1);
        me = sum / miArreglo.size();
        //units = Math.pow(se / (E * me), 2);

        double s = Math.sqrt(varianza);
        Double a = Math.sqrt(miArreglo.size());
        Double b = a * me;
        pre = s / b;


        arreglo.clear();
        arreglo.add("Pasos:" + " " + numero);
        arreglo.add("Precision: " + " " + format.format( (1 - pre)*100) + " " + "%");
        arreglo.add("R V M:" + " \t " + format.format(rvm));
        //arreglo.add("varianza:" + " " + format.format(varianza));
        arreglo.add("EE:" + " \t " + format.format(se));
        //arreglo.add("I Kuno:" + "  \t  " + format.format(kuno));

        //arreglo.add("N" + " " + format.format(units));

        list.setAdapter(new Adaptador3(miArreglo, this));
        miA2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);

        parametros.setAdapter(miA2);
        miA2.notifyDataSetChanged();
        series = new LineGraphSeries<>(dataPoints);
        series.setColor(Color.WHITE);
        funcion.getViewport().setMaxX(miArreglo.size() + 2);
        funcion.addSeries(series);

    }
*/
    public void remuetreo(){
        sum = 0;
        int p = 0;
        int x = miArreglo3.size();
        int y = 0;
        //miArreglo.clear();

       // try {

            for (int i = 0; i < x;i++){
                sum = sum + miArreglo3.get(i);
               // miArreglo.add(miArreglo3.get(i));
                T = T_n(i + 1);
                if (sum > T) {
                    if (p == 1){
                        y = y + 1;
                        sum = sum - miArreglo3.get(i);
                        //miArreglo.remove(miArreglo3.get(i));
                    }
                    p = 1;
                }


            }
            for (int i = miArreglo.size() - 1 ; i >= 0; i--){
                if (y > 0){
                    miArreglo.remove(i);
                    y = y - 1;
                }
            }
            sum = 0;
        DataPoint[] dataPoints = new DataPoint[miArreglo.size()];
        for (int i = 0; i < miArreglo.size(); i++) {
            sum = sum + miArreglo.get(i);
            dataPoints[i] = new DataPoint((i + 1), sum);
        }
        list.setAdapter(new Adaptador3(miArreglo, this));
        series = new LineGraphSeries<>(dataPoints);
        series.setColor(Color.WHITE);
        funcion.getViewport().setMaxX(miArreglo.size() + 2);
        funcion.addSeries(series);
        dialogo_personalizado(  sum, T);

      /* }catch (Exception e){

            Toast.makeText(this, "Remuestreo", Toast.LENGTH_SHORT).show();

        }*/
    }

    public void grafica(){
        DataPoint[] dataPoints3 = new DataPoint[150];

        for(int i = 0;i< 150; i++ ){
            dataPoints3[i] = new DataPoint((i + 1), T_n(i+1));
        }

        LineGraphSeries<DataPoint> serie3 = new LineGraphSeries<>(dataPoints3);
        serie3.setColor(Color.RED);
        funcion.addSeries(serie3);

    }

/*
    public void reiniciar(View view) {
        try {
            miArreglo.clear();
            funcion.removeAllSeries();
            miA = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, miArreglo);
            list.setAdapter(miA);
            texto1.setText("Suma:" + "  "+ 0 + "\n" + "Densidad:"+ "  " + 0);
            grafica();

        }catch (Exception e){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

        }

    }
*/

    public void editar(View view) {
        try {
            DecimalFormat format = new DecimalFormat("#.00");
            DecimalFormat format2 = new DecimalFormat("#");

            funcion.removeAllSeries();


            if (check == 1) {
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

            }else{

            sum = 0;

            miArreglo.remove(miArreglo.size() - 1);
            list.setAdapter(new Adaptador3(miArreglo, this));
            //miA = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, miArreglo);
            //list.setAdapter(miA);
            Toast.makeText(this, "Se ha eliminado el ultimo elemento", Toast.LENGTH_SHORT).show();
            DataPoint[] dataPoints = new DataPoint[miArreglo.size()];
            for (int i = 0; i < miArreglo.size(); i++) {
                sum = sum + miArreglo.get(i);
                texto1.setText("Suma:" + "  " + format2.format(sum) + "\n" + "Densidad:" + "  " + format.format(sum / (i + 1)));
                dataPoints[i] = new DataPoint((i + 1), sum);
            }
            if(miArreglo.size() ==0 ){
                if (muestreo.equals("Muestreo proporción") ){
                    texto1.setText("Suma:" + "  " + format2.format(sum) + "\n" + "Proporción:" + "  " + "0" + "\n"  );
                }else if (muestreo.equals("Muestreo secuencial")){
                    texto1.setText("Suma:" + "  " + format2.format(sum) + "\n" + "Densidad:" + "  " +"0"   + "\n" );
                }

            }
            series = new LineGraphSeries<>(dataPoints);
            funcion.getViewport().setMaxX(miArreglo.size() + 2);
            funcion.addSeries(series);
            funcion.getViewport().setMaxY(sum+1);
            series.setColor(Color.BLACK);
            if (pasar == 1) {
                grafica();
                //Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
            }else if (miArreglo.size()>= 25){
                //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                graficar_v();
                graficar_v2();
            }

        }

        } catch (Exception e) {
            Toast.makeText(this, "No hay datos para eliminar", Toast.LENGTH_SHORT).show();
        }


    }

    public void dialogo2(double s, double t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Completado");
        builder.setMessage("Puede parar, el muestreo ha finalizado");
        builder.setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Toast.makeText(Grafica.this, "Puede guardar", Toast.LENGTH_SHORT).show();
            }
        });
        Dialog dialog = builder.create();
        if (s >= t) {

            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public void dialogo_personalizado(double s, double t){
        AlertDialog.Builder builder = new AlertDialog.Builder(Grafica.this);
        LayoutInflater inflater = getLayoutInflater();
        DecimalFormat format = new DecimalFormat("#.00");
        muestras = miArreglo.size();
        densidad = Double.parseDouble(format.format(sum / muestras));
        obtenerubicacion();

        View view  = inflater.inflate(R.layout.alert_dialog,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (s >= t){
            dialog.setCancelable(false);
            dialog.show();
        }

        //pre = 0.2;

        para = 80;


        //arreglo.clear();
        ListView resultados = view.findViewById(R.id.resultados);
        Button btneliminar =  view.findViewById(R.id.eliminar);
        Button btnguardar = view.findViewById(R.id.guardar);
       // arreglo2.add("Cultivo:" + " \t " + cultivo);
        // arreglo2.add("Plaga:" + " \t " + plaga );
        arreglo2.add("Densidad:" +" \t "+ densidad);
        arreglo2.add("R V M:" + " \t " + format.format(rvm));
        arreglo2.add("EE:" + " \t " + format.format(se) );
        //arreglo2.add("I  Kuno:" + "  \t  " + format.format(kuno));
       // arreglo.add(String.valueOf(varianza));
        if(rvm < 0.70){
            criterio = ("N/A");
            arreglo2.add(criterio);
        }if (rvm >= 0.70 & rvm <= 1.3){
            criterio = ("Población al azar");
            arreglo2.add(criterio);
        }if (rvm > 1.3 & rvm <= 2.0){
            criterio = ("Población ligeramente agregada");
            arreglo2.add(criterio);
        }if (rvm > 2.0 & rvm <= 4.0){
            criterio = ("Población con agregación fuerte");
            arreglo2.add(criterio);
        }if (rvm > 4){
            criterio = ("Población muy fuertemente agregada");
            arreglo2.add(criterio);
        }

        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editar(view);
                arreglo2.clear();
                dialog.dismiss();
                onPause();
            }
        });

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Consulta(view);
                    Crear_pdf();
                    onPause();
                    Guardar();
                    dialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(Grafica.this, "Pocos registros para guardar",Toast.LENGTH_SHORT).show();
                }
            }
        });

        miA2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo2);
        resultados.setAdapter(miA2);

    }

    //relacion uno

    public void Relacion_VM(){
        Double sum2 = 0.0;
        miArreglo2.clear();
        varianza = 0.0;
        media = sum/miArreglo.size();
        for (int i=0; i < miArreglo.size();i++){
            miArreglo3.add(miArreglo.get(i));
            miArreglo2.add((miArreglo.get(i) - media)*(miArreglo.get(i) - media));
            sum2 = sum2 + miArreglo2.get(i);
        }
        //txt2.setText(miArreglo3.toString());
        varianza = sum2/(miArreglo.size() - 1);

       // texto1.setText(String.valueOf(varianza));

        rvm = varianza/media;

       Double n = Math.sqrt(miArreglo.size());
       Double de = Math.sqrt(varianza);

        Double me = sum / miArreglo.size();
        double s = Math.sqrt(varianza);
        Double a = Math.sqrt(miArreglo.size());
        Double b = a * me;
        pre = s / b;

       se = de/n; //error estandar


       double k1 = varianza - media;
       double k2 = varianza/miArreglo.size();
       double y2 = Math.pow(media,2);



       kuno = k1/(y2 - k2);

    }

    public static String strSeparator = ",";
    public static String convertArrayToString(ArrayList<Integer> array){
        String str = "";
        for (int i = 0;i<array.size(); i++) {
            str = str+array.get(i);
            // Do not append comma at the end of last element
            if(i<array.size()-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
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


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atención");
        builder.setMessage("Se han ingresado pocos datos");
        builder.setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

            }
        });
        Dialog dialog = builder.create();
        if (miArreglo.size()<=5) {
            dialog.show();
        } else {

            if(pasar == 1 & sum >= T){
                pre = 0.2; //PRGUNTAR POR LA PRECISIÓN EN EL MUESTREO SECUENCIAL
            }
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
            con = new ConexionSQliteHelper(Grafica.this, "db_Registro", null, 1);

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



            if (check == 1){



            }else{
                Intent i = new Intent(Grafica.this, MainActivity.class);
                startActivity(i);
                Grafica.this.finish();
            }


          //  texto1.setText(latitud + "\n" + longuitud);

            /*AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            builder3.setTitle("Atencion");
            builder3.setMessage("¿Está seguro que desea guardar? No podra cambiar los datos despues");
            builder3.setPositiveButton("Entiendo y deseo guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    //Toast.makeText(MainActivity.this, "Puede guardar", Toast.LENGTH_SHORT).show();
                    con = new ConexionSQliteHelper(Grafica.this, "db_Registro", null, 1);

                    SQLiteDatabase db = con.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(Utilidades.CAMPO_CULTIVO, cultivo);
                    values.put(Utilidades.CAMPO_PLAGA, plaga);
                    values.put(Utilidades.CAMPO_FECHA, fecha);
                    values.put(Utilidades.CAMPO_DENSIDAD, densidad);
                    values.put(Utilidades.CAMPO_LATITUD, latitud);
                    values.put(Utilidades.CAMPO_LONGUITUD, longuitud);
                    values.put(Utilidades.CAMPO_DATOS, lista);

                    Long id = db.insert(Utilidades.TABLA_REGISTRO, Utilidades.CAMPO_LONGUITUD, values);

                    Toast.makeText(getApplicationContext(), "Registro exitoso" + " " + id, Toast.LENGTH_SHORT).show();

                    db.close();
                    subir_datos();


                    Intent i = new Intent(Grafica.this,MainActivity.class);
                    startActivity(i);
                    Grafica.this.finish();



                }
            }) ;
            Dialog dialog3 = builder3.create();
            dialog3.show();*/

            //Log.d(latitud + "", longuitud + "" + latitud);


        }

    //} catch (Exception e) {

      // Toast.makeText(getApplicationContext(), "No hay datos para guardar", Toast.LENGTH_SHORT).show();
    //}


//}else{
  //  Toast.makeText(this,"ERROR Pocos registros para guardar",Toast.LENGTH_SHORT).show();
//}


    }
    @SuppressLint("MissingPermission")
    public void obtenerubicacion(){
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 100,  Grafica.this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }


    public void  Consulta(View view){
        try{
            SQLiteDatabase db = con.getReadableDatabase();

            Registro registro ;
            ListaRegistro = new ArrayList<>();

            Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_REGISTRO,null);

            while (cursor.moveToNext()){

                registro = new Registro();
                registro.setCapturas(cursor.getString(7));

                ListaRegistro.add(registro);
            }
            cursor.close();
            db.close();
            //obtenerlista();
        }catch (Exception e){
            Toast.makeText(this,"Error en la consulta",Toast.LENGTH_SHORT   ).show();
        }

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

    private void obtenerlista() {
        Listainformacion = new ArrayList<>();

        for (int i = 0; i < ListaRegistro.size(); i++){

            Listainformacion.add(ListaRegistro.get(i).getCapturas());
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,Listainformacion);
        list.setAdapter(adapter);


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



    @Override
    public void onLocationChanged( Location location) {

        try {
            Geocoder geocoder = new Geocoder(Grafica.this, Locale.getDefault());
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


    public void Parametro(String pl, String cu,String et){

        if (pl.equals("Mango") && cu.equals("Aulacaspis tubercularis (Escama)")){

            a = 1.4783; b = 1.1433; pasar = 1;
        }
        if (pl.equals("Soya") && cu.equals("Nezara viridula (Chinche apestosa)")){

            a =1.6;	b=1.1;pasar = 1;
        }
        if (pl.equals("Soya") && cu.equals("Piezodorus guildinii (Chinche de la Alfalfa)")){
            a =1.05; 	b =1.01;pasar = 1;

        }if (pl.equals("Soya") && cu.equals("Dichelops furcatus (Chinche de los cuernos)")){
            a = 1.03;	b = 0.99;pasar = 1;

        }if (pl.equals("Maíz") && cu.equals("Diatraea saccharalis (General)[Barrenador de la caña]")){//ESTA NO//
            a =1.476980794;	b = 1.27;pasar = 1;

        }if (pl.equals("Clavel") && cu.equals("Frankliniella occidentalis (Trips)")){//ESTA NO//
            a = 1.596;	b = 1.391;pasar = 1;

        }if (pl.equals("Arándano") && cu.equals("Frankliniella occidentalis (Trips)")){ //ESTA NO//
            a = 2.169;	b = 1.553;pasar = 1;

        }if (pl.equals("Pimiento") && cu.equals("Frankliniella occidentalis (Trips)") && et.equals("Ninfa/Larva") ){//esta no//
            a = 3.532;	b = 1.5192;pasar = 1;

        }
        if (pl.equals("Brócoli") && cu.equals("Plutella xylostella (Dorso de diamante)")){
            a = 1.09;	b = 1.37;pasar = 1;
        }
        if (pl.equals("Papa") && cu.equals("Thrips palmi (Trips)") && et.equals("Ninfa/Larva")){
            a = 2.32;	b = 2.1;pasar = 1;

        }
        if (pl.equals("Papa") && cu.equals("Thrips palmi (Trips)") && et.equals("Adulto")){
            a = 2.49;	b = 1.45;pasar = 1;

        }
        if (pl.equals("Tomate verde") && cu.equals("Trialeurodes vaporariorum (Mosquita blanca)")){
            a = 2.75;	b = 1.82;pasar = 1;
        }
        if (pl.equals("Cebolla") && cu.equals("Thrips tabaci (Trips)")){
            a = 2.586;	b = 1.511;pasar = 1;
        }
        if (pl.equals("Manzana") && cu.equals("Panonychus ulmi (Araña roja)")){
            a = 2.38;	b = 1.39;pasar = 1;
        }
        if (pl.equals("Vid") && cu.equals("Planococcus ficus (Piojo harinoso)")){
            a = 10.73;	b = 1.51;pasar = 1;
        }
        if (pl.equals("Limón") && cu.equals("Diaphorina citri (Psílido asiático)") && et.equals("Huevo") ){
            a = 5.96;	b = 1.56;pasar = 1;
        }
        if (pl.equals("Limón") && cu.equals("Diaphorina citri (Psílido asiático)") && et.equals("Ninfa/Larva") ){
            a = 7.72;	b = 1.26;pasar = 1;
        }
        if (pl.equals("Limón") && cu.equals("Diaphorina citri (Psílido asiático)") && et.equals("Adulto") ){
            a = 1.38;	b = 1.26;pasar = 1;
        }
        if (pl.equals("Naranja") && cu.equals("Diaphorina citri (Psílido asiático)") && et.equals("Huevo") ){
            a = 5.96;	b = 1.56;pasar = 1;
        }
        if (pl.equals("Naranja") && cu.equals("Diaphorina citri (Psílido asiático)") && et.equals("Ninfa/Larva") ){
            a = 7.72;	b = 1.26;pasar = 1;
        }
        if (pl.equals("Naranja") && cu.equals("Diaphorina citri (Psílido asiático)") && et.equals("Adulto") ){
            a = 1.38;	b = 1.26;pasar = 1;
        }
        if (pl.equals("Toronja") && cu.equals("Diaphorina citri (Psílido asiático)") && et.equals("Huevo")){
            a = 5.96;	b = 1.56;pasar = 1;
        }
        if (pl.equals("Toronja") && cu.equals("Diaphorina citri (Psílido asiático)") && et.equals("Ninfa/Larva")){
            a = 7.72;	b = 1.26;pasar = 1;
        }
        if (pl.equals("Toronja") && cu.equals("Diaphorina citri (Psílido asiático)") && et.equals("Adulto") ){
            a = 1.38;	b = 1.26;pasar = 1;
        }
        if (pl.equals("Fresa") && cu.equals("Duponchelia fovealis (Palomilla Europea)")){
            a = 1.67;   b = 1.12;pasar = 1;

        }


    }
}
