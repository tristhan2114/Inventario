package com.denisse.implemento.Fragment.Entrega;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.regex.Pattern;

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
    private TextView lblNames, lblDepartamento, lblArea, lblFechIngreso, lblError, lblFecha, lblTileToolBar;
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
    private long time_fecha_entrega = 0;


    //todo Inicio: para proceso de agregacion de implementos de entrega
    // inicio view
    EditText txtDescripcion, txtObservacion, txtTalla, txtCantidad;
    ImageView ImgEliminar;
    CheckBox cbReposicion, cbNuevo;
    LinearLayout lyCambio;
    // fin view



    int cantidadAux = 0;

    //todo Fin: para proceso de agregacion de implementos de entrega

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
        lblFecha = view.findViewById(R.id.lblFecha);
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

        //todo: inicio componetes add entrega
        txtDescripcion = view.findViewById(R.id.txtDescripcion);
        txtObservacion = view.findViewById(R.id.txtObservacion);
        txtTalla = view.findViewById(R.id.txtTalla);
        txtCantidad = view.findViewById(R.id.txtCantidad);
        ImgEliminar = view.findViewById(R.id.ImgEliminar);
        cbReposicion = view.findViewById(R.id.cbReposicion);
        cbNuevo = view.findViewById(R.id.cbNuevo);
        lyCambio = view.findViewById(R.id.lyCambio);

        //todo: fin componetes add entrega


        isDepartamento = true;
        rvEntrega = view.findViewById(R.id.rvEntrega);
        rvEntrega.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvEntrega.setHasFixedSize(true);
        adapterEntrega = new AdapterEntrega(context, listEntregas, new AdapterEntrega.OnCardClickListner() {
            @Override
            public void OnCardClicked(int position) {

            }
        });
        rvEntrega.setAdapter(adapterEntrega);

        loadSpinner();
        processAddEntrega();

    }

    private void processAddEntrega() {
        cbNuevo.setChecked(true);
        cbReposicion.setChecked(false);
        cbNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbNuevo.setChecked(true);
                cbReposicion.setChecked(false);
                lyCambio.setVisibility(View.GONE);
                txtObservacion.setText("");
            }
        });

        cbReposicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbReposicion.setChecked(true);
                cbNuevo.setChecked(false);
                lyCambio.setVisibility(View.VISIBLE);
            }
        });

        // buscar
        txtDescripcion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = txtDescripcion.getText().toString().trim();
                    buscarCodigoImplemento(search);
                    return true;
                }
                return false;
            }
        });

        // cantidad
        txtCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (Pattern.compile("[0-9]").matcher(txtCantidad.getText().toString()).find()) {
                                int cant = Integer.parseInt(txtCantidad.getText().toString());
                                if(cant < cantidadAux){
                                }else{
                                    msgDialog("Debe ser menor a "+cantidadAux);
                                    txtCantidad.setText("");
                                }
                            }
                        }
                    });
                }catch (Exception e){
                    Log.e("Error-jj", "- "+e.getMessage());
                }
            }
        });
    }

    private void buscarCodigoImplemento(String search) {
        ActivityFragmentUtils.hideTeclado(context, lblError);
        if (search.isEmpty()){
            ActivityFragmentUtils.ShowMessage("Debe poner un código de búsqueda", context, new ActivityFragmentUtils.onClickDialog() {
                @Override
                public void onClickDialog(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }else{
            getListImplementoByParams(search);
        }
    }

    private void getListImplementoByParams(String search) {
        if(ActivityFragmentUtils.isConnetionNetwork(context)){
            FirebaseImplemento.getInventarioByParams(context, search, new FirebaseImplemento.FbRsImplemento() {
                @Override
                public void isSuccesError(boolean isSucces, String msg, List<Implemento> implementos) {
                    if(isSucces){
                        if(implementos.get(0).getCantidad() > 0){
                            msgDialog("Hay en stock " + implementos.get(0).getCantidad() + "");
                            txtDescripcion.setText(""+implementos.get(0).getDescripcion());
                            cantidadAux = implementos.get(0).getCantidad();
                            txtCantidad.setText("");
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
            ActivityFragmentUtils.hideTeclado(context, lblError);
            if(validateAddEntrega()){
                listEntregas.add(addEntrega1());
                adapterEntrega.notifyDataSetChanged();
                camposVacios();
            }
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

        lblFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DatePickerFragmentDialog.newInstance(DateTimeBuilder.newInstance()
                            .withMinDate(ActivityFragmentUtils.minDate().getTimeInMillis())
                            .withMaxDate(ActivityFragmentUtils.maxDate().getTimeInMillis()))
                            .show(getChildFragmentManager(), "DatePickerFragmentDialog");
                }catch (Exception e){
                    Log.e("Error-jj", e.getMessage());
                }
            }
        });
    }

    private void camposVacios() {
        cantidadAux = 0;
        txtDescripcion.setText("");
        txtCantidad.setText("");
        txtObservacion.setText("");
        txtTalla.setText("");
        cbNuevo.setChecked(true);
        cbReposicion.setChecked(false);
    }

    private boolean validateCampos() {
        boolean respuesta = true;
        if(isDepartamento){
            if(departamento == null){
                msgDialog("Debe seleccionar un departamento");
                sp_departamento.requestFocus();
                return false;
            }
        }else if(isArea){
            if(puesto == null){
                msgDialog("Debe seleccionar un área");
                sp_area.requestFocus();
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
        if(lblFecha.getText().toString().isEmpty() || lblFecha.getText().toString().length() < 0){
            msgDialog("Debe ingresar una fecha de entrega");
            return false;
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
        FirebaseEmpleado.getEmpleadosByParams(context, "ci", params, new FirebaseEmpleado.FbRsEmpleado() {
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
        entregaModel.setFecha_creacion(ActivityFragmentUtils.getDateNowFB1());
        entregaModel.setFecha_time_creacion(ActivityFragmentUtils.getDateNow().getTime());

        entregaModel.setFecha_entrega(lblFecha.getText().toString());
        entregaModel.setFecha_time_entrega(time_fecha_entrega);
        entregaModel.setIs_create(true);
        entregaModel.setIs_entregado(false);

        entregaModel.setEntregaItems(listEntregas);
        return entregaModel;
    }

    private EntregaItem addEntrega1 (){
        EntregaItem item = new EntregaItem();
        item.setDescripcion(txtDescripcion.getText().toString().trim());
        item.setNuevo(cbNuevo.isChecked());
        item.setReposicion(cbReposicion.isChecked());
        item.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
        item.setMotivo_cambio(txtObservacion.getText().toString());
        item.setTalla(txtTalla.getText().toString());
        return item;
    }

    boolean validateAddEntrega(){
        if(txtDescripcion.getText().toString().isEmpty() || txtDescripcion.getText().toString().length() < 0){
            txtDescripcion.requestFocus();
            msgDialog("Campo descripción de búsqueda requerido");
            return  false;
        }

        if(txtCantidad.getText().toString().isEmpty() || txtCantidad.getText().toString().length() < 0){
            txtCantidad.requestFocus();
            msgDialog("Campo cantidad requerido");
            return  false;
        }

        return true;
    }

    @Override
    public void onDateSet(long date) {
        lblFecha.setText(String.valueOf(ActivityFragmentUtils.getDateOnly1(date)));
        time_fecha_entrega = date;
    }

    @Override
    public void onTimeSet(long timeOnly, long dateWithTime) {

    }
}