package se.mah.couchpotato;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import se.mah.couchpotato.activitytvshow.ActivityTvShow;

public class NotificationAlarmService extends Service {
    public NotificationAlarmService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new LocalService();
    }

    public void startAlarm(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 46);

        Intent intent = new Intent(this,NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE), tester = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);
        Log.d("NOTIFICATIONTEST", "alarm started at: " + calendar.getTime());
    }

    public class LocalService extends Binder {
        public NotificationAlarmService getService(MainActivity activity) {
            return NotificationAlarmService.this;
        }
    }
}
