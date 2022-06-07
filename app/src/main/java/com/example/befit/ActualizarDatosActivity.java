package com.example.befit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class ActualizarDatosActivity extends AppCompatActivity {

    private EditText nombre, direccion, telefono, correo;
    private Button guardar;
    private FirebaseAuth auth;
    private DatabaseReference UserRef;
    private ProgressDialog dialog;
    private String CurrentUserId;
    private static int Galery_pick = 1;
    private StorageReference UserImagePerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_datos);

        auth= FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        dialog = new ProgressDialog(this);
        UserImagePerfil = FirebaseStorage.getInstance().getReference().child("Perfil");
        nombre = (EditText)findViewById(R.id.actualizar_nombre);
        direccion = (EditText)findViewById(R.id.actualizar_direccion);
        telefono = (EditText)findViewById(R.id.actualizar_telefono);
        correo = (EditText)findViewById(R.id.actualizar_correo);
        guardar = (Button)findViewById(R.id.actualizar_boton);

        UserRef.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.hasChild("")){
                    String nombres = snapshot.child("nombre").getValue().toString();
                    String direccions = snapshot.child("direccion").getValue().toString();
                    String telefonos = snapshot.child("telefono").getValue().toString();
                    String correos = snapshot.child("correo").getValue().toString();

                    nombre.setText(nombres);
                    direccion.setText(direccions);
                    telefono.setText(telefonos);
                    correo.setText(correos);
                }else if (snapshot.exists()){
                    String nombres = snapshot.child("nombre").getValue().toString();
                    String direccions = snapshot.child("direccion").getValue().toString();
                    String telefonos = snapshot.child("telefono").getValue().toString();
                    String correos = snapshot.child("correo").getValue().toString();
                    nombre.setText(nombres);
                    direccion.setText(direccions);
                    telefono.setText(telefonos);
                    correo.setText(correos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        guardar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                GuardarInformacion();
            }
        });
    }
    private void GuardarInformacion(){
        String nombres = nombre.getText().toString().toUpperCase();
        String direcciones = direccion.getText().toString();
        String phones = telefono.getText().toString();
        String correos = correo.getText().toString();

        if(TextUtils.isEmpty(nombres)){
            Toast.makeText(this, "Ingrese el nombre", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(direcciones)){
            Toast.makeText(this, "Ingrese su direccion", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(phones)){
            Toast.makeText(this, "Ingrese su numero de telefono", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(correos)){
            Toast.makeText(this, "Ingrese su correo electronico", Toast.LENGTH_SHORT).show();
        }else{
            dialog.setTitle("Guardando");
            dialog.setMessage("Por favor espere....");
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);

            HashMap map = new HashMap();
            map.put("nombre", nombres);
            map.put("direccion", direcciones);
            map.put("telefono", phones);
            map.put("correo", correos);
            map.put("uid",CurrentUserId);

            UserRef.child(CurrentUserId).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        EnviarAlInicio();
                        dialog.dismiss();

                    }else{
                        String mensaje = task.getException().toString();
                        Toast.makeText(ActualizarDatosActivity.this, "Error: "+mensaje, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });

        }
    }

    private void EnviarAlInicio(){
        Intent intent = new Intent(ActualizarDatosActivity.this, PrincipalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
