package com.commvault.cloudplatform.controller;

import com.commvault.client.service.CvCommonService;
import com.commvault.client.service.impl.CvCommonServiceImpl;
import com.commvault.client.util.CvUtils;
import com.commvault.cloudplatform.dto.BatchInstallContentView;
import com.commvault.cloudplatform.dto.BatchInstallView;
import com.commvault.cloudplatform.dto.OsTypeEnum;
import com.commvault.cloudplatform.exception.ApplicationException;
import com.commvault.cloudplatform.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping(value = "/batch_install")
public class BatchInstallController {

    @Autowired
    private FileService fileService;

    private final static Logger logger = LoggerFactory.getLogger(BatchInstallController.class);
    private final static CvCommonService cvCommonService = new CvCommonServiceImpl();

    @RequestMapping(value = "/fileupload", method = RequestMethod.POST)
    public @ResponseBody
    BatchInstallView getUploadedFile(MultipartHttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        //get the files from the request object
        Iterator<String> itr = request.getFileNames();
        BatchInstallView view = new BatchInstallView();

        while(itr.hasNext()){
            MultipartFile file = request.getFile(itr.next());

            view = fileService.readBatchInstallExcel(file);
        }
        return view;
    }

    @RequestMapping(value = "/install", method = RequestMethod.POST)
    public @ResponseBody
    Map<Object, Object> startInstall(@RequestBody BatchInstallView batchInstallView) {
        Map<Object, Object> result = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = "";
        for(GrantedAuthority authority : authentication.getAuthorities()){
            token = authority.getAuthority();
        }

        List<BatchInstallContentView> installList = batchInstallView.getContentList();

        for(BatchInstallContentView contentView : installList){
            Map<String, Object> resp;

            List<String> componentList = new ArrayList<>();
            OsTypeEnum osType = OsTypeEnum.fromValue(contentView.getOsType());
            if (osType == null) {
                contentView.setComment("Unknown OS Type");
                continue;
            }

            switch(osType) {
                case WINDOWS:
                    componentList.add("File System Core");
                    componentList.add("File System");
                    break;
                case UNIX:
                    componentList.add("File System Core");
                    componentList.add("UNIX File System");
                    break;
            }

            resp = cvCommonService.installClient(
                    contentView.getCommCellName(),
                    contentView.getClientName(),
                    contentView.getOsType(),
                    componentList,
                    contentView.getUserName(),
                    contentView.getPassword(),
                    token);
            String errorMessage = CvUtils.getErrorMessage(resp);
            if(StringUtils.isNotBlank(errorMessage)) {
                logger.error("Failed to install client: {}, {}", contentView.getClientName(), errorMessage);
                contentView.setComment("Failed to install client: " + contentView.getClientName() + ", " + errorMessage);
                continue;
            }

            List<String> jobIds = CvUtils.getMapValue("jobIds", resp);
            if(jobIds == null || jobIds.isEmpty()){
                logger.error("Failed to submit installation job for client: {} ", contentView.getClientName());
                contentView.setComment("Failed to submit installation job");
                continue;
            }

            contentView.setComment("Job Submitted Successfully, id: " + jobIds.get(0));
        }

        result.put("view", batchInstallView);
        return result;
    }
}
