package com.example.ticketreservationapp;

public class ticketData {
    private int TicketID;
    private int ConcertID;
    private String TicketType;
    private String Price;

    public ticketData(int ticketID, int concertID, String ticketType, String price){
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

    public String getPrice() {
        return Price;
    }
    @Override
    public String toString() {
        return TicketType;
    }
}
