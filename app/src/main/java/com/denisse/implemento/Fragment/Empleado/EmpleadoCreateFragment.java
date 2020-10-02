package com.denisse.implemento.Fragment.Empleado;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.denisse.implemento.Model.Empleado.Departamento;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.EmpleadoUtils.FirebaseEmpleado;
import com.denisse.implemento.Model.Empleado.Empleado;
import com.denisse.implemento.Model.Empleado.Genero;
import com.denisse.implemento.Model.Empleado.Jornada;
import com.denisse.implemento.Model.Empleado.Puesto;
import com.denisse.implemento.Utils.ActivityFragmentUtils;

import java.util.List;

public class EmpleadoCreateFragment extends Fragment {

    private Context context;
    private View view;

    private ImageButton btnBack;
    private ImageView img_main, img_empleado, btnChangeImg;
    private Button btnSearch;
    private TextView lblActionBtn, lblTileToolBar;
    private CardView btnAddEmpleado;

    private Spinner sp_genero, sp_area, sp_jornada, sp_Puesto;
    private EditText txtNombres, txtApellidos, txtCedula, txtEdad, txtSearch;
    private CheckBox cbStarus;

    private String name = "", base64 = "";
    private final int GALLERY_REQUEST_CODE = 1;
    private boolean isSpinner = false;
    private Empleado empleadoData, empleado;
    private Departamento departamento;
    private Jornada jornada;
    private Genero genero;
    private Puesto puesto;

