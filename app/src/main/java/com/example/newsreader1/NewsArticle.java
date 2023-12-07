package com.example.newsreader1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsArticle {
    private String title;
    private String link;
    private String description;
    private Date date; // Changed type to Date

    public NewsArticle(String title, String link, String description, Date date) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.date = date;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Additional method to get formatted date string
    public String getFormattedDate() {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.US);
            return dateFormat.format(date);
        }
        return null;
    }
}
