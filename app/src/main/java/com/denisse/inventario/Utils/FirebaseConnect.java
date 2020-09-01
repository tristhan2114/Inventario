package com.denisse.inventario.Utils;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseConnect {

    private static boolean isConnect = false;

    public static boolean onConnect(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.e("Error-",".1. connected ");
                    isConnect = true;
                } else {
                    isConnect = false;
                    Log.e("Error-",".1. not connected ");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                isConnect = true;
                Log.e("Error-",".1. Listener was cancelled ");
            }
        });
        return isConnect;
    }

}
