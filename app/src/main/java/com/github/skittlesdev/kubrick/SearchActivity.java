package com.github.skittlesdev.kubrick;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.github.skittlesdev.kubrick.asyncs.SearchMediaTask;
import com.github.skittlesdev.kubrick.interfaces.SearchListener;
import com.github.skittlesdev.kubrick.ui.fragments.MediaSearchFragment;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvSeries;
import android.support.v7.widget.Toolbar;
import java.util.LinkedList;
import java.util.List;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private TmdbSearch.MultiListResultsPage results;
    private MediaSearchFragment mediaSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        this.setActionListener();

        ImageButton submitButton = (ImageButton) findViewById(R.id.searchButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.mediaSearch = new MediaSearchFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.mediaSearchLayout, this.mediaSearch, "mediaSearch");
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        new ToolbarMenu(this).filterItems(menu);
        return true;
    }

    private void setActionListener() {
        final EditText searchInput = (EditText) findViewById(R.id.search);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2){
                    Context context = ((ContextWrapper) searchInput.getContext()).getBaseContext();
                    SearchActivity searchActivity =(SearchActivity) context;
                    TextView tv = new TextView(context);
                    tv.setText(s);
                    searchActivity.executeSearchTask(tv, searchActivity);
                }
            }
        });

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView searchInput, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Context context = searchInput.getContext();

                    if (context instanceof SearchActivity) {
                        SearchActivity searchActivity = (SearchActivity) context;
                        searchActivity.executeSearchTask(searchInput, searchActivity);
                        handled = true;
                    }
                }

                return handled;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new ToolbarMenu(this).itemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        this.executeSearchTask((EditText) findViewById(R.id.search), this);
    }

    public void executeSearchTask(TextView textView, Context context) {
        String terms = textView.getText().toString();
        this.mediaSearch.executeSearchTask(terms);
    }
}
