package com.vaadin.timetable.view;

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
import com.vaadin.timetable.backend.CourseEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

@Route(value = "course-view",layout = MainView.class)
@PageTitle("Course View | Live Timetable")
public class CourseView extends VerticalLayout {
    String url = "jdbc:mysql://aauorfmbt136d0.cuz1bxluuufz.ap-south-1.rds.amazonaws.com:3306/liveTimetable";
    String user = "dbms";
    String pwd = "Password_123";

    Grid<CourseEntry> grid = new Grid<>(CourseEntry.class);
    TextField filterText = new TextField();
    CourseInformationForm form = new CourseInformationForm();

    Icon addNew = new Icon(VaadinIcon.PLUS_CIRCLE);

    Label heading = new Label("Course Records");
    Label message = new Label("Add,Edit and Delete records of Courses.");

    public CourseView(){
        setClassName("course-list");
        setSizeFull();

        configureGrid(grid);
        fillCourseGrid();

        configureFilter(filterText);
        Div content = new Div(grid,form);
        content.addClassName("course-content");
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
        form.courseCode.setValue("");
        form.courseName.setValue("");
    }

    private void configureFilter(TextField filterText) {
        filterText.setPlaceholder("Filter by course name");
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
            sql = "select courseCode,courseName from course where courseName like '%"+filter+"%';";
            rs = stmt.executeQuery(sql);

            Collection<CourseEntry> data = new ArrayList<>();
            while(rs.next()){
                CourseEntry entry = new CourseEntry();
                entry.setCourseCode(rs.getString("courseCode"));
                entry.setCourseName(rs.getString("courseName"));
                data.add(entry);
            }
            grid.setItems(data);
            rs.close();
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    public void fillCourseGrid() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;

            sql = "select courseCode, courseName from course";
            rs = stmt.executeQuery(sql);

            Collection<CourseEntry> data = new ArrayList<>();

            while(rs.next()){
                CourseEntry entry = new CourseEntry();
                entry.setCourseCode(rs.getString("courseCode"));
                entry.setCourseName(rs.getString("courseName"));
                data.add(entry);
            }
            grid.setItems(data);

        }catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }

    }

    private void configureGrid(Grid<CourseEntry> grid) {
        grid.getColumnByKey("courseCode").setHeader("Course Code");
        grid.getColumnByKey("courseName").setHeader("Course Name");

        //grid.setSizeFull();
        grid.setHeightByRows(true);

        grid.getColumns().forEach(col -> col.setWidth("300px"));
        grid.setSortableColumns();

        //Add Value Change Listener to display the form
        grid.asSingleSelect().addValueChangeListener(evt -> editForm(evt.getValue()));

    }

    private void editForm(CourseEntry value) {
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
