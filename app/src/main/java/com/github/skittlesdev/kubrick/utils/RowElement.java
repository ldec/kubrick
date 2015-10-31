package com.github.skittlesdev.kubrick.utils;

/**
 * Created by lowgr on 10/30/2015.
 */
public class RowElement {
    private int mIcon;
    private String mTitle;

    public RowElement(int icon) {
        this(icon, null);
    }

    public RowElement(String title) {
        this(-1, title);
    }

    public RowElement(int icon, String title) {
        this.mIcon = icon;
        this.mTitle = title;
    }

    public void setIcon(int icon) {
        this.mIcon = icon;
    }

    public int getIcon() {
        return this.mIcon;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTitle() {
        return this.mTitle;
    }
}
