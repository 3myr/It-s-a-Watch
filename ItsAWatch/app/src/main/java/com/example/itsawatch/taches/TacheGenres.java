package com.example.itsawatch.taches;

import android.os.AsyncTask;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itsawatch.R;
import com.example.itsawatch.modeles.Genre;

import com.example.itsawatch.modeles.Tags;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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

public class TacheGenres extends AsyncTask<String, Genre, JSONObject> {

    // ATTRIBUTS

    private static final String TAG = "TacheGenres";
    private final WeakReference<AppCompatActivity> myActivity;
    private String apiUrl;
    private ChipGroup chipGroup;
    private FlexboxLayout flexboxLayout;

    private Tags tags;


    //


    // CONSTRUCTEURS

    public TacheGenres(AppCompatActivity a, ChipGroup chipGroup, Tags tags)
    {
        super();
        this.myActivity = new WeakReference<>(a);
        this.chipGroup = chipGroup;
        this.tags = tags;
    }

    public TacheGenres(AppCompatActivity a, FlexboxLayout flexboxLayout, Tags tags)
    {
        super();
        this.myActivity = new WeakReference<>(a);
        this.flexboxLayout = flexboxLayout;
        this.tags = tags;
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
            url = new URL(apiUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            result = readSteam(in);

            try {

                JSONArray desc = result.getJSONArray("genres");

                // Initialisation de la liste contenant les noms des genres
                for(int i=0;i<desc.length();i++)
                {
                    // Charge le nom du film
                    String name = desc.getJSONObject(i).get("name")+"";
                    String id = desc.getJSONObject(i).get("id")+"";
                    //Log.i(TAG,name+"\t"+id);

                    Genre genre = new Genre(name,id);

                    publishProgress(genre);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
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
    protected void onProgressUpdate(final Genre... genres)
    {
        final Chip chip = (Chip)myActivity.get().getLayoutInflater().inflate(R.layout.item_genres,null,false);
        chip.setText(genres[0].getName());
        flexboxLayout.addView(chip);
        //chipGroup.addView(chip);

        // Evenements quand l'utilisateur clique sur un Chip
        chip.setOnCheckedChangeListener(new Chip.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    tags.ajouterTags(genres[0].getId());
                    //Log.i("TacheGenres","Je suis selectionne : "+chip.getText());
                }
                else
                {
                    tags.supprimerTags(genres[0].getId());
                    //Log.i("TacheGenres","Je ne suis plus selectionne : "+chip.getText());
                }
            }
        });
    }

    /**
     *
     */
    @Override
    protected void onPostExecute(JSONObject j)
    {

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
