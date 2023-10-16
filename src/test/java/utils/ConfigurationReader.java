package utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationReader {
    static Properties properties;
    static{

        String path = "src/test/resources/configs/configuration.properties";

        try{
            FileInputStream file = new FileInputStream(path);
            properties = new Properties();
            properties.load(file);
        }catch (Exception e){

        }
    }
    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}