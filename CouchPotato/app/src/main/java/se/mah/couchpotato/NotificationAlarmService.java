package se.mah.couchpotato;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import se.mah.couchpotato.activitytvshow.ActivityTvShow;
import se.mah.couchpotato.activtysettings.Settings;

public class NotificationAlarmService extends Service {

    private boolean isRunning = false;

    public NotificationAlarmService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startAlarm();
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void startAlarm(){
        long notificationTime = 18 * 60 * 60 * 1000;
        SharedPreferences sp;
        if ((sp = getSharedPreferences("MainActivity", MODE_PRIVATE)).contains(Settings.NOTIFICATION_TIME)) {
            notificationTime = sp.getLong(Settings.NOTIFICATION_TIME, 0);
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.setTimeInMillis(c.getTimeInMillis() + notificationTime);

        Intent intent = new Intent(this, MrReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,  c.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pendingIntent);
        Log.d("NOTIFICATIONTEST", "alarm set to go off: " + c.getTime());
    }

    public static class MrReciever extends BroadcastReceiver {

        public MrReciever() {
        }

        public void sendNotification(Context context, String message) {
            Log.v("NOTIFICATIONTEST","HELLO????");
            SharedPreferences sp = context.getSharedPreferences("MainActivity", MODE_PRIVATE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(android.R.drawable.sym_def_app_icon).setContentTitle(context.getResources().getString(R.string.app_name)).setContentText(message);
            Intent resultIntent = new Intent(context, MainActivity.class);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 2, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);
            int id = 001;
            NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notifyManager.notify(id, mBuilder.build());
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("NOTIFICATIONTEST", "HELLO I IS RECIEVE");
            Toast.makeText(context, "HELLO", Toast.LENGTH_SHORT).show();
            new NotificationScheduleTask(context).execute();
        }

        public class NotificationScheduleTask extends AsyncTask<String, String, ArrayList<TvShow>> {

            private HttpURLConnection urlConnection;
            private ObjectMapper mapper = new ObjectMapper();
            private ArrayList<String> favoriteIds = new ArrayList<>();
            private Context context;
            private String countryCode = "US";

            public NotificationScheduleTask(Context context) {
                this.context = context;
            }

            @Override
            protected void onPreExecute() {
                SharedPreferences sp = context.getSharedPreferences("MainActivity", MODE_PRIVATE);
                if (sp.contains("favourites")) {
                    favoriteIds = new ArrayList<>(sp.getStringSet("favourites", null));
                    countryCode = sp.getString(Settings.COUNT, "US");
                }
                super.onPreExecute();
            }

            @Override
            protected ArrayList<TvShow> doInBackground(String... strings) {
                URL url;
                String response = "";
                ArrayList<TvShow> resultList;
                JSONArray jsonArray = null;
                BufferedReader br = null;
                InputStream instream = null;
                try {
                    url = new URL(UrlBuilder.TODAYS_SCHEDULE + countryCode);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    instream = new BufferedInputStream(urlConnection.getInputStream());
                    br = new BufferedReader(new InputStreamReader(instream));
                    response = br.readLine();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();

                        } catch (IOException e) {

                        }
                    }
                    if (instream != null) {
                        try {
                            instream.close();
                        } catch (IOException e) {

                        }
                    }
                    if (urlConnection != null) {
                        try {
                            urlConnection.disconnect();
                        } catch (NullPointerException e) {

                        }
                    }
                }
                TvShow[] arr = null;
                try {
                    jsonArray = new JSONArray(response);
                    arr = new TvShow[jsonArray.length()];
                    JSONObject t;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        t = (JSONObject) jsonArray.get(i);
                        arr[i] = mapper.readValue(t.toString(), TvShow.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v("COMMSERVICE", "SOMETHING WENT WRONG IN READING JSON");
                }

                resultList = new ArrayList<>(Arrays.asList(arr));

                return resultList;
            }

            @Override
            protected void onPostExecute(ArrayList<TvShow> shows) {
                super.onPostExecute(shows);
                ArrayList<TvShow> favoritesAiring = new ArrayList<>();
                Log.v("NOTIFICATIONTEST","gonna create notification");
                if (favoriteIds != null) {
                    for (int i = 0; i < shows.size(); i++) {
                        for (int j = 0; j < favoriteIds.size(); j++) {
                            if (favoriteIds.get(j).equals(shows.get(i).getShow().getId().toString())) {
                                favoritesAiring.add(shows.get(i));
                                break;
                            }
                        }
                    }
                    if (!favoritesAiring.isEmpty()) {
                        Log.v("NOTIFICATIONTEST","LETS DO IT");
                        if (favoritesAiring.size() > 2) {
                            String message = context.getResources().getString(R.string.notification_description);
                            sendNotification(context, message);
                        } else {
                            String message = favoritesAiring.get(0).getShow().getName() + (favoritesAiring.size() == 2 ? " and " + favoritesAiring.get(1).getShow().getName() + " " : " ");
                            message += context.getResources().getString(R.string.notification_airing_today);
                            sendNotification(context, message);
                        }
                    }
                }
            }
        }
    }
}
