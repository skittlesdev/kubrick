package com.github.skittlesdev.kubrick;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import info.movito.themoviedbapi.model.people.Person;

/**
 * Created by low on 11/9/15.
 */
public class PeopleActivity extends AppCompatActivity {
    private Person person = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_people);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.person = (Person) this.getIntent().getBundleExtra("PERSON_OBJECT").getSerializable("person");
        //this.seriePosterPath = this.getIntent().getStringExtra("seriePoster");
        //this.serieBackdroptPath = this.getIntent().getStringExtra("serieBackdrop");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Bundle optionsHeader = new Bundle();
        //optionsHeader.putSerializable("person", person);
        //optionsHeader.putString("seriePoster", this.seriePosterPath);

        //Bundle optionsOverview = new Bundle();
        //optionsOverview.putSerializable("person", person);

        //FragmentTvEpisodeHeader header = new FragmentTvEpisodeHeader();
        //header.setArguments(optionsHeader);

        //FragmenTvEpisodeOverview overview = new FragmenTvEpisodeOverview();
        //overview.setArguments(optionsOverview);

        //transaction.add(R.id.episodeHeaderContainer, header);
        //transaction.add(R.id.episodeOverviewContainer, overview);
        transaction.commit();

        this.showBackdrop();
        this.showTitle();
    }

    private void showTitle() {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(true);
        collapsingToolbar.setTitle(this.person.getName());
    }

    private void showBackdrop() {
        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w780" + this.person.getProfilePath())
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                .into((ImageView) this.findViewById(R.id.personBackDropPicture));
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }
}