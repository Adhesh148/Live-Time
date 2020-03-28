package com.vaadin.timetable;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;


@Route("")
@CssImport("./styles/shared-styles.css")
public class MainView extends AppLayout {

    public MainView(){
        createHeader();
        createDrawer();

    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(new RouterLink("Dashboard",DashboardView.class),new RouterLink("Live View",ViewTimeTable.class),
                new RouterLink("Faculty",FacultyView.class)));
    }

    private void createHeader() {
        H1 logo = new H1("Live Timetable");
        logo.addClassName("logo");

        Label username = new Label("Hello admin!");
        username.addClassName("username");

        HorizontalLayout header =  new HorizontalLayout(new DrawerToggle(),logo);
        header.addClassName("header");
        header.setWidth("90%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);


        addToNavbar(header,username);
    }




}
