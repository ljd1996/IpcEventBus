package com.hearing.eventbusdemo.eventbus;

import androidx.annotation.NonNull;

/**
 * @author liujiadong
 * @since 2020/5/29
 */
// Event包装类，用来IPC转发
public class EventWrapper {
    public Event mEvent;

    public EventWrapper(Event event) {
        mEvent = event;
    }

    @NonNull
    @Override
    public String toString() {
        return mEvent == null ? "null" : mEvent.toString();
    }
}
