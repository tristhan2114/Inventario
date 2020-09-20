package com.denisse.implemento.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denisse.implemento.Model.Administracion;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.AdministracionOp.FirebaseAdministracion;

import java.util.List;

public class AdapterAdministracion extends RecyclerView.Adapter<AdapterAdministracion.ViewHolder> {

    private List<Administracion> listData;
    private Context context;
    private OnCardClickListner onCardClickListner;

    // Adapter's Constructor
    public AdapterAdministracion(Context context, List<Administracion> develops, OnCardClickListner onCardClickListner) {
        this.listData = develops;
        this.context = context;
        this.onCardClickListner = onCardClickListner;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_administracion, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Administracion item = listData.get(position);
        //Log.v("wsdd",item.getDesciption());
        holder.lblNameUser.setText( (item.getName_user()!=null) ? item.getName_user() : "" );
        holder.lblCorreo.setText(item.getCorreo());
        holder.lblContrasenia.setText(item.getContrasenia());
        holder.lblRol.setText(item.getRol());
        holder.cvEliminar.setVisibility(  (item.getRol()!=null && item.getRol().equals("Administrador"))? View.GONE : View.VISIBLE  );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClickListner.OnCardClicked(v, position);
            }
        });

        holder.cvEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityFragmentUtils.ShowMessageDefault("¿Está seguro de eliminar elemento", context, new ActivityFragmentUtils.onClickDialog() {
                    @Override
                    public void onClickDialog(DialogInterface dialog, int id) {
                        FirebaseAdministracion.deleteAdministracion(context, item.getId(), new FirebaseAdministracion.FbRsAdministracion() {
                            @Override
                            public void isSuccesError(boolean isSucces, String msg, List<Administracion> administracions) {
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
        TextView lblRol, lblNameUser, lblCorreo, lblContrasenia;
        CardView cvEliminar;

        public ViewHolder(View v) {
            super(v);
            lblRol = v.findViewById(R.id.lblRol);
            lblNameUser = v.findViewById(R.id.lblNameUser);
            lblCorreo = v.findViewById(R.id.lblCorreo);
            lblContrasenia = v.findViewById(R.id.lblContrasenia);
            cvEliminar = v.findViewById(R.id.cvEliminar);
        }
    }

    public interface OnCardClickListner {
        void OnCardClicked(View view, int position);
    }

}