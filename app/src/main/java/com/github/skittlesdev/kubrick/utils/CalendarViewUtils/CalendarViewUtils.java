package com.github.skittlesdev.kubrick.utils.CalendarViewUtils;

import android.text.TextUtils;

import android.util.Log;
import com.github.skittlesdev.kubrick.models.SeriesEpisode;
import com.github.skittlesdev.kubrick.utils.CastUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by louis on 04/11/2015.
 */
public class CalendarViewUtils {

    public static HashSet<CalendarDay> tvSeriesToEpisodeAirDate (List<SeriesEpisode> episodes){

        HashSet<CalendarDay> result = new HashSet<CalendarDay>();

        for(SeriesEpisode tvEpisode : episodes){
            if (tvEpisode != null && tvEpisode.airDate != null) {
                String[] split = tvEpisode.airDate.split("-");
                CalendarDay day = new CalendarDay(Integer.valueOf(split[0]), Integer.valueOf(split[1]) - 1, Integer.valueOf(split[2]));
                result.add(day);
            }
        }
        return result;
    }

    public static SeriesEpisode getEpisodeFromDate(CalendarDay day, List<SeriesEpisode> episodes){
        for (SeriesEpisode episode: episodes) {
            if (episode != null && episode.airDate != null) {
                String[] split = episode.airDate.split("-");
                if (day.getYear() == Integer.parseInt(split[0])) {
                    if (day.getMonth() + 1 == Integer.parseInt(split[1])) {
                        if (day.getDay() == Integer.parseInt(split[2])) {
                            return episode;
                        }
                    }
                }
            }
        }

        return null;
    }
}
