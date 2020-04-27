package com.moonactive.automation.conf;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import java.util.Map.Entry;

public class EnvConf {

    private static Configuration properties;

    static {
        load();
    }

    private EnvConf() {}

    private static void load() {
        Configurations configs = new Configurations();
        try {
            properties = configs.properties(getEnvPropFilePath());
            for(Entry p : System.getProperties().entrySet()){
                String key = (String)p.getKey();
                properties.setProperty(key , p.getValue());
            }
        } catch (ConfigurationException e) {
            System.err.println("failed to load '" + getEnvPropFilePath() + "' resource file");
            throw new RuntimeException(e);
        }
    }

    private static String getEnvPropFilePath(){
        String envPath = System.getProperty("env.conf");
        envPath = (envPath == null) ? "env/env.properties" : envPath;
        return envPath;
//        return (envPath == null) ? "C:\\Git\\seleniumProject\\src\\main\\resources\\env\\env.properties" : envPath;
    }

    public static boolean isDevelopmentEnv(){
        return getEnvName() == EnvName.DEVELOPMENT;
    }

    public static String getProperty(String key){
        return properties.getString(key);
    }

    private static EnvName getEnvName(){
        return EnvName.valueOf(getProperty("env.name"));
    }

}
