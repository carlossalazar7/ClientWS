package com.consiti.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Kettler {
    private static Properties prop = null;

    public static void loadProperties(String rutaPropertie) {
        if (prop == null) {
            prop = new Properties();
            try {
                File file = new File(rutaPropertie);
                if (file.exists()) {
                    prop.load(new FileReader(file));
                } else {
                    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                    prop.load(classloader.getResourceAsStream("application.properties"));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Properties getProperties() {
        return prop;
    }

    public static String getProperty(String key) {
        return getProperties().getProperty(key);
    }

    public static int getIntProperty(String key) {
        return Integer.parseInt(getProperties().getProperty(key));
    }

    public static Long getLongProperty(String key) {
        return Long.valueOf(Long.parseLong(getProperties().getProperty(key)));
    }

    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperties().getProperty(key));
    }
}
