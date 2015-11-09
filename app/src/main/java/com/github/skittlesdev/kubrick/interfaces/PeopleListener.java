package com.github.skittlesdev.kubrick.interfaces;

import info.movito.themoviedbapi.model.people.PersonPeople;

/**
 * Created by lowgr on 11/9/2015.
 */
public interface PeopleListener {
    void onPeopleRetrieved(PersonPeople personPeople);
}
