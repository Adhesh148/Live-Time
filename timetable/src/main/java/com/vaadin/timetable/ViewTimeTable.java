package com.vaadin.timetable;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.timetable.backend.TableEntry;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@PageTitle("ViewTimeTable | Timetable")
@Route(value = "view",layout = MainView.class)
public class ViewTimeTable extends VerticalLayout {

    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";
    Grid<TableEntry> grid;
    SlotInformationForm form = new SlotInformationForm();
    ComboBox comboBox = new ComboBox();

    Icon addNew = new Icon(VaadinIcon.PLUS_CIRCLE);

    public ViewTimeTable(){

        configureComboBox(comboBox);
        updateComboBox(comboBox);

        grid = new Grid<>(TableEntry.class);
        configureGrid(grid);

        comboBox.addValueChangeListener(valueChangeEvent -> {
            fillGrid(grid,(String) comboBox.getValue());
            //test.setText((String) comboBox.getValue());
        });

        HorizontalLayout toolbar = new HorizontalLayout(comboBox,addNew);
        toolbar.setAlignSelf(Alignment.CENTER, addNew);

        grid.addItemClickListener(evt -> updateForm(evt));
        addNew.addClickListener(evt -> {
            addSlot();
        });
        add(toolbar,grid,form);
    }

    private void addSlot() {
        Dialog dialog = new Dialog();
        VerticalLayout layout = new VerticalLayout();
        H2 header = new H2("Add a New Slot");
        ComboBox batchCode = new ComboBox();
        ComboBox courseCode = new ComboBox();
        ComboBox courseName = new ComboBox();
        ComboBox faculty = new ComboBox();
        ComboBox slotFrom = new ComboBox();
        ComboBox slotTo = new ComboBox();
        ComboBox venue = new ComboBox();
        ComboBox day  = new ComboBox();
        Button add = new Button("Add");
        Button cancel = new Button("Cancel");
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

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
        day.setLabel("Day");

        batchCode.setSizeFull();
        courseCode.setSizeFull();
        courseName.setSizeFull();
        faculty.setSizeFull();
        venue.setSizeFull();
        day.setSizeFull();

        layout.add(header,batchCode,courseCode,courseName,faculty,new HorizontalLayout(slotFrom,slotTo),venue,day,dialogButtons);
        dialog.add(layout);

        updateBatchCombo(batchCode);
        updateCourseCodeCombo(courseCode);
        updatecourseNameCombo(courseName);
        updatefacultyCombo(faculty);
        updateSlotFromCombo(slotFrom);
        updateVenueCombo(venue);
        updateDayCombo(day);

        slotFrom.addValueChangeListener(evt -> {
            updateSlotToCombo(slotFrom.getValue(),slotTo);
        });

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

        cancel.addClickListener(buttonClickEvent -> {
            dialog.close();
        });
        cancel.addClickShortcut(Key.ESCAPE);

        add.addClickShortcut(Key.ENTER);
        add.addClickListener(buttonClickEvent -> {
            addNew(batchCode,courseCode,courseName,faculty,slotFrom,slotTo,venue,day);
            String cb = (String) comboBox.getValue();
            add.getUI().ifPresent(ui -> {ui.navigate("");});
            add.getUI().ifPresent(ui -> {ui.navigate("view");});
            dialog.close();

        });

        dialog.setSizeFull();
        dialog.open();
    }

