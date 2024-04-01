package com.example.ticketreservationapp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class loginController implements Initializable {
    @FXML
    private Button button_login;

    @FXML
    private Button button_sign_up;

    @FXML
    private PasswordField tf_password;

    @FXML
    private TextField tf_username;

    @FXML
    private BorderPane mainPane ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> mainPane.requestFocus()); //this whole segment of code is to request focus for when the main pane is clicked so that the text fields aren't focused

        mainPane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getTarget() == mainPane) {
                    mainPane.requestFocus();
                    mouseEvent.consume();
                }
            }
        });

        button_sign_up.setOnAction(this::switchToSignupScene); //sets button_sign_up to switch to Sign Up scene
        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.logInUser(actionEvent, tf_username.getText(), tf_password.getText());
            }
        });

        tf_username.setOnAction(event -> tf_password.requestFocus());// press enter to move on to password field
        tf_password.setOnAction(event -> button_login.fire()); //press enter to login
    }

    @FXML
    private void switchToSignupScene(ActionEvent event) { //method for switching to sign-up.fxml
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