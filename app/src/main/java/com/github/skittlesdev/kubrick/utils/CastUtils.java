package com.github.skittlesdev.kubrick.utils;

import java.util.List;

import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.people.PersonCrew;

/**
 * Created by louis on 01/11/2015.
 */
public class CastUtils {

    // TODO : Implement a generics

    public static String getCastPrintableString(List<PersonCast> castList){
        String result = "";
        for(PersonCast item : castList){
            result += item.getName() + ", ";
        }

        result = result.substring(0,result.length()-1);
        result = result.substring(0,result.length()-1);

        return result;
    }

    public static String getCrewPrintableString(List<PersonCrew> crewList){
        String result = "";
        for(PersonCrew item : crewList){
            result += item.getName() + ", ";
        }

        result = result.substring(0,result.length()-1);
        result = result.substring(0,result.length()-1);

        return result;
    }
}
