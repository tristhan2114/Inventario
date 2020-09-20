package com.denisse.implemento.Utils.ImplementosOp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.denisse.implemento.R;
import com.denisse.implemento.Model.Implemento;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.ImplementoUtils.FirebaseImplemento;

import java.text.DecimalFormat;
import java.util.List;

public class AdministracionOp {

    static String codigo = "", descripcion = "";
    public static void DialogInfService(final Context context) {
        final Dialog MyDialog = new Dialog(context);
        CardView btnOk;
        ImageButton btnClose;

        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//quita titulo a los cuadros de dialogos
        MyDialog.setContentView(R.layout.dialogo_create_inventario);
        MyDialog.setCancelable(false);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText txtCodigo = MyDialog.findViewById(R.id.txtCodigo);
        EditText txtDescripcion = MyDialog.findViewById(R.id.txtDescripcion);


        txtCodigo.setEnabled(false);
        FirebaseImplemento.getImplementoCount(context, new FirebaseImplemento.FbRsImplemento() {
            @Override
            public void isSuccesError(boolean isSucces, String msg, List<Implemento> implementos) {
                txtCodigo.setText(msg);
            }
        });


        btnOk = MyDialog.findViewById(R.id.btnAdd);
        btnClose = MyDialog.findViewById(R.id.btnClose);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codigo = txtCodigo.getText().toString();
                descripcion = txtDescripcion.getText().toString();

                if(descripcion != null && descripcion.length() > 0 ){
                    Implemento implemento = new Implemento("0", codigo, descripcion.toUpperCase());
                    FirebaseImplemento.createInventrio(context, implemento, new FirebaseImplemento.FbRsImplemento() {
                        @Override
                        public void isSuccesError(boolean isSucces, String msg,  List<Implemento> implementos) {

                        }
                    });
                    MyDialog.dismiss();
                }else {
                    ActivityFragmentUtils.ShowMessage("Debe escribir una descripción", context, new ActivityFragmentUtils.onClickDialog() {
                        @Override
                        public void onClickDialog(DialogInterface dialog, int id) {

                        }
                    });
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog.dismiss();
            }
        });
        MyDialog.show();
    }

    static public String customFormat(String pattern, double value ) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(value);
        //System.out.println(value + "  " + pattern + "  " + output);
        return output;
    }

}
