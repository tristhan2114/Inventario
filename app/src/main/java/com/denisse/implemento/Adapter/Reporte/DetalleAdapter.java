package com.denisse.implemento.Adapter.Reporte;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.denisse.implemento.Model.Entrega.EntregaModel;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;

import java.util.List;

public class DetalleAdapter extends RecyclerView.Adapter<DetalleAdapter.ViewHolder> {

    private Context context;
    private List<EntregaModel> entregaModelList;
    private OnCardClickListner onCardClickListner;
    int row_index = -1;
    private Handler handler = new Handler();
    private boolean isFirts = true;

    public DetalleAdapter(Context context, List<EntregaModel> entregaModelList, OnCardClickListner onCardClickListner) {
        this.context = context;
        this.entregaModelList = entregaModelList;
        this.onCardClickListner = onCardClickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EntregaModel model = entregaModelList.get(position);

        String numEntrega = "", name = "";
        if(model.getEntregaItems()!=null && model.getEntregaItems().size() > 0){
            numEntrega = " ("+model.getEntregaItems().size()+")";
        }

        if(model.getTipo_entrega().equals("area")){
            name = model.getPuesto().getDescripcion();
        }else if(model.getTipo_entrega().equals("departamento")){
            name = model.getDepartamento().getDescripcion();
        }else if(model.getTipo_entrega().equals("reposiciones")){

        }

        String dato = ""+ ActivityFragmentUtils.ucFirst(name) +numEntrega+
                "\nEntrega: "+getFechaFormat(model.getFecha_entrega());

        String estado = "\nEstado: ";
        if(model.getIs_create()!=null && model.getIs_create()){
            estado += "creado";
        }
        if(model.getIs_entregado()!=null && model.getIs_entregado()){
            estado += ", entregado";
        }

        dato += estado;

        holder.txtterms.setText(dato);
        holder.txtterms.setTextColor(context.getResources().getColor(R.color.colorBlack));
        holder.txtterms.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        if(position == 0 && isFirts){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.md_light_blue_500));
                    onCardClickListner.OnCardClicked(0);
                    isFirts = false;
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                onCardClickListner.OnCardClicked(position);
                notifyDataSetChanged();
            }
        });

        if (row_index==position) {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.md_light_blue_500));
        } else {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

    }

    @Override
    public int getItemCount() {
        return entregaModelList.size();
    }

    public static String getFechaFormat(String fecha) {
        String respuesta = "";
        try {
            String fec[] = fecha.split(" ");
            fecha = fec[0];
            respuesta =
                    ActivityFragmentUtils.ucFirst(ActivityFragmentUtils.getDayNameNumber(fecha, "day_name").substring(0,3)) +". "+
                            ActivityFragmentUtils.getDayNameNumber(fecha, "day_number") +", "+
                            ActivityFragmentUtils.getDayNameNumber(fecha, "Month").substring(0,3) +". "+
                            ActivityFragmentUtils.getDayNameNumber(fecha, "years");
        }catch (Exception e){
            Log.e("Error-dsds", "..- "+e.getMessage());
            respuesta = fecha;
        }
        return respuesta;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtterms;
        CardView card;

        public ViewHolder(View v) {
            super(v);
            txtterms = v.findViewById(R.id.txtterms);
            card = v.findViewById(R.id.card);
        }

    }

    public interface OnCardClickListner {
        void OnCardClicked(int position);
    }
}
