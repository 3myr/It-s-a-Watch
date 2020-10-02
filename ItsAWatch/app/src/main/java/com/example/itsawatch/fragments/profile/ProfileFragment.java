package com.example.ItsAWatch.fragments.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ItsAWatch.R;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    // ATTRIBUTS

    private ImageView imageView;
    private static final int GALLERY=0;
    private static final int PHOTO=1;

    //

    // CONSTRUCTEUR

    /**
     * Constructeur vide empechant une erreur de Fragment
     * Unable to instantiate fragment com.example.ItsAWatch.fragments.xxxx.xxxxx: could not find Fragment constructor
     */
    public ProfileFragment()
    {

    }

    //

    // GETTER / SETTER



    //

    // PROCEDURES

    public void onViewCreated(final View view, Bundle savedInstanceState)
    {
        // Récupère les éléments graphiques
        imageView = view.findViewById(R.id.profile_photo);
        TextView textView = view.findViewById(R.id.bluetooth_name);

        // Récupère le nom bluetooth de l'appareil
        textView.setText(Settings.Secure.getString(getActivity().getContentResolver(), "bluetooth_name"));

        // Récupération des informations de l'utilisateur
        FileInputStream fis=null;
        try
        {
            fis = getActivity().openFileInput("image.data");
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        }

        if(fis!=null)
        {
            Bitmap bm = BitmapFactory.decodeStream(fis);
            imageView.setImageBitmap(bm);
        }

        // Evenements quand l'utilisateur clique sur la photo
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // AlertDialog permettant de choisir entre la Caméra et la Gallery photos
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose a media loader")
                        .setItems(R.array.media_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which){
                            case 0:
                                // Envoye vers une fenetre permettant de prendre une photo
                                Intent ic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if(ic.resolveActivity(getActivity().getPackageManager()) != null)
                                {
                                    startActivityForResult(ic,PHOTO);
                                }
                                break;
                            case 1:
                                // Envoye vers une fenetre permettant de choisir une photo dans la gallery
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, GALLERY);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Suivant le signal reçut, on récupère la photo et la fixe a l'imageView puis la photo est sauvegardé
        switch (requestCode)
        {
            case GALLERY:
                if (resultCode == getActivity().RESULT_OK)
                {
                    Uri targetUri = data.getData();
                    Bitmap bitmap;
                    try {
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));

                        // resize the image
                        int scaleFactor = Math.min(bitmap.getWidth()/300, bitmap.getHeight()/300);

                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/scaleFactor, bitmap.getHeight()/scaleFactor, true);
                        imageView.setImageBitmap(resized);

                        FileOutputStream fos = getActivity().openFileOutput("image.data",getActivity().MODE_PRIVATE);
                        resized.compress(Bitmap.CompressFormat.PNG,100,fos);
                        fos.flush();
                        fos.close();
                    } catch (IOException i) {
                        i.printStackTrace();
                    }
                }
                break;
            case PHOTO:
                if (resultCode == getActivity().RESULT_OK)
                {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    // Enregistre la photo
                    try
                    {
                        FileOutputStream fos = getActivity().openFileOutput("image.data",getActivity().MODE_PRIVATE);
                        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
                        imageView.setImageBitmap(imageBitmap);
                        fos.flush();
                        fos.close();
                        //File f = new File(System.getProperty("user.dir"),"image.data");
                    }
                    catch (IOException i)
                    {
                        i.printStackTrace();
                    }
                }
                break;
        }
    }

    //
}
