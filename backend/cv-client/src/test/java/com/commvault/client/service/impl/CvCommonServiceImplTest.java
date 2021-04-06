package com.commvault.client.service.impl;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;

public class CvCommonServiceImplTest {
    private CvCommonServiceImpl cvCommonServiceImpl = new CvCommonServiceImpl();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.options().port(81));

    @Test
    public void testLogin(){
        stubFor(post(urlEqualTo("/SearchSvc/CVWebService.svc/Login"))
            .withHeader("Content-Type", equalTo("application/xml"))
            .withHeader("Accept", equalTo("application/json"))
            .willReturn(aResponse()
                .withHeader("content-type", "application/json")
                .withStatus(200)
                .withBody("{\"token\":\"cv-test-token\"}")
            )
        );

        Map<String, Object> result = cvCommonServiceImpl.login("", "user", "password", "");
        Assert.assertNotNull(result.get("token"));
    }

    @Test
    public void testLogout(){
        stubFor(post(urlEqualTo("/SearchSvc/CVWebService.svc/Logout"))
            .withHeader("Accept", equalTo("application/json"))
            .withHeader("Authtoken", equalTo("cv-test-token"))
            .willReturn(aResponse()
                .withHeader("content-type", "application/json")
                .withStatus(200)
                .withBody("{\"msg\":\"success\"}")
            )
        );

        Assert.assertTrue(cvCommonServiceImpl.logout("cv-test-token"));
    }
}
