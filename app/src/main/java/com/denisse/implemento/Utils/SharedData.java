package com.denisse.implemento.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedData {

    public static String KEYPREFERENCES = "tesispreferences";

    public static String SESION = "sesion";
    public static String NAME_USER = "name_user";
    public static String ROL = "rol";


    public static String CORREO_DEFAULT = "proyectotesisapp@gmail.com";
    public static String CONTRASENIA_DEFAULT = "proyectotesis1234";

    public static String getKey(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedData.KEYPREFERENCES, context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getString(key, "");
        }
        return null;
    }

    public static void saveKey(SharedPreferences sharedPreferences,String key,String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static void clearSharedPreferences(Context ctx){
        SharedPreferences settings = ctx.getSharedPreferences(SharedData.KEYPREFERENCES, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

}
