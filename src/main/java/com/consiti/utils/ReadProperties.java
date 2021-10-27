/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consiti.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class ReadProperties {

    private static Properties prop = null;

    private static Properties getProperties() {
        if (prop == null) {
            prop = new Properties();
            try {
                String fileName = (new File(ReadProperties.class.getProtectionDomain().getCodeSource().getLocation().toURI())).getParent();
                fileName = fileName + File.separator + "application.properties";
                File file = new File(fileName);
                //System.out.println(fileName);
                if (file.exists()) {
                    prop.load(new FileReader(file));
                } else {
                    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                    prop.load(classloader.getResourceAsStream("application.properties"));
                }
            } catch (FileNotFoundException e) {
                System.out.println("File Not Found:" + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO Exception: " + e.getMessage());
            } catch (URISyntaxException e) {
                System.out.println("URI Exception:" + e.getMessage());
            }
        }
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
