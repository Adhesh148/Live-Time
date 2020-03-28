package com.vaadin.timetable;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.timetable.backend.FacultyEntry;


public class FacultyInformationForm extends VerticalLayout {
    H2 header = new H2("Edit Information");
    Label guideline = new Label("* Double-click on the field to edit.");
    TextField facultyCode = new TextField("Faculty Code");
    TextField facultyName = new TextField("Faculty Name");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");



    public FacultyInformationForm(){
        facultyName.setHeight("80px");
        facultyName.setWidth("300px");
        facultyCode.setWidth("300px");
        setSizeUndefined();
        addClassName("faculty-info-form");

        add(header, guideline,facultyCode, facultyName,createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        close.addClickListener(evt -> {
            this.setVisible(false);
        });

        return new HorizontalLayout(save,delete,close);

    }

    public void setInformation(FacultyEntry value) {
        facultyCode.setValue(value.getFacultyCode());
        facultyName.setValue(value.getFacultyName());
    }
}
