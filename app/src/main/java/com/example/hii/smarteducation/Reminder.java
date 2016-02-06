package com.example.hii.smarteducation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by hii on 1/23/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Reminder {
    private String title;
    private String date;
    private String time;

    public Reminder() {
    }
    public Reminder(String title, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
