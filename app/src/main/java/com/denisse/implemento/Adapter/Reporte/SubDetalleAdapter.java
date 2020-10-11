package com.denisse.implemento.Adapter.Reporte;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denisse.implemento.Model.Entrega.EntregaItem;
import com.denisse.implemento.Model.Entrega.EntregaModel;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;

import java.util.List;

public class SubDetalleAdapter extends RecyclerView.Adapter<SubDetalleAdapter.ViewHolder> {

    private Context context;
    private List<EntregaItem> entregaItems;
    private List<Integer> colors;
    private OnCardClickListner onCardClickListner;

    public SubDetalleAdapter(Context context, List<EntregaItem> entregaItems,List<Integer> colors, OnCardClickListner onCardClickListner) {
        this.context = context;
        this.entregaItems = entregaItems;
        this.colors = colors;
        this.onCardClickListner = onCardClickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EntregaItem model = entregaItems.get(position);

        if(colors!=null && colors.size()>0){
            holder.card.setCardBackgroundColor(colors.get(position));
        }

        String dato = "";

        dato += model.getDescripcion();

        holder.txtterms.setText(dato);
        holder.txtterms.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        holder.txtterms.setTextColor(context.getResources().getColor(R.color.colorWhite));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardClickListner.OnCardClicked(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return entregaItems.size();
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
