// IEventCallback.aidl
package com.hearing.eventbusdemo.eventbus;

import com.hearing.eventbusdemo.eventbus.Event;

interface IEventCallback {
    // 主进程向子进程发送消息
    void notifyEvent(in Event event);
}
