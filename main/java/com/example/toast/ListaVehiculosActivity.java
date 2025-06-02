package com.example.toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Activity para mostrar la lista de todos los vehículos registrados
 * Implementa las operaciones READ y DELETE del CRUD
 */
public class ListaVehiculosActivity extends AppCompatActivity implements VehiculoAdapter.OnVehiculoListener {

    // Declaración de componentes UI
    private RecyclerView recyclerViewVehiculos;
    private TextView tvMensajeVacio;
    private FloatingActionButton fabAgregar;

    // Adaptador y lista de datos
    private VehiculoAdapter vehiculoAdapter;
    private List<Vehiculo> listaVehiculos;

    // Base de datos helper
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vehiculos);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);

        // Inicializar componentes
        inicializarComponentes();

        // Configurar RecyclerView
        configurarRecyclerView();

        // Cargar datos
        cargarVehiculos();

        // Configurar FAB
        configurarFAB();
    }

    /**
     * Método para inicializar los componentes de la interfaz
     */
    private void inicializarComponentes() {
        recyclerViewVehiculos = findViewById(R.id.recyclerViewVehiculos);
        tvMensajeVacio = findViewById(R.id.tvMensajeVacio);
        fabAgregar = findViewById(R.id.fabAgregar);
    }

    /**
     * Método para configurar el RecyclerView
     */
    private void configurarRecyclerView() {
        recyclerViewVehiculos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVehiculos.setHasFixedSize(true);
    }

    /**
     * Método para cargar todos los vehículos desde la base de datos
     */
    private void cargarVehiculos() {
        listaVehiculos = databaseHelper.obtenerTodosLosVehiculos();

        if (listaVehiculos.isEmpty()) {
            // Mostrar mensaje cuando no hay datos
            recyclerViewVehiculos.setVisibility(View.GONE);
            tvMensajeVacio.setVisibility(View.VISIBLE);
        } else {
            // Mostrar lista de vehículos
            recyclerViewVehiculos.setVisibility(View.VISIBLE);
            tvMensajeVacio.setVisibility(View.GONE);

            vehiculoAdapter = new VehiculoAdapter(listaVehiculos, this);
            recyclerViewVehiculos.setAdapter(vehiculoAdapter);
        }
    }

    /**
     * Método para configurar el Floating Action Button
     */
    private void configurarFAB() {
        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaVehiculosActivity.this, AgregarVehiculoActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Método del interface OnVehiculoListener - Click en item
     */
    @Override
    public void onVehiculoClick(int position) {
        Vehiculo vehiculo = listaVehiculos.get(position);
        mostrarDetallesVehiculo(vehiculo);
    }

    /**
     * Método del interface OnVehiculoListener - Click en botón editar
     */
    @Override
    public void onEditarClick(int position) {
        Vehiculo vehiculo = listaVehiculos.get(position);
        Intent intent = new Intent(this, EditarVehiculoActivity.class);
        intent.putExtra("vehiculo_id", vehiculo.getId());
        startActivity(intent);
    }

    /**
     * Método del interface OnVehiculoListener - Click en botón eliminar
     */
    @Override
    public void onEliminarClick(int position) {
        Vehiculo vehiculo = listaVehiculos.get(position);
        mostrarDialogoEliminar(vehiculo, position);
    }

    /**
     * Método para mostrar detalles de un vehículo
     */
    private void mostrarDetallesVehiculo(Vehiculo vehiculo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Detalles del Vehículo");

        String detalles = "Placa: " + vehiculo.getPlaca() + "\n" +
                "Propietario: " + vehiculo.getPropietario() + "\n" +
                "Teléfono: " + (vehiculo.getTelefono().isEmpty() ? "No registrado" : vehiculo.getTelefono()) + "\n" +
                "Tipo: " + vehiculo.getTipoVehiculo() + "\n" +
                "Fecha de ingreso: " + vehiculo.getFechaIngreso() + "\n" +
                "Hora de ingreso: " + vehiculo.getHoraIngreso();

        builder.setMessage(detalles);
        builder.setPositiveButton("Cerrar", null);
        builder.show();
    }

    /**
     * Método para mostrar diálogo de confirmación para eliminar
     */
    private void mostrarDialogoEliminar(Vehiculo vehiculo, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Está seguro de que desea eliminar el vehículo con placa " + vehiculo.getPlaca() + "?");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarVehiculo(vehiculo.getId(), position);
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    /**
     * Método para eliminar un vehículo de la base de datos
     */
    private void eliminarVehiculo(int vehiculoId, int position) {
        int filasEliminadas = databaseHelper.eliminarVehiculo(vehiculoId);

        if (filasEliminadas > 0) {
            // Eliminar de la lista y notificar al adaptador
            listaVehiculos.remove(position);
            vehiculoAdapter.notifyItemRemoved(position);
            vehiculoAdapter.notifyItemRangeChanged(position, listaVehiculos.size());

            Toast.makeText(this, "Vehículo eliminado correctamente", Toast.LENGTH_SHORT).show();

            // Verificar si la lista quedó vacía
            if (listaVehiculos.isEmpty()) {
                recyclerViewVehiculos.setVisibility(View.GONE);
                tvMensajeVacio.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(this, "Error al eliminar el vehículo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar datos cuando regresamos a esta actividad
        cargarVehiculos();
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