package com.banken.personalbudget.gui;

import com.banken.personalbudget.WrapAroundInt;
import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class AddMissingTagsView {

    private Data data;

    private List<Transaction> transactions;

    private WrapAroundInt transactionIndex = new WrapAroundInt();
    private Storer storer;

    private Node createCurrentTransactionUi() {
        if (!transactions.isEmpty()) {
            Transaction transaction = transactions.get(transactionIndex.getValue());
            return createTransactionUi(transaction);
        }
        return null;
    }

    private Node createTransactionUi(Transaction transaction) {
        EditTransactionView editTransactionView = new EditTransactionView();
        editTransactionView.setTransaction(transaction);
        editTransactionView.setData(data);
        editTransactionView.setStorer(storer);
        return editTransactionView.getView();
//        children.clear();
//        List<String> staticInfo = Arrays.asList(transaction.getOtherParty(),
//                transaction.getTransactionDate(),
//                transaction.getAmount(),
//                transaction.getBank(),
//                transaction.getAccountName(),
//                transaction.getOwner());
//        staticInfo.forEach(info -> {
//            String labelinfo = info == null ? "" : info;
//            children.add(new Text(labelinfo));
//        });
//
//        TextArea description = new TextArea();
//        description.setWrapText(true);
//        children.add(description);
//
//        ComboBox<String> tag = new ComboBox<>();
//        tag.setEditable(true);
//        tag.getItems().addAll(allTags);
//        String suggestedTag = latestTagResolver.resolve(transaction.getOtherParty());
//        String suggestedTagLabel = "Suggested tag: ";
//        if (suggestedTag != null) {
//            tag.setValue(suggestedTagLabel + suggestedTag);
//        } else {
//            tag.setPromptText("tag");
//        }
//        children.add(tag);
//
//        Button applyButton = new Button("Save");
//        applyButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                String tagString = tag.getEditor().getText();
//                if (tagString.startsWith(suggestedTagLabel)) {
//                    tagString = suggestedTag;
//                }
//                System.out.println("tagString = " + tagString);
//
//                if (!allTags.contains(tagString) && !Common.isEmpty(tagString)) {
//                    allTags.add(tagString);
//                }
//                transaction.setTag(tagString);
//                transaction.setDescription(description.getText());
//                storer.store();
//            }
//        });
//        children.add(applyButton);
    }

    public Node createView() {
        if (transactions.isEmpty()) {
            return new Text("No transaction with missing tags found.");
        }
        HBox content = new HBox();
        Scene scene = content.getScene();
        ObservableList<Node> children = content.getChildren();
        Button previousButton = new Button("<-");

        previousButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                transactionIndex.dec();
                updateUi(children);
            }
        });

        Button nextButton = new Button("->");

        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                transactionIndex.inc();
                updateUi(children);
            }
        });


        children.add(previousButton);
        VBox data = new VBox();
        children.add(data);
        children.add(nextButton);

        updateUi(children);

//        previousButton.getScene().getAccelerators().put(
//                new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN),
//                previousButton::fire);
//
//        nextButton.getScene().getAccelerators().put(
//                new KeyCodeCombination(KeyCode.P, KeyCombination.SHORTCUT_DOWN),
//                nextButton::fire);
        return content;
    }

    private void updateUi(ObservableList<Node> children) {
        Node currentTransactionUi = createCurrentTransactionUi();
        if (currentTransactionUi != null) {
            children.set(1, currentTransactionUi);
        }
    }

    public void setData(Data data) {
        this.data = data;
        this.transactions = data.getNewTransactions();
        if (!transactions.isEmpty()) {
            transactionIndex.setMax(transactions.size() - 1);
        }
    }

    public void setStorer(Storer storer) {
        this.storer = storer;
    }
}
