package com.gfarcasiu.virtualgrafitti;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
//import android.widget.LinearLayout;


public class DrawActivity extends Activity {
    //private ImageButton currPaint = null;
    private DrawingView drawView;
    private Location lastLocation;

    public static HashSet<File> files = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Establish connection with Firebase

        //Add Firebase listener
        /*firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });*/

        //Writing to Firebase example
        //https://www.firebase.com/docs/android/guide/saving-data.html
        //myFirebaseRef.child("message").setValue("Do you have data? You'll love Firebase.");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        setContentView(R.layout.activity_draw);

        // Set the image
        Bundle extras = getIntent().getExtras();
        String imageLoc = (String)extras.get("imageLocation");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageLoc, options);

        ((ImageView)(findViewById(R.id.image_view))).setImageBitmap(
                decodeSampledBitmapFromResource(options, imageLoc, 960, 540)
        );

        drawView = (DrawingView)findViewById(R.id.image_view);

        // start getting location data
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                lastLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_draw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // HELPER METHODS
    public static Bitmap decodeSampledBitmapFromResource(
           BitmapFactory.Options options, String fileName, int reqWidth, int reqHeight) {

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(fileName, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    // Set paint colors
    /*public void paintClicked(View view){
        if(view!=currPaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            currPaint=(ImageButton)view;
        }//update color
    }//paint clicked*/
    public void changeRed(View view) {
        drawView.setColor(0xFFFF0000);
    }//changeRed
    public void changeOrange(View view) {
        drawView.setColor(0xFFFF6600);
    }//change orange
    public void changeYellow(View view) {
        drawView.setColor(0xFFFFFF00);
    }//change yellow
    public void changeGreen(View view) {
        drawView.setColor(0xFF00FF00);
    }//change green
    public void changeBlue(View view) {
        drawView.setColor(0xFF0000FF);
    }//change blue
    public void changeViolet(View view) {
        drawView.setColor(0xFFFF00FF);
    }//change violet

    public void submit(View view){
        writeToDisk(drawView.getBitmap());

        finish();
    }

    public void writeToDisk(Bitmap b) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("Error", "Error creating media file, check storage permissions");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            b.compress(Bitmap.CompressFormat.JPEG, 0, fos);
            files.add(pictureFile);

            Log.i("Info", "Abs Pat: " + pictureFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            Log.d("Error", "File not found: " + e);
        } catch (IOException e) {
            Log.d("Error", "Error accessing file: " + e);
        } catch (NullPointerException e) {
            Log.d("Error", "Null pointer exceptions and stuffs");
        }
    }

    // HELPER METHOD
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Error", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

}
