package com.vaadin.timetable;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("ViewProject | Timetable")
@Route(value = "project",layout = MainView.class)
public class ProjectView extends VerticalLayout {
    Button create = new Button("Create", VaadinIcon.PLUS.create());
    Button delete = new Button("Delete",VaadinIcon.MAGIC.create());


    public ProjectView(){
        create.addClassName("project-create");
        delete.addClassName("project-delete");
        create.addClickListener(buttonClickEvent -> {
            addNewProject();
        });
        add(new HorizontalLayout(create,delete));

        ProjectPanel panel1 = new ProjectPanel();
        ProjectPanel panel2 = new ProjectPanel();
        ProjectPanel panel[] = new ProjectPanel[10];

        add(panel1,panel2);
        for(int i=0;i<10;++i) {
            panel[i] = new ProjectPanel();
            add(panel[i]);
        }
    }

    private void addNewProject() {
        Dialog project = new Dialog();

        SplitLayout outer_layout = new SplitLayout();
        SplitLayout inner_layout = new SplitLayout();
        inner_layout.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        outer_layout.setOrientation(SplitLayout.Orientation.VERTICAL);

        project.setHeight("calc(90vh - (2*var(--lumo-space-m)))");
        project.setWidth("calc(100vw - (4*var(--lumo-space-m)))");

        //Primary of inner layout
        Label heading = new Label("Add A Project");
        heading.addClassName("project-add-heading");

        //Secondary split of outer layout
        ComboBox facultyCode = new ComboBox("Faculty Code");
        ComboBox facultyName = new ComboBox("Faculty Name");
        ComboBox courseCode  = new ComboBox("Course Code");
        ComboBox courseName  = new ComboBox("Course Name");
        ComboBox batch = new ComboBox("Batch");
        TextField marks = new TextField("Marks");
        DatePicker deadlineDate = new DatePicker("Due");
        deadlineDate.setPlaceholder("Pick a date");
        TextField deadlineTime = new TextField();
        deadlineTime.setPlaceholder("HH:MM:SS");
        TextField topic = new TextField("Topic");
        ComboBox teamSize = new ComboBox("Team Size");

        VerticalLayout outerSecondaryLayout = new VerticalLayout();
        HorizontalLayout Due = new HorizontalLayout();
        Due.add(deadlineDate,deadlineTime);
        Due.setAlignSelf(Alignment.END, deadlineTime);
        outerSecondaryLayout.add(new HorizontalLayout(facultyCode,facultyName),new HorizontalLayout(courseCode,courseName),batch,marks,Due,topic,teamSize);

        // Secondary of inner layout
        TextField title = new TextField("Title");
        title.setPlaceholder("Enter a title");
        TextArea desc = new TextArea("Description");
        desc.setPlaceholder("Enter description. (Optional)");
        Button assign = new Button("Assign",VaadinIcon.PLUS.create());

        VerticalLayout innerSecondaryLayout = new VerticalLayout();
        innerSecondaryLayout.add(title,desc,assign);
        inner_layout.addToPrimary(innerSecondaryLayout);
        inner_layout.addToSecondary(outerSecondaryLayout);
        outer_layout.addToPrimary(heading);
        outer_layout.addToSecondary(inner_layout);

        // Have to put media queries for devices.....
        title.setWidth("95%");
        desc.setWidth("95%");
        desc.setHeight("200px");
        facultyCode.setWidth("170px");
        facultyName.setWidth("400px");
        courseCode.setWidth("170px");
        courseName.setWidth("400px");
        topic.setWidth("400px");
        inner_layout.setPrimaryStyle("min-width", "65%");
        inner_layout.setPrimaryStyle("max-width", "65%");
        inner_layout.setSecondaryStyle("min-width", "35%");
        inner_layout.setSecondaryStyle("max-width", "35%");
        inner_layout.setSecondaryStyle("min-height", "100%");
        inner_layout.setSecondaryStyle("max-height", "100%");
        outer_layout.setPrimaryStyle("min-height","5%");
        outer_layout.setPrimaryStyle("max-height","5%");
        outer_layout.setSecondaryStyle("min-height","95%");
        outer_layout.setSecondaryStyle("max-height","95%");
        inner_layout.setSizeUndefined();
        outer_layout.setSizeFull();
        // ------------------------------------------------------------------------


        project.add(outer_layout);
        project.open();

    }
}
