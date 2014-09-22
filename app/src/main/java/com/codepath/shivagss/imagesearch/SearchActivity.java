package com.codepath.shivagss.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.codepath.shivagss.adapters.ImageResultsAdapater;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends Activity {

    private static final int REQUEST_CODE = 100;

    private EditText mEtSearch;
    private GridView mGvSearch;
    private ArrayList<ImageResult> mImageResults;
    private ImageResultsAdapater mImageResultsAdapater;

    private String size;
    private String color;
    private String type;
    private String site;
    private ImageFilter mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();

        mImageResults = new ArrayList<ImageResult>();
        mImageResultsAdapater = new ImageResultsAdapater(this, mImageResults);
        mGvSearch.setAdapter(mImageResultsAdapater);
        mGvSearch.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("DEBUGGING", "Load More: " + totalItemsCount );
                search(totalItemsCount);
            }
        });
    }

    private void setupViews() {
        mEtSearch = (EditText) findViewById(R.id.etSearch);
        mGvSearch = (GridView) findViewById(R.id.gvSearch);

        mGvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >= mImageResults.size()) return;
                ImageResult result = mImageResults.get(position);

                Intent intent = new Intent(SearchActivity.this, ImageActivity.class);
                intent.putExtra("result", result);

                startActivity(intent);
            }
        });
    }

    public void search(int offset){
        String search = mEtSearch.getText().toString();
        StringBuilder searchURL = new StringBuilder();
        searchURL.append("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=");
        searchURL.append(search);
        searchURL.append("&rsz=8");
        searchURL.append("&start=");
        searchURL.append(offset);
        if(mFilter != null){
            if(!mFilter.size.equals("none")){
                searchURL.append("&imgsz=");
                searchURL.append(mFilter.size);
            }
            if(!mFilter.color.equals("none")){
                searchURL.append("&imgcolor=");
                searchURL.append(mFilter.color);
            }
            if(!mFilter.type.equals("none")){
                searchURL.append("&imgtype=");
                searchURL.append(mFilter.type);
            }
            if(!TextUtils.isEmpty(mFilter.site)){
                searchURL.append("&as_sitesearch=");
                searchURL.append(mFilter.site);
            }
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(searchURL.toString(), new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject responseData = response.getJSONObject("responseData");
                            JSONArray results = responseData.getJSONArray("results");
                            mImageResultsAdapater.addAll(ImageResult.fromJSONArray(results));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                }
        );
    }

    public void onImageSearch(View v){
        mImageResultsAdapater.clear();
        search(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            ImageFilter filter = (ImageFilter) data.getSerializableExtra("filter");
            if(filter != null){
                mFilter = filter;
            }
            onImageSearch(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startSettingsActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("filter", mFilter);
        startActivityForResult(intent, REQUEST_CODE);
    }
}
