package com.example.ItsAWatch.modeles;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * OBSOLETE
 */
public class TaskManager {

    // ATTRIBUTS

    private List<AsyncTask> tasks;
    private static TaskManager INSTANCE = new TaskManager();

    //

    // CONSTRUCTEURS

    /**
     *
     */
    private TaskManager()
    {
        tasks = new ArrayList<>(); // Taille a changer ?
    }

    //

    // GETTER / SETTER

    public static TaskManager getInstace()
    {
        return INSTANCE;
    }

    //

    // PROCEDURES

    /**
     *
     * @param task
     */
    public void ajouterTask(AsyncTask task)
    {
        if(!tasks.contains(task))
        {
            tasks.add(task);
        }
    }

    /**
     *
     * @param task
     */
    public void retirerTask(AsyncTask task)
    {
        if(tasks.contains(task))
        {
            tasks.remove(task);
        }
    }

    /**
     *
     */
    public void cancelAllTask()
    {
        try
        {
            for(AsyncTask async : tasks)
            {
                //Log.i("TaskManager",async.getClass().getName());
                // Empeche l'erreur de Thread
                // Arrete la tache
                async.cancel(true);
            }

            tasks.clear();
        }
        catch (Exception e)
        {

        }

    }

    //
}
