package com.github.skittlesdev.kubrick.interfaces;

import info.movito.themoviedbapi.model.core.MovieResultsPage;

public interface SearchListener {
    public void onSearchResults(MovieResultsPage results);
}
