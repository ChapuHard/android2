package com.example.toast;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AgregarNotaActivity extends AppCompatActivity {

    EditText editTitulo, editContenido;
    Button btnGuardar;
    DBHelper dbHelper;
    int notaId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_nota);

        editTitulo = findViewById(R.id.editTitulo);
        editContenido = findViewById(R.id.editContenido);
        btnGuardar = findViewById(R.id.btnGuardar);
        dbHelper = new DBHelper(this);

        if (getIntent().hasExtra("id")) {
            notaId = getIntent().getIntExtra("id", -1);
            cargarNota();
        }

        btnGuardar.setOnClickListener(v -> {
            String titulo = editTitulo.getText().toString();
            String contenido = editContenido.getText().toString();

            boolean resultado;
            if (notaId == -1) {
                resultado = dbHelper.insertarNota(titulo, contenido);
                mensaje(resultado, "creada");
            } else {
                resultado = dbHelper.actualizarNota(notaId, titulo, contenido);
                mensaje(resultado, "actualizada");
            }
        });
    }

    private void cargarNota() {
        Cursor cursor = dbHelper.obtenerNotas();
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == notaId) {
                editTitulo.setText(cursor.getString(1));
                editContenido.setText(cursor.getString(2));
                break;
            }
        }
    }

    private void mensaje(boolean exito, String accion) {
        if (exito) {
            Toast.makeText(this, "Nota " + accion + " correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }
}
