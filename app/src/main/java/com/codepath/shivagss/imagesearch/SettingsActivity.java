package com.codepath.shivagss.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

    private Spinner mSpinnerType;
    private Spinner mSpinnerSize;
    private Spinner mSpinnerColor;
    private EditText mEditTextSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupViews();

        Intent intent = getIntent();
        ImageFilter filter = (ImageFilter) intent.getSerializableExtra("filter");

        if(filter != null){
            mSpinnerSize.setSelection(((ArrayAdapter)mSpinnerSize.getAdapter()).getPosition(filter.getSize()));
            mSpinnerColor.setSelection(((ArrayAdapter)mSpinnerColor.getAdapter()).getPosition(filter.getColor()));
            mSpinnerType.setSelection(((ArrayAdapter)mSpinnerType.getAdapter()).getPosition(filter.getType()));
            mEditTextSite.setText(filter.getSite());
        }
    }

    public void onSave(View v){
        String size = (String) mSpinnerSize.getSelectedItem();
        String color = (String) mSpinnerColor.getSelectedItem();
        String type = (String) mSpinnerType.getSelectedItem();
        String site = mEditTextSite.getText().toString();

        ImageFilter filter = new ImageFilter(size,color,type,site);

        Intent intent = new Intent();
        intent.putExtra("filter", filter);

        setResult(RESULT_OK, intent);

        finish();
    }

    private void setupViews() {
        mSpinnerSize = (Spinner) findViewById(R.id.spSize);
        mSpinnerColor = (Spinner) findViewById(R.id.spColor);
        mSpinnerType = (Spinner) findViewById(R.id.spType);
        mEditTextSite = (EditText) findViewById(R.id.etSite);
    }
}
