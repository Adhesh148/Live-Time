package com.vaadin.timetable;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.*;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

public class ProjectPanel extends VerticalLayout {

    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";

    public ProjectPanel(String postedDate, String courseCode,String facultyCode, String title, String desc, String batch, int marks, String dueDate, String dueTime) {
        Accordion panel = new Accordion();
        addClassName("project-panel");
        Label posted = new Label("Posted on");
        posted.addClassName("bolden");
        Label description = new Label(desc);
        Label dueTitle = new Label("Deadline");
        dueTitle.addClassName("bolden");
        Label due = new Label(dueDate + " "+ dueTime);

        //get the the attachment from mysql
        Label attach = null;
        Button download = new Button("Download");

        download.addClickListener(buttonClickEvent -> {
            setAttachment(postedDate,courseCode,facultyCode,title,batch,marks,dueDate,dueTime);
        });

        //Add a delete button
        Button delete  = new Button("Delete",VaadinIcon.MINUS.create());
        Button edit  = new Button("Edit",VaadinIcon.EDIT.create());
        panel.add(title,new VerticalLayout(new HorizontalLayout(posted,new Label(postedDate)),new HorizontalLayout(dueTitle,due),description,new HorizontalLayout(delete,edit,download)));
        panel.close();
        add(panel);

        // Add Click Listeners
        delete.addClickListener(evt -> {
            openDeleteDialog(postedDate,courseCode,facultyCode,title,batch,dueDate,dueTime);
            //onDelete(postedDate,courseCode,facultyCode,title,batch,dueDate,dueTime);
        });

        edit.addClickListener(evt -> {
            onEdit(postedDate,courseCode,facultyCode,title,desc,batch,marks,dueDate,dueTime);
        });
    }

    private void setAttachment(String postedDate, String courseCode, String facultyCode, String title, String batch, int marks, String dueDate, String dueTime) {
        try{
            // Have to work on this.
            String fileName = "download";

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            // Get Pno
            String sql = "select Sno from projectAssign where postedDate ='"+postedDate+"' and courseCode = '"+courseCode+"' and facultyCode = '"+facultyCode+"' and title ='"+title+"' and batchNo = '"+batch+"' and dueDate = '"+dueDate+"' and dueTime = '"+dueTime+"';";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int Pno = rs.getInt("Sno");
            rs.close();

            // get the blob
            String sqlA = "select attach,format from attachment  where Pno = "+Pno+";";
            rs = stmt.executeQuery(sqlA);
            if(rs.next()){
                InputStream is = rs.getBinaryStream("attach");
                String format = rs.getString("format");
                //OutputStream os = new FileOutputStream("/home/adheshreghu/Documents/SEM4/MYSQL/"+fileName+"."+format);
            }
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void download(InputStream is) throws IOException {

    }


    private void openDeleteDialog(String postedDate, String courseCode, String facultyCode, String title, String batch, String dueDate, String dueTime) {
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
            onDelete(postedDate,courseCode,facultyCode,title,batch,dueDate,dueTime);
            dialog.close();
            //Could show a dialog box
        });
        cancel.addClickListener(evt -> {
            dialog.close();
        });
    }

    private void onEdit(String InppostedDate, String InpcourseCode, String InpfacultyCode, String Inptitle, String Inpdesc, String Inpbatch, int Inpmarks, String InpdueDate, String InpdueTime) {
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
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        // Add Upload Button------------------------
        // Simple in memory single file upload.
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        //--------------------------------------

        VerticalLayout innerSecondaryLayout = new VerticalLayout();
        innerSecondaryLayout.add(title,desc,upload,save);
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

        // set Default Values
        courseCode.setValue(InpcourseCode);
        facultyCode.setValue(InpfacultyCode);
        title.setValue(Inptitle);
        desc.setValue(Inpdesc);
        marks.setValue(String.valueOf(Inpmarks));
        deadlineDate.setValue(LocalDate.parse(InpdueDate));
        deadlineTime.setValue(LocalTime.parse(InpdueTime));

        //add functionality to save button
        save.addClickListener(evt->{

        });


        project.add(outer_layout);
        project.open();

    }

    private void onDelete(String postedDate, String courseCode, String facultyCode, String title, String batch, String dueDate, String dueTime) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            //Get Sno from projectAssign table to delete data from attach table as well
            String sql = "Select Sno from projectAssign where postedDate ='"+postedDate+"' and courseCode = '"+courseCode+"' and facultyCode = '"+facultyCode+"' and title ='"+title+"' and batchNo = '"+batch+"' and dueDate = '"+dueDate+"' and dueTime = '"+dueTime+"';";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int Pno = rs.getInt("Sno");
            rs.close();

            String sql2 = "delete from attachment where Pno = "+Pno+";";
            Statement stmtA  = con.createStatement();
            int rstA = stmtA.executeUpdate(sql2);

            //Let us delete from projectAssign table
            String sql1 = "delete from projectAssign where Sno = "+Pno+";";
            int rst = stmt.executeUpdate(sql1);
            if(rst>0){
                Notification.show("Deletion successful",2000, Notification.Position.MIDDLE);
                UI.getCurrent().getPage().reload();
            }

        }catch(Exception e){
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
