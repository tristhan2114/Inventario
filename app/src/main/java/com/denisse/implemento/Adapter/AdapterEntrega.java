package com.denisse.implemento.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denisse.implemento.Model.Entrega.EntregaItem;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.os.Handler;

public class AdapterEntrega extends RecyclerView.Adapter<AdapterEntrega.ViewHolder> {

    private List<EntregaItem> listData;
    private Context context;
    private OnCardClickListner onCardClickListner;
    private Handler handler = new Handler();
    private boolean isEliminar = false;

    // Adapter's Constructor
    public AdapterEntrega(Context context, List<EntregaItem> develops, OnCardClickListner onCardClickListner) {
        this.listData = develops;
        this.context = context;
        this.onCardClickListner = onCardClickListner;
    }

    public AdapterEntrega(Context context, List<EntregaItem> develops, boolean isEliminar) {
        this.listData = develops;
        this.context = context;
        this.isEliminar = isEliminar;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_entrega_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        EntregaItem item = listData.get(position);


        holder.txtDescripcion.setText(item.getDescripcion());
        holder.txtCantidad.setText(""+item.getCantidad());
        //
        holder.cbNuevo.setChecked( (item.getNuevo()!=null && item.getNuevo())? item.getNuevo() : false );

        holder.cbReposicion.setChecked( (item.getReposicion()!=null && item.getReposicion())? item.getReposicion() : false );

        holder.txtTalla.setText(item.getTalla());

        holder.lyCambio.setVisibility((item.getReposicion()!=null && item.getReposicion())? View.VISIBLE : View.GONE);

        holder.txtObservacion.setText(item.getMotivo_cambio());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardClickListner.OnCardClicked(position);
            }
        });

        if (!isEliminar){
            holder.ImgEliminar.setVisibility(View.GONE);
        }else{
            holder.ImgEliminar.setVisibility(View.VISIBLE);
        }

        holder.ImgEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    private void msgDialog(String msg, EditText txtCantidad, Context context) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ActivityFragmentUtils.hideTeclado(context, new TextView(context));
                ActivityFragmentUtils.ShowMessage(msg, context, new ActivityFragmentUtils.onClickDialog() {
                    @Override
                    public void onClickDialog(DialogInterface dialog, int id) {
                        txtCantidad.setText("");
                        dialog.dismiss();
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
        TextView txtDescripcion, txtObservacion, txtTalla, txtCantidad;
        ImageView ImgEliminar;
        CheckBox cbReposicion, cbNuevo;
        LinearLayout lyCambio;

        public ViewHolder(View v) {
            super(v);
            txtDescripcion = v.findViewById(R.id.txtDescripcion);
            txtObservacion = v.findViewById(R.id.txtObservacion);
            txtCantidad = v.findViewById(R.id.txtCantidad);

            ImgEliminar = v.findViewById(R.id.ImgEliminar);
            cbReposicion = v.findViewById(R.id.cbReposicion);
            cbNuevo = v.findViewById(R.id.cbNuevo);
            txtTalla = v.findViewById(R.id.txtTalla);
            lyCambio = v.findViewById(R.id.lyCambio);

        }
    }

    public interface OnCardClickListner {
        void OnCardClicked(int position);
    }

}