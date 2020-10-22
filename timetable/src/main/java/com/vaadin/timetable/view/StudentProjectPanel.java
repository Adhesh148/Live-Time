package com.vaadin.timetable.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;


public class StudentProjectPanel extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/liveTimetable";
    String user = "dbms";
    String pwd = "Password_123";
    private int projectNo = 0;
    private String downloadFormat = "";

    public StudentProjectPanel(String postedDate, String courseCode,String facultyCode, String title, String desc, String batch, int marks, String dueDate, String dueTime,int teamSize,String topic) {
        addClassName("project-panel");
        Label titleLabel  = new Label(title);
        titleLabel.addClassName("title-project-panel");
        String DueDate[] = dueDate.split("-");
        String monthName = Month.of(Integer.parseInt(DueDate[1])).name();
        monthName = monthName.toLowerCase();
        monthName = StringUtils.capitalize(monthName);
        Icon clipboard = new Icon(VaadinIcon.CLIPBOARD);
        Icon circleIcon = new Icon(VaadinIcon.CIRCLE);
        Icon circleIcon_dupl = new Icon(VaadinIcon.CIRCLE);
        Icon circleIcon_1 = new Icon(VaadinIcon.CIRCLE);
        Icon circleIcon_2 = new Icon(VaadinIcon.CIRCLE);

        clipboard.getStyle().set("width","var(--iron-icon-width, 20px)");
        circleIcon.getStyle().set("width","var(--iron-icon-width, 8px)");
        circleIcon_dupl.getStyle().set("width","var(--iron-icon-width, 8px)");
        circleIcon_1.getStyle().set("width","var(--iron-icon-width, 8px)");
        circleIcon_2.getStyle().set("width","var(--iron-icon-width, 8px)");
        Label message = new Label("Assignment Due "+monthName+" "+DueDate[2]+", "+DueDate[0]+" ");
        Label point = new Label("Points: "+marks);
        Label teams = new Label("Team Size: "+teamSize);
        Label facultyLabel = new Label(facultyCode);
        Label courseLabel = new Label(courseCode);
        HorizontalLayout subheading;
        if(teamSize == 0)
            subheading = new HorizontalLayout(clipboard,message,circleIcon,point);
        else
            subheading = new HorizontalLayout(clipboard,message,circleIcon,point,circleIcon_dupl,teams);
        Label description = new Label(desc);
        description.getStyle().set("padding-bottom","15px");

        //get the the attachment from mysql
        Button download = new Button("Download");

        ByteArrayInputStream inputStream = setAttachment(postedDate,courseCode,facultyCode,title,batch,marks,dueDate,dueTime);
        String fileName = getFileName(projectNo);
        if(fileName == null)
            fileName = "download";

        if(!(downloadFormat.equalsIgnoreCase(""))){
            fileName = fileName +"."+downloadFormat;
        }
        FileDownloadWrapper buttonWrapper = new FileDownloadWrapper(
                new StreamResource(fileName, () -> inputStream));
        buttonWrapper.wrapComponent(download);


        //Add a delete button
        HorizontalLayout buttonLayout = new HorizontalLayout();
        HorizontalLayout courseLayout = new HorizontalLayout(facultyLabel,circleIcon_1,courseLabel);
        buttonLayout.add(buttonWrapper,courseLayout);
        buttonLayout.setWidthFull();
        buttonLayout.getStyle().set("display","flex");
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonLayout.setAlignSelf(Alignment.CENTER,buttonWrapper);
        buttonLayout.setAlignSelf(Alignment.CENTER,courseLayout);
        courseLayout.getStyle().set("margin-left","auto");

        if(hasDownload(postedDate, courseCode, facultyCode, title, batch, marks, dueDate, dueTime) == 0){
            buttonWrapper.setVisible(false);
        }

        add(titleLabel,subheading,new Hr(),description,new Hr(),buttonLayout);

    }

    private String getFileName(int projectNo) {
        String fName = "";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sqlA = "select fileName from attachment  where Pno = "+projectNo+";";
            rs = stmt.executeQuery(sqlA);
            if(rs.next()){
                fName = rs.getString("fileName");
            }
            rs.close();
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

        return fName;
    }

    private int hasDownload(String postedDate, String courseCode, String facultyCode, String title, String batch, int marks, String dueDate, String dueTime) {
        int flag = 0;
        try {
            // Have to work on this.
            String fileName = "download";

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            // Get Pno
            String sql = "select Sno from projectAssign where postedDate ='" + postedDate + "' and courseCode = '" + courseCode + "' and facultyCode = '" + facultyCode + "' and title ='" + title + "' and batchNo = '" + batch + "' and dueDate = '" + dueDate + "' and dueTime = '" + dueTime + "';";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int Pno = rs.getInt("Sno");
            rs.close();
            sql = "select count(*) as cnt from attachment where Pno = "+Pno+";";
            rs = stmt.executeQuery(sql);
            if(rs.next())
                flag = rs.getInt("cnt");
            rs.close();
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

        return flag;
    }

    private ByteArrayInputStream setAttachment(String postedDate, String courseCode, String facultyCode, String title, String batch, int marks, String dueDate, String dueTime) {
        ByteArrayInputStream is = null;
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
            projectNo = Pno;
            rs.close();

            // get the blob
            String sqlA = "select attach,format from attachment  where Pno = "+Pno+";";
            rs = stmt.executeQuery(sqlA);
            if(rs.next()){
                is = (ByteArrayInputStream) rs.getBinaryStream("attach");
                String format = rs.getString("format");
                downloadFormat = format;
                //OutputStream os = new FileOutputStream("/home/adheshreghu/Documents/SEM4/MYSQL/"+fileName+"."+format);
            }
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
        return is;
    }
}
