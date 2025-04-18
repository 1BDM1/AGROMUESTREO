package com.techco.Agromuestreo;

import static android.content.Context.LOCATION_SERVICE;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.techco.Agromuestreo.Entidad.Registro;
import com.techco.Agromuestreo.Entidad.registro2;
import com.techco.Agromuestreo.Utilidades.Entreno;
import com.techco.Agromuestreo.Utilidades.Utilidades;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Adaptadorh extends BaseExpandableListAdapter {


    final Context context;
    final ArrayList<Registro> listTitulo;
    final Map<String, ArrayList<String>> expandable;
    public ArrayList<Double> miArreglo = new ArrayList<>();
    public ArrayList<Double> miArreglo2 = new ArrayList<>();
    public ArrayList<Double> miArreglo3 = new ArrayList<>();
    ArrayList<registro2> ListaRegistro_1= null;
    String NOMBRE_DIRECTORIO = "Agromuestreo";
    public String lista, ruta;
    public String partes;
    public int NUMERO;
    String cultivo, plaga, areas, criterio, fecha, captura,tipo,longu,lati,etapa;
    Double densidad, rvm, se, kuno, a, b, sum, pre;
    Integer pasar = 0;
    ConexionSQliteHelper con;
    File file2;
    public LocationManager locationManager;
    ArrayList<Registro> ListaRegistro = null;
    public Integer posicion = 0;
    public Double PCS = 0.0;
    public String clasificacion = null;

    public Adaptadorh(Context context, ArrayList<Registro> listTitulo, Map<String, ArrayList<String>> expandable) {
        this.context = context;
        this.listTitulo = listTitulo;
        this.expandable = expandable;
    }

    @Override
    public int getGroupCount() {
        return this.listTitulo.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return this.listTitulo.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.expandable.get(this.listTitulo.get(i).getCapturas());
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {


        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    /*     String partes = String.valueOf(listTitulo.get(i).getCultivo());
           String[] division = partes.split("Y");
            pre = division[1];
            if (division.length == 2){

                String clt = division[0];

                partes = division[1];
                cultivo.setText(clt);
            }else{
                cultivo.setText(partes);
            }
*/

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.historial_lista, null);
        TextView fecha = view.findViewById(R.id.fecha);
        TextView cultivo = view.findViewById(R.id.cultivo);
        TextView plaga = view.findViewById(R.id.plaga);
        TextView densidad = view.findViewById(R.id.densidad);


        cultivo.setText(listTitulo.get(i).getCultivo());
        fecha.setText(listTitulo.get(i).getFecha());
        plaga.setText(listTitulo.get(i).getPlaga());

        String muestreo = String.valueOf(listTitulo.get(i).getTIPO());

        String tp = null;

        if (muestreo.equals("Muestreo proporción")){
            tp = "Proporción";
        }else{
            tp = "Densidad";
        }

        densidad.setText(String.valueOf(tp+ ": " + listTitulo.get(i).getDensidad()));

        return view;

    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {


            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.historial_lista2, null);
        }
        TextView capturas = view.findViewById(R.id.datos);


        con = new ConexionSQliteHelper(context, "db_Registro", null, 1);
        SQLiteDatabase db = con.getReadableDatabase();

        Registro registro;
        ListaRegistro = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Registro", null);

        while (cursor.moveToNext()) {

            registro = new Registro();
            registro.setFecha(cursor.getString(2));
            registro.setCultivo(cursor.getString(0));
            registro.setPlaga(cursor.getString(1));
            registro.setDensidad(cursor.getDouble(3));
            registro.setLatitud(cursor.getString(4));
            registro.setLonguitud(cursor.getString(5));
            registro.setArea(cursor.getString(6));
            registro.setCapturas(cursor.getString(8));
            registro.setPRECISION(cursor.getDouble(7));
            registro.setTIPO(cursor.getString(9));
            registro.setETAPA(cursor.getString(10));

            ListaRegistro.add(registro);
        }
        cursor.close();
        db.close();

        //PARA LA ENFERMEDAD
        //public void consulta(){
            try{

                SQLiteDatabase db2 = new Entreno(capturas.getContext(), "my_database", null, 1).getReadableDatabase();
                ListaRegistro_1 = new ArrayList<>();
                Cursor cursor2 = db2.rawQuery("SELECT " + Entreno.HOJA + ", MAX(" + Entreno.PC + ") FROM " + Entreno.TABLE_NAME + " GROUP BY " + Entreno.HOJA, null);
                while (cursor2.moveToNext()) {
                    registro2 registro2 = new registro2();
                    registro2.setHoja(cursor2.getString(0));
                    registro2.setPC(cursor2.getDouble(1));
                    ListaRegistro_1.add(registro2);
                }
                cursor2.close();
                db2.close();

                for (int ii = 0; ii < ListaRegistro_1.size(); ii++){
                    if (ListaRegistro_1.get(ii).getHoja().equals("General")){
                        PCS = ListaRegistro_1.get(ii).getPC();
                    }
                }

                if (ListaRegistro_1.isEmpty()) {

                    PCS=0.0;
                   // Toast.makeText(this,String.valueOf(PCS),Toast.LENGTH_SHORT).show();


                } else {

                    //Toast.makeText(this,String.valueOf(PCS),Toast.LENGTH_SHORT).show();


                }
                if (PCS <= 0.85){
                    clasificacion = "SEMILLA (APRENDIZ)";
                }else if(PCS > 0.85 & PCS <= 0.90){
                    clasificacion = "BROTE (PRINCIPIANTE)";
                }else if(PCS > 0.90 & PCS <= 0.95){
                    clasificacion = "PLÁNTULA (INTERMEDIO)";
                }else if(PCS > 0.95 & PCS <= 0.97){
                    clasificacion = "ÁRBOL FRONDOSO (AVANZADO)";
                }else if(PCS > 0.97){
                    clasificacion = "BOSQUE SABIO (EXPERTO)";
                }


            }catch (Exception e){

            }
        //}



        //PARA LA ENFERMEDAD



        capturas.setText(listTitulo.get(i).getCapturas());
        pre = listTitulo.get(i).getPRECISION();

        posicion = i;

        Button reporte = view.findViewById(R.id.generar2);

        reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // capturas.setText(listTitulo.get(i).getCultivo());

                try {

                    //DATOS SQL
                    densidad = 0.0;
                    tipo = listTitulo.get(i).getTIPO();
                    lista=listTitulo.get(i).getCapturas();

                    fecha = listTitulo.get(i).getFecha();
                    plaga = listTitulo.get(i).getPlaga();
                    //String clt = String.valueOf(ListaRegistro.get(i).getCultivo());
                      //      String [] prtes = clt.split("Y");
                    //cultivo = prtes[0];
                    cultivo = listTitulo.get(i).getCultivo();
                    densidad = listTitulo.get(i).getDensidad();
                    areas = listTitulo.get(i).getArea();
                    longu = listTitulo.get(i).getLonguitud();
                    lati = listTitulo.get(i).getLatitud();
                    etapa = listTitulo.get(i).getETAPA();




                    //TERMINA DATOS SQL

                    String s = listTitulo.get(i).getCapturas();
                    captura = s;
                    List<String> miLista = new ArrayList<String>(Arrays.asList(s.split(", ")));
                    String Tamaño = String.valueOf(miLista.size());
                    List<Double> numero = new ArrayList<Double>();
                    //areas = listTitulo.get(i).getArea();
                    Parametro(plaga, cultivo);
                    sum = 0.0;
                    numero.clear();

                    miArreglo.clear();
                    for (int i = 0; i < miLista.size(); i++) {
                        numero.add(Double.valueOf(miLista.get(i)));
                        miArreglo.add(Double.valueOf(miLista.get(i)));
                        sum = sum + Double.valueOf(miLista.get(i));

                    }

                    Relacion_VM();
                    Crear_pdf();


                   /* Bundle miBundle = new Bundle();
                    Intent i = new Intent(context, MainActivity.class);
                    Integer pass = 1;
                    String rutas = String.valueOf(ruta);
                    miBundle.putString("RUTA", rutas);
                    miBundle.putInt("PASE",pass);
                    i.putExtras(miBundle);
                    context.startActivity(i);*/

                    //Toast.makeText(context, "Reporte Generado", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, clasificacion, Toast.LENGTH_SHORT).show();

                        /*Bundle miBundle = getActivity().getIntent().getExtras();
                        ruta = miBundle.getString("RUTA");
                        sig = miBundle.getInt("PASE");
                        String s= String.valueOf(ruta); */

                    try {

                    File arch = new File(ruta);

                            if (arch.exists()) {

                                Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", arch);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(uri, "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);



                                    context.startActivity(intent);
                            }

                                } catch (ActivityNotFoundException e) {

                                    // Utils.showSnackBar(root.getResources().getString(R.string.error_pdf), root);
                                }

                            //Toast.makeText(this, ruta, Toast.LENGTH_SHORT).show();

                    //} catch (Exception e) {

                        //Toast.makeText(getContext(),"ERROR PDF",Toast.LENGTH_SHORT).show();
                    //}



                } catch (NumberFormatException e) {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    public byte[] toByteArray(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    public void Crear_pdf() {
        Document documento = new Document();
        String tp;
        try {
            if (tipo.equals("Muestreo proporción")){
                tp = "Proporción";
            }else{
                tp = "Densidad";
            }
            obtenerubicacion();
            DecimalFormat format = new DecimalFormat("#.00");
            Calendar calendario = Calendar.getInstance();
            fecha = DateFormat.getDateInstance().format(calendario.getTime());

            NUMERO = 1;
            File file = CrearFichero("Copia" + " " + String.valueOf(NUMERO) + ".pdf");
            ruta = file.getAbsolutePath();
            FileOutputStream ficheroPDF = new FileOutputStream(file.getAbsolutePath());


            file2 = file;
            ruta = file.getAbsolutePath();
           ///Image image = Image.getInstance(R.drawable.agromneg);
            Image image = Image.getInstance(toByteArray(context.getResources().getDrawable(R.drawable.agromneg)));
           image.scalePercent(10);
           image.setAlignment(Element.ALIGN_CENTER);


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
           // Document document = new Document(new PdfDocument(writer));
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
                documento.add(new Paragraph("Longitud:" + " " + longu + "                                                                           " + "Latitud:" + lati));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Cultivo evaluada:" + "     " + cultivo));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Enfermedad evaluada:" + "     " + plaga));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Area evaluada:" + "     " + areas));
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
                documento.add(new Paragraph("Longitud:" + " " + longu + "                                                                           " + "Latitud:" + lati));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Cultivo muestreado:" + "     " + cultivo));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Plaga muestreada:" + "     " + plaga));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Estado biológico:" + "     " + etapa));
                documento.add(new Paragraph("\n"));
                documento.add(new Paragraph("Area muestreada:" + "     " + areas));
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
        } catch (DocumentException e) {
            Toast.makeText(context,"Error PDF",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
        } finally {
            documento.close();
        }
    }

    public void obtenerubicacion() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 100, (LocationListener) Adaptadorh.this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public File CrearFichero(String nombreFichero) {

        File ruta = getRuta();
        File fichero = null;

        if(ruta != null){

            //fichero = new File(ruta,nombreFichero);
            fichero = new File(ruta,nombreFichero);
            while (fichero.exists()) {
                NUMERO++;
                nombreFichero = "Copia" + " " + String.valueOf(NUMERO) + ".pdf";
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

    public void estdisticos(){


    }
    /*public void Relacion_VM(){
        Double sum2 = 0.0;
        rvm = 0.0;
        se = 0.0;
        kuno = 0.0;

        Double varianza = 0.0;
        Double media = sum/miArreglo.size();
        for (int i=0; i < miArreglo.size();i++){
            miArreglo3.add(miArreglo.get(i));
            miArreglo2.add((miArreglo.get(i) - media)*(miArreglo.get(i) - media));
            sum2 = sum2 + miArreglo2.get(i);
        }

        varianza = sum2/(miArreglo.size() - 1);

        rvm = varianza/media;



        Double s = Math.sqrt(varianza);
        Double a = Math.sqrt(miArreglo.size());
        Double b = a * media;
        //pre = s / b;
        Double n = Math.sqrt(miArreglo.size());
        Double de = Math.sqrt(varianza);

        se = de/n; //error estandar

        double k1 = varianza - media;
        double k2 = varianza/miArreglo.size();
        double y2 = Math.pow(media,2);



        Parametro(plaga,cultivo);
        kuno = k1/(y2 - k2);

        if (pasar == 0){
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

    }*/

    public void Relacion_VM(){
        Double sum2 = 0.0;
        miArreglo2.clear();
       double varianza = 0.0;
        double media = sum/miArreglo.size();
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

        if (pasar == 0){
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

    }


    public void Parametro(String pl, String cu){

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

        }if (pl.equals("Pimiento") && cu.equals("Frankliniella occidentalis (Trips) [Larvas]")){//esta no//
            a = 3.532;	b = 1.5192;pasar = 1;

        }
        if (pl.equals("Brócoli") && cu.equals("Plutella xylostella (Dorso de diamante)")){
            a = 1.09;	b = 1.37;pasar = 1;
        }
        if (pl.equals("Papa") && cu.equals("Thrips palmi (Trips) [Larvas]")){
            a = 2.32;	b = 2.1;pasar = 1;

        }
        if (pl.equals("Papa") && cu.equals("Thrips palmi (Trips) [Adultos]")){
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
        if (pl.equals("Limón") && cu.equals("Diaphorina citri (Psílido asiático - Huevos)")){
            a = 5.96;	b = 1.56;pasar = 1;
        }
        if (pl.equals("Limón") && cu.equals("Diaphorina citri (Psílido asiático - Ninfa)")){
            a = 7.72;	b = 1.26;pasar = 1;
        }
        if (pl.equals("Limón") && cu.equals("Diaphorina citri (Psílido asiático - Adulto)")){
            a = 1.38;	b = 1.26;pasar = 1;
        }
        if (pl.equals("Naranja") && cu.equals("Diaphorina citri (Psílido asiático - Huevos)")){
            a = 5.96;	b = 1.56;pasar = 1;
        }
        if (pl.equals("Naranja") && cu.equals("Diaphorina citri (Psílido asiático - Ninfas)")){
            a = 7.72;	b = 1.26;pasar = 1;
        }
        if (pl.equals("Naranja") && cu.equals("Diaphorina citri (Psílido asiático - Adulto)")){
            a = 1.38;	b = 1.26;pasar = 1;
        }
        if (pl.equals("Toronja") && cu.equals("Diaphorina citri (Psílido asiático - Huevos)")){
            a = 5.96;	b = 1.56;pasar = 1;
        }
        if (pl.equals("Toronja") && cu.equals("Diaphorina citri (Psílido asiático - Ninfa)")){
            a = 7.72;	b = 1.26;pasar = 1;
        }
        if (pl.equals("Toronja") && cu.equals("Diaphorina citri (Psílido asiático - Adulto)")){
            a = 1.38;	b = 1.26;pasar = 1;
        }
        if (pl.equals("Fresa") && cu.equals("Duponchelia fovealis (Palomilla Europea)")){
            a = 1.67;   b = 1.12;pasar = 1;

        }


    }


}