package com.example.befit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.befit.Clases.Ordenes;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentDos extends Fragment {

    private View Vista;
    private RecyclerView recicler;
    private DatabaseReference OrdenRef;

    public FragmentDos() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Vista = inflater.inflate(R.layout.fragment_dos, container, false);
        OrdenRef = FirebaseDatabase.getInstance().getReference().child("Ordenes");
        recicler = (RecyclerView) Vista.findViewById(R.id.recicler_ordenes);
        recicler.setLayoutManager(new LinearLayoutManager(getContext()));
        return Vista;
    }

    @Override
    public void onStart() {

        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Ordenes>().setQuery(OrdenRef, Ordenes.class).build();
        FirebaseRecyclerAdapter<Ordenes, OrdenesViewHolder> adapter = new FirebaseRecyclerAdapter<Ordenes, OrdenesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrdenesViewHolder holder, int position, @NonNull Ordenes model) {
                holder.nombre.setText("Cliente: " + model.getNombre());
                holder.numero.setText("Tel: " + model.getTelefono());
                holder.precio.setText("Total: $ " + model.getTotal());
                holder.correo.setText("Correo: " + model.getCorreo() + "\nDir: " + model.getDireccion());
                holder.fecha.setText("Fecha: " + model.getFecha() + "\nHora: " + model.getHora());
                holder.boton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v ) {
                        Toast.makeText(getContext(), "Pedido confirmado", Toast.LENGTH_SHORT).show();
                        EnviarAlInicio();

                    }
                });

            }

            @NonNull
            @Override
            public OrdenesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordenes_layout, parent, false);
                OrdenesViewHolder viewHolder = new OrdenesViewHolder(view);
                return viewHolder;

            }
        };

        recicler.setAdapter(adapter);
        adapter.startListening();


    }

    public static class OrdenesViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, numero, precio, correo, fecha;
        Button boton;

        public OrdenesViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.ordenname);
            numero = itemView.findViewById(R.id.ordenphone);
            precio = itemView.findViewById(R.id.ordenprecio);
            correo = itemView.findViewById(R.id.ordencorreodir);
            fecha = itemView.findViewById(R.id.ordenfecha);
            boton = itemView.findViewById(R.id.verproductosorden);
        }
    }

    private void EnviarAlInicio(){
        Intent intent = new Intent(getContext(), AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}

