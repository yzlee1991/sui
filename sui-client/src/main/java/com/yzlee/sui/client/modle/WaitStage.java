package com.yzlee.sui.client.modle;

import com.yzlee.sui.client.fx.Main;
import javafx.beans.property.ReadOnlyProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * @Author: yzlee
 * @Date: 2019/2/14 16:38
 */
public class WaitStage extends Stage {

    Label label;

    public WaitStage(Stage primaryStage, String message) throws IOException {
        if (primaryStage == null) {
            // 抛异常
        }
        VBox wait = FXMLLoader.load(Main.class.getResource("/view/wait.fxml"));
        Scene scene = new Scene(wait);
        scene.setFill(null);
        setScene(scene);
        initOwner(primaryStage);
        initStyle(StageStyle.TRANSPARENT);
        initModality(Modality.APPLICATION_MODAL);
        setWidth(primaryStage.getWidth());
        setHeight(primaryStage.getHeight());
        setX(primaryStage.getX());
        setY(primaryStage.getY());
        label = (Label) wait.lookup("#title");
        label.setText(message);
    }

    public void bindProperty(ReadOnlyProperty readOnlyProperty) {
        if (readOnlyProperty != null) {
            label.textProperty().bind(readOnlyProperty);
        }
    }

}
