package com.banken.personalbudget.gui;

import com.banken.personalbudget.Common;
import com.banken.personalbudget.JsonFileStorage;
import com.banken.personalbudget.Storage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxGui extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setFullScreen(true);

        MainSceneProvider mainSceneProvider = new MainSceneProvider();

        mainSceneProvider.setStorage(getStorage());
        Scene startScene = mainSceneProvider.getScene();

        primaryStage.setScene(startScene);

        primaryStage.show();
    }

    protected Storage getStorage() {
        return new JsonFileStorage(Common.getDataPath());
    }
}
