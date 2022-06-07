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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class SetupadminActivity extends AppCompatActivity {

    private EditText nombre, direccion, telefono, correo;
    private Button guardar;
    private String phone ="";
    private FirebaseAuth auth;
    private DatabaseReference UserRef;
    private ProgressDialog dialog;
    private String CurrentUserId;
    private static int Galery_pick = 1;
    private StorageReference UserImagePerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setupadmin);

        auth= FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        dialog = new ProgressDialog(this);
        UserImagePerfil = FirebaseStorage.getInstance().getReference().child("Perfil");
        nombre = (EditText)findViewById(R.id.asetup_nombre);
        direccion = (EditText)findViewById(R.id.asetup_direccion);
        telefono = (EditText)findViewById(R.id.asetup_telefono);
        correo = (EditText)findViewById(R.id.asetup_correo);
        guardar = (Button)findViewById(R.id.asetup_boton);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            phone = bundle.getString("phone");
        }

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
                        Toast.makeText(SetupadminActivity.this, "Error: "+mensaje, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });

        }
    }

    private void EnviarAlInicio(){
        Intent intent = new Intent(SetupadminActivity.this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}