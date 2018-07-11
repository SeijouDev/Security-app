package com.app.inpahu.securityapp.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Report {

    private int id;
    private String address;
    private double latitude;
    private double longitude;
    private String date;
    private String hour;
    private int type;
    private String user_id;

    public Report(int id, String address, double latitude, double longitude, String date, String hour, String user_id, int type) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.hour = hour;
        this.user_id = user_id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static Report getReportFromJSONObject(JSONObject jobj) {
        try {

            int id = jobj.getInt("id");
            String address = jobj.getString("address");
            double latitude = jobj.getDouble("latitude");
            double longitude = jobj.getDouble("longitude");
            String date = jobj.getString("date");
            String hour = jobj.getString("hour");
            int type = jobj.getInt("type");
            String user_id = jobj.getString("user_id");

            return new Report(id,address,latitude,longitude,date,hour,user_id,type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Report> getReportsFromJSONArray(JSONArray arr) {

        ArrayList<Report> reports = new ArrayList();
        try {
            for(int i = 0; i < arr.length(); i++) {
                JSONObject jobj = arr.getJSONObject(i);
                reports.add(getReportFromJSONObject(jobj));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reports;
    }
}
