package com.example.itsawatch.holders;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.itsawatch.R;
import com.example.itsawatch.modeles.Movie;

import java.text.DateFormatSymbols;

public class ViewHolder extends RecyclerView.ViewHolder{

    // ATTRIBUTS

    private ImageView image;
    private TextView name;
    private TextView nameDetails;
    private TextView summaryText;
    private TextView dateReleaseText;
    private RelativeLayout summary;

    //

    // CONSTRUCTEUR

    public ViewHolder(View itemView)
    {
        super(itemView);

        image = (ImageView)itemView.findViewById(R.id.item_image);
        name = (TextView)itemView.findViewById(R.id.item_name);
        summary = (RelativeLayout)itemView.findViewById(R.id.summary);
        nameDetails = (TextView)itemView.findViewById(R.id.name_details);
        summaryText = (TextView)itemView.findViewById(R.id.summary_text);
        dateReleaseText = (TextView)itemView.findViewById(R.id.date_release);
    }

    //

    // GETTER / SETTER

    /**
     *
     * @param movie
     */
    public void updateWithModel(Movie movie)
    {
        this.image.setImageBitmap(movie.getImage());
        this.name.setText(movie.getName());
        this.nameDetails.setText(movie.getName());
        this.summaryText.setText(movie.getSummary());
        this.dateReleaseText.setText(toEuDate(movie.getDateRelease()));
    }

    //

    // PROCEDURES

    /**
     * Converti une date de format US au format EU
     * @param date Format "DAY MONTH YEARS"
     * @return
     */
    public String toEuDate(String date)
    {
        try
        {
            String dateTmp = date.substring(0,date.indexOf("-"));

            String years = dateTmp;
            //Log.i("ItemModel",years);
            dateTmp = date.substring(date.indexOf("-")+1,date.lastIndexOf("-"));
            String month = new DateFormatSymbols().getMonths()[Integer.parseInt(dateTmp)-1];
            //Log.i("ItemModel",month);
            dateTmp = date.substring(date.lastIndexOf("-")+1);
            String day = dateTmp;
            //Log.i("ItemModel",day);

            return day+" "+month+" "+years;
        }
        catch (Exception e)
        {
            return null;
        }

    }

    //
}