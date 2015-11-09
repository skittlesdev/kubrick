package com.github.skittlesdev.kubrick;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.skittlesdev.kubrick.asyncs.GetPersonPeople;
import com.github.skittlesdev.kubrick.interfaces.PeopleListener;
import com.github.skittlesdev.kubrick.ui.fragments.FragmentPersonPeopleHeader;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;

import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonPeople;

/**
 * Created by low on 11/9/15.
 */
public class PeopleActivity extends AppCompatActivity implements PeopleListener {
    private Person person = null;
    private GetPersonPeople task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_people);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.person = (Person) this.getIntent().getBundleExtra("PERSON_OBJECT").getSerializable("person");
        this.task = new GetPersonPeople(this);

        this.showBackdrop();
        this.showTitle();
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.task.execute(this.person.getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.task != null) {
            this.task.cancel(true);
        }
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

    @Override
    public void onPeopleRetrieved(PersonPeople personPeople) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Bundle optionsHeader = new Bundle();
        optionsHeader.putSerializable("personPeople", personPeople);

        FragmentPersonPeopleHeader header = new FragmentPersonPeopleHeader();
        header.setArguments(optionsHeader);

        transaction.add(R.id.personHeaderContainer, header);

        transaction.commit();
    }
}