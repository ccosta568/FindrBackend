package org.com.selenium;

public class Event {
    private String eventId;
    private String title;
    private String host;  // Host of the event
    private String time;  // Using String for time
    private String date;  // Using String for date
    private String img;   // Image URL or path
    private double price; // Event price
    private String link;

    // Constructor
    public Event(String eventId, String title, String host, String time, String date, String img, double price, String link) {
        this.eventId = eventId;
        this.title = title;
        this.host = host;
        this.time = time;
        this.date = date;
        this.img = img;
        this.price = price;
        this.link = link;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}

