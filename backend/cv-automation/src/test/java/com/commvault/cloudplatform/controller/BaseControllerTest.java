package com.commvault.cloudplatform.controller;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseControllerTest {
    @Autowired
    protected TestRestTemplate restTemplate;
    /*@LocalServerPort
    private int randomServerPort;
    private String BASEURL;*/
    protected HttpEntity httpEntity;

    /**
     * 登录
     *
     * @throws Exception
     */
    protected void login() throws Exception {
        //BASEURL = "http://localhost:"+randomServerPort;
        String expectStr = "{\"message\":\"登录成功\"}";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "admin");
        map.add("password", "U2h1WHVuQDEyMzQ1Ng==");
        ResponseEntity responseEntity = restTemplate.postForEntity( "/login", map, String.class);
        //添加cookie以保持状态
        HttpHeaders headers = new HttpHeaders();
        String headerValue = responseEntity.getHeaders().get("Set-Cookie").toString().replace("[", "").replace("]", "");
//        headerValue = headerValue.substring(0, headerValue.indexOf(";"));
        headers.set("Cookie", headerValue);
        httpEntity = new HttpEntity(headers);
        assertEquals(expectStr, responseEntity.getBody());
    }

    /**
     * 登出
     *
     * @throws Exception
     */
    protected void logout() throws Exception {
        String expectStr = "{\"message\":\"登出成功\"}";
        //String result = restTemplate.postForObject("/logout", null, String.class, httpEntity);
        ResponseEntity<String> result = restTemplate.exchange("/logout", HttpMethod.POST, httpEntity, String.class);
        //httpEntity = null;
        assertEquals(expectStr, result.getBody());
    }

    /*@Test
    private void testClientList() throws Exception {
        login();
        //getUserInfo();
        ResponseEntity<Map> result = restTemplate.exchange("/client", HttpMethod.GET, httpEntity, Map.class);
        assertNotNull(result.getBody().get("clientList"));
        logout();
    }*/
    @Test
    public void test(){

    }
}