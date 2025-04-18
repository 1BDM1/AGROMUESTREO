package com.techco.Agromuestreo.ui.home;

import android.Manifest;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.techco.Agromuestreo.ConexionSQliteHelper;
import com.techco.Agromuestreo.Entidad.Registro;
import com.techco.Agromuestreo.Grafica;
import com.techco.Agromuestreo.Grafica2;
import com.techco.Agromuestreo.R;
import com.techco.Agromuestreo.Utilidades.Utilidades;
import com.techco.Agromuestreo.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PLAGA_ACTIVITY extends AppCompatActivity {
    private FragmentHomeBinding binding;
    private Spinner spinner1,spinner2,spinner3,spinner4;
    public CheckBox caja;
    public int check = 0;
    public ImageView ima;
    public String plagas [];
    public String [] Plantas;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    ConexionSQliteHelper con;
    ArrayList<String> ListaRegistro = null;
    ArrayList<String> ListaRegistro2 = null;
    ArrayList<String> ListaRegistro3 = null;
    public String etapa_n = null;
    public int et = 0;
    Button comenzar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        //            setHasOptionsMenu(true);
        caja = findViewById(R.id.checkbox);
        ima = findViewById(R.id.imageView5);

        //PARA INICIALIZAR EL ESTADO DE PLAGAS
        et = 0;

        caja.setChecked(false);

        ima.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder2 = new AlertDialog.Builder(PLAGA_ACTIVITY.this);
                builder2.setTitle("Doble Muestreo");
                builder2.setMessage("Para  muestrear dos plagas o estados biológicos de forma simultánea, deberá especificar el nombres del cultivo, nombre  común de las plagas o los estados biológicos.");
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



        SharedPreferences sp = PLAGA_ACTIVITY.this.getSharedPreferences("M_P",0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("F_R",true).apply();
        boolean firstrun = sp.getBoolean("F_R",true);

        UiModeManager uiModeManager = (UiModeManager) PLAGA_ACTIVITY.this.getSystemService(UI_MODE_SERVICE);

        if(uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
            // Crear un AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(PLAGA_ACTIVITY.this);
            // Configurar el título, el mensaje y el botón positivo del diálogo
            builder.setTitle("Modo oscuro activado");
            builder.setMessage("Por favor, desactiva el modo oscuro y vuelve a iniciar la aplicacion");
            builder.setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Finalizar la actividad al hacer clic en el botón
                    PLAGA_ACTIVITY.this.finish();
                }
            });
            builder.setCancelable(false);
            // Mostrar el diálogo
            builder.show();
        }



        //PERMISO DE ESCRITURA EN LA APLICACION

        if (ActivityCompat.checkSelfPermission(PLAGA_ACTIVITY.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(PLAGA_ACTIVITY.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED){



            ActivityCompat.requestPermissions(PLAGA_ACTIVITY.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},1000);
        }


        // Obtener el LocationManager
        LocationManager locationManager = (LocationManager) PLAGA_ACTIVITY.this.getSystemService(Context.LOCATION_SERVICE);

// Verificar si el proveedor de ubicación está habilitado

        boolean isGpsEnabled = false;
        boolean isNetworkEnabled = false;

        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

// Si no está habilitado, mostrar un diálogo
        //  if (!isGpsEnabled && !isNetworkEnabled) {

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            // Crear un AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(PLAGA_ACTIVITY.this);
            // Establecer el título y el mensaje
            builder.setTitle("Ubicación desactivada");
            builder.setMessage("Por favor, activa la ubicación para usar esta aplicación.");
            // Establecer el botón positivo que lleva a la configuración de ubicación
            builder.setPositiveButton("Ir a configuración", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Crear un Intent con la acción Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    // Iniciar la actividad con el intent
                    startActivity(intent);
                }
            });
            // Establecer el botón negativo que cierra el diálogo
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Cerrar el diálogo
                    dialog.dismiss();
                    PLAGA_ACTIVITY.this.finish();
                }
            });
            // Mostrar el diálogo
            builder.create().show();
            builder.setCancelable(false);
        }

        //OBTENER INFORMACION DE LA BASE DE DATOS

        try {
            subir_datos();
        }catch (Exception e){
            Toast.makeText(PLAGA_ACTIVITY.this,"Bienvenido",Toast.LENGTH_SHORT).show();
        }

        //SUGERENCIAS PARA EL USUARIO

        AlertDialog.Builder builder2 = new AlertDialog.Builder(PLAGA_ACTIVITY.this);
        builder2.setTitle("AVISO");
        builder2.setMessage("Tiene el modo oscuro activado, desactivelo y vuelva a iniciar la aplicación");
        builder2.setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }

        });

        Dialog dialog2 = builder2.create();

        if (firstrun){
            dialog2.setCancelable(false);

            firstrun = false;
            editor.putBoolean("F_R",firstrun).apply();

        }


        spinner1 = findViewById(R.id.spinner) ;
        spinner2 = findViewById(R.id.spinner2) ;
        spinner3 = findViewById(R.id.spinnertipo);
        spinner4 = findViewById(R.id.spinner4);

        //SPINNER DE ETAPA FENOLOGICA



        String[] Areas = {"Planta completa",
                "Hoja",
                "Foliolo",
                "Fruto",
                "Tallo",
                "Flor",
                "Inflorescencia",
                "Porción de suelo"
        };

        String[] Plantas2 = {"Mango (Ataulfo)",
                "Soya",
                "Frijol",
                "Sorgo",
                "Caña de Azucar",
                "Café",
                "Avena",
                "Trigo",
                "Alfalfa",
                "Naranja",
                "Cebada",
                "Aguacate",
                "Limon",
                "Algodon",
                "Chile",
                "Nuez",
                "Agave",
                "Coco",
                "Garbanzo",
                "Plátano",
                "Ajonjoli",
                "Cacao",
                "Manzana",
                "Jitomate",
                "Tuna",
                "Arroz",
                "Tomate en verde",
                "Esparragos",
                "Sandia",
                "Vid",
                "Durazno",
                "Piña",
                "Cartamo",
                "Calabacita",
                "Guayaba",
                "Toronja",
                "Maiz",
                "Clavel",
                "Frutilla Invernadero",
                "Pimiento Invernadero",
                "Manzano",
                "Brocoli",
                "Papa",
                "Tomate",
                "Cebolla",
                "Otro"
        };

        Plantas = new String [] { "Agave"  ,              "Aguacate"  ,           "AjonjolÍ"  ,           "Alfalfa"       ,       "Algodón"     ,         "Arroz"        ,        "Avena",
                "Brócoli"       ,       "Cacao"         ,       "Café"          ,       "Calabacita"    ,       "Caña de Azúcar"   ,    "Cártamo"       ,       "Cebada",
                "Cebolla"       ,       "Chile"          ,      "Clavel"        ,       "Coco"         ,        "Durazno"       ,       "Espárragos"    , "Fresa",
                "Frijol",
                "Arándano" ,"Garbanzo"    ,         "Guayaba"   ,           "Jitomate"      ,       "Limón"      ,          "Maíz"     ,            "Mango",
                "Manzana"      ,        "Naranja"        ,      "Nogal"               ,         "Papa"         ,        "Pimiento",
                "Piña"        ,         "Plátano/Banano"        ,      "Sandía"       ,        "Sorgo"        ,        "Soya"              ,       "Tomate verde",
                "Toronja"       ,       "Trigo"          ,      "Nopal"        ,         "Vid"
                ,          "Otro"};

        String [] Plagas = {"Plaga1","Plaga2","Plaga2"};
        String [] Tamaño ={"Menor o igual a 2 Hectareas","Entre 2 y 5 Hectáreas","Mayor a 5 Hectáreas"};
        String [] muestreo ={"Muestreo secuencial","Muestreo proporción"};

        String [] Plagas_frijol = {"Epilachna sp. (Conchuela)","Diabrotica balteata (Diabrótica)","Liriomyza spp. (Minador)"};
        String [] Plagas_sorgo = {"Melanaphis sacchari (Pulgón amarillo)","Stenodiplosis sorghicola (Mosca del sorgo)", "Oebalus mexicanus (Chinche café)"};
        String [] Plagas_CanaAzucar = {"Diatraea saccharalis (Barrenador del tallo)","Aeneolamia sp. (Mosca pinta)","Eoreuma loftini (Barrenador mexicano)"};
        String [] Plagas_cafe = {"Hypothenemus hampei (Broca del café)","Leucoptera coffeella (Minador de la hoja)","Monoflata pallescens (Papalota del café)"};

        String [] Plagas_avena = {"Schizaphis graminum (Pulgón verde)","Rhopalosiphum padi (Pulgón del tallo)","Diuraphis noxia (Pulgón ruso)"};
        String [] Plagas_trigo = {"Sitobion avenae (Pulgón de la espiga)","Schizaphis graminum (Pulgón verde)","Rhopalosiphum padi (Pulgón del tallo)"};
        String [] Plagas_alfalfa = {"Epicauta spp. (Escarabajo)","Hypera postica (Picudo)","Acyrthosiphon pisum (Pulgón verde)"};

        String [] Plagas_naranja = {"Diaphorina citri (Psílido asiático)","Phyllocoptruta oleivora (Arador de los cítricos)"};
        String [] Plagas_cebada = {"Schizaphis graminum (Pulgón verde)","Diuraphis noxia (Pulgón ruso)","Metopolophium dirhodum (Pulgón amarillo)"};

        String [] Plagas_aguacate = {"Conotrachelus aguacatae (Barrenador del hueso)","Copturus aguacatae (Barrenador de las ramas)","Scirtothrips perseae (Trips)"};
        String [] Plagas_limon = {"Diaphorina citri (Psílido asiático)","Panonychus citri (Ácaro rojo)","Tetranychus urticae (Araña roja)"};

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

        String [] Plaga_Manzana = {"Cydia pomonella (Palomilla de la manzana)","Eriosoma lanigerum (Pulgón lanígero)","Rhagoletis pomonella (Mosca de la manzana)","Panonychus ulmi (Araña roja)"};
        String [] Plaga_Jitomate = {"Trialeurodes vaporariorum (Mosquita blanca)","Bemisia tabaci (Mosca blanca)", "Spodoptera exigua (Gusano soldado)","Helicoverpa zea (Gusano del fruto)","Bactericera cockerelli (Paratrioza)"};
        String [] Plagas_Tune = {"Cactophagus spinolae (Picudo del nopal)","Cylindrocopturus biradiatus (Picudo de las espinas)","Dactylopius opuntiae (Cochinilla o grana)"};

        String [] Plagas_arroz = {"Rupela albinella (Palomilla blanca)","Tagosodes orizicolus (Sogata del arroz)","Oebalus insularis (Chinche café)"};
        String [] Plaga_tomateverde = {"Trialeurodes vaporariorum (Mosquita blanca)","Heliothis subflexa (Gusano del fruto)","Trichobaris championi (Barrenador del tallo)","Bactericera cockerelli (Paratrioza)"};
        String [] Plaga_esparragos = {"Spodoptera exigua (Gusano soldado)","Tetranychus urticae (Araña roja)","Brachycorynella asparagi (Pulgón)"};
        String [] Plagas_sandia = {"Bemisia tabaci (Mosca blanca)", "Diabrotica sp. (Diabrótica)","Liriomyza spp. (Minador)"};

        String [] Plagas_vid = {"Dikrella cockerelli (Chicharrita)","Harrisina spp. (Descarnador de la hoja)","Planococcus ficus (Piojo harinoso)"};
        String [] Plagas_durazno = {"Eotetranychus lewisi (Araña roja)","Myzus persicae (Pulgón verde)","Macrodactylus mexicanus (Frailecillo)"};
        String [] Plaga_pina = {"Scutigerella immaculata (Sinfílidos)","Dysmicoccus neobrevipes (Cochinilla)","Metamasius dimidiatipennis (Picudo)"};

        String [] Plaga_cartamo = {"Agrotis sp. (Gusano trozador)","Blapstinus spp. (Cochinilla prieta)","Myzus persicae (Pulgón)"};
        String [] Plaga_calabacita = {"Bemisia tabaci (Mosca blanca)","Aphis gossypii (Pulgón verde)","Frankliniella occidentalis (Trips)"};
        String [] Plaga_guayaba = {"Conotrachelus psidii (Picudo de la guayaba)","Phyllophaga spp. (Gallina ciega)"};

        String [] Plaga_toronja = {"Diaphorina citri (Psílido asiático)","Toxoptera aurantii (Pulgón negro)","Phyllocnistis citrella (Minador)"};
        String [] Plagas_mango = {"Aulacaspis tubercularis (Escama)","Frankliniella occidentalis (Trips)","Ormenis pulverulenta (Papalota del mango)"};
        String [] Plagas_Soja = {"Piezodorus guildinii (Chinche de la Alfalfa)","Dichelops furcatus (Chinche de los cuernos)","Anticarsia gemmatalis (Gusano terciopelo)","Nezara viridula (Chinche apestosa)","Caliothrips phaseoli (Trips negro)"};

        String [] Plagas_maiz = {"Diatraea saccharalis (General)[Barrenador de la caña]","Spodoptera frugiperda (Gusano cogollero)","Helicoverpa zea (Gusano elotero)","Diabrotica virgifera (Diabrótica)","Phyllophaga spp. (Gallina ciega)"};
        String [] Plagas_clavel = {"Frankliniella occidentalis (Trips)"};
        //Frutilla invernadero = Arandano
        String [] Frutilla_invernadero = {"Frankliniella occidentalis (Trips)"};
        String [] Pimiento_invernadero = {"Frankliniella occidentalis (Trips)"};
        String [] Aguacate_hass = {"Oligonychus Punicae", "Olignugychus Perseae"};
        String [] Plagas_manzano = {"Panonychus Ulmi (Formas Moviles)"};
        String [] Plagas_brocoli = {"Trichoplusia ni (Gusano falso medidor)","Brevicoryne brassicae (Pulgón cenizo)","Plutella xylostella (Dorso de diamante)"};

        String [] Plagas_papa = {"Thrips palmi (Trips)","Leptinotarsa decemlineata (Catarinita)","Phthorimaea operculella (Palomilla de la papa)","Bactericera cockerelli (Paratrioza)"};
        String [] Plagas_tabaco = {"Trips"};
        //String [] Plagas_alfalfa = {"Threrioapis trifolli (Adulto Alado)","Threrioapis trifolli (Adulto Aptero)", "Threrioapis trifolli (Ninfas Grandes)","Threrioapis trifolli  (Ninfas Pequeñas)"};
        String [] Plagas_pinus = {"Thaumetopoea pityocampa"};
        String [] Plagas_tomate = {"Trialeurodes vaporariorum (Mosquita blanca)"};
        String [] Plagas_cebolla = {"Thrips tabaci (Trips)", "Spodoptera exigua (Gusano soldado)","Spodoptera frugiperda (Gusano cogollero)"};


        String [] Plagas_fresa = {"Duponchelia fovealis (Palomilla Europea)"};
        String [] Otro = {"Otra"};






        ArrayAdapter <String> adapter3 = new ArrayAdapter<>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Tamaño);
        spinner3.setAdapter(adapter3);

        ArrayAdapter<String> adaptert = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,muestreo);
        spinner3.setAdapter(adaptert);


        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Areas);
        spinner4.setAdapter(adapter4);

        adapter = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plantas);
        spinner1.setAdapter(adapter);



        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position == 0){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_agave);
                    spinner2.setAdapter(adapter2);

                }
                if (position == 1){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_aguacate);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 2){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,PLagas_ajonjoli);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 3){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_alfalfa);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 4){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_algodon);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 5){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_arroz);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 6){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_avena);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 7){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_brocoli);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 8){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plaga_cacao);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 9){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_cafe);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 10){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plaga_calabacita);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 11){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_CanaAzucar);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 12){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plaga_cartamo);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 13){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_cebada);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 14){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_cebolla);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 15){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_chile);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 16){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_clavel);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 17){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_coco);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 18){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_durazno);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 19){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plaga_esparragos);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 20){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_fresa);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 21){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_frijol);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 22){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Frutilla_invernadero);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 23){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_garbanzo);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 24){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plaga_guayaba);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 25){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plaga_Jitomate);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 26){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_limon);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 27){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_maiz);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 28){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_mango);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 29){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plaga_Manzana);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 30){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_naranja);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 31){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_nuez);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 32){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_papa);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 33){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Pimiento_invernadero);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 34){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plaga_pina);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 35){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_platano);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 36){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_sandia);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 37){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_sorgo);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 38){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_Soja);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 39){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plaga_tomateverde);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 40){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plaga_toronja);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 41){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_trigo);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 42){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_Tune);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 43){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas_vid);
                    spinner2.setAdapter(adapter2);
                }
                if (position == 44){
                    ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Otro);
                    spinner2.setAdapter(adapter2);
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plagas);
                spinner2.setAdapter(adapter2);
            }

        });





        caja.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if(caja.isChecked()) {

                    check = 1;
                    dialogo2();


                }

            }
        });


        comenzar = findViewById(R.id.INICIAR);
        comenzar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                try {

                    //String tamano = spinner3.getSelectedItem().toString();
                    String cultivo = spinner1.getSelectedItem().toString();
                    String Plaga = spinner2.getSelectedItem().toString();
                    String area = spinner4.getSelectedItem().toString();

                    if (cultivo.equals("Otro")){

                        dialogo();

                    }else {
                        if(et == 0){
                            dialogo_etapa();
                        }else {

                            Bundle miBundle = new Bundle();
                            Bundle eBundle = new Bundle();
                            eBundle.putString("cultivo", spinner1.getSelectedItem().toString());
                            eBundle.putString("plaga", spinner2.getSelectedItem().toString());
                            miBundle.putString("muestreo", spinner3.getSelectedItem().toString());
                            miBundle.putString("area", area);
                            miBundle.putString("etapa",etapa_n);
                            Intent i = new Intent(PLAGA_ACTIVITY.this, Grafica.class);

                            i.putExtras(miBundle);
                            i.putExtras(eBundle);
                            et = 0;
                            startActivity(i);
                        }
                    }

                }catch (Exception e){

                    Toast.makeText(PLAGA_ACTIVITY.this,"Error al iniciar",Toast.LENGTH_SHORT).show();
                }
            }
        });

    } //Oncreate

    public void dialogo_etapa (){
        AlertDialog.Builder builder = new AlertDialog.Builder(PLAGA_ACTIVITY.this);
        LayoutInflater inflater = getLayoutInflater();
        View view  = inflater.inflate(R.layout.dialogo_etapa,null);
        builder.setView(view);
        AlertDialog dialog2 = builder.create();
        Button cancelar =view.findViewById(R.id.cancelar);
        Button continuar = view.findViewById(R.id.continuar);
        TextView texto = view.findViewById(R.id.textos);

        Spinner areas = view.findViewById(R.id.spinarea);
        dialog2.setCancelable(false);
        dialog2.show();

        String [] Etapa = {"No seleccionado","Individuos Totales","Huevo","Ninfa/Larva","Formas Móviles","Adulto"};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Etapa);
        areas.setAdapter(adapter);
        ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Etapa);
        areas.setAdapter(adapter2);



        // etapa_n = areas.getSelectedItem().toString();

        continuar.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {


                                             if (et == 0 ) {
                                                 etapa_n = areas.getSelectedItem().toString();
                                                 if (etapa_n.equals("No seleccionado")) {

                                                     Toast.makeText(PLAGA_ACTIVITY.this, "Selecciona una etapa", Toast.LENGTH_SHORT).show();
                                                 } else {
                                                     dialog2.dismiss();
                                                     etapa_n = areas.getSelectedItem().toString();
                                                     et = 1;
                                                 }
                                             }
                                         }
                                     }
        );

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }});

    }

    public void dialogo2 (){
        AlertDialog.Builder builder = new AlertDialog.Builder(PLAGA_ACTIVITY.this);
        LayoutInflater inflater = getLayoutInflater();
        View view  = inflater.inflate(R.layout.doble_muestreo,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        Button cancelar =view.findViewById(R.id.cancelar);
        Spinner etapa1 = view.findViewById(R.id.estado1);
        Spinner etapa2 = view.findViewById(R.id.estado2);
        Button continuar = view.findViewById(R.id.continuar);
        AutoCompleteTextView clt = view.findViewById(R.id.spinner);
        AutoCompleteTextView plg = view.findViewById(R.id.spinner2);
        AutoCompleteTextView plg2 = view.findViewById(R.id.spinner3);
        Spinner area = view.findViewById(R.id.area);
        dialog.show();
        dialog.setCancelable(false);

        String [] Etapa = {"No seleccionado","Individuos Totales","Huevo","Ninfa/Larva","Formas Móviles","Adulto"};

        String [] plagas_total2 = {
                "Epilachna varibestis (Conchuela)",
                "Diabrótica balteata (Diabrótica)",
                "Liriomyza spp. (Minador)",
                "Melanaphis sacchari (Pulgón amarillo)",
                "Stenodiplosis sorghicola (Mosca midge)",
                "Oebalus mexicana (Chinche café)",
                "Diatrea saccharalis (Barrenador del tallo)",
                "Aeneolamia sp. (Mosca pinta)",
                "Eoreuma loftini (Barrenador mexicano)",
                "Hypotenemus hampei (Broca del café)",
                "Leucoptera coffeella (Minador de la hoja)",
                "Monoflata pallescens (Papalota del café)",
                "Schizaphis graminum (Pulgón verde)",
                "Rhopalosiphum padi (Pulgón del tallo)",
                "Diuraphis noxia (Pulgón ruso)"
                , "Sitobium avenae (Pulgón de la espiga)"
                ,"Epicauta spp. (Escarabajo)"
                ,"Hypera postica (Picudo)"
                ,"Acyrthosiphon pisum (Pulgón verde)"
                ,"Diaphorina citri (Psílido asiático - Huevos)"
                ,"Diaphorina citri (Psílido asiático - Ninfas)"
                ,"Diaphorina citri (Psílido asiático - Adultos)"
                ,"Phyllocoptruta oleivora (Arador de los cítricos)"
                ,"Metopolophium dirhodum (Pulgón amarillo)"
                ,"Conotrachelus aguacatae (Barrenador del hueso)"
                ,"Coptorus aguacatae (Barrenador de las ramas)"
                ,"Scirtothrips persea (Trips)"
                ,"Diaphorina citri (Psílido asiático - Ninfa)"
                ,"Diaphorina citri (Psílido asiático - Adulto)"
                ,"Panonychus citri (Ácaro rojo)"
                ,"Tetranychus urticae (Araña roja)"
                ,"Heliothis zea (Gusano bellotero)"
                ,"Anthonomus grandis (Picudo)"
                ,"Pectinophora gossypiella (Gusano rosado)"
                ,"Anthonomus eugenii (Picudo del chile)"
                ,"Bactericera cockerelli (Paratrioza)"
                ,"Myzus persicae (Pulgón verde)"
                ,"Acrobasis nuxvorella (Barrenador de la nuez)"
                ,"Cydia caryana (Barrenador del ruezno)"
                ,"Monellia caryella (Pulgón amarillo)"
                ,"Scyphophorus acupunctatus (Picudo del agave)"
                ,"Phyllophaga spp. (Gallina ciega)"
                ,"Pseudococcus agavis (Algodoncillo)"
                ,"Rhynchophorus palmarum (Mayate prieto)"
                ,"Rhina barbirostris (Picudo barbón)"
                ,"Eriophyes guerreronis (Ácaro del cocotero)"
                ,"Spodoptera exigua (Gusano soldado)"
                ,"Helicoverpa zea (Gusano bellotero)"
                ,"Cosmopolites sordidus (Picudo del plátano)"
                ,"Frankliniella parvula (Trips)"
                ,"Bemisia tabaci (Mosca blanca)"
                ,"Nezara viridula (Chinche  apestosa)"
                ,"Antigastra sp. (Enrollador)"
                ,"Toxoptera aurantii (Pulgón negro)"
                ,"Xyleborus ferrugineus (Barrenador)"
                ,"Selenothrips rubrocinctus (Trips)"
                ,"Cydia pomonella (Palomilla de la manzana)"
                ,"Eriosoma lanigerum (Pulgón lanígero)"
                ,"Rhagoletis pomonella (Mosca de la manzana)"
                ,"Helicoverpa zea (Gusano del fruto)"
                ,"Cactophagus spinolae (Picudo del nopal)"
                ,"Cylindrocopturus biradiatus (Picudo de las espinas)"
                ,"Dactylopius opuntiae (Cochinilla o grana)"
                ,"Rupella albinella (Palomilla blanca)"
                ,"Sogatodes orizicolus (Sogata del arroz)"
                ,"Oebalus insularis (Chinche café)"
                ,"Trialeurodes vaporariorum"
                ,"Heliothis subflexa (Gusano del fruto)"
                ,"Trichobaris championi (Barrenador del tallo)"
                ,"Brachycorynella asparagi (Pulgón)"
                ,"Diabrótica spp. (Diabrótica)"
                ,"Dikrella cockerelli (Chicharrita)"
                ,"Harrisina spp. (Descarnador de la hoja)"
                ,"Planococcus ficus (Piojo harinoso)"
                ,"Eotetranychus lewisi (Araña roja)"
                ,"Macrodactylus mexicanus (Frailecillo)"
                ,"Scutigerella inmaculata (Sinfílidos)"
                ,"Dysmiccocus neobevipes (Cochinilla)"
                ,"Metamasius dimidiatipennis (Picudo)"
                ,"Agrotis sp. (Gusano trozador)"
                ,"Blapstinus spp. (Cochinilla prieta)"
                ,"Myzus persicae (Pulgón)"
                ,"Aphis gossypii (Pulgón verde)"
                ,"Frankliniella occidentalis (Trips)"
                ,"Conotrachelus psidii (Picudo de la guayaba)"
                ,"Diaphorina citri (Psílido asiático - Huevo)"
                ,"Phyllocnistis citrella (Minador)"
                ,"Aulacapsis tuberculosis"
                ,"prueba"
                ,"Ormenis pulverulenta (Papalota del mango)"
                ,"Nezara viridula"
                ,"Piezedurus guildinii"
                ,"Dichelops furcatus,Anticarsia gemmatalis (Gusano terciopelo)"
                ,"Nezara viridula (Chinche apestosa)"
                ,"Caliothrips phaseoli (Trips negro)"
                ,"Diatraea Saccharalis (General)"
                ,"Spodoptera frugiperda (Gusano cogollero)"
                ,"Helicoverpa zea (Gusano elotero)"
                ,"Diabrotica virgifera (Diabrótica)"
                ,"Frankliniella occidentalis (Adultos)"
                ,"Frankliniella occidentalis (Larvas)"
                ,"Panonychus Ulmi (Formas Moviles)"
                ,"Trichoplusia ni (Gusano falso medidor)"
                ,"Brevicoryne brassicae (Pulgón cenizo)"
                ,"Plutella xylostella (Dorso de diamante)"
                ,"Trips"
                ,"Thaumetopoea pityocampa"
                ,"Thrips tabaci (Trips)"
                ,"Spodoptera exiguab (Gusano soldado)"

        };

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




        adapter = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plantas);
        clt.setAdapter(adapter);

        adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,plagas_total2);

        ArrayAdapter adapter_e = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Etapa);


        etapa1.setAdapter(adapter_e);
        etapa2.setAdapter(adapter_e);
        plg.setAdapter(adapter2);
        plg2.setAdapter(adapter2);



        String [] Otro = {"Otra"};
        String[] Areas = {"Planta completa",
                "Hoja",
                "Foliolo",
                "Fruto",
                "Tallo",
                "Flor",
                "Inflorescencia",
                "Porción de suelo"
        };


        ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Otro);
        area.setAdapter(adapter2);


        ArrayAdapter <String> adapter3 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Areas);
        area.setAdapter(adapter3);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                caja.setChecked(false);

            }
        });

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle miBundle = new Bundle();
                Bundle eBundle = new Bundle();
                dialog.dismiss();

                if (clt.getText().toString().isEmpty() | plg.getText().toString().isEmpty()
                        | plg2.getText().toString().isEmpty() | spinner3.getSelectedItem().toString().isEmpty()
                        | spinner4.getSelectedItem().toString().isEmpty() | etapa1.getSelectedItem().toString().isEmpty() |
                        etapa2.getSelectedItem().toString().isEmpty() | etapa1.getSelectedItem().toString().equals("No seleccionado")
                        | etapa2.getSelectedItem().toString().equals("No seleccionado")){

                    Toast.makeText(PLAGA_ACTIVITY.this,"Campos Vacios o erroneos",Toast.LENGTH_SHORT).show();

                }else {

                    //..  Toast.makeText(getContext(), etapa2.getSelectedItem().toString() +  etapa1.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();

                    eBundle.putInt("Doble_M", check);
                    eBundle.putString("cultivo", clt.getText().toString());
                    eBundle.putString("plaga", plg.getText().toString());
                    //eBundle.putString("cultivo", spinner1.getSelectedItem().toString());
                    eBundle.putString("plaga2", plg2.getText().toString());
                    miBundle.putString("etapa1", etapa1.getSelectedItem().toString());
                    miBundle.putString("etapa2", etapa2.getSelectedItem().toString());
                    miBundle.putString("area", area.getSelectedItem().toString());

                    //Intent i = new Intent(getActivity(), INSTRUCCIONES.class);
                    Intent i = new Intent(PLAGA_ACTIVITY.this, Grafica2.class);
                    i.putExtras(miBundle);
                    i.putExtras(eBundle);
                    startActivity(i);


                }

                caja.setChecked(false);

            }
        });
    }

    public void dialogo (){
        AlertDialog.Builder builder = new AlertDialog.Builder(PLAGA_ACTIVITY.this);
        LayoutInflater inflater = getLayoutInflater();
        View view  = inflater.inflate(R.layout.alert_dialog3,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        String [] plagas_total2 = {
                "Epilachna varibestis (Conchuela)",
                "Diabrótica balteata (Diabrótica)",
                "Liriomyza spp. (Minador)",
                "Melanaphis sacchari (Pulgón amarillo)",
                "Stenodiplosis sorghicola (Mosca midge)",
                "Oebalus mexicana (Chinche café)",
                "Diatrea saccharalis (Barrenador del tallo)",
                "Aeneolamia sp. (Mosca pinta)",
                "Eoreuma loftini (Barrenador mexicano)",
                "Hypotenemus hampei (Broca del café)",
                "Leucoptera coffeella (Minador de la hoja)",
                "Monoflata pallescens (Papalota del café)",
                "Schizaphis graminum (Pulgón verde)",
                "Rhopalosiphum padi (Pulgón del tallo)",
                "Diuraphis noxia (Pulgón ruso)"
                , "Sitobium avenae (Pulgón de la espiga)"
                ,"Epicauta spp. (Escarabajo)"
                ,"Hypera postica (Picudo)"
                ,"Acyrthosiphon pisum (Pulgón verde)"
                ,"Diaphorina citri (Psílido asiático - Huevos)"
                ,"Diaphorina citri (Psílido asiático - Ninfas)"
                ,"Diaphorina citri (Psílido asiático - Adultos)"
                ,"Phyllocoptruta oleivora (Arador de los cítricos)"
                ,"Metopolophium dirhodum (Pulgón amarillo)"
                ,"Conotrachelus aguacatae (Barrenador del hueso)"
                ,"Coptorus aguacatae (Barrenador de las ramas)"
                ,"Scirtothrips persea (Trips)"
                ,"Diaphorina citri (Psílido asiático - Ninfa)"
                ,"Diaphorina citri (Psílido asiático - Adulto)"
                ,"Panonychus citri (Ácaro rojo)"
                ,"Tetranychus urticae (Araña roja)"
                ,"Heliothis zea (Gusano bellotero)"
                ,"Anthonomus grandis (Picudo)"
                ,"Pectinophora gossypiella (Gusano rosado)"
                ,"Anthonomus eugenii (Picudo del chile)"
                ,"Bactericera cockerelli (Paratrioza)"
                ,"Myzus persicae (Pulgón verde)"
                ,"Acrobasis nuxvorella (Barrenador de la nuez)"
                ,"Cydia caryana (Barrenador del ruezno)"
                ,"Monellia caryella (Pulgón amarillo)"
                ,"Scyphophorus acupunctatus (Picudo del agave)"
                ,"Phyllophaga spp. (Gallina ciega)"
                ,"Pseudococcus agavis (Algodoncillo)"
                ,"Rhynchophorus palmarum (Mayate prieto)"
                ,"Rhina barbirostris (Picudo barbón)"
                ,"Eriophyes guerreronis (Ácaro del cocotero)"
                ,"Spodoptera exigua (Gusano soldado)"
                ,"Helicoverpa zea (Gusano bellotero)"
                ,"Cosmopolites sordidus (Picudo del plátano)"
                ,"Frankliniella parvula (Trips)"
                ,"Bemisia tabaci (Mosca blanca)"
                ,"Nezara viridula (Chinche  apestosa)"
                ,"Antigastra sp. (Enrollador)"
                ,"Toxoptera aurantii (Pulgón negro)"
                ,"Xyleborus ferrugineus (Barrenador)"
                ,"Selenothrips rubrocinctus (Trips)"
                ,"Cydia pomonella (Palomilla de la manzana)"
                ,"Eriosoma lanigerum (Pulgón lanígero)"
                ,"Rhagoletis pomonella (Mosca de la manzana)"
                ,"Helicoverpa zea (Gusano del fruto)"
                ,"Cactophagus spinolae (Picudo del nopal)"
                ,"Cylindrocopturus biradiatus (Picudo de las espinas)"
                ,"Dactylopius opuntiae (Cochinilla o grana)"
                ,"Rupella albinella (Palomilla blanca)"
                ,"Sogatodes orizicolus (Sogata del arroz)"
                ,"Oebalus insularis (Chinche café)"
                ,"Trialeurodes vaporariorum"
                ,"Heliothis subflexa (Gusano del fruto)"
                ,"Trichobaris championi (Barrenador del tallo)"
                ,"Brachycorynella asparagi (Pulgón)"
                ,"Diabrótica spp. (Diabrótica)"
                ,"Dikrella cockerelli (Chicharrita)"
                ,"Harrisina spp. (Descarnador de la hoja)"
                ,"Planococcus ficus (Piojo harinoso)"
                ,"Eotetranychus lewisi (Araña roja)"
                ,"Macrodactylus mexicanus (Frailecillo)"
                ,"Scutigerella inmaculata (Sinfílidos)"
                ,"Dysmiccocus neobevipes (Cochinilla)"
                ,"Metamasius dimidiatipennis (Picudo)"
                ,"Agrotis sp. (Gusano trozador)"
                ,"Blapstinus spp. (Cochinilla prieta)"
                ,"Myzus persicae (Pulgón)"
                ,"Aphis gossypii (Pulgón verde)"
                ,"Frankliniella occidentalis (Trips)"
                ,"Conotrachelus psidii (Picudo de la guayaba)"
                ,"Diaphorina citri (Psílido asiático - Huevo)"
                ,"Phyllocnistis citrella (Minador)"
                ,"Aulacapsis tuberculosis"
                ,"prueba"
                ,"Ormenis pulverulenta (Papalota del mango)"
                ,"Nezara viridula"
                ,"Piezedurus guildinii"
                ,"Dichelops furcatus,Anticarsia gemmatalis (Gusano terciopelo)"
                ,"Nezara viridula (Chinche apestosa)"
                ,"Caliothrips phaseoli (Trips negro)"
                ,"Diatraea Saccharalis (General)"
                ,"Spodoptera frugiperda (Gusano cogollero)"
                ,"Helicoverpa zea (Gusano elotero)"
                ,"Diabrotica virgifera (Diabrótica)"
                ,"Frankliniella occidentalis (Adultos)"
                ,"Frankliniella occidentalis (Larvas)"
                ,"Panonychus Ulmi (Formas Moviles)"
                ,"Trichoplusia ni (Gusano falso medidor)"
                ,"Brevicoryne brassicae (Pulgón cenizo)"
                ,"Plutella xylostella (Dorso de diamante)"
                ,"Trips"
                ,"Thaumetopoea pityocampa"
                ,"Thrips tabaci (Trips)"
                ,"Spodoptera exiguab (Gusano soldado)"

        };

        String [] Etapa = {"No seleccionado","Individuos Totales","Huevo","Ninfa/Larva","Formas Móviles","Adulto"};


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


        Button cancelar =view.findViewById(R.id.cancelar);
        Button continuar = view.findViewById(R.id.continuar);
        AutoCompleteTextView clt = view.findViewById(R.id.editcultivo);
        AutoCompleteTextView plg = view.findViewById(R.id.editplaga);
        Spinner Estado = view.findViewById(R.id.estado);
        dialog.setCancelable(false);
        dialog.show();


        adapter = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Plantas);
        clt.setAdapter(adapter);

        adapter2 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,plagas_total2);
        plg.setAdapter(adapter2);

        ArrayAdapter adapter3 = new ArrayAdapter<String>(PLAGA_ACTIVITY.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Etapa);
        Estado.setAdapter(adapter3);



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


                eBundle.putString("cultivo", clt.getText().toString());
                eBundle.putString("plaga", plg.getText().toString());
                miBundle.putString("muestreo", spinner3.getSelectedItem().toString());
                miBundle.putString("area",spinner4.getSelectedItem().toString());
                miBundle.putString("etapa",Estado.getSelectedItem().toString());

                //AREA
                //Intent i = new Intent(getActivity(), INSTRUCCIONES.class);
                Intent i = new Intent(PLAGA_ACTIVITY.this,Grafica.class);
                i.putExtras(miBundle);
                i.putExtras(eBundle);
                startActivity(i);



            }
        });



    }

    private String[] concat(String[] A, String[] B) {
        int aLen = A.length;
        int bLen = B.length;
        String[] C= new String[aLen+bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }

    private  void subir_datos() {
        con = new ConexionSQliteHelper(PLAGA_ACTIVITY.this, "db_Registro", null, 1);
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
}
