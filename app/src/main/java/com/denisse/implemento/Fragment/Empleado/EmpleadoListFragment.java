package com.denisse.implemento.Fragment.Empleado;

import android.annotation.SuppressLint;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denisse.implemento.R;
import com.denisse.implemento.Activity.DetailActivity;
import com.denisse.implemento.Adapter.AdapterEmpleado;
import com.denisse.implemento.Model.Empleado.Empleado;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.EmpleadoUtils.FirebaseEmpleado;

import java.util.List;

public class EmpleadoListFragment extends Fragment {

    private Context context;
    private View view;

    private ImageButton btnBack;
    private ImageView img_main;

    private LinearLayout lyError, lyData;
    private SwipeRefreshLayout refresh;
    private TextView lblError;
    private EditText txtSearch;
    private Button btnSearch;

    private CardView btnAddEmpleado;
    private RecyclerView rv_empleado;

    private AdapterEmpleado adapterEmpleado;

    public EmpleadoListFragment() {
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

    @SuppressLint("ClickableViewAccessibility")
    private void initializeToolbar() {
        btnSearch = view.findViewById(R.id.btnSearch);
        txtSearch = view.findViewById(R.id.txtSearch);
        txtSearch.setHint("Cédula");
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

            txtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(txtSearch.getText().toString().isEmpty()){
                        ActivityFragmentUtils.ShowMessageDefault("Parámetro de búsqueda<br>Cédula (ced:)<br>Apellido (ape:)", context, new ActivityFragmentUtils.onClickDialog() {
                            @Override
                            public void onClickDialog(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                    }
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
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("action","createUpdateEmpleado");
                intent.putExtra("name","Guardar");
                intent.putExtra("isCreate",true);
                intent.putExtra("empleadoData", new Empleado());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

    private void searchAction() {
        ActivityFragmentUtils.hideTeclado(context, lblError);
        String search = txtSearch.getText().toString().trim();
        if (search.isEmpty()){
            dlgMsg("Debe poner una descripción de búsqueda");
        }else{
            getListEmpleadoByParams(search);
        }
    }

    private void dlgMsg(String msg) {
        ActivityFragmentUtils.ShowMessage(msg, context, new ActivityFragmentUtils.onClickDialog() {
            @Override
            public void onClickDialog(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    private void msgError(String msg){
        lyData.setVisibility(View.GONE);
        lyError.setVisibility(View.VISIBLE);
        lblError.setText(msg);
        lblError.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
    }

    private void getListEmpleadoByParams(String search) {
        String params = "";
        if(search.contains("ced:")){
            params = "ci";
            search = search.replace("ced:", "");
        }else if (search.contains("ape:")){
            params = "apellidos";
            search = search.replace("ape:", "");
            search = search.replace(" ", "");
            search = search.toUpperCase();
        }else {
            dlgMsg("Parámetro de búsqueda no encontrada");
            return;
        }
        if(ActivityFragmentUtils.isConnetionNetwork(context)){
            FirebaseEmpleado.getEmpleadosByParamsSearch(context, params, search, new FirebaseEmpleado.FbRsEmpleado() {
                @Override
                public void isSuccesError(boolean isSucces, String msg, List<Empleado> empleados) {
                    if(isSucces){
                        Log.e("Error-klk", ".,ñ "+isSucces);
                        loadAdapter(empleados);
                    }else{
                        msgError(msg);
                    }
                }
            });
        }else{
            msgError("Verifique su conexión a internet");
        }
    }

    private void getListEmpleados() {
        FirebaseEmpleado.getEmpleados(context, new FirebaseEmpleado.FbRsEmpleado() {
            @Override
            public void isSuccesError(boolean isSucces, String msg, List<Empleado> empleados) {
                if(isSucces){
                    loadAdapter(empleados);
                }else{
                    msgError(msg);
                }
            }
        });
    }

    private void loadAdapter(List<Empleado> empleados) {
        //Log.e("Error-",".5. "+empleados.toString());
        adapterEmpleado = new AdapterEmpleado(context, empleados, new AdapterEmpleado.OnCardClickListner() {
            @Override
            public void OnCardClicked(View view, int position) {
                Empleado item = empleados.get(position);
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("action","createUpdateEmpleado");
                intent.putExtra("name","Actualizar");
                intent.putExtra("isCreate",false);
                intent.putExtra("empleadoData", item);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        rv_empleado.setAdapter(adapterEmpleado);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}