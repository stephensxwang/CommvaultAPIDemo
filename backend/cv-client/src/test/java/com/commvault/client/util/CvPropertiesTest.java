package com.commvault.client.util;

import org.junit.Assert;
import org.junit.Test;

public class CvPropertiesTest {
    @Test
    public void testGetProperty(){
        Assert.assertEquals("default", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION));
        Assert.assertEquals("http://localhost:8081/SearchSvc/CVWebService.svc", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_REST_URLPREFIX));
        Assert.assertNull(CvProperties.getInstance().getProperty("notexist"));
    }

    @Test
    public void testReloadProperties(){
        CvProperties.getInstance().reloadProperties();
        Assert.assertEquals("default", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION));
        Assert.assertEquals("http://localhost:8081/SearchSvc/CVWebService.svc", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_REST_URLPREFIX));
    }
}
