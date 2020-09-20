package com.denisse.implemento.Utils.AdministracionOp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.denisse.implemento.Model.Administracion;
import com.denisse.implemento.Model.Empleado.Departamento;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;

import java.util.List;
import java.util.regex.Pattern;

public class AdministracionOp {

    static String nameUser = "", correo = "", contraseña = "";
    static Departamento rol;

    public static void DialogAdministracion(final Context context, Administracion administracion) {
        final Dialog MyDialog = new Dialog(context);
        CardView btnOk;
        ImageButton btnClose;
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//quita titulo a los cuadros de dialogos
        MyDialog.setContentView(R.layout.dialogo_create_administracion);
        MyDialog.setCancelable(false);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView lblTitle = MyDialog.findViewById(R.id.lblTitle);
        EditText txtNameUser = MyDialog.findViewById(R.id.txtNameUser);
        EditText txtCorreo = MyDialog.findViewById(R.id.txtCorreo);
        EditText txtContrasenia = MyDialog.findViewById(R.id.txtContrasenia);

        // carga de spinner
        Spinner sp_rol = MyDialog.findViewById(R.id.sp_rol);
        ArrayAdapter<Departamento> adapterArea = new ArrayAdapter<Departamento>(context,
                R.layout.simple_spinner_item, R.id.txtterms, Departamento.roles());
        sp_rol.setAdapter(adapterArea);
        sp_rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ActivityFragmentUtils.hideTeclado(context, lblTitle);
                    if (!Departamento.roles().get(position).getDescripcion().equals("- SELECCIONE UNA OPCIÓN -")){
                        rol = Departamento.roles().get(position);
                    }
                }catch (Exception e){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // fin carga de spinner

        // si es update
        if(administracion != null){
            txtNameUser.setText(administracion.getName_user());
            txtCorreo.setText(administracion.getCorreo());
            txtContrasenia.setText(administracion.getContrasenia());
            getIndex(sp_rol, administracion.getRol());
        }

        btnOk = MyDialog.findViewById(R.id.btnAdd);
        btnClose = MyDialog.findViewById(R.id.btnClose);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameUser = txtNameUser.getText().toString();
                correo = txtCorreo.getText().toString();
                contraseña = txtContrasenia.getText().toString();

                if(validateCampos(context, nameUser, correo, contraseña, rol)){
                    Administracion administracionCreate = new Administracion("0", nameUser, correo, contraseña, rol.getDescripcion(), true);
                    if (administracion == null){
                        processCreateOrUpdate(context, administracionCreate, MyDialog, true);
                    }else{
                        // update
                        administracionCreate.setId(administracion.getId());
                        processCreateOrUpdate(context, administracionCreate, MyDialog, false);
                    }
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

    private static void processCreateOrUpdate(Context context, Administracion administracion, Dialog MyDialog, boolean isCreate) {
        if(ActivityFragmentUtils.isConnetionNetwork(context)){
            if(isCreate){
                FirebaseAdministracion.createAdministracion(context, administracion, new FirebaseAdministracion.FbRsAdministracion() {
                    @Override
                    public void isSuccesError(boolean isSucces, String msg, List<Administracion> administracions) {
                        if(isSucces){
                            msgRs(context, msg);
                        }else{
                            msgRs(context, msg);
                            MyDialog.dismiss();
                        }
                    }
                });
            }else {
                FirebaseAdministracion.updateAdministracion(context, administracion, new FirebaseAdministracion.FbRsAdministracion() {
                    @Override
                    public void isSuccesError(boolean isSucces, String msg, List<Administracion> administracions) {
                        if(isSucces){
                            msgRs(context, msg);
                        }else{
                            msgRs(context, msg);
                            MyDialog.dismiss();
                        }
                    }
                });
            }
        }else{
            msgRs(context, "Verifique su conexión a internet");
        }
    }

    private static boolean validateCampos(Context context, String codigo, String descripcion, String tipo, Departamento departamento) {
        if(codigo.isEmpty()){
            msgRs(context, "Campo nombre es requerido");
            return false;
        }
        if(descripcion.isEmpty()){
            msgRs(context, "Campo correo es requerido");
            return false;
        }
        if(tipo.isEmpty()){
            msgRs(context, "Campo contraseña es requerido");
            return false;
        }

        if(tipo.length() < 6){
            msgRs(context, "La contraseña debe tener más de 6 carácteres");
            return false;
        }

        if(isValidEmail(tipo)){
            msgRs(context, "Verifique el correo ingresado");
            return false;
        }

        if(departamento != null && departamento.getDescripcion().equals("- SELECCIONE UNA OPCIÓN -")){
            msgRs(context, "Debe seleccionar un rol para el usuario");
            return false;
        }
        return true;
    }

    private static void msgRs(Context context, String msg) {
        ActivityFragmentUtils.ShowMessage(msg, context, new ActivityFragmentUtils.onClickDialog() {
            @Override
            public void onClickDialog(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    public static boolean isValidEmail(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private static int getIndex(Spinner spinner, String myString){
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(myString)){
                spinner.setSelection(i);
                break;
            }
        }
        return index;
    }
}
