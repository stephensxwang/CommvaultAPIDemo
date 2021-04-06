package com.commvault.client.util;

import com.alibaba.fastjson.JSON;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RestfulUtilsTest {
    private static Logger logger = LoggerFactory.getLogger(RestfulUtilsTest.class);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.options().port(8500));

    @Before
    public void setUp(){
        logger.debug("reset wiremock");
        wireMockRule.resetMappings();
    }

    @Test
    public void testDoGetAPIRequest() throws IOException {
        stubFor(get(urlEqualTo("/v1/catalog/service/test_service"))
            .withHeader("Content-Type", equalTo("text/xml"))
            .willReturn(aResponse().withStatus(200).withBody("mock response here")));

        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml");
        Assert.assertEquals("mock response here", RestfulUtils.doGetAPIRequest("http://localhost:8500/v1/catalog/service/test_service", headers));
    }

    @Test
    public void testDoPostAPIRequest() throws IOException {
        stubFor(post(urlEqualTo("/v1/catalog/service/test_service"))
            .withHeader("Authorization", equalTo("Basic d8d74jf82o929d"))
            .withHeader("Accept", equalTo("application/json"))
            .withRequestBody(equalTo("{\"car\":\"Mini Cooper\"}"))
            .willReturn(aResponse()
                .withHeader("content-type", "application/json")
                .withStatus(200)
                .withBody("{\"message\":\"Mini Cooper car response body\", \"success\":true}")
            )
        );

        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Basic d8d74jf82o929d");
        headers.put("Accept", "application/json");
        String response = RestfulUtils.doPostAPIRequest("http://localhost:8500/v1/catalog/service/test_service", headers, "{\"car\":\"Mini Cooper\"}");
        Map<String, Object> result = JSON.parseObject(response);
        Assert.assertEquals("Mini Cooper car response body", result.get("message"));
    }

    @Test
    public void testDoPutAPIRequest() throws IOException {
        stubFor(put(urlEqualTo("/v1/catalog/service/test_service"))
            .withHeader("Authorization", equalTo("Basic d8d74jf82o929d"))
            .withHeader("Accept", equalTo("application/xml"))
            .withRequestBody(equalTo("<car>BMW</car>"))
            .willReturn(aResponse()
                .withHeader("content-type", "application/json")
                .withStatus(200)
                .withBody("{\"message\":\"BMW car response body\", \"success\":true}")
            )
        );

        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Basic d8d74jf82o929d");
        headers.put("Accept", "application/xml");
        String response = RestfulUtils.doPutAPIRequest("http://localhost:8500/v1/catalog/service/test_service", headers, "<car>BMW</car>");
        Map<String, Object> result = JSON.parseObject(response);
        Assert.assertTrue((Boolean) result.get("success"));
    }
}
