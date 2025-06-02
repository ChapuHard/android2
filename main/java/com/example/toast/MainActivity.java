package com.example.toast;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ListView listViewNotas;
    ArrayList<String> titulos;
    ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        listViewNotas = findViewById(R.id.listViewNotas);
        Button btnAgregar = findViewById(R.id.btnAgregar);

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AgregarNotaActivity.class);
            startActivity(intent);
        });

        listViewNotas.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(MainActivity.this, AgregarNotaActivity.class);
            intent.putExtra("id", ids.get(i));
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarNotas();
    }

    private void cargarNotas() {
        Cursor cursor = dbHelper.obtenerNotas();
        titulos = new ArrayList<>();
        ids = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(0)); // id
                titulos.add(cursor.getString(1)); // título
            } while (cursor.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titulos);
        listViewNotas.setAdapter(adapter);
    }
}
