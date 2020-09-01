package com.denisse.inventario.Fragment.Empleado;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.denisse.inventario.Model.Empleado.Area;
import com.denisse.inventario.Model.Empleado.Empleado;
import com.denisse.inventario.Model.Empleado.Genero;
import com.denisse.inventario.Model.Empleado.Jornada;
import com.denisse.inventario.R;
import com.denisse.inventario.Utils.ActivityFragmentUtils;
import com.denisse.inventario.Utils.EmpleadoUtils.FirebaseEmpleado;

import java.util.List;

public class EmpleadoCreateFragment extends Fragment {

    private Context context;
    private View view;

    private ImageButton btnBack;
    private ImageView img_main, img_empleado, btnChangeImg;
    private Button btnSearch;
    private TextView lblActionBtn;
    private CardView btnAddEmpleado;

    private Spinner sp_genero, sp_area, sp_jornada;
    private EditText txtNombres, txtApellidos, txtCedula, txtEdad, txtSearch;
    private CheckBox cbStarus;

    private String name = "", base64 = "";
    private final int GALLERY_REQUEST_CODE = 1;
    private boolean isSpinner = false;
    private Empleado empleadoData, empleado;
    private Area area;
    private Jornada jornada;
    private Genero genero;

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

                break;
            case "Actualizar":
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

        if(isSpinner){
            getIndex(sp_jornada, empleadoData.getJornada().getDescripcion());
            getIndex(sp_genero, empleadoData.getGenero().getDescripcion());
            getIndex(sp_area, empleadoData.getArea().getDescripcion());
            cbStarus.setChecked(empleadoData.isEstado());
            if(empleadoData.getFoto() != null && !empleadoData.getFoto().equals("-")){
                base64 = empleadoData.getFoto();
                img_empleado.setImageBitmap(ActivityFragmentUtils.getBitmapBase64(context, empleadoData.getFoto()));
            }
        }
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
        btnChangeImg = view.findViewById(R.id.btnChangeImg);
        cbStarus = view.findViewById(R.id.cbStarus);
        img_empleado = view.findViewById(R.id.img_empleado);
        btnAddEmpleado = view.findViewById(R.id.btnAddEmpleado);
        sp_genero = view.findViewById(R.id.sp_genero);
        sp_area = view.findViewById(R.id.sp_area);
        sp_jornada = view.findViewById(R.id.sp_jornada);
        loadDataSpinner();
        txtNombres = view.findViewById(R.id.txtNombres);
        txtApellidos = view.findViewById(R.id.txtApellidos);
        txtCedula = view.findViewById(R.id.txtCedula);
        txtEdad = view.findViewById(R.id.txtEdad);
    }

    private void loadDataSpinner() {
        ArrayAdapter<Area> adapterArea = new ArrayAdapter<Area>(context,
                R.layout.simple_spinner_item, R.id.txtterms, Area.areas());
        sp_area.setAdapter(adapterArea);
        sp_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (!Area.areas().get(position).getDescripcion().equals("- SELECCIONE UNA OPCIÓN -")){
                        area = Area.areas().get(position);
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
                if(validate()){
                    String msg = isSpinner ? "<b>Se procederá a actualizar registro</b>" : "<b>Se procederá a guardar registro</b>";
                    String msgConfirm = "<br>"+
                            txtNombres.getText().toString()+" "+txtApellidos.getText().toString()+"<br>"+
                            "<b>Ci:</b> "+txtCedula.getText().toString()+"<br>"+
                            "<b>Área:</b> "+area.getDescripcion()+"<br>"+
                            "<b>Jornada:</b> "+jornada.getDescripcion()+"<br>";
                    ActivityFragmentUtils.ShowMessage(msg+msgConfirm, context, new ActivityFragmentUtils.onClickDialog() {
                        @Override
                        public void onClickDialog(DialogInterface dialog, int id) {
                            dialog.dismiss();
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
                        }
                    });
                }
            }
        });
    }

    Empleado createDataEmpleado(boolean isUpdate) {
        empleado = new Empleado();
        empleado.setId(isUpdate ? empleadoData.getId() : "0");
        empleado.setNombres(txtNombres.getText().toString());
        empleado.setApellidos(txtApellidos.getText().toString());
        empleado.setCi(txtCedula.getText().toString());
        empleado.setFoto(base64.isEmpty() ? "-" : base64.trim());
        empleado.setArea(area);
        empleado.setJornada(jornada);
        empleado.setGenero(genero);
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
        }else if(area == null){
            msgDlg("Debe seleccionar un área del empleado");
        }else if(jornada == null){
            msgDlg("Debe seleccionar un jornada del empleado");
        }else if(genero == null){
            msgDlg("Debe seleccionar un genero del empleado");
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