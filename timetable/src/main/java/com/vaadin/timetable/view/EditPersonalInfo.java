package com.vaadin.timetable.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.timetable.security.MyUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PageTitle("Personal Information | Timetable")
@Route(value = "personal_info",layout = MainView.class)
public class EditPersonalInfo extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/liveTimetable";
    String user = "dbms";
    String pwd = "Password_123";

    public EditPersonalInfo(){
        H3 heading  = new H3("Edit your Personal Information");
        Label subhead = new Label("\"What\'s in a name? That which we call a rose\n By any other name would smell as sweet\"          William Shakespeare");
        subhead.addClassName("quote");
        subhead.setWidth("330px");
        Label guideline = new Label("Feel free  to choose a username and password of your choice");
        guideline.addClassName("guideline");

        FormLayout currUserDetailsForm = createCurUserUI();
        FormLayout changeUserNameForm = createChangeUserUI();
        FormLayout changePasswordForm = createChangePassUI();
        currUserDetailsForm.setVisible(false);
        changeUserNameForm.setVisible(false);
        changePasswordForm.setVisible(false);

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignSelf(Alignment.CENTER,currUserDetailsForm);
        setAlignSelf(Alignment.CENTER,changeUserNameForm);
        setAlignSelf(Alignment.CENTER,changePasswordForm);

        Tab currUserTab = new Tab("Current User Details");
        Tab changeUserNameTab = new Tab("Change Username");
        Tab changePassTab = new Tab("Change Password");

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(currUserTab,currUserDetailsForm);
        tabsToPages.put(changeUserNameTab,changeUserNameForm);
        tabsToPages.put(changePassTab,changePasswordForm);
        Tabs tabs = new Tabs(false,currUserTab,changeUserNameTab,changePassTab);
        Div pages = new Div(currUserDetailsForm,changeUserNameForm,changePasswordForm);
        Set<Component> pagesShown = Stream.of(currUserDetailsForm)
                .collect(Collectors.toSet());

        tabs.setSizeFull();
        tabs.setFlexGrowForEnclosedTabs(1);

        setAlignSelf(Alignment.CENTER,pages);

        tabs.addSelectedChangeListener(event -> {
            pagesShown.forEach(page -> page.setVisible(false));
            pagesShown.clear();
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
            pagesShown.add(selectedPage);
        });

        add(heading,guideline,subhead,new Hr(),tabs,pages);
    }

    private FormLayout createChangePassUI() {
        FormLayout formLayout = new FormLayout();
        Label guideline = new Label("Choose a strong password.");
        guideline.addClassName("password-guideline");

        Label message = new Label("Changing your password will sign you out. You will need to enter new password to sign in.");
        message.setMaxWidth("450px");
        ValidPasswordField newPassword = new ValidPasswordField();
        newPassword.setPlaceholder("New Password");
        Label strength = new Label("Password Strength");
        Label strengthMessage = new Label("Use at least 8 characters. Don’t use something too obvious like your name.");
        strengthMessage.setMaxWidth("450px");
        ValidPasswordField rePassword = new ValidPasswordField();
        rePassword.setPlaceholder("Confirm new Password");
        Button changePass = new Button("CHANGE PASSWORD");
        configure(newPassword,rePassword);
        changePass.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        //set Styles
        newPassword.setMaxWidth("450px");
        rePassword.setMaxWidth("450px");
        rePassword.getStyle().set("padding-bottom","20px");
        changePass.setMaxWidth("200px");
        changePass.getStyle().set("margin-bottom","20px");
        newPassword.getStyle().set("padding-bottom","20px");
        strengthMessage.getStyle().set("max-width","270px");
        strengthMessage.getStyle().set("font-size","0.9em");
        strengthMessage.getStyle().set("margin-bottom","10px");

        formLayout.add(guideline,message,newPassword,strength,strengthMessage,rePassword,changePass);
        formLayout.setMaxWidth("500px");
        formLayout.addClassName("password-form");

        //add action to button
        changePass.addClickListener(buttonClickEvent -> {
            if(newPassword.getValue().length()>=8 && newPassword.getValue().equals(rePassword.getValue())){
                onPasswordChange(newPassword.getValue());
                UI.getCurrent().getPage().setLocation(String.valueOf("/logout"));
            }
        });

        return formLayout;
    }

    private void onPasswordChange(String newPassword) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            int userId=0;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof MyUserDetails) {
                userId = ((MyUserDetails) principal).getId();
            }
            String sql = "update user set password = '"+newPassword+"' where id = "+userId+";";
            int rs = stmt.executeUpdate(sql);
            if(rs>0){
                Notification.show("Password Successfully Updated",2000, Notification.Position.MIDDLE);
            }
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void configure(ValidPasswordField newPassword, ValidPasswordField rePassword) {
        newPassword.setMinLength(8);
        newPassword.setMinLength(8);

        newPassword.addValidator(new StringLengthValidator("Insufficient Length",8,20));

        rePassword.addValidator(
                s -> s.equals(newPassword.getValue()), "Passwords do not match.");

    }

    private FormLayout createChangeUserUI() {
        FormLayout formLayout = new FormLayout();
        Label guideline = new Label("Pick your own username.");
        guideline.addClassName("password-guideline");

        Label message = new Label("Changing your username will sign you out. You will need to enter new username to sign in.");
        message.setMaxWidth("450px");

        TextField newUserName = new TextField();
        newUserName.setPlaceholder("Enter your new username");
        newUserName.setMaxWidth("450px");

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignSelf(Alignment.CENTER,newUserName);

        Button changeUsername = new Button("CHANGE USERNAME");
        changeUsername.setMaxWidth("200px");
        changeUsername.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Label warning  = new Label("Username already exists.");
        warning.getStyle().set("color","red");

        formLayout.add(guideline,message,warning,newUserName);
        warning.setVisible(false);

        newUserName.setValueChangeMode(ValueChangeMode.LAZY);
        newUserName.addValueChangeListener(evt -> {
            if(isValid(newUserName.getValue()) == false){
                warning.setVisible(true);
                newUserName.setInvalid(true);

            }else {
                warning.setVisible(false);
                newUserName.setInvalid(false);
            }
        });

        //Add margins
        message.getStyle().set("margin-bottom","20px");
        newUserName.getStyle().set("margin-bottom","20px");
        changeUsername.getStyle().set("margin-bottom","20px");

        //add Click listener
        changeUsername.addClickListener(evt->{
            if(warning.isVisible() == false){
                onChangeUsername(newUserName.getValue());
                UI.getCurrent().getPage().setLocation(String.valueOf("/logout"));
            }
        });

        formLayout.add(changeUsername);
        formLayout.setMaxWidth("500px");
        formLayout.addClassName("password-form");

        return formLayout;
    }

    private void onChangeUsername(String newUsername) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con  = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            int userId=0;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof MyUserDetails) {
                userId = ((MyUserDetails) principal).getId();
            }
            String sql = "update user set userName = '"+newUsername+"' where id = "+userId+";";
            int rs = stmt.executeUpdate(sql);
            if(rs>0){
                Notification.show("Username Successfully Updated",2000, Notification.Position.MIDDLE);
            }
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private boolean isValid(String value) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "select count(*) as cnt from user where username = '"+value+"';";
            ResultSet rst = stmt.executeQuery(sql);
            rst.next();
            int cnt = rst.getInt("cnt");
            if(cnt>0)
                return false;
            rst.close();
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }

        return true;
    }

    private FormLayout createCurUserUI() {
        TextField username = new TextField();
        PasswordField password = new PasswordField();
        TextField batchCode = new TextField();
        TextField year = new TextField();
        TextField access = new TextField();

        // Give padding to text Field
        username.addClassName("space-below");
        password.addClassName("space-below");
        batchCode.addClassName("space-below");
        username.addClassName("space-below");
        year.addClassName("space-below");
        access.addClassName("space-below");

        setCurrentValues(username,password,batchCode,year,access);
        configure(batchCode,year,access,password,username);

        Label formHeading = new Label("Current User Details");
        formHeading.addClassName("bolden");

        FormLayout formLayout = new FormLayout();
        formLayout.addFormItem(username,"User Name");
        formLayout.addFormItem(password,"Password");
        formLayout.addFormItem(batchCode,"Batch Code");
        formLayout.addFormItem(year,"Year");
        formLayout.addFormItem(access,"Access Level");
        formLayout.setMaxWidth("500px");

        formLayout.addClassName("userDetails-form");

        return formLayout;
    }

    private void configure(TextField batchCode, TextField year, TextField access,PasswordField password,TextField username) {
        batchCode.setEnabled(false);
        year.setEnabled(false);
        access.setEnabled(false);
    }

    private void setCurrentValues(TextField username, PasswordField password, TextField batchCode, TextField year, TextField access) {
        int batchNo = 0;
        String userName = "",passWord = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userName = ((MyUserDetails) principal).getUsername();
            passWord = ((MyUserDetails) principal).getPassword();
            batchNo = ((MyUserDetails) principal).getBatchNo();
            Collection<? extends GrantedAuthority> authorities = ((MyUserDetails) principal).getAuthorities();
            String s = authorities.stream().map(Object::toString).collect(Collectors.joining(","));
            if(s.equalsIgnoreCase("USER"))
                access.setValue("Student");
            else if(s.equalsIgnoreCase("ADMIN"))
                access.setValue("Administrator");

            username.setValue(userName);
            password.setValue(passWord);
        }
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql = "select batchCode,year from batch where batchNo = "+batchNo+";";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                batchCode.setValue(rs.getString("batchCode"));
                year.setValue(rs.getString("year").split("-")[0]);
            }
            rs.close();
            con.close();
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }
}
