package com.example.laporinc.lapor;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class UploadingImage implements Runnable {

    private Thread uploadingThread;
    private ArrayList<Bitmap> bitmaps;
    private String imageKey;
    private String jenisPelanggaran;
    private String waktuKejadian;
    private String lokasiKejadian;

    public UploadingImage(ArrayList<Bitmap> bitmaps, String imageKey, String jenisPelanggaran, String waktuKejadian, String lokasiKejadian) {
        this.bitmaps = bitmaps;
        this.imageKey = imageKey;
        this.jenisPelanggaran = jenisPelanggaran;
        this.waktuKejadian = waktuKejadian;
        this.lokasiKejadian = lokasiKejadian;
    }

    @Override
    public void run() {
        Log.i("UploadingImage", "Begin!");

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final int[] imageCount = {0};

        String folderName = jenisPelanggaran + "/" +  waktuKejadian + "_" + lokasiKejadian;

        //Log.i( "Uploading", "START" );

        for (final Bitmap bitmap : bitmaps) {
            final StorageReference imageRef = storage
                    .getReferenceFromUrl( "gs://laporinc.appspot.com/" )
                    .child( folderName + "/" + UUID.randomUUID() + ".jpeg" );

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress( Bitmap.CompressFormat.JPEG, 20, baos );
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes( data );

            if (imageRef == null){
                Log.i("Imageref", "null");
            }else {
                Log.i("ImageRef", "not null");
            }

            uploadTask.addOnFailureListener( new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception exception) {

                    //Toast.makeText( LaporActivity.this, "Gagal mengunggah foto", Toast.LENGTH_SHORT ).show();

                }

            } ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    imageRef.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //URIs.add( uri.toString() );
                            //Log.i("uri", uri.toString() );

                            if (imageKey == null){
                                Log.i("imageKey", "null");
                            }else {
                                Log.i("imageKey", "not null");
                            }

                            Log.i( "Uploading", Integer.toString( imageCount[0] ) );

                            databaseReference.child( "images" ).child( imageKey ).child( Integer.toString( imageCount[0] )).setValue( uri.toString() );

                            imageCount[0]++;
                        }
                    } );
                }
            } );


        }



        Log.i("UploadingImage", "Finish!");


    }

    public void start(){
        Log.i( "UploadingImageThread", "Starting" );
        if (uploadingThread == null) {
            uploadingThread = new Thread (this, "uploadingImage");
            uploadingThread.start ();
        }
    }
}
