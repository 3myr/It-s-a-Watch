package com.example.ItsAWatch.fragments.option;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ItsAWatch.R;
import com.example.ItsAWatch.modeles.Language;
import com.example.ItsAWatch.modeles.Options;
import com.example.ItsAWatch.modeles.Producteur;
import com.example.ItsAWatch.taches.TacheGenres;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class OptionFragment extends Fragment {

    // ATTRIBUTS

    private ChipGroup chipGroup;
    private FlexboxLayout flexboxLayout;
    private static String API = "";
    private RangeSlider rangeSlider;
    private AppCompatActivity activity;
    private Fragment fragment;

    private Options options;

    //

    // CONSTRUCTEUR

    /**
     * Constructeur empechant de générer une erreur d'instanciation
     * Unable to instantiate fragment com.example.ItsAWatch.fragments.xxxx.xxxxx: could not find Fragment constructor
     * @param options
     * @return
     */
    public static OptionFragment newInstance(Options options)
    {
        Bundle args = new Bundle();
        args.putSerializable("options", options);
        OptionFragment o = new OptionFragment();
        o.setArguments(args);
        return o;
    }

    public OptionFragment()
    {

    }

    //

    // GETTER / SETTER



    //

    // PROCEDURES
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
    {
        // Variable locale
        RadioGroup radioGroupDates;
        RadioGroup radioGroupTypes;

        // Récupère la classe options
        this.options = (Options)this.getArguments().getSerializable("options");

        // Charge les noms des différents producteur
        getProducteurs("jsons/tv_network_ids.json");

        // Charge les differentes langue au format ISO 639-1
        getLanguage("jsons/language.json");

        // Garde en mémoire le fragment
        this.fragment = this;

        // Récupere les elements graphique
        //chipGroup = findViewById(R.id.chip_group);
        flexboxLayout = view.findViewById(R.id.chip_group);
        rangeSlider = view.findViewById(R.id.rangeSlider);
        radioGroupTypes = view.findViewById(R.id.radioGroup_types);
        radioGroupDates = view.findViewById(R.id.radioGroup_dates);

        // TEST
        SearchableSpinner searchableSpinner = view.findViewById(R.id.search_spinner);

        ArrayAdapter<Language> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,options.getLanguagesList());

        searchableSpinner.setAdapter(adapter);
        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("OptionFragment",options.getLanguagesList().get(i).getId()+"");
                options.setLanguageSelected(options.getLanguagesList().get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //

        // Permet d'intialiser la barre permettant de choisir un film parmit deux dates
        if(options.getStartDate()!=0) {
            rangeSlider.setValueTo(options.getEndDate());
            rangeSlider.setValueFrom(options.getStartDate());
            rangeSlider.setValues((float) options.getStartDate(), (float) options.getEndDate());
        }
        else
        {
            rangeSlider.setValueTo(Calendar.getInstance().get(Calendar.YEAR));
            rangeSlider.setValueFrom(2000);
            rangeSlider.setValues((float) 2000, (float) Calendar.getInstance().get(Calendar.YEAR));
        }

        // Fixe le type d'éléments a affiché
        radioGroupTypes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton_movies:
                        options.clear();
                        options.setMovies(true);
                        flexboxLayout.removeAllViews();
                        new TacheGenres(fragment, flexboxLayout, options).execute("https://api.themoviedb.org/3/genre/movie/list?api_key=" + API + "&language=fr-FR");
                        break;
                    case R.id.radioButton_series:
                        options.clear();
                        options.setMovies(false);
                        flexboxLayout.removeAllViews();
                        new TacheGenres(fragment, flexboxLayout, options).execute("https://api.themoviedb.org/3/genre/tv/list?api_key=" + API + "&language=fr-FR");
                        break;
                }
            }
        });

        // Fixe la date minimum d'un film
        radioGroupDates.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton_1900:
                        // Permet d'intialiser la barre permettant de choisir un film parmit deux dates
                        rangeSlider.setEnabled(true);
                        rangeSlider.setValueTo(Calendar.getInstance().get(Calendar.YEAR));
                        rangeSlider.setValueFrom(1900);
                        rangeSlider.setValues((float) 1900, (float) Calendar.getInstance().get(Calendar.YEAR));
                        break;
                    case R.id.radioButton_1950:
                        // Permet d'intialiser la barre permettant de choisir un film parmit deux dates
                        rangeSlider.setEnabled(true);
                        rangeSlider.setValueTo(Calendar.getInstance().get(Calendar.YEAR));
                        rangeSlider.setValueFrom(1950);
                        rangeSlider.setValues((float) 1950, (float) Calendar.getInstance().get(Calendar.YEAR));
                        break;
                    case R.id.radioButton_2000:
                        // Permet d'intialiser la barre permettant de choisir un film parmit deux dates
                        rangeSlider.setEnabled(true);
                        rangeSlider.setValueTo(Calendar.getInstance().get(Calendar.YEAR));
                        rangeSlider.setValueFrom(2000);
                        rangeSlider.setValues((float) 2000, (float) Calendar.getInstance().get(Calendar.YEAR));
                        break;
                    case R.id.radioButton_all:
                        rangeSlider.setEnabled(false);
                        rangeSlider.setValueTo(Calendar.getInstance().get(Calendar.YEAR));
                        rangeSlider.setValueFrom(0);
                        rangeSlider.setValues((float) 0, (float) Calendar.getInstance().get(Calendar.YEAR));
                }
            }
        });


        // Evenements quand les valeurs change
        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {

                // Met a jour les valeurs des dates de films a chercher
                options.setStartDate((int) Math.round(slider.getValues().get(0)));
                options.setEndDate((int) Math.round(slider.getValues().get(1)));
                //Log.i("OptionActivity",slider.getValues()+"");
            }
        });

        // Lance Async Task pour récupérer les genres
        if (options.isMovies()) {
            new TacheGenres(this, flexboxLayout, options).execute("https://api.themoviedb.org/3/genre/movie/list?api_key=" + API + "&language=fr-FR");
        } else {
            new TacheGenres(this, flexboxLayout, options).execute("https://api.themoviedb.org/3/genre/tv/list?api_key=" + API + "&language=fr-FR");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_options, container, false);
    }

    /**
     * Charge le fichier contenant tout les producteurs et les charge dans la classe option
     */
    public void getProducteurs(String file)
    {
        try
        {
            // Converti le string en JSON puis en extrait les données
            JSONObject jsonObject = readJSON(file);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);
                options.ajouterProducteurs(new Producteur(object.get("id")+"",object.get("name")+""));
                //Log.i("OptionFragment",object.get("id")+"\t"+object.get("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge le fichier contenant tout les languages au format ISO 639-1 et les charge dans la classe option
     */
    public void getLanguage(String file)
    {
        try
        {
            // Converti le string en JSON puis en extrait les données
            JSONObject jsonObject = readJSON(file);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);
                options.ajouterLanguage(new Language(object.get("code")+"",object.get("name")+""));
                //Log.i("OptionFragment",object.get("code")+"\t"+object.get("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de lire un fichier JSON
     * @return
     */
    public JSONObject readJSON(String file)
    {
        JSONObject jsonObject=null;
        try
        {
            // Lecture du json
            InputStream is = getContext().getAssets().open(file);

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");

            // Converti le string en JSON puis en extrait les données
            jsonObject = new JSONObject(json);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return jsonObject;
    }

    //
}
