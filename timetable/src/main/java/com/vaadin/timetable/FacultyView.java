package com.vaadin.timetable;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.timetable.backend.FacultyEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


@Route(value = "faculty-view",layout = MainView.class)
@PageTitle("Faculty View | Live Timetable")
public class FacultyView extends VerticalLayout {

    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";
    Grid<FacultyEntry> grid = new Grid<>(FacultyEntry.class);
    TextField filterText = new TextField();
    FacultyInformationForm form = new FacultyInformationForm();

    Label heading = new Label("Faculty Records");
    Label message = new Label("Add,Edit and Delete records of Faculty.");

    Icon addNew = new Icon(VaadinIcon.PLUS_CIRCLE);

    public FacultyView(){
        setClassName("faculty-list");
        setSizeFull();

        configureGrid(grid);
        fillFacultyGrid();

        configureFilter(filterText);
        Div content = new Div(grid,form);
        content.addClassName("faculty-content");
        content.setSizeFull();
        //set form to not be visible unless grid is clicked
        form.setVisible(false);

        HorizontalLayout toolBar = new HorizontalLayout(filterText,addNew);
        toolBar.setAlignSelf(Alignment.CENTER, addNew);

        addNew.addClickListener(evt -> {
            addFaculty();
        });

        heading.addClassName("course-abbreviation-heading");
        message.addClassName("course-abbreviation-message");
        add(heading,message,new Hr());
        add(toolBar,content);

    }

    private void addFaculty() {
        form.setVisible(true);
        form.facultyCode.setValue("");
        form.facultyName.setValue("");
    }

    private void configureFilter(TextField filterText) {
        filterText.setPlaceholder("Filter by faculty name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(evt -> updateList());
    }

    private void updateList() {
        String filter = filterText.getValue();
        grid.setItems();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;

            // Get values from backend matching the filter pattern
            sql = "select facultyCode,facultyName from faculty where facultyName like '%"+filter+"%';";
            rs = stmt.executeQuery(sql);

            Collection<FacultyEntry> data = new ArrayList<FacultyEntry>();
            while(rs.next()){
                FacultyEntry entry = new FacultyEntry();
                entry.setFacultyCode(rs.getString("facultyCode"));
                entry.setFacultyName(rs.getString("facultyName"));
                data.add(entry);
            }
            grid.setItems(data);

        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    public void fillFacultyGrid() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;

            sql = "select facultyCode, facultyName from faculty";
            rs = stmt.executeQuery(sql);

            Collection<FacultyEntry> data = new ArrayList<FacultyEntry>();

            while(rs.next()){
                FacultyEntry entry = new FacultyEntry();
                entry.setFacultyCode(rs.getString("facultyCode"));
                entry.setFacultyName(rs.getString("facultyName"));
                data.add(entry);
            }
            grid.setItems(data);

        }catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }

    }

    private void configureGrid(Grid<FacultyEntry> grid) {
        grid.getColumnByKey("facultyCode").setHeader("Faculty Code");
        grid.getColumnByKey("facultyName").setHeader("Faculty Name");

        grid.setSizeFull();

        grid.getColumns().forEach(col -> col.setWidth("300px"));
        grid.setSortableColumns();

        //Add Value Change Listener to display the form
        grid.asSingleSelect().addValueChangeListener(evt -> editForm(evt.getValue()));

    }

    private void editForm(FacultyEntry value) {
        if(value == null){
            closeEditor();
        }else{
            form.setVisible(true);
            addClassName("faculty-editing");
            form.setInformation(value);
        }
    }

    private void closeEditor() {
        form.setVisible(false);
        removeClassName("faculty-editing");
    }


}
