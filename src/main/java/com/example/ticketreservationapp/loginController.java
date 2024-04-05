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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
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

    //User ID
    private int user_id;

    //Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
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
                if (displayRandomNumberGenerator()) {
                    DBUtils.logInUser(actionEvent, tf_username.getText(), tf_password.getText());
                    user_id = getUserID(tf_username.getText(), tf_password.getText());
                    LoggedInController.setUserId(user_id);
                }
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

    public int getUserID(String username, String password) {
        String query = "SELECT user_id FROM users WHERE username = ? AND password = ?";
        int userID = -1;

        try {
            // Connect to the database
            Connection connect = DBUtils.connectDb();

            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userID = resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userID;
    }

    public boolean displayRandomNumberGenerator() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000000);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Login Procedure");
        dialog.setHeaderText("Please enter this number to proceed with login: " + randomNumber);
        dialog.setContentText("Enter the number:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try {
                int userInput = Integer.parseInt(result.get());
                if (userInput == randomNumber) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Authentication successful. Proceeding with login...");
                    alert.showAndWait();
                    return true;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect number entered. Please try again.");
                    alert.showAndWait();
                    return false;
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Invalid input. Please enter a number.");
                alert.showAndWait();
                return false;
            }
        }
        return false;
    }
}