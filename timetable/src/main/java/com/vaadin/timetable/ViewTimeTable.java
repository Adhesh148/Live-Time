package com.vaadin.timetable;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.timetable.backend.TableEntry;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

@PageTitle("ViewTimeTable | Timetable")
@Route(value = "view",layout = MainView.class)
public class ViewTimeTable extends VerticalLayout {

    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";
    Grid<TableEntry> grid;
    SlotInformationForm form = new SlotInformationForm();

    public ViewTimeTable(){
        ComboBox comboBox = new ComboBox();
        configureComboBox(comboBox);
        updateComboBox(comboBox);
        Label test= new Label();

        grid = new Grid<>(TableEntry.class);
        configureGrid(grid);

        comboBox.addValueChangeListener(valueChangeEvent -> {
            fillGrid(grid,(String) comboBox.getValue(),test);
            //test.setText((String) comboBox.getValue());
        });

        grid.addItemClickListener(evt -> updateForm(evt));
        add(comboBox,grid,form);
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


    private void fillGrid(Grid<TableEntry> grid, String batchInfo, Label test) {
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
                sql = "select courseCode from weekTimetable where batchNo = '" + batchNo + "' and dayNo = '" + days + "';";
                rs = stmt.executeQuery(sql);
                time[days-1] = new TableEntry();
                //TableEntry timetable = new TableEntry();
                iter = 0;
                rs.next();
                while (rs.next()) {
                    test.setText(rs.getString("courseCode"));
                    setSlot(iter++, time[days-1], rs);
                }
                grid.setItems(time[0],time[1],time[2],time[3],time[4]);
                days++;
            }

        }catch (Exception e){
            e.getLocalizedMessage();
        }
    }

    private void setSlot(int iter, TableEntry timetable, ResultSet rs) {
        try{
            if(iter == 0)
                timetable.setSLOT_I(rs.getString("courseCode"));
            else if(iter == 1)
                timetable.setSLOT_II(rs.getString("courseCode"));
            else if(iter == 2)
                timetable.setSLOT_III(rs.getString("courseCode"));
            else if(iter == 3)
                timetable.setSLOT_IV(rs.getString("courseCode"));
            else if(iter == 4)
                timetable.setSLOT_V(rs.getString("courseCode"));
            else if(iter == 5)
                timetable.setSLOT_VI(rs.getString("courseCode"));
            else if(iter == 6)
                timetable.setSLOT_VII(rs.getString("courseCode"));
            else if(iter == 7)
                timetable.setSLOT_VIII(rs.getString("courseCode"));
            else if(iter == 8)
                timetable.setSLOT_IX(rs.getString("courseCode"));
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
