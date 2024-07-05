package org.yoosha.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private final Properties properties = new Properties();
    private static final String propertiesPath = "my.properties";

    public void load() {
        try {
            new FileInputStream(propertiesPath);
        } catch (FileNotFoundException ex) {
            setValue("maxWorkTime", "25");
            setValue("maxRestTime", "5");
            setValue("telegramBotToken", "");
        }

    }

    public Object getValue(Object key) {
        try {
            FileInputStream in = new FileInputStream(propertiesPath);
            properties.load(in);
        } catch (IOException ex) {System.out.println(ex.getMessage());}

        return properties.getOrDefault(key, null);
    }

    public void setValue(String key, String value) {
        properties.setProperty(key, value);

        try {
            FileOutputStream out = new FileOutputStream(propertiesPath);
            properties.store(out, "Standart settings");
        } catch (IOException ex) {System.out.println();}
    }

    public Properties getProperties() {
        return properties;
    }
}
