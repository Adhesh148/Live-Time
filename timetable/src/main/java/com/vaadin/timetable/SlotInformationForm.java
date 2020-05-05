package com.vaadin.timetable;

import com.flowingcode.vaadin.addons.ironicons.IronIcons;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
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
    //gives date of selected Slot
    public String slotDate;
    TextField courseCode = new TextField("Course Code");
    TextField courseName = new TextField("Course Name");
    TextField faculty = new TextField("Faculty InCharge");
    TextField venue = new TextField("Venue");
    TextArea requirements = new TextArea("Requirements");

    Button schedule = new Button("Schedule", IronIcons.SCHEDULE.create());
    Button cancelSchedule = new Button("Cancel");



    public SlotInformationForm(){
        addClassName("course-info-form");
        HorizontalLayout header = new HorizontalLayout();
        cancelSchedule.addThemeVariants(ButtonVariant.LUMO_ERROR);
        header.add(heading,schedule,cancelSchedule);
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

        cancelSchedule.addClickListener(buttonClickEvent -> {
            if(!courseCode.getValue().equalsIgnoreCase("")) {
                onCancel();
            }
        });
    }

    private void onCancel() {
        Dialog dialog = new Dialog();
        H4 header = new H4("Confirm Edit");
        Label message = new Label("Are you sure you want to cancel the class?");
        Button cancel = new Button("Close");
        Button update = new Button("Cancel Class");

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FlexLayout UpdateButtonWrapper = new FlexLayout(update);
        UpdateButtonWrapper.setJustifyContentMode(JustifyContentMode.END);

        HorizontalLayout dialogButtons = new HorizontalLayout(cancel,UpdateButtonWrapper);
        dialogButtons.expand(UpdateButtonWrapper);

        dialog.add(new VerticalLayout(header,message),dialogButtons);
        dialog.setWidth("400px");
        dialog.setHeight("150px");
        dialog.open();

        update.addClickListener(evt -> {
            cancelScheduleSlot();
            dialog.close();
            update.getUI().ifPresent(ui -> {ui.navigate("");});
            update.getUI().ifPresent(ui -> {ui.navigate("view");});

        });

        cancel.addClickListener(evt -> {
            dialog.close();
        });
    }

    private void cancelScheduleSlot() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String facultyCode = getFacultyCode(faculty.getValue());
            LocalDate date = LocalDate.now();
            String today = date.toString();
            //Notification.show(batchNo+" "+slotNo+" "+courseCode.getValue()+" "+faculty.getValue()+" "+venue.getValue()+" "+dayNo);
            String sql = "select count(*) as cnt from weekTimetable where batchNo = "+batchNo+" and slotNo = "+slotNo+" and courseCode = '"+courseCode.getValue()+"' and facultyCode = '"+facultyCode+"' and hallNo = '"+venue.getValue()+"' and dayNo="+dayNo+";";
            ResultSet rs = stmt.executeQuery(sql);
            int cnt = 0;
            if(rs.next())
                cnt = rs.getInt("cnt");
            rs.close();
            if(cnt>0){
                // Then the cancellation is on a week slot
                String sqlA  = "insert into updateTimetable (batchNo,slotNo,courseCode,facultyCode,hallNo,date,flag,postedDate) values("+batchNo+","+slotNo+",'"+courseCode.getValue()+"','"+facultyCode+"','"+venue.getValue()+"','"+slotDate+"','CW','"+today+"')";
                int rst = stmt.executeUpdate(sqlA);
                if(rst>0) {
                    Notification.show("Class cancelled",2000, Notification.Position.MIDDLE);
                    OpenAskNotifyDialogCancel(batchNo,slotNo,courseCode.getValue(),faculty.getValue(),venue.getValue(),slotDate);
                }
            }else{
                String sqlA = "delete from updateTimetable where batchNo = "+batchNo+" and slotNo = "+slotNo+" and courseCode ='"+courseCode.getValue()+"' and facultyCode = '"+facultyCode+"' and hallNo = '"+venue.getValue()+"' and date = '"+slotDate+"' and flag = 'S';";
                int rst = stmt.executeUpdate(sqlA);
                if(rst>0) {
                    Notification.show("Class cancelled",2000, Notification.Position.MIDDLE);
                    OpenAskNotifyDialogCancel(batchNo,slotNo,courseCode.getValue(),faculty.getValue(),venue.getValue(),slotDate);
                }
            }
            con.close();
        }catch (Exception e) {
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void OpenAskNotifyDialogCancel(int batchNo, int slotNo, String courseCode, String facultyName, String hall, String Date) {
        Dialog dialog = new Dialog();
        H4 header = new H4("Do you want to Notify?");
        Label message = new Label("Notify Students/TAs about the update/cancellation.");
        Button yes = new Button("YES");
        Button no = new Button("NO");
        dialog.add(new VerticalLayout(header,message,new HorizontalLayout(yes,no)));
        dialog.open();

        no.addClickListener(evt->{
            dialog.close();
            // UI.getCurrent().getPage().reload();
        });
        yes.addClickListener(evt->{
            String flag = "C";
            new MailingDialog(flag,batchNo,slotNo,courseCode,facultyName,hall,Date);
            dialog.close();
            // UI.getCurrent().getPage().reload();
        });
    }

    private String getFacultyCode(String value) {
        String facultyCode = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "select facultyCode from faculty where facultyName = '"+value+"';";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
                facultyCode = rs.getString("facultyCode");
            rs.close();
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

        return facultyCode;
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
                con.close();
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
                con.close();
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

            //Will be used to while sending email
            int fromSlot = fromSlotNo;

            if(((Date.isAfter(today) || Date.isEqual(today)) && (Date.isBefore(MAX_BOUND))) && (validateScheduledClass(batchNo,fromSlotNo,toSlotNo,venue.getValue(),Date) == 1)){
                int rst;
                while(fromSlotNo<=toSlotNo){
                    sql = "insert into updateTimetable (batchNo,slotNo,courseCode,facultyCode,hallNo,date,flag,postedDate) values("+batchNo+","+fromSlotNo+",'"+courseCode.getValue()+"','"+facultyCode+"','"+venue.getValue()+"','"+Date+"','S','"+today.toString()+"');";
                    rst = stmt.executeUpdate(sql);
                    if(rst>0) {
                        Notification.show("Slot successfully scheduled", 2000, Notification.Position.MIDDLE);
                        // Calling the email Service
                    }
                    else
                        Notification.show("Scheduling unsuccessful");
                    fromSlotNo++;
                }
                OpenAskNotifyDialog(Date,courseCode.getValue(),fromSlot,toSlotNo,batchNo,venue.getValue());
            }else
                throw new ArithmeticException("Enter proper date");
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void OpenAskNotifyDialog(LocalDate date, Object courseCode, int fromSlot, int toSlotNo, int batchNo, Object venue) {
        Dialog dialog = new Dialog();
        H4 header = new H4("Do you want to Notify?");
        Label message = new Label("Notify Students/TAs about the update/cancellation.");
        Button yes = new Button("YES");
        Button no = new Button("NO");
        dialog.add(new VerticalLayout(header,message,new HorizontalLayout(yes,no)));
        dialog.open();

        no.addClickListener(evt->{
            dialog.close();
            yes.getUI().ifPresent(ui -> {ui.navigate("");});
            yes.getUI().ifPresent(ui -> {ui.navigate("view");});
        });
        yes.addClickListener(evt->{
           String flag = "S";
           new MailingDialog(flag,courseCode,date,fromSlot,toSlotNo,batchNo,venue);
           dialog.close();
            yes.getUI().ifPresent(ui -> {ui.navigate("");});
            yes.getUI().ifPresent(ui -> {ui.navigate("view");});
        });
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
            con.close();
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
            con.close();
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
            con.close();
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
            con.close();
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
            con.close();
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
            con.close();
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
            con.close();
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
