package com.example.ItsAWatch.fragments.option;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ItsAWatch.R;
import com.example.ItsAWatch.modeles.Tags;
import com.example.ItsAWatch.taches.TacheGenres;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;

import java.util.Calendar;

public class OptionFragment extends Fragment {

    // ATTRIBUTS

    private ChipGroup chipGroup;
    private FlexboxLayout flexboxLayout;
    private static String API = "";
    private RangeSlider rangeSlider;
    private AppCompatActivity activity;
    private Fragment fragment;


    private Tags tags;

    //

    // CONSTRUCTEUR

    public OptionFragment(Tags tags)
    {
        this.tags = tags;
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

        // Charge les noms des différents producteur


        // Garde en mémoire le fragment
        this.fragment = this;

        // Récupere les elements graphique
        //chipGroup = findViewById(R.id.chip_group);
        flexboxLayout = view.findViewById(R.id.chip_group);
        rangeSlider = view.findViewById(R.id.rangeSlider);
        radioGroupTypes = view.findViewById(R.id.radioGroup_types);
        radioGroupDates = view.findViewById(R.id.radioGroup_dates);

        // Permet d'intialiser la barre permettant de choisir un film parmit deux dates
        if(tags.getStartDate()!=0) {
            rangeSlider.setValueTo(tags.getEndDate());
            rangeSlider.setValueFrom(tags.getStartDate());
            rangeSlider.setValues((float) tags.getStartDate(), (float) tags.getEndDate());
        }
        else
        {
            rangeSlider.setValueTo(Calendar.getInstance().get(Calendar.YEAR));
            rangeSlider.setValueFrom(2000);
            rangeSlider.setValues((float) 2000, (float) Calendar.getInstance().get(Calendar.YEAR));
        }

        // Initialise les dates


        /*
        try {
            FileOutputStream fos = getActivity().openFileOutput("rangeSlider.data", Context.MODE_PRIVATE);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
         */


        // Fixe le type d'éléments a affiché
        radioGroupTypes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton_movies:
                        tags.clear();
                        tags.setMovies(true);
                        flexboxLayout.removeAllViews();
                        new TacheGenres(fragment, flexboxLayout, tags).execute("https://api.themoviedb.org/3/genre/movie/list?api_key=" + API + "&language=fr-FR");
                        break;
                    case R.id.radioButton_series:
                        tags.clear();
                        tags.setMovies(false);
                        flexboxLayout.removeAllViews();
                        new TacheGenres(fragment, flexboxLayout, tags).execute("https://api.themoviedb.org/3/genre/tv/list?api_key=" + API + "&language=fr-FR");
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
                tags.setStartDate((int) Math.round(slider.getValues().get(0)));
                tags.setEndDate((int) Math.round(slider.getValues().get(1)));
                //Log.i("OptionActivity",slider.getValues()+"");
            }
        });

        // Lance Async Task pour récupérer les genres
        if (tags.isMovies()) {
            new TacheGenres(this, flexboxLayout, tags).execute("https://api.themoviedb.org/3/genre/movie/list?api_key=" + API + "&language=fr-FR");
        } else {
            new TacheGenres(this, flexboxLayout, tags).execute("https://api.themoviedb.org/3/genre/tv/list?api_key=" + API + "&language=fr-FR");
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_options, container, false);
    }

    /**
     * Charge le fichier contenant tout les producteurs
     */
    public void readJSON()
    {

    }

    //
}
