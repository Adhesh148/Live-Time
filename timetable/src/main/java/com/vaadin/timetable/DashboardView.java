package com.vaadin.timetable;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Dashboard | Timetable")
@Route(value = "dashbaord",layout = MainView.class)


public class DashboardView extends VerticalLayout {

    String url = "jdbc:mysql://localhost:3306/Flight";
    String user = "dbms";
    String pwd = "Password_123";
    public DashboardView(){
        addClassName("dashboard-view");
    }

}
