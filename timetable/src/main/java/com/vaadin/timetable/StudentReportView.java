package com.vaadin.timetable;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.timetable.security.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@PageTitle("Report | Timetable | Student")
@Route(value = "report",layout = MainView.class)
public class StudentReportView extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";
    H2 header = new H2("Feedback Form");
    Label subheading = new Label("We would love to hear your thoughts,concerns or problems if any so we can improve your experience.");

    public StudentReportView(){
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("Feedback Type");
        radioGroup.setItems("Comments","Bug Reports","Question");
        radioGroup.setValue("Comments");

        TextArea feedback = new TextArea("Detailed Feedback");
        feedback.setWidthFull();
        feedback.setHeight("400px");
        feedback.setPlaceholder("Enter your question/report/comments here.");
        Button submit = new Button("Submit");
        Button clear = new Button("Clear");
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        clear.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clear.setSizeUndefined();
        submit.setSizeUndefined();

        add(header,subheading,new Hr(),radioGroup,feedback,new HorizontalLayout(submit,clear));

        clear.addClickListener(buttonClickEvent -> {
            feedback.setValue("");
            radioGroup.setValue("Comments");
        });

        submit.addClickListener(buttonClickEvent -> {
            if(feedback.getValue().isEmpty()==false) {
                onSubmit(feedback.getValue(), radioGroup.getValue());
                feedback.setValue("");
            }else
                Notification.show("Feedback cannot be empty",2000, Notification.Position.MIDDLE);
        });

    }

    private void onSubmit(String feedback, String group) {
        int userId=0;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userId =  ((MyUserDetails) principal).getId();
        }
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String datetime = myDateObj.format(myFormatObj);
            String flag= "C";
            if(group.equalsIgnoreCase("Bug Reports"))
                flag = "B";
            else if(group.equalsIgnoreCase("Question"))
                flag = "Q";

            String sql = "insert into report (userId,report,flag,postedDate) values("+userId+",'"+feedback+"','"+flag+"','"+datetime+"')";
            int rs = stmt.executeUpdate(sql);
            if(rs>0)
                Notification.show("Report Successfully Filed.",2000, Notification.Position.MIDDLE);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

    }


}
