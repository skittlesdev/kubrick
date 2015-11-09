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

import info.movito.themoviedbapi.model.people.PersonPeople;

/**
 * Created by low on 09/11/2015.
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
        this.showJob();
        this.showBirthAndDeath();
        this.showPoster();
    }

    private void showName() {
        ((TextView) this.rootView.findViewById(R.id.personName)).setText(this.personPeople.getName());
    }

    private void showJob() {
        ((TextView) this.rootView.findViewById(R.id.personJob)).setText(this.personPeople.getJob());
    }

    private void showBirthAndDeath() {
        String birthAndDeath = "Born the ";
        birthAndDeath += this.personPeople.getBirthday();
        birthAndDeath += " at ";
        birthAndDeath += this.personPeople.getBirthplace();
        String death = this.personPeople.getDeathday();

        if (!TextUtils.isEmpty(death)) {
            birthAndDeath += "Dead the ";
            birthAndDeath += death;
        }

        ((TextView) this.rootView.findViewById(R.id.personBirth)).setText(birthAndDeath);
    }

    private void showPoster() {
        Glide.with(getActivity().getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500" + this.personPeople.getProfilePath())
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                .into((ImageView) rootView.findViewById(R.id.personPoster));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_person_people_header, container, false);

        return this.rootView;
    }
}
