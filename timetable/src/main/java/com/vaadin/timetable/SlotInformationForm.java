package com.vaadin.timetable;

import com.flowingcode.vaadin.addons.ironicons.IronIcons;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.timetable.Service.EmailService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;



public class SlotInformationForm extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";

    H2 heading  = new H2("Course Information");
    public int slotNo;
    public int batchNo;
    public int dayNo;
    TextField courseCode = new TextField("Course Code");
    TextField courseName = new TextField("Course Name");
    TextField faculty = new TextField("Faculty InCharge");
    TextField venue = new TextField("Venue");
    TextArea requirements = new TextArea("Requirements");

    Button schedule = new Button("Schedule", IronIcons.SCHEDULE.create());
    Button cancelSchedule = new Button();



    public SlotInformationForm(){
        addClassName("course-info-form");
        HorizontalLayout header = new HorizontalLayout();
        header.add(heading,schedule);
        header.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        //schedule.getStyle().set("margin-left", "800px");
        add(header);
        Div line = new Div();
        line.getStyle().set("width","100%").set("border-top","4px solid gainsboro");
        add(line);
        add(new FormLayout(courseCode,courseName,faculty,venue,requirements));

        schedule.addClickListener(buttonClickEvent -> {
            scheduleSlot();
        });
    }

    public SlotInformationForm(String role) {
        if(role.equals("STUDENT")) {
            addClassName("course-info-form");
            HorizontalLayout header = new HorizontalLayout();
            header.add(heading);
            header.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
            //schedule.getStyle().set("margin-left", "800px");
            add(header);
            Div line = new Div();
            line.getStyle().set("width", "100%").set("border-top", "4px solid gainsboro");
            add(line);
            add(new FormLayout(courseCode, courseName, faculty, venue, requirements));
        }

    }

    private void scheduleSlot() {
        Dialog dialog = new Dialog();
        VerticalLayout layout = new VerticalLayout();
        H2 header = new H2("Schedule a New Slot");
        ComboBox batchCode = new ComboBox();
        ComboBox courseCode = new ComboBox();
        ComboBox courseName = new ComboBox();
        ComboBox faculty = new ComboBox();
        ComboBox slotFrom = new ComboBox();
        ComboBox slotTo = new ComboBox();
        ComboBox venue = new ComboBox();
        DatePicker date = new DatePicker();
        Button add = new Button("Add");
        Button cancel = new Button("Cancel");
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickShortcut(Key.ESCAPE);

        FlexLayout AddButtonWrapper = new FlexLayout(add);
        AddButtonWrapper.setJustifyContentMode(JustifyContentMode.START);

        HorizontalLayout dialogButtons = new HorizontalLayout(AddButtonWrapper,cancel);
        dialogButtons.expand(AddButtonWrapper);

        batchCode.setLabel("Batch Code");
        courseCode.setLabel("Course Code");
        courseName.setLabel("Course Name");
        faculty.setLabel("Faculty");
        slotFrom.setLabel("From");
        slotTo.setLabel("To");
        venue.setLabel("Venue");
        date.setLabel("Date");

        batchCode.setSizeFull();
        courseCode.setSizeFull();
        courseName.setSizeFull();
        faculty.setSizeFull();
        venue.setSizeFull();
        date.setSizeFull();

        layout.add(header,batchCode,courseCode,courseName,faculty,new HorizontalLayout(slotFrom,slotTo),venue,date,dialogButtons);
        dialog.add(layout);

        updateBatchCombo(batchCode);
        updateCourseCodeCombo(courseCode);
        updatecourseNameCombo(courseName);
        updateFacultyCombo(faculty);
        updateSlotFromCombo(slotFrom);
        slotFrom.addValueChangeListener(evt -> {updateSlotToCombo(slotFrom.getValue(),slotTo);});
        updateVenueCombo(venue);

        courseCode.addValueChangeListener(evt->{
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

        courseName.addValueChangeListener(evt->{
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
        cancel.addClickListener(buttonClickEvent -> {dialog.close();});
        add.addClickListener(buttonClickEvent -> {
            scheduleNew(batchCode,courseCode,courseName,faculty,slotFrom,slotTo,venue,date);
            dialog.close();
        });

        dialog.open();
    }

    private void scheduleNew(ComboBox batchCode, ComboBox courseCode, ComboBox courseName, ComboBox faculty, ComboBox slotFrom, ComboBox slotTo, ComboBox venue, DatePicker date) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql;

            int batchNo,fromSlotNo,toSlotNo;
            String facultyCode;

            //get batchNo
            String BatchInfo[] = String.valueOf(batchCode.getValue()).split(" ");
            sql = "select batchNo from batch where batchCode ='"+BatchInfo[0]+"' and year ='"+BatchInfo[1]+"';";
            rs = stmt.executeQuery(sql);
            rs.next();
            batchNo = rs.getInt("batchNo");
            rs.close();

            //Get the SlotNo of the class to be added
            sql = "Select slotNo from slot where `from` = '"+slotFrom.getValue()+"';";
            rs = stmt.executeQuery(sql);
            rs.next();
            fromSlotNo = rs.getInt("slotNo");
            rs.close();

            sql = "Select slotNo from slot where `to` = '"+slotTo.getValue()+"';";
            rs = stmt.executeQuery(sql);
            rs.next();
            toSlotNo = rs.getInt("slotNo");
            rs.close();

            //get facultyCode
            sql = "select facultyCode from faculty where facultyName ='"+faculty.getValue()+"'";
            rs = stmt.executeQuery(sql);
            rs.next();
            facultyCode = rs.getString("facultyCode");
            rs.close();

            //get Date from datePicker
            Calendar cal = Calendar.getInstance();

            LocalDate today = LocalDate.now();
            LocalDate Date = date.getValue();
            LocalDate MAX_BOUND = LocalDate.now().plusMonths(2);
            if(((Date.isAfter(today) || Date.isEqual(today)) && (Date.isBefore(MAX_BOUND))) && (validateScheduledClass(batchNo,fromSlotNo,toSlotNo,venue.getValue(),Date) == 1)){
                int rst;
                while(fromSlotNo<=toSlotNo){
                    sql = "insert into updateTimetable (batchNo,slotNo,courseCode,facultyCode,hallNo,date,flag) values("+batchNo+","+fromSlotNo+",'"+courseCode.getValue()+"','"+facultyCode+"','"+venue.getValue()+"','"+Date+"','S');";
                    rst = stmt.executeUpdate(sql);
                    if(rst>0) {
                        Notification.show("Slot successfully scheduled", 2000, Notification.Position.MIDDLE);
                        EmailBean emailBean = new EmailBean();
                        emailBean.setTo("adheshreghu@gmail.com");
                        emailBean.setBody(courseCode.getValue()+" has been scheduled on "+Date+" at slotNo: "+fromSlotNo+" in hall "+venue.getValue()+".");
                        emailBean.setSubject("A CLASS HAS BEEN SCHEDULED.");
                        emailBean.setCc("coe18b001@iiitdm.ac.in,coe18b003@iiitdm.ac.in,coe18b004@iiitdm.ac.in,coe18b005@iiitdm.ac.in,coe18b006@iiitdm.ac.in");
                        EmailService emailService = new EmailService(emailBean);
                    }
                    else
                        Notification.show("Scheduling unsuccessful");
                    fromSlotNo++;
                }
            }else
                throw new ArithmeticException("Enter proper date");

        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private int validateScheduledClass(int batchNo, int fromSlotNo, int toSlotNo, Object hallNo, LocalDate date) {

        //Validate that no class is being conducted on that day,at given slot at the given hall

        //Validate that the given batch does not have a class at the given day and given slot

        return 1;
    }

    private void updateVenueCombo(ComboBox venue) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql = "select hallNo from hall;";
            rs = stmt.executeQuery(sql);
            Collection<Object> items = new ArrayList<>();
            while (rs.next()){
                Object entry = rs.getString("hallNo");
                items.add(entry);
            }
            venue.setItems(items);

        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void updateSlotToCombo(Object value, ComboBox slotTo) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql = "select `to` from slot where `to` > '"+value+"';";
            rs = stmt.executeQuery(sql);
            Collection<Object> to_items = new ArrayList<>();
            while (rs.next()){
                Object to_entry = rs.getString("to");
                to_items.add(to_entry);
            }
            slotTo.setItems(to_items);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void updateSlotFromCombo(ComboBox slotFrom) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql = "select `from` from slot;";
            rs = stmt.executeQuery(sql);
            Collection<Object> from_items = new ArrayList<>();
            while (rs.next()){
                Object from_entry = rs.getString("from");
                from_items.add(from_entry);
            }
            slotFrom.setItems(from_items);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void updateFacultyCombo(ComboBox faculty) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql = "select facultyName from faculty;";
            rs = stmt.executeQuery(sql);
            Collection<Object> items = new ArrayList<>();
            while (rs.next()){
                Object entry = rs.getString("facultyName");
                items.add(entry);
            }
            faculty.setItems(items);

        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void updatecourseNameCombo(ComboBox courseName) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql = "select courseName from course;";
            rs = stmt.executeQuery(sql);
            Collection<Object> items = new ArrayList<>();
            while (rs.next()){
                Object entry = rs.getString("courseName");
                items.add(entry);
            }
            courseName.setItems(items);

        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void updateCourseCodeCombo(ComboBox courseCode) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql = "select courseCode from course;";
            rs = stmt.executeQuery(sql);
            Collection<Object> items = new ArrayList<>();
            while (rs.next()){
                Object entry = rs.getString("courseCode");
                items.add(entry);
            }
            courseCode.setItems(items);

        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

    }

    private void updateBatchCombo(ComboBox batchCode) {
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
            batchCode.setItems(items);

        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    public void setValues(String courseC, String courseN, String facultyN, String hall, String[] require, int iter) {
        courseCode.setValue(courseC);
        courseName.setValue(courseN);
        faculty.setValue(facultyN);
        venue.setValue(hall);
        requirements.setValue("");
        requirements.setHeightFull();
        while(iter>=0){
            if(requirements.isEmpty() == true)
                requirements.setValue(require[iter--]);
            else
                requirements.setValue(requirements.getValue()+"\n"+require[iter--]);
        }

    }
}
