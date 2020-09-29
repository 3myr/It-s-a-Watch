package com.example.itsawatch.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.itsawatch.BuildConfig;
import com.example.itsawatch.R;
import com.example.itsawatch.modeles.Tags;
import com.example.itsawatch.taches.TacheGenres;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;

import java.util.Calendar;

public class OptionActivity extends AppCompatActivity {

    // ATTRIBUTS

    private ChipGroup chipGroup;
    private FlexboxLayout flexboxLayout;
    private static String API = BuildConfig.ApiKey;
    private RangeSlider rangeSlider;
    private RadioGroup radioGroupDates;
    private RadioGroup radioGroupTypes;
    private AppCompatActivity activity;

    private Tags tags;

    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fixe la vue
        setContentView(R.layout.activity_option);

        // Initialise une classe contenant les options choisit par l'utilisateur
        tags = new Tags();

        // Garde en mémoire l'activity
        this.activity = this;

        // Récupere les elements graphique
        //chipGroup = findViewById(R.id.chip_group);
        flexboxLayout = findViewById(R.id.chip_group);
        rangeSlider = findViewById(R.id.rangeSlider);
        radioGroupTypes = findViewById(R.id.radioGroup_types);
        radioGroupDates = findViewById(R.id.radioGroup_dates);

        // Permet d'intialiser la barre permettant de choisir un film parmit deux dates
        rangeSlider.setValueTo(Calendar.getInstance().get(Calendar.YEAR));
        rangeSlider.setValueFrom(2000);
        rangeSlider.setValues((float) 2000,(float) Calendar.getInstance().get(Calendar.YEAR));

        // Initialise les dates
        tags.setStartDate(2000);
        tags.setEndDate(Calendar.getInstance().get(Calendar.YEAR));

        // Fixe le type d'éléments a affiché
        radioGroupTypes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_movies:
                        tags.clear();
                        tags.setMovies(true);
                        flexboxLayout.removeAllViews();
                        new TacheGenres(activity,flexboxLayout,tags).execute("https://api.themoviedb.org/3/genre/movie/list?api_key="+API+"&language=fr-FR");
                        break;
                    case R.id.radioButton_series:
                        tags.clear();
                        tags.setMovies(false);
                        flexboxLayout.removeAllViews();
                        new TacheGenres(activity,flexboxLayout,tags).execute("https://api.themoviedb.org/3/genre/tv/list?api_key="+API+"&language=fr-FR");
                        break;
                }
            }
        });

        // Fixe la date minimum d'un film
        radioGroupDates.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_1900:
                        // Permet d'intialiser la barre permettant de choisir un film parmit deux dates
                        rangeSlider.setEnabled(true);
                        rangeSlider.setValueTo(Calendar.getInstance().get(Calendar.YEAR));
                        rangeSlider.setValueFrom(1900);
                        rangeSlider.setValues((float) 1900,(float) Calendar.getInstance().get(Calendar.YEAR));
                        break;
                    case R.id.radioButton_1950:
                        // Permet d'intialiser la barre permettant de choisir un film parmit deux dates
                        rangeSlider.setEnabled(true);
                        rangeSlider.setValueTo(Calendar.getInstance().get(Calendar.YEAR));
                        rangeSlider.setValueFrom(1950);
                        rangeSlider.setValues((float) 1950,(float) Calendar.getInstance().get(Calendar.YEAR));
                        break;
                    case R.id.radioButton_2000:
                        // Permet d'intialiser la barre permettant de choisir un film parmit deux dates
                        rangeSlider.setEnabled(true);
                        rangeSlider.setValueTo(Calendar.getInstance().get(Calendar.YEAR));
                        rangeSlider.setValueFrom(2000);
                        rangeSlider.setValues((float) 2000,(float) Calendar.getInstance().get(Calendar.YEAR));
                        break;
                    case R.id.radioButton_all:
                        rangeSlider.setEnabled(false);
                        rangeSlider.setValueTo(Calendar.getInstance().get(Calendar.YEAR));
                        rangeSlider.setValueFrom(0);
                        rangeSlider.setValues((float) 0,(float) Calendar.getInstance().get(Calendar.YEAR));
                }
            }
        });



        // Evenements quand les valeurs change
        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser)
            {
                // Met a jour les valeurs des dates de films a chercher
                tags.setStartDate((int)Math.round(slider.getValues().get(0)));
                tags.setEndDate((int)Math.round(slider.getValues().get(1)));
                //Log.i("OptionActivity",slider.getValues()+"");
            }
        });

        // Evenements quand l'utilisateur
        Button goToSwap = findViewById(R.id.got_to_swap);
        goToSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OptionActivity.this,MainActivity.class);
                i.putExtra("tags",tags);
                startActivity(i);
            }
        });

        // Lance Async Task pour récupérer les genres
        //new TacheGenres(this,chipGroup).execute("https://api.themoviedb.org/3/genre/movie/list?api_key="+API+"&language=fr-FR");
        if(tags.isMovies())
        {
            new TacheGenres(this,flexboxLayout,tags).execute("https://api.themoviedb.org/3/genre/movie/list?api_key="+API+"&language=fr-FR");
        }
        else
        {
            new TacheGenres(this,flexboxLayout,tags).execute("https://api.themoviedb.org/3/genre/tv/list?api_key="+API+"&language=fr-FR");
        }
    }
}