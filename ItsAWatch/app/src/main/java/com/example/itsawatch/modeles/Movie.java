package com.example.itsawatch.modeles;


import android.graphics.Bitmap;

public class Movie {

    // ATTRIBUTS

    private Bitmap image;
    private String name;
    private String summary;
    private String dateRelease;

    //

    // CONSTRUCTEUR

    /**
     *
     * @param image
     * @param name
     * @param summary
     * @param dateRelease
     */
    public Movie(Bitmap image, String name, String summary, String dateRelease)
    {
        this.image = image;
        this.name = name;
        this.summary = summary;
        this.dateRelease = dateRelease;
    }

    //

    // GETTER / SETTER

    /**
     *
     * @return
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public String getSummary() {
        return summary;
    }

    /**
     *
     * @return
     */
    public String getDateRelease() {
        return dateRelease;
    }

    //

    // PROCEDURES



    //
}
