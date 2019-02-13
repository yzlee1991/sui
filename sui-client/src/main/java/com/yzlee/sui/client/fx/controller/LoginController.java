package com.yzlee.sui.client.fx.controller;

import com.yzlee.sui.client.Client;
import com.yzlee.sui.client.fx.Main;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:34
 */
public class LoginController {

    @FXML
    private TextField account;

    @FXML
    private PasswordField password;

    @FXML
    public void login(){
        try{
            String ac=account.getText();
            String pw=password.getText();
            Stage waitStage=Main.waitShow("登录中...");
            waitStage.show();
            Client.newInstance().getCachedThreadPool().execute(new Task<Void>(){

                @Override
                protected Void call() throws Exception {
                    Client.newInstance().start(ac, pw);
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    try {
                        AnchorPane home = FXMLLoader.load(getClass().getResource("/view/home.fxml"));
                        Stage stage = Main.getPrimaryStage();
                        stage.setScene(new Scene(home));
                        stage.setResizable(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Alert alert=new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(e.getMessage());
                        alert.showAndWait();
                    }finally{
                        waitStage.close();
                    }
                }

                @Override
                protected void failed() {
                    super.failed();
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(getException().getMessage());
                    alert.showAndWait();
                    waitStage.close();
                }

            });

        }catch(Exception e){
            e.printStackTrace();
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }

    }

}
