package com.example.bdatos_alvaroleiva;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText met_id, met_nombre, met_raza, met_edad;
    private ListView mListView;
    private List<String> mLista = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        met_id = (EditText) findViewById(R.id.cet_id);
        met_nombre = (EditText) findViewById(R.id.cet_nombre);
        met_raza = (EditText) findViewById(R.id.cet_raza);
        met_edad = (EditText) findViewById(R.id.cet_edad);
        mListView = findViewById(R.id.listView);

        listar();
    }

    public void guardar(View view) {
        String id = met_id.getText().toString();
        String nombre = met_nombre.getText().toString();
        String raza = met_raza.getText().toString();
        String edad = met_edad.getText().toString();

        if(!id.isEmpty() || !nombre.isEmpty() || !raza.isEmpty() || !edad.isEmpty()) {
            ContentValues tupla = new ContentValues();
            tupla.put("id", id);
            tupla.put("nombre", nombre);
            tupla.put("raza", raza);
            tupla.put("edad", edad);

            SQLite x = new
                    SQLite( this, "BD_Perros", null, 1);
            SQLiteDatabase bd = x.getWritableDatabase();

            bd.insert("perros", null, tupla);

            Toast.makeText( this, R.string.storedDog, Toast.LENGTH_SHORT).show();
            met_id.setText("");
            met_nombre.setText("");
            met_raza.setText("");
            met_edad.setText("");
        } else {
            Toast.makeText(this, R.string.emptyData, Toast.LENGTH_SHORT).show();
        }

        listar();
    }

    public void buscar(View view) {
        String id = met_id.getText().toString();

        if (!id.isEmpty()) {
            SQLite x = new
                    SQLite( this, "BD_Perros", null, 1);
            SQLiteDatabase bd = x.getWritableDatabase();

            Cursor tuplas =
                    bd.rawQuery("Select * from perros where id = " + id, null);

            if(tuplas.moveToFirst()) {
                met_nombre.setText(tuplas.getString(1));
                met_raza.setText(tuplas.getString(2));
                met_edad.setText(tuplas.getString(3));

                Toast.makeText(this, R.string.recoveredDog, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText( this, R.string.dogNotFound, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.enterId, Toast.LENGTH_SHORT).show();
        }

        listar();
    }

    public void borrar(View view) {
        String id = met_id.getText().toString();

        if(!id.isEmpty()) {
            SQLite x = new
                    SQLite( this, "BD_Perros", null, 1);
            SQLiteDatabase bd = x.getWritableDatabase();

            int borrados = bd.delete("perros", "id = " + id, null);

            if(borrados!=0) {
                Toast.makeText(this, R.string.deletedDog, Toast.LENGTH_SHORT).show();
                met_id.setText("");
                met_nombre.setText("");
                met_raza.setText("");
                met_edad.setText("");
            }
            else {
                Toast.makeText( this, R.string.dogNotFound, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText( this, R.string.enterId, Toast.LENGTH_SHORT).show();
        }

        listar();
    }

    public void modificar(View view) {
        String id = met_id.getText().toString();
        String nombre = met_nombre.getText().toString();
        String raza = met_raza.getText().toString();
        String edad = met_edad.getText().toString();

        if (!id.isEmpty() || !nombre.isEmpty() || !raza.isEmpty() || !edad.isEmpty()) {
            ContentValues tupla = new ContentValues();
            tupla.put("id", id);
            tupla.put("nombre", nombre);
            tupla.put("raza", raza);
            tupla.put("edad", edad);

            SQLite x = new
                    SQLite( this, "BD_Perros", null, 1);
            SQLiteDatabase bd = x.getWritableDatabase();

            int modificados =
                    bd.update("perros", tupla, "id = " + id, null);

            if(modificados!=0) {
                Toast.makeText(this, R.string.updatedDog, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText( this, R.string.dogDoesNotExist, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.emptyData, Toast.LENGTH_SHORT).show();
        }

        listar();
    }

    public void listar() {
        int numPerros = 0;
        String texto = "";

        mLista.clear();

        SQLite x = new
                SQLite( this, "BD_Perros", null, 1);
        SQLiteDatabase bd = x.getWritableDatabase();

        Cursor tuplas =
                bd.rawQuery("Select * from perros", null);

        Cursor count = bd.rawQuery("Select count(*) from perros", null);

        if (count.moveToFirst()) {
            numPerros = Integer.parseInt(count.getString(0));
        }

            for (int i = 0; i <= numPerros; i++) {
                if(tuplas.moveToPosition(i)) {
                    texto = tuplas.getString(0) +
                            ", " + tuplas.getString(1) +
                            ", " + tuplas.getString(2) +
                            ", " + tuplas.getString(3) ;
                    mLista.add(texto);
                }
            }
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mLista);
        mListView.setAdapter(mAdapter);
    }
}