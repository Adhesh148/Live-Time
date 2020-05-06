package com.vaadin.timetable;

import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.sql.*;
import java.time.LocalDate;

@PageTitle("Student Project | Timetable")
@Route(value = "student_project",layout = MainView.class)
public class StudentProjectView  extends VerticalLayout{
    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";
    LocalDate today = LocalDate.now();
    int dayOfWeek = today.getDayOfWeek().getValue();
    LocalDate Sun = today.plusDays(-1*dayOfWeek);
    LocalDate Sat = Sun.plusDays(6);

    public StudentProjectView(){
        Label heading  = new Label("Assignment Manager");
        heading.addClassName("project-view-heading");
        add(new HorizontalLayout(heading));

        updateProjectPanels();
    }

    private void updateProjectPanels() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sql = "select postedDate,courseCode,facultyCode,title,description,batchNo,marks,dueDate,dueTime,teamSize,topic from projectAssign where dueDate>='"+Sun+"' and dueDate<='"+Sat+"' order by dueDate desc;";
            rs = stmt.executeQuery(sql);
            Label subhead = new Label("Due this Week");
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
                StudentProjectPanel panel = new StudentProjectPanel(postedDate,courseCode,facultyCode,title,desc,batchNo,marks,dueDate,dueTime,teamSize,topic);
                add(panel);
            }
            rs.close();
            Label subhead_new = new Label("Posted this Week");
            subhead_new.addClassName("project-division-headings");
            add(new VerticalLayout(subhead_new,new Hr()));
            sql = "select postedDate,courseCode,facultyCode,title,description,batchNo,marks,dueDate,dueTime,teamSize,topic from projectAssign where postedDate>='"+Sun+"' and postedDate<='"+Sat+"' order by postedDate desc;";
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
                StudentProjectPanel panel = new StudentProjectPanel(postedDate,courseCode,facultyCode,title,desc,batchNo,marks,dueDate,dueTime,teamSize,topic);
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
                StudentProjectPanel panel = new StudentProjectPanel(postedDate,courseCode,facultyCode,title,desc,batchNo,marks,dueDate,dueTime,teamSize,topic);
                add(panel);
            }
            rs.close();
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }
}
