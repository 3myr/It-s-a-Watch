package com.example.itsawatch.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.itsawatch.R;
import com.example.itsawatch.adapters.CardStackAdapter;
import com.example.itsawatch.modeles.Movie;
import com.example.itsawatch.modeles.Preference;
import com.example.itsawatch.taches.TacheMovies;
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
    private static String API = "4df3f5cbd01cc8041de28fdcb2a06703";

    private int currentPage;
    private String totalPage;

    //

    // GETTER / SETTER


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

        // TEST
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
                Log.i(TAG,"Position : "+manager.getTopPosition()+"\tItemCount : "+adapter.getItemCount());
                // Si on swipe a droite, ajoute le film swapé dans la liste des films choisit
                if(direction.equals(Direction.Right))
                {
                    chosenMovies.ajouterMovie(movies.get(manager.getTopPosition()-1));
                }

                // Quand on arrive vers la fin de la liste, charge une autre
                if( (manager.getTopPosition() == adapter.getItemCount()-5) && totalPage!=null)
                {
                    if(Integer.parseInt(totalPage)!=currentPage)
                    {
                        currentPage++;
                        new TacheMovies(mainActivity,adapter,manager).execute("https://api.themoviedb.org/3/discover/movie?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false&include_video=false&with_original_language=fr&page="+currentPage);
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
        new TacheMovies(this,adapter,manager).execute("https://api.themoviedb.org/3/discover/movie?api_key="+API+"&language=fr-FR&sort_by=popularity.desc&include_adult=false&include_video=false&with_original_language=fr&page=1");
    }

    //
}