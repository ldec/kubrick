package com.github.skittlesdev.kubrick;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.github.skittlesdev.kubrick.models.SeriesEpisode;
import com.github.skittlesdev.kubrick.ui.SeasonListAdapter;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import com.parse.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by louis on 10/11/2015.
 */
public class SeasonListActivity extends AppCompatActivity {
    private int seriesId;
    private TvSeries series;
    private SwipeMenuListView listView;
    private List<HashMap<String, Object>> seasons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.serie_episode_list_main);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();
// I want to change this too
        listView = (SwipeMenuListView) findViewById(R.id.seasonList);
// This is a test commit for a merge V2
        this.seriesIds = getIntent().getExtras().getInt("seriesId");
        this.series = (TvSeries) getIntent().getExtras().getSerializable("series");

        HashMap<String, String> params = new HashMap<>();V2
		
        params.put("seriesId", String.valueOf(seriesId));
        ParseCloud.callFunctionInBackground("getSeriesSeasons", params, new FunctionCallback<List<HashMap<String, Object>>>() {
            @Override
            public void done(List<HashMap<String, Object>> result, ParseException erfe) {
                ListAdapter appAdapter = new SeasonListAV2dapter(result);
                listView.setAdapter(appAdapter);
                seasons = result;
                setUpSeasonList();V2
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle state) { // SMALL FIX
        try {
            /*
            *
            * WE MUST DO THIS because sometime a org.json.JSONObject.NULL has to be
            * serialized (received by the TMDB wV2rapper), and it is not working
            * because org.json.JSONObject.NULL is no serializable.
            *
            * */
            // super.onSaveInstanceState(state); catch not taken?
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSeriesId() {
        return seriesId;
    }

    private void setUpSeasonList(){
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem seasonWatchedItem = new SwipeMenuItem(getApplicationContext());
                seasonWatchedItem.setBackground(new ColorDrawable(Color.WHITE));
                seasonWatchedItem.setWidth(dp2px(90));
                seasonWatchedItem.setIcon(R.drawable.ic_view);
                menu.addMenuItem(seasonWatchedItem);
            }
        };

        listView.setMenuCreator(creator);


        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                HashMap<String, Object> item = seasons.get(position);
                switch (index) {
                    case 0:
                        setSeasonAsWatched(series, item);
                        Toast.makeText(KubrickApplication.getContext(), "Season set as watched", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplication().getApplicationContext(), EpisodeListActivity.class);
                Bundle bundle = new Bundle();

                HashMap<String, Object> season = seasons.get(position);
                LinkedList<SeriesEpisode> episodes = new LinkedList<>();
                for (HashMap<String, Object> item: (List<HashMap<String, Object>>) season.get("episodes")) {
                    episodes.add(new SeriesEpisode(series, item));
                }

                bundle.putSerializable("episodes", episodes);
                bundle.putInt("seriesId", getSeriesId());
                bundle.putSerializable("series", series);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void setSeasonAsWatched(TvSeries tvSeries, HashMap<String, Object> tvSeason) {

        ParseObject favorite = new ParseObject("ViewedTvSeriesEpisodes");

        if (ParseUser.getCurrentUser() == null) {
            Toast.makeText(KubrickApplication.getContext(), "Please login", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("seriesId", String.valueOf(tvSeries.getId()));
        params.put("seasonNumber", String.valueOf(tvSeason.get("season_number")));
        ParseCloud.callFunctionInBackground("viewedSeriesSeason", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                Toast.makeText(KubrickApplication.getContext(), "Season watch success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        new ToolbarMenu(this).filterItems(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new ToolbarMenu(this).itemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }
}
