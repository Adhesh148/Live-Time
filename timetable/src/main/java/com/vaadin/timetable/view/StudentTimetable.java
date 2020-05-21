package com.vaadin.timetable.view;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.timetable.backend.TableEntry;
import com.vaadin.timetable.security.MyUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


@PageTitle("ViewTimeTable | Timetable | Student")
@Route(value = "student-view",layout = MainView.class)
public class StudentTimetable extends VerticalLayout {
    String url = "jdbc:mysql://aauorfmbt136d0.cuz1bxluuufz.ap-south-1.rds.amazonaws.com:3306/liveTimetable";
    String user = "dbms";
    String pwd = "Password_123";
    Grid<TableEntry> grid;
    String role = "STUDENT";
    SlotInformationForm form = new SlotInformationForm(role);
    ComboBox comboBox = new ComboBox();
    DatePicker datePicker = new DatePicker();

    Label heading = new Label("Live View");
    Label message = new Label("View your weekly timetable as it changes.");

    public StudentTimetable() {

        configureComboBox(comboBox);
        updateComboBox(comboBox);

        grid = new Grid<>(TableEntry.class);
        configureGrid(grid);
        grid.addClassName("timetable-grid");

        //get the batch of the student
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int batchNo=0;
        if (principal instanceof UserDetails) {
            batchNo = ((MyUserDetails) principal).getBatchNo();
        }
        String batchCode = "";
        if(batchNo>0){
            batchCode = getbatchCode(batchNo);
        }

        comboBox.setValue(batchCode);
        comboBox.setEnabled(false);

        // Adding datePicker
        configureDatePicker();
        HorizontalLayout toolbar = new HorizontalLayout(comboBox,datePicker);

        String weekSelected = "Week ";
        Label weekDisplay = new Label();
        datePicker.addValueChangeListener(datePickerLocalDateComponentValueChangeEvent -> {
            fillGrid(grid, (String) comboBox.getValue());
            LocalDate today = datePicker.getValue();
            int dayOfWeek = today.getDayOfWeek().getValue();
            LocalDate Sun = today.plusDays(-1*dayOfWeek);
            LocalDate Sat = Sun.plusDays(6);
            String input = datePicker.getValue().toString();
            Calendar cal = Calendar.getInstance();
            String format = "yyyy-MM-dd";
            SimpleDateFormat df = new SimpleDateFormat(format);
            Date date = null;
            try {
                date = df.parse(input);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTime(date);
            int week = cal.get(Calendar.WEEK_OF_YEAR);
            int year = cal.get(Calendar.YEAR);
            String monthNameSun = Month.of(Integer.parseInt(Sun.toString().split("-")[1])).name();
            monthNameSun = monthNameSun.toLowerCase();
            monthNameSun = StringUtils.capitalize(monthNameSun);
            String monthNameSat = Month.of(Integer.parseInt(Sat.toString().split("-")[1])).name();
            monthNameSat = monthNameSat.toLowerCase();
            monthNameSat = StringUtils.capitalize(monthNameSat);
            weekDisplay.setText("Week "+week+", "+year+" - "+Sun.toString().split("-")[2]+" "+monthNameSun+" to "+Sat.toString().split("-")[2]+" "+monthNameSat);
        });
        datePicker.setValue(LocalDate.now());

       // fillGrid(grid, (String) comboBox.getValue());

        grid.addItemClickListener(evt -> {
            updateForm(evt);
        });

        heading.addClassName("course-abbreviation-heading");
        message.addClassName("course-abbreviation-message");
        add(heading,message,new Hr());


        add(toolbar, weekDisplay,grid, form);

    }

    private void configureDatePicker() {
        datePicker.setPlaceholder("Pick a Date");
        datePicker.setWeekNumbersVisible(true);
        datePicker.setI18n(new DatePicker.DatePickerI18n().setWeek("Week")
                .setCalendar("Calendar").setClear("Clear").setToday("Today")
                .setCancel("cancel").setFirstDayOfWeek(1)
                .setMonthNames(Arrays.asList("January", "February", "March",
                        "April", "May", "June", "July", "August", "September",
                        "October", "November", "December"))
                .setWeekdays(Arrays.asList("Sunday", "Monday", "Tuesday",
                        "Wednesday", "Thursday", "Friday", "Saturday"))
                .setWeekdaysShort(Arrays.asList("Sun", "Mon", "Tue", "Wed",
                        "Thu", "Fri", "Sat")));
    }

    private String getbatchCode(int batchNo) {
        String batchCode ="";
        String year = "";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "select batchCode,year from batch where batchNo = "+batchNo+";";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                batchCode = rs.getString("batchCode");
                year = rs.getString("year");
                year = year.split("-")[0];
            }
            rs.close();
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
        String batch = batchCode+" "+year;
        return batch;
    }



