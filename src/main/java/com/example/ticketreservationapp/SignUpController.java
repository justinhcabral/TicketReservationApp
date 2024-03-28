package com.example.ticketreservationapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SignUpController{
    @FXML
    private Button button_login_switch;

    @FXML
    private Button button_register;

    @FXML
    private PasswordField tf_password;

    @FXML
    private TextField tf_username;

    @FXML
    public void initialize() {
        button_login_switch.setOnAction(this::switchToLogInScene);
    }

    @FXML
    private void switchToLogInScene(ActionEvent event) {
        try {
            Parent signInRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
            Scene signInScene = new Scene(signInRoot);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(signInScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
