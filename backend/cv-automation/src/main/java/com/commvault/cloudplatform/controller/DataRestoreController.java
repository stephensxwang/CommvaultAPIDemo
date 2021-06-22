package com.commvault.cloudplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.commvault.client.service.CvCommonService;
import com.commvault.client.service.impl.CvCommonServiceImpl;
import com.commvault.client.util.CvAgentTypeEnum;
import com.commvault.client.util.CvUtils;
import com.commvault.cloudplatform.dto.DataRestoreView;
import com.commvault.cloudplatform.dto.Db2DataRestoreView;
import com.commvault.cloudplatform.dto.MssqlDataRestoreView;
import com.commvault.cloudplatform.dto.OracleDataRestoreView;
import com.commvault.cloudplatform.exception.ApplicationException;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/data_restore")
public class DataRestoreController {

    private final static Logger logger = LoggerFactory.getLogger(DataRestoreController.class);
    private final static CvCommonService cvCommonService = new CvCommonServiceImpl();

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    Map<Object, Object> restoreBackupData(@RequestBody DataRestoreView dataRestoreView) throws ApplicationException {
        logger.debug("Restore backup data {}... from client: {} to client: {}", dataRestoreView.getFilePaths().get(0), dataRestoreView.getSrcClientName(), dataRestoreView.getTargetClientName());
        Map<Object, Object> result = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = "";
        for(GrantedAuthority authority : authentication.getAuthorities()){
            token = authority.getAuthority();
        }

        List<Map<String, Object>> subclientList = cvCommonService.getSubclientListByClientName(dataRestoreView.getSrcClientName(), token);
        Map<String, Object> srcSubclient = null;
        for(Map<String, Object> subclient : subclientList){
            if(subclient.get("appName").equals(CvAgentTypeEnum.FILESYSTEM.getValue()) || subclient.get("appName").equals(CvAgentTypeEnum.NAS.getValue())){
                Integer id = (Integer) subclient.get("subclientId");
                Map<String, Object> subclientPorperties = cvCommonService.getSubclientProperties(id.toString(), token);
                List<Map<String, Object>> content = CvUtils.getMapValue("content", subclientPorperties);
                if(content == null) continue;
                for(Map<String, Object> obj : content){
                    if(StringUtils.isNotBlank((String) obj.get("path"))) {
                        String path = (String) obj.get("path");
                        if(path.length() > 1 && (path.endsWith("/") || path.endsWith("\\"))) path = path.substring(0, path.length() - 1);
                        if(dataRestoreView.getFilePaths().get(0).startsWith(path)){
                            srcSubclient = subclient;
                            break;
                        }
                    }
                }
                if(srcSubclient != null) break;
            }
        }

        if(srcSubclient == null){
            throw new ApplicationException("No subclient found for client: " + dataRestoreView.getSrcClientName());
        }

        Map<String, Object> resp;
        if(srcSubclient != null){
            try {
                resp = cvCommonService.browseBackupData(
                        (String) srcSubclient.get("clientName"),
                        String.valueOf(srcSubclient.get("applicationId")),
                        String.valueOf(srcSubclient.get("clientId")),
                        String.valueOf(srcSubclient.get("subclientId")),
                        String.valueOf(srcSubclient.get("backupsetId")),
                        String.valueOf(srcSubclient.get("instanceId")),
                        String.valueOf((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataRestoreView.getBackupTime()).getTime())/1000L - 1L),
                        String.valueOf((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataRestoreView.getBackupTime()).getTime())/1000L + 86401L),
                        token
                        );
            } catch (Exception e) {
                logger.error("Failed to browse backup data for client: {} subclient: {}", dataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"), e);
                throw new ApplicationException("Failed to browse backup data for client: " + dataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName"));
            }

            Set<Integer> jobIds = new HashSet<Integer>();
            List browseResponses = (List) resp.get("browseResponses");
            if(browseResponses != null && !browseResponses.isEmpty()){
                try{
                    Map browseResult = (Map) ((Map) browseResponses.get(0)).get("browseResult");
                    List dataResultSet = (List) browseResult.get("dataResultSet");
                    for(Object data : dataResultSet){
                        Map advancedData = (Map) ((Map) data).get("advancedData");
                        jobIds.add((Integer) advancedData.get("backupJobId"));

                        logger.debug("Backup job:" + advancedData.get("backupJobId") + " size:" + ((Map) ((Map) advancedData.get("browseMetaData")).get("indexing")).get("folderSize") + " Bytes");
                    }
                } catch (Exception e){
                    logger.error("No backup job found for client: {} subclient: {} at time: {}", dataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"), dataRestoreView.getBackupTime(), e);
                    throw new ApplicationException("No backup job found for client: " + dataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName") + " at time: " + dataRestoreView.getBackupTime());
                }
            }

            if(jobIds.isEmpty() && srcSubclient.get("appName").equals(CvAgentTypeEnum.FILESYSTEM.getValue())){
                logger.error("No backup job found for client: {} subclient: {} at time: {}", dataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"), dataRestoreView.getBackupTime());
                throw new ApplicationException("No backup job found for client: " + dataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName") + " at time: " + dataRestoreView.getBackupTime());
            }

            for(Integer jobId : jobIds){
                resp = cvCommonService.listMediaForAJob(jobId.toString(), token);
                String mediaInfo = (String) resp.get("response");

                if(StringUtils.isBlank(mediaInfo) || mediaInfo.indexOf("-------") <=0 ){
                    logger.error("No media found for job: {}", jobId);
                    throw new ApplicationException("No media found for job: " + jobId);
                }
                String[] mediaInfoArray = mediaInfo.split("\n");
                String title = mediaInfoArray[0];
                for(int i=2; i<mediaInfoArray.length; i++){
                    String info = mediaInfoArray[i];

                    if(StringUtils.isNotBlank(info.trim())){
                        String barcode = info.substring(title.indexOf("BARCODE"), title.indexOf("LOCATION"));
                        String location = info.substring(title.indexOf("LOCATION"), title.indexOf("LIBRARY"));
                        String library = info.substring(title.indexOf("LIBRARY"), title.indexOf("STORAGEPOLICY"));
                        String sp = info.substring(title.indexOf("STORAGEPOLICY"), title.indexOf("COPYNAME"));
                        String copyName = info.substring(title.indexOf("COPYNAME"));

                        logger.debug("Media location for backup job:" + jobId + " location:" + location);
                    }
                }
            }

            Integer restoreJobId;
            try {
                if(((String) srcSubclient.get("appName")).trim().startsWith(CvAgentTypeEnum.NAS.getValue())){
                    resp = cvCommonService.restoreNdmpBackupData(
                            String.valueOf(srcSubclient.get("clientName")),
                            String.valueOf(srcSubclient.get("subclientName")),
                            String.valueOf(srcSubclient.get("backupsetName")),
                            String.valueOf(srcSubclient.get("instanceName")),
                            dataRestoreView.getFilePaths().get(0),
                            String.valueOf((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataRestoreView.getBackupTime()).getTime())/1000L - 1L),
                            String.valueOf((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataRestoreView.getBackupTime()).getTime())/1000L + 86401L),
                            dataRestoreView.getTargetClientName(),
                            dataRestoreView.getSrcClientName().equals(dataRestoreView.getTargetClientName()) ? "true" : "false",
                            dataRestoreView.getDestPath(),
                            token
                    );

                    List<String> ids = CvUtils.getMapValue("jobIds", resp);
                    restoreJobId = Integer.valueOf(ids.get(0));
                } else {
                    /*resp = cvCommonService.restoreBackupData(
                            String.valueOf(srcSubclient.get("clientId")),
                            String.valueOf(srcSubclient.get("applicationId")),
                            String.valueOf(srcSubclient.get("instanceId")),
                            String.valueOf(srcSubclient.get("backupsetId")),
                            String.valueOf(srcSubclient.get("subclientId")),
                            String.valueOf((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataRestoreView.getBackupTime()).getTime())/1000L - 1L),
                            String.valueOf((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataRestoreView.getBackupTime()).getTime())/1000L + 86401L),
                            String.valueOf(dataRestoreView.getTargetClientId()),
                            dataRestoreView.getTargetClientName(),
                            dataRestoreView.getSrcClientName().equals(dataRestoreView.getTargetClientName()) ? "1" : "0",
                            dataRestoreView.getDestPath(),
                            dataRestoreView.getFilePaths(),
                            token
                    );

                    restoreJobId = (Integer) resp.get("jobId");*/
                    SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    resp = cvCommonService.restoreFilesystemBackupData(
                            String.valueOf(srcSubclient.get("clientName")),
                            String.valueOf(srcSubclient.get("subclientName")),
                            String.valueOf(srcSubclient.get("backupsetName")),
                            String.valueOf(srcSubclient.get("instanceName")),
                            dataRestoreView.getFilePaths().get(0),
                            dateFormater.format(((dateFormater.parse(dataRestoreView.getBackupTime()).getTime())/1000L - 1L)*1000L),
                            dateFormater.format(((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataRestoreView.getBackupTime()).getTime())/1000L + 86401L)*1000L),
                            dataRestoreView.getTargetClientName(),
                            "false",
                            dataRestoreView.getDestPath(),
                            token
                    );
                    String errorMessage = CvUtils.getErrorMessage(resp);
                    if(StringUtils.isNotBlank(errorMessage)){
                        throw new Exception(errorMessage);
                    }

                    List<String> ids = CvUtils.getMapValue("jobIds", resp);
                    restoreJobId = Integer.valueOf(ids.get(0));
                }
            } catch (Exception e) {
                logger.error("Failed to restore backup data for client: {} subclient: {}", dataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"), e);
                throw new ApplicationException("Failed to restore backup data for client: " + dataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName"));
            }

            if(restoreJobId == null){
                logger.error("Failed to restore backup data for client: {} subclient: {}", dataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"));
                throw new ApplicationException("Failed to restore backup data for client: " + dataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName"));
            }

            result.put("restoreJobId", restoreJobId);
        }

        return result;
    }

    @RequestMapping(value = "/db2", method = RequestMethod.POST)
    public @ResponseBody
    Map<Object, Object> restoreDb2BackupData(@RequestBody Db2DataRestoreView db2DataRestoreView) throws ApplicationException {
        logger.debug("Restore DB2 backup {}... from client: {} to client: {}", db2DataRestoreView.getSrcBackupsetName(), db2DataRestoreView.getSrcClientName(), db2DataRestoreView.getDestClientName());
        Map<Object, Object> result = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = "";
        for(GrantedAuthority authority : authentication.getAuthorities()){
            token = authority.getAuthority();
        }

        List<Map<String, Object>> subclientList = cvCommonService.getSubclientListByClientName(db2DataRestoreView.getSrcClientName(), token);
        Map<String, Object> srcSubclient = null;
        for(Map<String, Object> subclient : subclientList) {
            if (((String) subclient.get("appName")).startsWith(CvAgentTypeEnum.DB2.getValue())) {

                if(((String) subclient.get("backupsetName")).equalsIgnoreCase(db2DataRestoreView.getSrcBackupsetName()) && ((String) subclient.get("subclientName")).equalsIgnoreCase(db2DataRestoreView.getSrcSubclientName())){
                    srcSubclient = subclient;
                }
                if(srcSubclient != null) break;
            }
        }

        if(srcSubclient == null){
            throw new ApplicationException("No subclient found for client: " + db2DataRestoreView.getSrcClientName());
        }

        Map<String, Object> resp;
        resp = cvCommonService.getDb2BackupJobInfo(
                String.valueOf(db2DataRestoreView.getSrcClientName()),
                String.valueOf(db2DataRestoreView.getSrcInstanceName()),
                String.valueOf(db2DataRestoreView.getSrcBackupsetName()),
                token
        );

        Object fieldValue = CvUtils.getMapValue("FieldValue", resp);
        List<Map<String, String>> jobList = new ArrayList();
        if(fieldValue instanceof List){
            jobList = (List<Map<String, String>>) fieldValue;
        }
        if(fieldValue instanceof Map){
            jobList.add((Map<String, String>) fieldValue);
        }

        Date backupDate = null;
        try {
            backupDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(db2DataRestoreView.getBackupDate());
        } catch (ParseException e) {
            logger.error("Failed to parse specified date:{} for client: {} subclient: {}", db2DataRestoreView.getBackupDate(), db2DataRestoreView.getSrcClientName(), db2DataRestoreView.getSrcSubclientName(), e);
            throw new ApplicationException("Failed to parse specified date:{} for client: {} subclient: {}" + db2DataRestoreView.getBackupDate() + db2DataRestoreView.getSrcClientName() + " subclient: " + db2DataRestoreView.getSrcSubclientName());
        }
        String backupDateString = new SimpleDateFormat("yyyyMMdd").format(backupDate);
        Map<String, String> jobInfo = jobList.stream().filter(job -> job.get("@__TIMESTAMP___").startsWith(backupDateString)).findAny().orElse(null);
        //jobInfo.get("@JOB_ID");
        //jobInfo.get("@__TIMESTAMP___");
        //jobInfo.get("@STREAMS");

        if(jobInfo == null){
            logger.error("No backup job found for client: {} subclient: {} at time: {}", db2DataRestoreView.getSrcClientName(), db2DataRestoreView.getSrcSubclientName(), db2DataRestoreView.getBackupDate());
            throw new ApplicationException("No backup job found for client: " + db2DataRestoreView.getSrcClientName() + " subclient: " + db2DataRestoreView.getSrcSubclientName() + " at time: " + db2DataRestoreView.getBackupDate());
        }

        try{
            resp = cvCommonService.restoreDb2BackupData(
                    db2DataRestoreView.getSrcSubclientName(),
                    db2DataRestoreView.getSrcBackupsetName(),
                    db2DataRestoreView.getSrcInstanceName(),
                    db2DataRestoreView.getSrcClientName(),
                    db2DataRestoreView.getDestClientName(),
                    db2DataRestoreView.getDestInstanceName(),
                    db2DataRestoreView.getTargetDbName(),
                    db2DataRestoreView.getTargetPath(),
                    jobInfo.get("@__TIMESTAMP___") + "," + jobInfo.get("@STREAMS") + ",1,0,0,",
                    token);
        } catch (Exception e) {
            logger.error("Failed to restore DB2 backup data for client: {} subclient: {}", db2DataRestoreView.getSrcClientName(), db2DataRestoreView.getSrcSubclientName(), e);
            throw new ApplicationException("Failed to restore DB2 backup data for client: " + db2DataRestoreView.getSrcClientName() + " subclient: " + db2DataRestoreView.getSrcSubclientName());
        }

        List<String> jobIds = CvUtils.getMapValue("jobIds", resp);
        if(jobIds == null || jobIds.isEmpty()){
            logger.error("Failed to restore backup data for client: {} subclient: {}", db2DataRestoreView.getSrcClientName(), db2DataRestoreView.getSrcSubclientName());
            throw new ApplicationException("Failed to restore backup data for client: " + db2DataRestoreView.getSrcClientName() + " subclient: " + db2DataRestoreView.getSrcSubclientName());
        }

        result.put("restoreJobId", jobIds.get(0));
        return result;
    }

    @RequestMapping(value = "/oracle", method = RequestMethod.POST)
    public @ResponseBody
    Map<Object, Object> restoreOracleBackupData(@RequestBody OracleDataRestoreView oracleDataRestoreView) throws ApplicationException {
        logger.debug("Restore Oracle backup {}... from client: {} to client: {}", oracleDataRestoreView.getSrcInstanceName(), oracleDataRestoreView.getSrcClientName(), oracleDataRestoreView.getDestClientName());
        Map<Object, Object> result = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = "";
        for(GrantedAuthority authority : authentication.getAuthorities()){
            token = authority.getAuthority();
        }

        List<Map<String, Object>> subclientList = cvCommonService.getSubclientListByClientName(oracleDataRestoreView.getSrcClientName(), token);
        Map<String, Object> srcSubclient = null;
        for(Map<String, Object> subclient : subclientList) {
            if (((String) subclient.get("appName")).startsWith(CvAgentTypeEnum.ORACLE.getValue())) {

                if(((String) subclient.get("instanceName")).equalsIgnoreCase(oracleDataRestoreView.getSrcInstanceName()) && ((String) subclient.get("subclientName")).equalsIgnoreCase(oracleDataRestoreView.getSrcSubclientName())){
                    srcSubclient = subclient;
                }
                if(srcSubclient != null) break;
            }
        }

        if(srcSubclient == null){
            throw new ApplicationException("No subclient found for client: " + oracleDataRestoreView.getSrcClientName());
        }

        Date backupDate = null;
        try {
            backupDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(oracleDataRestoreView.getBackupDate());
        } catch (ParseException e) {
            logger.error("Failed to parse specified date:{} for client: {} subclient: {}", oracleDataRestoreView.getBackupDate(), oracleDataRestoreView.getSrcClientName(), oracleDataRestoreView.getSrcSubclientName(), e);
            throw new ApplicationException("Failed to parse specified date:{} for client: {} subclient: {}" + oracleDataRestoreView.getBackupDate() + oracleDataRestoreView.getSrcClientName() + " subclient: " + oracleDataRestoreView.getSrcSubclientName());
        }

        Map<String, Object> resp;
        resp = cvCommonService.getJobList(String.valueOf(srcSubclient.get("subclientId")), String.valueOf(backupDate.getTime()/1000L-1L), String.valueOf(backupDate.getTime()/1000L+86401L), token);
        List<Map<String, Object>> jobs = CvUtils.getMapValue("jobs", resp);
        String jobId="-1", jobStartTime="-1", jobEndTime="-1", scn="";
        if(jobs != null && !jobs.isEmpty()){
            Map<String, Object> jobSummary = jobs.get(0);
            jobId = String.valueOf((Object) CvUtils.getMapValue("jobId", jobSummary));
            jobStartTime = String.valueOf((Object) CvUtils.getMapValue("jobStartTime", jobSummary));
            jobEndTime = String.valueOf((Object) CvUtils.getMapValue("jobEndTime", jobSummary));
        }
        if(StringUtils.isBlank(jobId) || jobId.equals("-1")){
            logger.error("No backup job found for client: {} subclient: {} at time: {}", oracleDataRestoreView.getSrcClientName(), oracleDataRestoreView.getSrcSubclientName(), oracleDataRestoreView.getBackupDate());
            throw new ApplicationException("No backup job found for client: " + oracleDataRestoreView.getSrcClientName() + " subclient: " + oracleDataRestoreView.getSrcSubclientName() + " at time: " + oracleDataRestoreView.getBackupDate());
        }

        String controlFileName;
        resp = cvCommonService.executeWorkflow(
                "GetOracleControlFileName",
                "\"srcClient\":\"" + oracleDataRestoreView.getDestClientName() + "\"," + "\"jobId\":" + jobId + "",
                token);

        String workflowResp = CvUtils.getMapValue("outputs", resp);

        JSONObject object = JSONObject.parseObject(workflowResp);
        controlFileName = object.getString("scriptOutput");
        if(StringUtils.isBlank(controlFileName)){
            logger.error("Failed to get control file name for client: {} subclient: {}", oracleDataRestoreView.getSrcClientName(), oracleDataRestoreView.getSrcSubclientName());
            throw new ApplicationException("Failed to get control file name for client: " + oracleDataRestoreView.getSrcClientName() + " subclient: " + oracleDataRestoreView.getSrcSubclientName());
        }

        resp = cvCommonService.executeWorkflow(
                "GenOraclePfile",
                "\"client\":\"" + oracleDataRestoreView.getDestClientName() + "\"," +
                        "\"sourceDatabase\":\"" + oracleDataRestoreView.getSrcInstanceName() + "\"," +
                        "\"targetDatabase\":\"" + oracleDataRestoreView.getDestInstanceName() + "\"," +
                        "\"targetPath\":\"" + oracleDataRestoreView.getTargetPath() + "\"",
                token);

        workflowResp = CvUtils.getMapValue("outputs", resp);
        object = JSONObject.parseObject(workflowResp);
        if(!object.getString("msg").equals("0")){
            logger.error("Failed to generate pfile on client: {}", oracleDataRestoreView.getDestClientName());
            throw new ApplicationException("Failed to generate pfile on client: {}" + oracleDataRestoreView.getDestClientName());
        }

        SimpleDateFormat  dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        resp = cvCommonService.restoreOracleBackupData(
                oracleDataRestoreView.getSrcClientName(),
                oracleDataRestoreView.getSrcSubclientName(),
                (String) srcSubclient.get("backupsetName"),
                oracleDataRestoreView.getSrcInstanceName(),
                dateFormatter.format(new Date(Long.parseLong(jobStartTime) * 1000L)),
                dateFormatter.format(new Date(Long.parseLong(jobEndTime) * 1000L)),
                oracleDataRestoreView.getDestClientName(),
                oracleDataRestoreView.getDestInstanceName(),
                oracleDataRestoreView.getTargetPath(),
                controlFileName,
                scn,
                token
                );

        List<String> jobIds = CvUtils.getMapValue("jobIds", resp);
        if(jobIds == null || jobIds.isEmpty()){
            logger.error("Failed to restore backup data for client: {} subclient: {}", oracleDataRestoreView.getSrcClientName(), oracleDataRestoreView.getSrcSubclientName());
            throw new ApplicationException("Failed to restore backup data for client: " + oracleDataRestoreView.getSrcClientName() + " subclient: " + oracleDataRestoreView.getSrcSubclientName());
        }

        result.put("restoreJobId", jobIds.get(0));
        return result;
    }

    @RequestMapping(value = "/mssql", method = RequestMethod.POST)
    public @ResponseBody
    Map<Object, Object> restoreMsSQLBackupData(@RequestBody MssqlDataRestoreView mssqlDataRestoreView) throws ApplicationException {
        logger.debug("Restore SQL Server DB {}... from client: {} to client: {}", mssqlDataRestoreView.getSrcDbName(), mssqlDataRestoreView.getSrcClientName(), mssqlDataRestoreView.getDestClientName());
        Map<Object, Object> result = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = "";
        for(GrantedAuthority authority : authentication.getAuthorities()){
            token = authority.getAuthority();
        }

        List<Map<String, Object>> subclientList = cvCommonService.getSubclientListByClientName(mssqlDataRestoreView.getSrcClientName(), token);
        Map<String, Object> srcSubclient = null;
        for(Map<String, Object> subclient : subclientList) {
            if (((String) subclient.get("appName")).startsWith(CvAgentTypeEnum.SQLSERVER.getValue())) {

                if(((String) subclient.get("instanceName")).equalsIgnoreCase(mssqlDataRestoreView.getSrcInstanceName())){
                    Integer id = (Integer) subclient.get("subclientId");
                    Map<String, Object> subclientPorperties = cvCommonService.getSubclientProperties(id.toString(), token);
                    List<Map<String, Object>> content = CvUtils.getMapValue("content", subclientPorperties);
                    if(content == null) continue;
                    for(Map<String, Object> obj : content){
                        String dbName = CvUtils.getMapValue("databaseName", obj);
                        if(StringUtils.isNotBlank(dbName) && dbName.equals(mssqlDataRestoreView.getSrcDbName())) {
                            srcSubclient = subclient;
                            break;
                        }
                    }
                }
                if(srcSubclient != null) break;
            }
        }

        if(srcSubclient == null){
            throw new ApplicationException("No subclient found for client: " + mssqlDataRestoreView.getSrcClientName());
        }

        Date backupDate = null;
        try {
            backupDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mssqlDataRestoreView.getBackupDate());
        } catch (ParseException e) {
            logger.error("Failed to parse specified date:{} for client: {} subclient: {}", mssqlDataRestoreView.getBackupDate(), mssqlDataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"), e);
            throw new ApplicationException("Failed to parse specified date:{} for client: {} subclient: {}" + mssqlDataRestoreView.getBackupDate() + mssqlDataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName"));
        }

        Map<String, Object> resp;
        resp = cvCommonService.getJobList(String.valueOf(srcSubclient.get("subclientId")), String.valueOf(backupDate.getTime()/1000L-1L), String.valueOf(backupDate.getTime()/1000L+86401L), token);
        List<Map<String, Object>> jobs = CvUtils.getMapValue("jobs", resp);
        String jobId="-1", jobStartTime="-1", jobEndTime="-1";
        if(jobs != null && !jobs.isEmpty()){
            Map<String, Object> jobSummary = jobs.get(0);
            jobId = String.valueOf((Object) CvUtils.getMapValue("jobId", jobSummary));
            jobStartTime = String.valueOf((Object) CvUtils.getMapValue("jobStartTime", jobSummary));
            jobEndTime = String.valueOf((Object) CvUtils.getMapValue("jobEndTime", jobSummary));
        }
        if(StringUtils.isBlank(jobId) || jobId.equals("-1")){
            logger.error("No backup job found for client: {} subclient: {} at time: {}", mssqlDataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"), mssqlDataRestoreView.getBackupDate());
            throw new ApplicationException("No backup job found for client: " + mssqlDataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName") + " at time: " + mssqlDataRestoreView.getBackupDate());
        }

        List<String> deviceList = new ArrayList<>();
        try{
            resp = cvCommonService.getMssqlRestoreOptions(
                    String.valueOf(srcSubclient.get("instanceId")),
                    mssqlDataRestoreView.getSrcDbName(),
                    token);

            List<Map<String, Object>> devices = CvUtils.getMapValue("sqlDbdeviceItem", resp);

            for(Map<String, Object> device : devices) {
                String fileName = (String) device.get("fileName");
                String logicalFileName = (String) device.get("logicalFileName");
                deviceList.add("|" + mssqlDataRestoreView.getSrcDbName() +
                        "|#12!" + mssqlDataRestoreView.getTargetDbName() +
                        "|#12!" + logicalFileName +
                        "|#12!" + mssqlDataRestoreView.getTargetPath() + fileName.substring(fileName.indexOf("\\" + logicalFileName + ".")) + "|#12!" +
                        fileName);
            }
        } catch (Exception e) {
            logger.error("Failed to get files for given database(s) and instance for client: {} subclient: {}", mssqlDataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"), e);
            throw new ApplicationException("Failed to restore SQL server backup data for client: " + mssqlDataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName"));
        }

        try{
            SimpleDateFormat  dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            resp = cvCommonService.restoreMssqlBackupData(
                    mssqlDataRestoreView.getSrcDbName(),
                    String.valueOf(srcSubclient.get("subclientName")),
                    mssqlDataRestoreView.getSrcInstanceName(),
                    mssqlDataRestoreView.getSrcClientName(),
                    mssqlDataRestoreView.getDestClientName(),
                    mssqlDataRestoreView.getDestInstanceName(),
                    mssqlDataRestoreView.getTargetDbName(),
                    mssqlDataRestoreView.getTargetPath(),
                    dateFormatter.format(new Date(Long.parseLong(jobEndTime) * 1000L)),
                    deviceList,
                    token);
        } catch (Exception e) {
            logger.error("Failed to restore DB2 backup data for client: {} subclient: {}", mssqlDataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"), e);
            throw new ApplicationException("Failed to restore DB2 backup data for client: " + mssqlDataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName"));
        }

        List<String> jobIds = CvUtils.getMapValue("jobIds", resp);
        if(jobIds == null || jobIds.isEmpty()){
            logger.error("Failed to restore backup data for client: {} subclient: {}", mssqlDataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"));
            throw new ApplicationException("Failed to restore backup data for client: " + mssqlDataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName"));
        }

        result.put("restoreJobId", jobIds.get(0));
        return result;
    }

    @RequestMapping(value = "/mysql", method = RequestMethod.POST)
    public @ResponseBody
    Map<Object, Object> restoreMySQLBackupData(@RequestBody MssqlDataRestoreView mysqlDataRestoreView) throws ApplicationException {
        logger.debug("Restore Mysql DB {}... from client: {} to client: {}", mysqlDataRestoreView.getSrcDbName(), mysqlDataRestoreView.getSrcClientName(), mysqlDataRestoreView.getDestClientName());
        Map<Object, Object> result = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = "";
        for(GrantedAuthority authority : authentication.getAuthorities()){
            token = authority.getAuthority();
        }

        List<Map<String, Object>> subclientList = cvCommonService.getSubclientListByClientName(mysqlDataRestoreView.getSrcClientName(), token);
        Map<String, Object> srcSubclient = null;
        for(Map<String, Object> subclient : subclientList) {
            if (((String) subclient.get("appName")).startsWith(CvAgentTypeEnum.MYSQL.getValue())) {

                if(((String) subclient.get("instanceName")).equalsIgnoreCase(mysqlDataRestoreView.getSrcInstanceName())){
                    Integer id = (Integer) subclient.get("subclientId");
                    Map<String, Object> subclientPorperties = cvCommonService.getSubclientProperties(id.toString(), token);
                    List<Map<String, Object>> content = CvUtils.getMapValue("content", subclientPorperties);
                    if(content == null) continue;
                    for(Map<String, Object> obj : content){
                        String dbName = CvUtils.getMapValue("databaseName", obj);
                        if(StringUtils.isNotBlank(dbName) && dbName.equals(mysqlDataRestoreView.getSrcDbName())) {
                            srcSubclient = subclient;
                            break;
                        }
                    }
                }
                if(srcSubclient != null) break;
            }
        }

        if(srcSubclient == null){
            throw new ApplicationException("No subclient found for client: " + mysqlDataRestoreView.getSrcClientName());
        }

        Date backupDate = null;
        try {
            backupDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mysqlDataRestoreView.getBackupDate());
        } catch (ParseException e) {
            logger.error("Failed to parse specified date:{} for client: {} subclient: {}", mysqlDataRestoreView.getBackupDate(), mysqlDataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"), e);
            throw new ApplicationException("Failed to parse specified date:{} for client: {} subclient: {}" + mysqlDataRestoreView.getBackupDate() + mysqlDataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName"));
        }

        Map<String, Object> resp;
        resp = cvCommonService.getJobList(String.valueOf(srcSubclient.get("subclientId")), String.valueOf(backupDate.getTime()/1000L-1L), String.valueOf(backupDate.getTime()/1000L+86401L), token);
        List<Map<String, Object>> jobs = CvUtils.getMapValue("jobs", resp);
        String jobId="-1", jobStartTime="-1", jobEndTime="-1";
        if(jobs != null && !jobs.isEmpty()){
            Map<String, Object> jobSummary = jobs.get(0);
            jobId = String.valueOf((Object) CvUtils.getMapValue("jobId", jobSummary));
            jobStartTime = String.valueOf((Object) CvUtils.getMapValue("jobStartTime", jobSummary));
            jobEndTime = String.valueOf((Object) CvUtils.getMapValue("jobEndTime", jobSummary));
        }
        if(StringUtils.isBlank(jobId) || jobId.equals("-1")){
            logger.error("No backup job found for client: {} subclient: {} at time: {}", mysqlDataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"), mysqlDataRestoreView.getBackupDate());
            throw new ApplicationException("No backup job found for client: " + mysqlDataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName") + " at time: " + mysqlDataRestoreView.getBackupDate());
        }

        try{
            SimpleDateFormat  dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            resp = cvCommonService.restoreMysqlBackupData(
                    mysqlDataRestoreView.getSrcDbName(),
                    String.valueOf(srcSubclient.get("subclientName")),
                    mysqlDataRestoreView.getSrcInstanceName(),
                    mysqlDataRestoreView.getSrcClientName(),
                    mysqlDataRestoreView.getDestClientName(),
                    mysqlDataRestoreView.getDestInstanceName(),
                    dateFormatter.format(new Date(Long.parseLong(jobStartTime) * 1000L)),
                    dateFormatter.format(new Date(Long.parseLong(jobEndTime) * 1000L)),
                    token);
        } catch (Exception e) {
            logger.error("Failed to restore Mysql backup data for client: {} subclient: {}", mysqlDataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"), e);
            throw new ApplicationException("Failed to restore Mysql backup data for client: " + mysqlDataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName"));
        }

        List<String> jobIds = CvUtils.getMapValue("jobIds", resp);
        if(jobIds == null || jobIds.isEmpty()){
            logger.error("Failed to restore backup data for client: {} subclient: {}", mysqlDataRestoreView.getSrcClientName(), srcSubclient.get("subclientName"));
            throw new ApplicationException("Failed to restore backup data for client: " + mysqlDataRestoreView.getSrcClientName() + " subclient: " + srcSubclient.get("subclientName"));
        }

        result.put("restoreJobId", jobIds.get(0));
        return result;
    }
}
