package com.example.jewharyimer.newopas.Notification;

public class Data {
    private String user;
    private int icon;
    private String body;
    private String tilte;
    private String sented;

    public Data(String user, int icon, String body, String tilte, String sented) {
        this.user = user;
        this.icon = icon;
        this.body = body;
        this.tilte = tilte;
        this.sented = sented;
    }

    public Data() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }
}
