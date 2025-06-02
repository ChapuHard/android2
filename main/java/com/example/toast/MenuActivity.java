package com.example.toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Activity principal del menú del sistema de estacionamiento
 * Integra todas las operaciones CRUD con navegación a las respectivas actividades
 */
public class MenuActivity extends AppCompatActivity {

    // Declaración de componentes UI
    private Button btnAgregar, btnMostrar, btnModificar, btnEliminar;

    // Base de datos helper para operaciones adicionales
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);

        // Inicialización de componentes
        btnAgregar = findViewById(R.id.btnAgregar);
        btnMostrar = findViewById(R.id.btnMostrar);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Configuración de eventos onClick para cada botón
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar mensaje Toast para confirmar acción
                Toast.makeText(MenuActivity.this, "Navegando a agregar vehículo", Toast.LENGTH_SHORT).show();

                // Navegación a AgregarVehiculoActivity (CREATE)
                Intent intent = new Intent(MenuActivity.this, AgregarVehiculoActivity.class);
                startActivity(intent);
            }
        });

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar mensaje Toast para confirmar acción
                Toast.makeText(MenuActivity.this, "Mostrando lista de vehículos", Toast.LENGTH_SHORT).show();

                // Navegación a ListaVehiculosActivity (READ)
                Intent intent = new Intent(MenuActivity.this, ListaVehiculosActivity.class);
                startActivity(intent);
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar mensaje Toast según los requerimientos
                Toast.makeText(MenuActivity.this, "Para modificar, seleccione un vehículo de la lista", Toast.LENGTH_SHORT).show();

                // Navegación a ListaVehiculosActivity donde se puede editar (UPDATE)
                Intent intent = new Intent(MenuActivity.this, ListaVehiculosActivity.class);
                startActivity(intent);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar mensaje Toast según los requerimientos
                Toast.makeText(MenuActivity.this, "Para eliminar, seleccione un vehículo de la lista", Toast.LENGTH_SHORT).show();

                // Navegación a ListaVehiculosActivity donde se puede eliminar (DELETE)
                Intent intent = new Intent(MenuActivity.this, ListaVehiculosActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualizar título con cantidad de vehículos registrados
        actualizarTituloConEstadisticas();
    }

    /**
     * Método para actualizar el título de la actividad con estadísticas
     */
    private void actualizarTituloConEstadisticas() {
        int totalVehiculos = databaseHelper.contarVehiculos();
        setTitle("Sistema de Estacionamiento (" + totalVehiculos + " vehículos)");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cerrar conexión a la base de datos
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}