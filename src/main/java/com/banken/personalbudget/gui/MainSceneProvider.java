package com.banken.personalbudget.gui;

import com.banken.personalbudget.Storage;
import com.banken.personalbudget.data.Data;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainSceneProvider {

    private Storage storage;
    private Data data;
    private JsonStorer jsonStorer;

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Scene getScene() {
        data = storage.getData();
        jsonStorer = new JsonStorer();


        VBox outer = new VBox();

        HBox top = new HBox();
        outer.getChildren().add(top);

        BodyOfMainScene bodyOfMainScene = new BodyOfMainScene();
        bodyOfMainScene.setOuter(outer);
        bodyOfMainScene.setBody(new Label("Show data!"));



        Button showDataButton = new Button("Show all transactions");
        showDataButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEntriesGridView allEntriesGridView = new AllEntriesGridView(jsonStorer, data);
                bodyOfMainScene.setBody(allEntriesGridView.createView());
            }
        });

        Button addMissingTagsButton = new Button("Finalize new entries");
        addMissingTagsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddMissingTagsView addMissingTagsView = new AddMissingTagsView();
                addMissingTagsView.setData(data);
                addMissingTagsView.setStorer(jsonStorer);
                bodyOfMainScene.setBody(addMissingTagsView.createView());
            }
        });

        Button finalizeNewEntriesMulti = new Button("Finalize new entries (multi)");
        finalizeNewEntriesMulti.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FinalizeNewEntriesMulti finalizeNewEntriesMultiView = new FinalizeNewEntriesMulti(jsonStorer, data);
                bodyOfMainScene.setBody(finalizeNewEntriesMultiView.createView());
            }
        });

        Button monthly = new Button("Monthly");
        monthly.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MonthlyView monthlyView = new MonthlyView();
                monthlyView.setData(data);
                monthlyView.setStorer(jsonStorer);
                Node yearViewView = monthlyView.getView();
                bodyOfMainScene.setBody(yearViewView);
            }
        });


        Button quarterly = new Button("Quarterly");
        quarterly.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                QuarterlyView quarterlyView = new QuarterlyView();
                quarterlyView.setData(data);
                quarterlyView.setStorer(jsonStorer);
                Node yearViewView = quarterlyView.getView();
                bodyOfMainScene.setBody(yearViewView);
            }
        });

        Button arbitrarily = new Button("Arbitrarily");
        arbitrarily.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                QuarterlyView quarterlyView = new QuarterlyView();
                quarterlyView.setCombineAllCompleteMonths(true);
                quarterlyView.setData(data);
                Node yearViewView = quarterlyView.getView();
                bodyOfMainScene.setBody(yearViewView);
            }
        });





        top.getChildren().add(showDataButton);
        top.getChildren().add(addMissingTagsButton);
        top.getChildren().add(finalizeNewEntriesMulti);
        top.getChildren().add(monthly);
        top.getChildren().add(quarterly);
        top.getChildren().add(arbitrarily);

        Scene scene = new Scene(outer);
        return scene;
    }

    class JsonStorer implements Storer {
        @Override
        public void store() {
            storage.storeData(data);
        }
    }
}
