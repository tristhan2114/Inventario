package com.denisse.implemento.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.denisse.implemento.Utils.EmpleadoUtils.FirebaseEmpleado;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SessionFirebase {

    private static ProgressDialog progressDialog;

    private static void loading(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Procesando....");
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    public static void authFirebase(Activity activity, String email, String password, FbRs fbRs){
        loading(activity);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getUid() == null){
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if(task.isSuccessful()){
                        //Log.e("Error-cr", "Correcto");
                        fbRs.isSuccesError(false, "ok");
                    }else{
                        FirebaseEmpleado.cerrarSesion(activity.getApplicationContext());
                        //Log.e("Error-cr", "Error");
                        fbRs.isSuccesError(true, "Error inesperado... vuelva a intentar");
                    }
                }
            });
        }
    }


    public interface FbRs {
        void isSuccesError(boolean isSucces, String msg);
    }
}
