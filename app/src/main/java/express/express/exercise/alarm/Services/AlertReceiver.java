package express.express.exercise.alarm.Services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.workout.exercise.R;

import express.express.exercise.activity.MainActivity;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            pendingIntent = PendingIntent.getActivity(context,
                    0, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }else {
            pendingIntent = PendingIntent.getActivity(context,
                    0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        nb.setContentIntent(pendingIntent);
        notificationHelper.getManager().notify(1, nb.build());


        int tone = intent.getIntExtra("Value",0);

        if(tone == 1){
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.classic_music);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.start();
        }
        else if(tone == 2){
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.crazy_music);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.start();
        }
        else if(tone == 3){
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.iphone_music);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.start();
        }
        else if(tone == 4){
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.trumpet_music);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.start();
        }
        else {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.rap_music);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.start();
        }

    }
}