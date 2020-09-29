package com.example.itsawatch.taches;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.itsawatch.BuildConfig;
import com.example.itsawatch.activity.MainActivity;
import com.example.itsawatch.adapters.CardStackAdapter;
import com.example.itsawatch.modeles.Movie;
import com.example.itsawatch.modeles.TaskManager;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class TacheMovies extends AsyncTask<String, Movie, JSONObject> {

    // ATTRIBUTS

    private static final String TAG = "TacheMovies";
    private static String API = BuildConfig.ApiKey;
    private final WeakReference<MainActivity> myActivity;
    private CardStackAdapter adapter;
    private CardStackLayoutManager manager;
    private String apiUrl;
    private boolean gotMovie;

    private AsyncTask task;


    //


    // CONSTRUCTEURS

    public TacheMovies(MainActivity a, CardStackAdapter adapter, CardStackLayoutManager manager)
    {
        super();
        this.myActivity = new WeakReference<>(a);
        this.adapter = adapter;
        this.manager = manager;
        this.gotMovie = false;
        task = null;
    }

    //


    // GETTER / SETTER



    //


    // PROCEDURES

    /**
     *
     */
    @Override
    protected void onPreExecute()
    {

    }

    @Override
    protected JSONObject doInBackground(String... urls) {

        this.apiUrl = urls[0];

        URL url = null;
        HttpURLConnection urlConnection = null;
        JSONObject result = null;

        try
        {
            Log.i("TacheMovies",apiUrl);
            url = new URL(apiUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            result = readSteam(in);

            try {
                // Recupere le nombre de pages
                if(myActivity.get().getTotalPage()==null)
                {
                    myActivity.get().setTotalPage(result.get("total_pages")+"");
                }

                JSONArray desc = result.getJSONArray("results");

                // Initialisation de la liste contenant les noms des films

                for(int i=0;i<desc.length();i++)
                {
                    // Charge le nom du film
                    String name = desc.getJSONObject(i).get("title")+"";
                    //Log.i("TacheNetflix",desc.getJSONObject(i).get("title")+"");

                    // Charge l'image du film
                    String image = desc.getJSONObject(i).get("poster_path")+"";

                    if(!image.equals("null"))
                    {
                        URL urlImage = new URL("https://image.tmdb.org/t/p/w500/"+image);Bitmap bmp = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream());
                        //Log.i("TacheNetflix","https://image.tmdb.org/t/p/w500/"+desc.getJSONObject(i).get("poster_path")+"");

                        String summary = desc.getJSONObject(i).get("overview")+"";
                        String date_release = desc.getJSONObject(i).get("release_date")+"";

                        if(summary!=null && date_release!=null && bmp!=null)
                        {
                            // Creer un film
                            Movie movie = new Movie(bmp,name,summary,date_release);

                            publishProgress(movie);
                        }
                    }
                }

            } catch (JSONException e) {
                //e.printStackTrace();
            }
        }
        catch (IOException | JSONException e)
        {
            //e.printStackTrace();
        }
        finally {
            if(urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }

        return result;
    }

    /**
     *
     */
    @Override
    protected void onProgressUpdate(Movie... movies)
    {
        this.gotMovie=true;

        // Récupère la position de l'item affiché
        int position = manager.getTopPosition();

        // Ajoute le film dans la recyclerView
        adapter.ajouterMovie(movies[0]);

        // Préviens l'adapter que ses données ont été modifiées
        adapter.notifyDataSetChanged();

        // Permet de rester sur l'ancien item affiché
        manager.setTopPosition(position);

        // Incremente le nombre de films affiché
        myActivity.get().incrementItem();
  }

    /**
     *
     */
    @Override
    protected void onPostExecute(JSONObject j)
    {
        if(!this.gotMovie && this.myActivity.get().getTags().getEndDate()>this.myActivity.get().getCurrentDateMovie())
        {
            if(myActivity.get().getCurrentDateMovie()!=0)
            {
                myActivity.get().setCurrentDateMovie(myActivity.get().getCurrentDateMovie()+1);
                TaskManager.getInstace().ajouterTask(task = new TacheMovies(myActivity.get(),adapter,manager).execute("https://api.themoviedb.org/3/discover/movie?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+myActivity.get().getTags().getTags()+"&primary_release_year="+myActivity.get().getCurrentDateMovie()+"&include_video=false&with_original_language=fr&page="+myActivity.get().getCurrentPage()));
            }
            else
            {
                TaskManager.getInstace().ajouterTask(task = new TacheMovies(myActivity.get(),adapter,manager).execute("https://api.themoviedb.org/3/discover/movie?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+myActivity.get().getTags().getTags()+"&include_video=false&with_original_language=fr&page="+myActivity.get().getCurrentPage()));
            }
        }
    }

    /**
     * Construit un JSON a partir d'un stream
     * @param is
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private JSONObject readSteam(InputStream is) throws IOException, JSONException
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for(String line = r.readLine(); line != null; line=r.readLine())
        {
            sb.append(line);
        }
        is.close();

        return new JSONObject(sb.toString());
    }

    //
}
