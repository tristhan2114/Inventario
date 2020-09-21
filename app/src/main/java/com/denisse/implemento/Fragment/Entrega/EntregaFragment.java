package com.denisse.implemento.Fragment.Entrega;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.denisse.implemento.Model.Empleado.Departamento;
import com.denisse.implemento.Model.Empleado.Puesto;
import com.denisse.implemento.Model.Entrega.EntregaModel;
import com.denisse.implemento.Model.Implemento;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.EmpleadoUtils.FirebaseEmpleado;
import com.denisse.implemento.Adapter.AdapterEntrega;
import com.denisse.implemento.Model.Empleado.Empleado;
import com.denisse.implemento.Model.Entrega.EntregaItem;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.EntregaUtils.FirebaseEntrega;
import com.denisse.implemento.Utils.ImplementoUtils.FirebaseImplemento;
import com.fastaccess.datetimepicker.DatePickerFragmentDialog;
import com.fastaccess.datetimepicker.DateTimeBuilder;
import com.fastaccess.datetimepicker.callback.DatePickerCallback;
import com.fastaccess.datetimepicker.callback.TimePickerCallback;

import java.util.ArrayList;
import java.util.List;

public class EntregaFragment extends Fragment implements DatePickerCallback, TimePickerCallback {

    private Context context;
    private View view;

    private ImageButton btnBack;
    private ImageView img_main;
    private Button btnSearch;

    private CardView btnAddEntrega, btnAddRv, cardEmpleado, cardArea, cardDepartamento;
    private EditText txtSearch, txtCedula;
    private LinearLayout lyData, lyError;
    private LinearLayout lyEmpleado, lyRsEmpleado, lyDepartamento, lyArea;
    private TextView lblNames, lblDepartamento, lblArea, lblFechIngreso, lblError;
    private TextView lblCardDepartamento, lblCardArea, lblCardEmpleado;
    private Spinner sp_departamento, sp_area;

    private AdapterEntrega adapterEntrega;
    private RecyclerView rvEntrega;
    private List<EntregaItem> listEntregas = new ArrayList<>();
    private boolean isDepartamento = false, isArea = false, isEmpleado = false;
    private Empleado empleado;
    private Departamento departamento;
    private Puesto puesto;

    private int positionFecha = 0;
    private Handler handler = new Handler();


