package com.example.android.quiz;

/**
 * Created by rishi on 13/9/18.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Event {

    private String eventId;
    private String eventName;
    private String eventDetails;
    private String eventDate;
    private String eventTime;

    public Event()
    {

    }

    public Event(String eventId, String eventName, String eventDetails, String eventDate, String eventTime) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDetails = eventDetails;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }
}

