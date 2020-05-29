package com.hearing.eventbusdemo.eventbus;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * @author liujiadong
 * @since 2020/5/29
 */
public class Event implements Parcelable {
    private Bundle mBundle;

    public Event(Bundle bundle) {
        mBundle = bundle;
    }

    protected Event(Parcel in) {
        mBundle = in.readBundle();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(mBundle);
    }

    @NonNull
    @Override
    public String toString() {
        return mBundle == null ? "null" : mBundle.toString();
    }
}
