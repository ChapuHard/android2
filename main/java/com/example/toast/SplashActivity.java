package com.example.toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    // Duración del splash screen en milisegundos (3 segundos)
    private static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Usar Handler para crear un retraso antes de navegar al LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Crear intent para navegar al LoginActivity
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);

                // Finalizar SplashActivity para que no se pueda volver a ella
                finish();
            }
        }, SPLASH_DURATION);
    }
}