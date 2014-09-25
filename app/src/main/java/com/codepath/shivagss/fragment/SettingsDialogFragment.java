package com.codepath.shivagss.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.shivagss.helpers.R;
import com.codepath.shivagss.model.ImageFilter;

public class SettingsDialogFragment extends DialogFragment {

    private Spinner mSpinnerType;
    private Spinner mSpinnerSize;
    private Spinner mSpinnerColor;
    private EditText mEditTextSite;
    private ImageFilter filter;
    private onFilterListener mCallback;

    public SettingsDialogFragment() {
    }

    public static SettingsDialogFragment getInstance(ImageFilter filter) {
        SettingsDialogFragment fragment = new SettingsDialogFragment();
        fragment.filter = filter;
        return fragment;
    }

    // Container Activity must implement this interface
    public interface onFilterListener {
        public void setFilter(ImageFilter filter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (onFilterListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnEditItemListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container);

        getDialog().setTitle(R.string.title_activity_settings);
        setupViews(view);

        if (filter != null) {
            mSpinnerSize.setSelection(((ArrayAdapter) mSpinnerSize.getAdapter()).getPosition(filter.getSize()));
            mSpinnerColor.setSelection(((ArrayAdapter) mSpinnerColor.getAdapter()).getPosition(filter.getColor()));
            mSpinnerType.setSelection(((ArrayAdapter) mSpinnerType.getAdapter()).getPosition(filter.getType()));
            mEditTextSite.setText(filter.getSite());
        }
        return view;
    }

    public void onSave() {
        String size = (String) mSpinnerSize.getSelectedItem();
        String color = (String) mSpinnerColor.getSelectedItem();
        String type = (String) mSpinnerType.getSelectedItem();
        String site = mEditTextSite.getText().toString();

        ImageFilter filter = new ImageFilter(size, color, type, site);

        mCallback.setFilter(filter);
        this.dismiss();
    }

    private void setupViews(View v) {
        mSpinnerSize = (Spinner) v.findViewById(R.id.spSize);
        mSpinnerColor = (Spinner) v.findViewById(R.id.spColor);
        mSpinnerType = (Spinner) v.findViewById(R.id.spType);
        mEditTextSite = (EditText) v.findViewById(R.id.etSite);

        Button save = (Button) v.findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });
    }
}
