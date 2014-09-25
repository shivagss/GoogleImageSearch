package com.codepath.shivagss.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.shivagss.adapters.ImageResultsAdapater;
import com.codepath.shivagss.helpers.EndlessScrollListener;
import com.codepath.shivagss.helpers.Utils;
import com.codepath.shivagss.model.ImageFilter;
import com.codepath.shivagss.model.ImageResult;
import com.codepath.shivagss.helpers.R;
import com.codepath.shivagss.fragment.SettingsDialogFragment;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends Activity implements SettingsDialogFragment.onFilterListener {

    public static final String QUERY_PARAM_IMGTYPE = "&imgtype=";
    public static final String QUERY_PARAM_SITESEARCH = "&as_sitesearch=";
    public static final String QUERY_PARAM_IMGCOLOR = "&imgcolor=";
    public static final String QUERY_PARAM_IMGSZ = "&imgsz=";
    public static final String QUERY_PARAM_RESULT_SIZE_8 = "&rsz=8";
    public static final String QUERY_PARAM_START_OFFSET = "&start=";
    public static final String QUERY_BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
    public static final String BUNDLE_PARAM_SEARCH = "search";
    public static final String BUNDLE_PARAM_FILTER = "filter";
    public static final String INTENT_EXTRA_RESULT = "result";
    public static final String FILTER_ANY = "any";

    private StaggeredGridView mGvSearch;
    private ArrayList<ImageResult> mImageResults;
    private ImageResultsAdapater mImageResultsAdapater;
    private ImageFilter mFilter;
    private SwipeRefreshLayout swipeContainer;
    private SearchView searchView;
    private String mSearchString;
    private ImageView mEmptyImage;
    private TextView mEmptyLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onImageSearch(null);
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mImageResults = new ArrayList<ImageResult>();
        mImageResultsAdapater = new ImageResultsAdapater(this, mImageResults);
        mGvSearch.setAdapter(mImageResultsAdapater);
        mGvSearch.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                search(totalItemsCount);
            }
        });

        handleIntent(getIntent());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!TextUtils.isEmpty(mSearchString)) {
            outState.putString(BUNDLE_PARAM_SEARCH, mSearchString);
            outState.putSerializable(BUNDLE_PARAM_FILTER, mFilter);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSearchString = savedInstanceState.getString(BUNDLE_PARAM_SEARCH);
            mFilter = (ImageFilter) savedInstanceState.getSerializable(BUNDLE_PARAM_FILTER);
            onImageSearch(null);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (intent != null && Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mSearchString = intent.getStringExtra(SearchManager.QUERY);
        }
        onImageSearch(null);
    }

    private void setupViews() {
        mGvSearch = (StaggeredGridView) findViewById(R.id.gvSearch);
        mGvSearch.setEmptyView(findViewById(android.R.id.empty));
        mEmptyLabel = (TextView) findViewById(R.id.empty_label);
        mEmptyImage = (ImageView) findViewById(R.id.empty_image);

        mGvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= mImageResults.size()) return;
                ImageResult result = mImageResults.get(position);

                Intent intent = new Intent(SearchActivity.this, ImageActivity.class);
                intent.putExtra(INTENT_EXTRA_RESULT, result);

                startActivity(intent);
            }
        });
    }

    public void search(int offset) {
        if (!Utils.isNetworkAvailable(this)) {
            mEmptyImage.setImageResource(R.drawable.offline);
            mEmptyLabel.setText(R.string.offline_label);
            if (offset > 0) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.no_internet),
                        Toast.LENGTH_LONG).show();
            }
            return;
        } else {
            mEmptyLabel.setText(R.string.empty_list_label);
            mEmptyImage.setImageResource(R.drawable.ic_launcher);
        }
        if (TextUtils.isEmpty(mSearchString)) {
            return;
        }
        String search = mSearchString;
        StringBuilder searchURL = new StringBuilder();
        searchURL.append(QUERY_BASE_URL);
        searchURL.append(search);
        searchURL.append(QUERY_PARAM_RESULT_SIZE_8);
        searchURL.append(QUERY_PARAM_START_OFFSET);
        searchURL.append(offset);
        if (mFilter != null) {
            if (!mFilter.getSize().equals(FILTER_ANY)) {
                searchURL.append(QUERY_PARAM_IMGSZ);
                searchURL.append(mFilter.getSize());
            }
            if (!mFilter.getColor().equals(FILTER_ANY)) {
                searchURL.append(QUERY_PARAM_IMGCOLOR);
                searchURL.append(mFilter.getColor());
            }
            if (!mFilter.getType().equals(FILTER_ANY)) {
                searchURL.append(QUERY_PARAM_IMGTYPE);
                searchURL.append(mFilter.getType());
            }
            if (!TextUtils.isEmpty(mFilter.getSite())) {
                searchURL.append(QUERY_PARAM_SITESEARCH);
                searchURL.append(mFilter.getSite());
            }
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(searchURL.toString(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject responseData = response.getJSONObject("responseData");
                            JSONArray results = responseData.getJSONArray("results");
                            ArrayList<ImageResult> imageResults = ImageResult.fromJSONArray(results);
                            if (imageResults.isEmpty()) {
                                mEmptyLabel.setText(getString(R.string.no_results));
                                mEmptyImage.setImageResource(R.drawable.ic_launcher);
                            }
                            mImageResultsAdapater.addAll(imageResults);
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

    public void onImageSearch(View v) {
        mImageResultsAdapater.clear();
        search(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchString = query;
                onImageSearch(null);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        if (!TextUtils.isEmpty(mSearchString)) {
            searchItem.expandActionView();
            searchView.setQuery(mSearchString, false);
            searchView.clearFocus();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startSettingsFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSettingsFragment() {
        DialogFragment dialogFrag = SettingsDialogFragment.getInstance(mFilter);
        dialogFrag.show(getFragmentManager(), "dialog");
    }

    @Override
    public void setFilter(ImageFilter filter) {
        mFilter = filter;
        onImageSearch(null);
    }
}
