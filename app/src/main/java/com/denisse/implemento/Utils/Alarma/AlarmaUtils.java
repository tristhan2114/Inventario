package com.denisse.implemento.Utils.Alarma;

import android.support.annotation.NonNull;
import android.util.Log;

import com.denisse.implemento.Model.Entrega.EntregaModel;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.Constantes;
import com.denisse.implemento.Utils.EntregaUtils.FirebaseEntrega;
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

public class AlarmaUtils {

    public static FirebaseDatabase getmDatabase(){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        return mDatabase;
    }

    public static DatabaseReference getmDatabaseReferenceSave() {
        DatabaseReference mDatabaseReference = getmDatabase().getReference();
        return mDatabaseReference;
    }

    public static void createAlarma(String titulo, String mensaje, String idEntrega, long fecha_time ){
        DatabaseReference databaseReference = getmDatabaseReferenceSave();
        String id = databaseReference.push().getKey();
        Alarma alarma = new Alarma();
        alarma.setId(id);
        alarma.setFecha_creacion(ActivityFragmentUtils.getDateNowFB1());
        alarma.setTitulo("Entrega a "+titulo);
        alarma.setMensaje(mensaje);
        alarma.setType_entrega(titulo);
        alarma.setId_entrega(idEntrega);
        alarma.setFecha_entrega_time(fecha_time);
        alarma.setEstado(true);
        Map<String, Object> valuedata = alarma.toMap();
        Map<String, Object> objectdata = new HashMap<>();
        objectdata.put(Constantes.REQUEST_ALARMAS + id + "/", valuedata);
        databaseReference.updateChildren(objectdata);
    }

    public static void updateAlarma(EntregaModel entregaModel){
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_ALARMAS);
        Query query = databaseReference.orderByChild("id_entrega").equalTo(entregaModel.getId()).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        String id = "";
                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                            id = (String) messageSnapshot.child("id").getValue();
                        }

                        if(id != null){
                            DatabaseReference dbReferenceUpdate = getmDatabase().getReference(Constantes.REQUEST_ALARMAS).child(id);
                            dbReferenceUpdate.child("estado").setValue(false);
                            entregaModel.setIs_create(true);
                            entregaModel.setIs_entregado(true);
                            FirebaseEntrega.updateEntrega(entregaModel);
                        }

                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error-fb", "err- "+databaseError.getMessage());
            }
        });
    }

    public static void getAlarma(long fechInicial, long fechFin, FbAlarmEntregas fbAlarmEntregas){
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_ALARMAS);
        Query query = databaseReference.orderByChild("fecha_entrega_time").startAt(fechInicial).endAt(fechFin);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    try {
                        List<Alarma> alarmas = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Alarma alarma = child.getValue(Alarma.class);
                            //Log.e("Error-",".3. "+alarma.toString());
                            alarmas.add(alarma);
                        }
                        fbAlarmEntregas.rsAlarm(false, "ok", alarmas);
                    }catch (Exception e){
                        Log.e("Error-frcat", "errcat "+e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error-frgt", "eergt "+databaseError.getMessage());
            }
        });
    }


    public interface FbAlarmEntregas{
        void rsAlarm(boolean isSucces, String msg, List<Alarma> list);
    }

}
