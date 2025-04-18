
package com.techco.Agromuestreo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.techco.Agromuestreo.ConexionSQliteHelper;
import com.techco.Agromuestreo.Detalles;
import com.techco.Agromuestreo.Entidad.registro2;
import com.techco.Agromuestreo.R;
import com.techco.Agromuestreo.Utilidades.Entreno;
import com.techco.Agromuestreo.Utilidades.Utilidades;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Tablero extends AppCompatActivity {

    private TextView userNombre,userEmail,userID,politica;
    private CircleImageView userImg;
    public FirebaseAuth mAuth;
    public Button pasar;
    Entreno con;
    registro2 registro ;
    ArrayList<registro2> ListaRegistro = null;
    public ImageView imll, ime,iml,imp,imt,img;
    public TextView pcll,pce,pcl,pcp,pct,pcg;
    public TextView clll,cle,cll,clp,clt,clg;
    public Double PCLL = 0.0,PCE = 0.0,PCL = 0.0,PCP = 0.0,PCT = 0.0,PCG = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablero);

        pasar = findViewById(R.id.BOTONES);

        imll = findViewById(R.id.imll);
        ime = findViewById(R.id.ime);
        imp = findViewById(R.id.imp);
        iml = findViewById(R.id.iml);
        imt = findViewById(R.id.imt);
        img = findViewById(R.id.img);

        pcll = findViewById(R.id.pcll);
        pce = findViewById(R.id.pce);
        pcp = findViewById(R.id.pcp);
        pcl = findViewById(R.id.pcl);
        pct = findViewById(R.id.pct);
        pcg = findViewById(R.id.pcg);

        clll = findViewById(R.id.clll);
        cle = findViewById(R.id.cle);
        clp = findViewById(R.id.clp);
        cll = findViewById(R.id.cll);
        clt = findViewById(R.id.clt);
        clg = findViewById(R.id.clg);





        userNombre = findViewById(R.id.userNombre);
        userImg = findViewById(R.id.userImagen);
        userID = findViewById(R.id.usarer);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        userNombre.setText(currentUser.getDisplayName());

        userID.setText("Usuario:");
        //setHasOptionsMenu(true);

        //Cargar imagen del usuario
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.userimg)
                .error(R.drawable.userimg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(this).load(currentUser.getPhotoUrl())
                .apply(options)
                .into(userImg);



       /* con = new Entreno(Tablero.this, "my_database", null, 1);
        //, "my_table", null, 1, "my_table", null, 1
        SQLiteDatabase db = con.getReadableDatabase();
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
        ACTUALIZAR();


        pasar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Tablero.this, top_mejores.class);
                startActivity(i);

            }
        });

    }

    public void ACTUALIZAR(){
        try {
           // String[] Hojas = {"Linear lanceolada","Eliptica" , "Lobulada", "Palmeada", "Triangular"};

            consta();

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

            pcll.setText(String.valueOf(PCLL));
            pce.setText(String.valueOf(PCE));
            pcl.setText(String.valueOf(PCL));
            pcp.setText(String.valueOf(PCP));
            pct.setText(String.valueOf(PCT));
            pcg.setText(String.valueOf(PCG));

            imagen(PCLL, imll,clll);
            imagen(PCE, ime,cle);
            imagen(PCL, iml,cll);
            imagen(PCP, imp,clp);
            imagen(PCT, imt,clt);
            imagen(PCG, img,clg);


        }catch (Exception e){

        }
    }

    public void imagen(Double a, ImageView imageView,TextView tex){

        Integer esta = 5;

        if (a <= 0.8){
            esta = 0;
        }else if(a > 0.8 & a <= 0.85){
            esta = 1;
        }else if(a > 0.85 & a <= 0.9){
            esta = 2;
        }else if(a > 0.90 & a <= 0.95){
            esta = 3;
        }else if(a > 0.95){
            esta = 4;
        }

        if (esta == 0){
            imageView.setImageResource(R.drawable.uno);
            tex.setText("SEMILLA\n(APRENDIZ)");

        }else if(esta == 1){
            imageView.setImageResource(R.drawable.dos);
            tex.setText("BROTE\n(PRINCIPIANTE)");

        }else if(esta == 2){
            imageView.setImageResource(R.drawable.tres);
            tex.setText("PLÁNTULA\n(INTERMEDIO)");

        }else if(esta == 3){
            imageView.setImageResource(R.drawable.cuatro);
            tex.setText("ÁRBOL FRONDOSO\n(AVANZADO)");

        }else if(esta == 4){
            imageView.setImageResource(R.drawable.cinco);
            tex.setText("BOSQUE SABIO\n(EXPERTO)");
        }
    }


    public void consta (){
        // Crear una lista para almacenar los registros
        SQLiteDatabase db = new Entreno(this, "my_database", null, 1).getReadableDatabase();
        ListaRegistro = new ArrayList<>();

// Obtener los valores máximos de PC para las hojas especificadas
        String[] Hojas = {"Linear lanceolada", "Eliptica", "Lobulada", "Palmeada", "Triangular"};

            Cursor cursor = db.rawQuery("SELECT " + Entreno.HOJA + ", MAX(" + Entreno.PC + ") FROM " + Entreno.TABLE_NAME + " GROUP BY " + Entreno.HOJA, null);
            while (cursor.moveToNext()) {
                registro2 registro = new registro2();
                registro.setHoja(cursor.getString(0));
                registro.setPC(cursor.getDouble(1));
                ListaRegistro.add(registro);
            }
            cursor.close();
// Cerrar la base de datos
        db.close();
    }
}