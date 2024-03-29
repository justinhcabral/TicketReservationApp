package com.example.ticketreservationapp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Button button_login_switch;

    @FXML
    private Button button_register;

    @FXML
    private PasswordField tf_password;

    @FXML
    private TextField tf_username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_login_switch.setOnAction(this::switchToLogInScene);

        button_register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!tf_username.getText().trim().isEmpty()){
                    DBUtils.signUpUser(actionEvent,tf_username.getText(), tf_password.getText());
                } else{
                    System.out.println("Please fill in all information.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up.");
                    alert.show();
                }
            }
        });
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
