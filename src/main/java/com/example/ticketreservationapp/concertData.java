package com.example.ticketreservationapp;

import java.sql.Date;

public class concertData {
    private int concertID;
    private String title;
    private String venue;
    private Date date;
    private String image;
    public concertData(int concertID,String title, String venue, String image, Date date){
        this.concertID = concertID;
        this.title = title;
        this.venue = venue;
        this.image = image;
        this.date = date;
    }

    public int getConcertID(){
        return concertID;
    }
    public String getTitle(){
        return title;
    }
    public String getVenue(){
        return venue;
    }
    public String getImage(){
        return image;
    }
    public Date getDate(){
        return date;
    }
}
