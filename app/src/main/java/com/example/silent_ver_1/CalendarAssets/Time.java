package com.example.silent_ver_1.CalendarAssets;

import android.util.Log;

/**
 * A class that hold a time  of day - hours and minutes.
 */
public class Time {
    private int hour, minute, year, month, day;
    private int MIN_IN_DAY = 1440;

    public Time() {
        this.hour = 0;
        this.minute = 0;
        this.year = 2022;
        this.month = 1;
        this.day = 1;
    }

    public Time(int hour, int minute, int year, int month, int day) {
        this.hour = hour;
        this.minute = minute;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Time(Time other){
        this.hour = other.hour;
        this.minute = other.minute;
        this.year = other.year;
        this.month = other.month;
        this.day = other.day;
    }

//    /**
//     * Gets time of day in minutes and converts it to hours and minutes format.
//     * Minutes range 0 - 1440.
//     * @param minute
//     * @return Time object
//     */
//    public Time(String minute){
//        int minutes = Integer.parseInt(minute);
//        this.hour = minutes / 60;
//        this.minute = minutes % 60;
////        Log.i("Time", "event: hour:"+this.hour+", min: "+this.minute);
//    }
//
//    public Time(int minute){
//        this.hour = minute / 60;
//        this.minute = minute % 60;
////        Log.i("Time", "event: hour:"+this.hour+", min: "+this.minute);
//    }

    public Time(long milli){
        milli += 7200000; // Timezone +2 hours
        int[] md;
        Log.i("Time", "Milliseconds 1: "+milli);
        long time = milli / 60000;
//        Log.i("Time", "Milliseconds 2: "+newTime);

//        double time = (newTime - 2440588);
        Log.i("Time", "Milliseconds 3: "+time);

        int year = (int) (time / 525600) + 1970;
        Log.i("Time", "Milliseconds 4: "+year);

        int minInYear = (int) (time % 525960);
        Log.i("Time", "Milliseconds 5: "+(time % 525960));
        if(year % 4 == 0){
            md = calcMonth(true, minInYear);
        }
        else{
            md = calcMonth(false, minInYear);
        }
        int month = md[0];
        Log.i("Time", "Milliseconds 6: "+month);

        int day = md[1] / MIN_IN_DAY;
        Log.i("Time", "Milliseconds 7: "+md[1]);

        int carry = md[1] % MIN_IN_DAY;
        Log.i("Time", "Milliseconds 8: "+carry);


        int hour = carry / 60;
        int minute = carry % 60;

        this.hour = hour;
        this.minute = minute;
        this.year = year;
        this.month = month;
        this.day = day;

    }

    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public int getDay() { return day; }
    public void setDay(int day) { this.day = day; }

    public String getTime(){
        String hour = this.hour+"";
        String minute = this.minute+"";
        if(this.hour < 10){
            hour = "0"+hour;
        }
        if(this.minute < 10){
            minute = "0"+minute;
        }
        return hour+":"+minute;
    }

    public String getDate(){
        return this.day+"."+this.month+"."+this.year;
    }

    /*
     * jan - 31
     * feb - 28 (29) = 59
     * mar - 31 = 90
     * apr - 30 = 120
     * may - 31 = 151
     * jun - 30 = 181
     * jul - 31 = 212
     * aug - 31 = 243
     * sep - 30 = 273
     * oct - 31 = 304
     * nov - 30 = 334
     * dec - 31 = 365
     */
//    public int[] getDate(int date){
//        int[] md = new int[2];
//        double time = (date - 2440588);
//        int year = (int) (time / 365.25) + 1970;
//        int days = (int) (time % 365.25);
//        if(year % 4 == 0){
//            md = calcMonth(true, days);
//        }
//        else{
//            md = calcMonth(false, days);
//        }
//        return new int[]{year, md[0], md[1]};
//    }

    private int[] calcMonth(boolean leapYear, int days){

        int  jan = 31 * MIN_IN_DAY, feb, mar = 31 * MIN_IN_DAY, apr = 30 * MIN_IN_DAY, may = 31 * MIN_IN_DAY, jun = 30 * MIN_IN_DAY,
                jul = 31 * MIN_IN_DAY, aug = 31 * MIN_IN_DAY, sep = 30 * MIN_IN_DAY, oct = 31 * MIN_IN_DAY, nov = 30 * MIN_IN_DAY,
                dec = 31 * MIN_IN_DAY;
        if (leapYear) feb = 29 * MIN_IN_DAY;
        else feb = 28 * MIN_IN_DAY;

        Log.i("Time", "Milliseconds s: "+jan);

        if(days <= jan){
            return new int[]{1, jan - days };
        }
        if(days <= jan + feb){
            return new int[]{2, (jan + feb) - days};
        }
        if(days <= jan + feb + mar){
            return new int[]{3, (jan + feb + mar) - days};
        }
        if(days <= jan + feb + mar + apr){
            return new int[]{4, (jan + feb + mar + apr) - days};
        }
        if(days <= jan + feb + mar + apr + may){
            return new int[]{5, (jan + feb + mar + apr + may) - days};
        }
        if(days <= jan + feb + mar + apr + may + jun){
            return new int[]{6, (jan + feb + mar + apr + may + jun) - days};
        }
        if(days <= jan + feb + mar + apr + may + jun + jul){
            return new int[]{7, (jan + feb + mar + apr + may + jun + jul) - days};
        }
        if(days <= jan + feb + mar + apr + may + jun + jul + aug){
            return new int[]{8, (jan + feb + mar + apr + may + jun + jul + aug) - days};
        }
        if(days <= jan + feb + mar + apr + may + jun + jul + aug + sep){
            return new int[]{9, (jan + feb + mar + apr + may + jun + jul + aug + sep) - days};
        }
        if(days <= jan + feb + mar + apr + may + jun + jul + aug + sep + oct){
            return new int[]{10, (jan + feb + mar + apr + may + jun + jul + aug + sep + oct) - days};
        }
        if(days <= jan + feb + mar + apr + may + jun + jul + aug + sep + oct + nov){
            return new int[]{11, (jan + feb + mar + apr + may + jun + jul + aug + sep + oct + nov) - days};
        }
        return new int[]{12, (jan + feb + mar + apr + may + jun + jul + aug + sep + oct + nov + dec) - days};
    }
}
