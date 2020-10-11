package com.denisse.implemento.Utils.EntregaUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.denisse.implemento.Adapter.AdapterEntregaList;
import com.denisse.implemento.Model.Entrega.EntregaModel;
import com.denisse.implemento.Model.Implemento;
import com.denisse.implemento.Utils.ActivityFragmentUtils;
import com.denisse.implemento.Utils.Alarma.AlarmaUtils;
import com.denisse.implemento.Utils.Constantes;
import com.denisse.implemento.Utils.ImplementoUtils.FirebaseImplemento;
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

    public static void getEntregas(Context context, FbRsEntregas rsEntregas){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_ENTREGAS);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.exists()) {
                    try {
                        List<EntregaModel> entregaModels = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            EntregaModel entregaModel = child.getValue(EntregaModel.class);
                            entregaModels.add(entregaModel);
                        }
                        rsEntregas.isSuccesError(false, "ok", entregaModels);
                    }catch (Exception e){
                        Log.e("Error-","catch "+e.getMessage());
                        rsEntregas.isSuccesError(true, "No hay entregas", new ArrayList<>());
                    }
                }else {
                    Log.e("Error-", "oool else");
                    rsEntregas.isSuccesError(true, "No hay entregas", new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error-dtf", ".- "+databaseError.toString());
                progressDialog.dismiss();
                rsEntregas.isSuccesError(true, "No hay entregas e-database", new ArrayList<>());
            }
        });
    }

    public static void getEntregasByID(Context context, String id, FbRsEntregas rsEntregas){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_ENTREGAS);
        Query myTopPostsQuery = databaseReference.orderByChild("id").equalTo(id);
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.exists()) {
                    try {
                        List<EntregaModel> entregaModels = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            EntregaModel entregaModel = child.getValue(EntregaModel.class);
                            entregaModels.add(entregaModel);
                        }
                        rsEntregas.isSuccesError(false, "ok", entregaModels);
                    }catch (Exception e){
                        Log.e("Error-","catch "+e.getMessage());
                        rsEntregas.isSuccesError(true, "No hay entregas", new ArrayList<>());
                    }
                }else {
                    Log.e("Error-", "oool else");
                    rsEntregas.isSuccesError(true, "No hay entregas", new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error-dtf", ".- "+databaseError.toString());
                progressDialog.dismiss();
                rsEntregas.isSuccesError(true, "No hay entregas e-database", new ArrayList<>());
            }
        });
    }

    public static void getEntregasByParams(Context context, String tipo_entrega, long fechInicial, long fechFin, FbRsEntregas rsEntregas){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_ENTREGAS);
        Query myTopPostsQuery = databaseReference
                .orderByChild("fecha_time_creacion").startAt(fechInicial).endAt(fechFin);
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.exists()) {
                    try {
                        List<EntregaModel> entregaModels = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            EntregaModel entregaModel = child.getValue(EntregaModel.class);
                            entregaModels.add(entregaModel);
                        }
                        rsEntregas.isSuccesError(false, "ok", entregaModels);
                    }catch (Exception e){
                        Log.e("Error-","catch "+e.getMessage());
                        rsEntregas.isSuccesError(true, "No hay entregas", new ArrayList<>());
                    }
                }else {
                    Log.e("Error-", "oool else");
                    rsEntregas.isSuccesError(true, "No hay entregas", new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error-dtf", ".- "+databaseError.toString());
                progressDialog.dismiss();
                rsEntregas.isSuccesError(true, "No hay entregas e-database", new ArrayList<>());
            }
        });
    }

    public static void createEntrega(Context context, EntregaModel entregaModel, FbRsEntregas rsEmpleado){
        loading(context);
        DatabaseReference databaseReference = getmDatabaseReferenceSave();
        String id = databaseReference.push().getKey();
        entregaModel.setId(id);
        Map<String, Object> valuedata = entregaModel.toMap();
        Map<String, Object> objectdata = new HashMap<>();
        objectdata.put(Constantes.REQUEST_ENTREGAS + id + "/", valuedata);
        String mensaje = "Realizar entrega a: ";
        if(entregaModel.getTipo_entrega().equals("departamento")){
            mensaje+=" "+ ActivityFragmentUtils.ucFirst(entregaModel.getDepartamento().getDescripcion().toLowerCase());
        }else if(entregaModel.getTipo_entrega().equals("area")){
            mensaje+=" "+ ActivityFragmentUtils.ucFirst(entregaModel.getPuesto().getDescripcion().toLowerCase());
        }else if(entregaModel.getTipo_entrega().equals("empleado")){
            mensaje+=" "+ entregaModel.getEmpleado().getNombres() + " "+entregaModel.getEmpleado().getApellidos();
        }
        mensaje +="\n En la siguiente fecha: "+ AdapterEntregaList.getFechaFormat(entregaModel.getFecha_entrega());
        AlarmaUtils.createAlarma(entregaModel.getTipo_entrega(), mensaje, id, entregaModel.getFecha_time_entrega());
        databaseReference.updateChildren(objectdata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseImplemento.updateStockImplementos(context, entregaModel.getEntregaItems(), new FirebaseImplemento.FbRsImplemento() {
                        @Override
                        public void isSuccesError(boolean isSucces, String msg, List<Implemento> implementos) {
                            progressDialog.dismiss();
                            rsEmpleado.isSuccesError(false, "Entrega regitrada con exito.", new ArrayList<>());
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    rsEmpleado.isSuccesError(true, "Hubo un error al registrar entrega.", new ArrayList<>());
                }
            }
        });
        databaseReference.keepSynced(true);
    }

    public static void updateEntrega(EntregaModel entregaModel){
        DatabaseReference databaseReference = getmDatabase()
                .getReference(Constantes.REQUEST_ENTREGAS)
                .child(String.valueOf(entregaModel.getId()));
        Map<String, Object> valuedata = entregaModel.toMap();
        databaseReference.updateChildren(valuedata);
        databaseReference.keepSynced(true);
    }


    public static void deleteImplemento(Context context, String id, FbRsEntregas rsEntregas){
        loading(context);
        DatabaseReference databaseReference = getmDatabase().getReference(Constantes.REQUEST_ENTREGAS).child(id);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    rsEntregas.isSuccesError(false, "Elemento eliminado", new ArrayList<>());
                }else{
                    rsEntregas.isSuccesError(true, "Error al eliminar elemento", new ArrayList<>());
                }
            }
        });
    }

    public interface FbRsEntregas {
        void isSuccesError(boolean isSucces, String msg, List<EntregaModel> entregaModels);
    }
}
