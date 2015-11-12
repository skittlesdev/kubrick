package com.github.skittlesdev.kubrick.interfaces;

import info.movito.themoviedbapi.TmdbPeople;

public interface PeopleSearchListener {
    void onSearchResults(TmdbPeople.PersonResultsPage results);
}
