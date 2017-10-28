package se.mah.couchpotato;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
import java.net.URLConnection;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

import se.mah.couchpotato.activitytvshow.ActivityTvShow;
import se.mah.couchpotato.activitytvshow.RatingListener;

/**
 * @author Robin Johnsson & Jonatan Fridsten <---- Look at these 2 gays
 */

public class CommunicationService extends Service {
    
    private SearchTask searchTask;
    private FavoriteTask favoriteTask;
    private ScheduleTask scheduleTask;
    private AllEpisodesTask allEpisodesTask;
    private ImageLoader imLoader;
    private RatingTask ratingTask;
    private String Omdb_api_key;
    private HttpURLConnection urlConnection;
    private MainActivity activity;
    private ActivityTvShow tvActivity;
    private ObjectMapper mapper;
    private UrlBuilder urlBuilder;
    private ArrayDeque<AsyncTask> networkQue = new ArrayDeque<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("CommunicationService", "In onBind");
        mapper = new ObjectMapper();
        urlBuilder = new UrlBuilder();
        return new LocalService();
    }

    public void setActivity(MainActivity activity) {
        Log.d("CommunicationService","adding reference");
        this.activity = activity;
    }

    public void setTvActivity(ActivityTvShow activity) {
        tvActivity = activity;
    }


    public void executeCommands() {
        while(networkQue.size() != 0){
            AsyncTask task = networkQue.pop();
            task.execute(new String[]{""});
        }
    }


    public class LocalService extends Binder {
        public CommunicationService getService(MainActivity activity, String api_key) {
            Omdb_api_key = api_key;
            setActivity(activity);
            Log.d("CommunicationService", "In getService");
            return CommunicationService.this;
        }

        public CommunicationService getService(ActivityTvShow activity) {
            setTvActivity(activity);
            Log.d("CommunicationService", "In getService TV");
            return CommunicationService.this;
        }
    }

    public void getAllEpisodes(String id, AllEpisodesListener listener){
        allEpisodesTask = new AllEpisodesTask(id, listener);
        if(!((MainActivity)activity).getNetworkProblem())
            allEpisodesTask.execute();
        else
            networkQue.add(allEpisodesTask);

    }

    public void sendSearchTask(String searchParam) {
        searchTask = new SearchTask(searchParam);
        if(!((MainActivity)activity).getNetworkProblem())
            searchTask.execute();
        else
            networkQue.add(searchTask);
    }

    public void sendGetFavorite(String id, FavoriteListener callback){
        favoriteTask = new FavoriteTask(callback, id);
        if(!((MainActivity)activity).getNetworkProblem())
            favoriteTask.execute();
        else
            networkQue.add(favoriteTask);
    }

    public void sendSchedule(String country){
        scheduleTask = new ScheduleTask(country);
        if(!((MainActivity)activity).getNetworkProblem())
            scheduleTask.execute();
        else
            networkQue.add(scheduleTask);
    }

    public void getRating(String id, RatingListener ratingListener){
        ratingTask = new RatingTask(id, ratingListener);
        if(!((MainActivity)activity).getNetworkProblem())
            ratingTask.execute();
        else
            networkQue.add(ratingTask);
    }

    public void downloadPicture(String id, PosterListener posterListener, String url){
        imLoader = new ImageLoader(id, posterListener, url);
        if(!((MainActivity)activity).getNetworkProblem())
            imLoader.execute();
        else
            networkQue.add(imLoader);
    }

