package com.commvault.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CvYamlProperties {
    private static final Logger logger = LoggerFactory.getLogger(CvYamlProperties.class);
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();
    private static final String PROPERTY_FILE = "classpath:cv.yml";

    private Map<String, Object> loadProperties(String... resourcesPaths){
        Map<String, Object> properties = new HashMap<>();
        Yaml yaml = new Yaml();

        for(String location : resourcesPaths) {
            logger.debug("Loading properties file from: {}", location);

            Resource ioe = resourceLoader.getResource(location);
            try(InputStream is = ioe.getInputStream()) {
                properties = yaml.load(is);
                logger.debug("loaded properties from: {}", location);
            } catch(IOException e) {
                logger.error("Could not load properties from: {}", location);
            }
        }

        return properties;
    }

    private Map<String, Object> cvProperties;
    private CvYamlProperties(){
        cvProperties = loadProperties(PROPERTY_FILE);
    }

    private static final CvYamlProperties instance = new CvYamlProperties();
    public static CvYamlProperties getInstance() {
        return instance;
    }
    public <T> T getProperty(String propertyName) {
        T result;
        String[] keys = propertyName.split("\\.");

        if(keys.length > 1){
            Map<String, Object> value = CvUtils.getMapValue(keys[0], cvProperties);
            for(int i=1; i<keys.length-1; i++){
                value = CvUtils.getMapValue(keys[i], value);
            }

            result = CvUtils.getMapValue(keys[keys.length-1], value);
        } else {
            result = CvUtils.getMapValue(keys[0], cvProperties);
        }

        return result;
    }

    public synchronized void reloadProperties(){
        cvProperties = loadProperties(PROPERTY_FILE);
    }

    public synchronized boolean saveProperties(){
        Resource ioe = resourceLoader.getResource(PROPERTY_FILE);
        Yaml yaml = new Yaml();
        try {
            yaml.dump(cvProperties, new FileWriter(ioe.getFile()));
        } catch (IOException e) {
            logger.error("Failed to save yml file", e);
            return false;
        }
        return true;
    }
}
