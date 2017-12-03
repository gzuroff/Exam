package com.gregoryzuroff.exam;


public class Book {
    public Book(String title, String author, String condition, String borrowed) {
        this.title = title;
        this.author = author;
        this.condition = condition;
        this.borrowed = borrowed;
    }
    public Book(){}
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCondition() {
        return condition;
    }

    public String getBorrowed() {
        return borrowed;
    }

    String title;

    public void setAuthor(String author) {
        this.author = author;
    }

    String author;

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setBorrowed(String borrowed) {
        this.borrowed = borrowed;
    }

    String condition;
    String borrowed;


}