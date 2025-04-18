package com.techco.Agromuestreo;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.techco.Agromuestreo.Entidad.Registro;
import com.techco.Agromuestreo.Entidad.registro2;
import com.techco.Agromuestreo.Utilidades.Entreno;
import com.techco.Agromuestreo.Utilidades.Utilidades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EnfermedadSe extends AppCompatActivity {


    public Spinner HOJA,AREA,ENFERMEDAD,MUESTREOS;
    public Button EMPEZAR;
    public ArrayAdapter<String> adapter2,adapter3,adapter4;
    public String [] Plantas;
    registro2  registro;
    ArrayList<String> ListaRegistro = null;
    ArrayList<String> ListaRegistro2 = null;
    ArrayList<String> ListaRegistro3 = null;
    ArrayList<registro2> ListaRegistro_1= null;
    public Double PCS = 0.0;
    ConexionSQliteHelper con;
    public String clasificacion = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enfermedad_se);


        HOJA = findViewById(R.id.hoja_2);
        EMPEZAR = findViewById(R.id.INICIAR);
        AREA = findViewById(R.id.AREAS_E);
        ENFERMEDAD = findViewById(R.id.PLAGA_2);
       // MUESTREOS = findViewById(R.id.Muestreo_selec);


        //String [] muestreo ={"Muestreo secuencial","Muestreo proporción"};

        try {
            subir_datos();
        }catch (Exception e){
            Toast.makeText(EnfermedadSe.this,"Bienvenido",Toast.LENGTH_SHORT).show();
        }
        String [] Hojas = {"Linear lanceolada","Eliptica","Lobulada","Palmeada","Triangular"};

        Plantas = new String [] { "Agave"  ,              "Aguacate"  ,           "AjonjolÍ"  ,           "Alfalfa"       ,       "Algodón"     ,         "Arroz"        ,        "Avena",
                "Brócoli"       ,       "Cacao"         ,       "Café"          ,       "Calabacita"    ,       "Caña de Azúcar"   ,    "Cártamo"       ,       "Cebada",
                "Cebolla"       ,       "Chile"          ,      "Melón"        ,       "Coco"         ,        "Durazno"       ,       "Espárragos"    , "Fresa",
                "Frijol",
                "Pepino" ,"Garbanzo"    ,         "Guayaba"   ,           "Jitomate"      ,       "Limón"      ,          "Maíz"     ,            "Mango",
                "Manzana"      ,        "Naranja"        ,      "Nogal"               ,         "Papa"         ,
                "Piña"        ,         "Plátano/Banano"        ,      "Sandía"       ,        "Sorgo"        ,        "Soya"              ,       "Tomate verde",
                "Toronja"       ,       "Trigo"          ,      "Nopal"        ,         "Vid"
                ,          "Otro"};

        String[] Areas = {
                "Hoja",
                "Foliolo"
        };

        String [] Enfermedad = {
                "Enfermedad1",
                "Enfermedad2",
                "otro"
        };

        String [] Otro = {"Otra"};


        String[] Plagas_maiz = {"Roya comun (Puccinia sorghi)", "Roya del sur (Puccinia polysora)"};
        String[] Plagas_frijol = {"Roya (Puccinia hordei)", "Moho blanco (Sclerotinia sclerotiorum)", "Antracnosis (Colletotrichum lindemuthianum)"};
        String[] Plagas_sorgo = {"Mancha foliar (Ascochyta sorghi)", "Mancha gris (Cercospora sorghi)"};
        String[] Plagas_cana_de_azucar = {"Roya marron (Puccinia melanocephala)", "Roya naranja (Puccinia kuehnii)", "Mancha parda (Cercospora longipides)", "Estria roja (Acidovorax avenae)"};
        String[] Plagas_cafe = {"Roya del cafe (Hemileia vastatrix)", "Mal de hilachas (Pellicularia koleroga)", "Antracnosis (Colletotrichum lindemuthianum)"};
        String[] Plagas_avena = {"Tizon foliar (Bipolaris spp.)", "Antracnosis (Colletotrichum graminicola)", "Mancha foliar (Drechslera avenacea)"};
        String[] Plagas_trigo = {"Roya de la hoja (Puccinia recondita)", "Mancha de la hoja (Septoria tritici)", "Roya amarilla (Puccinia striiformis)", "Mancha amarilla (Drechslera triticirepentis)"};
        String[] Plagas_alfalfa = {"Roya de la alfalfa (Uromyces striatus)", "Viruela de las hojas (Pseudopeziza medicaginis)"};
        String[] Plagas_naranja = {"Mancha grasienta (Micosphaerella citri)", "Melanosis (Diaporthe citri)"};
        String[] Plagas_cebada = {"Tizon foliar (Mycosphaerella graminicola)", "Septoriosis (Stagonospora nodorum)", "Roya amarilla (Puccinia striiformis)"};
        String[] Plagas_aguacate = {"Muerte regresiva (Botryosphaeria dothidea)", "Antracnosis (Colletotrichum gloeosporioides)", "Mancha negra (Cercospora purpura)"};
        String[] Plagas_limon = {"Mancha grasienta (Micosphaerella citri)", "Melanosis (Diaporthe citri)"};
        String[] Plagas_mango = {"Antracnosis (Colletotrichum gloeosporioides)", "Mancha algal (Cephaleuros virescens)", "Mancha negra (Alternaria alternata)"};
        String[] Plagas_soya = {"Tizon bacteriano (Pseudomonas syringae)", "Ojo de rana (Cercospora sojina)", "Roya de la soja (Phakopsora pachyrhizi)"};
        String[] Plagas_algodon = {"Mancha foliar (Mycosphaerella gossypina)", "Alternaria (Alternaria alternata)", "Roya (Puccinia cacabata)"};
        String[] Plagas_chile = {"Tizon temprano (Alternaria solani)", "Mancha gris (Stemphylium solani)", "Cenicilla (Leveillula taurica)"};
        String[] Plagas_nogal = {"Mancha vellosa (Mycosphaerella caryigena)", "Antracnosis (Gnomonia leptsostyla)", "Tizon bacteriano (Xanthomonas juglandis)"};
        String[] Plagas_agave = {"Tizon foliar (Cercospora agavicola)", "Marchitez (Fusarium oxysporum)", "Pudricion blanda (Pectobacterium carotovorum)"};
        String[] Plagas_coco = {"Tizon de la hoja (Pestalotia palmarum)"};
        String[] Plagas_garbanzo = {"Mildiu (Peronospora ciceris)", "Moho gris (Botrytis cinerea)", "Tizon (Alternaria alternata)"};
        String[] Plagas_platano = {"Sigatoka negra (Mycosphaerella fijiensis)", "Sigatoka amarilla (Mycosphaerella musicola)"};
        String[] Plagas_ajonjoli = {"Mancha foliar (Cercospora sesami)", "Mancha angular (Phaeoisariopsis griseola)"};
        String[] Plagas_papa = {"Tizon tardio (Phytophthora infestans)", "Tizon temprano (Alternaria solani)"};
        String[] Plagas_cacao = {"Antracnosis (Gnomonia leptsostyla)", "Mancha negra (Phytophthora sp.)"};
        String[] Plagas_manzano = {"Mancha foliar (Diplocarpon mali)", "Tizon de fuego (Erwinia amylovora)", "Sarna (Venturia inaequalis)", "Cenicilla (Podosphaera leucotricha)"};
        String[] Plagas_jitomate = {"Tizon temprano (Alternaria solani)", "Mancha bacteriana (Xanthomonas spp.)", "Tizon tardio (Phytophthora infestans)", "Cenicilla (Leveillula taurica)", "Mancha gris (Stemphylium solani)"};
        String[] Plagas_cebolla = {"Mildiu (Pseudoperonospora cubensis)", "Mancha purpura (Alternaria porri)", "Tizon (Botrytis escamosa)"};
        String[] Plagas_nopal = {"Mal del oro (Alternaria sp.)", "Mancha negra (Pseudocercospora opuntiae)"};
        String[] Plagas_arroz = {"Alternaria (Alternaria sp.)", "Marchitez (Fusarium equiseti)", "Curvularia (Curvularia geniculata)", "Quema (Magnaporthe oryzae)"};
        String[] Plagas_tomate_verde = {"Cenicilla (Oidium sp.)", "Mancha de la hoja (Cercospora physalidis)", "Carbon blanco (Entyloma australe)"};
        String[] Plagas_esparrago = {"Roya del esparrago (Puccinia asparagi)", "Mancha purpura (Stemphylium vesicarium)"};
        String[] Plagas_sandia = {"Cenicilla (Erysiphe cichoracearum)", "Antracnosis (Colletotrichum orbiculare)", "Mildiu (Pseudoperonospora cubensis)", "Fusariosis (Fusarium oxysporum)", "Mancha bacteriana (Pseudomonas spp.)"};
        String[] Plagas_vid = {"Cenicilla de la vid (Uncinula necator)", "Mildiu de la vid (Plasmopara viticola)"};
        String[] Plagas_brocoli = {"Mancha negra (Alternaria brassicae)", "Botritis (Botrytis cinerea)", "Mildiu (Peronospora parasitica)"};
        String[] Plagas_durazno = {"Tiro de municion (Coryneum beijerinckii)", "Verrucosis (Taphrina deformans)"};
        String[] Plagas_pina = {"Marchitez (Fusarium oxysporum)", "Pudricion del corazon (Phytophthora parasitica)", "Escaldadura (Erwinia chrysanthemi)"};
        String[] Plagas_cartamo = {"Falsa cenicilla (Ramularia carthami)", "Tizon foliar (Carthamus tinctorius)", "Roya (Puccinia carthami)"};
        String[] Plagas_calabacita = {"Cenicilla (Erysiphe cichoracearum)", "Mancha foliar (Cladosporium cucumerinum)", "Mildiu (Pseudoperonospora cubensis)", "Mancha bacteriana (Pseudomonas spp.)"};
        String[] Plagas_guayaba = {"Clavo (Pestalotia versicolor)", "Antracnosis (Colletotrichum gloeosporioides)"};
        String[] Plagas_toronja = {"Mancha grasienta (Micosphaerella citri)", "Melanosis (Diaporthe citri)"};
        String[] Plagas_fresa = {"Mancha purpura (Mycosphaerella fragarie)", "Cenicilla (Sphaeroteca sp.)", "Pudricion de la corona (Neopestalotiopsis spp.)"};
        String[] Plagas_pepino = {"Cenicilla (Erysiphe cichoracearum)", "Mancha foliar (Corynespora cassiicola)", "Mildiu (Pseudoperonospora cubensis)", "Mancha bacteriana (Pseudomonas spp.)"};
        String[] Plagas_melon = {"Cenicilla (Erysiphe cichoracearum)", "Mancha foliar (Corynespora cassiicola)", "Mildiu (Pseudoperonospora cubensis)", "Mancha bacteriana (Pseudomonas spp.)"};



        adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                ,Plantas);
        HOJA.setAdapter(adapter2);

        HOJA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {



                if (position == 0){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_agave);
                    ENFERMEDAD.setAdapter(adapter2);

                }
                if (position == 1){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_aguacate);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 2){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_ajonjoli);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 3){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_alfalfa);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 4){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_algodon);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 5){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_arroz);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 6){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_avena);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 7){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_brocoli);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 8){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_cacao);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 9){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_cafe);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 10){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_calabacita);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 11){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_cana_de_azucar);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 12){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_cartamo);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 13){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_cebada);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 14){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_cebolla);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 15){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_chile);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 16){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_melon);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 17){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_coco);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 18){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_durazno);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 19){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_esparrago);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 20){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_fresa);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 21){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_frijol);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 22){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_pepino);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 23){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_garbanzo);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 24){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_guayaba);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 25){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_jitomate);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 26){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_limon);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 27){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_maiz);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 28){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_mango);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 29){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_manzano);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 30){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_naranja);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 31){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_nogal);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 32){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_papa);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 33){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_pina);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 34){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_platano);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 35){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_sandia);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 36){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_sorgo);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 37){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_soya);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 38){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_tomate_verde);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 39){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_toronja);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 40){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_trigo);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 41){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_nopal);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 42){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_vid);
                    ENFERMEDAD.setAdapter(adapter2);
                }
                if (position == 43){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Otro);
                    ENFERMEDAD.setAdapter(adapter2);
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Enfermedad);
                ENFERMEDAD.setAdapter(adapter2);
            }

        });




        adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Areas);
        AREA.setAdapter(adapter2);

       /* adapter3 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Enfermedad);
        ENFERMEDAD.setAdapter(adapter3);

        */
       // adapter4 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
               // ,muestreo);
       // MUESTREOS.setAdapter(adapter4);

        EMPEZAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    String enfermedad = HOJA.getSelectedItem().toString();

                    if (enfermedad.equals("Otro")){

                        dialogo();

                    }else {

                        Bundle miBundle = new Bundle();
                        Bundle eBundle = new Bundle();
                        eBundle.putString("cultivo", HOJA.getSelectedItem().toString());
                        eBundle.putString("plaga", ENFERMEDAD.getSelectedItem().toString());
                        miBundle.putString("muestreo", "Muestreo secuencial");
                        miBundle.putString("area", AREA.getSelectedItem().toString());
                        miBundle.putString("etapa", "Enfermedad");
                        miBundle.putString("nivel",clasificacion);

                        Intent i = new Intent(EnfermedadSe.this, Grafica.class);
                        i.putExtras(miBundle);
                        i.putExtras(eBundle);
                        startActivity(i);
                    }
               }catch (Exception e){

                    Toast.makeText(EnfermedadSe.this,"Error al iniciar",Toast.LENGTH_SHORT).show();
                }

            }
        });

        consulta();

    }//fin del on create para escribir nuevas void
    private String[] concat(String[] A, String[] B) {
        int aLen = A.length;
        int bLen = B.length;
        String[] C= new String[aLen+bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }

    public void dialogo (){
        AlertDialog.Builder builder = new AlertDialog.Builder(EnfermedadSe.this);
        LayoutInflater inflater = getLayoutInflater();
        View view2  = inflater.inflate(R.layout.alert_dialog4,null);
        builder.setView(view2);
        AlertDialog dialog = builder.create();

        Button cancelar = view2.findViewById(R.id.cancelar1);
        Button continuar = view2.findViewById(R.id.continuar1);
        AutoCompleteTextView clt = view2.findViewById(R.id.editcultivo1);
        AutoCompleteTextView plg = view2.findViewById(R.id.editplaga1);

        String [] plagas_total2 = {
                "Roya comun (Puccinia sorghi)", "Roya del sur (Puccinia polysora)", "Roya (Puccinia hordei)", "Moho blanco (Sclerotinia sclerotiorum)", "Antracnosis (Colletotrichum lindemuthianum)", "Mancha foliar (Ascochyta sorghi)", "Mancha gris (Cercospora sorghi)", "Roya marron (Puccinia melanocephala)", "Roya naranja (Puccinia kuehnii)", "Mancha parda (Cercospora longipides)", "Estria roja (Acidovorax avenae)", "Roya del cafe (Hemileia vastatrix)", "Mal de hilachas (Pellicularia koleroga)", "Tizon foliar (Bipolaris spp.)", "Antracnosis (Colletotrichum graminicola)", "Mancha foliar (Drechslera avenacea)", "Roya de la hoja (Puccinia recondita)", "Mancha de la hoja (Septoria tritici)", "Roya amarilla (Puccinia striiformis)", "Mancha amarilla (Drechslera triticirepentis)", "Roya de la alfalfa (Uromyces striatus)", "Viruela de las hojas (Pseudopeziza medicaginis)", "Mancha grasienta (Micosphaerella citri)", "Melanosis (Diaporthe citri)", "Tizon foliar (Mycosphaerella graminicola)", "Septoriosis (Stagonospora nodorum)", "Muerte regresiva (Botryosphaeria dothidea)", "Mancha negra (Cercospora purpura)", "Mancha algal (Cephaleuros virescens)", "Mancha negra (Alternaria alternata)", "Tizon bacteriano (Pseudomonas syringae)", "Ojo de rana (Cercospora sojina)", "Roya de la soja (Phakopsora pachyrhizi)", "Mancha foliar (Mycosphaerella gossypina)", "Alternaria (Alternaria alternata)", "Cenicilla (Leveillula taurica)", "Mancha vellosa (Mycosphaerella caryigena)", "Tizon bacteriano (Xanthomonas juglandis)", "Marchitez (Fusarium oxysporum)", "Pudricion blanda (Pectobacterium carotovorum)", "Tizon de la hoja (Pestalotia palmarum)", "Mildiu (Peronospora ciceris)", "Moho gris (Botrytis cinerea)", "Tizon (Alternaria alternata)", "Sigatoka negra (Mycosphaerella fijiensis)", "Sigatoka amarilla (Mycosphaerella musicola)", "Mancha foliar (Cercospora sesami)", "Mancha angular (Phaeoisariopsis griseola)", "Tizon tardio (Phytophthora infestans)", "Mancha foliar (Diplocarpon mali)", "Tizon de fuego (Erwinia amylovora)", "Sarna (Venturia inaequalis)", "Mancha bacteriana (Xanthomonas spp.)", "Mancha purpura (Alternaria porri)", "Tizon (Botrytis escamosa)", "Mal del oro (Alternaria sp.)", "Curvularia (Curvularia geniculata)", "Quema (Magnaporthe oryzae)", "Carbon blanco (Entyloma australe)", "Roya del esparrago (Puccinia asparagi)", "Mancha purpura (Stemphylium vesicarium)", "Fusariosis (Fusarium oxysporum)", "Tiro de municion (Coryneum beijerinckii)", "Verrucosis (Taphrina deformans)", "Pudricion del corazon (Phytophthora parasitica)", "Escaldadura (Erwinia chrysanthemi)", "Falsa cenicilla (Ramularia carthami)", "Mancha foliar (Cladosporium cucumerinum)", "Clavo (Pestalotia versicolor)", "Pudricion de la corona (Neopestalotiopsis spp.)", "Mancha foliar (Corynespora cassiicola)", "Cenicilla de la vid (Uncinula necator)", "Mildiu de la vid (Plasmopara viticola)", "Botritis (Botrytis cinerea)", "Mildiu (Peronospora parasitica)"

        };

        //String [] Etapa = {"No seleccionado","Individuos Totales","Huevo","Ninfa/Larva","Formas Móviles","Adulto"};


        ListaRegistro3 = new ArrayList<>();
        if (ListaRegistro.size() !=  0) {

            Set<String> set = new HashSet<>(ListaRegistro2);
            ListaRegistro2.clear();
            ListaRegistro2.addAll(set);

            String[] plaga_db = ListaRegistro2.toArray(new String[ListaRegistro2.size()]);


            for (int i = 0; i < ListaRegistro.size(); i++) {

                String clt2 = String.valueOf(ListaRegistro.get(i));
                String[] prtes = clt2.split("Y");
                String cultivo2 = prtes[0];



                //plg.setText(cultivo2 );
                ListaRegistro3.add(cultivo2);
            }

            Set<String> set2 = new HashSet<>(ListaRegistro3);
            ListaRegistro3.clear();
            ListaRegistro3.addAll(set2);
            String[] cultivo_db = ListaRegistro3.toArray(new String[ListaRegistro3.size()]);

            plagas_total2 = concat(plagas_total2,plaga_db);
            Plantas = concat(Plantas,cultivo_db);

        }



        dialog.setCancelable(false);
        dialog.show();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Plantas);
        clt.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,plagas_total2);
        plg.setAdapter(adapter2);

       // ArrayAdapter adapter3 = new ArrayAdapter<String>(EnfermedadSe.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Etapa);
       // Estado.setAdapter(adapter3);



        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle miBundle = new Bundle();
                Bundle eBundle = new Bundle();
                dialog.dismiss();

                Toast.makeText(EnfermedadSe.this,clt.getText().toString(),Toast.LENGTH_SHORT).show();

                eBundle.putString("cultivo", clt.getText().toString());
                eBundle.putString("plaga", plg.getText().toString());
                miBundle.putString("muestreo", "Muestreo secuencial");
                miBundle.putString("area",AREA.getSelectedItem().toString());
                miBundle.putString("etapa","Enfermedad");
                miBundle.putString("nivel",clasificacion);



                //AREA
                //Intent i = new Intent(getActivity(), INSTRUCCIONES.class);
                Intent i = new Intent(EnfermedadSe.this,Grafica.class);
                i.putExtras(miBundle);
                i.putExtras(eBundle);
                startActivity(i);
            }
        });



    }

    private  void subir_datos() {
        con = new ConexionSQliteHelper(EnfermedadSe.this, "db_Registro", null, 1);
        SQLiteDatabase db = con.getReadableDatabase();
        Registro registro;
        Registro registro2;
        ListaRegistro = new ArrayList<>();
        ListaRegistro2 = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_REGISTRO, null);

        while (cursor.moveToNext()) {


            registro = new Registro();
            registro2 = new Registro();
            //registro.setFecha(cursor.getString(2));
            registro.setCultivo(cursor.getString(0));
            registro2.setPlaga(cursor.getString(1));
            //registro.setDensidad(cursor.getDouble(3));
            //registro.setLatitud(cursor.getString(4));
            //registro.setLonguitud(cursor.getString(5));
            //registro.setArea(cursor.getString(6));
            //registro.setCapturas(cursor.getString(7));

            ListaRegistro.add(cursor.getString(0));
            ListaRegistro2.add(cursor.getString(1));
        }
        cursor.close();
        db.close();
    }

    public void consulta(){
        try{

            SQLiteDatabase db = new Entreno(this, "my_database", null, 1).getReadableDatabase();
            ListaRegistro_1 = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT " + Entreno.HOJA + ", MAX(" + Entreno.PC + ") FROM " + Entreno.TABLE_NAME + " GROUP BY " + Entreno.HOJA, null);
            while (cursor.moveToNext()) {
                registro2 registro = new registro2();
                registro.setHoja(cursor.getString(0));
                registro.setPC(cursor.getDouble(1));
                ListaRegistro_1.add(registro);
            }
            cursor.close();
            db.close();

            for (int i = 0; i < ListaRegistro_1.size(); i++){
                if (ListaRegistro_1.get(i).getHoja().equals("General")){
                    PCS = ListaRegistro_1.get(i).getPC();
                }
            }

            if (ListaRegistro.isEmpty()) {

                PCS=0.0;
                //Toast.makeText(this,String.valueOf(PCS),Toast.LENGTH_SHORT).show();


            } else {

                //Toast.makeText(this,String.valueOf(PCS),Toast.LENGTH_SHORT).show();


            }
            clasific();

            cursor.close();
            db.close();

        }catch (Exception e){
            Toast.makeText(this,"Error en la consulta",Toast.LENGTH_SHORT   ).show();

        }
    }

    public void clasific(){
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

    }

}