package com.vaadin.timetable;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ProjectPanel extends VerticalLayout {
    Accordion panel = new Accordion();
    String title = "TITLE";

    public ProjectPanel(){
        addClassName("project-panel");
        VerticalLayout content = new VerticalLayout();
        content.add(new Label("04-04-2020"),new Label("This is the description"));
        panel.add(title,content);
        panel.close();
        add(panel);
    }

}
