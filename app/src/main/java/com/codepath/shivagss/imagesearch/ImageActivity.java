package com.codepath.shivagss.imagesearch;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getActionBar().hide();

        ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
        final ImageView imageView = (ImageView) findViewById(R.id.ivFullImage);

        Picasso picasso = new Picasso.Builder(getBaseContext()).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {

                Toast.makeText(getApplicationContext(),
                        "Failed loading image",
                        Toast.LENGTH_LONG).show();
                Log.e(ImageActivity.class.getName(), e.getMessage() + " - " + uri.toString());
                e.printStackTrace();
                imageView.setImageResource(android.R.drawable.ic_dialog_alert);
            }
        }).build();
        picasso.
                load(result.fullURL).
                placeholder(R.drawable.ic_launcher).
                into(imageView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
