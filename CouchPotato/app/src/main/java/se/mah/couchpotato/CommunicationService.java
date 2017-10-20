package se.mah.couchpotato;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

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

/**
 * @author Robin Johnsson & Jonatan Fridsten <---- Look at these 2 gays
 */

public class CommunicationService extends Service {

    private Controller controller;
    private SearchTask searchTask;
    private FavoriteTask favoriteTask;
    private ScheduleTask scheduleTask;
    private HttpURLConnection urlConnection;
    private MainActivity activity;
    private ObjectMapper mapper;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        task = new BackgroundTask();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("CommunicationService", "In onBind");
        mapper = new ObjectMapper();
        return new LocalService();
    }

    public void setActivity(MainActivity activity){
        Log.d("CommunicationService","adding reference");
        this.activity = activity;
    }


    public class LocalService extends Binder {
        public CommunicationService getService(MainActivity activity) {
            setActivity(activity);
            Log.d("CommunicationService", "In getService");
            return CommunicationService.this;
        }

    }

    public void sendSearchTask(String searchParam) {
        searchTask = new SearchTask();
        searchTask.execute(searchParam);
    }

    public void sendAddFavorite(String id){
        favoriteTask = new FavoriteTask();
        favoriteTask.execute(id);
    }

    public void sendSchedule(){
        scheduleTask = new ScheduleTask();
        scheduleTask.execute();
    }

    private class FavoriteTask extends AsyncTask<String, String, TvShow> {

        @Override
        protected TvShow doInBackground(String... params) {
            String response = "", id = params[0];
            URL url;
            JSONObject jsonObject = null;
            BufferedReader br = null;
            InputStream inStream = null;
            try {
                url = new URL(UrlBuilder.SHOW_BY_ID + id);
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
            activity.getController().favoritesReceived(tvShow);
            super.onPostExecute(tvShow);
        }
    }


    private class SearchTask extends AsyncTask<String, String, ArrayList<TvShow>> {

        @Override
        protected ArrayList<TvShow> doInBackground(String... strings) {
            URL url;
            String response = "";
            String searchParam = strings[0];
            ArrayList<TvShow> resultList;
            JSONArray jsonArray = null;
            BufferedReader br = null;
            InputStream instream = null;
            try {
                url = new URL(UrlBuilder.SHOW_SEARCH + searchParam);
                urlConnection = (HttpURLConnection) url.openConnection();
                instream = new BufferedInputStream(urlConnection.getInputStream());
                br = new BufferedReader(new InputStreamReader(instream));
                response = br.readLine();

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
            TvShow[] arr = null;
            try{
                jsonArray = new JSONArray(response);
                arr = new TvShow[jsonArray.length()];
                JSONObject t;
                for (int i = 0; i < jsonArray.length(); i++) {
                    t = (JSONObject) jsonArray.get(i);
                    arr[i] = mapper.readValue(t.toString(), TvShow.class);
                }
            }catch(Exception e){
                e.printStackTrace();
                Log.v("COMMSERVICE", "SOMETHING WENT WRONG IN READING JSON");
            }
            resultList = new ArrayList<>(Arrays.asList(arr));

            return resultList;

        }

            @Override
            protected void onPreExecute () {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute (ArrayList < TvShow > tvShows) {
                activity.getController().searchReceived(tvShows);
                super.onPostExecute(tvShows);
            }

            @Override
            protected void onProgressUpdate (String...values){
                super.onProgressUpdate(values);
            }
        }


        public class ScheduleTask extends AsyncTask<String, String, ArrayList<TvShow>> {

            protected ArrayList<TvShow> doInBackground(String... strings) {
                URL url;
                String response = "";
                ArrayList<TvShow> resultList;
                JSONArray jsonArray = null;
                BufferedReader br = null;
                InputStream instream = null;
                try {
                    url = new URL(UrlBuilder.TODAYS_SCHEDULE);
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
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(ArrayList<TvShow> tvShows) {
                activity.getController().scheduleRecieved(tvShows);
                for(TvShow t: tvShows){
                    ImageLoader imLoader = new ImageLoader();
                    imLoader.execute(t.getShow().getImage().getOriginal(), String.valueOf(t.getShow().getId()));
                }
                super.onPostExecute(tvShows);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }
        }

        public class ImageLoader extends AsyncTask<String, String, Bitmap>{

            private String id;

            @Override
            protected Bitmap doInBackground(String... strings) {
                URL url;
                String urlString = strings[0].trim();
                id = strings[1];
                Log.d("IMAGELOADER", urlString + " " +id);
                HttpURLConnection httpURLConnection = null;
                InputStream inputStream = null;
                BufferedInputStream br = null;
                Bitmap bitmap = null;
                try{
                    url = new URL(urlString);
                    Log.d("COMMSERVICE", url.toString());
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();
                    inputStream = httpURLConnection.getInputStream();
                    br = new BufferedInputStream(httpURLConnection.getInputStream());

                    bitmap = BitmapFactory.decodeStream(br);
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    if(inputStream != null){
                        try{
                            inputStream.close();
                        }catch(IOException e){

                        }
                    }

                    if(httpURLConnection != null){
                        httpURLConnection.disconnect();
                    }
                }

                return bitmap;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
//                Log.d("COMMSERVICE -BITMAP", bitmap.toString());
                //activity.getController().getDataFragment().putPictureMap(id, bitmap);
                super.onPostExecute(bitmap);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }
        }


}