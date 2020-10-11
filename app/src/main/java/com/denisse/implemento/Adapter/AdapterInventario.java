package com.denisse.implemento.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denisse.implemento.Model.Entrega.EntregaModel;
import com.denisse.implemento.R;
import com.denisse.implemento.Model.Implemento;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.EntregaUtils.FirebaseEntrega;

import java.util.List;

public class AdapterInventario extends RecyclerView.Adapter<AdapterInventario.ViewHolder> {

    private List<Implemento> listData;
    private Context context;
    private OnCardClickListner onCardClickListner;

    // Adapter's Constructor
    public AdapterInventario(Context context, List<Implemento> develops, OnCardClickListner onCardClickListner) {
        this.listData = develops;
        this.context = context;
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_inventario, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Implemento item = listData.get(position);
        holder.lblCodigo.setText(item.getCodigo());
        holder.lblArea.setText( (item.getDescripcion()!=null)? item.getDescripcion() : "");
        holder.lblDescripcion.setText(item.getDescripcion());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClickListner.OnCardClicked(v, position);
            }
        });

        holder.lyEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityFragmentUtils.ShowMessageDefault("¿Está seguro de eliminar elemento", context, new ActivityFragmentUtils.onClickDialog() {
                    @Override
                    public void onClickDialog(DialogInterface dialog, int id) {
                        FirebaseEntrega.deleteImplemento(context, item.getId(), new FirebaseEntrega.FbRsEntregas() {
                            @Override
                            public void isSuccesError(boolean isSucces, String msg, List<EntregaModel> entregaModels) {
                                ActivityFragmentUtils.ShowMessageDefault(msg, context, new ActivityFragmentUtils.onClickDialog() {
                                    @Override
                                    public void onClickDialog(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
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