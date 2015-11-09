package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.skittlesdev.kubrick.R;

import java.text.DecimalFormat;
import java.util.List;

import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.MovieImages;
import info.movito.themoviedbapi.model.people.PersonPeople;
import info.movito.themoviedbapi.model.tv.TvEpisode;

/**
 * Created by louis on 04/11/2015.
 */
public class FragmentPersonPeopleHeader extends Fragment {
    private PersonPeople personPeople;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.personPeople = (PersonPeople) this.getArguments().getSerializable("personPeople");
    }

    @Override
    public void onStart() {
        super.onStart();

        this.showName();
    }

    private void showName() {
        ((TextView) this.rootView.findViewById(R.id.personName)).setText(this.personPeople.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_person_people_header, container, false);

        return this.rootView;
    }
}
