package com.denisse.inventario.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denisse.inventario.Activity.ContainerActivity;
import com.denisse.inventario.R;

public class HomeFragment extends Fragment {

    private Context context;
    private View view;

    private CardView btnRegistrarInventario, btnRegistrarEmpleado;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        startWidgets();
        return view;
    }

    private void startWidgets() {
        btnRegistrarInventario = view.findViewById(R.id.btnRegistrarInventario);
        btnRegistrarEmpleado = view.findViewById(R.id.btnRegistrarEmpleado);

        btnRegistrarEmpleado.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("action", "listEmpleado");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnRegistrarInventario.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("action", "listInventario");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}