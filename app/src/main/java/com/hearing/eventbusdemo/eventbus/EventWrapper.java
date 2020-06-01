package com.hearing.eventbusdemo.eventbus;

import android.os.Bundle;

import androidx.annotation.NonNull;

/**
 * @author liujiadong
 * @since 2020/5/29
 */
// Event包装类，用来IPC转发
public class EventWrapper {
    public Bundle mBundle;

    public EventWrapper(Bundle bundle) {
        mBundle = bundle;
    }

    @NonNull
    @Override
    public String toString() {
        return mBundle == null ? "null" : mBundle.toString();
    }
}
