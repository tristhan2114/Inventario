package com.denisse.implemento.Utils.EntregaOp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.denisse.implemento.Adapter.AdapterEntrega;
import com.denisse.implemento.Adapter.AdapterEntregaList;
import com.denisse.implemento.Model.Entrega.EntregaItem;
import com.denisse.implemento.Model.Implemento;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.ImplementoUtils.FirebaseImplemento;

import java.text.DecimalFormat;
import java.util.List;

public class EntregasOp {

    static String codigo = "", descripcion = "";
    public static void DialogInfService(final Context context, List<EntregaItem> items) {
        final Dialog MyDialog = new Dialog(context);
        CardView btnOk;
        ImageButton btnClose;

        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//quita titulo a los cuadros de dialogos
        MyDialog.setContentView(R.layout.dialogo_view_entrega_item);
        MyDialog.setCancelable(false);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RecyclerView rv_entregaItems = MyDialog.findViewById(R.id.rv_entregaItems);
        rv_entregaItems.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rv_entregaItems.setHasFixedSize(true);

        AdapterEntrega adapterEntrega = new AdapterEntrega(context, items, new AdapterEntrega.OnCardClickListner() {
            @Override
            public void OnCardClicked(int position) {

            }
        });
        rv_entregaItems.setAdapter(adapterEntrega);

        btnClose = MyDialog.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               MyDialog.dismiss();
            }
        });

        MyDialog.show();
    }

}
