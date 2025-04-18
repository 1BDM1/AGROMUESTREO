package com.techco.Agromuestreo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.techco.Agromuestreo.Utilidades.Utilidades;

//import Utlilidades.Utilidades;

public class ConexionSQliteHelper extends SQLiteOpenHelper {


    public ConexionSQliteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
db.execSQL(Utilidades.CREAR_TABLA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int VersionAnterior, int VersionNueva) {
db.execSQL("DROP TABLE IF EXISTS Registro");
onCreate(db);
    }
}
