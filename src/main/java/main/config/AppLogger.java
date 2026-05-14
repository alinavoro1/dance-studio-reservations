package main.config;

import java.util.logging.Logger;

public final class AppLogger {

    private AppLogger() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}