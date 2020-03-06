package com.ironfake.quotes.model;

import java.util.List;

public class Quote {
    private int id;
    private int createdBy;
    private String text;
    private List<String> tagList;

    public Quote(int id, int createdBy, String text, List<String> tagList) {
        this.id = id;
        this.createdBy = createdBy;
        this.text = text;
        this.tagList = tagList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tags) {
        this.tagList = tags;
    }
}
