package com.denisse.implemento.Fragment.Administracion;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denisse.implemento.Model.Administracion;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.AdministracionOp.FirebaseAdministracion;
import com.denisse.implemento.Adapter.AdapterAdministracion;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.AdministracionOp.AdministracionOp;

import java.util.List;

public class AdministracionListFragment extends Fragment {

    private Context context;
    private View view;

    private ImageButton btnBack;
    private ImageView img_main;

    private LinearLayout lyError, lyData;
    private SwipeRefreshLayout refresh;
    private TextView lblError, lblTileToolBar;
    private EditText txtSearch;
    private Button btnSearch;

    private CardView btnAddEmpleado;
    private RecyclerView rv_empleado;

    private AdapterAdministracion adapterAdministracion;

    public AdministracionListFragment() {
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
        view = inflater.inflate(R.layout.fragment_empleado_list, container, false);
        context = getActivity();
        initializeToolbar();
        startWidgets();
        getListEmpleados();
        return view;
    }

    private void initializeToolbar() {
        lblTileToolBar = view.findViewById(R.id.lblTileToolBar);
        btnSearch = view.findViewById(R.id.btnSearch);
        txtSearch = view.findViewById(R.id.txtSearch);
        txtSearch.setHint("Cédula");
        btnSearch.setVisibility(View.GONE);
        lblTileToolBar.setVisibility(View.VISIBLE);
        lblTileToolBar.setText("Administración");
        img_main = view.findViewById(R.id.img_main);
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        img_main.setVisibility(View.GONE);
        txtSearch.setVisibility(View.GONE);


    }

    private void startWidgets() {
        refresh = view.findViewById(R.id.refresh);
        rv_empleado = view.findViewById(R.id.rv_empleado);
        lyData = view.findViewById(R.id.lyData);
        lyError = view.findViewById(R.id.lyError);
        lblError = view.findViewById(R.id.lblError);
        btnAddEmpleado = view.findViewById(R.id.btnAddEmpleado);

        rv_empleado.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv_empleado.setHasFixedSize(true);

        btnAddEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdministracionOp.DialogAdministracion(context, null);
            }
        });

        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorWhite));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListEmpleados();
                refresh.setRefreshing(false);
            }
        });
    }

    private void msgError(String msg){
        lyData.setVisibility(View.GONE);
        lyError.setVisibility(View.VISIBLE);
        lblError.setText(msg);
        lblError.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
    }

    private void getListEmpleados() {
        if(ActivityFragmentUtils.isConnetionNetwork(context)){
            FirebaseAdministracion.getAdministracion(context, new FirebaseAdministracion.FbRsAdministracion() {
                @Override
                public void isSuccesError(boolean isSucces, String msg, List<Administracion> administracions) {
                    if(isSucces){
                        loadAdapter(administracions);
                    }else{
                        msgError(msg);
                    }
                }
            });
        }else{
            msgError("Verifique su conexión a internet");
        }
    }

    private void loadAdapter(List<Administracion> administracions) {
        //Log.e("Error-",".5. "+administracions.toString());
        adapterAdministracion = new AdapterAdministracion(context, administracions, new AdapterAdministracion.OnCardClickListner() {
            @Override
            public void OnCardClicked(View view, int position) {
                Administracion item = administracions.get(position);
                AdministracionOp.DialogAdministracion(context, item);
            }
        });
        rv_empleado.setAdapter(adapterAdministracion);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}