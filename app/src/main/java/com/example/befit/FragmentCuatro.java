package com.example.befit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;


public class FragmentCuatro extends Fragment {


    private View fragmento;

    private ImageView alimentos;
    private ImageView bebidas;

    public FragmentCuatro() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmento = inflater.inflate(R.layout.fragment_cuatro, container, false);

        alimentos = (ImageView)fragmento.findViewById(R.id.alimentos);
        bebidas = (ImageView) fragmento.findViewById(R.id.bebidas);


        alimentos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getContext(), AgregarproductoActivity.class);
                intent.putExtra("categoria", "Alimentos");
                startActivity(intent);
            }

        });

        bebidas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getContext(), AgregarproductoActivity.class);
                intent.putExtra("categoria", "Bebidas");
                startActivity(intent);
            }

        });
        return fragmento;
    }
}