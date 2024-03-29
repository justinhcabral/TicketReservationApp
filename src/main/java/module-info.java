module com.example.ticketreservationapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;


    opens com.example.ticketreservationapp to javafx.fxml;
    exports com.example.ticketreservationapp;
}