    private void addNew(ComboBox batchCode, ComboBox courseCode, ComboBox courseName, ComboBox faculty, ComboBox slotFrom, ComboBox slotTo, ComboBox venue, ComboBox day){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql;

            int batchNo,fromSlotNo,toSlotNo,dayNo;
            String facultyCode;

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

            //getBatchNo
            String BatchInfo[] = String.valueOf(batchCode.getValue()).split(" ");
            sql = "select batchNo from batch where batchCode ='"+BatchInfo[0]+"' and year ='"+BatchInfo[1]+"';";
            rs = stmt.executeQuery(sql);
            rs.next();
            batchNo = rs.getInt("batchNo");
            rs.close();

            //get dayNo
            sql = "select dayNo from day where day = '"+day.getValue()+"';";
            rs = stmt.executeQuery(sql);
            rs.next();
            dayNo = rs.getInt("dayNo");
            rs.close();

            //get facultyCode
            sql = "select facultyCode from faculty where facultyName ='"+faculty.getValue()+"'";
            rs = stmt.executeQuery(sql);
            rs.next();
            facultyCode = rs.getString("facultyCode");
            rs.close();

            boolean rst;
            //Insert data for all the given slots
            while(fromSlotNo<=(toSlotNo)){
                Notification.show("OPENED");
                sql = "insert into weekTimetable (batchNo,slotNo,courseCode,facultyCode,hallNo,dayNo) values("+batchNo+","+fromSlotNo+",'"+ courseCode.getValue() +"','"+facultyCode+"','"+venue.getValue()+"',"+dayNo+");";
                rst = stmt.execute(sql);
                if(rst == false)
                    Notification.show("Slot successfully inserted");
                else
                    Notification.show("Insertion unsuccessful");
                rs.close();

                fromSlotNo++;
            }
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

    private void updateDayCombo(ComboBox day) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql = "select day from day;";
            rs = stmt.executeQuery(sql);
            Collection<Object> items = new ArrayList<>();
            while (rs.next()){
                Object entry = rs.getString("day");
                items.add(entry);
            }
            day.setItems(items);
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

    private void updatefacultyCombo(ComboBox faculty) {
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

    private void updateForm(ItemClickEvent<TableEntry> evt) {
        String slot = evt.getColumn().getKey();
        String courseCode = evt.getItem().getSlot(evt,slot);
        String courseName,facultyName,hallNo;
        String[] requirements = new String[10];
        int iter =0;

        //Get Values of courseName,facultyName, hallNo from the backend
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;
            sql = "select distinct w.courseCode,c.courseName,f.facultyName,w.hallNo from weekTimetable w \n" +
                    "inner join course c \n" +
                    "on c.courseCode = w.courseCode \n" +
                    "inner join faculty f on \n" +
                    "w.facultyCode = f.facultyCode \n" +
                    "where c.courseCode = '"+courseCode+"';";
            rs = stmt.executeQuery(sql);
            rs.next();
            courseName = rs.getString("courseName");
            facultyName = rs.getString("facultyName");
            hallNo = rs.getString("hallNo");
            rs.close();

            //Get the requirements of the course if any
            sql = "select requirements from courseRequirements where courseCode = '"+courseCode+"'";
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                requirements[iter++] = rs.getString("requirements");
            }

            form.setValues(courseCode,courseName,facultyName,hallNo,requirements,--iter);
        }catch (Exception e){
            e.getLocalizedMessage();
        }
    }


