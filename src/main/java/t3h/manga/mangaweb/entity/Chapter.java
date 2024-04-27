package org.example.model;

import java.awt.*;
import java.util.LinkedList;

public class Chapter {
    private String chapterID;
    private String name;
    private String date;
    private LinkedList<String> imagePathLinkedList;

    public Chapter() {

    }

    public Chapter(String name, String date, LinkedList<String> imagePathLinkedList) {
        this.name = name;
        this.date = date;
        this.imagePathLinkedList = imagePathLinkedList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
