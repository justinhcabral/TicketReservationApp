package com.example.ticketreservationapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
    private ComboBox<ticketData> concerts_tickets;

    @FXML
    private Spinner<?> concerts_tickets_quantity;

    @FXML
    private Label concerts_tickets_label;

    @FXML
    private Label concerts_tickets_price;

    @FXML
    private Label concerts_title;

    @FXML
    private Button home_btn_sidebar;

    @FXML
    private Label profileName;

    @FXML
    private AnchorPane ticketPurchasePage;

    @FXML
    private AnchorPane concerts_imageView_container;

    @FXML
    private AnchorPane cartPage;

    private Image image;

    //Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

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
        concerts_imageView.setOnMouseClicked(event -> showImagePreview(concerts_imageView.getImage())); //image preview when clicked

        concerts_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            concerts_imageView_container.setVisible(newSelection != null);
        });

        concerts_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            comboBox();
        });

    }

    public void setUserInformation(String username){
        profileName.setText(username);
    }

    public void switchScene(ActionEvent event){
        if(event.getSource() == home_btn_sidebar){
            HomePage.setVisible(true);
            ticketPurchasePage.setVisible(false);
            cartPage.setVisible(false);


            home_btn_sidebar.setStyle("-fx-background-color: #8f523b");
            concerts_btn_sidebar.setStyle("-fx-background-color: transparent");
            cart_btn_sidebar.setStyle("-fx-background-color: transparent");
        } else if(event.getSource() == concerts_btn_sidebar){
            HomePage.setVisible(false);
            ticketPurchasePage.setVisible(true);
            cartPage.setVisible(false);

            home_btn_sidebar.setStyle("-fx-background-color: transparent");
            concerts_btn_sidebar.setStyle("-fx-background-color: #8f523b");
            cart_btn_sidebar.setStyle("-fx-background-color: transparent");
        } else if(event.getSource() == cart_btn_sidebar){
            HomePage.setVisible(false);
            ticketPurchasePage.setVisible(false);
            cartPage.setVisible(true);

            home_btn_sidebar.setStyle("-fx-background-color: transparent");
            concerts_btn_sidebar.setStyle("-fx-background-color: transparent");
            cart_btn_sidebar.setStyle("-fx-background-color: #8f523b");
        }
    }

    public ObservableList<concertData> concertDataList(){
        ObservableList<concertData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM concerts ";

        connect = DBUtils.connectDb();

        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            concertData conD;

            while(result.next()){
                conD = new concertData(result.getInt("ConcertID"),
                        result.getString("Title"),
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

        image = new Image(url, 1000,1000,false,true);
        concerts_imageView.setImage(image);
    }
    private void showImagePreview(Image image) { //to make the image clickable for preview
        Stage previewStage = new Stage();
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(500); // Set the width of the image. The height will be adjusted to keep the aspect ratio.

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> previewStage.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(imageView, closeButton);
        layout.setAlignment(Pos.CENTER);
        Image icon = new Image("file:src/main/resources/com/example/ticketreservationapp/Logo.png");
        previewStage.getIcons().add(icon);

        Scene scene = new Scene(layout);
        previewStage.setScene(scene);
        previewStage.show();
    }

//    public void comboBox(){
//        concertData conD = concerts_tableView.getSelectionModel().getSelectedItem();
//
//        if (conD != null){
//            String selectTicketTypes = "SELECT TicketType FROM tickets WHERE ConcertID = ?";
//            connect = DBUtils.connectDb();
//
//            try {
//                prepare = connect.prepareStatement(selectTicketTypes);
//                prepare.setInt(1, conD.getConcertID());
//                result = prepare.executeQuery();
//
//                ObservableList listData = FXCollections.observableArrayList();
//
//                while(result.next()){
//                    String item = result.getString("TicketType");
//                    listData.add(item);
//                }
//
//                concerts_tickets.setItems(listData);
//                concerts_tickets_label.setText((String) concerts_tickets.getSelectionModel().getSelectedItem());
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void comboBox(){
    concertData conD = concerts_tableView.getSelectionModel().getSelectedItem();

    if (conD != null){
        String selectTickets = "SELECT * FROM tickets WHERE ConcertID = ?";
        connect = DBUtils.connectDb();

        try {
            prepare = connect.prepareStatement(selectTickets);
            prepare.setInt(1, conD.getConcertID());
            result = prepare.executeQuery();

            ObservableList<ticketData> listData = FXCollections.observableArrayList();

            while(result.next()){
                ticketData item = new ticketData(result.getInt("TicketID"),
                                                 result.getInt("ConcertID"),
                                                 result.getString("TicketType"),
                                                 result.getString("Price"));
                listData.add(item);
            }

            concerts_tickets.setItems(listData);
            concerts_tickets.setCellFactory(param -> new ListCell<ticketData>() {
                @Override
                protected void updateItem(ticketData item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null || item.getTicketType() == null) {
                        setText(null);
                    } else {
                        setText(item.getTicketType());
                    }
                }
            });

            concerts_tickets.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    concerts_tickets_label.setText(newSelection.getTicketType());
                    concerts_tickets_price.setText(newSelection.getPrice());
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

}
