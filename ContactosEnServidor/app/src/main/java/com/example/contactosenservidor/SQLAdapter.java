package com.example.contactosenservidor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class SQLAdapter extends BaseAdapter {

    Context contexto;
    List<contacto> valores = new ArrayList<>();
    SQLiteContactos sql;
    SQLiteDatabase db;

    public SQLAdapter(Context con){

        contexto = con;
        sql = new SQLiteContactos(contexto, "BBDDagenda", null, 1);
        db = sql.getWritableDatabase();

        recuperaDatos();
    }

    public int getCount()
    {
        return valores.size();
    }

    public Object getItem (int position){
        return valores.get(position);
    }

    public long getItemId (int position) { return position; }

    // MÉTODO ADAPTER que crea la vista personalizada de los Items del Spinner
    public View getView(int position, View convertView, ViewGroup parent){
        // posicion == id del elemento
        // convertView == vista que quiero utilizar como plantilla
        recuperaDatos();
        if (convertView == null){ // Si es la primera vez que va a usar la vista (optimización)

            LayoutInflater inflado = LayoutInflater.from(contexto);
            convertView = inflado.inflate(R.layout.plantilla_agenda, null); //se le pasa un entero
        }
        TextView tvNombre = (TextView) convertView.findViewById(R.id.nombre);
        tvNombre.setText(valores.get(position).nombre);

        ImageView foto2 = (ImageView) convertView.findViewById(R.id.face);


        //foto2.setBackgroundResource(valores.get(position).foto);
        foto2.setBackgroundResource(valores.get(position).foto);
        return convertView;
    }


    /* MÉTODO que lanza la consulta SQL y guarda los datos en nuevos objetos 'contacto' con los
        que llenamos el array 'valores':   */
    public void recuperaDatos() {

        valores.clear(); //Limpiamos la lista para sacar de 0 los datos
        Cursor c = db.rawQuery("SELECT * FROM contactos", null);

        String nom, tel, gmail = "";
        byte[] foto;
        int photo, contacto_id = 0;

        if (c.moveToFirst()) {

            do {

                contacto_id = c.getInt(0);
                nom = c.getString(1);
                tel = c.getString(2);
                gmail = c.getString(3);
                foto = c.getBlob(4);

                if (foto != null) //Si no se le ha puesto foto, le añado una por defecto
                    photo = new BigInteger(foto).intValue();
                else
                    photo = R.drawable.unknow;
                valores.add(new contacto( contacto_id, nom, tel, gmail, photo));


            } while (c.moveToNext());
        }
    }

    // MÉTODO que devuelve la List local de contactos con los datos extraidos de la B.D. por 'recuperaDatos()':
    public List<contacto> getValores(){ return valores; }


}

