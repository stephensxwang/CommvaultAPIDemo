package com.commvault.cloudplatform.controller;

import com.commvault.client.service.CvCommonService;
import com.commvault.client.service.impl.CvCommonServiceImpl;
import com.commvault.cloudplatform.dto.ClientView;
import com.commvault.cloudplatform.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/job")
public class JobController {

    private final static Logger logger = LoggerFactory.getLogger(JobController.class);
    private final static CvCommonService cvCommonService = new CvCommonServiceImpl();

    @RequestMapping(value = "/{jobId}", method = RequestMethod.GET)
    public @ResponseBody
    Map<Object, Object> getJobSummary(@PathVariable("jobId") String jobId) throws ApplicationException {
        Map<Object, Object> result = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = "";
        for(GrantedAuthority authority : authentication.getAuthorities()){
            token = authority.getAuthority();
        }

        Map<String, Object> jobs = cvCommonService.getJobSummary(jobId, token);
        Map<String, Object> jobSummary = null;
        try{
            jobSummary = (Map) ((Map) ((List) jobs.get("jobs")).get(0)).get("jobSummary");
        } catch (Exception e){
            logger.error("Failed to get job summary for job: {}", jobId, e);
            throw new ApplicationException("No job found for the provided ID: " + jobId);
        }
        result.put("status", jobSummary.get("status"));
        result.put("percentComplete", jobSummary.get("percentComplete"));
        return result;
    }
}
