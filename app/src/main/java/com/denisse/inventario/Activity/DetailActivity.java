package com.denisse.inventario.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.denisse.inventario.Fragment.Empleado.EmpleadoCreateFragment;
import com.denisse.inventario.Fragment.Empleado.EmpleadoListFragment;
import com.denisse.inventario.Fragment.Inventario.InventarioCreateFragment;
import com.denisse.inventario.Model.Empleado.Empleado;
import com.denisse.inventario.Model.Inventario;
import com.denisse.inventario.R;
import com.denisse.inventario.Utils.ActivityFragmentUtils;

public class DetailActivity extends AppCompatActivity {

    private String action = "", name = "";
    private boolean isCreate = false;
    private Empleado empleadoData;
    private Inventario inventario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent != null){
            action = (String) intent.getSerializableExtra("action");
        }

        Bundle bundle = new Bundle();
        switch (action){
            case "createUpdateEmpleado":
                isCreate = (Boolean) intent.getSerializableExtra("isCreate");
                name = (String) intent.getSerializableExtra("name");
                empleadoData = (Empleado) intent.getSerializableExtra("empleadoData");

                EmpleadoCreateFragment empleadoCreateFragment = new EmpleadoCreateFragment();
                bundle.putSerializable("name", name);
                bundle.putSerializable("isCreate", isCreate);
                bundle.putSerializable("empleadoData", empleadoData);
                empleadoCreateFragment.setArguments(bundle);

                ActivityFragmentUtils.changeFragment(false, getSupportFragmentManager(), empleadoCreateFragment);

                break;
            case "createUpdateInventario":
                isCreate = (Boolean) intent.getSerializableExtra("isCreate");
                name = (String) intent.getSerializableExtra("name");
                inventario = (Inventario) intent.getSerializableExtra("inventarioData");
                InventarioCreateFragment inventarioCreateFragment = new InventarioCreateFragment();

                bundle.putSerializable("name", name);
                bundle.putSerializable("isCreate", isCreate);
                bundle.putSerializable("inventarioData", inventario);
                inventarioCreateFragment.setArguments(bundle);

                ActivityFragmentUtils.changeFragment(false, getSupportFragmentManager(), inventarioCreateFragment);

                break;
            default:

                break;
        }

    }
}