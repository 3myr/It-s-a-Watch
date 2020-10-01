package com.example.ItsAWatch.modeles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tags implements Serializable {

    // ATTRIBUTS

    private int startDate;
    private int endDate;
    private List<String> tags;
    private boolean movies;

    //

    // CONSTRUCTEUR

    /**
     *
     */
    public Tags()
    {
        tags = new ArrayList<>(); // Taille a changer ?
        startDate=0;
        endDate=2020;
        movies=true;
    }

    //

    // GETTER / SETTER


    public boolean isMovies() {
        return movies;
    }

    public void setMovies(boolean movies) {
        this.movies = movies;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    /**
     *
     * @return
     */
    public List<String> getListTags()
    {
        return tags;
    }

    /**
     *
     * @return
     */
    public String getTags() {
        if(tags.size()>0)
        {
            StringBuilder s = new StringBuilder(tags.size()*2);

            s.append("&with_genres=");

            for(int i=0;i<tags.size();i++)
            {
                s.append(tags.get(i));
                if(i!=tags.size()-1)
                {
                    s.append(",");
                }
            }

            return s.toString();
        }
        else
        {
            return "";
        }
    }

    /**
     *
     * @param tags
     */
    public void ajouterTags(String tags) {
        if(!this.tags.contains(tags))
        {
            this.tags.add(tags);
        }
    }

    /**
     *
     * @param tags
     */
    public void supprimerTags(String tags)
    {
        this.tags.remove(tags);
    }

    //

    // PROCEDURES

    /**
     *
     */
    public void clear()
    {
        this.tags.clear();
    }

    //
}
