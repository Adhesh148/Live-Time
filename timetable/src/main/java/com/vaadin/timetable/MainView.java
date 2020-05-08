package com.vaadin.timetable;

import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.material.Material;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Route("")
@CssImport("./styles/shared-styles.css")
//@Theme(value = Lumo.class,variant = Lumo.DARK)
public class MainView extends AppLayout {
    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";

    public MainView(){
        //First get the user details and set Drawer accordingly
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        String role = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            role = ((UserDetails) principal).getAuthorities().toString();
            role = role.substring(1,role.length()-1);
        }

        createHeader(username);
        createDrawer(role);
    }


    private void createDrawer(String role) {
        //--admin links --
        RouterLink course = new RouterLink("Course",CourseView.class);
        RouterLink faculty = new RouterLink("Faculty",FacultyView.class);
        RouterLink batch = new RouterLink("Batch",BatchView.class);
        RouterLink mailingList = new RouterLink("Mailing List",MailingGrid.class);
        RouterLink students = new RouterLink("Students",AdminStudentRecord.class);
        RouterLink personalInfo = new RouterLink("Personal Information",EditPersonalInfo.class);
        RouterLink dashboard = new RouterLink("Dashboard",DashboardView.class);
        RouterLink adminLiveView = new RouterLink("Live View",ViewTimeTable.class);
        RouterLink adminProject = new RouterLink("Project",ProjectView.class);
        RouterLink adminReport = new RouterLink("View Report",AdminReportView.class);

        // --- add class Names -------
        course.addClassName("router-link");
        faculty.addClassName("router-link");
        batch.addClassName("router-link");
        mailingList.addClassName("router-link");
        students.addClassName("router-link");
        personalInfo.addClassName("router-link");
        dashboard.addClassName("router-link");
        adminLiveView.addClassName("router-link");
        adminProject.addClassName("router-link");
        adminReport.addClassName("router-link");

        //--user links ---
        RouterLink feedback = new RouterLink("Feedback",StudentReportView.class);
        RouterLink studentDashboard = new RouterLink("Dashboard",StudentDashboard.class);
        RouterLink studentLiveView = new RouterLink("Live View",StudentTimetable.class);
        RouterLink courseAbbrv = new RouterLink("Course Abbreviation",StudentAbbreviation.class);
        RouterLink studentProject = new RouterLink("Project",StudentProjectView.class);

        // --- add class Names -------
        feedback.addClassName("router-link");
        studentDashboard.addClassName("router-link");
        studentLiveView.addClassName("router-link");
        courseAbbrv.addClassName("router-link");
        studentProject.addClassName("router-link");


        if(role.equals("ADMIN")) {
            Accordion accordion = new Accordion();
            VerticalLayout db_children = new VerticalLayout();
            db_children.add(course, faculty,batch,
                    mailingList,students);
            accordion.add("Database", db_children);
            Accordion userDetails = new Accordion();
            VerticalLayout accordion_children = new VerticalLayout();
            accordion_children.add(personalInfo);
            userDetails.add("Account",accordion_children);
            addToDrawer(new VerticalLayout(dashboard, adminLiveView,
                    adminProject, accordion,userDetails,adminReport));
        }else if(role.equals("USER")){
            Accordion userDetails = new Accordion();
            VerticalLayout accordion_children = new VerticalLayout();
            accordion_children.add(personalInfo,feedback);
            userDetails.add("Account",accordion_children);
            addToDrawer(new VerticalLayout(studentDashboard,studentLiveView,
                  courseAbbrv,studentProject,userDetails));
        }
    }

    private void createHeader(String userName) {
        H1 logo = new H1("Live Timetable");
        logo.addClassName("logo");

        Label username = new Label("Hello "+userName);
        username.addClassName("username");

        ToggleButton themeToggle = new ToggleButton();
        //themeToggle.setLabel("Change Mode");

        themeToggle.addClickListener(toggleButtonClickEvent -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();
            if(themeList.contains(Lumo.DARK)){
                themeList.remove(Lumo.DARK);
            }else {
                themeList.add(Lumo.DARK);
            }
        });


        MenuBar menuBar = new MenuBar();
        MenuItem user = menuBar.addItem(new Icon(VaadinIcon.USER));
        Label name = new Label("Hi "+userName);
        name.addClassName("setVisible");
        user.getSubMenu().addItem(name).setEnabled(false);
        user.getSubMenu().add(new Hr());
        user.getSubMenu().addItem("Edit Personal Info",e-> UI.getCurrent().navigate("personal_info"));
        user.getSubMenu().addItem(new Anchor("/logout","Sign Out"));
        HorizontalLayout header =  new HorizontalLayout(new DrawerToggle(),logo);
        header.addClassName("header");
        header.setWidth("90%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        themeToggle.getStyle().set("padding-left","30px");
        addToNavbar(header,menuBar,themeToggle);
    }




}
