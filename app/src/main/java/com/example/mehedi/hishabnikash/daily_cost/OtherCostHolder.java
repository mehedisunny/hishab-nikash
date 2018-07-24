package com.example.mehedi.hishabnikash.daily_cost;

import java.util.Calendar;

public class OtherCostHolder {
    private int id;
    private String purpose;
    private int amount;
    private int month;
    private int date;
    private int year;
    Calendar calendar;

    public OtherCostHolder (int id, String purpose) {
        this.purpose = purpose;
        calendar = Calendar.getInstance();
        this.date = calendar.get(Calendar.DATE);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
    }

    public OtherCostHolder (String purpose, int amount) {
        this.purpose = purpose;
        this.amount = amount;
        calendar = Calendar.getInstance();
        this.date = calendar.get(Calendar.DATE);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
    }

    public OtherCostHolder (int id, String purpose, int amount, int date, int month, int year) {
        this.id = id;
        this.purpose = purpose;
        this.amount = amount;
        calendar = Calendar.getInstance();
        this.date = calendar.get(Calendar.DATE);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
    }

    public String getPurpose() {
        return this.purpose;
    }

    public int getAmount() {
        return amount;
    }

    public int getDate() {
        return date;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getId() {
        return id;
    }
}
