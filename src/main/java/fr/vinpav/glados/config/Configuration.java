package fr.vinpav.glados.config;

import fr.vinpav.glados.exception.GladosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Configuration {
    protected static final Logger logger = LoggerFactory.getLogger(Configuration.class);
    
    private Properties properties;
    private String configFileName;

    public Configuration() {
        configFileName = "glados.config";
    }

    public Configuration(String configFileName) {
        this.configFileName = configFileName;
    }

    public void load() throws GladosException {
        properties = new Properties();
        InputStream inputStream;
        inputStream = getClass().getClassLoader().getResourceAsStream(configFileName);

        try {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new GladosException("Error while loading Glados configuration file : " + configFileName);
            }
        } catch (IOException e) {
            logger.error("Error while loading Glados configuration : " + e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("Error while closing Glados configuration stream : " + e);
                }
            }
        }
    }

    public String getProperty (String aPropertyName) {
        return properties.getProperty(aPropertyName);
    }

}
