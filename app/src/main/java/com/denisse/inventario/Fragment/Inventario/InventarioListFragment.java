package com.denisse.inventario.Fragment.Inventario;

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
import android.widget.Toast;

import com.denisse.inventario.Activity.DetailActivity;
import com.denisse.inventario.Adapter.AdapterEmpleado;
import com.denisse.inventario.Adapter.AdapterInventario;
import com.denisse.inventario.Model.Empleado.Empleado;
import com.denisse.inventario.Model.Inventario;
import com.denisse.inventario.R;
import com.denisse.inventario.Utils.ActivityFragmentUtils;
import com.denisse.inventario.Utils.EmpleadoUtils.FirebaseEmpleado;
import com.denisse.inventario.Utils.InventarioUtils.FirebaseInventario;

import java.util.List;

public class InventarioListFragment extends Fragment {

    private Context context;
    private View view;

    private ImageButton btnBack;
    private ImageView img_main;

    private LinearLayout lyError, lyData;
    private SwipeRefreshLayout refresh;
    private TextView lblError;
    private EditText txtSearch;
    private Button btnSearch;

    private CardView btnAdd;
    private RecyclerView rv_inventario;

    private AdapterInventario adapterInventario;

    public InventarioListFragment() {
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
        btnSearch = view.findViewById(R.id.btnSearch);
        txtSearch = view.findViewById(R.id.txtSearch);
        txtSearch.setHint("Código");
        img_main = view.findViewById(R.id.img_main);
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        img_main.setVisibility(View.GONE);
        txtSearch.setVisibility(View.VISIBLE);

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
                ActivityFragmentUtils.DialogInfService(context);
            }
        });

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
            FirebaseInventario.getInventarioByParams(context, params, new FirebaseInventario.FbRsInventario() {
                @Override
                public void isSuccesError(boolean isSucces, String msg, List<Inventario> inventarios) {
                    if(isSucces){
                        loadAdapter(inventarios);
                    }else{
                        msgError(msg);
                    }
                }
            });
        }else{
            //Toast.makeText(context, "no nettt", 3000).show();
            ActivityFragmentUtils.ShowMessage("Verifica tu conexión a internet", context, new ActivityFragmentUtils.onClickDialog() {
                @Override
                public void onClickDialog(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void getListInvetario() {
        if(ActivityFragmentUtils.isConnetionNetwork(context)){
            FirebaseInventario.getInventarios(context, new FirebaseInventario.FbRsInventario() {
                @Override
                public void isSuccesError(boolean isSucces, String msg, List<Inventario> inventarios) {
                    Log.e("Error-",".1. "+isSucces+ " .. -  "+msg);
                    if(isSucces){
                        loadAdapter(inventarios);
                    }else{
                        msgError(msg);
                    }
                }
            });
        }else{
            ActivityFragmentUtils.ShowMessage("Verifica tu conexión a internet", context, new ActivityFragmentUtils.onClickDialog() {
                @Override
                public void onClickDialog(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void loadAdapter(List<Inventario> list) {
        Log.e("Error-",".5. "+list.toString());
        adapterInventario = new AdapterInventario(context, list, new AdapterInventario.OnCardClickListner() {
            @Override
            public void OnCardClicked(View view, int position) {
                Inventario item = list.get(position);
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("action","createUpdateInventario");
                intent.putExtra("name","Actualizar");
                intent.putExtra("isCreate",false);
                intent.putExtra("inventarioData", item);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        rv_inventario.setAdapter(adapterInventario);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}