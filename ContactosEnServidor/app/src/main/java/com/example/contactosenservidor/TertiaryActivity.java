package com.example.contactosenservidor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;

public class TertiaryActivity extends AppCompatActivity {

    SQLiteContactos sql;
    SQLiteDatabase db;

    EditText ETName;
    EditText ETTelefono;
    EditText ETMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tertiary);

        //Obtengo el intent (vacío) que inició este Activity
        Intent empthyIntent = getIntent();


        ETName = (EditText) findViewById(R.id.ETName);
        ETTelefono = (EditText) findViewById(R.id.ETTelefono);
        ETMail = (EditText) findViewById(R.id.ETMail);

    }

    public void cxlOperation(View v){

        this.finish();
    }

    public void saveContact(View v){

        String nombre = ETName.getText().toString();
        String telefono = ETTelefono.getText().toString();
        String mail = ETMail.getText().toString();
        byte[] img = null; //De momento, que el adapter ponga la foto por defecto

        //Recogo la instancia de la BD que ya creo SQLiteContactos:
        SQLiteContactos sql = new SQLiteContactos(this, "BBDDagenda", null, 1);
        db = sql.getWritableDatabase();

        //Busco el último id...
        Cursor c = db.rawQuery("SELECT * FROM contactos ORDER BY contacto_id DESC LIMIT 1;"
                , null);

        if (c.moveToFirst()) {
            //... y le añado uno para que este disponible para el nuevo contacto
            int contactoId = (c.getInt(0) + 1);

            ContentValues valores = new ContentValues();
            valores.put("contacto_id", contactoId);
            valores.put("nombre", nombre);
            valores.put("telefono", telefono);
            valores.put("gmail", mail);
            valores.put("foto", img);
            db.insert("contactos", null, valores);

            setResult(RESULT_OK);
            this.finish();
        }
        else{
            Toast.makeText(this, "La consulta del último Id ha fallado", Toast.LENGTH_SHORT);
        }
    }
}