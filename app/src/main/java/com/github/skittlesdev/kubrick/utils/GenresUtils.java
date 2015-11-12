package com.github.skittlesdev.kubrick.utils;

import java.util.List;

import info.movito.themoviedbapi.model.Genre;

/**
 * Created by louis on 01/11/2015.
 */
public class GenresUtils {

    public static String getGenrePrintableString(List<Genre> genreList){
        String result = "";
        for(Genre item : genreList){
            result += item.getName() + " | ";
        }

        if (result.length() > 1) {
            result = result.substring(0, result.length() - 1);
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

}