    private void fillGrid(Grid<TableEntry> grid, String batchInfo) {
        try {
            int iter =0,cnt=0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;

            //get batchCode and year from batchInfo
            String batch[] = batchInfo.split(" ");

            //Get Batch Number corresponding to the batchCode and year
            sql = "select batchNo from batch where batchCode ='"+batch[0]+"' and year = '"+batch[1]+"';";
            rs = stmt.executeQuery(sql);
            rs.next();
            int batchNo = rs.getInt("batchNo");
            rs.close();

            //Get entry for the Timetable
            int days = 1;
            TableEntry time[] = new TableEntry[5];
            while(days<=5) {
                sql = "select courseCode,slotNo from weekTimetable where batchNo = '" + batchNo + "' and dayNo = '" + days + "' order by slotNo asc;";
                rs = stmt.executeQuery(sql);
                time[days-1] = new TableEntry();
                iter = 1;
                rs.next();
                while (iter<=9) {
                    if(rs.getInt("slotNo") == iter){
                        setSlot(iter++,time[days-1],rs,1);
                        if(!rs.isLast())
                            rs.next();
                    }else{
                        setSlot(iter++, time[days-1], rs,0);
                    }
                }
                rs.close();
                days++;
            }
            //Notification.show("IN");
            grid.setItems(time[0],time[1],time[2],time[3],time[4]);

        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void setSlot(int iter, TableEntry timetable, ResultSet rs,int flag) {
        try{
            if(iter == 1){
                if(flag == 1)
                    timetable.setSLOT_I(rs.getString("courseCode"));
                else
                    timetable.setSLOT_I("");
            }
            else if(iter == 2){
                if(flag == 1)
                    timetable.setSLOT_II(rs.getString("courseCode"));
                else
                    timetable.setSLOT_II("");
            }
            else if(iter == 3){
                if(flag == 1)
                    timetable.setSLOT_III(rs.getString("courseCode"));
                else
                    timetable.setSLOT_III("");
            }
            else if(iter == 4){
                if(flag == 1)
                    timetable.setSLOT_IV(rs.getString("courseCode"));
                else
                    timetable.setSLOT_IV("");
            }
            else if(iter == 5){
                if(flag == 1)
                    timetable.setSLOT_V(rs.getString("courseCode"));
                else
                    timetable.setSLOT_V("");
            }
            else if(iter == 6){
                if(flag == 1)
                    timetable.setSLOT_VI(rs.getString("courseCode"));
                else
                    timetable.setSLOT_VI("");
            }
            else if(iter == 7){
                if(flag == 1)
                    timetable.setSLOT_VII(rs.getString("courseCode"));
                else
                    timetable.setSLOT_VII("");
            }
            else if(iter == 8){
                if(flag == 1)
                    timetable.setSLOT_VIII(rs.getString("courseCode"));
                else
                    timetable.setSLOT_VIII("");
            }
            else if(iter == 9){
                if(flag == 1)
                    timetable.setSLOT_IX(rs.getString("courseCode"));
                else
                    timetable.setSLOT_IX("");
            }
        }catch (Exception e){
            e.getLocalizedMessage();
        }

    }

    private void configureComboBox(ComboBox comboBox) {
        comboBox.setAllowCustomValue(false);
        comboBox.setPlaceholder("Select a batch...");
    }

    private void updateComboBox(ComboBox comboBox) {

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;
            // Find the number of distinct batches.
            sql = "select count(batchNo) as cnt from batch;";
            rs = stmt.executeQuery(sql);
            rs.next();
            //Instantiate an array of objects to store the items from batch table
            int iter =0;
            Object items[] = new Object[rs.getInt("cnt")];
            rs.close();
            //Call sql to get batchCode and BatchYear
            sql = "Select batchCode,year from batch;";
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                Calendar cal = Calendar.getInstance();
                cal.setTime(rs.getDate("year"));
                items[iter++] = rs.getString("batchCode")+" "+cal.get(Calendar.YEAR);
            }
            comboBox.setItems(items);
        }catch(Exception e){
            e.getLocalizedMessage();
        }

    }

    private void configureGrid(Grid<TableEntry> grid) {
        grid.setColumns("SLOT_I","SLOT_II","SLOT_III","SLOT_IV","SLOT_V","SLOT_VI","SLOT_VII","SLOT_VIII","SLOT_IX");
        grid.getColumnByKey("SLOT_I").setHeader("8:00 - 9:00");
        grid.getColumnByKey("SLOT_II").setHeader("9:00 - 10:00");
        grid.getColumnByKey("SLOT_III").setHeader("10:00 - 11:00");
        grid.getColumnByKey("SLOT_IV").setHeader("11:00 - 12:00");
        grid.getColumnByKey("SLOT_V").setHeader("12:00 - 13:00");
        grid.getColumnByKey("SLOT_VI").setHeader("13:00 - 14:00");
        grid.getColumnByKey("SLOT_VII").setHeader("14:00 - 15:00");
        grid.getColumnByKey("SLOT_VIII").setHeader("15:00 - 16:00");
        grid.getColumnByKey("SLOT_IX").setHeader("16:00 - 17:00");

        grid.setHeightByRows(true);
        grid.setSortableColumns();

    }


}
