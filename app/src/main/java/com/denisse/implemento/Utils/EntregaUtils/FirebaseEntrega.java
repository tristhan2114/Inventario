package com.denisse.implemento.Utils.EntregaUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.denisse.implemento.Model.Empleado.Empleado;
import com.denisse.implemento.Model.Entrega.EntregaModel;
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

public class FirebaseEntrega {

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
/*
    public static void getEmpleadosByParams(Context context, String params, FbRsEntregas rsEmpleado){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_EMPLEADOS);
        Query myTopPostsQuery = databaseReference.orderByChild("ci").equalTo(params);
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("Error-",".0. "+dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    try {
                        //List<Empleado> empleados = (List<Empleado>) dataSnapshot.getValue(Empleado.class);
                        //Log.e("Error-",".1. "+dataSnapshot.toString());
                        List<Empleado> empleados = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            //Log.e("Error-",".2. "+child.toString());
                            Empleado empleado = child.getValue(Empleado.class);
                            //Log.e("Error-",".3. "+empleado.toString());
                            empleados.add(empleado);
                        }
                        progressDialog.dismiss();
                        rsEmpleado.isSuccesError(true, "", empleados);
                    }catch (Exception e){
                        Log.e("Error-",e.getMessage());
                        progressDialog.dismiss();
                        rsEmpleado.isSuccesError(false, "", new ArrayList<>());
                    }
                }else {
                    progressDialog.dismiss();
                    rsEmpleado.isSuccesError(false, "", new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                rsEmpleado.isSuccesError(false, "", new ArrayList<>());
            }
        });
    }

    public static void checkEmpleadoExit(String cedula, FbRsEntregas rsEmpleado){
        //loading(context);
        DatabaseReference reference = getmDatabase().getReference(Constantes.REQUEST_EMPLEADOS);
        Query query = reference.orderByChild("ci").equalTo(cedula).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    rsEmpleado.isSuccesError(true, "", new ArrayList<>());
                }else{
                    rsEmpleado.isSuccesError(false, "", new ArrayList<>());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                rsEmpleado.isSuccesError(false, "", new ArrayList<>());
            }
        });

    }

    public static void getEmpleados(Context context, FbRsEntregas rsEmpleado){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_EMPLEADOS);
        ValueEventListener eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        //Log.e("Error-",".1. "+dataSnapshot.toString());
                        List<Empleado> empleados = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            //Log.e("Error-",".2. "+child.toString());
                            Empleado empleado = child.getValue(Empleado.class);
                            //Log.e("Error-",".3. "+empleado.toString());
                            empleados.add(empleado);
                        }
                        progressDialog.dismiss();
                        rsEmpleado.isSuccesError(true, "ok", empleados);
                    }catch (Exception e){
                        //Log.e("Error-",e.getMessage());
                        progressDialog.dismiss();
                        rsEmpleado.isSuccesError(false, "No hay Empleados registrados", new ArrayList<>());
                    }
                }else {
                    progressDialog.dismiss();
                    rsEmpleado.isSuccesError(false, "No hay Empleados registrados", new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error-dtf", ".- "+databaseError.toString());
                progressDialog.dismiss();
                rsEmpleado.isSuccesError(false, "No hay Imventario registrados e-database", new ArrayList<>());
            }
        });
        databaseReference.addValueEventListener(eventListener);
    }
*/
    public static void createEntrega(Context context, EntregaModel entregaModel, FbRsEntregas rsEmpleado){
        loading(context);
        DatabaseReference databaseReference = getmDatabaseReferenceSave();
        String id = databaseReference.push().getKey();
        entregaModel.setId(id);
        Map<String, Object> valuedata = entregaModel.toMap();
        Map<String, Object> objectdata = new HashMap<>();
        objectdata.put(Constantes.REQUEST_ENTREGAS + id + "/", valuedata);
        databaseReference.updateChildren(objectdata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    rsEmpleado.isSuccesError(false, "Entrega regitrada con exito.", new ArrayList<>());
                }else{
                    rsEmpleado.isSuccesError(true, "Hubo un error al registrar entrega.", new ArrayList<>());
                }
            }
        });
        databaseReference.keepSynced(true);
    }
/*
    public static void updateEmpleado(Context context, Empleado empleado, FbRsEntregas rsEmpleado){
        loading(context);
        DatabaseReference databaseReference = getmDatabase()
                .getReference(Constantes.REQUEST_EMPLEADOS)
                .child(String.valueOf(empleado.getId()));
        Map<String, Object> valuedata = empleado.toMap();
        databaseReference.updateChildren(valuedata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    rsEmpleado.isSuccesError(false, "Empleado editado con exito.", new ArrayList<>());
                }else{
                    rsEmpleado.isSuccesError(true, "Hubo un error al editar empleado.", new ArrayList<>());
                }
            }
        });
        databaseReference.keepSynced(true);
    }

*/

    public interface FbRsEntregas {
        void isSuccesError(boolean isSucces, String msg, List<EntregaModel> entregaModels);
    }
}
