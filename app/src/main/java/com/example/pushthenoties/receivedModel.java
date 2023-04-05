package com.example.pushthenoties;

public class receivedModel {

    String title;
    String description;

    public receivedModel(String title, String body) {
        this.title = title;
        this.description = body;
    }

    public receivedModel() {
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

    public void setDescription(String body) {
        this.description = body;
    }
}
