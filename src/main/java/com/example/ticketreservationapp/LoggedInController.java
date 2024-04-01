package com.example.ticketreservationapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private AnchorPane HomePage;

    @FXML
    private Button boughtTickets_btn_sidebar;

    @FXML
    private Button button_signOut;

    @FXML
    private Button cart_btn_sidebar;

    @FXML
    private Button concerts_btn_addToCart;

    @FXML
    private Label concerts_btn_date;

    @FXML
    private Button concerts_btn_selectEvent;

    @FXML
    private Button concerts_btn_sidebar;

    @FXML
    private Label concerts_btn_title;

    @FXML
    private Label concerts_btn_venue;

    @FXML
    private TableColumn<concertData, String> concerts_col_date;

    @FXML
    private TableColumn<concertData, String> concerts_col_title;

    @FXML
    private TableColumn<concertData, String> concerts_col_venue;

    @FXML
    private ImageView concerts_imageView;

    @FXML
    private TableView<concertData> concerts_tableView;

    @FXML
    private ComboBox<?> concerts_tickets;

    @FXML
    private Spinner<?> concerts_tickets_quantity;

    @FXML
    private Label concerts_title;

    @FXML
    private Button home_btn_sidebar;

    @FXML
    private Label profileName;

    @FXML
    private AnchorPane ticketPurchasePage;

    private Image image;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        concerts_sidebar.setOnAction(this::switchToConcertsScene);
        button_signOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "login.fxml", "Stage Pass", null);
            }
        });

        showConcertDataList();
    }

    public void setUserInformation(String username){
        profileName.setText(username);
    }

    public void switchScene(ActionEvent event){
        if(event.getSource() == home_btn_sidebar){
            HomePage.setVisible(true);
            ticketPurchasePage.setVisible(false);

            home_btn_sidebar.setStyle("-fx-background-color: #8f523b");
            concerts_btn_sidebar.setStyle("-fx-background-color: transparent");
        } else if(event.getSource() == concerts_btn_sidebar){
            HomePage.setVisible(false);
            ticketPurchasePage.setVisible(true);

            home_btn_sidebar.setStyle("-fx-background-color: transparent");
            concerts_btn_sidebar.setStyle("-fx-background-color: #8f523b");
        }
    }

    //Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public ObservableList<concertData> concertDataList(){
        ObservableList<concertData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM concerts ";

        connect = DBUtils.connectDb();

        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            concertData conD;

            while(result.next()){
                conD = new concertData(result.getString("Title"),
                        result.getString("Venue"),
                        result.getString("Image"),
                        result.getDate("Date"));
                listData.add(conD);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listData;
    }
    private ObservableList<concertData> listConcerts;
    public void showConcertDataList(){
        listConcerts = concertDataList();

        concerts_col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        concerts_col_venue.setCellValueFactory(new PropertyValueFactory<>("venue"));
        concerts_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        concerts_tableView.setItems(listConcerts);
    }

    public void selectConcertDataList(){
        concertData conD = concerts_tableView.getSelectionModel().getSelectedItem();
        int num = concerts_tableView.getSelectionModel().getSelectedIndex();

        if((num-1)<-1){
            return;
        }

        concerts_btn_title.setText(conD.getTitle());
        concerts_btn_venue.setText(conD.getVenue());
        concerts_title.setText(conD.getTitle());

        String getDate = String.valueOf(conD.getDate());

        concerts_btn_date.setText(getDate);

        String url = "file:" + conD.getImage();

        image = new Image(url, 150,150,false,true);
        concerts_imageView.setImage(image);
    }
}
