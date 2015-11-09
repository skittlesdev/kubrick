package com.github.skittlesdev.kubrick.utils.CalendarViewUtils;

import android.text.TextUtils;

import com.github.skittlesdev.kubrick.customsWrapperTypes.CustomTvEpisode;
import com.github.skittlesdev.kubrick.utils.CastUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import info.movito.themoviedbapi.model.ExternalIds;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by louis on 04/11/2015.
 */
public class CalendarViewUtils {

    public static HashSet<CalendarDay> tvSeriesToEpisodeAirDate (TvSeries tvSeries){

        HashSet<CalendarDay> result = new HashSet<>();

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

    public static HashSet<CalendarDay> tvEpiosdeListToAirDateHash(List<TvEpisode> tvEpisodeList){
        HashSet<CalendarDay> result = new HashSet<>();

        for(TvEpisode tvEpisode : tvEpisodeList){
            if (tvEpisode != null && !TextUtils.isEmpty(tvEpisode.getAirDate())) {
                String[] split = tvEpisode.getAirDate().split("-");
                CalendarDay day = new CalendarDay(Integer.valueOf(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]));
                result.add(day);
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
                                        ExternalIds e = new ExternalIds();
                                        e.setId(tvSeries.getId());
                                        tvEpisode.setExternalIds(e);
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

    public static HashSet<CalendarDay> tvSeriesWatchedEpisodesListToAirDate(List<CustomTvEpisode> customTvEpisodeList){
        HashSet<CalendarDay> result = new HashSet<>();

        for(CustomTvEpisode episode : customTvEpisodeList){
            if (episode != null && !TextUtils.isEmpty(episode.getAirDate())) {
                String[] split = episode.getAirDate().split("-");
                CalendarDay day = new CalendarDay(Integer.valueOf(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]));
                result.add(day);
            }
        }
        return result;
    }

    public static List<CustomTvEpisode> getEpisodesFromDateInWatchedList(CalendarDay day, List<CustomTvEpisode> customTvEpisodeList){

        List<CustomTvEpisode> result = new ArrayList<>();

        for(CustomTvEpisode episode : customTvEpisodeList){
            if(!episode.getAirDate().isEmpty()){
                String[] split = episode.getAirDate().split("-");

                if (day.getYear() == Integer.valueOf(split[0])) {
                    if (day.getMonth() == Integer.valueOf(split[1])) {
                        if (day.getDay() == Integer.valueOf(split[2])) {
                            result.add(episode);
                        }
                    }
                }
            }
        }

        return result;
    }
}
