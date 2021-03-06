package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;

import info.movito.themoviedbapi.model.people.PersonPeople;

/**
 * Created by low on 09/11/2015.
 */
public class FragmentPersonPeopleOverview extends Fragment {
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

        this.showOverview();
    }

    private void showOverview() {
        String overview = this.personPeople.getBiography();
        TextView container = (TextView) rootView.findViewById(R.id.personBiography);

        if (!TextUtils.isEmpty(overview)) {
            container.setText(overview);
        } else {
            ((CardView) this.rootView.findViewById(R.id.personOverviewCardView)).setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_person_people_overview, container, false);

        return this.rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
