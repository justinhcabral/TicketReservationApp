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

public class loginController {
    @FXML
    private Button button_login;

    @FXML
    private Button button_sign_up;

    @FXML
    private PasswordField tf_password;

    @FXML
    private TextField tf_username;

    @FXML
    public void initialize() {
        button_sign_up.setOnAction(this::switchToSignupScene);
    }

    @FXML
    private void switchToSignupScene(ActionEvent event) {
        try {
            Parent signupRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("sign-up.fxml")));
            Scene signupScene = new Scene(signupRoot);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(signupScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}