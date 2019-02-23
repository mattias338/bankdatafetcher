package com.banken.personalbudget.gui;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class BodyOfMainScene {
    private VBox outer;
    private Node currentBody;

    public void setOuter(VBox outer) {
        this.outer = outer;
    }

    public void setBody(Node body) {
        if (currentBody != null) {
            outer.getChildren().remove(currentBody);
        }
        outer.getChildren().add(body);
        currentBody = body;
    }
}
