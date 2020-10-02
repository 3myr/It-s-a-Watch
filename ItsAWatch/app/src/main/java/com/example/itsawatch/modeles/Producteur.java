package com.example.ItsAWatch.modeles;

import java.io.Serializable;

public class Producteur implements Serializable {

    // ATTRIBUTS

    private String name;
    private String id;

    //

    // CONSTRUCTEURS

    /**
     *
     * @param name
     * @param id
     */
    public Producteur(String name, String id)
    {
        this.name = name;
        this.id = id;
    }


    //

    // GETTER / SETTER

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
    public String getId() {
        return id;
    }


    //

    // PROCEDURES



    //
}
