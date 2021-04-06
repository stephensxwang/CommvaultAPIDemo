package com.commvault.cloudplatform.controller;

import com.commvault.client.service.CvCommonService;
import com.commvault.client.service.impl.CvCommonServiceImpl;
import com.commvault.cloudplatform.dto.ClientView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/client")
public class ClientController {

    private final static Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final static CvCommonService cvCommonService = new CvCommonServiceImpl();

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Map<Object, Object> getClientList() {
        Map<Object, Object> result = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = "";
        for(GrantedAuthority authority : authentication.getAuthorities()){
            token = authority.getAuthority();
        }

        List<Map<String, Object>> clientList = cvCommonService.getClient(token);
        result.put(
                "clientList",
                clientList.stream().filter(client -> {
                    Map clientPropsMap = (Map) client.get("clientProps");
                    int clusterType = (int) clientPropsMap.get("clusterType");
                    return clusterType == 0;
                }).
                map(client -> {
                    Map clientMap = (Map) client.get("client");
                    Map clientEntity = (Map) clientMap.get("clientEntity");
                    ClientView view = new ClientView();
                    view.setId((Integer) clientEntity.get("clientId"));
                    view.setName((String) clientEntity.get("clientName"));
                    return view;
                }).
                sorted(Comparator.comparing(ClientView::getName)).
                collect(Collectors.toList())
        );
        return result;
    }
}
