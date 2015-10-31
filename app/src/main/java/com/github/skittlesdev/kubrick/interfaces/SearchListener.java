package com.github.skittlesdev.kubrick.interfaces;

import info.movito.themoviedbapi.TmdbSearch;

public interface SearchListener {
    void onSearchResults(TmdbSearch.MultiListResultsPage results);
}
