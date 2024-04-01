package com.example.ticketreservationapp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ConcertsPageController implements Initializable {
    @FXML
    private Button button_signOut;
    @FXML
    private Label profileName;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_signOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "login.fxml", "Stage Pass", null);
            }
        });
    }

    public void setUserInformation(String username){
        profileName.setText(username);
    }
}