    private void updateForm(ItemClickEvent<TableEntry> evt) {
        String slot = evt.getColumn().getKey();
        if(!evt.getItem().getSlot(evt,slot).equalsIgnoreCase("")) {
            String courseCode = getCourseCode(evt.getItem().getSlot(evt,slot));
            String courseName, facultyName, hallNo, facultyCode;
            String[] requirements = new String[10];
            int iter = 0;
            int slotNo = getSlotNo(slot);
            int batchNo;
            String batchInfo[] = String.valueOf(comboBox.getValue()).split(" ");

            //Get Values of courseName,facultyName, hallNo from the backend
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pwd);
                Statement stmt = con.createStatement();
                String sql;
                ResultSet rs;
                //get batch No
                sql = "select batchNo from batch where batchCode ='" + batchInfo[0] + "' and year ='" + batchInfo[1] + "';";
                rs = stmt.executeQuery(sql);
                rs.next();
                batchNo = rs.getInt("batchNo");
                rs.close();
                //get facultyCode
                sql = "select facultyCode,hallNo from weekTimetable where batchNo=" + batchNo + " and slotNo =" + slotNo + " and courseCode = '" + courseCode + "' and dayNo =" + evt.getItem().getDay() + ";";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    facultyCode = rs.getString("facultyCode");
                    hallNo = rs.getString("hallNo");
                    rs.close();
                } else {
                    rs.close();
                    int dayNo = datePicker.getValue().getDayOfWeek().getValue();
                    LocalDate dayDate = datePicker.getValue().plusDays((-1 * dayNo));
                    dayDate = dayDate.plusDays(evt.getItem().getDay());
                    String Date = dayDate.toString();
                    sql = "select facultyCode,hallNo from updateTimetable where batchNo=" + batchNo + " and slotNo =" + slotNo + " and courseCode = '" + courseCode + "' and date = '" + Date + "';";
                    rs = stmt.executeQuery(sql);
                    rs.next();
                    facultyCode = rs.getString("facultyCode");
                    hallNo = rs.getString("hallNo");
                    rs.close();
                }

                //get facultyName
                sql = "Select facultyName from faculty where facultyCode='" + facultyCode + "';";
                rs = stmt.executeQuery(sql);
                rs.next();
                facultyName = rs.getString("facultyName");
                rs.close();
                //get Course Name
                sql = "select courseName from course where courseCode='" + courseCode + "';";
                rs = stmt.executeQuery(sql);
                rs.next();
                courseName = rs.getString("courseName");
                rs.close();

