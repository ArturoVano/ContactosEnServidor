package com.example.contactosenservidor;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigInteger;

//Toda clase que extienda de SQLiteOpenHelper deberá disponder de OnCreate y onUpgrade.
public class SQLiteContactos extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE contactos(contacto_id INTEGER , nombre TEXT, telefono TEXT, gmail TEXT, foto BLOB)";
    Context contexto;

    public SQLiteContactos(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version){

        super(contexto, nombre, factory, version);
    }

    public void onCreate(SQLiteDatabase database){

        database.execSQL(sqlCreate);

        if(database != null)
        {

            byte[] img1 = BigInteger.valueOf(R.drawable.uno).toByteArray();
            byte[] img2 = BigInteger.valueOf(R.drawable.jim).toByteArray();
            byte[] img3 = BigInteger.valueOf(R.drawable.hair).toByteArray();

            ContentValues cv = new ContentValues();

            cv.put("contacto_id", 1);
            cv.put("nombre", "Julia");
            cv.put("telefono", "6653454");
            cv.put("gmail", "julia@gmail.com");
            cv.put("foto", img1);
            database.insert("contactos", null, cv);
            cv.put("contacto_id", 2);
            cv.put("nombre", "Jim");
            cv.put("telefono", "6621323");
            cv.put("gmail", "jim@gmail.com");
            cv.put("foto", img2);
            database.insert("contactos", null, cv);
            cv.put("contacto_id", 3);
            cv.put("nombre", "Kevin");
            cv.put("telefono", "6654321");
            cv.put("gmail", "kevin@gmail.com");
            cv.put("foto", img3);
            database.insert("contactos", null, cv);

        }
    }

    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva){

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Contactos");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
    // usado para acutualizar desde MainActivity los contactos leídos de mi servidor web:
    public void actualizaContactos(int id, String n, String t, String m){


    }
}
