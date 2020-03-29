package com.vaadin.timetable;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;



public class SlotInformationForm extends VerticalLayout {
    H2 heading  = new H2("Course Information");
    TextField courseCode = new TextField("Course Code");
    TextField courseName = new TextField("Course Name");
    TextField faculty = new TextField("Faculty InCharge");
    TextField venue = new TextField("Venue");
    TextArea requirements = new TextArea("Requirements");



    public SlotInformationForm(){
        addClassName("course-info-form");
        HorizontalLayout header = new HorizontalLayout();
        header.add(heading);
        header.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        add(header);
        Div line = new Div();
        line.getStyle().set("width","100%").set("border-top","4px solid gainsboro");
        add(line);
        add(new FormLayout(courseCode,courseName,faculty,venue,requirements));

    }

    public void setValues(String courseC, String courseN, String facultyN, String hall, String[] require, int iter) {
        courseCode.setValue(courseC);
        courseName.setValue(courseN);
        faculty.setValue(facultyN);
        venue.setValue(hall);
        requirements.setValue("");
        requirements.setHeightFull();
        while(iter>=0){
            if(requirements.isEmpty() == true)
                requirements.setValue(require[iter--]);
            else
                requirements.setValue(requirements.getValue()+"\n"+require[iter--]);
        }

    }
}
