package com.denisse.implemento.Utils.AdministracionOp;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.denisse.implemento.Model.Administracion;
import com.denisse.implemento.Utils.Constantes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseAdministracion {

    private static ProgressDialog progressDialog;

    public static FirebaseDatabase getmDatabase(){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        return mDatabase;
    }

    public static DatabaseReference getmDatabaseReferenceSave() {
        DatabaseReference mDatabaseReference = getmDatabase().getReference();
        return mDatabaseReference;
    }

    private static void loading(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Procesando....");
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    public static void getUserLogin(Context context, String email, String contraseña, FbRsAdministracion rsAdministracion){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_USUARIO);
        Query myTopPostsQuery = databaseReference.orderByChild("correo").equalTo(email).limitToFirst(1);
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("Error-",".0. "+dataSnapshot.toString());
                progressDialog.dismiss();
                if (dataSnapshot.exists()) {
                    try {
                        Administracion administracions = null;
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            administracions = postSnapshot.getValue(Administracion.class);
                        }
                        //Log.e("Error-",".parse. "+administracions.toString());
                        //Log.e("Error-",".very. "+administracions.getContrasenia().equals(contraseña));
                        if(administracions.getContrasenia().equals(contraseña)){
                            List<Administracion> aux = new ArrayList<>();
                            aux.add(administracions);
                            rsAdministracion.isSuccesError(false, "ok", aux);
                        }else{
                            rsAdministracion.isSuccesError(true, "Verifique las credenciales ingresadas", new ArrayList<>());
                        }
                    }catch (Exception e){
                        Log.e("Error-",".-. "+e.getMessage());
                        rsAdministracion.isSuccesError(true, "El correo ingresado no existe", new ArrayList<>());
                    }
                }else {
                    rsAdministracion.isSuccesError(true, "El correo ingresado no existe", new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                rsAdministracion.isSuccesError(true, "Error al intentar conectar a firebase "+databaseError.getMessage(), new ArrayList<>());
            }
        });
    }

    public static void getAdministracion(Context context, FbRsAdministracion rsAdministracion){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_USUARIO);
        ValueEventListener eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        //Log.e("Error-",".1. "+dataSnapshot.toString());
                        List<Administracion> administracions = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            //Log.e("Error-",".2. "+child.toString());
                            Administracion administracion = child.getValue(Administracion.class);
                            //Log.e("Error-",".3. "+administracion.toString());
                            administracions.add(administracion);
                        }
                        progressDialog.dismiss();
                        rsAdministracion.isSuccesError(true, "ok", administracions);
                    }catch (Exception e){
                        //Log.e("Error-",e.getMessage());
                        progressDialog.dismiss();
                        rsAdministracion.isSuccesError(false, "No hay elementos que mostrar", new ArrayList<>());
                    }
                }else {
                    progressDialog.dismiss();
                    rsAdministracion.isSuccesError(false, "No hay elementos que mostrar", new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error-dtf", ".- "+databaseError.toString());
                progressDialog.dismiss();
                rsAdministracion.isSuccesError(false, "No hay elementos que mostrar e-database", new ArrayList<>());
            }
        });
        databaseReference.addValueEventListener(eventListener);
    }

    public static void createAdministracion(Context context, Administracion administracion, FbRsAdministracion rsAdministracion){
        loading(context);
        DatabaseReference databaseReference = getmDatabaseReferenceSave();
        String id = databaseReference.push().getKey();
        administracion.setId(id);
        Map<String, Object> valuedata = administracion.toMap();
        Map<String, Object> objectdata = new HashMap<>();
        objectdata.put(Constantes.REQUEST_USUARIO + id + "/", valuedata);
        databaseReference.updateChildren(objectdata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    rsAdministracion.isSuccesError(false, "Empleado Guardado con exito.", new ArrayList<>());
                }else{
                    rsAdministracion.isSuccesError(true, "Hubo un error al guardar empleado.", new ArrayList<>());
                }
            }
        });
        databaseReference.keepSynced(true);
    }

    public static void deleteAdministracion(Context context, String id, FbRsAdministracion rsAdministracion){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_USUARIO).child(id);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    rsAdministracion.isSuccesError(false, "Elemento eliminado", new ArrayList<>());
                }else{
                    rsAdministracion.isSuccesError(true, "Error al eliminar elemento", new ArrayList<>());
                }
            }
        });
    }

    public static void updateAdministracion(Context context, Administracion administracion, FbRsAdministracion rsAdministracion){
        loading(context);
        DatabaseReference databaseReference = getmDatabase()
                .getReference(Constantes.REQUEST_USUARIO)
                .child(String.valueOf(administracion.getId()));
        Map<String, Object> valuedata = administracion.toMap();
        databaseReference.updateChildren(valuedata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    rsAdministracion.isSuccesError(false, "Usuario editado con exito.", new ArrayList<>());
                }else{
                    rsAdministracion.isSuccesError(true, "Hubo un error al editar usuario.", new ArrayList<>());
                }
            }
        });
        databaseReference.keepSynced(true);
    }

    public interface FbRsAdministracion {
        void isSuccesError(boolean isSucces, String msg, List<Administracion> administracions);
    }

}
