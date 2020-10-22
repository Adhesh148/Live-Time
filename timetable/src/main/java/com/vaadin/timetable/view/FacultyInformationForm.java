package com.vaadin.timetable.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.timetable.backend.FacultyEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class FacultyInformationForm extends VerticalLayout {
    //Pre-requisites for mysql connection
    String url = "jdbc:mysql:/localhost:3306/liveTimetable";
    String user = "dbms";
    String pwd = "Password_123";

    String originalFC = "";
    String OriginalFN = "";

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

        delete.addClickListener(evt -> deleteFaculty());
        
        save.addClickListener(evt -> {
            saveFaculty();
        });

        return new HorizontalLayout(save,delete,close);

    }

    private void saveFaculty() {
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
            if(originalFC.equals(""))
                onADD();
            else
                onSave();
            dialog.close();
            update.getUI().ifPresent(ui -> {ui.navigate("");});
            update.getUI().ifPresent(ui -> {ui.navigate("faculty-view");});

        });

        cancel.addClickListener(evt -> {
            dialog.close();
        });

    }

    private void onADD() {
        String FC = facultyCode.getValue();
        String FN = facultyName.getValue();

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            boolean rs;
            String sql = "Insert into faculty values ('"+FC+"','"+FN+"');";
            rs = stmt.execute(sql);
            Notification.show("Row Successfully Inserted.", 2000,Notification.Position.MIDDLE);
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void onSave() {
        String newFC = facultyCode.getValue();
        String newFN = facultyName.getValue();

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            int rs;
            String sql = "update faculty set facultyName ='"+newFN+"',facultyCode = '"+newFC+"' where facultyCode='"+originalFC+"';";
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

    private void deleteFaculty() {
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
            delete.getUI().ifPresent(ui -> {ui.navigate("faculty-view");});
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
            sql = "delete from faculty where facultyCode = '"+facultyCode.getValue()+"';";
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

    public void setInformation(FacultyEntry value) {
        facultyCode.setValue(value.getFacultyCode());
        facultyName.setValue(value.getFacultyName());

        originalFC = value.getFacultyCode();
        OriginalFN = value.getFacultyName();
    }
}
