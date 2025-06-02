package com.example.toast;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Activity para editar vehículos existentes en el sistema
 * Implementa la operación UPDATE del CRUD
 */
public class EditarVehiculoActivity extends AppCompatActivity {

    // Declaración de componentes UI
    private TextInputLayout tilPlaca, tilPropietario, tilTelefono;
    private TextInputEditText etPlaca, etPropietario, etTelefono;
    private Spinner spinnerTipoVehiculo;
    private Button btnActualizar, btnCancelar;

    // Base de datos helper y vehículo actual
    private DatabaseHelper databaseHelper;
    private Vehiculo vehiculoActual;
    private int vehiculoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_vehiculo);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);

        // Obtener ID del vehículo desde el intent
        vehiculoId = getIntent().getIntExtra("vehiculo_id", -1);

        if (vehiculoId == -1) {
            Toast.makeText(this, "Error: ID de vehículo no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar componentes UI
        inicializarComponentes();

        // Configurar Spinner
        configurarSpinner();

        // Cargar datos del vehículo
        cargarDatosVehiculo();

        // Configurar eventos de botones
        configurarEventos();
    }

    /**
     * Método para inicializar todos los componentes de la interfaz
     */
    private void inicializarComponentes() {
        tilPlaca = findViewById(R.id.tilPlaca);
        tilPropietario = findViewById(R.id.tilPropietario);
        tilTelefono = findViewById(R.id.tilTelefono);

        etPlaca = findViewById(R.id.etPlaca);
        etPropietario = findViewById(R.id.etPropietario);
        etTelefono = findViewById(R.id.etTelefono);

        spinnerTipoVehiculo = findViewById(R.id.spinnerTipoVehiculo);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnCancelar = findViewById(R.id.btnCancelar);
    }

    /**
     * Método para configurar el Spinner con los tipos de vehículos
     */
    private void configurarSpinner() {
        String[] tiposVehiculos = {"Automóvil", "Motocicleta", "Camioneta", "SUV", "Bicicleta"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tiposVehiculos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTipoVehiculo.setAdapter(adapter);
    }

    /**
     * Método para cargar los datos del vehículo a editar
     */
    private void cargarDatosVehiculo() {
        vehiculoActual = databaseHelper.obtenerVehiculoPorId(vehiculoId);

        if (vehiculoActual != null) {
            // Llenar campos con datos actuales
            etPlaca.setText(vehiculoActual.getPlaca());
            etPropietario.setText(vehiculoActual.getPropietario());
            etTelefono.setText(vehiculoActual.getTelefono());

            // Seleccionar tipo de vehículo en el spinner
            String[] tiposVehiculos = {"Automóvil", "Motocicleta", "Camioneta", "SUV", "Bicicleta"};
            for (int i = 0; i < tiposVehiculos.length; i++) {
                if (tiposVehiculos[i].equals(vehiculoActual.getTipoVehiculo())) {
                    spinnerTipoVehiculo.setSelection(i);
                    break;
                }
            }
        } else {
            Toast.makeText(this, "Error: No se pudo cargar el vehículo", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Método para configurar los eventos de los botones
     */
    private void configurarEventos() {
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarVehiculo();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cerrar sin guardar cambios
            }
        });
    }

    /**
     * Método principal para actualizar el vehículo
     */
    private void actualizarVehiculo() {
        if (validarCampos()) {
            // Obtener datos actualizados
            String placa = etPlaca.getText().toString().trim().toUpperCase();
            String propietario = etPropietario.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String tipoVehiculo = spinnerTipoVehiculo.getSelectedItem().toString();

            // Actualizar objeto vehículo manteniendo fecha y hora originales
            vehiculoActual.setPlaca(placa);
            vehiculoActual.setPropietario(propietario);
            vehiculoActual.setTelefono(telefono);
            vehiculoActual.setTipoVehiculo(tipoVehiculo);

            // Intentar actualizar en la base de datos
            int filasAfectadas = databaseHelper.actualizarVehiculo(vehiculoActual);

            if (filasAfectadas > 0) {
                Toast.makeText(this, "Vehículo actualizado exitosamente", Toast.LENGTH_SHORT).show();
                finish(); // Cerrar actividad después de actualizar
            } else {
                Toast.makeText(this, "Error al actualizar vehículo", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Método para validar todos los campos del formulario
     * @return true si todos los campos son válidos
     */
    private boolean validarCampos() {
        boolean esValido = true;

        // Validar placa
        String placa = etPlaca.getText().toString().trim();
        if (placa.isEmpty()) {
            tilPlaca.setError("La placa es requerida");
            esValido = false;
        } else if (placa.length() < 6) {
            tilPlaca.setError("La placa debe tener al menos 6 caracteres");
            esValido = false;
        } else {
            tilPlaca.setError(null);
        }

        // Validar propietario
        String propietario = etPropietario.getText().toString().trim();
        if (propietario.isEmpty()) {
            tilPropietario.setError("El nombre del propietario es requerido");
            esValido = false;
        } else if (propietario.length() < 2) {
            tilPropietario.setError("El nombre debe tener al menos 2 caracteres");
            esValido = false;
        } else {
            tilPropietario.setError(null);
        }

        // Validar teléfono (opcional pero si se ingresa debe ser válido)
        String telefono = etTelefono.getText().toString().trim();
        if (!telefono.isEmpty() && telefono.length() < 8) {
            tilTelefono.setError("El teléfono debe tener al menos 8 dígitos");
            esValido = false;
        } else {
            tilTelefono.setError(null);
        }

        return esValido;
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