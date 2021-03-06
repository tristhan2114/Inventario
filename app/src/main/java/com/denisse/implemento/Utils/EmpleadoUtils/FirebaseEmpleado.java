package com.denisse.implemento.Utils.EmpleadoUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.util.Log;

import com.denisse.implemento.Model.Empleado.Departamento;
import com.denisse.implemento.Model.Empleado.Empleado;
import com.denisse.implemento.Model.Empleado.Puesto;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.Constantes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

public class FirebaseEmpleado {

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

    public static void auth(Activity activity, String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getUid() == null){
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.e("Error-cr", "Correcto");
                    }else{
                        Log.e("Error-cr", "Error");
                    }
                }
            });
        }
    }

    public static void cerrarSesion(Context context){
        if(ActivityFragmentUtils.isConnetionNetwork(context)){
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
        }else {
            ActivityFragmentUtils.ShowMessage("Por favor, verifique su conexón a internet", context, new ActivityFragmentUtils.onClickDialog() {
                @Override
                public void onClickDialog(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
    }

    public static void getEmpleadosByParams(Context context, String params, String search, FbRsEmpleado rsEmpleado){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_EMPLEADOS);
        Query myTopPostsQuery = databaseReference.orderByChild(params).equalTo(search);
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

    public static void getEmpleadosByParamsEntrega(Context context, String params, Departamento departamento, Puesto puesto, FbRsEmpleado rsEmpleado){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_EMPLEADOS);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("Error-",".0. "+dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    try {
                        List<Empleado> empleados = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Empleado empleado = child.getValue(Empleado.class);
                            assert empleado != null;
                            if(departamento != null) {
                                if (empleado.getDepartamento() != null){
                                    if(departamento.getId().equals(empleado.getDepartamento().getId())){
                                        empleados.add(empleado);
                                    }
                                }
                            }
                            if(puesto != null){
                                if(empleado.getPuesto() != null){
                                    if(puesto.getId().equals(empleado.getPuesto().getId())){
                                        empleados.add(empleado);
                                    }
                                }

                            }
                            //Log.e("Error-",".3. "+empleado.toString());
                        }
                        progressDialog.dismiss();
                        if(empleados!=null && empleados.size()>0 ){
                            rsEmpleado.isSuccesError(false, "pk", empleados);
                        }else{
                            rsEmpleado.isSuccesError(true, "No se puede programar entrega no hay empleado en "+params, new ArrayList<>());
                        }
                    }catch (Exception e){
                        progressDialog.dismiss();
                        rsEmpleado.isSuccesError(true, "No se puede programar entrega no hay empleado en "+params, new ArrayList<>());
                    }
                }else {
                    progressDialog.dismiss();
                    rsEmpleado.isSuccesError(true, "No se puede programar entrega no hay empleado en "+params, new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                rsEmpleado.isSuccesError(true, "No se puede programar entrega no hay empleado en "+params, new ArrayList<>());
            }
        });
    }

    public static void getEmpleadosByParamsSearch(Context context, String params, String search, FbRsEmpleado rsEmpleado){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_EMPLEADOS);
        Query myTopPostsQuery = null;
        if(params.equals("ci")){
            Log.e("Error-yu",".123 "+params+" - "+search);
            myTopPostsQuery = databaseReference.orderByChild(params).equalTo(search.trim());
        }else{
            Log.e("Error-yu",".456 "+params+" - "+search);
            myTopPostsQuery = databaseReference.orderByChild(params).startAt(search).endAt(search+"\uf8ff");
        }
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
                            Log.e("Error-",".2. "+child.toString());
                            Empleado empleado = child.getValue(Empleado.class);
                            //Log.e("Error-",".3. "+empleado.toString());
                            empleados.add(empleado);
                        }
                        progressDialog.dismiss();
                        rsEmpleado.isSuccesError(true, "", empleados);
                    }catch (Exception e){
                        Log.e("Error-",e.getMessage());
                        progressDialog.dismiss();
                        rsEmpleado.isSuccesError(false, "No se encuentra empleado", new ArrayList<>());
                    }
                }else {
                    progressDialog.dismiss();
                    rsEmpleado.isSuccesError(false, "No se encuentra empleado", new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                rsEmpleado.isSuccesError(false, "No se encuentra empleado e-database", new ArrayList<>());
            }
        });
    }

    public static void checkEmpleadoExit(String cedula, FbRsEmpleado rsEmpleado){
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

    public static void getEmpleados(Context context, FbRsEmpleado rsEmpleado){
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

    public static void createEmpleado(Context context, Empleado empleado, FbRsEmpleado rsEmpleado){
        loading(context);
        DatabaseReference databaseReference = getmDatabaseReferenceSave();
        String id = databaseReference.push().getKey();
        empleado.setId(id);
        Map<String, Object> valuedata = empleado.toMap();
        Map<String, Object> objectdata = new HashMap<>();
        objectdata.put(Constantes.REQUEST_EMPLEADOS + id + "/", valuedata);
        databaseReference.updateChildren(objectdata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    rsEmpleado.isSuccesError(false, "Empleado Guardado con exito.", new ArrayList<>());
                }else{
                    rsEmpleado.isSuccesError(true, "Hubo un error al guardar empleado.", new ArrayList<>());
                }
            }
        });
        databaseReference.keepSynced(true);
    }

    public static void updateEmpleado(Context context, Empleado empleado, FbRsEmpleado rsEmpleado){
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



    public interface FbRsEmpleado {
        void isSuccesError(boolean isSucces, String msg, List<Empleado> empleados);
    }
}
