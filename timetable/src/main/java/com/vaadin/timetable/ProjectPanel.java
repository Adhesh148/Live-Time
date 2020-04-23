package com.vaadin.timetable;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProjectPanel extends VerticalLayout {

    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";

    public ProjectPanel(String postedDate, String courseCode,String facultyCode, String title, String desc, String batch, int marks, String dueDate, String dueTime) {
        Accordion panel = new Accordion();
        addClassName("project-panel");
        Label posted = new Label("Posted on");
        posted.addClassName("bolden");
        Label description = new Label(desc);
        Label dueTitle = new Label("Deadline");
        dueTitle.addClassName("bolden");
        Label due = new Label(dueDate + " "+ dueTime);

        //Add a delete button
        Button delete  = new Button("Delete",VaadinIcon.MINUS.create());
        Button edit  = new Button("Edit",VaadinIcon.EDIT.create());
        panel.add(title,new VerticalLayout(new HorizontalLayout(posted,new Label(postedDate)),new HorizontalLayout(dueTitle,due),description,new HorizontalLayout(delete,edit)));
        panel.close();
        add(panel);

        // Add Click Listeners
        delete.addClickListener(evt -> {
            onDelete(postedDate,courseCode,facultyCode,title,batch,dueDate,dueTime);
        });

        edit.addClickListener(evt -> {
            onEdit(postedDate,courseCode,facultyCode,title,desc,batch,marks,dueDate,dueTime);
        });
    }

    private void onEdit(String postedDate, String courseCode, String facultyCode, String title, String desc, String batch, int marks, String dueDate, String dueTime) {
    }

    private void onDelete(String postedDate, String courseCode, String facultyCode, String title, String batch, String dueDate, String dueTime) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            //Get Sno from projectAssign table to delete data from attach table as well
            String sql = "Select Sno from projectAssign where postedDate ='"+postedDate+"' and courseCode = '"+courseCode+"' and facultyCode = '"+facultyCode+"' and title ='"+title+"' and batchNo = '"+batch+"' and dueDate = '"+dueDate+"' and dueTime = '"+dueTime+"';";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int Pno = rs.getInt("Sno");
            rs.close();

            String sql2 = "delete from attachment where Pno = "+Pno+";";
            Statement stmtA  = con.createStatement();
            int rstA = stmtA.executeUpdate(sql2);

            //Let us delete from projectAssign table
            String sql1 = "delete from projectAssign where Sno = "+Pno+";";
            int rst = stmt.executeUpdate(sql1);
            if(rst>0){
                Notification.show("Deletion successful",2000, Notification.Position.MIDDLE);
                UI.getCurrent().getPage().reload();
            }

        }catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }

    }
}
