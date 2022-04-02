package com.example.bonbon.data_models;

import java.util.ArrayList;

public class Observation {
    String tags;
    String body;
    long timeStamp;
    String image;

    public Observation(String tags, String body, long timeStamp, String image) {
        this.tags = tags;
        this.body = body;
        this.timeStamp = timeStamp;
        this.image = image;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
