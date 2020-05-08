package com.vaadin.timetable;

import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

@PageTitle("Report View | Timetable")
@Route(value = "report_view",layout = MainView.class)
public class AdminReportView extends VerticalLayout {

    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";

    LocalDate today = LocalDate.now();
    int dayOfWeek = today.getDayOfWeek().getValue();
    LocalDate Sun = today.plusDays(-1*dayOfWeek);
    LocalDate Sat = Sun.plusDays(6);

    public AdminReportView(){
        Label heading  = new Label("Report View");
        Label message = new Label("View Comments/Bugs/Questions reported by users.");
        heading.addClassName("project-view-heading");
        add(heading,message,new Hr());

        updateReportPanels();

    }

    private void updateReportPanels() {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,pwd);
                Statement stmt = con.createStatement();
                String sql = "select id from report where postedDate >='"+Sun+"' and postedDate<='"+Sat+"' order by postedDate desc;";
                ResultSet rs = stmt.executeQuery(sql);
                int count =0;
                Label subhead = new Label("Posted this Week");
                subhead.addClassName("project-division-headings");
                add(new VerticalLayout(subhead,new Hr()));
                while (rs.next()){
                    AdminReportPanel adminReportPanel = new AdminReportPanel(rs.getInt("id"),count++);
                    add(adminReportPanel);
                }
                rs.close();
                Label subhead_older = new Label("Older Posts");
                subhead_older.addClassName("project-division-headings");
                add(new VerticalLayout(subhead_older,new Hr()));
                sql = "select id from report where postedDate <'"+Sun+"' order by postedDate desc;";
                rs = stmt.executeQuery(sql);
                count = 0;
                while (rs.next()){
                    AdminReportPanel adminReportPanel = new AdminReportPanel(rs.getInt("id"),count++);
                    add(adminReportPanel);
                }
                rs.close();
                con.close();
            }catch (Exception e){
                Notification.show(e.getLocalizedMessage());
            }
    }
}
