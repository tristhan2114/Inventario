package com.denisse.inventario.Utils.EmpleadoUtils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.denisse.inventario.Model.Empleado.Empleado;
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

public class FirebaseEmpleado {


    public static FirebaseDatabase getmDatabase(){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        return mDatabase;
    }

    public static DatabaseReference getmDatabaseReferenceSave() {
        DatabaseReference mDatabaseReference = getmDatabase().getReference();
        return mDatabaseReference;
    }

    public static void getEmpleadosByParams(String params, FbRsEmpleado rsEmpleado){
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_EMPLEADOS);
        Query myTopPostsQuery = databaseReference.orderByChild("ci").equalTo(params);
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("Error-",".0. "+dataSnapshot.toString());
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
                        rsEmpleado.isSuccesError(true, empleados);
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

    public static void getEmpleados(FbRsEmpleado rsEmpleado){
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
                        rsEmpleado.isSuccesError(true, empleados);
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

    public static void createEmpleado(Empleado empleado){
        DatabaseReference databaseReference = getmDatabaseReferenceSave();
        String id = databaseReference.push().getKey();
        empleado.setId(id);
        Map<String, Object> valuedata = empleado.toMap();
        Map<String, Object> objectdata = new HashMap<>();
        objectdata.put(Constantes.REQUEST_EMPLEADOS + id + "/", valuedata);
        databaseReference.updateChildren(objectdata);
        databaseReference.keepSynced(true);
    }

    public static void updateEmpleado(Empleado empleado){
        DatabaseReference databaseReference = getmDatabase()
                .getReference(Constantes.REQUEST_EMPLEADOS)
                .child(String.valueOf(empleado.getId()));
        Map<String, Object> valuedata = empleado.toMap();
        databaseReference.updateChildren(valuedata);
        databaseReference.keepSynced(true);
    }



    public interface FbRsEmpleado {
        void isSuccesError(boolean isSucces, List<Empleado> empleados);
    }
}
