package main.view;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import main.model.TableDetails;
import main.service.BackendService;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "master-detail", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("DynamoDB-to-CSV")
@CssImport("styles/views/masterdetail/master-detail-view.css")
public class MasterDetailView extends Div implements AfterNavigationObserver {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private BackendService service;

    private Grid<TableDetails> employees;

    private TextField tableName = new TextField();
    private TextField columnName = new TextField();
    private TextField columnValue = new TextField();
    private boolean validatedForm = false;

    private Button exportButton = new Button("Export");

    private Binder<TableDetails> binder;

    public MasterDetailView() {
        setId("master-detail-view");
        // Configure Grid
        employees = new Grid<>();
        employees.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        employees.setHeightFull();
        employees.addColumn(TableDetails::getTableName).setHeader("Table Name");

        //when a row is selected or deselected, populate form
        employees.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

        // Configure Form
        binder = new Binder<>(TableDetails.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);
        // note that password field isn't bound since that property doesn't exist in
        // Employee

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        exportButton.addClickListener(e -> {

            String textFieldTable = tableName.getValue();
            String textFieldIColumnName = columnName.getValue();
            String textFieldColumnValue = columnValue.getValue();

            if(textFieldTable.equals("")) {
                Notification.show("Please, select a table.");
                validatedForm = false;
            }
            else if(!textFieldIColumnName.isEmpty()){
                if(textFieldColumnValue.isEmpty()){
                    Notification.show("Please, fill out Column Value or remove Column Name.");
                    validatedForm = false;
                }
                else{
                    validatedForm = true;
                }
            }
            else{
                validatedForm = true;
            }

            if(validatedForm){
                service.callAwsService(textFieldTable, textFieldIColumnName, textFieldColumnValue);
                Notification.show("Exporting finished.");
            }

        });

    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorDiv = new Div();
        editorDiv.setId("editor-layout");
        FormLayout formLayout = new FormLayout();
        addFormItem(editorDiv, formLayout, tableName, "Table Name");
        addFormItem(editorDiv, formLayout, columnName, "Column Name");
        addFormItem(editorDiv, formLayout, columnValue, "Column Value");
        createButtonLayout(editorDiv);
        splitLayout.addToSecondary(editorDiv);
    }

    private void createButtonLayout(Div editorDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        exportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(exportButton);
        editorDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(employees);
    }

    private void addFormItem(Div wrapper, FormLayout formLayout, AbstractField field, String fieldName) {
        formLayout.addFormItem(field, fieldName);
        wrapper.add(formLayout);
        field.getElement().getClassList().add("full-width");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        // Lazy init of the grid items, happens only when we are sure the view will be
        // shown to the user
        employees.setItems(service.getTableList());
    }

    private void populateForm(TableDetails value) {
        // Value can be null as well, that clears the form
        binder.readBean(value);
        tableName.setValue(value.getTableName());
    }
}
