package com.vaadin.timetable;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

@PageTitle("ViewProject | Timetable")
@Route(value = "project",layout = MainView.class)
public class ProjectView extends VerticalLayout {
    Button create = new Button("Create", VaadinIcon.PLUS.create());
    Button delete = new Button("Delete",VaadinIcon.MINUS.create());

    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";


    public ProjectView(){
        create.addClassName("project-create");
        delete.addClassName("project-delete");
        create.addClickListener(buttonClickEvent -> {
            addNewProject();
        });
        add(new HorizontalLayout(create,delete));

        updateProjectPanels();
    }

    private void updateProjectPanels() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql = "select postedDate,facultyCode,title,description,batchCode,marks,dueDate,dueTime from projectAssign;";
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                String postedDate = rs.getString("postedDate");
                String facultyCode = rs.getString("facultyCode");
                String title = rs.getString("title");
                String desc = rs.getString("description");
                String batch = rs.getString("batchCode");
                int marks = rs.getInt("marks");
                String dueDate = rs.getString("dueDate");
                String dueTime = rs.getString("dueTime");
                ProjectPanel panel = new ProjectPanel(postedDate,facultyCode,title,desc,batch,marks,dueDate,dueTime);
                add(panel);
            }
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void addNewProject() {
        Dialog project = new Dialog();

        SplitLayout outer_layout = new SplitLayout();
        SplitLayout inner_layout = new SplitLayout();
        inner_layout.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        outer_layout.setOrientation(SplitLayout.Orientation.VERTICAL);

        project.setHeight("calc(90vh - (2*var(--lumo-space-m)))");
        project.setWidth("calc(100vw - (4*var(--lumo-space-m)))");

        //Primary of inner layout
        Label heading = new Label("Add A Project");
        heading.addClassName("project-add-heading");

        //Secondary split of outer layout
        ComboBox facultyCode = new ComboBox("Faculty Code");
        ComboBox facultyName = new ComboBox("Faculty Name");
        ComboBox courseCode  = new ComboBox("Course Code");
        ComboBox courseName  = new ComboBox("Course Name");
        ComboBox batch = new ComboBox("Batch");
        TextField marks = new TextField("Marks");
        DatePicker deadlineDate = new DatePicker("Due");
        deadlineDate.setPlaceholder("Pick a date");
        TimePicker deadlineTime = new TimePicker();
        //TextField deadlineTime = new TextField();
        deadlineTime.setPlaceholder("HH:MM:SS");
        TextField topic = new TextField("Topic");
        TextField teamSize = new TextField("Team Size");

        VerticalLayout outerSecondaryLayout = new VerticalLayout();
        HorizontalLayout Due = new HorizontalLayout();
        Due.add(deadlineDate,deadlineTime);
        Due.setAlignSelf(Alignment.END, deadlineTime);
        outerSecondaryLayout.add(new HorizontalLayout(facultyCode,facultyName),new HorizontalLayout(courseCode,courseName),batch,marks,Due,topic,teamSize);

        // Secondary of inner layout
        TextField title = new TextField("Title");
        title.setPlaceholder("Enter a title");
        TextArea desc = new TextArea("Description");
        desc.setPlaceholder("Enter description. (Optional)");
        teamSize.setPlaceholder("Enter Size of Team");
        Button assign = new Button("Assign",VaadinIcon.PLUS.create());

        VerticalLayout innerSecondaryLayout = new VerticalLayout();
        innerSecondaryLayout.add(title,desc,assign);
        inner_layout.addToPrimary(innerSecondaryLayout);
        inner_layout.addToSecondary(outerSecondaryLayout);
        outer_layout.addToPrimary(heading);
        outer_layout.addToSecondary(inner_layout);

        // Have to put media queries for devices.....
        title.setWidth("95%");
        desc.setWidth("95%");
        desc.setHeight("200px");
        facultyCode.setWidth("170px");
        facultyName.setWidth("400px");
        courseCode.setWidth("170px");
        courseName.setWidth("400px");
        topic.setWidth("400px");
        inner_layout.setPrimaryStyle("min-width", "65%");
        inner_layout.setPrimaryStyle("max-width", "65%");
        inner_layout.setSecondaryStyle("min-width", "35%");
        inner_layout.setSecondaryStyle("max-width", "35%");
        inner_layout.setSecondaryStyle("min-height", "100%");
        inner_layout.setSecondaryStyle("max-height", "100%");
        outer_layout.setPrimaryStyle("min-height","5%");
        outer_layout.setPrimaryStyle("max-height","5%");
        outer_layout.setSecondaryStyle("min-height","95%");
        outer_layout.setSecondaryStyle("max-height","95%");
        inner_layout.setSizeUndefined();
        outer_layout.setSizeFull();
        // ------------------------------------------------------------------------

        // Update all Combo Boxes
        updateFacultyCodeCombo(facultyCode);
        updateFacultyNameCombo(facultyName);
        updateCourseCodeCombo(courseCode);
        updateCourseNameCombo(courseName);
        updateBatchCombo(batch);
//
//        // Add ValueChange Listeners
        facultyCode.addValueChangeListener(evt -> {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,pwd);
                Statement stmt = con.createStatement();
                ResultSet rs;
                String sql = "select facultyName from faculty where facultyCode = '"+evt.getValue()+"';";
                rs = stmt.executeQuery(sql);
                rs.next();
                facultyName.setValue(rs.getString("facultyName"));
            }catch (Exception e){
                Notification.show(e.getLocalizedMessage());
            }
        });
