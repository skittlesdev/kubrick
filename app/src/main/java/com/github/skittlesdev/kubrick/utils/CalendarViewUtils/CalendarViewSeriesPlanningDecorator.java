package com.github.skittlesdev.kubrick.utils.CalendarViewUtils;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by louis on 11/3/15.
 */
public class CalendarViewSeriesPlanningDecorator implements DayViewDecorator {

    private final int noEpisodeColor;
    private final int episodeColor;
    private final int nextEpisodeColor;

    private final HashSet<CalendarDay> dates;

    public CalendarViewSeriesPlanningDecorator(int noEpisodeColor,int episodeColor, int nextEpisodeColor, Collection<CalendarDay> dates) {

        this.noEpisodeColor = noEpisodeColor;
        this.episodeColor = episodeColor;
        this.nextEpisodeColor = nextEpisodeColor;

        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, nextEpisodeColor));
    }
}
