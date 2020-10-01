package com.example.ItsAWatch.modeles;

import java.util.ArrayList;
import java.util.List;

public class Preference {

    // ATTRIBUTS

    private List<Movie> movies;

    //

    // CONSTRUCTEURS

    public Preference()
    {
        this.movies = new ArrayList<Movie>();
    }

    //

    // GETTER / SETTER



    //

    // PROCEDURES

    /**
     * Ajoute un film dans la liste des films choisit
     * @param movie
     */
    public void ajouterMovie(Movie movie)
    {
        movies.add(movie);
    }

    //
}
