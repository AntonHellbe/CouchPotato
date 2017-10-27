package se.mah.couchpotato;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by robin on 27/10/2017.
 */

public class NotificationReciever extends BroadcastReceiver {
    public NotificationReciever(){}
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,NotificationService.class);
        Log.d("NOTIFICATIONTEST", "In reciver");
        context.startService(intent1);
    }
}
