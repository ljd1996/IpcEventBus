package com.hearing.eventbusdemo.util;

/**
 * @author liujiadong
 * @since 2020/5/29
 */
public class Utils {

    public static void sleep(int t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
