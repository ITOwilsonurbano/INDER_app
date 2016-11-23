package com.itosoftware.inderandroid.Utils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Activities.ReservationDetailActivity;
import com.itosoftware.inderandroid.Api.DatesReservation.DatesReservation;
import com.itosoftware.inderandroid.Api.News.ApiNews;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.Api.Reservations.ApiReservation;
import com.itosoftware.inderandroid.Api.Reservations.Reservations;
import com.itosoftware.inderandroid.Api.Users.ApiUser;
import com.itosoftware.inderandroid.Api.Users.Participant;
import com.itosoftware.inderandroid.Api.Users.UserContainer;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.ApiUserListener;
import com.itosoftware.inderandroid.Interface.NewsListener;
import com.itosoftware.inderandroid.Interface.ReservationsListener;
import com.itosoftware.inderandroid.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GcmServiceHandler extends IntentService implements ApiUserListener, ReservationsListener {

    private Handler handler;
    public GcmServiceHandler() {
        super("GcmServiceHandler");
    }
    private NotificationManager mNotificationManager;

    public String userId;
    public String reservationId;
    public Globals globals;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("GCMServiceHandler","onHandleIntent");
        Bundle extras = intent.getExtras();
        String message = extras.getString("message");

        try {
            JSONObject json_message = new JSONObject(message);
            Log.d("message", json_message.toString());
            String type = json_message.getString("type");
            SharedPreferences settings = this.getApplicationContext().getSharedPreferences("Preferences", 0);

            if(type.equals("noticia")){
                Log.d("GcmServiceHandler","GCM tipo noticia");
                Boolean notification_news = settings.getBoolean("notification_news", true);
                if(notification_news == true){
                    String news_title = json_message.getString("news_title");
                    mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("show_news_tab", true);
                    i.putExtra("type", "notification");
                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                            i, PendingIntent.FLAG_UPDATE_CURRENT);

                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    int color = getResources().getColor(R.color.Verde);
                    Bitmap largeIcon = (((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_logo_inder)).getBitmap());

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.inder_logo)
                                    .setContentTitle("Inder")
                                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                    .setSound(soundUri)
                                    .setColor(color)
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(news_title))
                                    .setContentText(news_title)
                                    .setAutoCancel(true);

                    mBuilder.setContentIntent(contentIntent);
                    Random r = new Random();
                    int id = r.nextInt();
                    mNotificationManager.notify(id, mBuilder.build());
                }else{
                    Log.d("GcmServiceHandler => ","Notification without permission");
                }
            }
            else if(type.equals("reserva")){
                Log.d("GcmServiceHandler","GCM tipo reserva");
                Boolean notification_reservation = settings.getBoolean("notification_reservations", false);
                if(notification_reservation == true){
                    String access_tokens = settings.getString("access_token", null);
                    String refresh_tokens = settings.getString("refresh_token", null);

                    globals = (Globals) getApplication();
                    if(access_tokens != null){
                        globals.setUserIsAuthenticated(true);
                        globals.setToken(access_tokens);
                        globals.setRefreshToken(refresh_tokens);
                        HashMap params = new HashMap();
                        params.put("token",access_tokens);
                        userId = new String(extras.getString("usuario_id"));
                        reservationId = extras.getString("id");
                        ApiUser apiUser = new ApiUser(this);
                        apiUser.getProfile(params);

                    }else{
                        Log.d("GcmServiceHandler => ","User Not Login");
                    }
                }else{
                    Log.d("GcmServiceHandler => ","Notification without permission");
                }
            }

            GcmBroadcastReceiver.completeWakefulIntent(intent);


        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + message + "\"");
        }
    }


    /*** Start functions of implements ApiUserListener ***/
    @Override
    public void onFinishedAuthentication(UserContainer userContainer, Boolean authenticated) {

    }
    @Override
    public void onFinishedRegister(Boolean success, HashMap data) {

    }
    @Override
    public void onFinishedForgetPassword() {

    }
    @Override
    public void onFinishedForgetUser() {

    }
    @Override
    public void onFinishedAddParticipant(Participant participant, Integer status) {

    }
    @Override
    public void onFinishedProfile(ApiUser.Info responseData) {
        String userIdResponse = responseData.getId();
        Boolean jeje = userIdResponse == userId;
        Log.d("userIdResponse",userIdResponse.toString());
        Log.d("userId",userId.toString());
        Log.d("jeje",jeje.toString());
        if(userIdResponse.equals(userId)){
            HashMap params = new HashMap();
            params.put("id",reservationId);

            ApiReservation apiReservation = new ApiReservation(this);
            apiReservation.getReservarion(params);
        }else{
            Log.d("GcmServiceHandler => ","Notification to other user");
        }

    }

    @Override
    public void onFinishedProfilePqr(JSONObject response) {

    }
    /*** End functions of implements ApiUserListener ***/


    /*** Start functions of implements ReservationsListener ***/
    @Override
    public void onfinishedLoadReservations(ArrayList<Reservations> reservations, Integer maxPage) {

    }
    @Override
    public void onCreateReservation(Reservations reservation) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i = new Intent(this, ReservationDetailActivity.class);
        i.putExtra("reservation", reservation.toString());
        i.putExtra("type", "notification");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                i, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int color = getResources().getColor(R.color.Verde);
        Bitmap largeIcon = (((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_logo_inder)).getBitmap());

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.inder_logo)
                        .setContentTitle("Inder")
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setSound(soundUri)
                        .setColor(color)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Respuesta de solicitud de reserva"))
                        .setContentText("Respuesta de solicitud de reserva")
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        Random r = new Random();
        int id = r.nextInt();
        mNotificationManager.notify(id, mBuilder.build());
    }
    @Override
    public void onResponseDates(ArrayList<DatesReservation> date) {

    }
    @Override
    public void onFinishTerminos(String terminos) {

    }
    @Override
    public void onCancelReservation(Integer code) {

    }
    /*** End functions of implements ReservationsListener ***/

}
