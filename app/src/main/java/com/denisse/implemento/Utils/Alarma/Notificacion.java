package com.denisse.implemento.Utils.Alarma;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.denisse.implemento.Activity.ContainerActivity;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.Constantes;
import com.denisse.implemento.Utils.SharedData;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Notificacion {

    static NotificationCompat.Builder mBuilder;
    static String channelId = "my_channel_tesis";


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void notificationAlarma(Context context, String title, String body, String idEntrega){

        if(SharedData.getKey(context, SharedData.ROL) != null){
            if (SharedData.getKey(context, SharedData.ROL).equals(Constantes.ROL_ADMINISTRADOR) ||
                    SharedData.getKey(context, SharedData.ROL).equals(Constantes.ROL_ASISTENTE)){
                Activity activity = (Activity) context;

                NotificationManager mNotifyMgr = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(activity, null);

                int importancia = NotificationManager.IMPORTANCE_HIGH;
                long[] vibration = new long[]{400, //sleep
                        600, //vibrate
                        100,300,100,150,100,75};
                Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel mchannel_ = new NotificationChannel(channelId, title, importancia);
                    mchannel_.setDescription(body);
                    mchannel_.setLightColor(R.color.colorAccent);
                    mchannel_.enableLights(true);
                    mchannel_.enableVibration(true);
                    mchannel_.setSound(null, null);
                    mchannel_.setVibrationPattern(vibration);
                    mchannel_.setShowBadge(true);
                    mchannel_.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                    v.vibrate(VibrationEffect.createWaveform(vibration, VibrationEffect.DEFAULT_AMPLITUDE));

                    mNotifyMgr.createNotificationChannel(mchannel_);
                    mBuilder = new NotificationCompat.Builder(activity, channelId);
                }
                mBuilder.setDefaults(Notification.DEFAULT_SOUND)
                        .setContentTitle(Html.fromHtml(title))
                        .setContentText(Html.fromHtml(body))
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setLights(context.getResources().getColor(R.color.colorAccent), 500, 500)
                        .setSmallIcon(getNotificationIcon())
                        .setAutoCancel(true)
                        .setContentTitle(Html.fromHtml(title)).setContentText(Html.fromHtml(body))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setContentIntent(getPendintIntent(activity, idEntrega));

                mNotifyMgr.notify(getNotificacionID(), mBuilder.build());

            }
        }
    }

    private static int getNotificacionID(){
        return new Random().nextInt(60000)*new Random().nextInt(10);
    }

    private static int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP & Build.VERSION.SDK_INT < Build.VERSION_CODES.O);
        return useWhiteIcon ? R.drawable.common_full_open_on_phone : R.drawable.common_full_open_on_phone;
    }

    private static PendingIntent getPendintIntent(Activity activity, String idEntrega) {
        PendingIntent pendingIntent = null;
        Bundle extrasA = new Bundle();
        extrasA.putString("action", "entregaNotify");
        extrasA.putSerializable("id_entrega", idEntrega);

        pendingIntent = PendingIntent
                .getActivity(activity, 0,
                        new Intent(activity, ContainerActivity.class)
                                .putExtras(extrasA).setAction(Long.toString(System.currentTimeMillis()))
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }
}
