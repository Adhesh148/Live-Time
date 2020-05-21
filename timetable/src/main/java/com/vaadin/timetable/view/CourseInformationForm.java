package com.vaadin.timetable.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.timetable.backend.CourseEntry;
import com.vaadin.timetable.backend.FacultyEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CourseInformationForm extends VerticalLayout {
    //Pre-requisites for mysql connection
    String url = "jdbc:mysql://aauorfmbt136d0.cuz1bxluuufz.ap-south-1.rds.amazonaws.com:3306/liveTimetable";
    String user = "dbms";
    String pwd = "Password_123";

    String originalCC = "";
    String OriginalCN = "";

    H2 header = new H2("Edit Information");
    Label guideline = new Label("* Double-click on the field to edit.");
    TextField courseCode = new TextField("Course Code");
    TextField courseName = new TextField("Course Name");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public CourseInformationForm(){
        courseName.setHeight("80px");
        courseName.setWidth("400px");
        courseCode.setWidth("400px");
        this.setWidth("500px");
        //setSizeUndefined();
        addClassName("course-info-form");

        add(header, guideline, courseCode, courseName,createButtonsLayout());
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

        delete.addClickListener(evt -> deleteCourse());

        save.addClickListener(evt -> {
            saveCourse();
        });

        return new HorizontalLayout(save,delete,close);
    }

    private void saveCourse() {
        Dialog dialog = new Dialog();
        H4 header = new H4("Confirm Edit");
        Label message = new Label("Are you sure you want to update the item?");
        Button cancel = new Button("Cancel");
        Button update = new Button("Update");

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FlexLayout UpdateButtonWrapper = new FlexLayout(update);
        UpdateButtonWrapper.setJustifyContentMode(JustifyContentMode.END);

        HorizontalLayout dialogButtons = new HorizontalLayout(cancel,UpdateButtonWrapper);
        dialogButtons.expand(UpdateButtonWrapper);

        dialog.add(new VerticalLayout(header,message),dialogButtons);
        dialog.setWidth("400px");
        dialog.setHeight("150px");
        dialog.open();

        update.addClickListener(evt -> {
            if(originalCC.equals(""))
                onADD();
            else
                onSave();
            dialog.close();
            update.getUI().ifPresent(ui -> {ui.navigate("");});
            update.getUI().ifPresent(ui -> {ui.navigate("course-view");});

        });

        cancel.addClickListener(evt -> {
            dialog.close();
        });

    }

    private void onADD() {
        String CC = courseCode.getValue();
        String CN = courseName.getValue();

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            boolean rs;
            String sql = "Insert into course values ('"+CC+"','"+CN+"');";
            rs = stmt.execute(sql);
            Notification.show("Row Successfully Inserted.", 2000,Notification.Position.MIDDLE);
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void onSave() {
        String newCC = courseCode.getValue();
        String newCN = courseName.getValue();

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            int rs;
            String sql = "update course set courseName ='"+newCN+"',courseCode = '"+newCC+"' where courseCode='"+originalCC+"';";
            rs = stmt.executeUpdate(sql);
            if(rs>0)
                Notification.show("Row Successfully Updated.", 2000,Notification.Position.MIDDLE);
            else if(rs == 0)
                Notification.show("Row cannot be updated.", 2000,Notification.Position.MIDDLE);
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage(),2000,Notification.Position.MIDDLE);
        }

    }

    private void deleteCourse() {
        Dialog dialog = new Dialog();
        H4 header = new H4("Confirm Delete");
        Label message = new Label("Are you sure you want to delete the item?");
        Button cancel = new Button("Cancel");
        Button delete = new Button("Delete");

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        FlexLayout deleteButtonWrapper = new FlexLayout(delete);
        deleteButtonWrapper.setJustifyContentMode(JustifyContentMode.END);

        HorizontalLayout dialogButtons = new HorizontalLayout(cancel,deleteButtonWrapper);
        dialogButtons.expand(deleteButtonWrapper);

        dialog.add(new VerticalLayout(header,message),dialogButtons);
        dialog.setWidth("400px");
        dialog.setHeight("150px");
        dialog.open();

        delete.addClickListener(evt -> {
            onDelete();
            dialog.close();
            // Roundabout way to refresh the grid. Could look into another way..
            delete.getUI().ifPresent(ui -> {ui.navigate("");});
            delete.getUI().ifPresent(ui -> {ui.navigate("course-view");});
            //Could show a dialog box
        });
        cancel.addClickListener(evt -> {
            dialog.close();
        });

    }

    private void onDelete() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            int rs;
            String sql;
            sql = "delete from course where courseCode = '"+courseCode.getValue()+"';";
            rs = stmt.executeUpdate(sql);
            if(rs > 0)
                Notification.show("Row Successfully Deleted.", 2000,Notification.Position.MIDDLE);
            else
                Notification.show("Unsuccessful.", 2000,Notification.Position.MIDDLE);
            this.setVisible(false);
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    public void setInformation(CourseEntry value) {
        courseCode.setValue(value.getCourseCode());
        courseName.setValue(value.getCourseName());

        originalCC = value.getCourseCode();
        OriginalCN = value.getCourseName();
    }
}
