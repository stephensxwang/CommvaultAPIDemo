package com.commvault.client.dao;

import com.commvault.client.entity.CvApi;
import org.junit.Assert;
import org.junit.Test;

public class CvApiMapperTest {

    @Test
    public void testQueryByNameAndVersion(){
        CvApiMapper cvApiMapper = new CvApiMapper();
        CvApi cvApi = cvApiMapper.queryByNameAndVersion("login", "default");

        Assert.assertNotNull(cvApi.getBody());

        cvApi = cvApiMapper.queryByNameAndVersion("login", "test");
        Assert.assertNull(cvApi);
    }
}
