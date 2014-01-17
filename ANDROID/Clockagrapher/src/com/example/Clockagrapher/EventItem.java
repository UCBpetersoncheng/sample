package com.example.Clockagrapher;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

/* An event item holds an offset from a base and a string
 */
public class EventItem implements Parcelable{

    private String _id;
    private long _offset;

    /** Formats a HH:MM:SS from an offset. */
    static public String dateString(long offset) {
        return DateFormat.format("kk:mm:ss", -16 * 60 * 60 * 1000 + offset).toString();
    }

    public static final Parcelable.Creator<EventItem> CREATOR =
            new Parcelable.Creator<EventItem>() {
                public EventItem createFromParcel(Parcel in) {
                    return new EventItem(in);
                }

                public EventItem[] newArray(int size) {
                    return new EventItem[size];
                }
            };

    /** Constructs an EventItem from its serialized form. */
    public EventItem(String serialized) {
        String[] words = serialized.split("\t");
        _id = words[0];
        _offset = Long.parseLong(words[1]);
    }

    /** Constructs an EventItem from a tag and an offset. */
    public EventItem(String id, long offset) {
        _id = id;
        _offset = offset;
    }

    /** Constructs an EventItem from a Parcel. */
    public EventItem(Parcel in) {
        _id = in.readString();
        _offset = in.readLong();
    }


    /** Constructs the serialized form of an EventItem. */
    public String serialString() {
        return _id + "\t" + _offset;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeLong(_offset);
    }

    @Override
    public String toString() {
        return String.format("[%s]\t%s", dateString(_offset), _id);
    }

    public String getID() { return _id; }

    public long getOffset() { return _offset; }

    public void setOffSet(long new_offset) { _offset = new_offset; }

    public void setID(String new_id) { _id = new_id; }

}
