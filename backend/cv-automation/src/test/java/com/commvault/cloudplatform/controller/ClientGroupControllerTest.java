package com.commvault.cloudplatform.controller;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class ClientGroupControllerTest extends BaseControllerTest{

    @Test
    public void testGetClientGroupList() throws Exception{
        login();
        ResponseEntity<Map> result = restTemplate.exchange("/client_group", HttpMethod.GET, httpEntity, Map.class);
        assertNotNull(result.getBody().get("clientGroupList"));
        logout();
    }
}
