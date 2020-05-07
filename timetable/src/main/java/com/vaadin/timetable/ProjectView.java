package com.vaadin.timetable;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.annotations.Check;

import javax.faces.context.FacesContext;
import java.io.File;

import java.io.FileInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

@PageTitle("ViewProject | Timetable")
@Route(value = "project",layout = MainView.class)
public class ProjectView extends VerticalLayout {
    Button create = new Button("Create", VaadinIcon.PLUS.create());

    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";
    LocalDate today = LocalDate.now();
    int dayOfWeek = today.getDayOfWeek().getValue();
    LocalDate Sun = today.plusDays(-1*dayOfWeek);
    LocalDate Sat = Sun.plusDays(6);

    public ProjectView(){
        create.addClassName("project-create");
        create.addClickListener(buttonClickEvent -> {
            addNewProject();
        });
        Label heading  = new Label("Assignment Manager");
        heading.addClassName("project-view-heading");
        add(new HorizontalLayout(heading,create));

        updateProjectPanels();
    }

    private void updateProjectPanels() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql = "select postedDate,courseCode,facultyCode,title,description,batchNo,marks,dueDate,dueTime,teamSize,topic from projectAssign where postedDate>='"+Sun+"' and postedDate<='"+Sat+"' order by postedDate desc;";
            rs = stmt.executeQuery(sql);
            Label subhead = new Label("Posted this Week");
            subhead.addClassName("project-division-headings");
            add(new VerticalLayout(subhead,new Hr()));
            while(rs.next()){
                String courseCode = rs.getString("courseCode");
                String postedDate = rs.getString("postedDate");
                String facultyCode = rs.getString("facultyCode");
                String title = rs.getString("title");
                String desc = rs.getString("description");
                String batchNo = String.valueOf(rs.getInt("batchNo"));
                int marks = rs.getInt("marks");
                String dueDate = rs.getString("dueDate");
                String dueTime = rs.getString("dueTime");
                int teamSize = rs.getInt("teamSize");
                String topic = rs.getString("topic");
                ProjectPanel panel = new ProjectPanel(postedDate,courseCode,facultyCode,title,desc,batchNo,marks,dueDate,dueTime,teamSize,topic);
                add(panel);
            }
            rs.close();
            Label subhead_older = new Label("Older Posts");
            subhead_older.addClassName("project-division-headings");
            add(new VerticalLayout(subhead_older,new Hr()));
            sql = "select postedDate,courseCode,facultyCode,title,description,batchNo,marks,dueDate,dueTime,teamSize,topic from projectAssign where postedDate <'"+Sun+"' order by postedDate desc;";
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                String courseCode = rs.getString("courseCode");
                String postedDate = rs.getString("postedDate");
                String facultyCode = rs.getString("facultyCode");
                String title = rs.getString("title");
                String desc = rs.getString("description");
                String batchNo = String.valueOf(rs.getInt("batchNo"));
                int marks = rs.getInt("marks");
                String dueDate = rs.getString("dueDate");
                String dueTime = rs.getString("dueTime");
                int teamSize = rs.getInt("teamSize");
                String topic = rs.getString("topic");
                ProjectPanel panel = new ProjectPanel(postedDate,courseCode,facultyCode,title,desc,batchNo,marks,dueDate,dueTime,teamSize,topic);
                add(panel);
            }
            rs.close();
            con.close();
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
        // Add Upload Button------------------------
        // Simple in memory single file upload.
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        //upload.setMaxFileSize();
        //--------------------------------------

        //-------Set as required-----------
        title.setRequired(true);
        title.setRequiredIndicatorVisible(true);
        desc.setRequired(true);
        desc.setRequiredIndicatorVisible(true);
        facultyCode.setRequired(true);
        facultyCode.setRequiredIndicatorVisible(true);
        courseCode.setRequired(true);
        courseCode.setRequiredIndicatorVisible(true);
        batch.setRequired(true);
        batch.setRequiredIndicatorVisible(true);
        deadlineDate.setRequired(true);
        deadlineTime.setRequired(true);
        marks.setRequired(true);
        marks.setRequiredIndicatorVisible(true);
        topic.setRequired(true);
        topic.setRequiredIndicatorVisible(true);


        VerticalLayout innerSecondaryLayout = new VerticalLayout();
        innerSecondaryLayout.add(title,desc,upload,assign);
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
        // Check if all essential fields are filled.
        int flag =1;

        int finalFlag = flag;
        assign.addClickListener(buttonClickEvent -> {
            if(title.getValue().equalsIgnoreCase("") || desc.getValue().equalsIgnoreCase("") || facultyCode.isEmpty() == true || courseCode.isEmpty() == true ||
                    batch.isEmpty() == true || marks.getValue().equalsIgnoreCase("") || deadlineDate.isEmpty() == true || deadlineDate.isEmpty()==true || topic.getValue().equalsIgnoreCase("")){
               Notification.show("Empty Fields",2000, Notification.Position.MIDDLE);
            }else{
                insertNewProject(title, desc, facultyCode, courseCode, batch, marks, deadlineDate, deadlineTime, topic, teamSize, upload, buffer);
                project.close();
            }
        });


        project.add(outer_layout);
        project.open();

    }

    private void insertNewProject(TextField title, TextArea desc, ComboBox facultyCode, ComboBox courseCode, ComboBox batch, TextField marks, DatePicker deadlineDate, TimePicker deadlineTime, TextField topic, TextField teamSize, Upload upload, MemoryBuffer buffer) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();

            // Let us extract batchNo from batch
            String batchInfo[] = String.valueOf(batch.getValue()).split(" ");
            String sql1 = "select batchNo from batch where batchCode = '"+batchInfo[0]+"' and year = '"+batchInfo[1]+"';";
            ResultSet rst = stmt.executeQuery(sql1);
            rst.next();
            int batchNo = rst.getInt("batchNo");
            rst.close();

            //Insert into projectAssign table
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String postDate = sdf.format(cal.getTime());
            String sql;
            if(!teamSize.getValue().equalsIgnoreCase("")){
               sql  = "insert into projectAssign (postedDate,courseCode,facultyCode,title,description,batchNo,marks,dueDate,dueTime,topic,teamSize) \n" +
                        " values('"+postDate+"','"+courseCode.getValue()+"','"+facultyCode.getValue()+"','"+title.getValue()+"','"+desc.getValue()+"',\n" +
                        ""+batchNo+","+marks.getValue()+",'"+deadlineDate.getValue()+"',\n" +
                        "'"+deadlineTime.getValue()+"','"+topic.getValue()+"',"+teamSize.getValue()+");";
            }else{
                sql  = "insert into projectAssign (postedDate,courseCode,facultyCode,title,description,batchNo,marks,dueDate,dueTime,topic) \n" +
                        " values('"+postDate+"','"+courseCode.getValue()+"','"+facultyCode.getValue()+"','"+title.getValue()+"','"+desc.getValue()+"',\n" +
                        ""+batchNo+","+marks.getValue()+",'"+deadlineDate.getValue()+"',\n" +
                        "'"+deadlineTime.getValue()+"','"+topic.getValue()+"');";
            }

            int rs;
            rs = stmt.executeUpdate(sql);
            if(rs>0) {
                Notification.show("Successfully Inserted", 2000, Notification.Position.MIDDLE);
                // Now we have to insert attachment to attachment table
                //First Let us insert data without attachment into attachment table
                if(buffer.getInputStream().read()!=-1) {
                    String fileName = buffer.getFileName();
                    String fileType = FilenameUtils.getExtension(fileName);
                    String sqlA = "select Sno from projectAssign where postedDate ='" + postDate + "'and courseCode = '" + courseCode.getValue() + "' and title = '" + title.getValue() + "' and facultyCode='" + facultyCode.getValue() + "' and batchNo = " + batchNo + ";";
                    Statement stmtA = con.createStatement();
                    ResultSet rstA = stmtA.executeQuery(sqlA);
                    rstA.next();
                    int Pno = rstA.getInt("Sno");
                    rstA.close();
                    PreparedStatement pStmt = null;
                    String sqlB = "insert into attachment (Pno,attach,format,fileName) values(?,?,?,?);";
                    pStmt = con.prepareStatement(sqlB);
                    pStmt.setInt(1, Pno);
                    pStmt.setBinaryStream(2, buffer.getInputStream());
                    pStmt.setString(3,fileType);
                    String fname = buffer.getFileName();
                    String name = fname.substring(0, fname.lastIndexOf('.'));
                    pStmt.setString(4,name);
                    pStmt.executeUpdate();
                    Notification.show("Attachment Successfully inserted.", 2000, Notification.Position.MIDDLE);
                    // reload, can think of another way.
                }
                UI.getCurrent().getPage().reload();
            }
            else
                Notification.show("Insertion unsuccessful.",2000, Notification.Position.MIDDLE);
            con.close();
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
