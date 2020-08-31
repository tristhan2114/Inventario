package com.denisse.inventario.Utils.InventarioUtils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.denisse.inventario.Model.Empleado.Empleado;
import com.denisse.inventario.Model.Inventario;
import com.denisse.inventario.Utils.Constantes;
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


    public static FirebaseDatabase getmDatabase(){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        return mDatabase;
    }

    public static DatabaseReference getmDatabaseReferenceSave() {
        DatabaseReference mDatabaseReference = getmDatabase().getReference();
        return mDatabaseReference;
    }

    public static void getInventarioByParams(String params, FbRsInventario rsEmpleado){
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
                        rsEmpleado.isSuccesError(true, inventarios);
                    }catch (Exception e){
                        //Log.e("Error-",e.getMessage());
                        rsEmpleado.isSuccesError(false, new ArrayList<>());
                    }
                }else {
                    rsEmpleado.isSuccesError(false, new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                rsEmpleado.isSuccesError(false, new ArrayList<>());
            }
        });
    }

    public static void getEmpleados(FbRsInventario rsEmpleado){
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_INVENTARIOS);
        ValueEventListener eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Error-",".1. "+dataSnapshot.toString());
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
                        rsEmpleado.isSuccesError(true, inventarios);
                    }catch (Exception e){
                        //Log.e("Error-",e.getMessage());
                        rsEmpleado.isSuccesError(false, new ArrayList<>());
                    }
                }else {
                    rsEmpleado.isSuccesError(false, new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                rsEmpleado.isSuccesError(false, new ArrayList<>());
            }
        });
        databaseReference.addValueEventListener(eventListener);
    }

    public static void createInventrio(Inventario inventario){
        DatabaseReference databaseReference = getmDatabaseReferenceSave();
        String id = databaseReference.push().getKey();
        inventario.setId(id);
        Map<String, Object> valuedata = inventario.toMap();
        Map<String, Object> objectdata = new HashMap<>();
        objectdata.put(Constantes.REQUEST_INVENTARIOS + id + "/", valuedata);
        databaseReference.updateChildren(objectdata);
        databaseReference.keepSynced(true);
    }

    public static void updateInventario(Inventario inventario){
        DatabaseReference databaseReference = getmDatabase()
                .getReference(Constantes.REQUEST_INVENTARIOS)
                .child(String.valueOf(inventario.getId()));
        Map<String, Object> valuedata = inventario.toMap();
        databaseReference.updateChildren(valuedata);
        databaseReference.keepSynced(true);
    }



    public interface FbRsInventario {
        void isSuccesError(boolean isSucces, List<Inventario> inventarios);
    }
}
