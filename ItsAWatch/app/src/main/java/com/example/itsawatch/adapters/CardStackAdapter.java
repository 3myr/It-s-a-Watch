package com.example.itsawatch.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.example.itsawatch.R;
import com.example.itsawatch.holders.ViewHolder;
import com.example.itsawatch.modeles.Movie;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<ViewHolder> {

    // ATTRIBUTS

    private CardStackLayoutManager manager;
    private List<Movie> movies;
    private int row_index;

    //

    // CONSTRUCTEUR

    /**
     *
     */
    public CardStackAdapter(List<Movie> movies)
    {
        this.movies = movies;
        row_index=-1;
    }

    //

    // GETTER /SETTER


    public void setManager(CardStackLayoutManager manager) {
        this.manager = manager;
    }

    /**
     *
     * @return
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return movies.size();
    }

    /**
     *
     */
    public void ajouterMovie(Movie movie)
    {
        boolean present=false;
        if(movies.size()>0)
        {
            for(Movie m : movies)
            {
                if(m.getName().equals(movie.getName()))
                {
                    present=true;
                }
            }
        }

        // Si l'ItemModel n'est pas présent, on l'ajoute
        if(!present)
        {
            movies.add(movie);
        }
    }


    //

    // PROCEDURES

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.updateWithModel(this.movies.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Si la vue est déà activé, la déselectionne
                if(view.isActivated())
                {
                    row_index=-1;
                }
                else // sinon sélectionne l'item activé
                {
                    row_index=position;
                }

                // Préviens l'adapter que ses données ont changé
                notifyDataSetChanged();

               // Permet de rester sur l'ancien item affiché
                manager.setTopPosition(position);
            }
        });

        // Récupération des éléments graphique
        ImageView image = (ImageView)holder.itemView.findViewById(R.id.item_image);
        TextView name = (TextView)holder.itemView.findViewById(R.id.item_name);
        RelativeLayout summary = (RelativeLayout)holder.itemView.findViewById(R.id.summary);

        // Cas où l'item est sélectionné
        if(row_index==position)
        {
            //Log.i("ViewHolder", "Je suis Invisible");
            image.setAlpha(100);
            image.setBackgroundColor(Color.BLACK);

            summary.setVisibility(View.VISIBLE);
            name.setVisibility(View.GONE);

            // Indique que l'item est activé
            holder.itemView.setActivated(true);
        }
        else // Cas où l'item n'est plus sélectionné
        {
            //Log.i("ViewHolder", "Je suis Visible");
            image.setAlpha(255);

            summary.setVisibility(View.GONE);
            name.setVisibility(View.VISIBLE);

            // Indique que l'item est désactivé
            holder.itemView.setActivated(false);
        }
    }


    //
}
