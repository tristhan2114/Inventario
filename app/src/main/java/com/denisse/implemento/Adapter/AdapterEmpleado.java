package com.denisse.implemento.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Model.Empleado.Empleado;

import java.util.List;

public class AdapterEmpleado extends RecyclerView.Adapter<AdapterEmpleado.ViewHolder> {

    private List<Empleado> listData;
    private Context context;
    private OnCardClickListner onCardClickListner;

    // Adapter's Constructor
    public AdapterEmpleado(Context context, List<Empleado> develops, OnCardClickListner onCardClickListner) {
        this.listData = develops;
        this.context = context;
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_empleado, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Empleado item = listData.get(position);
        //Log.v("wsdd",item.getDesciption());
        holder.lblArea.setText( (item.getDepartamento()!=null)? item.getDepartamento().getDescripcion() : "" );
        holder.lblJornada.setText(Html.fromHtml("Jornada: "+item.getJornada().getDescripcion()));
        String name = "";
        if(item.getNombres() != null && item.getNombres().length()>0){
            name += item.getNombres()+ " \n";
        }
        if(item.getApellidos() != null && item.getApellidos().length()>0){
            name += item.getApellidos()+ " ";
        }
        holder.lblNames.setText(name);
        holder.lblCedula.setText("C.I. "+item.getCi());
        holder.img_empleado.setImageBitmap(ActivityFragmentUtils.getBitmapBase64(context, item.getFoto()));
        holder.img_empleado.setScaleType(ImageView.ScaleType.CENTER_CROP);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClickListner.OnCardClicked(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lblArea, lblJornada, lblNames, lblCedula;
        ImageView img_empleado;

        public ViewHolder(View v) {
            super(v);
            lblArea = v.findViewById(R.id.lblArea);
            lblJornada = v.findViewById(R.id.lblJornada);
            img_empleado = v.findViewById(R.id.img_empleado);
            lblCedula = v.findViewById(R.id.lblCedula);

            lblNames = v.findViewById(R.id.lblNames);
        }
    }

    public interface OnCardClickListner {
        void OnCardClicked(View view, int position);
    }

}