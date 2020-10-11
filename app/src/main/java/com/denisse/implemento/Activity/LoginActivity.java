package com.denisse.implemento.Activity;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.denisse.implemento.Model.Administracion;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.EmpleadoUtils.FirebaseEmpleado;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.AdministracionOp.AdministracionOp;
import com.denisse.implemento.Utils.AdministracionOp.FirebaseAdministracion;
import com.denisse.implemento.Utils.SessionFirebase;
import com.denisse.implemento.Utils.SharedData;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Context context;
    private Activity activity;
    private CardView btnIniciarSesion;
    private EditText txtEmail, txtContrasenia;
    private TextView lblTitle;
    String email = "", contraseña ="";
    private SharedPreferences sharedPreferences;
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        authVerify();
        sharedPreferences = context.getSharedPreferences(SharedData.KEYPREFERENCES, Context.MODE_PRIVATE);
        activity = this;

        startwidget();
    }

    private void startwidget() {
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        txtEmail = findViewById(R.id.txtEmail);
        txtContrasenia = findViewById(R.id.txtContrasenia);
        lblTitle = findViewById(R.id.lblTitle);

        btnIniciarSesion.setOnClickListener(view -> {
            email = txtEmail.getText().toString().trim();
            contraseña = txtContrasenia.getText().toString().trim();
            ActivityFragmentUtils.hideTeclado(context, lblTitle);
            // Verifico conexion a internet
            if(ActivityFragmentUtils.isConnetionNetwork(context)){
                // Verifico validacion de campos
                if(validateCampos(context, email, contraseña)){
                    // verifico la sesion auth de firebase
                    SessionFirebase.authFirebase(activity, SharedData.CORREO_DEFAULT, SharedData.CONTRASENIA_DEFAULT, new SessionFirebase.FbRs() {
                        @Override
                        public void isSuccesError(boolean isSucces, String msg) {
                            //Log.e("Error-",".isSuccesError. "+isSucces);
                            if(isSucces){
                                msgRs(context, msg);
                            }else{
                                isVerifyUserExitByEmailPass(context, email, contraseña);
                            }
                        }
                    });
                }
            }else{
                msgRs(context, "No tiene conexión a internet...");
            }
        });
    }

    private static boolean validateCampos(Context context, String email, String contraseña) {
        if(email.isEmpty()){
            msgRs(context, "Campo email es requerido");
            return false;
        }

        if(!AdministracionOp.isValidEmail(email)){
            msgRs(context, "Verifique el correo ingresado");
            return false;
        }

        if(contraseña.isEmpty()){
            msgRs(context, "Campo contraseña es requerido");
            return false;
        }

        return true;
    }

    private static void msgRs(Context context, String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ActivityFragmentUtils.ShowMessage(msg, context, new ActivityFragmentUtils.onClickDialog() {
                    @Override
                    public void onClickDialog(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void isVerifyUserExitByEmailPass(Context context, String email, String contraseña){
        FirebaseAdministracion.getUserLogin(context, email, contraseña, new FirebaseAdministracion.FbRsAdministracion() {
            @Override
            public void isSuccesError(boolean isSucces, String msg, List<Administracion> administracions) {
                //Log.e("Error-",".isVerifyUserExitByEmailPass2. "+isSucces);
                if(isSucces){
                    FirebaseEmpleado.cerrarSesion(activity.getApplicationContext());
                    msgRs(context, msg);
                }else {
                    // si todo correcto inicia sesion
                    SharedData.saveKey(sharedPreferences, SharedData.SESION, "ok");
                    SharedData.saveKey(sharedPreferences, SharedData.NAME_USER, administracions.get(0).getName_user());
                    SharedData.saveKey(sharedPreferences, SharedData.ROL, administracions.get(0).getRol());
                    authVerify();
                }
            }
        });
    }

    private void authVerify() {
        // verifico sesion mediante dato guardado en las preferencias
        if(SharedData.getKey(context, SharedData.SESION) != null ){
            Log.e("Error-shd", "..- "+SharedData.getKey(context, SharedData.SESION));
            if(SharedData.getKey(context, SharedData.SESION).equals("ok")){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    }

}