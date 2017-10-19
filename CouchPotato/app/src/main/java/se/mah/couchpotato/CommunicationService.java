package se.mah.couchpotato;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Robin Johnsson & Jonatan Fridsten
 */

public class CommunicationService extends Service {

    private RunOnThread thread;
    private Buffer<String> buffer;
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

    public void run(){
        thread.start();
    }

    public class LocalService extends Binder {

        public CommunicationService getService(){
            return CommunicationService.this;
        }

    }

    public void sendToURL(String url){
        task.execute(url);
    }

    private class BackgroundTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            URL url;
            try {
                url = new URL("http://api.tvmaze.com/singlesearch/shows?q=girls");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inStream = new BufferedInputStream(connection.getInputStream());
                response = inStream.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("TESTING","In service, backgroundTask, response from GET-request is: " + result);
            super.onPostExecute(result);
        }
    }

}
