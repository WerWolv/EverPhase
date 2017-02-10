package com.deltabase.everphase.api;

import com.deltabase.everphase.main.Settings;

public class Log {

    public static void d(String tag, String msg) {
        if (!Settings.debugMode) return;
        System.out.println("[DEBUG][" + tag + "] " + msg);
    }


    public static void i(String tag, String msg) {
        System.out.println("[" + tag + "] " + msg);
    }

    public static void wtf(String tag, String msg) {
        System.out.println("![" + tag + "]! " + msg);
    }

}
