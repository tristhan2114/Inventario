package com.denisse.implemento.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.denisse.implemento.Fragment.Reportes.ReporteDetailFragment;
import com.denisse.implemento.Fragment.Reportes.ReporteStockFragment;
import com.denisse.implemento.R;
import com.denisse.implemento.Fragment.Empleado.EmpleadoCreateFragment;
import com.denisse.implemento.Fragment.Implementos.InventarioCreateFragment;
import com.denisse.implemento.Model.Empleado.Empleado;
import com.denisse.implemento.Model.Implemento;
import com.denisse.implemento.Utils.ActivityFragmentUtils;

public class DetailActivity extends AppCompatActivity {

    private String action = "", name = "", titulo = "", tipo = "";
    private boolean isCreate = false;
    private Empleado empleadoData;
    private Implemento implemento;

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
                implemento = (Implemento) intent.getSerializableExtra("inventarioData");
                InventarioCreateFragment inventarioCreateFragment = new InventarioCreateFragment();

                bundle.putSerializable("name", name);
                bundle.putSerializable("isCreate", isCreate);
                bundle.putSerializable("inventarioData", implemento);
                inventarioCreateFragment.setArguments(bundle);

                ActivityFragmentUtils.changeFragment(false, getSupportFragmentManager(), inventarioCreateFragment);

                break;
            case "repote_stock":
                ActivityFragmentUtils.changeFragment(false, getSupportFragmentManager(), new ReporteStockFragment());

                break;
            case "repote_":
                ReporteDetailFragment reporteDetailFragment = new ReporteDetailFragment();
                titulo = (String) intent.getSerializableExtra("titulo");
                tipo = (String) intent.getSerializableExtra("tipo");

                bundle.putSerializable("titulo", titulo);
                bundle.putSerializable("tipo", tipo);
                reporteDetailFragment.setArguments(bundle);
                ActivityFragmentUtils.changeFragment(false, getSupportFragmentManager(), reporteDetailFragment);

                break;
            default:

                break;
        }

    }
}