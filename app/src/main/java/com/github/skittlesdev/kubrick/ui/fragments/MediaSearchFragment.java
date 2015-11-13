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
import com.github.skittlesdev.kubrick.MediaActivity;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.asyncs.SearchMediaTask;
import com.github.skittlesdev.kubrick.interfaces.SearchListener;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvSeries;

import java.util.LinkedList;
import java.util.List;

public class MediaSearchFragment extends Fragment implements SearchListener, AdapterView.OnItemClickListener {
    private View view;
    private ListView listView;
    private TmdbSearch.MultiListResultsPage results;
    private SearchMediaTask searchTask;
    private TextView title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.view = inflater.inflate(R.layout.fragment_media_search, container, false);
        this.listView = (ListView) this.view.findViewById(R.id.results);
        this.title = (TextView) this.view.findViewById(R.id.type);
        this.title.setText(R.string.media_search_title);
        this.title.setVisibility(View.GONE);
        return this.view;
    }

    public void search(String searchTerms) {
        if (this.searchTask != null) {
            this.searchTask.cancel(true);
        }
        this.searchTask = new SearchMediaTask(this);
        this.searchTask.execute(searchTerms);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.searchTask != null) {
            this.searchTask.cancel(true);
        }
    }

    @Override
    public void onSearchResults(TmdbSearch.MultiListResultsPage results) {
        this.results = results;
        List<String> titles = new LinkedList<>();
        for (Multi item: results.getResults()) {
            if (item.getMediaType() == Multi.MediaType.MOVIE) {
                titles.add(((MovieDb) item).getTitle());
            }
            if (item.getMediaType() == Multi.MediaType.TV_SERIES) {
                titles.add(((TvSeries) item).getName());
            }
        }
        if (!titles.isEmpty()){
            title.setVisibility(View.VISIBLE);
        }

        ArrayAdapter<String> items = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, titles);
        this.listView.setAdapter(items);
        this.listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Multi item = this.results.getResults().get(position);
        if (item.getMediaType() == Multi.MediaType.MOVIE) {
            int movieId = ((IdElement) item).getId();
            Intent intent = new Intent(getActivity(), MediaActivity.class);
            intent.putExtra("MEDIA_ID", movieId);
            intent.putExtra("MEDIA_TYPE", "movie");
            startActivity(intent);
        }

        if (item.getMediaType() == Multi.MediaType.TV_SERIES) {
            int seriesId = ((IdElement) item).getId();
            Intent intent = new Intent(getActivity(), MediaActivity.class);
            intent.putExtra("MEDIA_ID", seriesId);
            intent.putExtra("MEDIA_TYPE", "tv");
            startActivity(intent);
        }
        getFragmentManager().beginTransaction().remove(this).commit();
    }
}
