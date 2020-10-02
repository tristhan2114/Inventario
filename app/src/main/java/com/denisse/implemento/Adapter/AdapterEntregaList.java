package com.denisse.implemento.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denisse.implemento.Model.Entrega.EntregaItem;
import com.denisse.implemento.Model.Entrega.EntregaModel;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
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
        }
        holder.lblEstado.setText(estado);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardClickListner.OnCardClicked(position);
            }
        });


    }


    public static String getFechaFormat(String fecha) {
        String respuesta = "";
        try {
            String fec[] = fecha.split(" ");
            fecha = fec[0];
            respuesta =
                    ActivityFragmentUtils.ucFirst(ActivityFragmentUtils.getDayNameNumber(fecha, "day_name")) +" "+
                            ActivityFragmentUtils.getDayNameNumber(fecha, "day_number") +", "+
                            ActivityFragmentUtils.getDayNameNumber(fecha, "Month") +" "+
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

        public ViewHolder(View v) {
            super(v);
            lblTipoEntrega = v.findViewById(R.id.lblTipoEntrega);
            lblFechaCreacion = v.findViewById(R.id.lblFechaCreacion);
            lblNameEntrega = v.findViewById(R.id.lblNameEntrega);
            lblFechaEntrega = v.findViewById(R.id.lblFechaEntrega);
            lblEstado = v.findViewById(R.id.lblEstado);

        }
    }

    public interface OnCardClickListner {
        void OnCardClicked(int position);
    }

}