package com.example.ItsAWatch.modeles;

import java.io.Serializable;

public class Language implements Serializable {

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
    public Language(String id, String name)
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

    @Override
    public String toString()
    {
        return name;
    }

    //
}
