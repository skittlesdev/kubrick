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
    private ListView view;
    private TmdbSearch.MultiListResultsPage results;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.view = (ListView) inflater.inflate(R.layout.fragment_media_search, container, false);
        return this.view;
    }

    public void search(String searchTerms) {
        SearchMediaTask searchTask = new SearchMediaTask(this);
        searchTask.execute(searchTerms);
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

        ArrayAdapter<String> items = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, titles);
        view.setAdapter(items);
        view.setOnItemClickListener(this);
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
    }
}
