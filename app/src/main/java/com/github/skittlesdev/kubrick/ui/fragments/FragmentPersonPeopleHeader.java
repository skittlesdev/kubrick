package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        this.showBirth();
        this.showDeath();
        this.showPoster();
    }

    private void showName() {
        ((TextView) this.rootView.findViewById(R.id.personName)).setText(this.personPeople.getName());
    }

    private void showJob() {
        String job = this.personPeople.getJob();
        TextView jobContainer = (TextView) this.rootView.findViewById(R.id.personJob);

        if (TextUtils.isEmpty(job)) {
            jobContainer.setVisibility(View.GONE);
        } else {
            jobContainer.setText(this.personPeople.getJob());
        }
    }

    private void showBirth() {
        String birth = "Born ";
        birth += this.formatDate(this.personPeople.getBirthday());
        birth += ", ";
        birth += this.personPeople.getBirthplace();
        birth += ".";

        ((TextView) this.rootView.findViewById(R.id.personBirth)).setText(birth);
    }

    private void showDeath() {
        String death = this.personPeople.getDeathday();
        String deathString = "";
        TextView deathContainer = (TextView) this.rootView.findViewById(R.id.personDeath);

        if (!TextUtils.isEmpty(death)) {
            deathString += "Dead ";
            deathString += this.formatDate(death);
            deathString += ".";
            deathContainer.setText(deathString);
        } else {
            deathContainer.setVisibility(View.GONE);
        }
    }

    private String formatDate(String dateFormat) {
        try {
            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat newFormat = new SimpleDateFormat("MMMM d, yyyy");
            Date date = oldFormat.parse(dateFormat);

            return newFormat.format(date);
        } catch (ParseException exception) {
            exception.printStackTrace();

            return dateFormat;
        }
    }

    private void showPoster() {
        ((SimpleDraweeView) rootView.findViewById(R.id.personPoster)).setImageURI(Uri.parse("http://image.tmdb.org/t/p/w500" + this.personPeople.getProfilePath()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_person_people_header, container, false);

        return this.rootView;
    }
}
