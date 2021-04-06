package com.commvault.cloudplatform.controller;

import com.commvault.client.service.CvCommonService;
import com.commvault.client.service.impl.CvCommonServiceImpl;
import com.commvault.client.util.CvUtils;
import com.commvault.cloudplatform.dto.ClientGroupView;
import com.commvault.cloudplatform.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/client_group")
public class ClientGroupController {

    private final static Logger logger = LoggerFactory.getLogger(ClientGroupController.class);
    private final static CvCommonService cvCommonService = new CvCommonServiceImpl();

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Map<Object, Object> getClientGroupList() {
        Map<Object, Object> result = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = "";
        for(GrantedAuthority authority : authentication.getAuthorities()){
            token = authority.getAuthority();
        }

        List<Map<String, Object>> clientGroupList = cvCommonService.getClientGroup(token);
        result.put("clientGroupList", clientGroupList.stream().map(group -> {
            ClientGroupView view = new ClientGroupView();
            view.setId((Integer) group.get("Id"));
            view.setName((String) group.get("name"));
            return view;
        }).collect(Collectors.toList()));
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    Map<Object, Object> createClientGroup(@RequestBody ClientGroupView clientGroup) throws ApplicationException {
        Map<Object, Object> result = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = "";
        for(GrantedAuthority authority : authentication.getAuthorities()){
            token = authority.getAuthority();
        }

        Map<String, Object> resp = cvCommonService.createClientGroup(clientGroup.getName(), token);

        String errorMessage = CvUtils.getErrorMessage(resp);
        if(StringUtils.isBlank(errorMessage)) {
            clientGroup.setId(CvUtils.getMapValue("clientGroupId", resp));
        } else {
            logger.error("Failed to create client group \"" + clientGroup.getName() + "\": " + errorMessage);
            throw new ApplicationException("Failed to create client group \"" + clientGroup.getName() + "\": " + errorMessage);
        }
        result.put("clientGroup", clientGroup);
        return result;
    }

    @RequestMapping(value = "/{clientGroupId:.+}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<Object, Object> deleteClientGroup(@PathVariable("clientGroupId") String clientGroupId) throws ApplicationException {
        Map<Object, Object> result = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = "";
        for(GrantedAuthority authority : authentication.getAuthorities()){
            token = authority.getAuthority();
        }

        Map<String, Object> resp = cvCommonService.deleteClientGroup(clientGroupId, token);

        Integer errorCode = CvUtils.getErrorCode(resp);
        if(errorCode == null || errorCode != 0) {
            logger.error("Failed to delete client group with ID \"" + clientGroupId + "\": " + CvUtils.getErrorMessage(resp));
            throw new ApplicationException("Failed to delete client group with ID \"" + clientGroupId + "\": " + CvUtils.getErrorMessage(resp));
        }
        return result;
    }

    @RequestMapping(value = "/{clientGroupId}", method = RequestMethod.POST)
    public @ResponseBody
    Map<Object, Object> updateClientGroup(@PathVariable("clientGroupId") String clientGroupId, @RequestBody ClientGroupView clientGroup) throws ApplicationException {
        logger.debug("Update client group, id:{}, client group properties:{}", clientGroupId, clientGroup);
        Map<Object, Object> result = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = "";
        for(GrantedAuthority authority : authentication.getAuthorities()){
            token = authority.getAuthority();
        }

        Map<String, Object> resp = cvCommonService.updateClientGroup(clientGroupId, clientGroup.getName(), token);

        Integer errorCode = CvUtils.getErrorCode(resp);
        if(errorCode == null || errorCode != 0) {
            logger.error("Failed to update client group, id \"" + clientGroupId + "\", error: " + CvUtils.getErrorMessage(resp));
            throw new ApplicationException("Failed to update client group, id \"" + clientGroupId + "\", error: " + CvUtils.getErrorMessage(resp));
        }

        return result;
    }
}
