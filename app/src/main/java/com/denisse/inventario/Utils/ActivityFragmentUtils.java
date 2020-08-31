package com.denisse.inventario.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.denisse.inventario.Model.Empleado.Area;
import com.denisse.inventario.Model.Inventario;
import com.denisse.inventario.R;
import com.denisse.inventario.Utils.InventarioUtils.FirebaseInventario;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class ActivityFragmentUtils {

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



    static Area area = null;
    static String codigo = "", descripcion = "", tipo = "";
    public static void DialogInfService(final Context context) {
        final Dialog MyDialog = new Dialog(context);
        CardView btnOk;
        ImageButton btnClose;

        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//quita titulo a los cuadros de dialogos
        MyDialog.setContentView(R.layout.dialogo_create_inventario);
        MyDialog.setCancelable(false);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText txtCodigo = MyDialog.findViewById(R.id.txtCodigo);
        EditText txtDescripcion = MyDialog.findViewById(R.id.txtDescripcion);
        EditText txtTipo = MyDialog.findViewById(R.id.txtTipo);

        // carga de spinner
        Spinner sp_area = MyDialog.findViewById(R.id.sp_area);
        ArrayAdapter<Area> adapterArea = new ArrayAdapter<Area>(context,
                R.layout.simple_spinner_item, R.id.txtterms, Area.areas());
        sp_area.setAdapter(adapterArea);
        sp_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (!Area.areas().get(position).getDescripcion().equals("- SELECCIONE UNA OPCIÓN -")){
                        area = Area.areas().get(position);
                    }
                }catch (Exception e){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // fin carga de spinner


        btnOk = MyDialog.findViewById(R.id.btnAdd);
        btnClose = MyDialog.findViewById(R.id.btnClose);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onClickDialogService.onClickDialog(MyDialog);
                codigo = txtCodigo.getText().toString();
                descripcion = txtDescripcion.getText().toString();
                tipo = txtTipo.getText().toString();

                if(area != null && !area.getDescripcion().equals("- SELECCIONE UNA OPCIÓN -")){
                    Inventario inventario = new Inventario("0", codigo, descripcion, tipo, area);
                    FirebaseInventario.createInventrio(inventario);
                    MyDialog.dismiss();
                }else {
                    ShowMessage("Debe seleccionar un área", context, new onClickDialog() {
                        @Override
                        public void onClickDialog(DialogInterface dialog, int id) {

                        }
                    });
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog.dismiss();
            }
        });
        MyDialog.show();
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
        Bitmap result = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawColor(context.getResources().getColor(R.color.colorAccent));
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

}
