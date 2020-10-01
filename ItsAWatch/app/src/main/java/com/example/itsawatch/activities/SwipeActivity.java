package com.example.ItsAWatch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.example.ItsAWatch.R;
import com.example.ItsAWatch.adapters.CardStackAdapter;
import com.example.ItsAWatch.modeles.Movie;
import com.example.ItsAWatch.modeles.Preference;
import com.example.ItsAWatch.modeles.Tags;
import com.example.ItsAWatch.modeles.TaskManager;
import com.example.ItsAWatch.taches.TacheMovies;
import com.example.ItsAWatch.taches.TacheSeasons;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class SwipeActivity extends AppCompatActivity {

    // ATTRIBUTS

    private List<Movie> movies;
    private Preference chosenMovies;
    private SwipeActivity mainActivity;

    private CardStackAdapter adapter;
    private CardStackLayoutManager manager;
    private static final String TAG = "MainActivity";
    private static String API = "";

    private int currentPage;
    private String totalPage;
    private int currentDateMovie;
    private Tags tags;

    private int itemLoad;
    private Direction manualDirection;
    private boolean manual;

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
        setContentView(R.layout.activity_swipe);

        // Récupère la classe Tags
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tags = (Tags)extras.get("tags");
        }

        //tags = new Tags();

        // Initialise le nombre de films chargés
        itemLoad = 0;

        //"+tags.getTags()+"&

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
        final CardStackView cardStackView = findViewById(R.id.card_stack_view);

        // Récupération des éléments graphique
        Button like = findViewById(R.id.button_like);
        Button unlike = findViewById(R.id.button_unlike);
        Button superLike = findViewById(R.id.button_superLike);

        // Fixe l'adapter a la RecyclerView
        cardStackView.setAdapter(adapter);

        // Création d'un gestionnaire d'événements

        final CardStackListener cardStackListener = new CardStackListener() {

            @Override
            public void onCardDragging(Direction direction, float ratio) {

            }

            @Override
            public void onCardSwiped(Direction direction)
            {
                //Log.i(TAG,"Je swipe : "+manual+" ( "+manualDirection+" )");
                // Decremente le nombre de films affiché
                itemLoad--;

                //Log.i(TAG,"Position : "+manager.getTopPosition()+"\tItemCount : "+adapter.getItemCount());
                // Verifie si il s'agit d'un swipe manual ou automatique ( par button )
                if(manual)
                {
                    //Log.i(TAG,"Je swipe : "+manual+" ( "+manualDirection+" )");
                    // Si on swipe a droite, ajoute le film swapé dans la liste des films choisit
                    if(manualDirection.equals(Direction.Right))
                    {
                        chosenMovies.ajouterMovie(movies.get(manager.getTopPosition()-1));
                    }
                }
                else
                {
                    //Log.i(TAG,"Je swipe : "+manual+" ( "+direction+" )");
                    // Si on swipe a droite, ajoute le film swapé dans la liste des films choisit
                    if(direction.equals(Direction.Right))
                    {
                        chosenMovies.ajouterMovie(movies.get(manager.getTopPosition()-1));
                    }
                }

                // Réévalue la variable
                manual=false;

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
                                    TaskManager.getInstace().ajouterTask(new TacheMovies(mainActivity,adapter,manager).execute("https://api.themoviedb.org/3/discover/movie?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&primary_release_year="+currentDateMovie+"&include_video=false&page="+currentPage));

                                }
                                else
                                {
                                    TaskManager.getInstace().ajouterTask(new TacheSeasons(mainActivity,adapter,manager).execute("https://api.themoviedb.org/3/discover/tv?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&first_air_date_year="+currentDateMovie+"&include_video=false&page="+currentPage));
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
                                        TaskManager.getInstace().ajouterTask(new TacheMovies(mainActivity,adapter,manager).execute("https://api.themoviedb.org/3/discover/movie?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&primary_release_year="+currentDateMovie+"&include_video=false&page="+currentPage));
                                    }
                                    else
                                    {
                                        TaskManager.getInstace().ajouterTask(new TacheSeasons(mainActivity,adapter,manager).execute("https://api.themoviedb.org/3/discover/tv?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&first_air_date_year="+currentDateMovie+"&include_video=false&page="+currentPage));
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
                                    TaskManager.getInstace().ajouterTask(new TacheMovies(mainActivity,adapter,manager).execute("https://api.themoviedb.org/3/discover/movie?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&primary_release_year="+currentDateMovie+"&include_video=false&page="+currentPage));

                                }
                                else
                                {
                                    TaskManager.getInstace().ajouterTask(new TacheSeasons(mainActivity,adapter,manager).execute("https://api.themoviedb.org/3/discover/tv?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&first_air_date_year="+currentDateMovie+"&include_video=false&page="+currentPage));
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
                                TaskManager.getInstace().ajouterTask(new TacheMovies(mainActivity, adapter, manager).execute("https://api.themoviedb.org/3/discover/movie?api_key=" + API + "&language=fr-FR&sort_by=popularity.desc&include_adult=false" + tags.getTags() + "&include_video=false&page=" + currentPage));
                            }
                            else
                            {
                                TaskManager.getInstace().ajouterTask(new TacheSeasons(mainActivity, adapter, manager).execute("https://api.themoviedb.org/3/discover/tv?api_key=" + API + "&language=fr-FR&sort_by=popularity.desc&include_adult=false" + tags.getTags() + "&include_video=false&page=" + currentPage));
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

        // Evenements du bouton like
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Indique que l'on a swipé manuellement
                manual = true;
                manualDirection = Direction.Right;

                // Swipe
                SwipeAnimationSetting swipeAnimationSetting = new SwipeAnimationSetting.Builder().setDirection(Direction.Right).build();
                manager.setSwipeAnimationSetting(swipeAnimationSetting);
                cardStackView.swipe();
            }
        });

        // Evenements du bouton unlike
        unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Indique que l'on a swipé manuellement
                manual = true;
                manualDirection = Direction.Left;

                // Swipe
                SwipeAnimationSetting swipeAnimationSetting = new SwipeAnimationSetting.Builder().setDirection(Direction.Left).build();
                manager.setSwipeAnimationSetting(swipeAnimationSetting);
                cardStackView.swipe();

            }
        });

        // Evenements du bouton superLike
        superLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Indique que l'on a swipé manuellement
                manual = true;
                manualDirection = Direction.Top;

                // Swipe
                SwipeAnimationSetting swipeAnimationSetting = new SwipeAnimationSetting.Builder().setDirection(Direction.Top).build();
                manager.setSwipeAnimationSetting(swipeAnimationSetting);
                cardStackView.swipe();
            }
        });

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
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
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
                TaskManager.getInstace().ajouterTask(new TacheMovies(this,adapter,manager).execute("https://api.themoviedb.org/3/discover/movie?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&primary_release_year="+currentDateMovie+"&include_video=false&page=1"));
            }
            else
            {
                TaskManager.getInstace().ajouterTask(new TacheSeasons(this,adapter,manager).execute("https://api.themoviedb.org/3/discover/tv?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false"+tags.getTags()+"&first_air_date_year="+currentDateMovie+"&include_video=false&page=1"));
            }
        }
        else
        {
            if(tags.isMovies())
            {
                TaskManager.getInstace().ajouterTask(new TacheMovies(this, adapter, manager).execute("https://api.themoviedb.org/3/discover/movie?api_key=" + API + "&language=fr-FR&sort_by=popularity.desc&include_adult=false" + tags.getTags() + "&include_video=false&page=1"));
            }
            else
            {
                TaskManager.getInstace().ajouterTask(new TacheSeasons(this, adapter, manager).execute("https://api.themoviedb.org/3/discover/tv?api_key=" + API + "&language=fr-FR&sort_by=popularity.desc&include_adult=false" + tags.getTags() + "&include_video=false&page=1"));
            }
        }
    }

    /**
     *
     */
    @Override
    public void onBackPressed()
    {
        TaskManager.getInstace().cancelAllTask();
        movies.clear();
        currentPage=1;
        super.onBackPressed();
    }
    //
}