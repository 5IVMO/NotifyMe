package com.example.hii.smarteducation;

/**
 * Created by hii on 1/30/2016.
 */
public class Todo {

    private String title;
    private String description;

    public Todo(){

    }
    public Todo(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
