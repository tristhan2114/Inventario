package com.denisse.implemento.Utils.ImplementoUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.denisse.implemento.Model.Entrega.EntregaItem;
import com.denisse.implemento.Model.Implemento;
import com.denisse.implemento.Utils.Constantes;
import com.denisse.implemento.Utils.ImplementosOp.AdministracionOp;
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

public class FirebaseImplemento {

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

    public static void getInventarioByParams(Context context, String params, FbRsImplemento fbRsImplemento){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_IMPLEMENTOS);
        Query myTopPostsQuery = databaseReference.orderByChild("codigo").equalTo(params);
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("Error-",".0. "+dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    try {
                        //Log.e("Error-",".1. "+dataSnapshot.toString());
                        List<Implemento> implementos = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            //Log.e("Error-",".2. "+child.toString());
                            Implemento empleado = child.getValue(Implemento.class);
                            //Log.e("Error-",".3. "+empleado.toString());
                            implementos.add(empleado);
                        }
                        progressDialog.dismiss();
                        fbRsImplemento.isSuccesError(true, "ok", implementos);
                    }catch (Exception e){
                        //Log.e("Error-",e.getMessage());
                        progressDialog.dismiss();
                        fbRsImplemento.isSuccesError(false, "No hay Implementos registrados. ", new ArrayList<>());
                    }
                }else {
                    progressDialog.dismiss();
                    fbRsImplemento.isSuccesError(false, "No hay Implementos registrados", new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                fbRsImplemento.isSuccesError(false, "No hay Implementos registrados e-database", new ArrayList<>());
            }
        });
    }

    public static void getInventarios(Context context, FbRsImplemento fbRsImplemento){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_IMPLEMENTOS);
        ValueEventListener eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("Error-",".1. "+dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    try {
                        //Log.e("Error-",".1. "+dataSnapshot.toString());
                        //List<Inventario> inventarios = (List<Inventario>) dataSnapshot.getValue(Inventario.class);
                        List<Implemento> implementos = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            //Log.e("Error-",".2. "+child.toString());
                            Implemento implemento = child.getValue(Implemento.class);
                            //Log.e("Error-",".3. "+empleado.toString());
                            implementos.add(implemento);
                        }
                        progressDialog.dismiss();
                        fbRsImplemento.isSuccesError(true, "ok", implementos);
                    }catch (Exception e){
                        Log.e("Error-",e.getMessage());
                        progressDialog.dismiss();
                        fbRsImplemento.isSuccesError(false, "No hay Implementos registrados", new ArrayList<>());
                    }
                }else {
                    progressDialog.dismiss();
                    fbRsImplemento.isSuccesError(false, "No hay Implementos registrados", new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                fbRsImplemento.isSuccesError(false, "No hay Implementos registrados e-database",new ArrayList<>());
            }
        });
        databaseReference.addValueEventListener(eventListener);
    }

    public static void getInventarioReporte(Context context, FbRsImplemento fbRsImplemento){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_IMPLEMENTOS);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("Error-",".1. "+dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    try {
                        //Log.e("Error-",".1. "+dataSnapshot.toString());
                        //List<Inventario> inventarios = (List<Inventario>) dataSnapshot.getValue(Inventario.class);
                        List<Implemento> implementos = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Implemento implemento = child.getValue(Implemento.class);
                            Log.e("Error-",".3. "+implemento.toString());
                            implementos.add(implemento);
                        }
                        progressDialog.dismiss();
                        fbRsImplemento.isSuccesError(true, "ok", implementos);
                    }catch (Exception e){
                        Log.e("Error-",e.getMessage());
                        progressDialog.dismiss();
                        fbRsImplemento.isSuccesError(false, "No hay Implementos registrados", new ArrayList<>());
                    }
                }else {
                    progressDialog.dismiss();
                    fbRsImplemento.isSuccesError(false, "No hay Implementos registrados", new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                fbRsImplemento.isSuccesError(false, "No hay Implementos registrados e-database",new ArrayList<>());
            }
        });
    }

    public static void createInventrio(Context context, Implemento implemento, FbRsImplemento fbRsImplemento){
        loading(context);
        DatabaseReference databaseReference = getmDatabaseReferenceSave();
        String id = databaseReference.push().getKey();
        implemento.setId(id);
        Map<String, Object> valuedata = implemento.toMap();
        Map<String, Object> objectdata = new HashMap<>();
        objectdata.put(Constantes.REQUEST_IMPLEMENTOS + id + "/", valuedata);
        databaseReference.updateChildren(objectdata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    fbRsImplemento.isSuccesError(false, "Implemento guardado.", new ArrayList<>());
                }else{
                    fbRsImplemento.isSuccesError(true, "Hubo un error al guardar implemento.", new ArrayList<>());
                }
            }
        });
        databaseReference.keepSynced(true);
    }

    public static void updateInventario(Context context, Implemento implemento, FbRsImplemento fbRsImplemento){
        DatabaseReference databaseReference = getmDatabase()
                .getReference(Constantes.REQUEST_IMPLEMENTOS)
                .child(String.valueOf(implemento.getId()));
        Map<String, Object> valuedata = implemento.toMap();
        databaseReference.updateChildren(valuedata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    fbRsImplemento.isSuccesError(false, "Implemento editado con exito.",  new ArrayList<>());
                }else{
                    fbRsImplemento.isSuccesError(true,"Hubo un error al editar implemento.", new ArrayList<>());
                }
            }
        });
        databaseReference.keepSynced(true);
    }

    public static void getImplementoCount(Context context, FbRsImplemento rsImplemento){
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_IMPLEMENTOS);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        int size = (int) dataSnapshot.getChildrenCount() + 1;
                        String codigo = AdministracionOp.customFormat("00000000", size);
                        rsImplemento.isSuccesError(true, codigo, new ArrayList<>());
                    }catch (Exception e){
                        rsImplemento.isSuccesError(true, AdministracionOp.customFormat("00000000", 1), new ArrayList<>());
                    }
                }else {
                    rsImplemento.isSuccesError(true, AdministracionOp.customFormat("00000000", 1), new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error-dtf", ".- "+databaseError.toString());
                rsImplemento.isSuccesError(true, AdministracionOp.customFormat("00000000", 1), new ArrayList<>());
            }
        });
    }

    public static void updateStockImplementos(Context context, List<EntregaItem> entregaItems, FbRsImplemento fbRsImplemento){
        //loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_IMPLEMENTOS);
        if(entregaItems!=null && entregaItems.size()>0){
         for (EntregaItem item : entregaItems){
             Query myTopPostsQuery = databaseReference.orderByChild("descripcion").equalTo(item.getDescripcion()).limitToFirst(1);
             myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     if (dataSnapshot.exists()){
                        try {
                            String id = "";
                            int auxCantidad = 0;
                            for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                id = (String) messageSnapshot.child("id").getValue();
                                auxCantidad = Integer.parseInt(String.valueOf(messageSnapshot.child("cantidad").getValue()));
                            }

                            int cantidadRs = auxCantidad - item.getCantidad();

                            DatabaseReference dbReferenceUpdate = getmDatabase().getReference(Constantes.REQUEST_IMPLEMENTOS).child(id);
                            dbReferenceUpdate.child("cantidad").setValue(cantidadRs);
                            fbRsImplemento.isSuccesError(false, "ok", new ArrayList<>());
                        }catch (Exception e){
                            Log.e("Error-tr", "..- "+e.getMessage());
                            fbRsImplemento.isSuccesError(true, "Error", new ArrayList<>());
                        }
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {
                     Log.e("Error-tr", ".databaseError.- "+databaseError.getMessage());
                     fbRsImplemento.isSuccesError(true, "Error", new ArrayList<>());
                 }
             });
         }
        }
    }

    public interface FbRsImplemento {
        void isSuccesError(boolean isSucces, String msg, List<Implemento> implementos);
    }
}
