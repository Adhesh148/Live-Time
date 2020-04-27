package com.vaadin.timetable;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.timetable.backend.CourseEntry;
import com.vaadin.timetable.backend.DashboardProjectGrid;
import com.vaadin.timetable.backend.DashboardScheduleGrid;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;


@PageTitle("Dashboard | Timetable")
@Route(value = "dashboard",layout = MainView.class)


public class DashboardView extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";

    Grid<DashboardScheduleGrid> scheduleGrid = new Grid<>(DashboardScheduleGrid.class);
    Grid<DashboardProjectGrid> projectGrid = new Grid<>(DashboardProjectGrid.class);

    public DashboardView(){
        H2 heading  = new H2("Dashboard");
        add(heading);

        // Add three panels
        dashboardPanel panel1 = new dashboardPanel(25,"Classes Scheduled");
        dashboardPanel panel2 = new dashboardPanel(50,"Classes Cancelled");
        dashboardPanel panel3 = new dashboardPanel(10,"Projects Assigned");
        dashboardPanel panel4 = new dashboardPanel(100,"Visitors");
        HorizontalLayout cards = new HorizontalLayout(panel1,panel2,panel3,panel4);
        cards.addClassName("dashboard-card");
        add(cards);

        // Add two grids - scheduled/cancelled classes and projects assigned

        //First Grid
        H4 grid_1 = new H4("Recent Schedules/Cancellations");
        configureScheduleGrid(scheduleGrid);
        fillScheduleGrid();
        add(new VerticalLayout(grid_1,scheduleGrid));

        //Second Grid
        H4 grid_2 = new H4("Recent Projects");
        configureProjectGrid();
        fillProjectGrid();
        add(new VerticalLayout(grid_2,projectGrid));


    }

    private void fillProjectGrid() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "select Sno,postedDate,courseCode,facultyCode,title,batchNo,dueDate,dueTime from projectAssign order by postedDate desc;";
            ResultSet rs = stmt.executeQuery(sql);
            Collection<DashboardProjectGrid> data = new ArrayList<>();
            while(rs.next()){
                DashboardProjectGrid entry = new DashboardProjectGrid();
                entry.setPno(rs.getInt("Sno"));
                entry.setCourseCode(rs.getString("courseCode"));
                entry.setFaculty(rs.getString("facultyCode"));
                entry.setDate(rs.getString("postedDate"));
                entry.setTitle(rs.getString("title"));
                //get batch
                int batchNo = rs.getInt("batchNo");
                Statement stmtA = con.createStatement();
                String sqlA = "select batchCode,year from batch where batchNo = "+batchNo+";";
                ResultSet rst = stmtA.executeQuery(sqlA);
                rst.next();
                String batch = rst.getString("batchCode")+" "+rst.getString("year").split("-")[0];
                rst.close();
                entry.setBatch(batch);

                entry.setDue(rs.getString("dueDate")+" "+ rs.getString("dueTime"));
                data.add(entry);
            }
            rs.close();
            projectGrid.setItems(data);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

    }

    private void configureProjectGrid() {
        projectGrid.setHeightByRows(true);
        projectGrid.setSortableColumns();

        projectGrid.setColumnOrder(projectGrid.getColumnByKey("pno"),projectGrid.getColumnByKey("date"),projectGrid.getColumnByKey("batch"),
                projectGrid.getColumnByKey("courseCode"),projectGrid.getColumnByKey("faculty"),projectGrid.getColumnByKey("title"),projectGrid.getColumnByKey("due"));
        
    }

    private void fillScheduleGrid() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "select Sno,date,batchNo,courseCode,facultyCode,flag from updateTimetable;";
            ResultSet rs = stmt.executeQuery(sql);
            Collection<DashboardScheduleGrid> data = new ArrayList<>();
            while(rs.next()){
                DashboardScheduleGrid entry = new DashboardScheduleGrid();
                entry.setSno(rs.getInt("Sno"));
                entry.setCourseCode(rs.getString("courseCode"));
                entry.setFaculty(rs.getString("facultyCode"));
                entry.setDate(rs.getString("date"));
                entry.setFlag(rs.getString("flag"));
                int batchNo = rs.getInt("batchNo");
                Statement stmtA = con.createStatement();
                String sqlA = "select batchCode,year from batch where batchNo = "+batchNo+";";
                ResultSet rst = stmtA.executeQuery(sqlA);
                rst.next();
                String batch = rst.getString("batchCode")+" "+rst.getString("year").split("-")[0];
                rst.close();
                entry.setBatch(batch);
                data.add(entry);
            }
            rs.close();
            scheduleGrid.setItems(data);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

    }

    private void configureScheduleGrid(Grid<DashboardScheduleGrid> scheduleGrid) {
        scheduleGrid.setColumnOrder(scheduleGrid.getColumnByKey("sno"),scheduleGrid.getColumnByKey("date"),scheduleGrid.getColumnByKey("batch"),
                scheduleGrid.getColumnByKey("courseCode"),scheduleGrid.getColumnByKey("faculty"),scheduleGrid.getColumnByKey("flag"));

        scheduleGrid.setSortableColumns();
        scheduleGrid.setHeightByRows(true);
    }

}
