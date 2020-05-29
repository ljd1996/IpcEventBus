package com.hearing.eventbusdemo.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * @author liujiadong
 * @since 2020/5/29
 */
public class MyEventBus extends EventBus {
    private MyEventBus() {
    }

    private static class SingleTon {
        private static MyEventBus sInstance = new MyEventBus();
    }

    public static MyEventBus getInstance() {
        return SingleTon.sInstance;
    }

    // 转发到主进程和子进程
    public void post(Event event) {
        super.post(event);
        super.post(new EventWrapper(event));
    }

    // 只发送到本进程
    public void postSingle(Event event) {
        super.post(event);
    }
}
