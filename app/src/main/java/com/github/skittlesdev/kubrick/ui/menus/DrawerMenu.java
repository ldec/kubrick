package com.github.skittlesdev.kubrick.ui.menus;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.adapters.HomeDrawerAdapter;
import com.github.skittlesdev.kubrick.utils.ProfileElement;
import com.github.skittlesdev.kubrick.utils.RowElement;

import java.util.ArrayList;
import java.util.List;

public class DrawerMenu {
    private Activity activity;
    private DrawerLayout layout;
    private RecyclerView view;

    public DrawerMenu(Activity activity, DrawerLayout layout, RecyclerView view) {
        this.activity = activity;
        this.layout = layout;
        this.view = view;
    }

    public void draw() {
        view.setHasFixedSize(true); // for a better performance
        HomeDrawerAdapter mHomeDrawerAdapter = new HomeDrawerAdapter(generateTitles(), generateProfile());

        this.view.setAdapter(mHomeDrawerAdapter);
        this.view.setLayoutManager(new LinearLayoutManager(activity));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this.activity, this.layout, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code to execute when the drawer is opened
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // code to execute when the drawer is closed
            }
        };

        //toggle.setDrawerIndicatorEnabled(false);
        this.layout.setDrawerListener(toggle);
        toggle.syncState();
    }

    private List<RowElement> generateTitles() {
        List<RowElement> titles = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            titles.add(new RowElement(R.drawable.ic_row_element, "Menu " + i));
        }

        return titles;
    }

    private ProfileElement generateProfile() {
        ProfileElement profileElement = new ProfileElement(R.drawable.default_profile_avatar, "Kubrick", "kubrick@kubrick.com");

        return profileElement;
    }

}
