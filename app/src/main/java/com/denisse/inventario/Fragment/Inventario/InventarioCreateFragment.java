package com.denisse.inventario.Fragment.Inventario;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.denisse.inventario.Model.Empleado.Area;
import com.denisse.inventario.Model.Empleado.Empleado;
import com.denisse.inventario.Model.Inventario;
import com.denisse.inventario.R;

public class InventarioCreateFragment extends Fragment {

    private Context context;
    private View view;

    private ImageButton btnBack;
    private ImageView img_main;
    private Button btnSearch;
    private TextView lblActionBtn;

    private EditText txtSearch, txtCodigo, txtDescripcion, txtTipo, txtColor, txtVidaUtil, txtCantidad, txtTalla, txtFechRegistro;
    private Spinner sp_area;

    private Area area;
    private String name = "";

    private Inventario inventarioData;

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
        lblActionBtn = view.findViewById(R.id.lblActionBtn);
        txtCodigo = view.findViewById(R.id.txtCodigo);
        txtDescripcion = view.findViewById(R.id.txtDescripcion);
        txtTipo = view.findViewById(R.id.txtTipo);
        sp_area = view.findViewById(R.id.sp_area);
        loadSpinner();
        txtFechRegistro = view.findViewById(R.id.txtFechRegistro);
        txtTalla = view.findViewById(R.id.txtTalla);
        txtCantidad = view.findViewById(R.id.txtCantidad);
        txtVidaUtil = view.findViewById(R.id.txtVidaUtil);
        txtColor = view.findViewById(R.id.txtColor);
    }

    private void getDataIntent() {
        Bundle bundle = getArguments();
        if(bundle != null){
            name = (String)bundle.getSerializable("name");
        }
        inventarioData = (Inventario) bundle.getSerializable("inventarioData");
        Log.e("Error-","..- "+inventarioData.toString());
        lblActionBtn.setText(name);
        setterData();
    }

    private void setterData() {
        txtCodigo.setText(inventarioData.getCodigo());
        txtCodigo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        txtDescripcion.setText(inventarioData.getDescripcion());
        txtDescripcion.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        txtTipo.setText(inventarioData.getTipo());
        txtTipo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        if(inventarioData.getArea() != null){
            getIndex(sp_area, inventarioData.getArea().getDescripcion());
        }
    }

    private void loadSpinner() {
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

}