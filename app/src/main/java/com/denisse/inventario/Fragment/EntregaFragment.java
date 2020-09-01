package com.denisse.inventario.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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

import com.denisse.inventario.Activity.ContainerActivity;
import com.denisse.inventario.Model.Empleado.Empleado;
import com.denisse.inventario.R;
import com.denisse.inventario.Utils.ActivityFragmentUtils;
import com.denisse.inventario.Utils.EmpleadoUtils.FirebaseEmpleado;

import java.util.List;

public class EntregaFragment extends Fragment {

    private Context context;
    private View view;

    private ImageButton btnBack;
    private ImageView img_main;
    private Button btnSearch;

    private CardView btnAddEntrega;
    private EditText txtSearch, txtCedula;
    private LinearLayout lyData, lyError;
    private TextView lblNames, lblDepartamento, lblArea, lblFechIngreso, lblError;


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
    }

    private void onActionClickListener() {
        txtCedula.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchAction();
                    return true;
                }
                return false;
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

    private void getListEmpleadoByParams(String params) {
        FirebaseEmpleado.getEmpleadosByParams(context, params, new FirebaseEmpleado.FbRsEmpleado() {
            @Override
            public void isSuccesError(boolean isSucces, String msg, List<Empleado> empleados) {
                if(isSucces){
                    lyData.setVisibility(View.VISIBLE);
                    lyError.setVisibility(View.GONE);

                    lblNames.setText(empleados.get(0).getNombres()+" "+empleados.get(0).getApellidos());
                    lblDepartamento.setText("empleados.get(0).getDepartamento()");
                    lblArea.setText(empleados.get(0).getArea().getDescripcion());
                    lblFechIngreso.setText("empleados.get(0).getFechaIngreso()");

                }else{
                    lyData.setVisibility(View.GONE);
                    lyError.setVisibility(View.VISIBLE);
                    lblError.setText("No se encuentra empleado..");
                }
            }
        });
    }

}