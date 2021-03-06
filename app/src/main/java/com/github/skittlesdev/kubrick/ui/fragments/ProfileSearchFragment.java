package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.ProfileActivity;
import com.github.skittlesdev.kubrick.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.LinkedList;
import java.util.List;

public class ProfileSearchFragment extends Fragment implements AdapterView.OnItemClickListener {
    private View view;
    private ListView listView;
    private List<ParseUser> results;
    private TextView title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.view = inflater.inflate(R.layout.fragment_media_search, container, false);
        this.listView = (ListView) this.view.findViewById(R.id.results);
        this.title = (TextView) this.view.findViewById(R.id.type);
        this.title.setText(R.string.profiles_search_title);
        this.title.setVisibility(View.GONE);
        return this.view;
    }

    public void search(String searchTerms) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereStartsWith("username", searchTerms);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    showResults(objects);
                }
            }
        });
    }

    private void showResults(List<ParseUser> users) {
        this.results = users;
        List<String> usernames = new LinkedList<>();
        if (!users.isEmpty()){
            title.setVisibility(View.VISIBLE);
        }
        for (ParseUser user: users) {
            usernames.add(user.getUsername());
        }

        ArrayAdapter<String> items = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, usernames);
        this.listView.setAdapter(items);
        this.listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParseUser user = this.results.get(position);
        Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
        profileIntent.putExtra("user_id", user.getObjectId());
        startActivity(profileIntent);
        getFragmentManager().beginTransaction().remove(this).commit();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
