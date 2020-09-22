package com.denisse.implemento.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
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

        if(item.getDescripcion().equals("código")){
            holder.txtDescripcion.setHint(item.getDescripcion());
            holder.txtCantidad.setEnabled(false);
        }else{
            holder.txtDescripcion.setText(item.getDescripcion());
            holder.txtCantidad.setEnabled(true);
        }

        holder.txtCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Log.e("Error-jj", "{- "+holder.txtCantidad.getText().toString());
                            if (Pattern.compile("[0-9]").matcher(holder.txtCantidad.getText().toString()).find()) {
                                Log.e("Error-jj", "2- "+holder.txtCantidad.getText().toString());
                                int cant = Integer.parseInt(holder.txtCantidad.getText().toString());
                                //int cant = Integer.parseInt(editable.toString());
                                if(cant <= item.getCantidad()){
                                    Log.e("Error-jj", "ok ");
                                    listData.get(position).setCantidad(cant);
                                }else{
                                    Log.e("Error-jj", "else ");
                                    //holder.txtCantidad.setText("");
                                    msgDialog("Debe ser menor a "+item.getCantidad(), holder.txtCantidad, context);
                                }
                            }else{
                                //holder.txtCantidad.setText("");
                            }
                        }
                    });
                }catch (Exception e){
                    Log.e("Error-jj", "- "+e.getMessage());
                }
            }
        });

        //
        holder.cbNuevo.setChecked( (item.getNuevo()!=null && item.getNuevo())? item.getNuevo() : false );
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

        holder.cbReposicion.setChecked( (item.getReposicion()!=null && item.getReposicion())? item.getReposicion() : false );
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

        holder.txtDescripcion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = holder.txtDescripcion.getText().toString().trim();
                    onCardClickListner.OnCardClickedSearch(position, search);
                    return true;
                }
                return false;
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

    private void msgDialog(String msg, EditText txtCantidad, Context context) {
        ActivityFragmentUtils.ShowMessage(msg, this.context, new ActivityFragmentUtils.onClickDialog() {
            @Override
            public void onClickDialog(DialogInterface dialog, int id) {
                ActivityFragmentUtils.hideTeclado(context, new TextView(context));
                txtCantidad.setText("");
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText txtDescripcion, txtObservacion, txtTalla, txtCantidad;
        ImageView ImgEliminar;
        TextView lblFecha;
        CheckBox cbReposicion, cbNuevo;
        LinearLayout lyCambio;

        public ViewHolder(View v) {
            super(v);
            txtDescripcion = v.findViewById(R.id.txtDescripcion);
            txtObservacion = v.findViewById(R.id.txtObservacion);
            txtCantidad = v.findViewById(R.id.txtCantidad);

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
        void OnCardClickedSearch(int position, String search);
    }

}