package com.gfarcasiu.virtualgrafitti;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;


public class GalleryActivity extends Activity {
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_gallery);

        // Init firebase stuff
        GridView gridView = (GridView)findViewById(R.id.grid_view);
        ImageAdapter ia = new ImageAdapter(this);

        /*Bitmap[] bitMaps = new Bitmap[DrawActivity.files.size()];
        int index = 0;
        for (File f : DrawActivity.files) {
                bitMaps[index++] = BitmapFactory.decodeFile(f.getAbsolutePath());
        }

        Log.i("info stuffs #yolololo", bitMaps.length+"");


        ia.addImages(bitMaps);*/

        int extra = 6;
        File[] file = new File[DrawActivity.files.size() + extra];
        int index = extra;
        for (File f : DrawActivity.files)
            file[index++] = f;

        for (int i = 0; i < extra; i++)
            file[i] = new File("/storage/emulated/0/Pictures/Special/"+(i+1)+".jpg");

        ia.addFiles(file);

        gridView.setAdapter(ia);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(GalleryActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
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
}

class ImageAdapter extends BaseAdapter {
    private Context mContext;
    //private Bitmap[] images = new Bitmap[0];
    File[] files = new File[0];

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        //return images.length;

        return files.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public void addImages(Bitmap[] images) {
//        this.images = images;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        //Log.i("getView stuff", position + " " + images[position]);

        //imageView.setImageBitmap(images[position]);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.outHeight = 75;
        opts.inSampleSize = 3;
        imageView.setImageBitmap(BitmapFactory.decodeFile(files[position].getAbsolutePath(), opts));
        return imageView;
    }

    public void addFiles(File[] file) {
        this.files = file;
    }
}
