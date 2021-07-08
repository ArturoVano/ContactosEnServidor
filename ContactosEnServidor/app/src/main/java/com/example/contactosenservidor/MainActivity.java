package com.example.contactosenservidor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.StrictMode;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<contacto> agenda = new ArrayList<>();
    Intent intent;
    Spinner spin;
    SQLAdapter adaptador;

    InputStream miBuffer;
    String listaContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        otroHilo();
        //Ahora mi var listaContactos tiene la info. extraida de mi servidor por el hilo
        // secundario, y leída en readStream

        JSONObject documento;
        JSONArray contactosArray;

        int idContacto;
        String nombre;
        String telefono;
        String email;

        try{

            if (listaContactos == null) {
                Thread.sleep(1000);

            }
            //Creo un JSONObject con el String que contiene el json
            documento = new JSONObject(listaContactos);
            contactosArray = documento.getJSONArray("contactos");

            SQLiteContactos sql = new SQLiteContactos(this, "BBDDagenda", null, 1);
            SQLiteDatabase db = sql.getWritableDatabase();


            //Bucle para sacar cada objeto del documento contactos:
            for (int i = 0; i < contactosArray.length(); i++) {

                idContacto = contactosArray.getJSONObject(i).getInt("contacto_id");
                nombre = contactosArray.getJSONObject(i).getString("nombre");
                telefono = contactosArray.getJSONObject(i).getString("telefono");
                email = contactosArray.getJSONObject(i).getString("email");

                db.execSQL("UPDATE contactos SET nombre = ?, telefono=?, gmail=? WHERE contacto_id=?",
                        new String [] {String.valueOf(nombre), String.valueOf(telefono),
                                String.valueOf(email), String.valueOf(idContacto)});

            }
        }
        catch(JSONException je){
            Toast.makeText(this,"JSONExcepcion: "+ je.getMessage(), Toast.LENGTH_LONG).show();

        }
        catch(InterruptedException ie) {
            Toast.makeText(this,"InterruptedExcepcion: "+ ie.getMessage(), Toast.LENGTH_LONG).show();
        }


        // Casteo el objeto spinner de activity_main:
        spin = (Spinner) (findViewById(R.id.spinner));

        adaptador = new SQLAdapter(this);

        // La Lista de contactos local será la misma que la recogida de la B.D.
        agenda = adaptador.getValores();

        // Aplico el adaptador personalizado al Spinner
        spin.setAdapter(adaptador);

        intent = new Intent(this, SecondaryActivity.class);

        // Cuando hagamos click en un Item del Spinner, enviamos los datos con el intent hacia el ActivitySecundario:
        accionesSpinner();

    }


    public void accionesSpinner()
    {
        //Actualizo agenda:
        adaptador.recuperaDatos();
        agenda = adaptador.getValores();

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                for ( int i = 0; i < agenda.size(); i++)

                    if (pos == i) {
                        //POS es de 0 - 2, num_contacto es de 1 - 3:
                        int idContacto = (i+1);
                        Log.e("ID a enviar", Integer.toString(idContacto));
                        intent.putExtra("NUM_CONTACTO",idContacto);

                        String telefono = agenda.get(pos).telefono;
                        intent.putExtra("TELEFONO", telefono);
                        String nombre = agenda.get(pos).nombre;
                        intent.putExtra("NOMBRE", nombre);
                        String gmail = agenda.get(pos).gmail;
                        intent.putExtra("GMAIL", gmail);
                        intent.putExtra("FOTO", agenda.get(pos).foto);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void abrirSecundario(View vista){

        startActivityForResult(intent,1);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle the logic for the requestCode, resultCode and data returned...
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){           //Recivo resultado de el intent mostrar/modificar
            if (resultCode == RESULT_OK){

                //Creo el adaptador de nuevo, para que este refresque los datos:
                adaptador = new SQLAdapter(this);
                adaptador.recuperaDatos();
                spin.setAdapter(adaptador);
                agenda = adaptador.getValores();

            }
        }
        else if (requestCode == 2){      //Recivo resultado de el intent agregar nuevo contacto
            if (resultCode == RESULT_OK){

                //Creo el adaptador de nuevo, para que este refresque los datos:
                adaptador = new SQLAdapter(this);
                adaptador.recuperaDatos();
                spin.setAdapter(adaptador);
                agenda = adaptador.getValores();
            }
        }


        Log.e(Integer.toString(requestCode), "Ha terminado");
    }

    //Abrir el Activity que permite añadir nuevos contactos:
    public void Añadir(View vista){

        Intent intentAdd = new Intent(this, TertiaryActivity.class);
        startActivityForResult(intentAdd,2);
    }

    //He tenido que mirar el reemplazo de las dos formas que Paco Navarro nos mostró, pues en la API 30 me daban problemas:
    //Un hilo secundario abrirá la conexión y la convertira en un Buffer:
    public void otroHilo(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    URL url = new URL("http://10.0.2.2/pmulContactos/contactosParaJson.php");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    //Lo convierto en un Buffer de entrada y lo leo como un fichero normal:
                    miBuffer = con.getInputStream();

                    readStream(miBuffer);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }).start();
    }



    //MÉTODO que lee el buffer de la conexión http y lo almacena en un String:
    private void readStream(InputStream in)
    {
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(in));
            String cadena="";
            String line = "";

            while ((line = reader.readLine()) != null)
            {
                cadena+=line;
            }
            listaContactos = cadena;
        }
        catch (IOException e) {e.printStackTrace();}
        finally
        {
            if (reader != null)
            {
                try {reader.close();}
                catch (IOException e) {e.printStackTrace();}
            }
        }
    }
}