package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.adapters.CreditsOverviewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCast;

import java.util.LinkedList;
import java.util.List;

public class CreditsOverviewFragment extends Fragment {
    private UltimateRecyclerView view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layout = inflater.inflate(R.layout.fragment_favorites_overview, container, false);

        TextView typeView = (TextView) layout.findViewById(R.id.type);
        typeView.setText("Cast");

        this.view = (UltimateRecyclerView) layout.findViewById(R.id.recyclerView);
        this.view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        Credits credits = (Credits) getArguments().getSerializable("credits");
        List<Person> cast = new LinkedList<>();
        for (PersonCast item: credits.getCast()) {
            cast.add(item);
        }
        this.view.setAdapter(new CreditsOverviewAdapter(cast));

        return layout;
    }
}
