package com.example.itsawatch.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.itsawatch.BuildConfig;
import com.example.itsawatch.R;
import com.example.itsawatch.adapters.CardStackAdapter;
import com.example.itsawatch.modeles.Movie;
import com.example.itsawatch.modeles.Preference;
import com.example.itsawatch.modeles.Tags;
import com.example.itsawatch.modeles.TaskManager;
import com.example.itsawatch.taches.TacheMovies;
import com.example.itsawatch.taches.TacheSeasons;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // ATTRIBUTS

    private List<Movie> movies;
    private Preference chosenMovies;
    private MainActivity mainActivity;

    private CardStackAdapter adapter;
    private CardStackLayoutManager manager;
    private static final String TAG = "MainActivity";
    private static String API = BuildConfig.ApiKey;

    private int currentPage;
    private String totalPage;
    private int currentDateMovie;
    private Tags tags;

    private int itemLoad;
    private AsyncTask task;

    //

    // GETTER / SETTER

    /**
     *
     * @return
     */
    public int getItemLoad() {
        return itemLoad;
    }

    /**
     *
     */
    public void incrementItem() {
        this.itemLoad++;
    }

    /**
     *
     * @param currentDateMovie
     */
    public void setCurrentDateMovie(int currentDateMovie) {
        if(currentDateMovie<=tags.getEndDate())
        {
            this.currentPage=1;
            this.currentDateMovie = currentDateMovie;
        }
    }

    /**
     *
     * @return
     */
    public Tags getTags() {
        return tags;
    }

    /**
     *
     * @return
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     *
     * @return
     */
    public int getCurrentDateMovie() {
        return currentDateMovie;
    }

    /**
     *
     * @return
     */
    public String getTotalPage()
    {
        return totalPage;
    }

    /**
     *
     * @param totalPage
     */
    public void setTotalPage(String totalPage)
    {
        this.totalPage = totalPage;
    }


    //

    // PROCEDURES

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fixe la vue
        setContentView(R.layout.activity_main);

        // Récupère la classe Tags
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tags = (Tags)extras.get("tags");
        }

        // Initialise le nombre de films chargés
        itemLoad = 0;

        task = null;
        //"+tags.getTags()+"&
        //Log.i("MainActivity",tags.getTags());

        // Affecte la date de départ des films à rechercher
        currentDateMovie = tags.getStartDate();

        // Initialisation
        mainActivity = this;
        currentPage=1;

        // Initialise la liste qui contiendra les films récupérer par l'API
        movies = new ArrayList<Movie>();

        // Initialise la liste de films choisit
        chosenMovies = new Preference();

        // Initialisation de l'adapter
        adapter = new CardStackAdapter(movies);

        // Récupération de la RecyclerView
        CardStackView cardStackView = findViewById(R.id.card_stack_view);

        // Fixe l'adapter a la RecyclerView
        cardStackView.setAdapter(adapter);

        // Création d'un gestionnaire d'événements

        CardStackListener cardStackListener = new CardStackListener() {

            @Override
            public void onCardDragging(Direction direction, float ratio) {

            }

            @Override
            public void onCardSwiped(Direction direction)
            {
                // Decremente le nombre de films affiché
                itemLoad--;

                //Log.i(TAG,"Position : "+manager.getTopPosition()+"\tItemCount : "+adapter.getItemCount());
                // Si on swipe a droite, ajoute le film swapé dans la liste des films choisit
                if(direction.equals(Direction.Right))
                {
                    chosenMovies.ajouterMovie(movies.get(manager.getTopPosition()-1));
                }

                // Quand on arrive vers la fin de la liste, charge une autre
                // Cas ou l'utilisateur selectionne une partie de films dans la base de donnée
                if(currentDateMovie!=0)
                {
                    if( (manager.getTopPosition() == adapter.getItemCount()) && totalPage!=null || itemLoad<30)
                    {
                        // Si on se situe dans la plage de date, on continue
                        if(currentDateMovie<tags.getEndDate())
                        {
                            // Si le nombre de page totale est supérieur a la page visité, continue
                            if(Integer.parseInt(totalPage)>currentPage )
                            {
                                currentPage++;
                                if(tags.isMovies())
                                {
                                    TaskManager.getInstace().ajouterTask(new TacheMovies(mainActivity,adapter,manager).execute("https://api.themoviedb.org/3/discover/movie?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&primary_release_year="+currentDateMovie+"&include_video=false&with_original_language=fr&page="+currentPage));

                                }
                                else
                                {
                                    TaskManager.getInstace().ajouterTask(new TacheSeasons(mainActivity,adapter,manager).execute("https://api.themoviedb.org/3/discover/tv?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&first_air_date_year="+currentDateMovie+"&include_video=false&with_original_language=fr&page="+currentPage));
                                }
                            }
                            else
                            {
                                currentPage=1;
                                if(currentDateMovie!=0)
                                {
                                    currentDateMovie++;
                                    if(tags.isMovies())
                                    {
                                        TaskManager.getInstace().ajouterTask(new TacheMovies(mainActivity,adapter,manager).execute("https://api.themoviedb.org/3/discover/movie?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&primary_release_year="+currentDateMovie+"&include_video=false&with_original_language=fr&page="+currentPage));
                                    }
                                    else
                                    {
                                        TaskManager.getInstace().ajouterTask(new TacheSeasons(mainActivity,adapter,manager).execute("https://api.themoviedb.org/3/discover/tv?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&first_air_date_year="+currentDateMovie+"&include_video=false&with_original_language=fr&page="+currentPage));
                                    }
                                }
                            }
                        }
                        else
                        {
                            // Si le nombre de page totale est supérieur a la page visité, continue
                            if(Integer.parseInt(totalPage)>currentPage )
                            {
                                currentPage++;
                                if(tags.isMovies())
                                {
                                    TaskManager.getInstace().ajouterTask(new TacheMovies(mainActivity,adapter,manager).execute("https://api.themoviedb.org/3/discover/movie?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&primary_release_year="+currentDateMovie+"&include_video=false&with_original_language=fr&page="+currentPage));

                                }
                                else
                                {
                                    TaskManager.getInstace().ajouterTask(new TacheSeasons(mainActivity,adapter,manager).execute("https://api.themoviedb.org/3/discover/tv?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&first_air_date_year="+currentDateMovie+"&include_video=false&with_original_language=fr&page="+currentPage));
                                }
                            }
                        }
                    }
                }
                else // Cas ou l'utilisateur selectionne tout les films dans la base de données
                {
                    if( (manager.getTopPosition() == adapter.getItemCount()) && totalPage!=null || itemLoad<30) {
                        // Si le nombre de page totale est supérieur a la page visité, continue
                        if (Integer.parseInt(totalPage) > currentPage) {
                            currentPage++;
                            if(tags.isMovies())
                            {
                                TaskManager.getInstace().ajouterTask(new TacheMovies(mainActivity, adapter, manager).execute("https://api.themoviedb.org/3/discover/movie?api_key=" + API + "&language=fr-FR&sort_by=popularity.desc&include_adult=false" + tags.getTags() + "&include_video=false&with_original_language=fr&page=" + currentPage));
                            }
                            else
                            {
                                TaskManager.getInstace().ajouterTask(new TacheSeasons(mainActivity, adapter, manager).execute("https://api.themoviedb.org/3/discover/tv?api_key=" + API + "&language=fr-FR&sort_by=popularity.desc&include_adult=false" + tags.getTags() + "&include_video=false&with_original_language=fr&page=" + currentPage));
                            }
                        }

                    }
                }

            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {

            }

            @Override
            public void onCardAppeared(View view, int position)
            {

            }

            @Override
            public void onCardDisappeared(View view, int position) {

            }
        };

        // Création du Layout
        manager = new CardStackLayoutManager(this, cardStackListener);

        // Fixe les propriétés du LayoutManager
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());

        // Fixe le LayoutManager a la RecyclerView
        cardStackView.setLayoutManager(manager);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

        // Fixe le manager dans l'adapter
        adapter.setManager(manager);

        // Lance la tache
        //new TacheSeasons(this,adapter,manager).execute("https://api.themoviedb.org/3/discover/tv?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false&include_video=false&with_original_language=de&page=1");
        if(currentDateMovie!=0)
        {
            if(tags.isMovies())
            {
                TaskManager.getInstace().ajouterTask(new TacheMovies(this,adapter,manager).execute("https://api.themoviedb.org/3/discover/movie?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&primary_release_year="+currentDateMovie+"&include_video=false&with_original_language=fr&page=1"));
            }
            else
            {
                TaskManager.getInstace().ajouterTask(new TacheSeasons(this,adapter,manager).execute("https://api.themoviedb.org/3/discover/tv?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&first_air_date_year="+currentDateMovie+"&include_video=false&with_original_language=fr&page=1"));
            }
        }
        else
        {
            if(tags.isMovies())
            {
                TaskManager.getInstace().ajouterTask(new TacheMovies(this, adapter, manager).execute("https://api.themoviedb.org/3/discover/movie?api_key=" + API + "&language=fr-FR&sort_by=popularity.desc&include_adult=false" + tags.getTags() + "&include_video=false&with_original_language=fr&page=1"));
            }
            else
            {
                TaskManager.getInstace().ajouterTask(new TacheSeasons(this, adapter, manager).execute("https://api.themoviedb.org/3/discover/tv?api_key=" + API + "&language=fr-FR&sort_by=popularity.desc&include_adult=false" + tags.getTags() + "&include_video=false&with_original_language=fr&page=1"));
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        TaskManager.getInstace().cancelAllTask();
        currentPage=1;
    }
    //
}