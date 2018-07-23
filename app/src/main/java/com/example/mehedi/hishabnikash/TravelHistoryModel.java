package com.example.mehedi.hishabnikash;

import java.util.Calendar;

public class TravelHistoryModel {

    private int _id;
    private String _sourcePlace;
    private String _destinationPlace;
    private String _vehicleType;
    private int _amount;
    Calendar calendar;
    int date;
    int month;
    int year;

    public TravelHistoryModel (String sourcePlace, String destinationPlace, String vehicleType, int amount) {
        this._sourcePlace = sourcePlace;
        this._destinationPlace = destinationPlace;
        this._vehicleType = vehicleType;
        this._amount = amount;
        calendar = Calendar.getInstance();
        date = calendar.get(Calendar.DATE);
        month = calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);
    }


    public TravelHistoryModel (int id, String sourcePlace, String destinationPlace, String vehicleType, int amount) {
        this._id = id;
        this._sourcePlace = sourcePlace;
        this._destinationPlace = destinationPlace;
        this._vehicleType = vehicleType;
        this._amount = amount;
        calendar = Calendar.getInstance();
        date = calendar.get(Calendar.DATE);
        month = calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);
    }

    public int getId() {
        return _id;
    }

    public String getSourcePlace() {
        return _sourcePlace;
    }

    public String getDestinationPlace() {
        return _destinationPlace;
    }

    public String getVehicleType() {
        return _vehicleType;
    }

    public int getAmount() {
        return _amount;
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
}
