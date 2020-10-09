package com.denisse.implemento.Fragment.Entrega;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denisse.implemento.Activity.DetailActivity;
import com.denisse.implemento.Adapter.AdapterEntrega;
import com.denisse.implemento.Adapter.AdapterEntregaList;
import com.denisse.implemento.Model.Empleado.Empleado;
import com.denisse.implemento.Model.Entrega.EntregaModel;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.EmpleadoUtils.FirebaseEmpleado;
import com.denisse.implemento.Utils.EntregaOp.EntregasOp;
import com.denisse.implemento.Utils.EntregaUtils.FirebaseEntrega;

import java.util.List;

public class EntregaListFragment extends Fragment {

    private Context context;
    private View view;

    private ImageButton btnBack;
    private ImageView img_main;
    private Button btnSearch;
    private TextView lblError, lblTileToolBar;
    private LinearLayout lyData, lyError;
    private EditText txtSearch;

    private RecyclerView rv_entrega_list;
    private CardView btnAdd;
    private SwipeRefreshLayout refresh;
    private String id_entrega = "";
    private boolean isRefresh = true;


    public EntregaListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_entrega_list, container, false);
        context = getActivity();
        initializeToolbar();
        startWidgets();
        onActionClickListener();
        getDataArgunments();

        return view;
    }

    private void getDataArgunments() {
        try {
            Bundle bundle = getArguments();
            if(bundle.getSerializable("id_entrega")!=null){
                id_entrega = (String) bundle.getSerializable("id_entrega");
                isRefresh = false;
                getListByNotify();
                btnAdd.setVisibility(View.GONE);
            }else{
                getListEntregas();
            }
        }catch (Exception e){
            getListEntregas();
        }
    }

    private void initializeToolbar() {
        lblTileToolBar = view.findViewById(R.id.lblTileToolBar);
        btnSearch = view.findViewById(R.id.btnSearch);
        txtSearch = view.findViewById(R.id.txtSearch);
        img_main = view.findViewById(R.id.img_main);
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        btnSearch.setVisibility(View.GONE);
        txtSearch.setVisibility(View.GONE);
        img_main.setVisibility(View.GONE);
        lblTileToolBar.setVisibility(View.VISIBLE);
        lblTileToolBar.setText("Entrega de implementos");
    }

    private void startWidgets() {
        btnAdd = view.findViewById(R.id.btnAdd);
        lyData = view.findViewById(R.id.lyData);
        lyError = view.findViewById(R.id.lyError);
        lblError = view.findViewById(R.id.lblError);
        refresh = view.findViewById(R.id.refresh);

        rv_entrega_list = view.findViewById(R.id.rv_entrega_list);
        rv_entrega_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv_entrega_list.setHasFixedSize(true);

        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorWhite));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isRefresh){
                    getListEntregas();
                }else{
                    getListByNotify();
                }
                refresh.setRefreshing(false);
            }
        });
    }

    private void onActionClickListener() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("action","createEntrega");
                //intent.putExtra("name","Guardar");
                //intent.putExtra("isCreate",true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    private void msgDialog(String msg) {
        ActivityFragmentUtils.ShowMessageDefault(msg, context, new ActivityFragmentUtils.onClickDialog() {
            @Override
            public void onClickDialog(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    private void getListEntregas() {
        FirebaseEntrega.getEntregas(context, new FirebaseEntrega.FbRsEntregas() {
            @Override
            public void isSuccesError(boolean isSucces, String msg, List<EntregaModel> entregaModels) {
                if(!isSucces){
                    lyData.setVisibility(View.VISIBLE);
                    lyError.setVisibility(View.GONE);
                    startRecycler(entregaModels);
                }else{
                    lyData.setVisibility(View.GONE);
                    lyError.setVisibility(View.VISIBLE);
                    lblError.setText(msg);
                }
            }
        });
    }

    private void getListByNotify(){
        FirebaseEntrega.getEntregasByID(context, id_entrega, new FirebaseEntrega.FbRsEntregas() {
            @Override
            public void isSuccesError(boolean isSucces, String msg, List<EntregaModel> entregaModels) {
                if(!isSucces){
                    lyData.setVisibility(View.VISIBLE);
                    lyError.setVisibility(View.GONE);
                    startRecycler(entregaModels);
                }else{
                    lyData.setVisibility(View.GONE);
                    lyError.setVisibility(View.VISIBLE);
                    lblError.setText(msg);
                }
            }
        });
    }

    private void startRecycler(List<EntregaModel> entregaModels) {
        AdapterEntregaList adapterEntrega = new AdapterEntregaList(context, entregaModels, new AdapterEntregaList.OnCardClickListner() {
            @Override
            public void OnCardClicked(int position) {
                EntregaModel entregaModel = entregaModels.get(position);
                EntregasOp.DialogInfService(context, entregaModel.getEntregaItems());
            }

            @Override
            public void updateList(boolean status) {
                getDataArgunments();
            }
        });
        rv_entrega_list.setAdapter(adapterEntrega);
    }

}