package com.codepath.shivagss.imagesearch;

import java.io.Serializable;

/**
 * Created by Sandeep on 9/21/2014.
 */
public class ImageFilter implements Serializable {
    String size;
    String color;
    String type;
    String site;

    public ImageFilter(String size, String color, String type, String site) {
        this.size = size;
        this.color = color;
        this.type = type;
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
