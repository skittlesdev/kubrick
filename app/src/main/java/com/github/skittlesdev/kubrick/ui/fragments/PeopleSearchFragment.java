package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.MediaActivity;
import com.github.skittlesdev.kubrick.PeopleActivity;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.asyncs.SearchMediaTask;
import com.github.skittlesdev.kubrick.asyncs.SearchPeopleTask;
import com.github.skittlesdev.kubrick.interfaces.PeopleSearchListener;
import com.github.skittlesdev.kubrick.interfaces.SearchListener;

import java.util.LinkedList;
import java.util.List;

import info.movito.themoviedbapi.TmdbPeople;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class PeopleSearchFragment extends Fragment implements PeopleSearchListener, AdapterView.OnItemClickListener {
    private ListView view;
    private TmdbPeople.PersonResultsPage results;
    private SearchPeopleTask peopleSearchTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.view = (ListView) inflater.inflate(R.layout.fragment_media_search, container, false);
        return this.view;
    }

    public void search(String searchTerms) {
        this.peopleSearchTask = new SearchPeopleTask(this);
        this.peopleSearchTask.execute(searchTerms);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.peopleSearchTask != null) {
            this.peopleSearchTask.cancel(true);
        }

        KubrickApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Override
    public void onSearchResults(TmdbPeople.PersonResultsPage results) {
        this.results = results;
        List<String> names = new LinkedList<>();
        for (Person item: results.getResults()) {
            names.add(item.getName());
        }
        ArrayAdapter<String> items = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, names);
        view.setAdapter(items);
        view.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Person item = this.results.getResults().get(position);
        Intent intent = new Intent(getActivity(), PeopleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("person", item);

        intent.putExtra("PERSON_OBJECT", bundle);
        startActivity(intent);
        getFragmentManager().beginTransaction().remove(this).commit();
    }
}
