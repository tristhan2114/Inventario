package com.denisse.implemento.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denisse.implemento.Model.Implemento;
import com.denisse.implemento.R;

import java.util.List;

public class AdapterReporteStock extends RecyclerView.Adapter<AdapterReporteStock.ViewHolder> {

    private List<Implemento> listData;
    private Context context;
    private Boolean isDelete = false;
    private OnCardClickListner onCardClickListner;

    // Adapter's Constructor
    public AdapterReporteStock(Context context, List<Implemento> develops, OnCardClickListner onCardClickListner) {
        this.listData = develops;
        this.context = context;
        this.onCardClickListner = onCardClickListner;
    }

    public AdapterReporteStock(Context context, List<Implemento> develops, boolean isDelete) {
        this.listData = develops;
        this.context = context;
        this.isDelete = isDelete;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_inventario, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Implemento item = listData.get(position);
        if (isDelete != null && isDelete){
            holder.lyEliminar.setVisibility(View.GONE);
        }
        holder.lblCodigo.setText(item.getCodigo());
        holder.lblArea.setText( (item.getDescripcion()!=null)? item.getDescripcion() : "");
        holder.lblDescripcion.setText(Html.fromHtml("<font color='#2a2a2a'>Cantidad : </font>"+item.getCantidad()));

        if (isDelete != null && !isDelete){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClickListner.OnCardClicked(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lblCodigo, lblArea, lblDescripcion;
        LinearLayout lyEliminar;

        public ViewHolder(View v) {
            super(v);
            lblCodigo = v.findViewById(R.id.lblCodigo);
            lblArea = v.findViewById(R.id.lblArea);
            lblDescripcion = v.findViewById(R.id.lblDescripcion);
            lyEliminar = v.findViewById(R.id.lyEliminar);
        }
    }

    public interface OnCardClickListner {
        void OnCardClicked(View view, int position);
    }

}