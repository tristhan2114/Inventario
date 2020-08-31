package com.denisse.inventario.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.denisse.inventario.Fragment.Empleado.EmpleadoListFragment;
import com.denisse.inventario.Fragment.Inventario.InventarioListFragment;
import com.denisse.inventario.R;
import com.denisse.inventario.Utils.ActivityFragmentUtils;

public class ContainerActivity extends AppCompatActivity {

    private String action = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent != null){
            action = (String) intent.getSerializableExtra("action");
        }

        switch (action){
            case "listEmpleado":
                ActivityFragmentUtils.changeFragment(false, getSupportFragmentManager(), new EmpleadoListFragment());
                break;
            case "listInventario":
                ActivityFragmentUtils.changeFragment(false, getSupportFragmentManager(), new InventarioListFragment());
                break;
            default:

                break;
        }

    }

}