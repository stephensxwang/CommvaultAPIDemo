package com.commvault.client.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CvUtilsTest {

    @Test
    public void testGetError(){
        Map<String, Object> errorMsg = new HashMap<>();

        errorMsg.put("errorCode", -1);
        errorMsg.put("errorString", "test string");

        Assert.assertEquals("-1", CvUtils.getError(errorMsg).get("errorCode"));
        Assert.assertEquals("test string", CvUtils.getError(errorMsg).get("errorMessage"));

        errorMsg.put("errorMessage", "test message");
        Assert.assertEquals("test message", CvUtils.getError(errorMsg).get("errorMessage"));
    }

    @Test
    public void testGenError(){
        Map<String, Object> errorMsg = CvUtils.genError(-1, "test error");

        Assert.assertEquals(-1, errorMsg.get("errorCode"));
        Assert.assertEquals("test error", errorMsg.get("errorMessage"));
    }

    @Test
    public void testGetMapValue(){
        Map<String, Object> errorMsg = new HashMap<>();
        Map<String, Object> errorMap = new HashMap<>();
        Map<String, Object> errorString = new HashMap<>();
        List<Map<String, Object>> errorList = new ArrayList();

        errorMsg.put("errorMessage", "test message");
        errorString.put("errorString", "test string");
        errorList.add(errorString);
        errorMap.put("errorCode", -1);
        errorMap.put("errorMap", errorMsg);
        errorMap.put("errorList", errorList);

        Assert.assertEquals("test message", CvUtils.getMapValue("errorMessage", errorMap));
        Assert.assertEquals("test string", CvUtils.getMapValue("errorString", errorMap));
    }
}
