package com.vaadin.timetable;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.timetable.backend.BatchEntry;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

@Route(value = "batch-view",layout = MainView.class)
@PageTitle("Batch View | Timetable")
public class BatchView extends VerticalLayout {
    String url = "jdbc:mysql://localhost:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";

    Grid<BatchEntry> grid = new Grid<>(BatchEntry.class);
    TextField filterText = new TextField();
    BatchInformationForm form = new BatchInformationForm();

    Icon addNew = new Icon(VaadinIcon.PLUS_CIRCLE);

    Label heading = new Label("Batch Records");
    Label message = new Label("Add,Edit and Delete records of Batches.");

    public BatchView(){
        setClassName("batch-list");
        setSizeFull();

        configureGrid(grid);
        fillBatchGrid();

        configureFilter(filterText);
        Div content = new Div(grid,form);
        content.addClassName("batch-content");
        content.setSizeFull();
        //set form to not be visible unless grid is clicked
        form.setVisible(false);

        HorizontalLayout toolBar = new HorizontalLayout(filterText,addNew);
        toolBar.setAlignSelf(Alignment.CENTER, addNew);

        addNew.addClickListener(evt -> {
            addBatch();
        });

        heading.addClassName("course-abbreviation-heading");
        message.addClassName("course-abbreviation-message");
        add(heading,message,new Hr());
        add(toolBar,content);

    }

    private void addBatch() {
        form.setVisible(true);
        form.batchNo.setValue("");
        form.batchCode.setValue("");
        form.batchName.setValue("");
        form.year.setValue("");
        form.spec.setValue("");

        form.setInformation(null,0);
    }

    private void configureFilter(TextField filterText) {
        filterText.setPlaceholder("Filter by batch name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(evt -> updateList());
    }

    private void updateList() {
        String filter = filterText.getValue();
        grid.setItems();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;

            // Get values from backend matching the filter pattern
            sql = "select * from batch where batchName like '%"+filter+"%';";
            rs = stmt.executeQuery(sql);

            Collection<BatchEntry> data = new ArrayList<>();
            while(rs.next()){
                BatchEntry entry = new BatchEntry();
                entry.setBatchNo(Integer.parseInt(rs.getString("batchNo")));
                entry.setBatchCode(rs.getString("batchCode"));
                entry.setBatchName(rs.getString("batchName"));
                entry.setYear(rs.getString("year"));
                entry.setSpecialization(rs.getString("specialization"));
                data.add(entry);
            }
            grid.setItems(data);


        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    public void fillBatchGrid() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt = con.createStatement();
            String sql;
            ResultSet rs;

            sql = "select * from batch";
            rs = stmt.executeQuery(sql);

            Collection<BatchEntry> data = new ArrayList<>();
            while(rs.next()){
                BatchEntry entry = new BatchEntry();
                entry.setBatchNo(Integer.parseInt(rs.getString("batchNo")));
                entry.setBatchCode(rs.getString("batchCode"));
                entry.setBatchName(rs.getString("batchName"));
                Calendar cal = Calendar.getInstance();
                cal.setTime(rs.getDate("year"));
                entry.setYear(String.valueOf(cal.get(Calendar.YEAR)));
                entry.setSpecialization(rs.getString("specialization"));
                data.add(entry);
            }
            grid.setItems(data);

        }catch(Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

    private void configureGrid(Grid<BatchEntry> grid) {
        grid.getColumnByKey("batchNo").setHeader("Batch No");
        grid.getColumnByKey("batchCode").setHeader("Batch Code");
        grid.getColumnByKey("batchName").setHeader("Batch Name");
        grid.getColumnByKey("year").setHeader("Year");
        grid.getColumnByKey("specialization").setHeader("Specialization");

        //grid.setSizeFull();
        grid.setHeightByRows(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setSortableColumns();

        //Add Value Change Listener to display the form
        grid.asSingleSelect().addValueChangeListener(evt -> editForm(evt.getValue()));

    }

    private void editForm(BatchEntry value) {
        if(value==null){
            closeEditor();
        }else{
            form.setVisible(true);
            form.setInformation(value,1);
            addClassName("batch-editing");

        }
    }

    private void closeEditor() {
        form.setVisible(false);
        removeClassName("batch-editing");
    }



}
