package com.codepath.shivagss.imagesearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sandeep on 9/21/2014.
 */
public class ImageResult implements Serializable {

    public String fullURL;
    public String thumbURL;
    public String title;
    public String height;
    public String width;

    public ImageResult(JSONObject response) {
        try {
            this.fullURL = response.getString("url");
            this.thumbURL = response.getString("tbUrl");
            this.title = response.getString("title");
            this.height = response.getString("height");
            this.width = response.getString("width");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray array) {

        ArrayList<ImageResult> list = new ArrayList<ImageResult>();
        for (int i = 0; i < array.length(); i++) {
            try {
                list.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return "ImageResult{" +
                "fullURL='" + fullURL + '\'' +
                ", thumbURL='" + thumbURL + '\'' +
                ", title='" + title + '\'' +
                ", height='" + height + '\'' +
                ", width='" + width + '\'' +
                '}';
    }
}
