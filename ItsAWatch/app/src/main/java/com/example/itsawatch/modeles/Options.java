package com.example.ItsAWatch.modeles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Options implements Serializable {

    // ATTRIBUTS

    private int startDate;
    private int endDate;
    private List<String> tags;
    private List<Producteur> producteurs;
    private List<Language> languages;
    private String languageSelected;
    private boolean movies;
    private String releaseDateGte;
    private String releaseDateIte;

    //

    // CONSTRUCTEUR

    /**
     *
     */
    public Options()
    {
        tags = new ArrayList<>(); // Taille a changer ?
        producteurs = new ArrayList<>(); // Taille a changer ?
        languages = new ArrayList<>(); // Taille a changer ?
        startDate=0;
        endDate=2020;
        movies=true;

        // Permet de choisir tout les languages
        languages.add(new Language("ALL","All"));
    }

    //

    // GETTER / SETTER

    public List<Language> getLanguagesList() {
        return languages;
    }

    /**
     *
     * @param languageSelected
     */
    public void setLanguageSelected(String languageSelected) {
        this.languageSelected = languageSelected;
    }

    public String getLanguage()
    {
        if(languageSelected!=null)
        {
            if(!languageSelected.equals("ALL"))
            {
                return "&with_original_language="+languageSelected;
            }
        }

        return "";
    }

    public String getProducteurs()
    {
        if(producteurs.size()>0)
        {
            StringBuilder s = new StringBuilder(producteurs.size()*2);

            s.append("&with_networks=");

            for(int i = 0; i< producteurs.size(); i++)
            {
                s.append(producteurs.get(i));
                if(i!= producteurs.size()-1)
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

    public List<Producteur> getProducteursList() {
        return producteurs;
    }

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
        this.releaseDateGte=startDate+"-01-01";
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
        this.releaseDateIte=endDate+"-01-01";
    }

    public String getReleaseDate()
    {
        if(startDate>0)
        {
            return "&primary_release_date.gte="+releaseDateGte+"&primary_release_date.lte="+releaseDateIte;
        }
        else
        {
            return "";
        }
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

            for(int i = 0; i< tags.size(); i++)
            {
                s.append(tags.get(i));
                if(i!= tags.size()-1)
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
     * @param language
     */
    public void ajouterLanguage(Language language)
    {
        this.languages.add(language);
    }

    /**
     *
     * @param producteur
     */
    public void ajouterProducteurs(Producteur producteur) {
        this.producteurs.add(producteur);
    }

    /**
     *
     * @param options
     */
    public void ajouterTags(String options) {
        if(!this.tags.contains(options))
        {
            this.tags.add(options);
        }
    }

    /**
     *
     * @param options
     */
    public void supprimerTags(String options)
    {
        this.tags.remove(options);
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
