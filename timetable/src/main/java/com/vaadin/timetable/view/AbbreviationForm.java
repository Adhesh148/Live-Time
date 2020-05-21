package com.vaadin.timetable.view;

import com.vaadin.flow.component.Component;
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
import com.vaadin.timetable.backend.AbbreviationEntry;
import com.vaadin.timetable.backend.MailEntry;
import com.vaadin.timetable.security.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AbbreviationForm extends VerticalLayout {
    //Pre-requisites for mysql connection
    String url = "jdbc:mysql://aauorfmbt136d0.cuz1bxluuufz.ap-south-1.rds.amazonaws.com:3306/liveTimetable";
    String user = "dbms";
    String pwd = "Password_123";

    String originalCC = "";

    H2 header = new H2("Edit Information");
    Label guideline = new Label("* Double-click on the field to edit.");

    TextField courseCode = new TextField("Course Code");
    TextField courseName = new TextField("Course Name");
    TextField abbreviation = new TextField("Abbreviation");

    Button save = new Button("Save");
    Button close = new Button("Cancel");

    public AbbreviationForm(){
        courseCode.setWidth("400px");
        courseName.setWidth("400px");
        abbreviation.setWidth("400px");

        this.setWidth("500px");
        //may change later
        addClassName("mailing-list-form");
        add(header,guideline,courseCode,courseName,abbreviation,createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        close.addClickListener(evt -> {
            this.setVisible(false);
        });

        save.addClickListener(evt -> {
            saveCourse();
        });

        return new HorizontalLayout(save,close);
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
            onSave();
            dialog.close();
            update.getUI().ifPresent(ui -> {ui.navigate("");});
            update.getUI().ifPresent(ui -> {ui.navigate("course_abbreviation");});

        });

        cancel.addClickListener(evt -> {
            dialog.close();
        });

    }

    private void onSave() {
        String newAbbrev = abbreviation.getValue();

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            int userId=0;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof MyUserDetails) {
                userId = ((MyUserDetails) principal).getId();
            }
            String sql = "select count(*) as cnt from courseAbbreviation where courseCode = '"+originalCC+"' and userId = "+userId+";";
            ResultSet rst = stmt.executeQuery(sql);
            rst.next();
            int cnt = rst.getInt("cnt");
            rst.close();
            int rs;
            String sqlA = "";
            if(cnt>0){
                sqlA = "update courseAbbreviation set abbrv = '"+newAbbrev+"' where userId = "+userId+" and courseCode = '"+originalCC+"';";
            }else {
                sqlA = "insert into courseAbbreviation (userId,courseCode,abbrv) values("+userId+",'"+originalCC+"','"+newAbbrev+"');";
            }
            rs = stmt.executeUpdate(sqlA);
            if(rs>0)
                Notification.show("Row Successfully Updated.", 2000,Notification.Position.MIDDLE);
            else if(rs == 0)
                Notification.show("Row cannot be updated.", 2000,Notification.Position.MIDDLE);
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage(),2000,Notification.Position.MIDDLE);
        }
    }

    public void setInformation(AbbreviationEntry value) {
        courseCode.setValue(value.getCourseCode());
        courseName.setValue(value.getCourseName());
        abbreviation.setValue(value.getAbbreviation());

        originalCC = value.getCourseCode();
    }
}
