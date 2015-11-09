package com.github.skittlesdev.kubrick;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.asyncs.GetPersonPeople;
import com.github.skittlesdev.kubrick.interfaces.PeopleListener;
import com.github.skittlesdev.kubrick.ui.fragments.FragmentPersonPeopleHeader;
import com.github.skittlesdev.kubrick.ui.fragments.FragmentPersonPeopleOverview;
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
        this.task.execute(this.person.getId());

        this.showBackdrop();
        this.showTitle();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        ((SimpleDraweeView) findViewById(R.id.personBackDropPicture)).setImageURI(Uri.parse("http://image.tmdb.org/t/p/w780" + this.person.getProfilePath()));
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

        Bundle options = new Bundle();
        options.putSerializable("personPeople", personPeople);

        FragmentPersonPeopleHeader header = new FragmentPersonPeopleHeader();
        header.setArguments(options);

        FragmentPersonPeopleOverview overview = new FragmentPersonPeopleOverview();
        overview.setArguments(options);

        transaction.add(R.id.personHeaderContainer, header);
        transaction.add(R.id.personOverviewContainer, overview);

        transaction.commit();
    }
}