package com.banken.personalbudget.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxGui extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setFullScreen(true);

        MainSceneProvider mainSceneProvider = new MainSceneProvider();

        Scene startScene = mainSceneProvider.getScene();

        primaryStage.setScene(startScene);

        primaryStage.show();
    }
}
