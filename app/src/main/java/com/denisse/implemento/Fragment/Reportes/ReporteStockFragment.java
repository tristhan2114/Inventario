package com.denisse.implemento.Fragment.Reportes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Toast;

import com.denisse.implemento.Activity.DetailActivity;
import com.denisse.implemento.Model.Implemento;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.ImplementoUtils.FirebaseImplemento;
import com.denisse.implemento.Utils.SharedData;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class ReporteStockFragment extends Fragment {

    private Context context;
    private View view;

    // toolbar
    private ImageButton btnBack;
    private EditText txtSearch;
    private ImageView img_main;
    private Button btnSearch;
    private TextView lblTileToolBar, lblHome;
    private SharedPreferences sharedPreferences;

    private ColumnChartView chart;
    private ColumnChartData data;
    private List<Column> columns;
    List<SubcolumnValue> values;
    SwipeRefreshLayout refresh;

    public ReporteStockFragment() {
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
        view = inflater.inflate(R.layout.fragment_reporte_detail, container, false);
        context = getActivity();
        sharedPreferences = context.getSharedPreferences(SharedData.KEYPREFERENCES, Context.MODE_PRIVATE);
        initToobar();
        startWidgets();
        return view;
    }

    private void initToobar() {
        lblHome = view.findViewById(R.id.lblHome);
        lblTileToolBar = view.findViewById(R.id.lblTileToolBar);
        lblTileToolBar = view.findViewById(R.id.lblTileToolBar);
        btnSearch = view.findViewById(R.id.btnSearch);
        img_main = view.findViewById(R.id.img_main);
        txtSearch = view.findViewById(R.id.txtSearch);
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setVisibility(View.GONE);
        txtSearch.setVisibility(View.GONE);
        img_main.setVisibility(View.GONE);
        btnSearch.setVisibility(View.GONE);
        lblTileToolBar.setVisibility(View.VISIBLE);
        lblTileToolBar.setText("");
        lblTileToolBar.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

        String user = (SharedData.getKey(context, SharedData.NAME_USER)!=null)?SharedData.getKey(context, SharedData.NAME_USER) : "";
        lblHome.setText("Bienvenido, "+ user);
        lblHome.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
    }

    private void startWidgets() {
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        chart = (ColumnChartView) view.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());

        getImplementos();

        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorWhite));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getImplementos();
                refresh.setRefreshing(false);
            }
        });
    }

    void getImplementos() {
        if(ActivityFragmentUtils.isConnetionNetwork(context)){
            FirebaseImplemento.getInventarioReporte(context, new FirebaseImplemento.FbRsImplemento() {
                @Override
                public void isSuccesError(boolean isSucces, String msg, List<Implemento> implementos) {
                    Log.e("Error-",".1. "+isSucces+ " .. -  "+msg);
                    if(isSucces){
                        generateData(implementos);
                    }else{
                        msgDialogo(msg);
                    }
                }
            });
        }else{
            msgDialogo("Verifica tu conexión a internet");
        }
    }

    private void generateData(List<Implemento> implementos) {
        columns = new ArrayList<Column>();
        values = new ArrayList<SubcolumnValue>();

        for (Implemento implemento : implementos){
            SubcolumnValue subcolumnValue = new SubcolumnValue();
            subcolumnValue.setColor(ChartUtils.pickColor());
            subcolumnValue.setLabel(implemento.getDescripcion());
            subcolumnValue.setValue(implemento.getCantidad());
            values.add(subcolumnValue);
        }
        Column column = new Column(values);
        column.setHasLabels(true);
        column.setHasLabelsOnlyForSelected(true);
        columns.add(column);

        data = new ColumnChartData(columns);

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("Implementos");
        axisY.setName("Cantidad");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        chart.setColumnChartData(data);

    }

    private void msgDialogo(String msg){
        ActivityFragmentUtils.ShowMessageDefault(msg, context, new ActivityFragmentUtils.onClickDialog() {
            @Override
            public void onClickDialog(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }


    private class ValueTouchListener implements ColumnChartOnValueSelectListener {
        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            //Toast.makeText(getActivity(), "Selected: " + columns.get(0).getValues().get(subcolumnIndex).getLabelAsChars(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

}