package com.example.laporinc.reportdetail;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.laporinc.R;
import com.example.laporinc.lapor.LaporActivity;
import com.example.laporinc.recent.GlideApp;
import com.example.laporinc.recent.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PagerAdapter extends android.support.v4.view.PagerAdapter {

    private Context context;
    private ArrayList<ImageModel> imageList;

    public PagerAdapter(ArrayList<ImageModel> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View view = LayoutInflater.from( context ).inflate( R.layout.image_collection_item, null );

        final ImageView imageView = view.findViewById( R.id.iv_image_collection_item );
        //imageView.setImageResource( imageList.get( position ).getImage() );
        GlideApp.with( context )
                .load( imageList.get( position ).getImageUri() )
                .centerCrop()
                .into( imageView );

        // on image item click (show full image)
        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                Bitmap bitmap = null;

                ImageDownloader task = new ImageDownloader();
                try {
                    bitmap = task.execute( imageList.get( position ).getImageUri() ).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Dialog builder = new Dialog( context, android.R.style.Theme_Light );
                builder.requestWindowFeature( Window.FEATURE_NO_TITLE );
                builder.getWindow().setBackgroundDrawable( new ColorDrawable( Color.WHITE ) );
                builder.setOnDismissListener( new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //nothing;
                    }
                } );

                // show full image when clicked
                ImageView imageView = new ImageView( context );
                imageView.setImageBitmap( bitmap );
                builder.addContentView( imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT ) );
                builder.show();
            }
        } );


        container.addView( view );

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (View) object );
    }

    @Override
    public int getCount() {
        return (imageList != null) ? imageList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return o == view;
    }


    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL( urls[0] );

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream = connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream( inputStream );

                return myBitmap;


            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return null;

        }


    }



}
