package com.vaadin.timetable;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Report View | Timetable")
@Route(value = "report_view",layout = MainView.class)
public class AdminReportView extends VerticalLayout {

    public AdminReportView(){


    }
}
