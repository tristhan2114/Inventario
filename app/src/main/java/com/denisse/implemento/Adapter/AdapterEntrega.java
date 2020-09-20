package com.denisse.implemento.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denisse.implemento.Model.Entrega.EntregaItem;
import com.denisse.implemento.R;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

public class AdapterEntrega extends RecyclerView.Adapter<AdapterEntrega.ViewHolder> {

    private List<EntregaItem> listData;
    private Context context;
    private OnCardClickListner onCardClickListner;
    private Handler handler = new Handler();

    // Adapter's Constructor
    public AdapterEntrega(Context context, List<EntregaItem> develops, OnCardClickListner onCardClickListner) {
        this.listData = develops;
        this.context = context;
        this.onCardClickListner = onCardClickListner;
    }

    public void updateData(List<EntregaItem> develops){
        listData.clear();
        listData =new ArrayList<>(develops);
        //this.listData = develops;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_entrega, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        EntregaItem item = listData.get(position);

        holder.lblFecha.setText(item.getFecha());
        holder.lblFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardClickListner.OnCardClickedFecha(position);
            }
        });

        //
        holder.cbNuevo.setChecked( (item.isNuevo()!=null && item.isNuevo())? item.isNuevo() : false );
        holder.cbNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData.get(position).setNuevo(true);
                listData.get(position).setReposicion(false);
                holder.lyCambio.setVisibility(View.GONE);
                holder.txtObservacion.setText("");
                listData.get(position).setMotivo_cambio("");
                notifyDataSetChanged();
            }
        });

        holder.cbReposicion.setChecked( (item.isReposicion()!=null && item.isReposicion())? item.isReposicion() : false );
        holder.cbReposicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData.get(position).setNuevo(false);
                listData.get(position).setReposicion(true);
                holder.lyCambio.setVisibility(View.VISIBLE);

                notifyDataSetChanged();
            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClickListner.OnCardClicked(v, position);
            }
        });

        holder.ImgEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.txtDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = holder.txtDescripcion.getText().toString();
                listData.get(position).setDescripcion(str.toUpperCase());
            }
        });

        holder.txtTalla.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = holder.txtTalla.getText().toString();
                listData.get(position).setTalla(str.toUpperCase());
            }
        });

        holder.txtObservacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String str = holder.txtObservacion.getText().toString();
                        listData.get(position).setMotivo_cambio(str.toUpperCase());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText txtDescripcion, txtObservacion, txtTalla;
        ImageView ImgEliminar;
        TextView lblFecha;
        CheckBox cbReposicion, cbNuevo;
        LinearLayout lyCambio;

        public ViewHolder(View v) {
            super(v);
            txtDescripcion = v.findViewById(R.id.txtDescripcion);
            txtObservacion = v.findViewById(R.id.txtObservacion);

            ImgEliminar = v.findViewById(R.id.ImgEliminar);
            lblFecha = v.findViewById(R.id.lblFecha);
            cbReposicion = v.findViewById(R.id.cbReposicion);
            cbNuevo = v.findViewById(R.id.cbNuevo);
            txtTalla = v.findViewById(R.id.txtTalla);
            lyCambio = v.findViewById(R.id.lyCambio);

        }
    }

    public interface OnCardClickListner {
        void OnCardClicked(View view, int position);
        void OnCardClickedFecha(int position);
    }

}