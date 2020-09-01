package com.denisse.inventario.Utils.InventarioUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.denisse.inventario.Model.Empleado.Empleado;
import com.denisse.inventario.Model.Inventario;
import com.denisse.inventario.Utils.Constantes;
import com.denisse.inventario.Utils.FirebaseConnect;
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

public class FirebaseInventario {

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

    public static void getInventarioByParams(Context context, String params, FbRsInventario fbRsInventario){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_INVENTARIOS);
        Query myTopPostsQuery = databaseReference.orderByChild("codigo").equalTo(params);
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("Error-",".0. "+dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    try {
                        //Log.e("Error-",".1. "+dataSnapshot.toString());
                        List<Inventario> inventarios = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            //Log.e("Error-",".2. "+child.toString());
                            Inventario empleado = child.getValue(Inventario.class);
                            //Log.e("Error-",".3. "+empleado.toString());
                            inventarios.add(empleado);
                        }
                        progressDialog.dismiss();
                        fbRsInventario.isSuccesError(true, "ok", inventarios);
                    }catch (Exception e){
                        //Log.e("Error-",e.getMessage());
                        progressDialog.dismiss();
                        fbRsInventario.isSuccesError(false, "No hay encuentra inventario. ", new ArrayList<>());
                    }
                }else {
                    progressDialog.dismiss();
                    fbRsInventario.isSuccesError(false, "No hay Inventario registrados", new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                fbRsInventario.isSuccesError(false, "No hay Imventario registrados e-database", new ArrayList<>());
            }
        });
    }

    public static void getInventarios(Context context, FbRsInventario fbRsInventario){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_INVENTARIOS);
        ValueEventListener eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("Error-",".1. "+dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    try {
                        //Log.e("Error-",".1. "+dataSnapshot.toString());
                        //List<Inventario> inventarios = (List<Inventario>) dataSnapshot.getValue(Inventario.class);
                        List<Inventario> inventarios = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            //Log.e("Error-",".2. "+child.toString());
                            Inventario inventario = child.getValue(Inventario.class);
                            //Log.e("Error-",".3. "+empleado.toString());
                            inventarios.add(inventario);
                        }
                        progressDialog.dismiss();
                        fbRsInventario.isSuccesError(true, "ok", inventarios);
                    }catch (Exception e){
                        Log.e("Error-",e.getMessage());
                        progressDialog.dismiss();
                        fbRsInventario.isSuccesError(false, "No hay Inventario registrados", new ArrayList<>());
                    }
                }else {
                    progressDialog.dismiss();
                    fbRsInventario.isSuccesError(false, "No hay Inventario registrados else", new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                fbRsInventario.isSuccesError(false, "No hay Inventario registrados e-database",new ArrayList<>());
            }
        });
        databaseReference.addValueEventListener(eventListener);
        /*if(!FirebaseConnect.onConnect()){
            progressDialog.dismiss();
            fbRsInventario.isSuccesError(false, "Verifique su conexión a internet", new ArrayList<>());
        }*/
    }

    public static void createInventrio(Context context, Inventario inventario, FbRsInventario fbRsInventario){
        loading(context);
        DatabaseReference databaseReference = getmDatabaseReferenceSave();
        String id = databaseReference.push().getKey();
        inventario.setId(id);
        Map<String, Object> valuedata = inventario.toMap();
        Map<String, Object> objectdata = new HashMap<>();
        objectdata.put(Constantes.REQUEST_INVENTARIOS + id + "/", valuedata);
        databaseReference.updateChildren(objectdata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    fbRsInventario.isSuccesError(false, "Inventario guardado.", new ArrayList<>());
                }else{
                    fbRsInventario.isSuccesError(true, "Hubo un error al guardar inventario.", new ArrayList<>());
                }
            }
        });
        databaseReference.keepSynced(true);
    }

    public static void updateInventario(Context context, Inventario inventario, FbRsInventario fbRsInventario){
        DatabaseReference databaseReference = getmDatabase()
                .getReference(Constantes.REQUEST_INVENTARIOS)
                .child(String.valueOf(inventario.getId()));
        Map<String, Object> valuedata = inventario.toMap();
        databaseReference.updateChildren(valuedata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    fbRsInventario.isSuccesError(false, "Hubo un error al editar inventario.",  new ArrayList<>());
                }else{
                    fbRsInventario.isSuccesError(true,"Inventario editado con exito.", new ArrayList<>());
                }
            }
        });
        databaseReference.keepSynced(true);
    }



    public interface FbRsInventario {
        void isSuccesError(boolean isSucces, String msg, List<Inventario> inventarios);
    }
}
