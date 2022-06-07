package com.example.befit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class IngresarComo extends AppCompatActivity {

    private Button usuario, admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_como);

        usuario=(Button)findViewById(R.id.usuario);
        admin=(Button)findViewById(R.id.admin);

        usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(IngresarComo.this, LoginUser.class);
                startActivity(intent);

            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(IngresarComo.this, LoginAdmin.class);
                startActivity(i);

            }
        });

    }
}