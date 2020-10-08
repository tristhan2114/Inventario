package com.denisse.implemento.Fragment.Reportes;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denisse.implemento.Adapter.Reporte.DetalleAdapter;
import com.denisse.implemento.Adapter.Reporte.SubDetalleAdapter;
import com.denisse.implemento.Model.Entrega.EntregaItem;
import com.denisse.implemento.Model.Entrega.EntregaModel;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.EntregaOp.EntregasOp;
import com.denisse.implemento.Utils.EntregaUtils.FirebaseEntrega;
import com.fastaccess.datetimepicker.DatePickerFragmentDialog;
import com.fastaccess.datetimepicker.DateTimeBuilder;
import com.fastaccess.datetimepicker.callback.DatePickerCallback;
import com.fastaccess.datetimepicker.callback.TimePickerCallback;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class ReporteDetailFragment extends Fragment implements DatePickerCallback, TimePickerCallback {

    private Context context;
    private View view;

    // toolbar
    private ImageButton btnBack;
    private EditText txtSearch;
    private ImageView img_main;
    private Button btnSearch;
    private TextView lblTileToolBar, lblHome, lblFechaFin, lblFechaInicial;

    private ColumnChartView chart;
    private ColumnChartData data;
    private List<Column> columns;
    List<SubcolumnValue> values;
    SwipeRefreshLayout refresh;

    private String tipo = "", name = "";
    private boolean isInicial = true;
    private long timeFechaInit, timeFechaFin;
    private List<EntregaModel> listData = new ArrayList<>();
    private RecyclerView rv_detalle_reporte, rv_subdetalle_reporte;
    private List<Integer> colors = new ArrayList<>();
    private LinearLayout lySearch;

    /*

    https://github.com/lecho/hellocharts-android

     */

    public ReporteDetailFragment() {
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
        initToobar();
        getDataIntent();
        startWidgets();
        return view;
    }

    private void getDataIntent() {
        Bundle bundle = getArguments();
        if(bundle != null){
            tipo = (String) bundle.get("tipo");

            String titulo = (String) bundle.get("titulo");
            //lblHome.setText(titulo);
            //lblHome.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            lblTileToolBar.setText(titulo);
            lblTileToolBar.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        }
    }

    private void initToobar() {
        lySearch = view.findViewById(R.id.lySearch);
        lblHome = view.findViewById(R.id.lblHome);
        lblTileToolBar = view.findViewById(R.id.lblTileToolBar);
        lblTileToolBar = view.findViewById(R.id.lblTileToolBar);
        btnSearch = view.findViewById(R.id.btnSearch);
        img_main = view.findViewById(R.id.img_main);
        txtSearch = view.findViewById(R.id.txtSearch);
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setVisibility(View.VISIBLE);
        txtSearch.setVisibility(View.GONE);
        img_main.setVisibility(View.GONE);
        btnSearch.setVisibility(View.GONE);
        lblTileToolBar.setVisibility(View.VISIBLE);


    }

    private void startWidgets() {
        rv_subdetalle_reporte =  view.findViewById(R.id.rv_subdetalle_reporte);
        rv_detalle_reporte =  view.findViewById(R.id.rv_detalle_reporte);
        lblFechaInicial =  view.findViewById(R.id.lblFechaInicial);
        lblFechaFin =  view.findViewById(R.id.lblFechaFin);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        chart = (ColumnChartView) view.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());

        //getImplementos();

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

        lblFechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    isInicial = true;
                    DatePickerFragmentDialog.newInstance(DateTimeBuilder.newInstance()
                            .withMinDate(ActivityFragmentUtils.minDate().getTimeInMillis())
                            .withMaxDate(ActivityFragmentUtils.maxDate().getTimeInMillis()))
                            .show(getChildFragmentManager(), "DatePickerFragmentDialog");
                }catch (Exception e){
                    Log.e("Error-jj", e.getMessage());
                }
            }
        });

        lblFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    isInicial = false;
                    DatePickerFragmentDialog.newInstance(DateTimeBuilder.newInstance()
                            .withMinDate(ActivityFragmentUtils.minDate().getTimeInMillis())
                            .withMaxDate(ActivityFragmentUtils.maxDate().getTimeInMillis()))
                            .show(getChildFragmentManager(), "DatePickerFragmentDialog");
                }catch (Exception e){
                    Log.e("Error-jj", e.getMessage());
                }
            }
        });

        lySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImplementos();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    void getImplementos() {
        if(ActivityFragmentUtils.isConnetionNetwork(context)){

            if(timeFechaInit != 0 && timeFechaFin !=0){
                chart.setVisibility(View.INVISIBLE);
                FirebaseEntrega.getEntregasByParams(context, tipo, timeFechaInit, timeFechaFin, new FirebaseEntrega.FbRsEntregas() {
                    @Override
                    public void isSuccesError(boolean isSucces, String msg, List<EntregaModel> entregaModels) {
                        Log.e("Error-",".1. "+isSucces+ " .. -  "+msg);
                        if(isSucces){
                            msgDialogo(msg);
                        }else{
                            //Log.e("Error-", "dbo2 "+entregaModels.toString());
                            generateData1(entregaModels);
                        }
                    }
                });
            }else{
                msgDialogo("Debes poner fecha inicial y final");
            }


        }else{
            msgDialogo("Verifica tu conexión a internet");
        }
    }

    private void generateData1(List<EntregaModel> entregaModels ) {
        listData.clear();
        chart.setVisibility(View.VISIBLE);
        for (int model = 0; model < entregaModels.size() ; ++model){
            EntregaModel entregaModel = entregaModels.get(model);
            Log.e("Error-lko", "llegue -equal-- "+entregaModel.getTipo_entrega() + " ...-... "+tipo+ " position "+model);
            if(entregaModel.getTipo_entrega().equals(tipo)){
                listData.add(entregaModel);
            }
        }

        if(listData == null || listData.isEmpty() ||listData.size() <0){
            msgDialogo("No hay información");
            chart.setVisibility(View.INVISIBLE);
            return;
        }

        rv_detalle_reporte.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rv_detalle_reporte.setHasFixedSize(true);

        rv_subdetalle_reporte.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rv_subdetalle_reporte.setHasFixedSize(true);
        DetalleAdapter detalleAdapter = new DetalleAdapter(context, listData, new DetalleAdapter.OnCardClickListner() {
            @Override
            public void OnCardClicked(int position) {
                EntregaModel model = listData.get(position);
                if(model.getTipo_entrega().equals("area")){
                    name = model.getPuesto().getDescripcion();
                }else if(model.getTipo_entrega().equals("departamento")){
                    name = model.getDepartamento().getDescripcion();
                }else if(model.getTipo_entrega().equals("reposiciones")){

                }
                colors.clear();
                if(model.getEntregaItems()!=null && model.getEntregaItems().size()>0){
                    for (int itemReport = 0; itemReport < model.getEntregaItems().size(); ++itemReport){
                        colors.add(ChartUtils.pickColor());
                    }
                }
                createGrap(model);

                SubDetalleAdapter subDetalleAdapter = new SubDetalleAdapter(context, model.getEntregaItems(), colors, new SubDetalleAdapter.OnCardClickListner() {
                    @Override
                    public void OnCardClicked(int position) {
                        EntregaModel entregaModel = listData.get(position);
                        EntregasOp.DialogInfService(context, entregaModel.getEntregaItems());
                    }
                }) ;
                rv_subdetalle_reporte.setAdapter(subDetalleAdapter);
            }
        });
        rv_detalle_reporte.setAdapter(detalleAdapter);
    }

    void createGrap(EntregaModel entregaItem){
        columns = new ArrayList<Column>();
        values = new ArrayList<SubcolumnValue>();

        int i = 0;
        if (entregaItem.getEntregaItems()!=null && entregaItem.getEntregaItems().size()>0) {
                for (EntregaItem items : entregaItem.getEntregaItems()) {
                    SubcolumnValue subcolumnValue = new SubcolumnValue();
                    subcolumnValue.setColor(colors.get(i));
                    subcolumnValue.setLabel(items.getDescripcion());
                    subcolumnValue.setValue((float) items.getCantidad());
                    values.add(subcolumnValue);
                    i++;
                }
            }

        Column column = new Column(values);
        column.setHasLabels(true);
        column.setHasLabelsOnlyForSelected(true);
        columns.add(column);
        data = new ColumnChartData(columns);

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);

        axisX.setName(name);
        axisY.setName("Cantidad");


        axisX.setTextSize(18);
        axisX.setTextColor(context.getResources().getColor(R.color.colorBlack));
        axisY.setTextSize(18);
        axisY.setTextColor(context.getResources().getColor(R.color.colorBlack));

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        chart.setZoomEnabled(true);
        chart.setScrollEnabled(true);

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

    @Override
    public void onDateSet(long date) {
        if(isInicial){
            timeFechaInit = date;
            lblFechaInicial.setText(String.valueOf(ActivityFragmentUtils.getDateOnly1(date)));
        }else{
            timeFechaFin = date;
            lblFechaFin.setText(String.valueOf(ActivityFragmentUtils.getDateOnly1(date)));
        }
    }

    @Override
    public void onTimeSet(long timeOnly, long dateWithTime) {

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