package com.vaadin.timetable.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Month;

public class AdminReportPanel extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/liveTimetable";
    String user = "dbms";
    String pwd = "Password_123";

    String flag = "";
    String report = "";
    String name = "";
    int userId = 0;
    String date = "";

    public AdminReportPanel(int id,int styleId){
        String reportDetails[] = getDetails(id);
        userId = Integer.parseInt(reportDetails[0]);
        report = reportDetails[1];
        flag = reportDetails[2];
        date = reportDetails[3];

        String dateString = date.split(" ")[0];
        String timeString = date.split(" ")[1];

        int monthNo = Integer.parseInt(dateString.split("-")[1]);
        String monthName = Month.of(monthNo).name();
        monthName = monthName.toLowerCase();
        monthName = StringUtils.capitalize(monthName);

        date = dateString.split("-")[2] + " "+monthName+" "+dateString.split("-")[0];
        date = date + " "+timeString;

        String userName = getUsername(userId);

        Label flagLabel = new Label(flag);
        Label reportLabel = new Label(report);
        Label dateLabel = new Label(date);
        Label postedBy = new Label(userName);

        Icon question = new Icon(VaadinIcon.QUESTION);
        Icon bug = new Icon(VaadinIcon.BUG);
        Icon comment = new Icon(VaadinIcon.COMMENT);
        Icon user = new Icon(VaadinIcon.USER);
        Icon clock = new Icon(VaadinIcon.CLOCK);

        VerticalLayout flagLayout = new VerticalLayout();
        VerticalLayout reportLayout = new VerticalLayout(reportLabel);
        VerticalLayout userLayout = new VerticalLayout(new HorizontalLayout(user,postedBy),new HorizontalLayout(clock,dateLabel));

        if(flag.equalsIgnoreCase("C")){
            flagLabel.setText("Comment");
            flagLayout.add(comment,flagLabel);
        }else if(flag.equalsIgnoreCase("B")){
            flagLabel.setText("Bug Report");
            flagLayout.add(bug,flagLabel);
        }else{
            flagLabel.setText("Question");
            flagLayout.add(question,flagLabel);
        }

        HorizontalLayout finalLayout = new HorizontalLayout(flagLayout,reportLayout,userLayout);
        finalLayout.setWidthFull();
        finalLayout.addClassName("admin-report-final-layout");
        addClassName("admin-report-panel");

        flagLayout.addClassName("admin-report-panel-flag");
        reportLayout.addClassName("admin-report-panel-report");
        userLayout.addClassName("admin-report-panel-user");

        flagLayout.getStyle().set("width","10%");
        reportLayout.getStyle().set("width","65%");
        userLayout.getStyle().set("width","25%");

//        if(styleId%2 == 1){
//        }

        add(finalLayout);
    }

    private String getUsername(int userId) {
        String username = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "select userName from user where id = "+userId+";";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                username = rs.getString("userName");
            }
            rs.close();
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

        return username;
    }

    private String[] getDetails(int id) {
        String reportDetails[] = new String[4];
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "select userId,report,flag,postedDate from report where id = "+id+";";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                reportDetails[0] = String.valueOf(rs.getInt("userId"));
                reportDetails[1] = rs.getString("report");
                reportDetails[2] = rs.getString("flag");
                reportDetails[3] = rs.getString("postedDate");
            }
            rs.close();
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
        return reportDetails;
    }
}
