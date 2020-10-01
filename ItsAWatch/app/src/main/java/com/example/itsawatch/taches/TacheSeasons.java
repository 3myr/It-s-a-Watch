package com.example.ItsAWatch.taches;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ItsAWatch.activities.SwipeActivity;
import com.example.ItsAWatch.adapters.CardStackAdapter;
import com.example.ItsAWatch.modeles.Movie;
import com.example.ItsAWatch.modeles.TaskManager;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class TacheSeasons extends AsyncTask<String, Movie, JSONObject> {

    // ATTRIBUTS

    private static final String TAG = "TacheSeries";
    //private static String API = BuildConfig.ApiKey;
    private final WeakReference<SwipeActivity> myActivity;
    private CardStackAdapter adapter;
    private CardStackLayoutManager manager;
    //private String apiUrl;
    private boolean gotSerie;
    private boolean stop;


    //


    // CONSTRUCTEURS

    public TacheSeasons(SwipeActivity a, CardStackAdapter adapter, CardStackLayoutManager manager)
    {
        super();
        this.myActivity = new WeakReference<>(a);
        this.adapter = adapter;
        this.manager = manager;
        this.gotSerie = false;
        this.stop = false;
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

        String apiUrl = urls[0];

        URL url = null;
        HttpURLConnection urlConnection = null;
        JSONObject result = null;

        try
        {
            Log.i("TacheSeasons",apiUrl);
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

                // Initialisation de la liste contenant les noms des series
                int i=0;
                while(i<desc.length() && !stop)
                {
                    // Charge le nom de la série
                    String name = desc.getJSONObject(i).get("name")+"";
                    //Log.i("TacheNetflix",desc.getJSONObject(i).get("title")+"");

                    // Charge l'image de la série
                    String image = desc.getJSONObject(i).get("poster_path")+"";

                    if(!image.equals("null"))
                    {
                        Bitmap bmp=null;

                        if(isCancelled())
                        {
                            stop = true;
                        }
                        else
                        {
                            URL urlImage=new URL("https://image.tmdb.org/t/p/w500/"+image);
                            if(isCancelled())
                            {
                                stop = true;
                            }
                            else
                            {
                                urlImage.openConnection();
                                if(isCancelled())
                                {
                                    stop = true;
                                }
                                else
                                {
                                    InputStream inputStream = urlImage.openConnection().getInputStream();
                                    if(isCancelled())
                                    {
                                        stop = true;
                                    }
                                    else
                                    {
                                        bmp = BitmapFactory.decodeStream(inputStream);
                                    }
                                }
                            }

                        }

                        String summary = desc.getJSONObject(i).get("overview")+"";
                        String date_release = desc.getJSONObject(i).get("first_air_date")+"";

                        // Creer un film
                        Movie movie = new Movie(bmp,name,summary,date_release);
                        publishProgress(movie);
                    }

                    i++;
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
        this.gotSerie = true;

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
        // Variable Locale
        String API = "4df3f5cbd01cc8041de28fdcb2a06703";

        if(this.myActivity.get().getItemLoad()<20)
        {
            this.gotSerie=false;
        }

        if(!this.gotSerie && this.myActivity.get().getTags().getEndDate()>this.myActivity.get().getCurrentDateMovie()) {
            if(myActivity.get().getCurrentDateMovie()!=0)
            {
                myActivity.get().setCurrentDateMovie(myActivity.get().getCurrentDateMovie() + 1);
                TaskManager.getInstace().ajouterTask(new TacheSeasons(myActivity.get(), adapter, manager).execute("https://api.themoviedb.org/3/discover/tv?api_key=" + API + "&language=fr-FR&sort_by=popularity.desc&include_adult=false" + myActivity.get().getTags().getTags() + "&first_air_date_year=" + myActivity.get().getCurrentDateMovie() + "&include_video=false&page=" + myActivity.get().getCurrentPage()));
            }
            else
            {
                TaskManager.getInstace().ajouterTask(new TacheSeasons(myActivity.get(), adapter, manager).execute("https://api.themoviedb.org/3/discover/tv?api_key=" + API + "&language=fr-FR&sort_by=popularity.desc&include_adult=false" + myActivity.get().getTags().getTags() + "&include_video=false&page=" + myActivity.get().getCurrentPage()));
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
