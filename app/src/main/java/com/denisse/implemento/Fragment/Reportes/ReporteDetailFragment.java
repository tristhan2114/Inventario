package com.denisse.implemento.Fragment.Reportes;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.denisse.implemento.Model.Entrega.EntregaItem;
import com.denisse.implemento.Model.Entrega.EntregaModel;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.EntregaUtils.FirebaseEntrega;
import com.fastaccess.datetimepicker.DatePickerFragmentDialog;
import com.fastaccess.datetimepicker.DateTimeBuilder;
import com.fastaccess.datetimepicker.callback.DatePickerCallback;
import com.fastaccess.datetimepicker.callback.TimePickerCallback;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.AxisValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

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

    private String tipo = "";
    private boolean isInicial = true;
    private long timeFechaInit, timeFechaFin;

    /*

    https://github.com/lecho/hellocharts-android

     */

    LineChartView lineChartView;
    String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
            "Oct", "Nov", "Dec"};
    int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18};

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

    ArrayList yAxisValues ;
    ArrayList axisValues ;
    private void lineChart(List<EntregaModel> entregaModels) {
        lineChartView = view.findViewById(R.id.chart1);

        yAxisValues = new ArrayList();
        axisValues = new ArrayList();


        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));
        Log.e("Error-lko", "llegue "+tipo+ " .-.size  "+entregaModels.size());

        for (int model = 0; model < entregaModels.size() ; ++model){
            EntregaModel entregaModel = entregaModels.get(model);

            Log.e("Error-lko", "llegue -equal-- "+entregaModel.getTipo_entrega() + " ...-... "+tipo+ " position "+model);

            if(entregaModel.getTipo_entrega().equals(tipo)){

                for (int itemReport = 0; itemReport < entregaModel.getEntregaItems().size(); ++itemReport){
                    EntregaItem entregaItem = entregaModel.getEntregaItems().get(itemReport);

                    if(entregaModel.getTipo_entrega().equals(tipo)){
                        Log.e("Error-lko", "llegue -1-- "+entregaItem.getDescripcion()+ " ..-.. "+itemReport);
                        Log.e("Error-lko", "llegue -2-- "+entregaItem.getCantidad());
                        axisValues.add(itemReport, new AxisValue(itemReport).setLabel(entregaItem.getDescripcion().replace(" ", "\n")));
                        yAxisValues.add(new PointValue(itemReport, entregaItem.getCantidad()));
                    }

                }

            }

        }
/*
        for (EntregaModel modelEntrega : entregaModels){
            if(tipo.equals("area")){
                for (int i = 0; i< modelEntrega.getEntregaItems().size(); ++i){
                    Log.e("Error-lko", "llegue -1-- "+modelEntrega.getEntregaItems().get(i).getDescripcion());
                    Log.e("Error-lko", "llegue -2-- "+modelEntrega.getEntregaItems().get(i).getCantidad());
                    axisValues.add(i, new AxisValue(i).setLabel(modelEntrega.getEntregaItems().get(i).getDescripcion()));
                    yAxisValues.add(new PointValue(i, modelEntrega.getEntregaItems().get(i).getCantidad()));
                }
            }else if(tipo.equals("departamento")){
                for (int i = 0; i< modelEntrega.getEntregaItems().size(); ++i){
                    Log.e("Error-lko", "llegue -31-- "+modelEntrega.getEntregaItems().get(i).getDescripcion());
                    Log.e("Error-lko", "llegue -32-- "+modelEntrega.getEntregaItems().get(i).getCantidad());
                    axisValues.add(i, new AxisValue(i).setLabel(modelEntrega.getEntregaItems().get(i).getDescripcion()));
                    yAxisValues.add(new PointValue(i, modelEntrega.getEntregaItems().get(i).getCantidad()));
                }
            }

        }*/

        /*for (int i = 0; i < axisData.length; i++) {
             axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }*/

        Log.e("Error-lko", "llegue -1544-- "+yAxisValues.toString()+ " ..-.. "+axisValues.get(0).toString());

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("Sales in millions");
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 20;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }

    private void getDataIntent() {
        Bundle bundle = getArguments();
        if(bundle != null){
            tipo = (String) bundle.get("tipo");

            String titulo = (String) bundle.get("titulo");
            lblHome.setText(titulo);
            lblHome.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        }
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

    }

    private void startWidgets() {
        lblFechaInicial =  view.findViewById(R.id.lblFechaInicial);
        lblFechaFin =  view.findViewById(R.id.lblFechaFin);
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
    }

    void getImplementos() {
        if(ActivityFragmentUtils.isConnetionNetwork(context)){
            FirebaseEntrega.getEntregas(context, new FirebaseEntrega.FbRsEntregas() {
                @Override
                public void isSuccesError(boolean isSucces, String msg, List<EntregaModel> entregaModels) {
                    Log.e("Error-",".1. "+isSucces+ " .. -  "+msg);
                    if(isSucces){
                        msgDialogo(msg);
                    }else{
                        Log.e("Error-", "dbo2 "+entregaModels.toString());
                        //generateData1(entregaModels);
                        lineChart(entregaModels);
                    }
                }
            });
        }else{
            msgDialogo("Verifica tu conexión a internet");
        }
    }

    private void generateData1(List<EntregaModel> entregaModels ) {
        columns = new ArrayList<Column>();
        values = new ArrayList<SubcolumnValue>();

        for (EntregaModel modelEntrega : entregaModels){

            if(modelEntrega.getTipo_entrega().equals("area")){
                for (EntregaItem items : modelEntrega.getEntregaItems()){
                    SubcolumnValue subcolumnValue = new SubcolumnValue();
                    subcolumnValue.setColor(ChartUtils.pickColor());
                    subcolumnValue.setLabel(items.getDescripcion());
                    subcolumnValue.setValue((float)items.getCantidad());
                    values.add(subcolumnValue);
                }
            }else if(modelEntrega.getTipo_entrega().equals("departamento")){
                for (EntregaItem items : modelEntrega.getEntregaItems()){
                    SubcolumnValue subcolumnValue = new SubcolumnValue();
                    subcolumnValue.setColor(ChartUtils.pickColor());
                    subcolumnValue.setLabel(items.getDescripcion());
                    //subcolumnValue.setValue(Float.parseFloat(String.valueOf(items.getCantidad())));
                    subcolumnValue.setValue((float)items.getCantidad());
                    values.add(subcolumnValue);
                }
            }

        }

        Column column = new Column(values);
        column.setHasLabels(true);
        column.setHasLabelsOnlyForSelected(true);
        columns.add(column);

        data = new ColumnChartData(columns);


        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);

        if(tipo.equals("area")){
            axisX.setName("Área");
            axisY.setName("Cantidad");
        }else if(tipo.equals("departamento")){
            axisX.setName("Departamento");
            axisY.setName("Cantidad");
        }else if(tipo.equals("reposiciones")){
            axisX.setName("Área");
            axisY.setName("Cantidad");
        }

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