package com.vaadin.timetable;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.timetable.backend.AbbreviationEntry;
import com.vaadin.timetable.backend.MailEntry;
import com.vaadin.timetable.security.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

@PageTitle("Course Abbreviation | Timetable")
@Route(value = "course_abbreviation",layout = MainView.class)

public class StudentAbbreviation extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";

    Label heading = new Label("Course Abbreviation");
    Label message = new Label("Personalize your live timetable by setting your own course abbreviations.");
    Grid<AbbreviationEntry>grid = new Grid<>(AbbreviationEntry.class);
    TextField filterText = new TextField();

    AbbreviationForm form = new AbbreviationForm();

    public StudentAbbreviation(){
        // may change later
        setClassName("course-list");
        setSizeFull();

        configureGrid(grid);
        configureFilter(filterText);

        fillGrid();

        Div content = new Div(grid,form);
        content.addClassName("course-content");
        content.setSizeFull();

        form.setVisible(false);

        heading.addClassName("course-abbreviation-heading");
        message.addClassName("course-abbreviation-message");
        add(heading,message,new Hr());
        add(filterText,content);
    }

    private void fillGrid() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            int userId=0,batchNo=0;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof MyUserDetails) {
                userId = ((MyUserDetails) principal).getId();
                batchNo = ((MyUserDetails) principal).getBatchNo();
            }
            String sql = "select (@n:=@n+1) as sno, courseCode,courseName,abbrv from \n" +
                    "(select distinct w.courseCode,p.courseName,IFNULL(a.abbrv,'') as abbrv from weekTimetable w\n" +
                    "inner join course p\n" +
                    "on p.courseCode = w.courseCode\n" +
                    "left join courseAbbreviation a\n" +
                    "on a.courseCode = p.courseCode and a.userId = "+userId+"\n" +
                    "where batchNo = "+batchNo+") t1\n" +
                    "join (select @n:=0) n;";

            ResultSet rs = stmt.executeQuery(sql);
            Collection<AbbreviationEntry> entry = new ArrayList<>();
            while(rs.next()){
                AbbreviationEntry data = new AbbreviationEntry();
                data.setSno(rs.getInt("sno"));
                data.setCourseCode(rs.getString("courseCode"));
                data.setCourseName(rs.getString("courseName"));
                data.setAbbreviation(rs.getString("abbrv"));
                entry.add(data);
            }
            rs.close();
            grid.setItems(entry);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void configureFilter(TextField filterText) {
        filterText.setPlaceholder("Filter by course name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(evt -> updateList());
    }

    private void updateList() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            int userId=0,batchNo=0;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof MyUserDetails) {
                userId = ((MyUserDetails) principal).getId();
                batchNo = ((MyUserDetails) principal).getBatchNo();
            }
            String sql = "select (@n:=@n+1) as sno, courseCode,courseName,abbrv from \n" +
                    "(select distinct w.courseCode,p.courseName,IFNULL(a.abbrv,'') as abbrv from weekTimetable w\n" +
                    "inner join course p\n" +
                    "on p.courseCode = w.courseCode\n" +
                    "left join courseAbbreviation a\n" +
                    "on a.courseCode = p.courseCode and a.userId = "+userId+"\n" +
                    "where batchNo = "+batchNo+" and courseName like '%"+filterText.getValue()+"%') t1\n" +
                    "join (select @n:=0) n;";

            ResultSet rs = stmt.executeQuery(sql);
            Collection<AbbreviationEntry> entry = new ArrayList<>();
            while(rs.next()){
                AbbreviationEntry data = new AbbreviationEntry();
                data.setSno(rs.getInt("sno"));
                data.setCourseCode(rs.getString("courseCode"));
                data.setCourseName(rs.getString("courseName"));
                data.setAbbreviation(rs.getString("abbrv"));
                entry.add(data);
            }
            rs.close();
            grid.setItems(entry);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

    }

    private void configureGrid(Grid<AbbreviationEntry> grid) {
        //grid.setSizeFull();
        grid.setSortableColumns();
        grid.getColumnByKey("sno").setWidth("30px");
        grid.getColumnByKey("courseCode").setWidth("40px");
        grid.getColumnByKey("abbreviation").setWidth("50px");

        grid.setHeightByRows(true);

        grid.setColumnOrder(grid.getColumnByKey("sno"),grid.getColumnByKey("courseCode"),grid.getColumnByKey("courseName"),grid.getColumnByKey("abbreviation"));
        grid.asSingleSelect().addValueChangeListener(evt -> editForm(evt.getValue()));
    }

    private void editForm(AbbreviationEntry value) {
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
