package com.commvault.client.util;

import org.junit.Assert;
import org.junit.Test;

public class CvYamlPropertiesTest {
    @Test
    public void testGetProperty(){
        Assert.assertEquals("user", CvYamlProperties.getInstance().getProperty("cvuser"));
        Assert.assertEquals("default", CvYamlProperties.getInstance().getProperty("api.version"));
        Assert.assertNull(CvYamlProperties.getInstance().getProperty("notexist"));
    }

    @Test
    public void testReloadProperties(){
        CvYamlProperties.getInstance().reloadProperties();
        Assert.assertEquals("password", CvYamlProperties.getInstance().getProperty("cvpassword"));
        Assert.assertEquals("http://localhost:81/SearchSvc/CVWebService.svc", CvYamlProperties.getInstance().getProperty("url"));
    }
}
