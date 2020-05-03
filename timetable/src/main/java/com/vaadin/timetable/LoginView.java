package com.vaadin.timetable;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Collections;

@Route("login")
@PageTitle("Login | Vaadin CRM")
public class LoginView extends VerticalLayout {
    LoginForm login = new LoginForm();

    public LoginView(){
        login.setAction("login");
        login.setVisible(true);
        add(login);
    }


}
