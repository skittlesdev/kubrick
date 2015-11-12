package com.github.skittlesdev.kubrick.ui.calendar.decorators;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;

/**
 * Created by louis on 04/11/2015.
 */
public class CalendarViewSeriesPlanningDecoratorPassedEpisodes implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;

    public CalendarViewSeriesPlanningDecoratorPassedEpisodes(int color, HashSet<CalendarDay> dates){
        this.color = color;
        this.dates = dates;
    }

    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        if(dates.contains(calendarDay) && ! calendarDay.isAfter(CalendarDay.today())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {
        dayViewFacade.addSpan(new ForegroundColorSpan(color));
    }
}
