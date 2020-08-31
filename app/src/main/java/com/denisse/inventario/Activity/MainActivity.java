package com.denisse.inventario.Activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.denisse.inventario.Fragment.Empleado.EmpleadoListFragment;
import com.denisse.inventario.Fragment.HomeFragment;
import com.denisse.inventario.Fragment.Inventario.InventarioListFragment;
import com.denisse.inventario.R;
import com.denisse.inventario.Utils.ActivityFragmentUtils;

public class MainActivity extends AppCompatActivity {

    /**
     proyectotesisapp@gmail.com
     proyectotesis1234

     https://www.youtube.com/watch?v=8FJkt4ZwAnQ
     https://github.com/PhilJay/MPAndroidChart

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityFragmentUtils.permisosGaleria(this);

        ActivityFragmentUtils.changeFragment(false, getSupportFragmentManager(), new HomeFragment());
    }
}