package com.techco.Agromuestreo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.techco.Agromuestreo.Entidad.Registro;
import com.techco.Agromuestreo.Utilidades.Utilidades;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.techco.Agromuestreo.ui.gallery.DatePickerFragment;

import java.sql.Date;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Detalles extends AppCompatActivity {
    public Spinner cultivo, plaga1;
    GraphView grafica;
    TextView ver, prueba;
    public String fecha_selec;
    Calendar fecha_filt;

    ConexionSQliteHelper con;
    ArrayList<Double> Listainformacion1 = null;
    ArrayList<Date> Listainformacion2 = null;
    ArrayList<Double> Listainformacion3 = null;
    ArrayList<Date> Listainformacion4 = null;
    ArrayList<String> listainf = null;

    private ArrayList<Date> events = null;

    public ArrayList<Integer> miArreglo = new ArrayList<>();

    public TextView pruebas;

    public  int sum;
    public Date selectedDate;

    ArrayList<String> ListaCultivo = null;
    ArrayList<String> ListaPlaga = null;
    public int piker;

    ArrayList<Registro> ListaRegistro = null;
    String [] Plagas_final;
    String [] Cultivo_final;
    public String [] FECHA;
    public Button FILTRO;


    public BarGraphSeries series;
    int pasar=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);


        cultivo = findViewById(R.id.dcultivo);
        plaga1 = findViewById(R.id.plaga);
        grafica = findViewById(R.id.dGrafica);
        ver = findViewById(R.id.PRUEBA);
        prueba = findViewById(R.id.textView7);
        FILTRO = findViewById(R.id.Filtro);

       // showDatePickerDialog();

        String [] Plantas = { "Agave"  ,              "Aguacate"  ,           "Ajonjoli"  ,           "Alfalfa"       ,       "Algodon"     ,         "Arroz"        ,        "Avena",
                "Brocoli"       ,       "Cacao"         ,       "Café"          ,       "Calabacita"    ,       "Caña de Azucar"   ,    "Cartamo"       ,       "Cebada",
                "Cebolla"       ,       "Chile"          ,      "Clavel"        ,       "Coco"         ,        "Durazno"       ,       "Esparragos"    ,       "Frijol",
                "Frutilla Invernadero" ,"Garbanzo"    ,         "Guayaba"   ,           "Jitomate"      ,       "Limon"      ,          "Maiz"     ,            "Mango (Ataulfo)",
                "Manzana"       ,       "Manzano"      ,        "Naranja"        ,      "Nuez"               ,         "Papa"         ,        "Pimiento Invernadero",
                "Piña"        ,         "Plátano"        ,      "Sandia"       ,        "Sorgo"        ,        "Soya"       ,          "Tomate"        ,       "Tomate en verde",
                "Toronja"       ,       "Trigo"          ,      "Tuna"        ,         "Vid"  ,          "Otro"};



        String [] Plagas = {"Plaga1","Plaga2","Plaga2"};



        String [] Plagas_frijol = {"Epilachna varibestis (Conchuela)","Diabrótica balteata (Diabrótica)","Liriomyza spp. (Minador)"};
        String [] Plagas_sorgo = {"Melanaphis sacchari (Pulgón amarillo)","Stenodiplosis sorghicola (Mosca midge)", "Oebalus mexicana (Chinche café)"};
        String [] Plagas_CanaAzucar = {"Diatrea saccharalis (Barrenador del tallo)","Aeneolamia sp. (Mosca pinta)","Eoreuma loftini (Barrenador mexicano)"};
        String [] Plagas_cafe = {"Hypotenemus hampei (Broca del café)","Leucoptera coffeella (Minador de la hoja)","Monoflata pallescens (Papalota del café)"};
        String [] Plagas_avena = {"Schizaphis graminum (Pulgón verde)","Rhopalosiphum padi (Pulgón del tallo)","Diuraphis noxia (Pulgón ruso)"};
        String [] Plagas_trigo = {"Sitobium avenae (Pulgón de la espiga)","Schizaphis graminum (Pulgón verde)","Rhopalosiphum padi (Pulgón del tallo)"};
        String [] Plagas_alfalfa = {"Epicauta spp. (Escarabajo)","Hypera postica (Picudo)","Acyrthosiphon pisum (Pulgón verde)"};
        String [] Plagas_naranja = {"Diaphorina citri (Psílido asiático - Huevos)","Diaphorina citri (Psílido asiático - Ninfas)","Diaphorina citri (Psílido asiático - Adultos)","Phyllocoptruta oleivora (Arador de los cítricos)"};
        String [] Plagas_cebada = {"Schizaphis graminum (Pulgón verde)","Diuraphis noxia (Pulgón ruso)","Metopolophium dirhodum (Pulgón amarillo)"};
        String [] Plagas_aguacate = {"Conotrachelus aguacatae (Barrenador del hueso)","Coptorus aguacatae (Barrenador de las ramas)","Scirtothrips persea (Trips)"};
        String [] Plagas_limon = {"Diaphorina citri (Psílido asiático - Huevos)","Diaphorina citri (Psílido asiático - Ninfa)","Diaphorina citri (Psílido asiático - Adulto)","Panonychus citri (Ácaro rojo)","Tetranychus urticae (Araña roja)"};
        String [] Plagas_soya = {"Anticarsia gemmatalis (Gusano terciopelo)","Nezara viridula (Chinche apestosa)","Caliothrips phaseoli (Trips negro)"};
        String [] Plagas_algodon = {"Heliothis zea (Gusano bellotero)","Anthonomus grandis (Picudo)","Pectinophora gossypiella (Gusano rosado)"};
        String [] Plagas_chile = {"Anthonomus eugenii (Picudo del chile)","Bactericera cockerelli (Paratrioza)","Myzus persicae (Pulgón verde)"};
        String [] Plagas_nuez = {"Acrobasis nuxvorella (Barrenador de la nuez)","Cydia caryana (Barrenador del ruezno)","Monellia caryella (Pulgón amarillo)"};
        String [] Plagas_agave = {"Scyphophorus acupunctatus (Picudo del agave)","Phyllophaga spp. (Gallina ciega)", "Pseudococcus agavis (Algodoncillo)"};
        String [] Plagas_coco = {"Rhynchophorus palmarum (Mayate prieto)","Rhina barbirostris (Picudo barbón)", "Eriophyes guerreronis (Ácaro del cocotero)"};
        String [] Plagas_garbanzo = {"Spodoptera exigua (Gusano soldado)","Helicoverpa zea (Gusano bellotero)","Liriomyza spp. (Minador)"};
        String [] Plagas_platano = {"Cosmopolites sordidus (Picudo del plátano)","Frankliniella parvula (Trips)","Tetranychus urticae (Araña roja)"};
        String [] PLagas_ajonjoli = {"Bemisia tabaci (Mosca blanca)","Nezara viridula (Chinche  apestosa)","Antigastra sp. (Enrollador)"};
        String [] Plaga_cacao = {"Toxoptera aurantii (Pulgón negro)","Xyleborus ferrugineus (Barrenador)","Selenothrips rubrocinctus (Trips)"};
        String [] Plaga_Manzana = {"Cydia pomonella (Palomilla de la manzana)","Eriosoma lanigerum (Pulgón lanígero)","Rhagoletis pomonella (Mosca de la manzana)"};
        String [] Plaga_Jitomate = {"Bemisia tabaci (Mosca blanca)", "Spodoptera exigua (Gusano soldado)","Helicoverpa zea (Gusano del fruto)","Bactericera cockerelli (Paratrioza)"};
        String [] Plagas_Tune = {"Cactophagus spinolae (Picudo del nopal)","Cylindrocopturus biradiatus (Picudo de las espinas)","Dactylopius opuntiae (Cochinilla o grana)"};
        String [] Plagas_arroz = {"Rupella albinella (Palomilla blanca)","Sogatodes orizicolus (Sogata del arroz)","Oebalus insularis (Chinche café)"};
        String [] Plaga_tomateverde = {"Trialeurodes vaporariorum","Heliothis subflexa (Gusano del fruto)","Trichobaris championi (Barrenador del tallo)","Bactericera cockerelli (Paratrioza)"};
        String [] Plaga_esparragos = {"Spodoptera exigua (Gusano soldado)","Tetranychus urticae (Araña roja)","Brachycorynella asparagi (Pulgón)"};
        String [] Plagas_sandia = {"Bemisia tabaci (Mosca blanca)", "Diabrótica spp. (Diabrótica)","Liriomyza spp. (Minador)"};
        String [] Plagas_vid = {"Dikrella cockerelli (Chicharrita)","Harrisina spp. (Descarnador de la hoja)","Planococcus ficus (Piojo harinoso)"};
        String [] Plagas_durazno = {"Eotetranychus lewisi (Araña roja)","Myzus persicae (Pulgón verde)","Macrodactylus mexicanus (Frailecillo)"};
        String [] Plaga_pina = {"Scutigerella inmaculata (Sinfílidos)","Dysmiccocus neobevipes (Cochinilla)","Metamasius dimidiatipennis (Picudo)"};
        String [] Plaga_cartamo = {"Agrotis sp. (Gusano trozador)","Blapstinus spp. (Cochinilla prieta)","Myzus persicae (Pulgón)"};
        String [] Plaga_calabacita = {"Bemisia tabaci (Mosca blanca)","Aphis gossypii (Pulgón verde)","Frankliniella occidentalis (Trips)"};
        String [] Plaga_guayaba = {"Conotrachelus psidii (Picudo de la guayaba)","Phyllophaga spp. (Gallina ciega)"};
        String [] Plaga_toronja = {"Diaphorina citri (Psílido asiático - Huevo)","Diaphorina citri (Psílido asiático - Ninfas)","Diaphorina citri (Psílido asiático - Adulto)","Toxoptera aurantii (Pulgón negro)","Phyllocnistis citrella (Minador)"};

        String [] Plagas_mango = {"Aulacapsis tuberculosis","prueba","Frankliniella occidentalis (Trips)","Ormenis pulverulenta (Papalota del mango)"};
        String [] Plagas_Soja = {"Nezara viridula","Piezedurus guildinii","Dichelops furcatus,Anticarsia gemmatalis (Gusano terciopelo)","Nezara viridula (Chinche apestosa)","Caliothrips phaseoli (Trips negro)"};
        String [] Plagas_maiz = {"Diatraea Saccharalis (General)","Spodoptera frugiperda (Gusano cogollero)","Helicoverpa zea (Gusano elotero)","Diabrotica virgifera (Diabrótica)","Phyllophaga spp. (Gallina ciega)"};
        String [] Plagas_clavel = {"Frankliniella occidentalis (Adultos)"};
        String [] Frutilla_invernadero = {"Frankliniella occidentalis (Larvas)"};
        String [] Pimiento_invernadero = {"Frankliniella occidentalis (Larvas)"};
        String [] Aguacate_hass = {"Oligonychus Punicae", "Olignugychus Perseae"};
        String [] Plagas_manzano = {"Panonychus Ulmi (Formas Moviles)"};
        String [] Plagas_brocoli = {"Trichoplusia ni (Gusano falso medidor)","Brevicoryne brassicae (Pulgón cenizo)","Plutella xylostella (Dorso de diamante)"};
        String [] Plagas_papa = {"Thrips Palmi (Larvas)","Thrips Palmi (Adultos)","Leptinotarsa decemlineata (Catarinita)","Phthorimaea operculella (Palomilla de la papa)","Bactericera cockerelli (Paratrioza)"};
        String [] Plagas_tabaco = {"Trips"};
        //String [] Plagas_alfalfa = {"Threrioapis trifolli (Adulto Alado)","Threrioapis trifolli (Adulto Aptero)", "Threrioapis trifolli (Ninfas Grandes)","Threrioapis trifolli  (Ninfas Pequeñas)"};
        String [] Plagas_pinus = {"Thaumetopoea pityocampa"};
        String [] Plagas_tomate = {"Trialeurodes vaporariorum"};
        String [] Plagas_cebolla = {"Thrips tabaci (Trips)", "Spodoptera exiguab (Gusano soldado)","Spodoptera frugiperda (Gusano cogollero)"};


        con = new ConexionSQliteHelper(Detalles.this, "db_Registro", null, 1);
        SQLiteDatabase db = con.getReadableDatabase();

        Registro registro ;
        ListaRegistro = new ArrayList<>(); //Registro

        Cursor cursor = db.rawQuery("SELECT * FROM Registro" ,null);


        while (cursor.moveToNext()){

            registro = new Registro();
            registro.setCultivo(cursor.getString(0));
            registro.setPlaga(cursor.getString(1));
            registro.setFecha(cursor.getString(2));
            registro.setDensidad(cursor.getDouble(3));

            ListaRegistro.add(registro);
        }


        //Toast.makeText(this,String.valueOf(ListaRegistro.size()),Toast.LENGTH_SHORT).show();



        db.close();
        cursor.close();



        FILTRO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

            ObtenerCultivo();
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(Detalles.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Cultivo_final);
            cultivo.setAdapter(adapter);
            cultivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    ObtenerPlaga();
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(Detalles.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Plagas_final);
                    plaga1.setAdapter(adapter2);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(Detalles.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Plagas);
                    plaga1.setAdapter(adapter2);
                }
            });


        }catch (Exception e){

            //Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }


        final java.text.DateFormat dateTimeFormatter = DateFormat.getTimeFormat(Detalles.this);

        plaga1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                obtenerlista();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        grafica.getViewport().setScrollable(true);
        grafica.getViewport().setScrollableY(true);
        grafica.getViewport().setScalable(true);
        grafica.getViewport().setScalableY(true);
        grafica.getViewport().setXAxisBoundsManual(true);
        grafica.getViewport().setYAxisBoundsManual(true);
        grafica.getViewport().setMaxY(10);
        //grafica.getViewport().setMaxX(30);
       grafica.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

// Usar el método getViewport().setDrawBorder() para mostrar el eje X y el eje Y
        grafica.getViewport().setDrawBorder(true);
        //grafica.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        //grafica.getViewport().setMaxX(15);
        //grafica.getGridLabelRenderer().setHumanRounding(false);



    }
    //FIN DEL ON CREATE

    public void BASEDEDATOS(){
        con = new ConexionSQliteHelper(Detalles.this, "db_Registro", null, 1);
        SQLiteDatabase db = con.getReadableDatabase();
        db.enableWriteAheadLogging();


        Registro registro ;
        ListaRegistro = new ArrayList<>(); //Registro


           // Cursor cursor = db.rawQuery("SELECT * FROM Registro where REPLACE(Fecha,'abr.', '04')   >='" + "2023-04-08" + "'" ,null);

        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_REGISTRO + " WHERE " + Utilidades.CAMPO_FECHA + " > ?", new String[] {fecha_selec});
//Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_REGISTRO,null);

        while (cursor.moveToNext()){

            registro = new Registro();
            registro.setCultivo(cursor.getString(0));
            registro.setPlaga(cursor.getString(1));
            registro.setFecha(cursor.getString(2));
            registro.setDensidad(cursor.getDouble(3));

            ListaRegistro.add(registro);
        }

        db.close();
        cursor.close();

    }

    private void showDatePickerDialog() {
        //piker = 1;
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                int day1 = datePicker.getDayOfMonth();
                int month1 = datePicker.getMonth();
                int year1 = datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month , day);
                String fechaSeleccionada = year + "-" + (month + 1) + "-" + day;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                fecha_selec = dateFormat.format(calendar.getTime());

                //Toast.makeText(Detalles.this, fecha_selec, Toast.LENGTH_SHORT).show();
                BASEDEDATOS();
                ObtenerCultivo();
                ObtenerPlaga();
                obtenerlista();
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private  void ObtenerPlaga(){


        ListaPlaga = new ArrayList<>();
        miArreglo.clear();

        try {
            //Toast.makeText(this, String.valueOf(ListaRegistro.size()), Toast.LENGTH_SHORT).show();


            for (int i = 0; i < ListaRegistro.size(); i++) {

                ListaCultivo.add(ListaRegistro.get(i).getCultivo());
                String cultivo2 = String.valueOf(ListaRegistro.get(i).getCultivo());

                if (cultivo.getSelectedItem().toString().equals(ListaRegistro.get(i).getCultivo())) {

                    ListaPlaga.add(ListaRegistro.get(i).getPlaga());

                }

            }

            Set<String> set2 = new HashSet<>(ListaPlaga);
            ListaPlaga.clear();
            ListaPlaga.addAll(set2);
            Plagas_final = ListaPlaga.toArray(new String[ListaPlaga.size()]);
        }catch (Exception e){

        }

    }


    private void ObtenerCultivo(){
        ListaCultivo = new ArrayList<>();
        try {
            for (int i = 0; i < ListaRegistro.size(); i++) {
                ListaCultivo.add(ListaRegistro.get(i).getCultivo());
            }
            Set<String> set = new HashSet<>(ListaCultivo);
            ListaCultivo.clear();
            ListaCultivo.addAll(set);
            Cultivo_final =  ListaCultivo.toArray(new String[ListaCultivo.size()]);
        }catch (Exception e){
        }
    }

    private void obtenerlista() {
        Listainformacion1 = new ArrayList<Double>();
        Listainformacion2 = new ArrayList<>();
        Listainformacion3 = new ArrayList<Double>();
        Listainformacion4 = new ArrayList<>();
       try {
           for (int i = 0; i < ListaRegistro.size(); i++){


               if (cultivo.getSelectedItem().toString().equals(ListaRegistro.get(i).getCultivo()) && plaga1.getSelectedItem().toString().equals(ListaRegistro.get(i).getPlaga())){

                       Listainformacion1.add(ListaRegistro.get(i).getDensidad());
                       //listainf.add((ListaRegistro.get(i).getFecha()));

               }
           }
           tamamo();
           graficar();
       }catch (Exception e){

       }


    }

    private void graficar() {
        grafica.removeAllSeries();
        grafica.getViewport().setMaxX(Listainformacion1.size() + 1 );
        DataPoint[] dataPoints = new DataPoint[Listainformacion1.size()];

        grafica.getViewport().setMaxY(Math.round(Double.valueOf( Collections.max(Listainformacion1))) + 1);
        for (int i=0 ; i < Listainformacion1.size();i++){
            dataPoints[i] = new DataPoint(i + 1, Double.valueOf(Listainformacion1.get(i)));
        }
        series = new  BarGraphSeries<>(dataPoints);
        grafica.addSeries(series);
        series.setColor(Color.RED);
        //grafica.getViewport().setXAxisBoundsManual(true);
       // grafica.getGridLabelRenderer().setHumanRounding(false);
        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
        //Toast.makeText(this,String.valueOf(Listainformacion1.size()),Toast.LENGTH_SHORT).show();


    }


    public  void tamamo(){
        if(Listainformacion1.size() > 30){
            for (int i =1; i <= 30;i++){
               Listainformacion3.add(Listainformacion1.get(Listainformacion1.size() - i));
            }
            Collections.reverse(Listainformacion3);
            Listainformacion1 = Listainformacion3;
        }

        if(Listainformacion2.size() > 30){
            for (int i =1; i <= 30;i++){
                Listainformacion4.add(Listainformacion2.get(Listainformacion2.size() - i));
            }
            Collections.reverse(Listainformacion4);
            Listainformacion2 = Listainformacion4;
        }

    }






}