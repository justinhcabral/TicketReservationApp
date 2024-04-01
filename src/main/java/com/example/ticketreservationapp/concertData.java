package com.example.ticketreservationapp;

import java.sql.Date;

public class concertData {
    private String title;
    private String venue;
    private Date date;
    private String image;
    public concertData(String title, String venue, String image, Date date){

        this.title = title;
        this.venue = venue;
        this.image = image;
        this.date = date;
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
