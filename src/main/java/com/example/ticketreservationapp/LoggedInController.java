package com.example.ticketreservationapp;

//Imports

import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
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
    private Spinner<Integer> concerts_tickets_quantity;

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

    @FXML
    private TableView<Purchase> cartTableView;

    @FXML
    private TableColumn<Purchase, String> colConcertTitle;

    @FXML
    private TableColumn<Purchase,String> colTicketType;

    @FXML
    private TableColumn<Purchase,Integer> colQuantity;

    @FXML
    private TableColumn<Purchase,Double> colPrice;

    @FXML
    private TableView<Purchase> cartTableView1;

    @FXML
    private TableColumn<Purchase, String> colConcertTitle1;

    @FXML
    private TableColumn<Purchase,String> colTicketType1;

    @FXML
    private TableColumn<Purchase,Integer> colQuantity1;

    @FXML
    private TableColumn<Purchase,Double> colPrice1;

    @FXML
    private Label total_price;

    @FXML
    private Button btn_remove;

    @FXML
    private Button cart_checkout;

    @FXML
    private BorderPane checkOutPage;

    @FXML
    private TextField tf_fullName;

    @FXML
    private TextField tf_billingAddress;

    @FXML
    private TextField tf_city;

    @FXML
    private TextField tf_postalCode;

    @FXML
    private TextField tf_phoneNumber;

    @FXML
    private TextField tf_emailAddress;

    @FXML
    private TextField tf_cardNumber;

    @FXML
    private TextField tf_cvv;

    @FXML
    private Button btn_order;

    @FXML
    private Button btn_cancelOrder;

    @FXML
    private BorderPane dashboard;

    @FXML
    private Label total_price1;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private ImageView lanyposter;

    @FXML
    private ImageView auroraposter;

    private Image image;
    private static int userID;
    private ticketData ticketType;



    //Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public int getUserId() {
        return this.userID;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        concerts_sidebar.setOnAction(this::switchToConcertsScene);
        button_signOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "login.fxml", "Stage Pass", null);
                userID = -1;
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

        showSpinnerValue();

        // Declare ObservableList
        ObservableList<Purchase> cartItems = FXCollections.observableArrayList();

        // Set ObservableList as the items of the TableView
        cartTableView.setItems(cartItems);

        concerts_btn_addToCart.setOnAction(new EventHandler<ActionEvent>() { //add to cart button
            @Override
            public void handle(ActionEvent event) {
                int user_id = userID;
                ticketData selectedTicket = ticketType;

                if (selectedTicket != null) {
                    String ticketType = selectedTicket.getTicketType();
                    int TicketID = selectedTicket.getTicketID();
                    double ticketPrice = selectedTicket.getPrice();

                    Integer Quantity = concerts_tickets_quantity.getValue();
                    if (Quantity == null || Quantity == 0) {
                        System.out.println("Please select a quantity.");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Tickets cannot be 0");
                        alert.show();
                        return;
                    }
                    int QuantityPurchased = Quantity.intValue();
                    double Price = ticketPrice*QuantityPurchased;
                    Date PurchaseDate = Date.valueOf(LocalDate.now());

                    // Get the concert title
                    String concertTitle = "";
                    try {
                        String selectConcert = "SELECT * FROM concerts WHERE ConcertID = ?";
                        PreparedStatement prepareConcert = connect.prepareStatement(selectConcert);
                        prepareConcert.setInt(1, selectedTicket.getConcertID());
                        ResultSet resultConcert = prepareConcert.executeQuery();
                        if (resultConcert.next()) {
                            concertTitle = resultConcert.getString("Title");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // Create the Purchase object
                    Purchase purchase = new Purchase(userID, TicketID, concertTitle, ticketType, QuantityPurchased, Price, PurchaseDate);

                    // Add the Purchase object to the cart items
                    cartItems.add(purchase);

                    // Update the cart TableView
                    colConcertTitle.setCellValueFactory(new PropertyValueFactory<>("ConcertTitle"));
                    colTicketType.setCellValueFactory(new PropertyValueFactory<>("TicketType"));
                    colQuantity.setCellValueFactory(new PropertyValueFactory<>("QuantityPurchased"));
                    colPrice.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));
                } else {
                    System.out.println("No ticket selected.");
                }
                setTotalPrice();
            }
        });

        btn_remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Get the selected item
                Purchase selectedItem = cartTableView.getSelectionModel().getSelectedItem();

                // Remove the selected item
                if (selectedItem != null) {
                    cartTableView.getItems().remove(selectedItem);
                } else {
                    System.out.println("No item selected.");
                }
            }
        });

        cartTableView.getItems().addListener(new ListChangeListener<Purchase>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Purchase> c) {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        setTotalPrice();
                    }
                }
            }
        });
        cartTableView1.itemsProperty().bind(cartTableView.itemsProperty());

        colConcertTitle1.setCellValueFactory(new PropertyValueFactory<>("ConcertTitle"));
        colTicketType1.setCellValueFactory(new PropertyValueFactory<>("TicketType"));
        colQuantity1.setCellValueFactory(new PropertyValueFactory<>("QuantityPurchased"));
        colPrice1.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));

        btn_order.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String fullName = tf_fullName.getText();
                String billingAddress = tf_billingAddress.getText();
                String city = tf_city.getText();
                String postalCode = tf_postalCode.getText();
                String phoneNumber = tf_phoneNumber.getText();
                String emailAddress = tf_emailAddress.getText();
                String cardNumber = tf_cardNumber.getText();
                String cvv = tf_cvv.getText();

                if (cardNumber.length() != 16 || cvv.length() != 3) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Invalid inputs in card number and CVV text fields");
                    alert.show();
                    return;
                }

                // If inputs are valid, insert the data into the database
                try (Connection connect = DBUtils.connectDb()) {
                    String query = "UPDATE users SET fullName = ?, billingAddress = ?, city = ?, postalCode = ?, phoneNumber = ?, emailAddress = ?, cardNumber = ?, CVV = ? WHERE user_id = ?";
                    assert connect != null;
                    PreparedStatement preparedStatement = connect.prepareStatement(query);
                    preparedStatement.setString(1, fullName);
                    preparedStatement.setString(2, billingAddress);
                    preparedStatement.setString(3, city);
                    preparedStatement.setString(4, postalCode);
                    preparedStatement.setString(5, phoneNumber);
                    preparedStatement.setString(6, emailAddress);
                    preparedStatement.setString(7, cardNumber);
                    preparedStatement.setString(8, cvv);
                    preparedStatement.setInt(9, userID);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try (Connection connect = DBUtils.connectDb()) {
                    // Check if the user_id exists in the users table
                    String queryUser = "SELECT user_id FROM users WHERE user_id = ?";
                    PreparedStatement preparedStatementUser = connect.prepareStatement(queryUser);
                    preparedStatementUser.setInt(1, userID);
                    ResultSet resultSetUser = preparedStatementUser.executeQuery();

                    if (!resultSetUser.next()) {
                        System.out.println("User ID does not exist in the users table.");
                        return;
                    }

                    // Prepare the SQL query to insert into the purchases table
                    String queryPurchase = "INSERT INTO purchases (user_id, TicketID, ConcertTitle, TicketType, QuantityPurchased, TotalPrice, PurchaseDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatementPurchase = connect.prepareStatement(queryPurchase, Statement.RETURN_GENERATED_KEYS);

                    // Iterate over the items in the cartTableView1
                    for (Purchase purchase : cartTableView1.getItems()) {
                        // Retrieve the concert title, ticket type, quantity, and price
                        int ticketId = purchase.getTicketID();
                        String concertTitle = purchase.getConcertTitle();
                        String ticketType = purchase.getTicketType();
                        int quantity = purchase.getQuantityPurchased();
                        double price = purchase.getTotalPrice();
                        Date purchaseDate = Date.valueOf(LocalDate.now());

                        // Set the parameters for the purchases table
                        preparedStatementPurchase.setInt(1, userID);
                        preparedStatementPurchase.setInt(2, ticketId);
                        preparedStatementPurchase.setString(3, concertTitle);
                        preparedStatementPurchase.setString(4, ticketType);
                        preparedStatementPurchase.setInt(5, quantity);
                        preparedStatementPurchase.setDouble(6, price);
                        preparedStatementPurchase.setDate(7, purchaseDate);

                        // Execute the query to insert into the purchases table
                        preparedStatementPurchase.executeUpdate();

                        // Get the generated purchase_id
                        ResultSet rs = preparedStatementPurchase.getGeneratedKeys();
                        if (rs.next()) {
                            int purchaseId = rs.getInt(1);

                        }
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Order Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Your order has been placed successfully!");
                    alert.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                HomePage.setVisible(true);
                home_btn_sidebar.setStyle("-fx-background-color: #8f523b");
                concerts_btn_sidebar.setStyle("-fx-background-color: transparent");
                cart_btn_sidebar.setStyle("-fx-background-color: transparent");
            }
        });

        tf_fullName.setOnAction(event -> tf_billingAddress.requestFocus());
        tf_billingAddress.setOnAction(event -> tf_city.requestFocus());
        tf_city.setOnAction(event -> tf_postalCode.requestFocus());
        tf_postalCode.setOnAction(event -> tf_phoneNumber.requestFocus());
        tf_phoneNumber.setOnAction(event -> tf_emailAddress.requestFocus());
        tf_emailAddress.setOnAction(event -> tf_cardNumber.requestFocus());
        tf_cardNumber.setOnAction(event -> tf_cvv.requestFocus());
        tf_cvv.setOnAction(event -> mainPane.requestFocus());

        switchVisibility();

    }

    public static void setUserId(int userID) {
        LoggedInController.userID = userID;
    }

    public void setUserInformation(String username){
        profileName.setText(username);
    }

    public void switchScene(ActionEvent event){
        if(HomePage.isVisible()){
            home_btn_sidebar.setStyle("-fx-background-color: #8f523b");
            concerts_btn_sidebar.setStyle("-fx-background-color: transparent");
            cart_btn_sidebar.setStyle("-fx-background-color: transparent");
        } else if(ticketPurchasePage.isVisible()){
            home_btn_sidebar.setStyle("-fx-background-color: transparent");
            concerts_btn_sidebar.setStyle("-fx-background-color: #8f523b");
            cart_btn_sidebar.setStyle("-fx-background-color: transparent");
        } else if(cartPage.isVisible()){
            home_btn_sidebar.setStyle("-fx-background-color: transparent");
            concerts_btn_sidebar.setStyle("-fx-background-color: transparent");
            cart_btn_sidebar.setStyle("-fx-background-color: #8f523b");
        }


        if(event.getSource() == home_btn_sidebar){
            HomePage.setVisible(true);
            ticketPurchasePage.setVisible(false);
            cartPage.setVisible(false);
            checkOutPage.setVisible(false);

            home_btn_sidebar.setStyle("-fx-background-color: #8f523b");
            concerts_btn_sidebar.setStyle("-fx-background-color: transparent");
            cart_btn_sidebar.setStyle("-fx-background-color: transparent");
        } else if(event.getSource() == concerts_btn_sidebar){
            HomePage.setVisible(false);
            ticketPurchasePage.setVisible(true);
            cartPage.setVisible(false);
            checkOutPage.setVisible(false);

            home_btn_sidebar.setStyle("-fx-background-color: transparent");
            concerts_btn_sidebar.setStyle("-fx-background-color: #8f523b");
            cart_btn_sidebar.setStyle("-fx-background-color: transparent");
        } else if(event.getSource() == cart_btn_sidebar){
            HomePage.setVisible(false);
            ticketPurchasePage.setVisible(false);
            cartPage.setVisible(true);
            checkOutPage.setVisible(false);

            home_btn_sidebar.setStyle("-fx-background-color: transparent");
            concerts_btn_sidebar.setStyle("-fx-background-color: transparent");
            cart_btn_sidebar.setStyle("-fx-background-color: #8f523b");
        } else if(event.getSource() == cart_checkout){
            dashboard.setVisible(false);
            checkOutPage.setVisible(true);
        } else if (event.getSource() == btn_cancelOrder){
            dashboard.setVisible(true);
            checkOutPage.setVisible(false);
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

    public void comboBox(){ //combobox for ticket selection
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
                                                     result.getDouble("Price"));
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
                        int ticketID = newSelection.getTicketID();
                        String ticketTypeText = newSelection.getTicketType();
                        double price = newSelection.getPrice();
                        concerts_tickets_label.setText(newSelection.getTicketType());
                        concerts_tickets_price.setText(String.valueOf(newSelection.getPrice()));

                        if (newSelection != null) {
                            ticketID = newSelection.getTicketID();
                            ticketTypeText = newSelection.getTicketType();
                            price = newSelection.getPrice();
                            concerts_tickets_label.setText(ticketTypeText);
                            concerts_tickets_price.setText(String.valueOf(price));

                            // Initialize ticketType before calling methods on it
                            ticketType = new ticketData(ticketID, newSelection.getConcertID(), ticketTypeText, price);
                        }

                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private SpinnerValueFactory<Integer> quantitySpinner;

    public void showSpinnerValue(){

        quantitySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10, 0);
        concerts_tickets_quantity.setValueFactory(quantitySpinner);

    }

    public ObservableList<ticketData> TicketList() {
        ObservableList<ticketData> ticketDataList = FXCollections.observableArrayList();

        // Connect to the database
        Connection connect = DBUtils.connectDb();

        // Define the SQL query
        String sql = "SELECT * FROM tickets";

        try {
            // Prepare and execute the SQL query
            PreparedStatement prepare = connect.prepareStatement(sql);
            ResultSet result = prepare.executeQuery();

            // For each row in the result set, create a ticketData object and add it to the list
            while (result.next()) {
                ticketData ticket = new ticketData(result.getInt("TicketID"),
                        result.getInt("ConcertID"),
                        result.getString("TicketType"),
                        result.getDouble("Price"));
                ticketDataList.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticketDataList;
    }

    public void setTotalPrice(){
        double sum = 0;
        for (Purchase purchase : cartTableView.getItems()) {
            sum += purchase.getTotalPrice();
        }
        total_price.setText(String.valueOf(sum));
        total_price1.setText(String.valueOf(sum));
    }

    public void switchVisibility() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            boolean isVisible = lanyposter.isVisible();
            lanyposter.setVisible(!isVisible);
            auroraposter.setVisible(isVisible);
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
