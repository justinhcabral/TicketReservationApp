package com.example.ticketreservationapp;

import java.util.Date;

public class Purchase {
    private int PurchaseID;
    private int userId;
    private int TicketID;
    private String ConcertTitle;
    private String TicketType;
    private int QuantityPurchased;
    private double TotalPrice;
    private Date PurchaseDate;

    public Purchase(int userId, int TicketID, String concertTitle,String TicketType ,int QuantityPurchased, double TotalPrice, Date PurchaseDate) {
        this.userId = userId;
        this.TicketID = TicketID;
        this.ConcertTitle = concertTitle;
        this.TicketType = TicketType;
        this.QuantityPurchased = QuantityPurchased;
        this.TotalPrice = TotalPrice;
        this.PurchaseDate = PurchaseDate;
    }

    public int getPurchaseID() {
        return PurchaseID;
    }

    public int getUserId() {
        return userId;
    }
    public String getConcertTitle(){
        return ConcertTitle;
    }

    public int getTicketID() {
        return TicketID;
    }

    public int getQuantityPurchased() {
        return QuantityPurchased;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public Date getPurchaseDate() {
        return PurchaseDate;
    }

    public String getTicketType() {
        return this.TicketType;
    }
}