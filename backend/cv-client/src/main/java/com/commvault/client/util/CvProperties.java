package com.commvault.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

public class CvProperties {

	private static final Logger logger = LoggerFactory.getLogger(CvProperties.class);
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	private static final String PROPERTY_FILE = "classpath:cv.properties";
	
	private Properties loadProperties(String... resourcesPaths) {
		Properties props = new Properties();
		
		for(String location : resourcesPaths) {
			logger.debug("Loading properties file from: {}", location);
			
			Resource ioe = resourceLoader.getResource(location);
			try(InputStream is = ioe.getInputStream()) {
				props.load(is);
				logger.debug("loaded properties from: {}", location);
			} catch(IOException e) {
				logger.error("Could not load properties from: {}", location);
			} 
		}
		
		return props;
	}
	
	private Properties cvProperties;
	private CvProperties() {
		cvProperties = loadProperties(PROPERTY_FILE);
	}
	
	private static final CvProperties instance = new CvProperties();
	public static CvProperties getInstance() {
		return instance;
	}
	public String getProperty(String propertyName) {
	    String result = cvProperties.getProperty(propertyName);

	    return result == null ? null : new String(result.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
	}

	public synchronized void reloadProperties(){
		cvProperties = loadProperties(PROPERTY_FILE);
	}

	public synchronized boolean saveProperties(){
        Resource ioe = resourceLoader.getResource(PROPERTY_FILE);
        try {
            cvProperties.store(new FileOutputStream(ioe.getFile()), "");
        } catch (IOException e) {
            logger.error("Failed to save properties file", e);
            return false;
        }
        return true;
    }

    public Properties getAll(){
	    return cvProperties;
    }

    public synchronized boolean updateProperty(String id, String value){
	    String oldValue = (String) cvProperties.get(id);
	    oldValue = oldValue == null ? null : new String(oldValue.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

	    cvProperties.setProperty(id, value);
	    if(saveProperties()){
	        return true;
        }

        cvProperties.setProperty(id, oldValue);
        return false;
    }
}