//    public void sendEpisodeTask(String id, String season, String episode, EpisodeListener episodeListener) {
//        EpisodeLoader epLoader = new EpisodeLoader(id, episode, season, episodeListener);
//        epLoader.execute();
//    }

    private class FavoriteTask extends AsyncTask<String, String, TvShow> {

        private FavoriteListener callback;
        private String id;

        public FavoriteTask(FavoriteListener callback, String id) {
            this.callback = callback;
            this.id = id;
        }

        @Override
        protected TvShow doInBackground(String... params) {
            String response = "";
            URL url;
            JSONObject jsonObject = null;
            BufferedReader br = null;
            InputStream inStream = null;
            try {
                url = new URL(UrlBuilder.SHOW_BY_ID + id);
                urlConnection = (HttpURLConnection) url.openConnection();
                inStream = new BufferedInputStream(urlConnection.getInputStream());
                br = new BufferedReader((new InputStreamReader(inStream)));
                response = br.readLine();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
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

            TvShow newShow = new TvShow();
            newShow.setId(Integer.parseInt(id));
            try {
                jsonObject = new JSONObject(response);
            }catch (JSONException e){ }
            try {
                newShow.setShow(mapper.readValue(jsonObject.toString(), TvShow.Show.class));
                Log.d("COMMUNICATIONSERVICE", "SUCCESSFULL PARSING" + newShow.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return newShow;
        }

        @Override
        protected void onPostExecute(TvShow tvShow) {
            callback.onFavoriteRecieved(tvShow);
        }
    }

    private class SearchTask extends AsyncTask<String, String, ArrayList<TvShow>> {

        private String searchParam;

        public SearchTask(String searchParam){
            this.searchParam = searchParam;
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

            private String countryCode;

            public ScheduleTask(String countryCode){
                this.countryCode = countryCode;
            }

            protected ArrayList<TvShow> doInBackground(String... strings) {
                URL url;
                String response = "";
                ArrayList<TvShow> resultList;
                String fullUrl = urlBuilder.getScheduleByCountry(countryCode);
                JSONArray jsonArray = null;
                BufferedReader br = null;
                InputStream instream = null;
                try {
                    url = new URL(fullUrl);
                    URLConnection urlConnection = (HttpURLConnection) url.openConnection();
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
                super.onPostExecute(tvShows);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }
        }

        public class ImageLoader extends AsyncTask<String, String, Bitmap>{

            private String id, urlString;
            private PosterListener posterListener;

            public ImageLoader(String id, PosterListener posterListener, String url){
                this.id = id;
                this.posterListener = posterListener;
                this.urlString = url;
            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                URL url;
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

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;

                    bitmap = BitmapFactory.decodeStream(br, null, options);

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
                posterListener.onPosterDownloaded(id, bitmap);
                super.onPostExecute(bitmap);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }
        }

        public class EpisodeLoader extends AsyncTask<String, String, TvShow>{

            private String id;
            private String episodeNumber;
            private String season;
            private EpisodeListener episodeListener;

            public EpisodeLoader(String id, String episodeNumber, String season, EpisodeListener episodeListener){
                this.id = id;
                this.episodeNumber = episodeNumber;
                this.season = season;
                this.episodeListener = episodeListener;
            }

            @Override
            protected TvShow doInBackground(String... strings) {
                URL url;
                String response = "";
                String fullUrl = urlBuilder.getEpisodeByNumber(id, season, episodeNumber);
                JSONObject fetchedObject = null;
                BufferedReader br = null;
                InputStream instream = null;
                try {
                    url = new URL(fullUrl);
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

                TvShow episodeObject = null;
                try {
                    fetchedObject = new JSONObject(response);
                    episodeObject = mapper.readValue(fetchedObject.toString(), TvShow.class);
                    Log.v("COMMSERVICE", episodeObject.getName() + " " + episodeObject.getSeason() + " " + episodeObject.getNumber());



                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v("COMMSERVICE", "SOMETHING WENT WRONG IN READING JSON");
                }
                return episodeObject;

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(TvShow tvShow) {
                episodeListener.onEpisodeRetrieved(tvShow);
                super.onPostExecute(tvShow);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }
        }


        public class AllEpisodesTask extends AsyncTask<String, String, ArrayList<EpisodeObject>>{

            private String id;
            private AllEpisodesListener listener;

            public AllEpisodesTask(String id, AllEpisodesListener listener){
                this.id = id;
                this.listener = listener;
            }

            @Override
            protected ArrayList<EpisodeObject> doInBackground(String... strings) {
                URL url;
                String response = "";
                String fullUrl = urlBuilder.getEpisodeList(id);
                ArrayList<EpisodeObject> allEpisodes = new ArrayList<>();
                HttpURLConnection httpUrlConnection = null;
                JSONArray episodeArray = null;
                BufferedReader br = null;
                InputStream instream = null;
                try {
                    url = new URL(fullUrl);
                    httpUrlConnection = (HttpURLConnection) url.openConnection();
                    instream = new BufferedInputStream(httpUrlConnection.getInputStream());
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
                    if (httpUrlConnection != null) {
                        try {
                            httpUrlConnection.disconnect();
                        } catch (NullPointerException e) {

                        }
                    }
                }
                try {
                    episodeArray = new JSONArray(response);
                    JSONObject temp;
                    for (int i = 0; i < episodeArray.length(); i++) {
                        temp = (JSONObject) episodeArray.get(i);
                        allEpisodes.add(mapper.readValue(temp.toString(), EpisodeObject.class));
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    Log.v("COMMSERVICE", "SOMETHING WENT WRONG IN READING JSON");
                }

                return allEpisodes;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(ArrayList<EpisodeObject> tvShows) {
                listener.onEpisodesRetrieved(tvShows);
                super.onPostExecute(tvShows);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }
        }

    public class RatingTask extends AsyncTask<String, String, OmdbObject>{

        private String id;
        private RatingListener ratingListener;

        public RatingTask(String id, RatingListener ratingListener){
            this.id = id;
            this.ratingListener = ratingListener;
        }

        @Override
        protected OmdbObject doInBackground(String... strings) {
            URL url;
            String response = "";
            String fullUrl = urlBuilder.ratingById(id);
            fullUrl += Omdb_api_key;
            OmdbObject obj = null;
            HttpURLConnection httpUrlConnection = null;
            JSONObject temp;
            BufferedReader br = null;
            InputStream instream = null;
            try {
                url = new URL(fullUrl);
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                instream = new BufferedInputStream(httpUrlConnection.getInputStream());
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
                if (httpUrlConnection != null) {
                    try {
                        httpUrlConnection.disconnect();
                    } catch (NullPointerException e) {

                    }
                }
            }

            Log.v("RatingTASK", "GOT FOLLOWING " + response);

            try {
                temp = new JSONObject(response);
                obj = mapper.readValue(temp.toString(), OmdbObject.class);

            }catch (Exception e) {
                e.printStackTrace();
                Log.v("COMMSERVICE", "SOMETHING WENT WRONG IN READING JSON");
            }

            return obj;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(OmdbObject omdbObject) {
            ratingListener.onRaitingRecieved(omdbObject);
            super.onPostExecute(omdbObject);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }



}