    public EmpleadoCreateFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_empleado_create, container, false);
        context = getActivity();
        initializeToolbar();
        startWidgets();
        getDataIntent();
        onActionClisListener();
        return view;
    }

    private void getDataIntent() {
        lblActionBtn = view.findViewById(R.id.lblActionBtn);
        Bundle bundle = getArguments();
        if(bundle != null){
            name = (String)bundle.getSerializable("name");
        }

        switch (name){
            case "Guardar":
                lblTileToolBar.setText("Crear Empleado");
                break;
            case "Actualizar":
                lblTileToolBar.setText("Actualizar empleado");
                isSpinner = true;
                empleadoData = (Empleado) bundle.getSerializable("empleadoData");
                Log.e("Error-","..- "+empleadoData.toString());
                setterData();
                break;
            default:

                break;
        }

        lblActionBtn.setText(name);

    }

    private void setterData() {
        txtNombres.setText(empleadoData.getNombres());
        txtNombres.setEnabled(false);
        txtNombres.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        txtApellidos.setText(empleadoData.getApellidos());
        txtApellidos.setEnabled(false);
        txtApellidos.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        txtCedula.setText(empleadoData.getCi());
        txtCedula.setEnabled(false);
        txtCedula.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        txtEdad.setText(String.valueOf(empleadoData.getEdad()));
        txtEdad.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        txtCedula.setEnabled(false);
        txtEdad.setEnabled(false);
        sp_genero.setEnabled(false);
        sp_jornada.setEnabled(false);
        sp_Puesto.setEnabled(false);
        if(isSpinner){
            getIndex(sp_jornada, empleadoData.getJornada().getDescripcion());
            getIndex(sp_genero, empleadoData.getGenero().getDescripcion());
            getIndex(sp_area, empleadoData.getDepartamento().getDescripcion());
            getIndex(sp_Puesto, empleadoData.getPuesto().getDescripcion());
            cbStarus.setChecked(empleadoData.isEstado());
            if(empleadoData.getFoto() != null && !empleadoData.getFoto().equals("-")){
                base64 = empleadoData.getFoto();
                img_empleado.setImageBitmap(ActivityFragmentUtils.getBitmapBase64(context, empleadoData.getFoto()));
            }
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
    }

    private void startWidgets() {
        btnChangeImg = view.findViewById(R.id.btnChangeImg);
        cbStarus = view.findViewById(R.id.cbStarus);
        img_empleado = view.findViewById(R.id.img_empleado);
        btnAddEmpleado = view.findViewById(R.id.btnAddEmpleado);
        sp_genero = view.findViewById(R.id.sp_genero);
        sp_area = view.findViewById(R.id.sp_area);
        sp_jornada = view.findViewById(R.id.sp_jornada);
        sp_Puesto = view.findViewById(R.id.sp_Puesto);
        loadDataSpinner();
        txtNombres = view.findViewById(R.id.txtNombres);
        txtApellidos = view.findViewById(R.id.txtApellidos);
        txtCedula = view.findViewById(R.id.txtCedula);
        txtEdad = view.findViewById(R.id.txtEdad);
    }

    private void loadDataSpinner() {
        ArrayAdapter<Departamento> adapterArea = new ArrayAdapter<Departamento>(context,
                R.layout.simple_spinner_item, R.id.txtterms, Departamento.areas());
        sp_area.setAdapter(adapterArea);
        sp_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        ArrayAdapter<Genero> adapterGenero = new ArrayAdapter<Genero>(context,
                R.layout.simple_spinner_item, R.id.txtterms, Genero.generos());
        sp_genero.setAdapter(adapterGenero);
        sp_genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (!Genero.generos().get(position).getDescripcion().equals("- SELECCIONE UNA OPCIÓN -")){
                        genero = Genero.generos().get(position);
                    }
                }catch (Exception e){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<Jornada> adapterJornada = new ArrayAdapter<Jornada>(context,
                R.layout.simple_spinner_item, R.id.txtterms, Jornada.jornadas());
        sp_jornada.setAdapter(adapterJornada);
        sp_jornada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (!Jornada.jornadas().get(position).getDescripcion().equals("- SELECCIONE UNA OPCIÓN -")){
                        jornada = Jornada.jornadas().get(position);
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
        sp_Puesto.setAdapter(adapterPuesto);
        sp_Puesto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private int getIndex(Spinner spinner, String myString){
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(myString)){
                spinner.setSelection(i);
                break;
            }
        }
        return index;
    }

    private void onActionClisListener(){
        cbStarus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbStarus.isChecked()) {
                    validateStatusEmpleado("Al descmarcar la casilla va a desabilitar al empleado", false);
                }
                cbStarus.setChecked(true);
            }
        });

        btnChangeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.app_name)), GALLERY_REQUEST_CODE);
            }
        });

        btnAddEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // realizo accion Guardar o Actualizar segun campo action
                ActivityFragmentUtils.hideTeclado(context, lblActionBtn);
                if(validate()){
                    String msg = isSpinner ? "<b>Se procederá a actualizar registro</b>" : "<b>Se procederá a guardar registro</b>";
                    String msgConfirm = "<br>"+
                            txtNombres.getText().toString()+" "+txtApellidos.getText().toString()+"<br>"+
                            "<b>Ci:</b> "+txtCedula.getText().toString()+"<br>"+
                            "<b>Departamento:</b> "+ departamento.getDescripcion()+"<br>"+
                            "<b>Jornada:</b> "+jornada.getDescripcion()+"<br>";
                    ActivityFragmentUtils.ShowMessage(msg+msgConfirm, context, new ActivityFragmentUtils.onClickDialog() {
                        @Override
                        public void onClickDialog(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            if(ActivityFragmentUtils.isConnetionNetwork(context)){
                                switch (name){
                                    case "Guardar":
                                        FirebaseEmpleado.checkEmpleadoExit(txtCedula.getText().toString(), new FirebaseEmpleado.FbRsEmpleado() {
                                            @Override
                                            public void isSuccesError(boolean isSucces, String msg, List<Empleado> empleados) {
                                                if(isSucces){
                                                    msgDlg("Empleado con cédula: "+txtCedula.getText().toString() + " ya existe.");
                                                }else{
                                                    FirebaseEmpleado.createEmpleado(context, createDataEmpleado(false), new FirebaseEmpleado.FbRsEmpleado() {
                                                        @Override
                                                        public void isSuccesError(boolean isSucces, String msg, List<Empleado> empleados) {
                                                            if(isSucces){
                                                                msgDlg(msg);
                                                            }else{
                                                                msgDlg(msg);
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        break;
                                    case "Actualizar":
                                        FirebaseEmpleado.updateEmpleado(context, createDataEmpleado(true), new FirebaseEmpleado.FbRsEmpleado() {
                                            @Override
                                            public void isSuccesError(boolean isSucces, String msg, List<Empleado> empleados) {
                                                if(isSucces){
                                                    msgDlg(msg);
                                                }else{
                                                    msgDlg(msg);
                                                }
                                            }
                                        });
                                        break;
                                    default:

                                        break;
                                }
                            }else {
                             msgDlg("Verifique su conexión a internet");
                            }
                        }
                    });
                }
            }
        });
    }

    void validateStatusEmpleado(String msg, boolean isChecked){
        ActivityFragmentUtils.ShowMessage(msg, context, new ActivityFragmentUtils.onClickDialog() {
            @Override
            public void onClickDialog(DialogInterface dialog, int id) {
                cbStarus.setChecked(isChecked);
            }
        });
    }

    Empleado createDataEmpleado(boolean isUpdate) {
        empleado = new Empleado();
        empleado.setId(isUpdate ? empleadoData.getId() : "0");
        empleado.setNombres(txtNombres.getText().toString().toUpperCase());
        empleado.setApellidos(txtApellidos.getText().toString().toUpperCase());
        empleado.setCi(txtCedula.getText().toString());
        empleado.setFoto(base64.isEmpty() ? "-" : base64.trim());
        empleado.setDepartamento(departamento);
        empleado.setJornada(jornada);
        empleado.setGenero(genero);
        empleado.setPuesto(puesto);
        empleado.setEstado(cbStarus.isChecked());
        empleado.setEdad(Integer.parseInt(txtEdad.getText().toString()));
        return empleado;
    }

    boolean validate(){
        boolean respuesta = false;
        if(txtNombres.getText().toString().isEmpty()){
            msgDlg("Ingrese los nombres del empleado");
        }else if(txtApellidos.getText().toString().isEmpty()){
            msgDlg("Ingrese los apellidos del empleado");
        }else if(txtCedula.getText().toString().isEmpty()){
            msgDlg("Ingrese cédula del empleado");
        }else if(txtEdad.getText().toString().isEmpty()){
            msgDlg("Ingrese edad del empleado");
        }else if(txtCedula.getText().toString().trim().length() < 10){
            msgDlg("la cédula debe tener 10 dígitos");
        }else if(departamento == null){
            msgDlg("Debe seleccionar un área del empleado");
        }else if(jornada == null){
            msgDlg("Debe seleccionar un jornada del empleado");
        }else if(genero == null){
            msgDlg("Debe seleccionar un género del empleado");
        }else if(puesto == null){
            msgDlg("Debe seleccionar un puesto del empleado");
        }else{
            respuesta = true;
        }
        return respuesta;
    }

    void msgDlg(String msg){
        ActivityFragmentUtils.ShowMessageDefault(msg, context, new ActivityFragmentUtils.onClickDialog() {
            @Override
            public void onClickDialog(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }
    // obtiene imagen de galeria
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        try {
            switch(requestCode){
                case GALLERY_REQUEST_CODE: // Galeria
                    if (resultCode == Activity.RESULT_OK && null != data) {
                        Uri cd = data.getData();
                        if (cd != null) {
                            img_empleado.setImageBitmap(ActivityFragmentUtils.getBitmapImg(context, cd));
                            base64 = ActivityFragmentUtils.getBStringBase64Img(context, img_empleado);
                        }
                    }
                    break;
            }
        }catch (Exception e){
            Log.e("Error-nn", e.getMessage());
        }
    }
}