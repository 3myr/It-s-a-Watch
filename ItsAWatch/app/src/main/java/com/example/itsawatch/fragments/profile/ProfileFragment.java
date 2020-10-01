package com.example.ItsAWatch.fragments.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.ItsAWatch.R;

import java.io.File;
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

    // GETTER / SETTER



    //

    // PROCEDURES

    public void onViewCreated(final View view, Bundle savedInstanceState)
    {
        // Récupère les éléments graphiques
        imageView = view.findViewById(R.id.profile_photo);

        // Récupération des informations de l'utilisateur
        FileInputStream fis=null;
        try {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                        int scaleFactor = Math.min(bitmap.getWidth()/150, bitmap.getHeight()/150);
                        Log.i("ProfileFragment",scaleFactor+"");

                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/scaleFactor, bitmap.getHeight()/scaleFactor, true);
                        imageView.setImageBitmap(resized);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
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
                        File f = new File(System.getProperty("user.dir"),"image.data");
                        Log.i("Profi",f.toString());
                        fos.close();
                    }
                    catch (IOException i)
                    {

                    }
                }
                break;
        }
    }

    //
}
