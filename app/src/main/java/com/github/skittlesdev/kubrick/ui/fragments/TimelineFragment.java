package com.github.skittlesdev.kubrick.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.adapters.TimelineAdapter;
import com.parse.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TimelineFragment extends Fragment {
    private RecyclerView view;
    private List<HashMap<String, Object>> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.view = (RecyclerView) inflater.inflate(R.layout.fragment_timeline, container, false);
        this.view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return this.view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser() == null) {
            return;
        }

        ParseCloud.callFunctionInBackground("getTimeline", new HashMap<String, Object>(), new FunctionCallback<List<HashMap<String, Object>>>() {
            @Override
            public void done(List<HashMap<String, Object>> object, ParseException e) {
                TimelineAdapter adapter = new TimelineAdapter(object);
                view.setAdapter(adapter);
            }
        });
    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, Object> item = this.items.get(position);
        String uri = (String) item.get("uri");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));

        startActivity(intent);
    }*/
}
