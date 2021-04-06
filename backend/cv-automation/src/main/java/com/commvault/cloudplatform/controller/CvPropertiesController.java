package com.commvault.cloudplatform.controller;

import com.commvault.client.service.CvCommonService;
import com.commvault.client.service.impl.CvCommonServiceImpl;
import com.commvault.client.util.CvProperties;
import com.commvault.client.util.CvUtils;
import com.commvault.cloudplatform.dto.ClientGroupView;
import com.commvault.cloudplatform.dto.CvPropertyView;
import com.commvault.cloudplatform.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/cv_property")
public class CvPropertiesController {

    private final static Logger logger = LoggerFactory.getLogger(CvPropertiesController.class);
    private final static CvCommonService cvCommonService = new CvCommonServiceImpl();

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Map<Object, Object> getCvPropertyList() {
        Map<Object, Object> result = new HashMap<>();

        result.put("cvPropertyList", getAll());
        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public @ResponseBody
    Map<Object, Object> updateCvProperty(@PathVariable("id") String cvPropertyId, @RequestBody CvPropertyView view) throws ApplicationException {
        logger.debug("Update cv property, id:{}, properties:{}", cvPropertyId, view);
        Map<Object, Object> result = new HashMap<>();

        if(!CvProperties.getInstance().updateProperty(view.getId(), view.getValue())) {
            logger.error("Failed to update cv property, id \"" + cvPropertyId + "\", value " + view.getValue());
            throw new ApplicationException("Failed to update cv property, id \"" + cvPropertyId + "\", value " + view.getValue());
        }

        return result;
    }

    private List<CvPropertyView> getAll(){
        List<CvPropertyView> result = new ArrayList<>();

        Properties properties = CvProperties.getInstance().getAll();
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        for(Map.Entry<Object, Object> entry : entries){
            CvPropertyView view = new CvPropertyView();
            view.setId((String) entry.getKey());
            view.setValue((String) entry.getValue());

            result.add(view);
        }

        return result;
    }
}