                //Get the requirements of the course if any
                sql = "select requirements from courseRequirements where courseCode = '" + courseCode + "'";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    requirements[iter++] = rs.getString("requirements");
                }
                form.batchNo = batchNo;
                form.slotNo = slotNo;
                form.dayNo = evt.getItem().getDay();

                form.setValues(courseCode, courseName, facultyName, hallNo, requirements, --iter);
                rs.close();
                con.close();
            } catch (Exception e) {
                Notification.show(e.getLocalizedMessage());
            }
        }else{
            form.setValues("","","","", new String[]{""},0);
        }
    }

    private String getCourseCode(String courseCode) {
        String CC = courseCode;
        int userId = 0;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails) {
            userId = ((MyUserDetails) principal).getId();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con  = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "select count(*) as cnt from course where courseCode = '"+courseCode+"';";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int cnt = rs.getInt("cnt");
            rs.close();
            if(cnt<=0){
                String sqlA = "select courseCode from courseAbbreviation where abbrv ='"+courseCode+"' and userId = "+userId+";";
                ResultSet rst = stmt.executeQuery(sqlA);
                rst.next();
                CC = rst.getString("courseCode");
                rst.close();
            }
            rs.close();
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
        return CC;
    }

    private int getSlotNo(String slot) {
        if(slot.equals("SLOT_I"))
            return 1;
        else if(slot.equals("SLOT_II"))
            return 2;
        else if(slot.equals("SLOT_III"))
            return 3;
        else if(slot.equals("SLOT_IV"))
            return 4;
        else if(slot.equals("SLOT_V"))
            return 5;
        else if(slot.equals("SLOT_VI"))
            return 6;
        else if(slot.equals("SLOT_VII"))
            return 7;
        else if(slot.equals("SLOT_VIII"))
            return 8;
        else
            return 9;
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
                time[days-1].setDay(days);
                iter = 1;
                if(rs.next()) {
                    while (iter <= 9) {
                        if (rs.getInt("slotNo") == iter) {
                            setSlot(iter++, time[days - 1], rs, 1,0);
                            if (!rs.isLast())
                                rs.next();
                        } else {
                            setSlot(iter++, time[days - 1], rs, 0,0);
                        }
                    }
                }
                rs.close();
                days++;
            }
            //Notification.show("IN");
            fillGridSchedule(grid,batchInfo,time);
            grid.setItems(time[0],time[1],time[2],time[3],time[4]);
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void fillGridSchedule(Grid<TableEntry> grid, String batchInfo, TableEntry[] time) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql;

            // Update grid with values from current week. So get date and date of weekend
            //LocalDate today = LocalDate.now();
            //Calendar cal  = Calendar.getInstance();
            //cal.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //String friday[] =  sdf.format(cal.getTime()).split("-");

            //get batchCode and year from batchInfo
            String batch[] = batchInfo.split(" ");

            //Get Batch Number corresponding to the batchCode and year
            sql = "select batchNo from batch where batchCode ='"+batch[0]+"' and year = '"+batch[1]+"';";
            rs = stmt.executeQuery(sql);
            rs.next();
            int batchNo = rs.getInt("batchNo");
            rs.close();

            // Call sql to update ones with S flag
            //Notification.show(String.valueOf(datePicker.getValue().getDayOfWeek().getValue()));
            int dayNo = datePicker.getValue().getDayOfWeek().getValue();
            LocalDate dayDate = datePicker.getValue().plusDays((-1*dayNo));
            int days =1;
            while(days<=5){
                String Date = dayDate.plusDays(days).toString();
//                Calendar dayDate = Calendar.getInstance();
//                dayDate.set(Calendar.DAY_OF_WEEK,days+1);
//                String Date = sdf.format(dayDate.getTime());
                sql = "select flag,courseCode,slotNo from updateTimetable where batchNo = "+batchNo+" and date ='"+Date+"' order by slotNo asc;";
                rs = stmt.executeQuery(sql);
                int slotNo;
                while(rs.next()){
                    String cancelFlag = rs.getString("flag");
                    int cancelled = 1;
                    if(cancelFlag.equalsIgnoreCase("S"))
                        cancelled = 0;
                    slotNo = rs.getInt("slotNo");
                    setSlot(slotNo,time[days-1],rs,1,cancelled);
                }
                rs.close();
                days++;
            }
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

    }

    private void setSlot(int iter, TableEntry timetable, ResultSet rs,int flag,int cancelled) {
        try{
            String abbrv = getAbbrv(rs.getString("courseCode"));
            String courseCode = rs.getString("courseCode");
            if(!abbrv.equalsIgnoreCase(""))
                courseCode = abbrv;

            if(iter == 1){
                if(flag == 1 && cancelled==0)
                    timetable.setSLOT_I(courseCode);
                else
                    timetable.setSLOT_I("");
            }
            else if(iter == 2){
                if(flag == 1 && cancelled==0)
                    timetable.setSLOT_II(courseCode);
                else
                    timetable.setSLOT_II("");
            }
            else if(iter == 3){
                if(flag == 1 && cancelled==0)
                    timetable.setSLOT_III(courseCode);
                else
                    timetable.setSLOT_III("");
            }
            else if(iter == 4){
                if(flag == 1 && cancelled==0)
                    timetable.setSLOT_IV(courseCode);
                else
                    timetable.setSLOT_IV("");
            }
            else if(iter == 5){
                if(flag == 1 && cancelled==0)
                    timetable.setSLOT_V(courseCode);
                else
                    timetable.setSLOT_V("");
            }
            else if(iter == 6){
                if(flag == 1 && cancelled==0)
                    timetable.setSLOT_VI(courseCode);
                else
                    timetable.setSLOT_VI("");
            }
            else if(iter == 7){
                if(flag == 1 && cancelled==0)
                    timetable.setSLOT_VII(courseCode);
                else
                    timetable.setSLOT_VII("");
            }
            else if(iter == 8){
                if(flag == 1 && cancelled==0)
                    timetable.setSLOT_VIII(courseCode);
                else
                    timetable.setSLOT_VIII("");
            }
            else if(iter == 9){
                if(flag == 1 && cancelled==0)
                    timetable.setSLOT_IX(courseCode);
                else
                    timetable.setSLOT_IX("");
            }
        }catch (Exception e){
            e.getLocalizedMessage();
        }

    }

    private String getAbbrv(String courseCode) {
        String abbrv = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            int userId = 0;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof MyUserDetails) {
                userId = ((MyUserDetails) principal).getId();
            }
            String sql = "select ifnull(abbrv,'') as abbrv from courseAbbreviation where userId = "+userId+" and courseCode = '"+courseCode+"';";
            ResultSet rst = stmt.executeQuery(sql);
            if(rst.next())
                abbrv = rst.getString("abbrv");
            rst.close();
            con.close();
        }catch (Exception e){
            Notification.show("NE");
            Notification.show(e.getLocalizedMessage());
        }
        return  abbrv;
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
            rs.close();
            con.close();
            comboBox.setItems(items);
        }catch(Exception e){
            e.getLocalizedMessage();
        }

    }

    private void configureGrid(Grid<TableEntry> grid) {
        grid.setColumns("SLOT_I","SLOT_II","SLOT_III","SLOT_IV","SLOT_V","SLOT_VI","SLOT_VII","SLOT_VIII","SLOT_IX");
        // grid.getColumnByKey("Day").setHeader("Day");
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
