package com.vaadin.timetable;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import java.util.Calendar;


@PageTitle("Dashboard | Timetable")
@Route(value = "dashboard",layout = MainView.class)


public class DashboardView extends VerticalLayout {
    public DashboardView(){


    }

}
