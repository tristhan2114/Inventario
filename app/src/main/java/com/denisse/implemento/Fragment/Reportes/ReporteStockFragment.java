package com.denisse.implemento.Fragment.Reportes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denisse.implemento.Activity.DetailActivity;
import com.denisse.implemento.Adapter.AdapterInventario;
import com.denisse.implemento.Adapter.AdapterReporteStock;
import com.denisse.implemento.Model.Implemento;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.ImplementoUtils.FirebaseImplemento;
import com.denisse.implemento.Utils.ImplementosOp.AdministracionOp;

import java.util.List;

public class ReporteStockFragment extends Fragment {

    private Context context;
    private View view;

    private ImageButton btnBack;
    private ImageView img_main;
    private TextView lblTileToolBar;

    private LinearLayout lyError, lyData;
    private SwipeRefreshLayout refresh;
    private TextView lblError;
    private EditText txtSearch;
    private Button btnSearch;

    private CardView btnAdd;
    private RecyclerView rv_inventario;

    private AdapterReporteStock adapterInventario;

    public ReporteStockFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inventario_list, container, false);
        context = getActivity();
        initializeToolbar();
        startWidgets();
        getListInvetario();
        return view;
    }

    private void initializeToolbar() {
        lblTileToolBar = view.findViewById(R.id.lblTileToolBar);
        btnSearch = view.findViewById(R.id.btnSearch);
        txtSearch = view.findViewById(R.id.txtSearch);
        txtSearch.setHint("Código");
        btnSearch.setVisibility(View.GONE);
        lblTileToolBar.setVisibility(View.VISIBLE);
        lblTileToolBar.setText("Reporte Stock");
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

        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchAction();
                    return true;
                }
                return false;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAction();
            }
        });
    }

    private void startWidgets() {
        refresh = view.findViewById(R.id.refresh);
        rv_inventario = view.findViewById(R.id.rv_inventario);
        lyData = view.findViewById(R.id.lyData);
        lyError = view.findViewById(R.id.lyError);
        lblError = view.findViewById(R.id.lblError);
        btnAdd = view.findViewById(R.id.btnAddEmpleado);

        rv_inventario.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv_inventario.setHasFixedSize(true);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdministracionOp.DialogInfService(context);
            }
        });
        btnAdd.setVisibility(View.GONE);
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorWhite));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListInvetario();
                refresh.setRefreshing(false);
            }
        });
    }

    private void searchAction() {
        ActivityFragmentUtils.hideTeclado(context, lblError);
        String search = txtSearch.getText().toString().trim();
        if (search.isEmpty()){
            ActivityFragmentUtils.ShowMessage("Debe poner una descripción de búsqueda", context, new ActivityFragmentUtils.onClickDialog() {
                @Override
                public void onClickDialog(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }else{
            getListEmpleadoByParams(search);
        }
    }

    private void msgError(String msg){
        lyData.setVisibility(View.GONE);
        lyError.setVisibility(View.VISIBLE);
        lblError.setText(msg);
        lblError.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
    }

    private void getListEmpleadoByParams(String params) {
        if(ActivityFragmentUtils.isConnetionNetwork(context)){
            FirebaseImplemento.getInventarioByParams(context, params, new FirebaseImplemento.FbRsImplemento() {
                @Override
                public void isSuccesError(boolean isSucces, String msg, List<Implemento> implementos) {
                    if(isSucces){
                        loadAdapter(implementos);
                    }else{
                        msgError(msg);
                    }
                }
            });
        }else{
            msgDialogo("Verifica tu conexión a internet");
        }
    }

    private void getListInvetario() {
        if(ActivityFragmentUtils.isConnetionNetwork(context)){
            FirebaseImplemento.getInventarios(context, new FirebaseImplemento.FbRsImplemento() {
                @Override
                public void isSuccesError(boolean isSucces, String msg, List<Implemento> implementos) {
                    Log.e("Error-",".1. "+isSucces+ " .. -  "+msg);
                    if(isSucces){
                        loadAdapter(implementos);
                    }else{
                        msgError(msg);
                    }
                }
            });
        }else{
           msgDialogo("Verifica tu conexión a internet");
        }
    }

    private void loadAdapter(List<Implemento> list) {
        Log.e("Error-",".5. "+list.toString());
        adapterInventario = new AdapterReporteStock(context, list, true);
        rv_inventario.setAdapter(adapterInventario);
    }

    private void msgDialogo(String msg){
        ActivityFragmentUtils.ShowMessage(msg, context, new ActivityFragmentUtils.onClickDialog() {
            @Override
            public void onClickDialog(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}