//
        facultyName.addValueChangeListener(evt -> {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,pwd);
                Statement stmt = con.createStatement();
                ResultSet rs;
                String sql = "select facultyCode from faculty where facultyName = '"+evt.getValue()+"';";
                rs = stmt.executeQuery(sql);
                rs.next();
                facultyCode.setValue(rs.getString("facultyCode"));
            }catch (Exception e){
                Notification.show(e.getLocalizedMessage());
            }
        });
//
        courseCode.addValueChangeListener(evt -> {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,pwd);
                Statement stmt = con.createStatement();
                ResultSet rs;
                String sql = "select courseName from course where courseCode = '"+evt.getValue()+"';";
                rs = stmt.executeQuery(sql);
                rs.next();
                courseName.setValue(rs.getString("courseName"));
            }catch (Exception e){
                Notification.show(e.getLocalizedMessage());
            }
        });

        courseName.addValueChangeListener(evt -> {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,pwd);
                Statement stmt = con.createStatement();
                ResultSet rs;
                String sql = "select courseCode from course where courseName = '"+evt.getValue()+"';";
                rs = stmt.executeQuery(sql);
                rs.next();
                courseCode.setValue(rs.getString("courseCode"));
            }catch (Exception e){
                Notification.show(e.getLocalizedMessage());
            }
        });
        LocalDate today = LocalDate.now();
        // Setting min deadline as next day and max deadline as 6 months from today .. should decide if to change
        deadlineDate.setMin(today);
        deadlineDate.setMax(today.plusMonths(6));
        //Add Functionality to AssignButton
        assign.addClickListener(buttonClickEvent -> {
            insertNewProject(title,desc,facultyCode,courseCode,batch,marks,deadlineDate,deadlineTime,topic,teamSize);
            project.close();
        });


        project.add(outer_layout);
        project.open();

    }

    private void insertNewProject(TextField title, TextArea desc, ComboBox facultyCode, ComboBox courseCode, ComboBox batch, TextField marks, DatePicker deadlineDate, TimePicker deadlineTime, TextField topic, TextField teamSize) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String postDate = sdf.format(cal.getTime());
            String sql = "insert into projectAssign (postedDate,facultyCode,title,description,batchCode,marks,dueDate,dueTime,topic) values('"+postDate+"','"+facultyCode.getValue()+"','"+title.getValue()+"','"+desc.getValue()+"','"+batch.getValue()+"',"+marks.getValue()+",'"+deadlineDate.getValue()+"','"+deadlineTime.getValue()+"','"+topic.getValue()+"');";
            int rs;
            rs = stmt.executeUpdate(sql);
            if(rs>0)
                Notification.show("Successfully Inserted",2000, Notification.Position.MIDDLE);
            else
                Notification.show("Insertion unsuccessful.",2000, Notification.Position.MIDDLE);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void updateBatchCombo(ComboBox batch) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql = "select batchCode,year from batch;";
            rs = stmt.executeQuery(sql);
            Collection<Object> items = new ArrayList<>();
            while (rs.next()){
                String code = rs.getString("batchCode");
                Calendar cal = Calendar.getInstance();
                cal.setTime(rs.getDate("year"));
                Object entry = code+" "+cal.get(Calendar.YEAR);
                items.add(entry);
            }
            batch.setItems(items);

        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void updateCourseNameCombo(ComboBox courseName) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;
            sql = "Select courseName from course;";
            rs = stmt.executeQuery(sql);
            Collection<Object> items = new ArrayList<>();
            while(rs.next()){
                Object entry = rs.getString("courseName");
                items.add(entry);
            }
            courseName.setItems(items);
        }catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void updateCourseCodeCombo(ComboBox courseCode) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;
            sql = "Select courseCode from course;";
            rs = stmt.executeQuery(sql);
            Collection<Object> items = new ArrayList<>();
            while(rs.next()){
                Object entry = rs.getString("courseCode");
                items.add(entry);
            }
            courseCode.setItems(items);
        }catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void updateFacultyNameCombo(ComboBox facultyName) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;
            sql = "Select facultyName from faculty;";
            rs = stmt.executeQuery(sql);
            Collection<Object> items = new ArrayList<>();
            while(rs.next()){
                Object entry = rs.getString("facultyName");
                items.add(entry);
            }
            facultyName.setItems(items);
        }catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void updateFacultyCodeCombo(ComboBox facultyCode) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;
            sql = "Select facultyCode from faculty;";
            rs = stmt.executeQuery(sql);
            Collection<Object> items = new ArrayList<>();
            while(rs.next()){
                Object entry = rs.getString("facultyCode");
                items.add(entry);
            }
            facultyCode.setItems(items);
        }catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }
}
