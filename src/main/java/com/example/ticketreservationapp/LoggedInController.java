package com.example.ticketreservationapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private AnchorPane bought_tickets;

    @FXML
    private Button button_signout;

    @FXML
    private AnchorPane cart;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
