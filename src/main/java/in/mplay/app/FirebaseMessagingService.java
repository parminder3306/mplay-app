package in.mplay.app;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;


import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import in.mplay.app.activites.MainActivity;


/**
 * Created by User on 2/20/2017.
 */

@SuppressLint("Registered")
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService  {
    Notification.Builder notificationBuilder;

    public FirebaseMessagingService() {
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle(); //get title
            String message = remoteMessage.getNotification().getBody(); //get message
            String click_action = remoteMessage.getNotification().getClickAction(); //get click_action;
            Uri imgurl = remoteMessage.getNotification().getImageUrl(); //image;
            sendNotification(title, message,click_action,imgurl.toString());
        }
    }

    private void sendNotification(String title, String messageBody, String click_action, String imageurl) {
        Intent intent = null;

        if(click_action.equals("MainActivity")){
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_ONE_SHOT);

        if(imageurl.equals("null")) {
           notificationBuilder = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
        }else{
            Bitmap banner = null;
            try {
                InputStream ban = new URL(imageurl).openStream();
                banner = BitmapFactory.decodeStream(ban);
            } catch (IOException e) {
                e.printStackTrace();
            }
            notificationBuilder = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(FirebaseMessagingService.this.getResources(), R.mipmap.ic_launcher))
                    .setStyle(new Notification.BigPictureStyle().bigPicture(banner));
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 , notificationBuilder.build());
    }
}