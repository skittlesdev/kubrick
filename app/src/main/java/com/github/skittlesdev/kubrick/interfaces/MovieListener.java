package com.github.skittlesdev.kubrick.interfaces;

import info.movito.themoviedbapi.model.MovieDb;

public interface MovieListener {
    public void onMovieRetrieved(MovieDb movie);
}
