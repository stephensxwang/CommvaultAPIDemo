package com.commvault.cloudplatform.controller;

import com.commvault.client.service.CvCommonService;
import com.commvault.client.service.impl.CvCommonServiceImpl;
import com.commvault.client.util.CvAgentTypeEnum;
import com.commvault.client.util.CvUtils;
import com.commvault.cloudplatform.dto.BatchConfigContentView;
import com.commvault.cloudplatform.dto.BatchConfigView;
import com.commvault.cloudplatform.exception.ApplicationException;
import com.commvault.cloudplatform.service.FileService;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
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
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/batch_config_groovy")
public class BatchConfigGroovyController {

    @Autowired
    private FileService fileService;

    private final static Logger logger = LoggerFactory.getLogger(BatchConfigGroovyController.class);
    private final static CvCommonService cvCommonService = new CvCommonServiceImpl();

    @RequestMapping(value = "/fileupload", method = RequestMethod.POST)
    public @ResponseBody
    BatchConfigView getUploadedFile(MultipartHttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        //get the files from the request object
        Iterator<String> itr = request.getFileNames();
        BatchConfigView view = new BatchConfigView();

        while(itr.hasNext()){
            MultipartFile file = request.getFile(itr.next());

            view = fileService.readBatchConfigExcel(file);
        }
        return view;
    }

    @RequestMapping(value = "/config", method = RequestMethod.POST)
    public @ResponseBody
    Map<Object, Object> addConfig(@RequestBody BatchConfigView batchConfigView) throws ApplicationException {
        Map<Object, Object> result = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = "";
        for(GrantedAuthority authority : authentication.getAuthorities()){
            token = authority.getAuthority();
        }

        List<BatchConfigContentView> configList = batchConfigView.getContentList();
        for(BatchConfigContentView contentView : configList){
            List<Map<String, Object>> subclientList = cvCommonService.getSubclientListByClientName(contentView.getClientName(), token);
            Map<String, Object> subClient = subclientList.stream().filter(subclient -> subclient.get("appName").equals(CvAgentTypeEnum.FILESYSTEM.getValue()) && subclient.get("subclientName").equals(contentView.getSubclientName())).findAny().orElse(null);

            try{
                CompilerConfiguration config = new CompilerConfiguration();
                config.setSourceEncoding("UTF-8");
                GroovyClassLoader groovyClassLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader(), config);
                File groovyFile = new File("cv-automation/src/main/java/com/commvault/cloudplatform/controller/groovy/BatchConfigRule.groovy");

                Class<?> groovyClass = groovyClassLoader.parseClass(groovyFile);
                GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
                Object methodResult = groovyObject.invokeMethod("getConfigValue", contentView);
                System.out.println(((Map) methodResult).get("clientName"));
                continue;
            } catch (Exception e){
                e.printStackTrace();
            }

            Map<String, Object> resp;
            if(subClient != null) {
                //子客户端已存在
                resp = cvCommonService.addContentForFileSystemSubclientById(((Integer) subClient.get("subclientId")).toString(), contentView.getClientName(), (String) subClient.get("backupsetName"), contentView.getSubclientName(), contentView.getStoragePolicy(), contentView.getPath(), token);
                String errorMessage = CvUtils.getErrorMessage(resp);
                if(StringUtils.isNotBlank(errorMessage)) {
                    logger.error("Failed to add backup path for client: {} subclient: {}", contentView.getClientName(), contentView.getSubclientName());
                    throw new ApplicationException("Failed to add backup path for client: " + contentView.getClientName() + " subclient: " + contentView.getSubclientName());
                }

                List<Map<String, Object>> schedules = cvCommonService.getSchedulesBySubclientId(((Integer) subClient.get("subclientId")).toString(), token);
                if(schedules == null || schedules.isEmpty()) {
                    //添加计划
                    resp = cvCommonService.updateSchedulePolicyEntityAssoc(contentView.getSchedulePolicy(), CvAgentTypeEnum.FILESYSTEM.getValue(), contentView.getClientName(), contentView.getSubclientName(), (String) subClient.get("backupsetName"), "", token);
                    errorMessage = CvUtils.getErrorMessage(resp);
                    if(StringUtils.isNotBlank(errorMessage)) {
                        logger.error("Failed to add schedule to client: {} subclient: {}", contentView.getClientName(), contentView.getSubclientName());
                        throw new ApplicationException("Failed to add schedule to client: " + contentView.getClientName() + " subclient: " + contentView.getSubclientName());
                    }
                }
            } else {
                boolean useVss = false;

                resp = cvCommonService.createFileSystemSubclient(contentView.getClientName(), "defaultBackupSet", contentView.getSubclientName(), contentView.getStoragePolicy(), contentView.getPath(), useVss, token);
                String errorMessage = CvUtils.getErrorMessage(resp);
                if (StringUtils.isNotBlank(errorMessage)) {
                    logger.error("Failed to create subclient for client: {} subclient: {}", contentView.getClientName(), contentView.getSubclientName());
                    throw new ApplicationException("Failed to create subclient for client: " + contentView.getClientName() + " subclient: " + contentView.getSubclientName());
                }

                //添加计划
                resp = cvCommonService.updateSchedulePolicyEntityAssoc(contentView.getSchedulePolicy(), CvAgentTypeEnum.FILESYSTEM.getValue(), contentView.getClientName(), contentView.getSubclientName(), "defaultBackupSet", "", token);
                errorMessage = CvUtils.getErrorMessage(resp);
                if (StringUtils.isNotBlank(errorMessage)) {
                    logger.error("Failed to add schedule to client: {} subclient: {}", contentView.getClientName(), contentView.getSubclientName());
                    throw new ApplicationException("Failed to add schedule to client: " + contentView.getClientName() + " subclient: " + contentView.getSubclientName());
                }
            }
        }

        return result;
    }
}
