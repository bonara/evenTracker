package com.example.eventracker.model;


import android.os.Parcel;
import android.os.Parcelable;

public class mEvent implements Parcelable {
    private String name;
    private String description;
    private String id;
    private String eventUrl;
    private String start;
    private String end;
    private String timezone;
    private String organizer_id;
    private boolean isFree;
    private String summary;
    private String imageUrl;
    private String venueId;

    public mEvent() {
    }

    public mEvent(String name, String description, String id, String eventUrl, String start, String end, String timezone, String organizer_id, boolean isFree, String summary, String imageUrl, String venueId) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.eventUrl = eventUrl;
        this.start = start;
        this.end = end;
        this.timezone = timezone;
        this.organizer_id = organizer_id;
        this.isFree = isFree;
        this.summary = summary;
        this.imageUrl = imageUrl;
        this.venueId = venueId;
    }

    /**
     * Use when reconstructing User object from parcel
     * This will be used only by the 'CREATOR'
     * @param in a parcel to read this object
     */

    public mEvent(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.id = in.readString();
        this.eventUrl = in.readString();
        this.start = in.readString();
        this.end = in.readString();
        this.timezone = in.readString();
        this.organizer_id = in.readString();
        this.isFree = in.readBoolean();
        this.summary = in.readString();
        this.imageUrl = in.readString();
        this.venueId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(id);
        dest.writeString(eventUrl);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(timezone);
        dest.writeString(organizer_id);
        dest.writeBoolean(isFree);
        dest.writeString(summary);
        dest.writeString(imageUrl);
        dest.writeString(venueId);
    }

    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays
     *
     * If you don’t do that, Android framework will through exception
     * Parcelable protocol requires a Parcelable.Creator object called CREATOR
     */
    public static final Parcelable.Creator<mEvent> CREATOR = new Parcelable.Creator<mEvent>() {

        public mEvent createFromParcel(Parcel in) {
            return new mEvent(in);
        }

        public mEvent[] newArray(int size) {
            return new mEvent[size];
        }
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getOrganizer_id() {
        return organizer_id;
    }

    public void setOrganizer_id(String organizer_id) {
        this.organizer_id = organizer_id;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }
}
