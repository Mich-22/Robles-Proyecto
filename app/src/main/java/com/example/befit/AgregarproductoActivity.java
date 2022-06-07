package com.example.befit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AgregarproductoActivity extends AppCompatActivity {

    private ImageView imagenpro;
    private TextView textox;
    private EditText nombrepro, descripcionpro, preciocomprapro, precioventapro, cantidadpro;
    private Button agregarpro;
    private static final int Gallery_Pick =1;
    private Uri imagenUri;
    private String productosRandonKey, downloadUri;
    private StorageReference ProductoImagenRef;
    private DatabaseReference ProductoRef;
    private ProgressDialog dialog;
    private String Categoria, Nom, Desc, PrecioCom, PrecioVen, Cant, CurrenDate, CurrenTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarproducto);

        Categoria = getIntent().getExtras().get("categoria").toString();
        ProductoImagenRef = FirebaseStorage.getInstance().getReference().child("Imagenes Productos");
        ProductoRef = FirebaseDatabase.getInstance().getReference().child("Productos");

        textox =(TextView)findViewById(R.id.textox);
        imagenpro = (ImageView)findViewById(R.id.imagepro);
        nombrepro = (EditText) findViewById(R.id.nombrepro);
        descripcionpro = (EditText) findViewById(R.id.descripcionpro);
        preciocomprapro = (EditText) findViewById(R.id.preciocomprapro);
        precioventapro = (EditText) findViewById(R.id.precioventapro);
        cantidadpro = (EditText) findViewById(R.id.cantidadpro);
        agregarpro = (Button)findViewById(R.id.agregarpro);

        dialog = new ProgressDialog(this);

        imagenpro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AbrirGaleria();
            }
        });

        agregarpro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ValidarProductos();
            }
        });
    }

    private void AbrirGaleria() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/");
        startActivityForResult(intent, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data !=null){
            imagenUri = data.getData();
            imagenpro.setImageURI(imagenUri);

        }
    }

    private void ValidarProductos() {
        Nom = nombrepro.getText().toString();
        Desc = descripcionpro.getText().toString();
        PrecioCom = preciocomprapro.getText().toString();
        PrecioVen = precioventapro.getText().toString();
        Cant = cantidadpro.getText().toString();
        if(imagenUri == null){
            Toast.makeText(this, "Primero agrega una imagen", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Nom)){
            Toast.makeText(this, "Debes ingresar el nombre del producto", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Desc)){
            Toast.makeText(this, "Debes ingresar la descripcion del producto", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(PrecioCom)){
            Toast.makeText(this, "Debes ingresar el precio del compra del producto", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(PrecioVen)){
            Toast.makeText(this, "Debes ingresar el precio de venta del producto", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Cant)){
            Toast.makeText(this, "Debes ingresar la cantidad de producto", Toast.LENGTH_SHORT).show();
        }else{
            GuardarInformacionProducto();
        }
    }

    private void GuardarInformacionProducto() {

        dialog.setTitle("Guardando producto");
        dialog.setMessage("Por favor espere mienstras guardamos el producto");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat curreDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        CurrenDate = curreDateFormat.format(calendar.getTime());

        SimpleDateFormat CurrenTimeFormat = new SimpleDateFormat("HH:mm:ss");
        CurrenTime = CurrenTimeFormat.format(calendar.getTime());

        productosRandonKey = CurrenDate + CurrenTime;

        final StorageReference filePath = ProductoImagenRef.child(imagenUri.getLastPathSegment() + productosRandonKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imagenUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String mensaje = e.toString();
                Toast.makeText(AgregarproductoActivity.this, "Error: "+mensaje, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AgregarproductoActivity.this, "Imagen guardada exitosamente", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadUri = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadUri = task.getResult().toString();
                            Toast.makeText(AgregarproductoActivity.this, "Imagen guardado en Firebase", Toast.LENGTH_SHORT).show();
                            GuardarEnFirebase();

                        }else{
                            Toast.makeText(AgregarproductoActivity.this, "Error.....", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void GuardarEnFirebase() {

        HashMap<String,Object> map = new HashMap<>();
        map.put("pid",productosRandonKey);
        map.put("fecha",CurrenDate);
        map.put("hora",CurrenTime);
        map.put("descripcion",Desc);
        map.put("nombre",Nom);
        map.put("preciocom",PrecioCom);
        map.put("precioven",PrecioVen);
        map.put("cantidad",Cant);
        map.put("imagen",downloadUri);
        map.put("categoria",Categoria);

        ProductoRef.child(productosRandonKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(AgregarproductoActivity.this, AdminActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                    Toast.makeText(AgregarproductoActivity.this, "Solicitud Exitosa!", Toast.LENGTH_SHORT).show();

                }else{
                    dialog.dismiss();
                    String mensaje = task.getException().toString();
                    Toast.makeText(AgregarproductoActivity.this, "Error: "+mensaje, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}