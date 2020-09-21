package com.denisse.implemento.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.denisse.implemento.R;
import com.denisse.implemento.Utils.EmpleadoUtils.FirebaseEmpleado;
import com.denisse.implemento.Activity.ContainerActivity;
import com.denisse.implemento.Activity.LoginActivity;
import com.denisse.implemento.Utils.Constantes;
import com.denisse.implemento.Utils.SharedData;

public class HomeFragment extends Fragment {

    private Context context;
    private View view;

    private CardView btnRegistrarInventario, btnRegistrarEmpleado, btnCerrarSesion, btnAdministracion, btnRegistrarEntregas, btnRegistrarReportes;

    // toolbar
    private ImageButton btnBack;
    private EditText txtSearch;
    private ImageView img_main;
    private Button btnSearch;
    private TextView lblTileToolBar, lblHome;
    private SharedPreferences sharedPreferences;

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
        sharedPreferences = context.getSharedPreferences(SharedData.KEYPREFERENCES, Context.MODE_PRIVATE);
        initToobar();
        startWidgets();
        verifyPermissionByRol();
        return view;
    }

    private void verifyPermissionByRol() {
        if(SharedData.getKey(context, SharedData.ROL) != null){
            switch (SharedData.getKey(context, SharedData.ROL)){
                case Constantes.ROL_ADMINISTRADOR:

                    break;
                case Constantes.ROL_JEFE:
                    btnAdministracion.setVisibility(View.GONE);

                    btnRegistrarInventario.setVisibility(View.GONE);
                    btnRegistrarEmpleado.setVisibility(View.GONE);
                    // reportes Graficos
                    break;
                case Constantes.ROL_ASISTENTE:
                    btnAdministracion.setVisibility(View.GONE);
                    btnRegistrarInventario.setVisibility(View.VISIBLE);
                    btnRegistrarEmpleado.setVisibility(View.VISIBLE);

                    break;
                case Constantes.ROL_INSPECTOR:
                    btnAdministracion.setVisibility(View.GONE);

                    btnRegistrarInventario.setVisibility(View.GONE);
                    btnRegistrarEmpleado.setVisibility(View.GONE);
                    // entrega

                    break;
            }
        }
    }

    private void initToobar() {
        lblHome = view.findViewById(R.id.lblHome);
        lblTileToolBar = view.findViewById(R.id.lblTileToolBar);
        lblTileToolBar = view.findViewById(R.id.lblTileToolBar);
        btnSearch = view.findViewById(R.id.btnSearch);
        img_main = view.findViewById(R.id.img_main);
        txtSearch = view.findViewById(R.id.txtSearch);
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setVisibility(View.GONE);
        txtSearch.setVisibility(View.GONE);
        img_main.setVisibility(View.GONE);
        btnSearch.setVisibility(View.GONE);
        lblTileToolBar.setVisibility(View.VISIBLE);
        lblTileToolBar.setText("");
        lblTileToolBar.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

        String user = (SharedData.getKey(context, SharedData.NAME_USER)!=null)?SharedData.getKey(context, SharedData.NAME_USER) : "";
        lblHome.setText("Bienvenido, "+ user);
        lblHome.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
    }

    private void startWidgets() {
        btnRegistrarEntregas = view.findViewById(R.id.btnRegistrarEntregas);
        btnAdministracion = view.findViewById(R.id.btnAdministracion);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        btnRegistrarInventario = view.findViewById(R.id.btnRegistrarInventario);
        btnRegistrarEmpleado = view.findViewById(R.id.btnRegistrarEmpleado);
        btnRegistrarReportes = view.findViewById(R.id.btnRegistrarReportes);

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

        btnCerrarSesion.setOnClickListener(view -> {
            FirebaseEmpleado.cerrarSesion(context);
            SharedData.clearSharedPreferences(context);
            startActivity(new Intent(context, LoginActivity.class));
            getActivity().finish();
        });

        btnAdministracion.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("action", "listAdministracion");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnRegistrarEntregas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ContainerActivity.class);
                intent.putExtra("action", "Entrega");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnRegistrarReportes.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("action", "reporte");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}