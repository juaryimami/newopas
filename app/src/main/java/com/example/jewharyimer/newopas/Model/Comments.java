package com.example.jewharyimer.newopas.Model;

public class Comments {
    private String date;
    private String note;

    public Comments() {
    }

    public Comments(String date, String note) {
        this.date = date;
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
