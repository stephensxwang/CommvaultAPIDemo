package com.commvault.cloudplatform.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class ClientControllerTest extends BaseControllerTest{

    @Test
    public void testGetClientList() throws Exception{
        //restTemplate = restTemplate.withBasicAuth("admin","U2h1WHVuQDEyMzQ1Ng==");
        //Map<Object, Object> result = restTemplate.getForObject("/client", Map.class);
        //System.out.println(result.get("clientList"));
        login();
        ResponseEntity<Map> result = restTemplate.exchange("/client", HttpMethod.GET, httpEntity, Map.class);
        assertNotNull(result.getBody().get("clientList"));
        logout();
    }
}
