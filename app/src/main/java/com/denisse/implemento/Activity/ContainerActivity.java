package com.denisse.implemento.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.denisse.implemento.Fragment.Empleado.EmpleadoListFragment;
import com.denisse.implemento.Fragment.Entrega.EntregaFragment;
import com.denisse.implemento.R;
import com.denisse.implemento.Fragment.Administracion.AdministracionListFragment;
import com.denisse.implemento.Fragment.Implementos.InventarioListFragment;
import com.denisse.implemento.Utils.ActivityFragmentUtils;

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
                case "listAdministracion":
                ActivityFragmentUtils.changeFragment(false, getSupportFragmentManager(), new AdministracionListFragment());
                break;
                case "Entrega":
                ActivityFragmentUtils.changeFragment(false, getSupportFragmentManager(), new EntregaFragment());
                break;
            default:

                break;
        }

    }

}