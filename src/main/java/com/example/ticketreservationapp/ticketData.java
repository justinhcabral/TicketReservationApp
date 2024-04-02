package com.example.ticketreservationapp;

public class ticketData {
    private int TicketID;
    private int ConcertID;
    private String TicketType;
    private Double Price;

    public ticketData(int ticketID, int concertID, String ticketType, Double price){
        this.TicketID = ticketID;
        this.ConcertID = concertID;
        this.TicketType = ticketType;
        this.Price = price;
    }

    public int getTicketID() {
        return TicketID;
    }

    public int getConcertID() {
        return ConcertID;
    }

    public String getTicketType() {
        return TicketType;
    }

    public Double getPrice() {
        return Price;
    }
    @Override
    public String toString() {
        return TicketType;
    }

    public void setTicketID(int ticketID){
        this.TicketID = ticketID;
    }
    public void setTicketType(String ticketType) {
        this.TicketType = ticketType;
    }

    public void setPrice(double price){
        this.Price = price;
    }

}
