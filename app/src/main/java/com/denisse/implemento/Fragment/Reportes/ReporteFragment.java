package com.denisse.implemento.Fragment.Reportes;

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

import com.denisse.implemento.Activity.ContainerActivity;
import com.denisse.implemento.Activity.DetailActivity;
import com.denisse.implemento.Activity.LoginActivity;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.Constantes;
import com.denisse.implemento.Utils.EmpleadoUtils.FirebaseEmpleado;
import com.denisse.implemento.Utils.SharedData;

public class ReporteFragment extends Fragment {

    private Context context;
    private View view;

    // toolbar
    private ImageButton btnBack;
    private EditText txtSearch;
    private ImageView img_main;
    private Button btnSearch;
    private TextView lblTileToolBar, lblHome;
    private SharedPreferences sharedPreferences;

    private CardView btnRepStock, btnRepArea, btnRepDepart, btnRepReposi;

    public ReporteFragment() {
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
        view = inflater.inflate(R.layout.fragment_reporte, container, false);
        context = getActivity();
        sharedPreferences = context.getSharedPreferences(SharedData.KEYPREFERENCES, Context.MODE_PRIVATE);
        initToobar();
        startWidgets();
        return view;
    }

    private void initToobar() {
        lblHome = view.findViewById(R.id.lblHome);
        lblTileToolBar = view.findViewById(R.id.lblTileToolBar);
        lblTileToolBar = view.findViewById(R.id.lblTileToolBar);
        btnSearch = view.findViewById(R.id.btnSearch);
        img_main = view.findViewById(R.id.img_main);
        txtSearch = view.findViewById(R.id.txtSearch);
        btnBack = view.findViewById(R.id.btnBack);
        txtSearch.setVisibility(View.GONE);
        img_main.setVisibility(View.GONE);
        btnSearch.setVisibility(View.GONE);
        lblTileToolBar.setVisibility(View.VISIBLE);
        lblTileToolBar.setText("Reportes");
        //lblTileToolBar.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

        String user = (SharedData.getKey(context, SharedData.NAME_USER)!=null)?SharedData.getKey(context, SharedData.NAME_USER) : "";
        lblHome.setText("Bienvenido, "+ user);
        lblHome.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    private void startWidgets() {
        btnRepStock = view.findViewById(R.id.btnRepStock);
        btnRepArea = view.findViewById(R.id.btnRepArea);
        btnRepDepart = view.findViewById(R.id.btnRepDepart);
        btnRepReposi = view.findViewById(R.id.btnRepReposi);

        btnRepStock.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("action", "repote_stock");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnRepArea.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("action", "repote_");
            intent.putExtra("titulo", "Reporte por área");
            intent.putExtra("tipo", "area");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnRepDepart.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("action", "repote_");
            intent.putExtra("titulo", "Reporte por departamento");
            intent.putExtra("tipo", "departamento");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnRepReposi.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("action", "repote_");
            intent.putExtra("titulo", "Reporte por reposiciones");
            intent.putExtra("tipo", "reposiciones");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }
}