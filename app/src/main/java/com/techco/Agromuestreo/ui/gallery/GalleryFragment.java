package com.techco.Agromuestreo.ui.gallery;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.techco.Agromuestreo.Adaptador;
import com.techco.Agromuestreo.Adaptador2;
import com.techco.Agromuestreo.Adaptadorh;
import com.techco.Agromuestreo.ConexionSQliteHelper;
import com.techco.Agromuestreo.Detalles;
import com.techco.Agromuestreo.Entidad.Registro;
import com.techco.Agromuestreo.R;
import com.techco.Agromuestreo.Utilidades.Utilidades;
import com.techco.Agromuestreo.databinding.FragmentGalleryBinding;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

//VISUALIZAR
    ConexionSQliteHelper con;
    public ListView lista;
    public Double a,b;
    public Integer pasar;
    ArrayList<Registro> ListaRegistro = null;
    public TextView cultivo,plaga;
    Button Detalles;
    public TextView texto;
    Adaptadorh adaptadorh;
    Registro registro ;
    public  Button generar;
    public String fecha_selec,varia;


    private ArrayList<String> ListaPreguntas;
    private Map<String,ArrayList<String>> mapChild;
    private ExpandableListView expLV;

    public  int p = 0 ;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
            galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        setHasOptionsMenu(true);

        varia = "PLAGA";
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        con = new ConexionSQliteHelper(getActivity(), "db_Registro", null, 1);

        expLV = root.findViewById(R.id.Lista);
        SQLiteDatabase db = con.getReadableDatabase();
        Detalles = root.findViewById(R.id.button);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(1900, Calendar.JANUARY, 1); // Ajusta el año, mes y día
        fecha_selec = dateFormat.format(calendar.getTime());


        // Registro registro ;
        ListaRegistro = new ArrayList<>();

        ListaPreguntas = new ArrayList<>();
        mapChild = new HashMap<>();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_REGISTRO + " WHERE " + Utilidades.CAMPO_ETAPA + " <> ? ",new String[]{"Enfermedad"});

        while (cursor.moveToNext()){

            registro = new Registro();
            registro.setCultivo(cursor.getString(0));
            registro.setPlaga(cursor.getString(1));
            registro.setFecha(cursor.getString(2));
            registro.setDensidad(cursor.getDouble(3));
            registro.setLatitud(cursor.getString(4));
            registro.setLonguitud(cursor.getString(5));
            registro.setArea(cursor.getString(6));
            registro.setPRECISION(cursor.getDouble(7));
            registro.setCapturas(cursor.getString(8));
            registro.setTIPO(cursor.getString(9));
            registro.setETAPA(cursor.getString(10));

            ListaRegistro.add(registro);
        }
        cursor.close();
        db.close();


        /*lista.setAdapter(new Adaptador(getActivity(),ListaRegistro));
       if (ListaRegistro != null){
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // Toast.makeText(getActivity(),"Usted salio de aqui"+ " " +p,Toast.LENGTH_SHORT).show();
                    if (p == 0) {
                        lista.setAdapter(new Adaptador2(ListaRegistro, getActivity()));
                    }if(p == 1) {
                        lista.setAdapter(new Adaptador(getActivity(),ListaRegistro));
                    }
                    if(p == 1){
                        p = 0;
                    }else{p = 1;}


                }
            });

        }*/

        Adaptador();

        Detalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), com.techco.Agromuestreo.Detalles.class);
                startActivity(i);

            }
        });
        return root;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        /*switch (item.getItemId()) {
            case R.id.action_setting:
                //metodoAdd()
                Toast.makeText(getContext(), "Archivos", Toast.LENGTH_SHORT).show();

                openFolder();
                return true;

            case R.id.action_calendar:

                showDatePickerDialog();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/

        if (itemId == R.id.action_setting) {
            // Método para manejar la acción de "Archivos"
            //Toast.makeText(getContext(), "Archivos", Toast.LENGTH_SHORT).show();
            openFolder();
            return true;
        } else if (itemId == R.id.action_calendar) {
            // Método para manejar la acción de "Calendario"
            showDatePickerDialog();
            return true;
        } else if (itemId == R.id.cambiar) {

            if (varia.equals("Enfermedad")){
                varia = "PLAGA";
            }else{
                varia = "Enfermedad";
            }
            BASEDB();

            return true;
        } else {
            // Manejo para otros casos (si es necesario)
            return super.onOptionsItemSelected(item);
        }

    }


    public void openFolder(){
        //
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(//Environment.getExternalStorageDirectory().getPath()
                //0+ "/Agromuestreo/");7
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/Agromuestreo");

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
        //Intent.createChooser(intent, "Open");
    }






    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "-" + (month + 1) + "-" + year;
                // etPlannedDate.setText(selectedDate);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month  , day);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                fecha_selec = dateFormat.format(calendar.getTime());
               //
                //
                //
                // Toast.makeText(getContext(),fecha_selec,Toast.LENGTH_SHORT).show();
                BASEDB();


            }
        });
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public  void Adaptador(){
        ArrayList<String> listp1 = new ArrayList<>();

        for (int i=0; i< ListaRegistro.size();i++){

            listp1.add(ListaRegistro.get(i).getFecha());
            mapChild.put(ListaRegistro.get(i).getFecha(),listp1);
        }

        adaptadorh = new Adaptadorh(getContext(),ListaRegistro,mapChild);
        expLV.setAdapter(adaptadorh);

    }


@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void Parametros(String pl, String cu){
        if (pl.equals("Mango (Ataufo)") && cu.equals("Aulacapsis tuberculosis")){
            a =1.4783;
            b = 1.1433;
            pasar = 1;
        }
        if (pl.equals("Soya") && cu.equals("Nezara viridula")){
            a =1.6;	b=1.1;pasar = 1;

        }
        if (pl.equals("Soya") && cu.equals("Piezedurus guildinii")){
            a =1.05; 	b =1.01;pasar = 1;

        }if (pl.equals("Soya") && cu.equals("Dichelops furcatus")){
            a = 1.03;	b = 0.99;pasar = 1;

        }if (pl.equals("Maiz") && cu.equals("Diatraea Saccharalis (General)")){//ESTA NO//
            a =1.476980794;	b = 1.27;pasar = 1;

        }if (pl.equals("Clavel") && cu.equals("Frankliniella occidentalis (Adultos)")){//ESTA NO//
            a = 1.596;	b = 1.391;pasar = 1;

        }if (pl.equals("Frutilla Invernadero") && cu.equals("Frankliniella occidentalis (Larvas)")){ //ESTA NO//
            a = 2.169;	b = 1.553;pasar = 1;

        }if (pl.equals("Pimiento Invernadero") && cu.equals("Frankliniella occidentalis (Larvas)")){//esta no//
            a = 3.532;	b = 1.5192;pasar = 1;

        }
        if (pl.equals("Brocoli") && cu.equals("Plutella xylostella (Etapa Fenologica Tardia)")){
            a = 2.181472265;	b = 1.47;pasar = 1;
        }
        if (pl.equals("Brocoli") && cu.equals("Plutella xylostella (Etapa Fenologica Temprana)")){
            a = 1.097940385;	b = 1.37;pasar = 1;

        }
        if (pl.equals("Papa") && cu.equals("Thrips Palmi (Larvas)")){
            a = 2.32;	b = 2.1;pasar = 1;

        }
        if (pl.equals("Papa") && cu.equals("Thrips Palmi (Adultos)")){
            a = 2.49;	b = 1.45;pasar = 1;

        }
        if (pl.equals("Tomate") && cu.equals("Trialeurodes vaporariorum")){
            a = 2.75;	b = 1.82;pasar = 1;
        }
        if (pl.equals("cebolla") && cu.equals("Thrips tabaci")){
            a = 2.586;	b = 1.511;pasar = 1;
        }
        if (pl.equals("Manzano") && cu.equals("Panonychus Ulmi (Formas Moviles)")){
            a = 2.38;	b = 1.39;pasar = 1;
        }


    }

public void BASEDB() {
    if (varia.equals("Enfermedad")){
    con = new ConexionSQliteHelper(getContext(), "db_Registro", null, 1);
    SQLiteDatabase db = con.getReadableDatabase();
    db.enableWriteAheadLogging();
    //Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_REGISTRO,null);
    Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_REGISTRO + " WHERE " + Utilidades.CAMPO_ETAPA + " = ? " + " and " + Utilidades.CAMPO_FECHA + " > ? ", new String[]{"Enfermedad",fecha_selec});
    ListaRegistro = new ArrayList<>();

    while (cursor.moveToNext()) {

        registro = new Registro();
        registro.setCultivo(cursor.getString(0));
        registro.setPlaga(cursor.getString(1));
        registro.setFecha(cursor.getString(2));
        registro.setLatitud(cursor.getString(4));
        registro.setLonguitud(cursor.getString(5));
        registro.setArea(cursor.getString(6));
        registro.setDensidad(cursor.getDouble(3));
        registro.setCapturas(cursor.getString(8));
        registro.setTIPO(cursor.getString(9));
        registro.setETAPA(cursor.getString(10));

        ListaRegistro.add(registro);
    }
    cursor.close();
    db.close();

    Adaptador();

}else {
        con = new ConexionSQliteHelper(getContext(), "db_Registro", null, 1);
        SQLiteDatabase db = con.getReadableDatabase();
        db.enableWriteAheadLogging();
        //Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_REGISTRO,null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_REGISTRO + " WHERE " + Utilidades.CAMPO_FECHA + " > ?"  + " and " + Utilidades.CAMPO_ETAPA + " <> ? ", new String[]{fecha_selec,"Enfermedad"});
        ListaRegistro = new ArrayList<>();

        while (cursor.moveToNext()) {

            registro = new Registro();
            registro.setCultivo(cursor.getString(0));
            registro.setPlaga(cursor.getString(1));
            registro.setFecha(cursor.getString(2));
            registro.setDensidad(cursor.getDouble(3));
            registro.setLatitud(cursor.getString(4));
            registro.setLonguitud(cursor.getString(5));
            registro.setArea(cursor.getString(6));
            registro.setETAPA(cursor.getString(10));
            registro.setCapturas(cursor.getString(8));
            registro.setTIPO(cursor.getString(9));

            ListaRegistro.add(registro);
        }
        cursor.close();
        db.close();

        Adaptador();
    }

}


    public void reporte(View view){
        Double prueba = Double.parseDouble(ListaRegistro.get(0).getCapturas());
        Toast.makeText(getContext(),String.valueOf(prueba),Toast.LENGTH_SHORT).show();
    }

    public void pruebas(){
        Toast.makeText(getContext(),"Hola",Toast.LENGTH_SHORT).show();
    }


}