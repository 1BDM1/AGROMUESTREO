package com.techco.Agromuestreo.ui.home;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.UI_MODE_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lowagie.text.ElementTags;
import com.techco.Agromuestreo.Adaptador;
import com.techco.Agromuestreo.ConexionSQliteHelper;
import com.techco.Agromuestreo.EnfermedadSe;
import com.techco.Agromuestreo.Entidad.Registro;
import com.techco.Agromuestreo.Grafica;
import com.techco.Agromuestreo.INSTRUCCIONES;
import com.techco.Agromuestreo.MainActivity;
import com.techco.Agromuestreo.R;
import com.techco.Agromuestreo.Utilidades.Utilidades;
import com.techco.Agromuestreo.databinding.FragmentHomeBinding;
import com.techco.Agromuestreo.selhoja;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HomeFragment extends Fragment implements Serializable{
    private HomeViewModel homeViewModel;

private Spinner spinner1,spinner2,spinner3,spinner4;
public CheckBox caja;
public int check = 0;
public ImageView ima;
public String plagas [];
public String [] Plantas;
public Integer a = 0;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    ConexionSQliteHelper con;
    ArrayList<String> ListaRegistro = null;
    ArrayList<String> ListaRegistro2 = null;
    ArrayList<String> ListaRegistro3 = null;
    public String etapa_n = null;
    public int et = 0;

    
Button comenzar;
public String ruta;

    public TextView ver;

    public ImageView duda;
    public Button ENTRENO, MUESTREO;
    private String latitud;
    private String longitud;
    public LocationManager locationManager;
    private Dialog dialog;
    public LocationListener locationListener;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_enfermedad, container, false);



        setHasOptionsMenu(true);

        duda = root.findViewById(R.id.informacionE);
        duda.setVisibility(View.GONE);

        ENTRENO = root.findViewById(R.id.ENTRENAR);
        ENTRENO.setText("PLAGAS");

        MUESTREO = root.findViewById(R.id.MUESTREO);
        MUESTREO.setText("ENFERMEDADES");

        SharedPreferences sp = getActivity().getSharedPreferences("M_P",0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("F_R",true).apply();
        boolean firstrun = sp.getBoolean("F_R",true);


        MUESTREO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //try {

                    Intent i = new Intent(getActivity(), ENFERMEDAD_ACTIVITY.class);
                    startActivity(i);


                //}catch (Exception e){

                  //  Toast.makeText(getActivity(),"Error al iniciar",Toast.LENGTH_SHORT).show();
                //}

            }
        });


        ENTRENO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Intent i = new Intent(getActivity(), PLAGA_ACTIVITY.class);
                    startActivity(i);

                }catch (Exception e){

                    Toast.makeText(getActivity(),"Error al iniciar",Toast.LENGTH_SHORT).show();
                }

            }
        });

        UiModeManager uiModeManager = (UiModeManager) getActivity().getSystemService(UI_MODE_SERVICE);

        if(uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
            // Crear un AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Configurar el título, el mensaje y el botón positivo del diálogo
            builder.setTitle("Modo oscuro activado");
            builder.setMessage("Por favor, desactiva el modo oscuro y vuelve a iniciar la aplicacion");
            builder.setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Finalizar la actividad al hacer clic en el botón
                    getActivity().finish();
                }
            });
            builder.setCancelable(false);
            // Mostrar el diálogo
            builder.show();
        }

        //PERMISO DE ESCRITURA EN LA APLICACION

        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED){



            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},1000);
        }


        // Obtener el LocationManager
         locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

// Verificar si el proveedor de ubicación está habilitado


// Si no está habilitado, mostrar un diálogo
        //  if (!isGpsEnabled && !isNetworkEnabled) {

                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    // Crear un AlertDialog.Builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                            getActivity().finish();

                        }
                    });
                    // Establecer el botón negativo que cierra el diálogo
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cerrar el diálogo
                            dialog.dismiss();
                            getActivity().finish();
                        }
                    });
                    // Mostrar el diálogo
                    builder.create().show();
                    builder.setCancelable(false);
                }else{
                    mostrarCuadroDeDialogo();
                }





        //OBTENER INFORMACION DE LA BASE DE DATOS

        try {
            subir_datos();
        }catch (Exception e){
            Toast.makeText(getContext(),"Bienvenido",Toast.LENGTH_SHORT).show();
        }

        //SUGERENCIAS PARA EL USUARIO

        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
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





        return root;

    }//oncreate

    private void mostrarCuadroDeDialogo() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.carga_inicial);
        dialog.setCancelable(false); // Evitar que el usuario cierre el cuadro manualmente

        Window window =dialog.getWindow();
        if (window != null){
            //window.setLayout(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,2000);
            window.setWindowAnimations(androidx.appcompat.R.style.Animation_AppCompat_DropDownUp);
        }
        // Validar ubicación y cerrar el cuadro de diálogo
        if (latitud == null && longitud == null) {
            //Toast.makeText(MainActivity.this, "Ubicación obtenida", Toast.LENGTH_SHORT).show();

            dialog.show();
            obtenerUbicacion();

        }
        // Mostrar el cuadro de diálogo

    }

    private  void subir_datos() {
        con = new ConexionSQliteHelper(getContext(), "db_Registro", null, 1);
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

    @SuppressLint("MissingPermission")
    private void obtenerUbicacion() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        latitud = String.valueOf(location.getLatitude());
                        longitud = String.valueOf(location.getLongitude());

                        // Validar y navegar a la siguiente actividad
                        if (latitud != null && longitud != null) {

                            dialog.dismiss();
                            locationManager.removeUpdates(this);
                        }
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







}//FIN DE LA CLASE

