package com.vaadin.timetable;

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
import com.vaadin.timetable.backend.BatchEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.Year;



public class BatchInformationForm extends VerticalLayout {
    public int flag =0;
    //Pre-requisites for mysql connection
    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";

    int originalBNo;
    String originalBC ="";
    String originalBN ="";
    String originalYear ="";
    String originalSpec = "";

    H2 header = new H2("Edit Information");
    Label guideline = new Label("* Double-click on the field to edit.");
    TextField batchNo = new TextField("Batch Number");
    TextField batchCode = new TextField("Batch Code");
    TextField batchName = new TextField("Batch Name");
    TextField year = new TextField("Year");
    TextField spec = new TextField("Specialization");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public BatchInformationForm(){
        batchNo.setWidth("400px");
        batchCode.setWidth("400px");
        batchName.setWidth("400px");
        year.setWidth("400px");
        spec.setWidth("400px");

        this.setWidth("500px");
        addClassName("batch-info-form");

        add(header,guideline,batchNo,batchCode,batchName,year,spec,createButtonsLayout());

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

        delete.addClickListener(evt -> deleteBatch());

        save.addClickListener(evt -> {
            saveBatch();
        });

        return new HorizontalLayout(save,delete,close);
    }

    private void saveBatch() {
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
            if(flag == 0) {
                onADD();
            }else{
                onSave();
            }
            dialog.close();
            update.getUI().ifPresent(ui -> {ui.navigate("");});
            update.getUI().ifPresent(ui -> {ui.navigate("batch-view");});

        });
        cancel.addClickListener(evt -> {
            dialog.close();
        });
    }

    private void onADD() {
        int BNo = Integer.parseInt(batchNo.getValue());
        String BC = batchCode.getValue();
        String BN = batchName.getValue();
        String Year = year.getValue();
        String Spec = spec.getValue();


        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            boolean rs;
            String sql = "Insert into batch values ("+BNo+",'"+BC+"','"+BN+"','"+Year+"','"+Spec+"');";
            rs = stmt.execute(sql);
            Notification.show("Row Successfully Inserted.", 2000,Notification.Position.MIDDLE);

        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void onSave() {
        int newBNo = Integer.parseInt(batchNo.getValue());
        String newBC = batchCode.getValue();
        String newBN = batchName.getValue();
        String newYear = year.getValue();
        String newSpec = spec.getValue();

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            int rs;
            String sql = "update batch set batchNo ='"+newBNo+"',batchCode = '"+newBC+"',batchName = '"+newBN+"',year = '"+newYear+"',specialization ='"+newSpec+"' where batchNo="+Integer.parseInt(batchNo.getValue())+";";
            rs = stmt.executeUpdate(sql);
            if(rs>0)
                Notification.show("Row Successfully Updated.", 2000,Notification.Position.MIDDLE);
            else if(rs == 0)
                Notification.show("Row cannot be updated.", 2000,Notification.Position.MIDDLE);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage(),2000,Notification.Position.MIDDLE);
        }
    }

    private void deleteBatch() {
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
            delete.getUI().ifPresent(ui -> {ui.navigate("batch-view");});
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
            sql = "delete from batch where batchNo = "+Integer.parseInt(batchNo.getValue())+";";
            rs = stmt.executeUpdate(sql);
            if(rs > 0)
                Notification.show("Row Successfully Deleted.", 2000,Notification.Position.MIDDLE);
            else
                Notification.show("Unsuccessful.", 2000,Notification.Position.MIDDLE);
            this.setVisible(false);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    public void setInformation(BatchEntry value,int cnt) {
        flag =0;
        if(cnt!=0)
            ++flag;
        batchNo.setValue(String.valueOf(value.getBatchNo()));
        batchCode.setValue(value.getBatchCode());
        batchName.setValue(value.getBatchName());
        year.setValue(value.getYear());
        spec.setValue(value.getSpecialization());

        originalBNo = Integer.parseInt(batchNo.getValue());
        originalBC = batchCode.getValue();
        originalBN = batchName.getValue();
        originalYear = year.getValue();
        originalSpec = spec.getValue();
    }

}
