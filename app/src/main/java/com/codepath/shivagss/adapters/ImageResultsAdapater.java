package com.codepath.shivagss.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.shivagss.imagesearch.ImageResult;
import com.codepath.shivagss.imagesearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sandeep on 9/21/2014.
 */
public class ImageResultsAdapater extends ArrayAdapter<ImageResult> {

    public ImageResultsAdapater(Context context, List<ImageResult> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position >= getCount()) return convertView;
        ImageResult result = getItem(position);

        if(convertView == null){
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_result,parent,false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivThumbnail);
        TextView textView = (TextView) convertView.findViewById(R.id.tvTitle);

        imageView.setImageResource(0);
        Picasso.with(getContext()).load(result.thumbURL).placeholder(R.drawable.ic_launcher).into(imageView);
        textView.setText(Html.fromHtml(result.title)
        );

        return convertView;
    }
}
