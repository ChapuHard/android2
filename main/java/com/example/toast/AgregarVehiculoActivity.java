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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Activity para agregar nuevos vehículos al sistema de estacionamiento
 * Implementa la operación CREATE del CRUD
 */
public class AgregarVehiculoActivity extends AppCompatActivity {
    // Declaración de componentes UI
    private TextInputLayout tilPlaca, tilPropietario, tilTelefono;
    private TextInputEditText etPlaca, etPropietario, etTelefono;
    private Spinner spinnerTipoVehiculo;
    private Button btnGuardar, btnCancelar;

    // Base de datos helper
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_vehiculo);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);

        // Inicializar componentes UI
        inicializarComponentes();

        // Configurar Spinner con tipos de vehículos
        configurarSpinner();

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
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);
    }

    /**
     * Método para configurar el Spinner con los tipos de vehículos disponibles
     */
    private void configurarSpinner() {
        String[] tiposVehiculos = {"Automóvil", "Motocicleta", "Camioneta", "SUV", "Bicicleta"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tiposVehiculos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTipoVehiculo.setAdapter(adapter);
    }

    /**
     * Método para configurar los eventos de los botones
     */
    private void configurarEventos() {
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarVehiculo();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cerrar la actividad sin guardar
            }
        });
    }

    /**
     * Método principal para guardar un nuevo vehículo
     */
    private void guardarVehiculo() {
        if (validarCampos()) {
            // Obtener datos de los campos
            String placa = etPlaca.getText().toString().trim().toUpperCase();
            String propietario = etPropietario.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String tipoVehiculo = spinnerTipoVehiculo.getSelectedItem().toString();

            // Obtener fecha y hora actual
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            Date ahora = new Date();

            String fechaIngreso = formatoFecha.format(ahora);
            String horaIngreso = formatoHora.format(ahora);

            // Crear objeto Vehiculo
            Vehiculo nuevoVehiculo = new Vehiculo(placa, propietario, telefono,
                    tipoVehiculo, fechaIngreso, horaIngreso);

            // Intentar guardar en la base de datos
            long resultado = databaseHelper.agregarVehiculo(nuevoVehiculo);

            if (resultado != -1) {
                Toast.makeText(this, "Vehículo registrado exitosamente", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                finish(); // Cerrar actividad después de guardar
            } else {
                Toast.makeText(this, "Error al registrar vehículo. Verifique los datos o si la placa ya existe.", Toast.LENGTH_LONG).show();
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

    /**
     * Método para limpiar todos los campos del formulario
     */
    private void limpiarCampos() {
        etPlaca.setText("");
        etPropietario.setText("");
        etTelefono.setText("");
        spinnerTipoVehiculo.setSelection(0);

        // Limpiar errores
        tilPlaca.setError(null);
        tilPropietario.setError(null);
        tilTelefono.setError(null);
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