package com.yzlee.sui.client.fx;

import com.yzlee.sui.client.Client;
import com.yzlee.sui.client.modle.WaitStage;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:24
 */
public class Main extends Application {

    Logger logger = LoggerFactory.getLogger(Main.class);

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        // FXMLLoader fxmlLoader=new FXMLLoader();
        AnchorPane ap = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(ap);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResource("/image/meunicon.png").toURI().toString()));
        primaryStage.setResizable(false);
        primaryStage.setTitle("随小僵客户端");
        //退出监听
        primaryStage.setOnCloseRequest(event -> {
            Client.newInstance().exit();
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static WaitStage waitShow(String message) throws IOException {
        return new WaitStage(primaryStage, message);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

}
