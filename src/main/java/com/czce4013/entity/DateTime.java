package com.czce4013.entity;

import java.util.HashMap;

public class DateTime {
    int year;
    int month;
    int day;
    int hour;
    int minute;

    public DateTime(){
        this.year = -1;
        this.month = -1;
        this.day = -1;
        this.hour = -1;
        this.minute = -1;
    }

    public DateTime(int y,int mo, int d, int h,int mi){
        this.year = y;
        this.month = mo;
        this.day = d;
        this.hour = h;
        this.minute = mi;
    }

    public String toNiceString(){
        return Integer.toString(year) + "/" + Integer.toString(month) + "/" + Integer.toString(day) + " " +
                Integer.toString(hour) + Integer.toString(minute) + "hrs";
    }

    public String toString(){
        return "DateTime{" +
                "year=" + year +
                "month=" + month +
                "day=" + day +
                "hour=" + hour +
                "minute=" + minute +
                '}';
    }
}
