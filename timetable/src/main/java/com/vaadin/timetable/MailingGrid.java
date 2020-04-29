package com.vaadin.timetable;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.timetable.backend.CourseEntry;
import com.vaadin.timetable.backend.MailEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

@PageTitle("Mailing List | Timetable")
@Route(value = "mailingList",layout = MainView.class)
public class MailingGrid extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";

    Icon addNew = new Icon(VaadinIcon.PLUS_CIRCLE);
    Button addImport = new Button("Import from file");

    Grid<MailEntry> mailEntryGrid  = new Grid<>(MailEntry.class);
    TextField filterText = new TextField();
    MailingListForm form = new MailingListForm();

    public MailingGrid(){
        setClassName("course-list");
        setSizeFull();

        configureGrid();
        fillMailingGrid();
        configureFilter(filterText);
        Div content = new Div(mailEntryGrid,form);
        content.addClassName("course-content");
        content.setSizeFull();

        form.setVisible(false);

        HorizontalLayout toolBar = new HorizontalLayout(filterText,addNew,addImport);
       toolBar.setAlignSelf(Alignment.CENTER, addNew);

        addNew.addClickListener(evt -> {
            addFaculty();
        });

        addImport.addClickListener(evt-> {
            addFromImport();
        });

        add(toolBar,content);

    }

    private void addFromImport() {
        Dialog dialog = new Dialog();

        H4 header = new H4("Import as csv");
        Label message = new Label("The format of the csv should be the same as the table. Do not include Sno as a field.");
        Label message2 = new Label("(Multiple groups can entered in the same field separated by '-'.)");
        Button cancel = new Button("Cancel");
        Button update = new Button("Insert");
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout dialogButtons = new VerticalLayout(upload,new HorizontalLayout(cancel,update));

        dialog.add(new VerticalLayout(header,message,message2),dialogButtons);
        dialog.open();

        cancel.addClickListener(buttonClickEvent -> {
            dialog.close();
        });

        update.addClickListener(buttonClickEvent -> {
            addOnImport(buffer,dialog);
        });

    }

    private void addOnImport(MemoryBuffer buffer, Dialog dialog) {
        try {
            Scanner inputStream = new Scanner(buffer.getInputStream());
            // ignore the first line - name of columns
            String data = inputStream.next();   // gets whole line
            String[] values = data.split(",");
            if(values[0].equalsIgnoreCase("name") && values[1].equalsIgnoreCase("email") && values[2].equalsIgnoreCase("group"))
            {
                while (inputStream.hasNext()){
                    data = inputStream.next();
                    values = data.split(",");
                    String name = values[0];
                    String email = values[1];
                    String group = values[2];
                    insertIntoTable(name,email,group);
                }
                dialog.close();
                Notification.show("Insertion successful.",2000, Notification.Position.MIDDLE);
                UI.getCurrent().getPage().reload();
            }
            else {
                Notification.show("Invalid Format", 2000, Notification.Position.MIDDLE);
                dialog.close();
            }

            inputStream.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void insertIntoTable(String name, String email, String group) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String groups = group.replaceAll("-",",");
            String sql = "insert into mailingList (name,email,`group`) values ('"+name+"','"+email+"','"+group+"');";
            int rs = stmt.executeUpdate(sql);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void addFaculty() {
        form.setVisible(true);
        form.name.setValue("");
        form.email.setValue("");
        form.group.setValue("");
    }

    private void configureFilter(TextField filterText) {
        filterText.setPlaceholder("Filter by Name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(evt -> updateList());
    }

    private void updateList() {
        String filter = filterText.getValue();
        mailEntryGrid.setItems();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;

            // Get values from backend matching the filter pattern
            sql = "select * from mailingList where name like '%"+filter+"%';";
            rs = stmt.executeQuery(sql);

            Collection<MailEntry> data = new ArrayList<>();
            while(rs.next()){
                MailEntry entry = new MailEntry();
                entry.setSno(rs.getInt("Sno"));
                entry.setName(rs.getString("name"));
                entry.setEmail(rs.getString("email"));
                entry.setGroup(rs.getString("group"));
                data.add(entry);
            }
            mailEntryGrid.setItems(data);


        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void fillMailingGrid() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "select * from mailingList;";
            ResultSet rs = stmt.executeQuery(sql);
            Collection<MailEntry> entry = new ArrayList<>();
            while(rs.next()){
                MailEntry data = new MailEntry();
                data.setSno(rs.getInt("Sno"));
                data.setName(rs.getString("name"));
                data.setEmail(rs.getString("email"));
                data.setGroup(rs.getString("group"));
                entry.add(data);
            }
            rs.close();
            mailEntryGrid.setItems(entry);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

    }

    private void configureGrid() {
        mailEntryGrid.setSortableColumns();

       mailEntryGrid.setColumnOrder(mailEntryGrid.getColumnByKey("sno"),mailEntryGrid.getColumnByKey("name"),
               mailEntryGrid.getColumnByKey("email"),mailEntryGrid.getColumnByKey("group"));

        mailEntryGrid.asSingleSelect().addValueChangeListener(evt -> editForm(evt.getValue()));
    }

    private void editForm(MailEntry value) {
        if(value == null){
            closeEditor();
        }else{
            form.setVisible(true);
            addClassName("course-editing");
            form.setInformation(value);
        }
    }

    private void closeEditor() {
        form.setVisible(false);
        removeClassName("course-editing");
    }

}
