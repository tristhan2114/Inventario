package com.denisse.implemento.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denisse.implemento.Model.Entrega.EntregaModel;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.Alarma.AlarmaUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterEntregaList extends RecyclerView.Adapter<AdapterEntregaList.ViewHolder> {

    private List<EntregaModel> listData;
    private Context context;
    private OnCardClickListner onCardClickListner;
    private Handler handler = new Handler();

    public AdapterEntregaList(Context context, List<EntregaModel> develops, OnCardClickListner onCardClickListner) {
        this.listData = develops;
        this.context = context;
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_entrega_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EntregaModel item = listData.get(position);

        holder.lblTipoEntrega.setText("Entrega a "+item.getTipo_entrega());
        holder.lblFechaCreacion.setText("Creado: "+getFechaFormat(item.getFecha_creacion()));

        String nameEntrega = "";
        if(item.getTipo_entrega().equals("empleado")){
            nameEntrega = item.getEmpleado().getNombres() + " "+ item.getEmpleado().getApellidos();
        }else if(item.getTipo_entrega().equals("departamento")){
            nameEntrega = item.getDepartamento().getDescripcion();
        }else if(item.getTipo_entrega().equals("area")){
            nameEntrega = item.getPuesto().getDescripcion();
        }
        holder.lblNameEntrega.setText(nameEntrega);

        holder.lblFechaEntrega.setText("Fecha de entrega: "+getFechaFormat(item.getFecha_entrega()));
        String estado = "Estado: ";
        if(item.getIs_create()!=null && item.getIs_create()){
            estado += "creado";
        }
        if(item.getIs_entregado()!=null && item.getIs_entregado()){
            estado += ", entregado";
            holder.btnRealizaEntrega.setVisibility(View.GONE);
        }
        holder.lblEstado.setText(estado);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardClickListner.OnCardClicked(position);
            }
        });

        holder.btnRealizaEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fecha_actual = ActivityFragmentUtils.getDateNowFB1();
                String fecha_entrega = item.getFecha_entrega();

                String msg = "¿Seguro quieres realizar entrega? <br>"+
                        "Fecha de entrega: "+fecha_entrega+"<br>"+
                        "Fecha actual: "+fecha_actual.substring(0, 10);
                ActivityFragmentUtils.ShowMessageDefault(msg, context, new ActivityFragmentUtils.onClickDialog() {
                    @Override
                    public void onClickDialog(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        try {
                            //Log.e("Error-po", " "+fecha_actual+ " " + fecha_entrega);
                            SimpleDateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy");
                            Date date1 = dateFormat.parse(fecha_actual.substring(0, 10));
                            Date date2 = dateFormat.parse(fecha_entrega.substring(0, 10));

                            if(fecha_actual.equals(fecha_actual)){
                                // es hoy
                                AlarmaUtils.updateAlarma(item);
                                onCardClickListner.updateList(true);
                            }else{
                                //comprueba si es que entrega esta después que fecha actual
                                if(date2.after(date1)) {
                                    AlarmaUtils.updateAlarma(item);
                                    onCardClickListner.updateList(true);
                                }
                            }

                        }catch (Exception e){

                        }

                    }
                });
            }
        });

    }


    public static String getFechaFormat(String fecha) {
        String respuesta = "";
        try {
            String fec[] = fecha.split(" ");
            fecha = fec[0];
            respuesta =
                    ActivityFragmentUtils.ucFirst(ActivityFragmentUtils.getDayNameNumber(fecha, "day_name")).substring(0,3) +" "+
                            ActivityFragmentUtils.getDayNameNumber(fecha, "day_number") +", "+
                            ActivityFragmentUtils.getDayNameNumber(fecha, "Month").substring(0,3) +" "+
                            ActivityFragmentUtils.getDayNameNumber(fecha, "years");
        }catch (Exception e){
            Log.e("Error-dsds", "..- "+e.getMessage());
            respuesta = fecha;
        }
        return respuesta;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lblTipoEntrega, lblFechaCreacion, lblNameEntrega, lblFechaEntrega, lblEstado;
        CardView btnRealizaEntrega;

        public ViewHolder(View v) {
            super(v);
            lblTipoEntrega = v.findViewById(R.id.lblTipoEntrega);
            lblFechaCreacion = v.findViewById(R.id.lblFechaCreacion);
            lblNameEntrega = v.findViewById(R.id.lblNameEntrega);
            lblFechaEntrega = v.findViewById(R.id.lblFechaEntrega);
            lblEstado = v.findViewById(R.id.lblEstado);
            btnRealizaEntrega = v.findViewById(R.id.btnRealizaEntrega);

        }
    }

    public interface OnCardClickListner {
        void OnCardClicked(int position);
        void updateList(boolean status);
    }

}