    public EntregaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_entrega, container, false);
        context = getActivity();
        initializeToolbar();
        startWidgets();
        onActionClickListener();
        return view;
    }

    private void initializeToolbar() {
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
        img_main.setVisibility(View.VISIBLE);
    }

    private void startWidgets() {
        txtCedula = view.findViewById(R.id.txtCedula);
        lyData = view.findViewById(R.id.lyData);
        lyError = view.findViewById(R.id.lyError);
        lblNames = view.findViewById(R.id.lblNames);
        lblDepartamento = view.findViewById(R.id.lblDepartamento);
        lblArea = view.findViewById(R.id.lblArea);
        lblFechIngreso = view.findViewById(R.id.lblFechIngreso);
        lblError = view.findViewById(R.id.lblError);
        btnAddEntrega = view.findViewById(R.id.btnAddEntrega);
        btnAddRv = view.findViewById(R.id.btnAddRv);
        cardEmpleado = view.findViewById(R.id.cardEmpleado);
        cardArea = view.findViewById(R.id.cardArea);
        cardDepartamento = view.findViewById(R.id.cardDepartamento);
        lblCardDepartamento = view.findViewById(R.id.lblCardDepartamento);
        lblCardArea = view.findViewById(R.id.lblCardArea);
        lblCardEmpleado = view.findViewById(R.id.lblCardEmpleado);
        lyEmpleado = view.findViewById(R.id.lyEmpleado);
        lyRsEmpleado = view.findViewById(R.id.lyRsEmpleado);
        lyDepartamento = view.findViewById(R.id.lyDepartamento);
        sp_departamento = view.findViewById(R.id.sp_departamento);
        lyArea = view.findViewById(R.id.lyArea);
        sp_area = view.findViewById(R.id.sp_area);

        isDepartamento = true;
        rvEntrega = view.findViewById(R.id.rvEntrega);
        rvEntrega.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvEntrega.setHasFixedSize(true);
        adapterEntrega = new AdapterEntrega(context, listEntregas, new AdapterEntrega.OnCardClickListner() {
            @Override
            public void OnCardClicked(View view, int position) {

            }

            @Override
            public void OnCardClickedFecha(int position) {
                positionFecha = position;
                try {
                    DatePickerFragmentDialog.newInstance(DateTimeBuilder.newInstance()
                            .withMinDate(ActivityFragmentUtils.minDate().getTimeInMillis())
                            .withMaxDate(ActivityFragmentUtils.maxDate().getTimeInMillis()))
                            .show(getChildFragmentManager(), "DatePickerFragmentDialog");
                }catch (Exception e){
                    Log.e("Error-jj", e.getMessage());
                }
            }

            @Override
            public void OnCardClickedSearch(int position, String search) {
                buscarCodigoImplemento(position, search);
            }
        });
        rvEntrega.setAdapter(adapterEntrega);

        loadSpinner();

    }

    private void buscarCodigoImplemento(int position, String search) {
        ActivityFragmentUtils.hideTeclado(context, lblError);
        if (search.isEmpty()){
            ActivityFragmentUtils.ShowMessage("Debe poner un código de búsqueda", context, new ActivityFragmentUtils.onClickDialog() {
                @Override
                public void onClickDialog(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }else{
            getListImplementoByParams(search, position);
        }
    }

    private void getListImplementoByParams(String search, int position) {
        if(ActivityFragmentUtils.isConnetionNetwork(context)){
            FirebaseImplemento.getInventarioByParams(context, search, new FirebaseImplemento.FbRsImplemento() {
                @Override
                public void isSuccesError(boolean isSucces, String msg, List<Implemento> implementos) {
                    if(isSucces){
                        if(implementos.get(0).getCantidad() > 0){
                            msgDialog("Hay en stock " + implementos.get(0).getCantidad() + "");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    EntregaItem item = listEntregas.get(position);
                                    item.setDescripcion(implementos.get(0).getDescripcion());
                                    listEntregas.set(position, item);
                                    adapterEntrega.notifyDataSetChanged();
                                }
                            });
                        }else{
                            msgDialog("La cantidad de elementos es 0");
                        }
                    }else{
                        msgDialog(msg);
                    }
                }
            });
        }else{
            msgDialog("Verifique su conexión a internet");
        }
    }

    private void loadSpinner() {
        ArrayAdapter<Departamento> adapterArea = new ArrayAdapter<Departamento>(context,
                R.layout.simple_spinner_item, R.id.txtterms, Departamento.areas());
        sp_departamento.setAdapter(adapterArea);
        sp_departamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (!Departamento.areas().get(position).getDescripcion().equals("- SELECCIONE UNA OPCIÓN -")){
                        departamento = Departamento.areas().get(position);
                    }
                }catch (Exception e){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<Puesto> adapterPuesto = new ArrayAdapter<Puesto>(context,
                R.layout.simple_spinner_item, R.id.txtterms, Puesto.puestos());
        sp_area.setAdapter(adapterPuesto);
        sp_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (!Puesto.puestos().get(position).getDescripcion().equals("- SELECCIONE UNA OPCIÓN -")){
                        puesto = Puesto.puestos().get(position);
                    }
                }catch (Exception e){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onActionClickListener() {
        txtCedula.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchAction();
                return true;
            }
            return false;
        });

        btnAddRv.setOnClickListener(view -> {
            listEntregas.add(addEntrega1());
            adapterEntrega.notifyDataSetChanged();
        });

        cardDepartamento.setOnClickListener(view -> {
            cardDepartamento.setCardBackgroundColor(context.getResources().getColor(R.color.md_light_blue_500));
            lblCardDepartamento.setTextColor(context.getResources().getColor(R.color.colorWhite));
            isDepartamento = true;

            cardEmpleado.setCardBackgroundColor(context.getResources().getColor(R.color.colorBlackDark));
            lblCardEmpleado.setTextColor(context.getResources().getColor(R.color.colorWhite));
            isEmpleado = false;

            cardArea.setCardBackgroundColor(context.getResources().getColor(R.color.colorBlackDark));
            lblCardArea.setTextColor(context.getResources().getColor(R.color.colorWhite));
            isArea = false;

            lyArea.setVisibility(View.GONE);
            lyEmpleado.setVisibility(View.GONE);
            lyDepartamento.setVisibility(View.VISIBLE);
        });

        cardEmpleado.setOnClickListener(view -> {
            cardEmpleado.setCardBackgroundColor(context.getResources().getColor(R.color.md_light_blue_500));
            lblCardEmpleado.setTextColor(context.getResources().getColor(R.color.colorWhite));
            isEmpleado = true;

            cardArea.setCardBackgroundColor(context.getResources().getColor(R.color.colorBlackDark));
            lblCardArea.setTextColor(context.getResources().getColor(R.color.colorWhite));
            isArea = false;

            cardDepartamento.setCardBackgroundColor(context.getResources().getColor(R.color.colorBlackDark));
            lblCardDepartamento.setTextColor(context.getResources().getColor(R.color.colorWhite));
            isDepartamento = false;

            lyEmpleado.setVisibility(View.VISIBLE);
            lyArea.setVisibility(View.GONE);
            lyDepartamento.setVisibility(View.GONE);
        });

        cardArea.setOnClickListener(view -> {
            cardArea.setCardBackgroundColor(context.getResources().getColor(R.color.md_light_blue_500));
            lblCardArea.setTextColor(context.getResources().getColor(R.color.colorWhite));
            isArea = true;

            cardDepartamento.setCardBackgroundColor(context.getResources().getColor(R.color.colorBlackDark));
            lblCardDepartamento.setTextColor(context.getResources().getColor(R.color.colorWhite));
            isDepartamento = false;

            cardEmpleado.setCardBackgroundColor(context.getResources().getColor(R.color.colorBlackDark));
            lblCardEmpleado.setTextColor(context.getResources().getColor(R.color.colorWhite));
            isEmpleado = false;

            lyEmpleado.setVisibility(View.GONE);
            lyDepartamento.setVisibility(View.GONE);
            lyArea.setVisibility(View.VISIBLE);
        });

        btnAddEntrega.setOnClickListener(view -> {
            if (validateCampos()){
                String data = "Estás seguro de guardar";
                ActivityFragmentUtils.ShowMessage(data, context, new ActivityFragmentUtils.onClickDialog() {
                    @Override
                    public void onClickDialog(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if(ActivityFragmentUtils.isConnetionNetwork(context)){
                            FirebaseEntrega.createEntrega(context, modelEntrega(), new FirebaseEntrega.FbRsEntregas() {
                                @Override
                                public void isSuccesError(boolean isSucces, String msg, List<EntregaModel> entregaModels) {
                                    if (isSucces){
                                        msgDialog(msg);
                                    }else{
                                        msgDialog(msg);
                                    }
                                }
                            });
                        }else{
                            msgDialog("Verifica tu conexión a internet");
                        }
                    }
                });
            }
        });
    }

    private boolean validateCampos() {
        boolean respuesta = true;
        if(isDepartamento){
            if(departamento == null){
                msgDialog("Debe seleccionar un departamento");
                return false;
            }
        }else if(isArea){
            if(puesto == null){
                msgDialog("Debe seleccionar un área");
                return false;
            }
        }else if (isEmpleado){
            if(empleado == null){
                msgDialog("Debe seleccionar un empleado");
                return false;
            }
        }
        if(listEntregas == null || listEntregas.isEmpty() || listEntregas.size() < 0){
            msgDialog("Debes agregar ítems");
            respuesta = false;
        }
        return respuesta;
    }

    private void msgDialog(String msg) {
        ActivityFragmentUtils.ShowMessageDefault(msg, context, new ActivityFragmentUtils.onClickDialog() {
            @Override
            public void onClickDialog(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    private void searchAction() {
        ActivityFragmentUtils.hideTeclado(context, lblError);
        String search = txtCedula.getText().toString().trim();
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

    private void getListEmpleadoByParams(String params) {
        FirebaseEmpleado.getEmpleadosByParams(context, params, new FirebaseEmpleado.FbRsEmpleado() {
            @Override
            public void isSuccesError(boolean isSucces, String msg, List<Empleado> empleados) {
                lyRsEmpleado.setVisibility(View.VISIBLE);
                if(isSucces){
                    lyData.setVisibility(View.VISIBLE);
                    lyError.setVisibility(View.GONE);
                    empleado = empleados.get(0);
                    lblNames.setText(empleados.get(0).getNombres()+" "+empleados.get(0).getApellidos());
                    lblDepartamento.setText(empleados.get(0).getDepartamento().getDescripcion());
                    lblArea.setText(empleados.get(0).getDepartamento().getDescripcion());
                    lblFechIngreso.setText("C.I.: "+empleados.get(0).getCi());

                }else{
                    lyData.setVisibility(View.GONE);
                    lyError.setVisibility(View.VISIBLE);
                    lblError.setText("No se encuentra empleado..");
                }
            }
        });
    }

    private EntregaModel modelEntrega (){
        EntregaModel entregaModel = new EntregaModel();
        if(isDepartamento){
            entregaModel.setTipo_entrega("departamento");
            entregaModel.setDepartamento(departamento);
        }else if(isArea){
            entregaModel.setTipo_entrega("area");
            entregaModel.setPuesto(puesto);
        }else if (isEmpleado){
            entregaModel.setTipo_entrega("empleado");
            entregaModel.setEmpleado(empleado);
        }
        entregaModel.setFecha_creacion(ActivityFragmentUtils.getDateNowStr());
        entregaModel.setEntregaItems(listEntregas);
        return entregaModel;
    }

    private EntregaItem addEntrega1 (){
        EntregaItem item = new EntregaItem();
        item.setDescripcion("");
        item.setFecha("dd/mm/yyy");
        item.setNuevo(false);
        item.setReposicion(false);
        item.setMotivo_cambio("");
        item.setTalla("");
        return item;
    }

    @Override
    public void onDateSet(long date) {
        EntregaItem item = listEntregas.get(positionFecha);
        //Log.e("Error-ññ", "xx "+listEntregas.toString());
        item.setFecha(ActivityFragmentUtils.getDateOnly1(date));
        listEntregas.set(positionFecha, item);
        //Log.e("Error-ññ", "x-x "+listEntregas.toString());
        adapterEntrega.notifyDataSetChanged();
    }

    @Override
    public void onTimeSet(long timeOnly, long dateWithTime) {

    }
}