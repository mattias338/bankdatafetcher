package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewTransactionGuiElements {
    private NewTransactionManager newTransactionManager;
    private TextInputControl description;
    private List<Text> staticTexts;
    private ComboBox<Object> tag;
    private CheckBox treatAsIncomeCheckBox;
    private CheckBox ignoreEntryCheckBox;
    private Button saveButton;

    public NewTransactionGuiElements(NewTransactionManager newTransactionManager) {
        this.newTransactionManager = newTransactionManager;

        createStaticTexts();
        createTagComboBox();
        createTreatAsIncomeCheckBox();
        createIgnoreEntryCheckBox();
        createSaveButton();
    }

    private void createStaticTexts() {
        List<String> staticValues = newTransactionManager.getTransaction().getStaticValues();
        ArrayList<String> copy = new ArrayList<>(staticValues);
        String newText = newTransactionManager.getTransaction().isNewTransaction() ?
                "New transaction: All input fields are suggestions." :
                "Saved Transaction";
        copy.add(0, newText);
        staticTexts = copy.stream().
                map(s -> s == null ? "" : s).
                map(Text::new).collect(Collectors.toList());
    }

    public void setDescriptionTextInputControl(TextInputControl textInputControl) {
        this.description = textInputControl;
    }

    private void createTagComboBox() {
        tag = new ComboBox<>();
        tag.setEditable(true);
        Data data = newTransactionManager.getData();
        List<String> allTags = data.getAllTags();
        tag.getItems().addAll(allTags);
        tag.setPromptText("tag");
        tag.setValue(newTransactionManager.getTag());
    }

    private void createTreatAsIncomeCheckBox() {
        treatAsIncomeCheckBox = new CheckBox("Treat as income");
        treatAsIncomeCheckBox.setSelected(newTransactionManager.isTreatAsIncome());
    }

    private void createIgnoreEntryCheckBox() {
        ignoreEntryCheckBox = new CheckBox("Ignore entry");
        ignoreEntryCheckBox.setSelected(newTransactionManager.isIgnore());
    }


    private void createSaveButton() {
        saveButton = new Button("Sa_ve");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                save();
            }
        });
    }

    public void save() {
        Transaction transaction = newTransactionManager.getTransaction();
        String tagString = tag.getEditor().getText();
        if (ignoreEntryCheckBox.isSelected()) {
            transaction.setIgnore(true);
        }
        transaction.setTag(tagString);
        transaction.setDescription(description.getText());
        transaction.setTreatAsIncome(treatAsIncomeCheckBox.isSelected());
        transaction.setNewTransaction(false);
        newTransactionManager.save();
    }


    public List<Text> getStaticTexts() {
        return staticTexts;
    }

    public ComboBox<Object> getTag() {
        return tag;
    }

    public CheckBox getTreatAsIncomeCheckBox() {
        return treatAsIncomeCheckBox;
    }

    public CheckBox getIgnoreEntryCheckBox() {
        return ignoreEntryCheckBox;
    }

    public Button getSaveButton() {
        return saveButton;
    }
}
