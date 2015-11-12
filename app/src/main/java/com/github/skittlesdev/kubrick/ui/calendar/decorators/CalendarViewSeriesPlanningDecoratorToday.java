package com.github.skittlesdev.kubrick.ui.calendar.decorators;

import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

/**
 * Created by louis on 04/11/2015.
 */
public class CalendarViewSeriesPlanningDecoratorToday implements DayViewDecorator {

    private final int color;

    public CalendarViewSeriesPlanningDecoratorToday(int color) {
        this.color = color;
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {
        dayViewFacade.addSpan(new DotSpan(5, color));
    }

    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        if(calendarDay.equals(CalendarDay.today())){
            return true;
        }else{
            return false;
        }
    }
}
