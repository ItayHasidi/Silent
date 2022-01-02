package com.example.silent_ver_1.CalendarAssets;

import android.util.Log;

import java.util.Date;

/**
 * A class that holds an calendar event - start date & time, end date & time, title, id, description and mute status.
 */
public class CalendarEventModel {
    private String description, title;
    private Date start, end;
    private int id;

    //toMute  - whether or not to mute the event.
    boolean toMute;

    public CalendarEventModel(){

    }

    /**
     *
     * @param start
     * @param end
     * @param title
     * @param id
     */
    public CalendarEventModel(Date start, Date end, String title, String id) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.id = Integer.parseInt(id);
    }

    /**
     *
     * @param start
     * @param end
     * @param description
     * @param title
     * @param id
     */
    public CalendarEventModel(Date start, Date end, String description, String title, String id) {
        this.start = start;
        this.end = end;
        this.description = description;
        this.title = title;
        this.id = Integer.parseInt(id);
    }

    /**
     *
     * @param start
     * @param end
     * @param description
     * @param title
     * @param id
     * @param toMute
     */
    public CalendarEventModel(Date start, Date end, String description, String title, String id, boolean toMute) {
        this.start = start;
        this.end = end;
        this.description = description;
        this.title = title;
        this.id = Integer.parseInt(id);
        this.toMute = toMute;
    }

    public Date getStartDate() {
        return start;
    }

    public void setStartDate(Date start) {
        this.start = start;
    }

    public Date getEndDate() {
        return end;
    }

    public void setEndDate(java.util.Date endDate) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isToMute() {
        return toMute;
    }

    public void setToMute(boolean toMute) {
        this.toMute = toMute;
    }

    @Override
    public String toString(){
        return "\nTitle: "+title+
                "\nId: "+id+
                "\nStart Date & Time: "+start+
                "\n  End Date & Time: "+end+
                "\nTo Mute: "+toMute;
    }
}
