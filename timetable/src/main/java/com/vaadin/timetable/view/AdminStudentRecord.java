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
import com.vaadin.timetable.backend.StudentRecordEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

@PageTitle("Student Record | Timetable")
@Route(value = "student_record",layout = MainView.class)
public class AdminStudentRecord extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/liveTimetable";
    String user = "dbms";
    String pwd = "Password_123";

    Label heading = new Label("Student Records");
    Label message = new Label("Add,Edit and Delete records of Students.");
    Grid<StudentRecordEntry>grid = new Grid<>(StudentRecordEntry.class);
    TextField filterText = new TextField();

    StudentRecordForm form = new StudentRecordForm();
    Icon addNew = new Icon(VaadinIcon.PLUS_CIRCLE);

    public AdminStudentRecord(){
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
        HorizontalLayout toolBar = new HorizontalLayout(filterText,addNew);
        toolBar.setAlignSelf(Alignment.CENTER, addNew);

        addNew.addClickListener(evt -> {
            addStudent();
        });

        add(toolBar,content);
    }

    private void addStudent() {
        form.setVisible(true);
        form.rollNo.setValue("");
        form.loginId.setValue("");
        form.phone.setValue("");
        form.emailId.setValue("");
        form.dob.setValue("");
        form.name.setValue("");
    }

    private void fillGrid() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();

            String sql = "select * from studentRecord;";
            ResultSet rs = stmt.executeQuery(sql);
            Collection<StudentRecordEntry> entry = new ArrayList<>();
            while(rs.next()){
                StudentRecordEntry data = new StudentRecordEntry();
                data.setRollNo(rs.getString("rollNo"));
                data.setLoginId(rs.getInt("loginId"));
                data.setEmailId(rs.getString("emailId"));
                data.setPhone(rs.getString("phone"));
                data.setDob(rs.getString("DOB"));
                data.setName(rs.getString("name"));
                entry.add(data);
            }
            rs.close();
            con.close();
            grid.setItems(entry);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void configureFilter(TextField filterText) {
        filterText.setPlaceholder("Filter by Name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(evt -> updateList());
    }

    private void updateList() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();

            String sql = "select * from studentRecord where name like '%"+filterText.getValue()+"%';";
            ResultSet rs = stmt.executeQuery(sql);
            Collection<StudentRecordEntry> entry = new ArrayList<>();
            while(rs.next()){
                StudentRecordEntry data = new StudentRecordEntry();
                data.setRollNo(rs.getString("rollNo"));
                data.setName(rs.getString("name"));
                data.setLoginId(rs.getInt("loginId"));
                data.setEmailId(rs.getString("emailId"));
                data.setPhone(rs.getString("phone"));
                data.setDob(rs.getString("DOB"));
                entry.add(data);
            }
            rs.close();
            grid.setItems(entry);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

    }

    private void configureGrid(Grid<StudentRecordEntry> grid) {
        //grid.setSizeFull();
        grid.setSortableColumns();
        grid.setHeightByRows(true);

        grid.setColumnOrder(grid.getColumnByKey("rollNo"),grid.getColumnByKey("name"),grid.getColumnByKey("loginId"),grid.getColumnByKey("phone"),grid.getColumnByKey("emailId"),grid.getColumnByKey("dob"));
        grid.asSingleSelect().addValueChangeListener(evt -> editForm(evt.getValue()));
    }

    private void editForm(StudentRecordEntry value) {
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
