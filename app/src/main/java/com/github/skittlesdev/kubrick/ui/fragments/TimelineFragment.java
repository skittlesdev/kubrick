package com.github.skittlesdev.kubrick.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.github.skittlesdev.kubrick.R;
import com.parse.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TimelineFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView view;
    private List<HashMap<String, Object>> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.view = (ListView) inflater.inflate(R.layout.fragment_media_search, container, false);
        this.view.setOnItemClickListener(this);
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
                items = object;
                List<String> messages = new LinkedList<String>();
                for(HashMap<String, Object> item: object) {
                    messages.add((String) item.get("message"));
                }
                ArrayAdapter<String> items = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, messages);
                view.setAdapter(items);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, Object> item = this.items.get(position);
        String uri = (String) item.get("uri");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));

        startActivity(intent);
    }
}
