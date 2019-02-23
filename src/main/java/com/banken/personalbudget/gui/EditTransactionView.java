package com.banken.personalbudget.gui;

import com.banken.personalbudget.Common;
import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;

public class EditTransactionView {
    private static final String SUGGESTED_TAG_LABEL = "Suggested tag: ";

    private Transaction transaction;
    private Data data;
    private List<String> allTags;
    private Storer storer;

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void setData(Data data) {
        this.data = data;
        allTags = data.getAllTags();
    }

    public Node getView() {
        VBox vbox = new VBox();
        ObservableList<Node> children = vbox.getChildren();

        List<String> staticInfo = Arrays.asList(transaction.getOtherParty(),
                transaction.getTransactionDate(),
                transaction.getAmount(),
                transaction.getBank(),
                transaction.getAccountName(),
                transaction.getOwner());
        staticInfo.forEach(info -> {
            String labelinfo = info == null ? "" : info;
            children.add(new Text(labelinfo));
        });

        TextArea description = new TextArea();
        description.setWrapText(true);
        description.setPromptText("Description");
        children.add(description);

        ComboBox<String> tag = new ComboBox<>();
        tag.setEditable(true);
        tag.getItems().addAll(allTags);

        if (Common.isEmpty(transaction.getTag())) {
            String suggestedTag = data.getLatestTagResolver().resolve(transaction.getOtherParty());
            if (suggestedTag != null) {
                tag.setValue(SUGGESTED_TAG_LABEL + suggestedTag);
            } else {
                tag.setPromptText("tag");
            }
        } else {
            tag.setValue(transaction.getTag());
        }
        children.add(tag);

        CheckBox treatAsIncomeCheckBox = new CheckBox("Treat as income");
        if (transaction.isNewTransaction()) {
            Boolean suggestedTreatAsIncome = data.getLatestTreatAsIncomeResolver().resolve(transaction.getOtherParty());
            if (suggestedTreatAsIncome == null) {
                System.out.println("suggestedTreatAsIncome = " + suggestedTreatAsIncome);
            }
            treatAsIncomeCheckBox.setSelected(suggestedTreatAsIncome);
        } else {
            treatAsIncomeCheckBox.setSelected(transaction.isTreatAsIncome());
        }
        children.add(treatAsIncomeCheckBox);

        CheckBox ignoreEntryCheckBox = new CheckBox("Ignore entry");
        children.add(ignoreEntryCheckBox);

        Button saveButton = new Button("Sa_ve");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String tagString = tag.getEditor().getText();
                if (tagString.startsWith(SUGGESTED_TAG_LABEL)) {
                    tagString = tagString.replaceAll(SUGGESTED_TAG_LABEL, "");
                }
                System.out.println("tagString = " + tagString);

                if (!allTags.contains(tagString) && !Common.isEmpty(tagString)) {
                    allTags.add(tagString);
                }
                if (ignoreEntryCheckBox.isSelected()) {
                    transaction.setIgnore(true);
                }
                transaction.setTag(tagString);
                transaction.setDescription(description.getText());
                transaction.setTreatAsIncome(treatAsIncomeCheckBox.isSelected());
                transaction.setNewTransaction(false);
                storer.store();
            }
        });
        children.add(saveButton);

        return vbox;
    }

    public void setStorer(Storer storer) {
        this.storer = storer;
    }
}
