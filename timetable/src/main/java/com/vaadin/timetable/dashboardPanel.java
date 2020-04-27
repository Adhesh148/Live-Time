package com.vaadin.timetable;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class dashboardPanel extends VerticalLayout {
    public dashboardPanel(int number,String desc){
        addClassName("dashboard-panel");
        Label stat = new Label(String.valueOf(number));
        stat.addClassName("stat");
        Label detail = new Label(desc);
        detail.addClassName("detail");

        setSizeUndefined();
        add(stat,detail);
    }
}
