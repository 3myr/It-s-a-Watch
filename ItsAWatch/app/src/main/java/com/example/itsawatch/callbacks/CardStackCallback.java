package com.example.itsawatch.callbacks;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

import com.example.itsawatch.modeles.Movie;

public class CardStackCallback extends DiffUtil.Callback {

    // ATTRIBUTS

    private List<Movie> old, baru;

    //

    // CONSTRUCTEUR

    public CardStackCallback(List<Movie> old, List<Movie> baru) {
        this.old = old;
        this.baru = baru;
    }

    //


    // GETTER / SETTER

    @Override
    public int getOldListSize()
    {
        return old.size();
    }

    @Override
    public int getNewListSize()
    {
        return baru.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition)
    {
        return old.get(oldItemPosition).getImage() == baru.get(newItemPosition).getImage();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition)
    {
        return old.get(oldItemPosition) == baru.get(newItemPosition);
    }


    //

    // PROCEDURES



    //
}
