package com.github.skittlesdev.kubrick.ui.calendar.decorators.TvSeries;

import android.graphics.drawable.ColorDrawable;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by louis on 11/3/15.
 */
public class CalendarViewSeriesPlanningDecoratorNoEpisode implements DayViewDecorator {

    private final int color;

    private final HashSet<CalendarDay> dates;

    public CalendarViewSeriesPlanningDecoratorNoEpisode(int color, Collection<CalendarDay> dates) {

        this.color = color;


        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if(dates.contains(day)){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {
        dayViewFacade.addSpan(new ForegroundColorSpan(color));
    }
}
