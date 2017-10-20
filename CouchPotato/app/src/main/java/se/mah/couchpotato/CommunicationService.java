package se.mah.couchpotato;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Robin Johnsson & Jonatan Fridsten
 */

public class CommunicationService extends Service {

    private Controller controller;
    private SearchTask task;
    private HttpURLConnection urlConnection;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        task = new BackgroundTask();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("CommunicationService", "In onBind");
        return new LocalService();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public class LocalService extends Binder {

        public CommunicationService getService() {
            Log.d("CommunicationService", "In getService");
            return CommunicationService.this;
        }

    }

    public void sendToURL(String url) {
        task = new SearchTask();
        task.execute(url);
    }

    private class BackgroundTask extends AsyncTask<String, String, TvShow> {

        @Override
        protected TvShow doInBackground(String... params) {
            String response = "";
            URL url;
            JSONObject jsonObject = null;
            BufferedReader br = null;
            InputStream inStream = null;
            try {
                url = new URL("http://api.tvmaze.com/search/shows?q=girls");
                Log.d("CommunicationService", "in doInBackground, message to send: " + url);
                urlConnection = (HttpURLConnection) url.openConnection();
                inStream = new BufferedInputStream(urlConnection.getInputStream());
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
                if (urlConnection != null) {
                    try {
                        urlConnection.disconnect();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }

            TvShow newShow = null;
            try {
                newShow = new ObjectMapper().readValue(jsonObject.toString(), TvShow.class);
                Log.d("COMMUNICATIONSERVICE", "SUCCESSFULL PARSING" + newShow.toString());
            } catch (Exception e) {
                Log.d("COMMUNICATIONSERVICE", "ERROR PARSING TVSHOW");
            }


            return newShow;
        }

        @Override
        protected void onPostExecute(TvShow tvShow) {
            Log.d("CommunicationService", "In service, backgroundTask, response from GET-request is: " + tvShow.toString());
            //@TODO fixa så att det inte kan komma ett null json objekt?
            controller.recievedData(tvShow);
            super.onPostExecute(tvShow);
        }
    }


    private class SearchTask extends AsyncTask<String, String, ArrayList<TvShow>> {

        @Override
        protected ArrayList<TvShow> doInBackground(String... strings) {
            URL url;
            String response = "";
            ArrayList<TvShow> resultList = new ArrayList<>();
            TvShow resultShow;
            String searchParam = "game of thrones";
            JSONArray jsonArray = new JSONArray();
            JSONObject temp;
            BufferedReader br = null;
            InputStream instream = null;
            try {
                url = new URL(UrlBuilder.FULL_SCHEDULE);
                urlConnection = (HttpURLConnection) url.openConnection();
                instream = new BufferedInputStream(urlConnection.getInputStream());
                br = new BufferedReader(new InputStreamReader(instream));
                response = br.readLine();
                jsonArray = new JSONArray(response);

            }catch (Exception e) {
                e.printStackTrace();
            }finally{
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
            ObjectMapper mapper = new ObjectMapper();
            try{
                jsonArray = new JSONArray(response);
                Example[] arr = new Example[jsonArray.length()];
                JSONObject t;
                for (int i = 0; i < jsonArray.length(); i++) {
                    t = (JSONObject) jsonArray.get(i);
                    arr[i] = mapper.readValue(t.toString(), Example.class);
                    Log.v("GOT FOLLOWING" , arr[i].getEmbedded().getShow().toString());
                }
//                Collection<TvShow2> readValues =  mapper.readValue(response, new TypeReference<Collection<TvShow2>>() {});
//                Log.v("COMMSERVICE", showList.get(1).toString());
            }catch(Exception e){
                e.printStackTrace();
                Log.v("COMMSERVICE", "SOMETHING WENT WRONG IN READING JSON");
            }
//            for (int i = 0; i < showList.size(); i++) {
//                Log.v("COMMSERVICE", showList.get(i).toString());
//            }


//            for (int i = 0; i < jsonArray.length(); i++) {
//                try {
//                    temp = (JSONObject) jsonArray.get(i);
//                    resultShow = new ObjectMapper().readValue(temp.toString(), TvShow.class);
//                    resultList.add(resultShow);
//                }catch(Exception e){
//                    e.printStackTrace();
//
//                }
//            }

            for (int i = 0; i < resultList.size(); i++) {
                Log.v("COMMUNICATIONSERVICE", resultList.get(i).toString());
            }
            return resultList;

        }

            @Override
            protected void onPreExecute () {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute (ArrayList < TvShow > tvShows) {
                super.onPostExecute(tvShows);
            }

            @Override
            protected void onProgressUpdate (String...values){
                super.onProgressUpdate(values);
            }
        }


}