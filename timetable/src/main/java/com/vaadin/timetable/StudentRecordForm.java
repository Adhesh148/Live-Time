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
import com.vaadin.timetable.backend.StudentRecordEntry;

import java.sql.*;

import static com.vaadin.flow.component.Key.FN;

public class StudentRecordForm extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";

    String originalRollNo = "";

    H2 header = new H2("Edit Information");
    Label guideline = new Label("* Double-click on the field to edit.");

    TextField rollNo = new TextField("Roll No");
    TextField name = new TextField("Name");
    TextField loginId = new TextField("Login ID");
    TextField phone = new TextField("Mobile No");
    TextField emailId = new TextField("Email ID");
    TextField dob = new TextField("DOB");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public StudentRecordForm(){
        rollNo.setWidth("400px");
        loginId.setWidth("400px");
        phone.setWidth("400px");
        emailId.setWidth("400px");
        dob.setWidth("400px");
        name.setWidth("400px");

        this.setWidth("500px");
        //may change later
        addClassName("mailing-list-form");
        add(header,guideline,rollNo,name,loginId,phone,emailId,dob,createButtonsLayout());


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

        delete.addClickListener(evt -> deleteStudent());

        save.addClickListener(evt -> {
            saveStudent();
        });

        return new HorizontalLayout(save,delete,close);
    }

    private void saveStudent() {
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
            if(!rollNo.getValue().equalsIgnoreCase("")){
                if(originalRollNo.equals("")){
                    onADD();
                }else{
                    onSave();
                }
                dialog.close();
                update.getUI().ifPresent(ui -> {ui.navigate("");});
                update.getUI().ifPresent(ui -> {ui.navigate("student_record");});
            }else{
                Notification.show("Roll No is a necessary field.",2000, Notification.Position.MIDDLE);
            }
        });

        cancel.addClickListener(evt -> {
            dialog.close();
        });

    }

    private void onADD() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            String sql = "insert into studentRecord(rollNo,loginId,phone,emailId,DOB,name) values(?,?,?,?,?,?);";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,rollNo.getValue());
            if(loginId.isEmpty() == true){
                stmt.setNull(2, Types.INTEGER);
            }else{
                stmt.setInt(2, Integer.parseInt(loginId.getValue()));
            }

            if(phone.isEmpty() == true){
                stmt.setNull(3,Types.VARCHAR);
            }else{
                stmt.setString(3,loginId.getValue());
            }

            if(emailId.isEmpty() == true){
                stmt.setNull(4,Types.VARCHAR);
            }else{
                stmt.setString(4,emailId.getValue());
            }

            if(dob.isEmpty() == true){
                stmt.setNull(5,Types.VARCHAR);
            }else{
                stmt.setString(5,dob.getValue());
            }

            if(name.isEmpty() == true){
                stmt.setNull(6,Types.VARCHAR);
            }else{
                stmt.setString(6,name.getValue());
            }
            int rs;
            rs = stmt.executeUpdate();
            if(rs>0)
                Notification.show("Row Successfully Inserted.", 2000,Notification.Position.MIDDLE);
            else
                Notification.show("Insertion Unsuccessful.", 2000,Notification.Position.MIDDLE);
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void onSave() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            String sql = "update studentRecord set loginId = ?,phone = ?,emailId = ?,DOB = ?,name = ? where rollNo = '"+rollNo.getValue()+"';";
            PreparedStatement stmt = con.prepareStatement(sql);
            if(loginId.isEmpty() == true){
                stmt.setNull(1, Types.INTEGER);
            }else{
                stmt.setInt(1, Integer.parseInt(loginId.getValue()));
            }

            if(phone.isEmpty() == true){
                stmt.setNull(2,Types.VARCHAR);
            }else{
                stmt.setString(2,phone.getValue());
            }

            if(emailId.isEmpty() == true){
                stmt.setNull(3,Types.VARCHAR);
            }else{
                stmt.setString(3,emailId.getValue());
            }

            if(dob.isEmpty() == true){
                stmt.setNull(4,Types.VARCHAR);
            }else{
                stmt.setString(4,dob.getValue());
            }

            if(name.isEmpty() == true){
                stmt.setNull(5,Types.VARCHAR);
            }else{
                stmt.setString(5,name.getValue());
            }
            int rs;
            rs = stmt.executeUpdate();
            if(rs>0)
                Notification.show("Row Successfully Updated.", 2000,Notification.Position.MIDDLE);
            else if(rs == 0)
                Notification.show("Row cannot be updated.", 2000,Notification.Position.MIDDLE);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage(),2000,Notification.Position.MIDDLE);
        }
    }

    public void setInformation(StudentRecordEntry value) {
        rollNo.setValue(value.getRollNo());
        phone.setValue("");
        emailId.setValue("");
        loginId.setValue("");
        dob.setValue("");
        name.setValue("");
        if(value.getEmailId() != null)
            emailId.setValue(value.getEmailId());
        if(value.getDob() != null)
            dob.setValue(value.getDob());
        if(value.getPhone() != null)
            phone.setValue(value.getPhone());
        if(value.getName() != null)
            name.setValue(value.getName());
        if(value.getLoginId() != 0)
            loginId.setValue(String.valueOf(value.getLoginId()));
       originalRollNo = value.getRollNo();
    }

    private void deleteStudent() {
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
            delete.getUI().ifPresent(ui -> {ui.navigate("student_record");});
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
            sql = "delete from studentRecord where rollNo = '"+rollNo.getValue()+"';";
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
}
