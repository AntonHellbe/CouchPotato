package se.mah.couchpotato;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Robin Johnsson & Jonatan Fridsten
 */

public class CommunicationService extends Service {

    private RunOnThread thread;
    private Buffer<JSONObject> buffer;
    private BackgroundTask task;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread = new RunOnThread();
        buffer = new Buffer();
        task = new BackgroundTask();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalService();
    }

    public void run() {
        thread.start();
    }

    public class LocalService extends Binder {

        public CommunicationService getService() {
            return CommunicationService.this;
        }

    }

    public void sendToURL(String url) {
        task = new BackgroundTask();
        task.execute(url);
    }

    private class BackgroundTask extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            String response = "";
            URL url;
            JSONObject jsonObject = null;
            HttpURLConnection connection = null;
            BufferedReader br = null;
            InputStream inStream = null;
            try {
                url = new URL("http://api.tvmaze.com/shows/1");
                Log.d("CommunicationService", "in doInBackground, message to send: " + url);
                connection = (HttpURLConnection) url.openConnection();
                inStream = new BufferedInputStream(connection.getInputStream());
                br = new BufferedReader((new InputStreamReader(inStream)));
                response = br.readLine();
                jsonObject = new JSONObject(response);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    try {
                        connection.disconnect();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.d("CommunicationService", "In service, backgroundTask, response from GET-request is: " + jsonObject.toString());
            //@TODO fixa så att det inte kan komma ett null json objekt?
            buffer.put(jsonObject);
            super.onPostExecute(jsonObject);
        }
    }

}
