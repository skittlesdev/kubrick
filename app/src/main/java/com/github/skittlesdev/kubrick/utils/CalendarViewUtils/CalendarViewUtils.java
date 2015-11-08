package com.github.skittlesdev.kubrick.utils.CalendarViewUtils;

import android.text.TextUtils;

import com.github.skittlesdev.kubrick.utils.CastUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.HashSet;

import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by louis on 04/11/2015.
 */
public class CalendarViewUtils {

    public static HashSet<CalendarDay> tvSeriesToEpisodeAirDate (TvSeries tvSeries){

        HashSet<CalendarDay> result = new HashSet<CalendarDay>();

            for(TvSeason tvSeason : tvSeries.getSeasons()){
                for(TvEpisode tvEpisode : tvSeason.getEpisodes()){
                    if (tvEpisode != null && !TextUtils.isEmpty(tvEpisode.getAirDate())) {
                        String[] split = tvEpisode.getAirDate().split("-");
                        CalendarDay day = new CalendarDay(Integer.valueOf(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]));
                        result.add(day);
                    }
                }
            }
        return result;
    }

    public static TvEpisode getEpisodeFromDate(CalendarDay day, TvSeries tvSeries){

        if(tvSeries.getSeasons() != null && tvSeries.getSeasons().size() != 0 ) {
            for (TvSeason tvSeason : tvSeries.getSeasons()) {
                if (tvSeason.getEpisodes() != null && tvSeason.getEpisodes().size() != 0) {
                    for (TvEpisode tvEpisode : tvSeason.getEpisodes()) {
                        if (tvEpisode != null && !TextUtils.isEmpty(tvEpisode.getAirDate())) {
                            String[] split = tvEpisode.getAirDate().split("-");
                            if (day.getYear() == Integer.valueOf(split[0])) {
                                if (day.getMonth() == Integer.valueOf(split[1])) {
                                    if (day.getDay() == Integer.valueOf(split[2])) {
                                        return tvEpisode;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
