package com.vaadin.timetable;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class MailingDialog extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";

    public  MailingDialog(){
        Dialog dialog = new Dialog();
        H2 header = new H2("Notify");
        Label message = new Label("Select the groups you wish to notify your message.");
        Label message2 = new Label("(Multiple groups can selected.)");
        TextArea personal = new TextArea("Enter Email Ids");
        personal.setPlaceholder("Enter Email Ids not included in any group");
        personal.setWidth("400px");
        Button cancel = new Button("Cancel");
        Button update = new Button("Notify");

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // create a new List Box
        MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
        ArrayList<String> groups = getAllGroups();
        List<String> unique = new ArrayList<String>(new HashSet<String>(groups));
        listBox.setHeight("60px");
        listBox.setWidthFull();
        listBox.setItems(unique);

        dialog.add(new VerticalLayout(header,message,message2,listBox,personal,new HorizontalLayout(cancel,update)));

        cancel.addClickListener(buttonClickEvent -> {
            dialog.close();
        });

        dialog.setSizeFull();
        dialog.open();

    }

    private ArrayList<String> getAllGroups() {
        ArrayList<String> items = new ArrayList<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "select distinct `group` from mailingList;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                String input[] = rs.getString("group").split(",");
                for(int i=0;i<input.length;++i){
                    items.add(input[i]);
                }
            }

        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

        return items;

    }
}
