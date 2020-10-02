package com.example.ItsAWatch.taches;

import android.os.AsyncTask;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;

import com.example.ItsAWatch.R;
import com.example.ItsAWatch.modeles.Genre;
import com.example.ItsAWatch.modeles.Options;
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
    private final WeakReference<Fragment> myActivity;
    //private String apiUrl;
    private ChipGroup chipGroup;
    private FlexboxLayout flexboxLayout;

    private Options options;


    //


    // CONSTRUCTEURS

    public TacheGenres(Fragment a, ChipGroup chipGroup, Options options)
    {
        super();
        this.myActivity = new WeakReference<>(a);
        this.chipGroup = chipGroup;
        this.options = options;
    }

    public TacheGenres(Fragment a, FlexboxLayout flexboxLayout, Options options)
    {
        super();
        this.myActivity = new WeakReference<>(a);
        this.flexboxLayout = flexboxLayout;
        this.options = options;
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
        try
        {
            final Chip chip = (Chip)myActivity.get().getLayoutInflater().inflate(R.layout.item_genres,null,false);
            chip.setText(genres[0].getName());

            // Si le options est déjà sélectionné dans le modele, sélectionne l'éléments graphique
            if(options.getListTags().contains(genres[0].getId()))
            {
                chip.setChecked(true);
            }

            flexboxLayout.addView(chip);

            //chipGroup.addView(chip);

            // Evenements quand l'utilisateur clique sur un Chip
            chip.setOnCheckedChangeListener(new Chip.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                    {
                        options.ajouterTags(genres[0].getId());
                        //Log.i("TacheGenres","Je suis selectionne : "+chip.getText());
                    }
                    else
                    {
                        options.supprimerTags(genres[0].getId());
                        //Log.i("TacheGenres","Je ne suis plus selectionne : "+chip.getText());
                    }
                }
            });
        }
        catch (Exception e)
        {
            // Empeche l'application de planter quand l'utilisateur switch très rapidement de fragments
        }
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
