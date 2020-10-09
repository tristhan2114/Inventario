package com.denisse.implemento.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Base64;
import android.util.TypedValue;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.denisse.implemento.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ActivityFragmentUtils {

    public static Locale localeDefault = new Locale("es", "ES");
    public static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", localeDefault);
    public static DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", localeDefault);
    public static TimeZone timeZoneDefault = TimeZone.getTimeZone("EST");

    // inicio carga fragmento
    public static void changeFragment(boolean isBack, FragmentManager fragmentManager, Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(isBack){
            fragmentTransaction.addToBackStack(null);
        }else{
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
    // fin carga fragmento



    // inicio oculta teclado
    public static void hideTeclado(Context context, TextView textView){
        Activity activity = (Activity)context ;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    // fin oculta teclado



    // inicio mensaje dialogo
    public static AlertDialog ShowMessage(String mensaje, Context context, final onClickDialog onClick) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(Html.fromHtml("<font color='#2a2a2a' style='font-size: 10px'>" + mensaje + "</font>"));
            builder.setPositiveButton(
                    "Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            onClick.onClickDialog(dialog, id);
                        }
                    });
            builder.setNegativeButton(
                    "Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
            builder.setCancelable(false);

            AlertDialog dialog = builder.create();
            dialog.show();

            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            positiveButton.setTextColor(context.getResources().getColorStateList(R.color.colorPrimary));

            negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            negativeButton.setTextColor(context.getResources().getColorStateList(R.color.colorPrimary));
            return dialog;
        }catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }

    public static AlertDialog ShowMessageDefault(String mensaje, Context context, final onClickDialog onClick) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(Html.fromHtml("<font color='#2a2a2a' style='font-size: 10px'>" + mensaje + "</font>"));
            builder.setPositiveButton(
                    "Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            onClick.onClickDialog(dialog, id);
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            positiveButton.setTextColor(context.getResources().getColorStateList(R.color.colorPrimary));

            return dialog;
        }catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }





    public interface onClickDialog {
        void onClickDialog(DialogInterface dialog, int id);
    }
    // fin mensaje dialogo


    // inicio permiso galeria
    public static void permisosGaleria(final Activity activity) {
        Dexter.withActivity(activity).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.isAnyPermissionPermanentlyDenied()) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", activity.getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).onSameThread().check();
    }
    // fin permiso galeria



    // convert image Bitmap
    public static Bitmap getBitmapImg(Context context, Uri cd){
        Bitmap bitmap = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(cd);
            bitmap = BitmapFactory.decodeStream(inputStream);
            float aspectRatio = bitmap.getWidth() /
                    (float) bitmap.getHeight();
            int width = 480;
            int height = Math.round(width / aspectRatio);
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // convert image string base64
    public static String getBStringBase64Img(Context context, ImageView imageView){
        String base64 = "";
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] image = stream.toByteArray();
            base64 = Base64.encodeToString(image, 0);
        } catch (Exception e) {
            e.printStackTrace();
            base64 = "-";
        }
        return base64;
    }

    // convert String base64 a Bitmap
    public static Bitmap getBitmapBase64(Context context, String base64){
        Bitmap decodedImage = null;
        try {
            if(base64!=null && !base64.equals("-")){
                byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                Bitmap imageScaled = Bitmap.createScaledBitmap(decodedImage, 80, 80, false);
                imageScaled.compress(Bitmap.CompressFormat.JPEG, 5, stream);
            }else{
                decodedImage = bitmapDefault(context);
            }
        }catch (Exception e){
            decodedImage = bitmapDefault(context);
        }
        return decodedImage;
    }

    // Bitmap default
    static Bitmap bitmapDefault(Context context){
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_profile_default_dr);
        Bitmap result = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return result;
    }



    public static boolean isConnetionNetwork(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null){
                return networkInfo.isConnected();
            }
        }
        return false;
    }

    public static Calendar minDate(){
        Calendar minDate = Calendar.getInstance();
        minDate.set(2010, minDate.get(Calendar.MONTH), minDate.get(Calendar.DAY_OF_MONTH));
        return minDate;
    }

    public static Calendar maxDate(){
        Calendar minDate = Calendar.getInstance();
        minDate.set(2010, minDate.get(Calendar.MONTH), minDate.get(Calendar.DAY_OF_MONTH));
        Calendar maxDate = Calendar.getInstance();
        maxDate.set(2076, minDate.get(Calendar.MONTH) + 1, minDate.get(Calendar.DAY_OF_MONTH));
        return maxDate;
    }

    public static String getDateOnly(long time) {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(time);
    }

    public static String getDateOnly1(long time) {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(time);
    }

    public static String getDateAndTime(long time) {
        SimpleDateFormat sample = new SimpleDateFormat("dd MMM yyyy, hh:mma", Locale.getDefault());
        return sample.format(new Date(time));
    }

    public static String getTimeOnly(long time) {
        SimpleDateFormat sample = new SimpleDateFormat("hh:mma", Locale.getDefault());
        return sample.format(time);
    }

    public static String getDateNowStr() {
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeZone(timeZoneDefault);
        Date date = calendarNow.getTime();
        return dateFormat.format(date);
    }

    public static String getDateNowFB1() {
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeZone(timeZoneDefault);
        Date date = new Date();
        calendarNow.setTime(date);
        return dateFormat1.format(calendarNow.getTime());
    }

    public static Date getDateNow() {
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeZone(timeZoneDefault);
        Date today = calendarNow.getTime();
        return today;
    }

    public static String getDayNameNumber(String fecha, String opcion) {
        String date_ = "";
        String fechaS[] = fecha.split("/");

        int dia = Integer.parseInt(fechaS[0]);
        int mes = Integer.parseInt(fechaS[1]);
        int anio = Integer.parseInt(fechaS[2]);

        if (opcion.equals("day_number")) {
            date_ = fechaS[0];
        }
        if (opcion.equals("Month")) {
            SimpleDateFormat outFormat = new SimpleDateFormat("MMMM", localeDefault);
            outFormat.setTimeZone(timeZoneDefault);
            Date date = new Date(anio, (mes - 1), dia - 1);
            date_ = outFormat.format(date);
        }
        if (opcion.equals("day_name")) {
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE", localeDefault);
            outFormat.setTimeZone(timeZoneDefault);
            Date date = new Date(anio, (mes - 1), dia - 1);
            date_ = outFormat.format(date);
        }
        if (opcion.equals("years")) {
            date_ = fechaS[2];
        }
        return date_;
    }

    public static  Date getFechaInitBefore(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -3);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.setTimeZone(timeZoneDefault);
        Date startDate = cal.getTime();
        return startDate;
    }

    public static  Date getFechaFin(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.setTimeZone(timeZoneDefault);
        Date finDate = cal.getTime();
        return finDate;
    }

    public static String ucFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }

}
