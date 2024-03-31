package com.example.ticketreservationapp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private AnchorPane bought_tickets;

    @FXML
    private Button bought_tickets_sidebar;

    @FXML
    private Button button_signOut;

    @FXML
    private AnchorPane cart;

    @FXML
    private Button cart_sidebar;

    @FXML
    private Label profileName;

    @FXML
    private Label concerts;

    @FXML
    private Button concerts_sidebar;
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
