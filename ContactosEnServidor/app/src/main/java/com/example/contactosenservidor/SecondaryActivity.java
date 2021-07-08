package com.example.contactosenservidor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SecondaryActivity extends AppCompatActivity {

    LinearLayout layout;
    public int numContacto;
    public String nombre;
    public String telefono;
    public String gmail;
    private int muestraMod = 0; //Para mostrar/ocultar los elementos para modificar
    EditText nombreET ;
    EditText telefonoET ;
    EditText mailET ;
    Button guardar;
    Intent intencion;

    SQLiteContactos sql;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        SQLiteContactos sql = new SQLiteContactos(this, "BBDDagenda", null, 1);
        db = sql.getWritableDatabase();

        //Obtengo el Intent que inició este Activity
        intencion = getIntent();


        //Extraigo los datos del Intent que inició este Activity
        numContacto = intencion.getIntExtra("NUM_CONTACTO", -10);
        telefono = intencion.getStringExtra("TELEFONO");
        nombre = intencion.getStringExtra("NOMBRE");
        gmail = intencion.getStringExtra("GMAIL");

        //Instancio elementos para corresponderlos con las vistas del xml secundario y usarlas desde aquí
        ImageView foto = (ImageView) findViewById(R.id.foto);
        TextView tNombre = (TextView) findViewById(R.id.nombre);
        TextView tTelefono = (TextView) findViewById(R.id.telefono);
        TextView tGmain = (TextView) findViewById(R.id.gmail);

        //Muestro los datos mencionados
        foto.setBackgroundResource(intencion.getIntExtra("FOTO", 0));
        tNombre.setText("NOMBRE: "+ nombre);
        tTelefono.setText("TELEFONO: "+ telefono);
        tGmain.setText("GMAIL: "+ gmail);

        guardar = (Button)findViewById(R.id.guardaCambios);
        guardar.setVisibility(View.GONE);


    }

    public void onClick(View view){
        this.finish();
    }


    //Botón para mostrar los EditText para modificar los valores del contacto:
    public void modificaciones(View vista) {

        guardar.setVisibility(View.VISIBLE);
        layout = (LinearLayout)findViewById(R.id.layoutSecundario);

        nombreET = new EditText(this);
        telefonoET = new EditText(this);
        mailET = new EditText(this);

        nombreET.setText(nombre);
        telefonoET.setText(telefono);
        mailET.setText(gmail);


        if (muestraMod == 0){       //Si es la primera vez que se va a usar
            layout.addView(nombreET);
            layout.addView(telefonoET);
            layout.addView(mailET);
            muestraMod++;
        }

        Button btn = (Button) new Button(this);
        btn.setText("Guardar");
    }


    public void guardarCambios(View v){

        if (numContacto > 0){

            nombre = nombreET.getText().toString();
            telefono = telefonoET.getText().toString();
            gmail = mailET.getText().toString();

            String sql = " UPDATE contactos SET nombre = '"+ nombre + "' WHERE contacto_id = "+ numContacto+ ";";
            db.execSQL(sql);

            sql = " UPDATE contactos SET telefono = '"+ telefono + "' WHERE contacto_id = "+ numContacto+ ";";
            db.execSQL(sql);

            sql = " UPDATE contactos SET gmail = '"+ gmail + "' WHERE contacto_id = "+ numContacto+ ";";
            db.execSQL(sql);


            //Devuelvo un resultado a MainActivity:
            setResult(RESULT_OK);

            this.finish();
        }
    }
}
