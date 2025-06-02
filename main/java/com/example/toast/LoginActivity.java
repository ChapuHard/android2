package com.example.toast;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    // Declaración de componentes UI
    private TextInputLayout tilUsuario, tilPassword;
    private TextInputEditText etUsuario, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicialización de componentes
        tilUsuario = findViewById(R.id.tilUsuario);
        tilPassword = findViewById(R.id.tilPassword);
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Configuración del evento onClick para el botón de login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validación de campos
                if (validarCampos()) {
                    // Si la validación es exitosa, navegar al menú principal
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish(); // Finalizar esta actividad para que no se pueda volver atrás
                }
            }
        });
    }

    /**
     * Método para validar los campos del formulario
     * @return true si los campos son válidos, false en caso contrario
     */
    private boolean validarCampos() {
        boolean esValido = true;

        // Validar campo de usuario
        String usuario = etUsuario.getText().toString().trim();
        if (usuario.isEmpty()) {
            tilUsuario.setError("El usuario es requerido");
            esValido = false;
        } else {
            tilUsuario.setError(null);
        }

        // Validar campo de contraseña
        String password = etPassword.getText().toString().trim();
        if (password.isEmpty()) {
            tilPassword.setError("La contraseña es requerida");
            esValido = false;
        } else if (password.length() < 4) {
            tilPassword.setError("La contraseña debe tener al menos 4 caracteres");
            esValido = false;
        } else {
            tilPassword.setError(null);
        }

        // Si los campos son válidos, mostrar un mensaje de bienvenida
        if (esValido) {
            Toast.makeText(this, "Bienvenido al sistema de estacionamiento", Toast.LENGTH_SHORT).show();
        }

        return esValido;
    }
}