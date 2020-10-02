package com.denisse.implemento.Fragment.Implementos;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.denisse.implemento.R;
import com.denisse.implemento.Model.Implemento;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.ImplementoUtils.FirebaseImplemento;
import com.fastaccess.datetimepicker.DatePickerFragmentDialog;
import com.fastaccess.datetimepicker.DateTimeBuilder;
import com.fastaccess.datetimepicker.callback.DatePickerCallback;
import com.fastaccess.datetimepicker.callback.TimePickerCallback;

import java.util.List;

public class InventarioCreateFragment extends Fragment implements DatePickerCallback, TimePickerCallback {

    private Context context;
    private View view;

    private ImageButton btnBack;
    private ImageView img_main;
    private Button btnSearch;
    private TextView lblActionBtn, txtFechRegistro, lblTileToolBar;

    private EditText txtSearch, txtCodigo, txtDescripcion, txtColor, txtVidaUtil, txtCantidad, txtTalla;
    private CardView btnAddEmpleado;

    private String name = "";

    private Implemento implementoData;

    public InventarioCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inventario_create, container, false);
        context = getActivity();
        initializeToolbar();
        startWidgets();
        getDataIntent();
        onActionListener();
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
        img_main.setVisibility(View.GONE);
        lblTileToolBar = view.findViewById(R.id.lblTileToolBar);
        lblTileToolBar.setVisibility(View.VISIBLE);
        lblTileToolBar.setText("Actualizar Implemento");
    }

    private void startWidgets() {
        lblActionBtn = view.findViewById(R.id.lblActionBtn);
        txtCodigo = view.findViewById(R.id.txtCodigo);
        txtDescripcion = view.findViewById(R.id.txtDescripcion);
        txtFechRegistro = view.findViewById(R.id.txtFechRegistro);
        txtTalla = view.findViewById(R.id.txtTalla);
        txtCantidad = view.findViewById(R.id.txtCantidad);
        txtVidaUtil = view.findViewById(R.id.txtVidaUtil);
        txtColor = view.findViewById(R.id.txtColor);
        btnAddEmpleado = view.findViewById(R.id.btnAddEmpleado);

        txtCodigo.setEnabled(false);
        txtDescripcion.setEnabled(false);


        txtFechRegistro.setOnClickListener(new View.OnClickListener() {
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

    private void getDataIntent() {
        Bundle bundle = getArguments();
        if(bundle != null){
            name = (String)bundle.getSerializable("name");
            implementoData = (Implemento) bundle.getSerializable("inventarioData");
            //Log.e("Error-","..- "+inventarioData.toString());
            lblActionBtn.setText(name);
            setterData();
        }
    }

    private void setterData() {
        txtCodigo.setText(implementoData.getCodigo());
        txtCodigo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        txtDescripcion.setText(implementoData.getDescripcion());
        txtDescripcion.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        txtColor.setText( (implementoData.getColor()!=null)? implementoData.getColor() : "" );
        txtTalla.setText( (implementoData.getTalla()!=null)? implementoData.getTalla() : "" );
        txtFechRegistro.setText( (implementoData.getFecha_registro()!=null)? implementoData.getFecha_registro() : "" );
        txtVidaUtil.setText( (implementoData.getVida_util()!=null)? implementoData.getVida_util() : "" );
        txtCantidad.setText( (implementoData.getCantidad()>0)? String.valueOf(implementoData.getCantidad()) : "" );
    }

    private void onActionListener() {
        btnAddEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityFragmentUtils.hideTeclado(context, lblActionBtn);
                if(validateCampos()){
                    FirebaseImplemento.updateInventario(context, inventarioUpdate(), new FirebaseImplemento.FbRsImplemento() {
                        @Override
                        public void isSuccesError(boolean isSucces, String msg, List<Implemento> implementos) {
                            if(isSucces){
                                msgDialog(msg);
                            }else{
                                msgDialog(msg);
                            }
                        }
                    });
                }
            }
        });
    }

    public Implemento inventarioUpdate(){
        Implemento item = new Implemento();
        item.setId(implementoData.getId());
        item.setCodigo(implementoData.getCodigo());
        item.setDescripcion(implementoData.getDescripcion().toUpperCase());
        item.setColor(txtColor.getText().toString().toUpperCase());
        item.setTalla(txtTalla.getText().toString().toUpperCase());
        item.setVida_util(txtVidaUtil.getText().toString().toUpperCase());
        item.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
        item.setFecha_registro(txtFechRegistro.getText().toString());

        return item;
    }

    boolean validateCampos(){
        boolean respuesta = false;
        if(txtColor.getText().toString().isEmpty()){
            msgDialog("Campo color requerido");
            respuesta = false;
        }else if(txtVidaUtil.getText().toString().isEmpty()){
            msgDialog("Campo vida útil requerido");
            respuesta = false;
        }else if(txtFechRegistro.getText().toString().isEmpty()){
            msgDialog("Campo fecha requerido");
            respuesta = false;
        }else if(txtCantidad.getText().toString().isEmpty()) {
            msgDialog("Campo cantidad requerido");
            respuesta = false;
        }else{
            respuesta = true;
        }
        return respuesta;
    }

    private void msgDialog(String msg) {
        ActivityFragmentUtils.ShowMessage(msg, context, new ActivityFragmentUtils.onClickDialog() {
            @Override
            public void onClickDialog(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onDateSet(long date) {
        Log.e("Error-ttt", "ee "+ActivityFragmentUtils.getDateOnly(date));
        txtFechRegistro.setText(""+ActivityFragmentUtils.getDateOnly1(date));
        txtFechRegistro.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
    }

    @Override
    public void onTimeSet(long timeOnly, long dateWithTime) {
        Log.e("Error-fdf", "-- "+String.format("Full Date: %s\nTime Only: %s", ActivityFragmentUtils.getDateAndTime(dateWithTime), ActivityFragmentUtils.getTimeOnly(timeOnly)));
    }

}