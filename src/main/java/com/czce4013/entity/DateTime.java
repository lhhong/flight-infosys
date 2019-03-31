package com.czce4013.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DateTime {
    Date date;

    public DateTime(int y,int mo, int d, int h,int mi){
        Calendar cal = Calendar.getInstance();
        cal.set(y, mo - 1, d, h, mi, 0);
        cal.set(Calendar.MILLISECOND, 0);
        this.date = cal.getTime();
    }

    public String toNiceString(){
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        return df.format(this.date) + " hrs";
    }
}
