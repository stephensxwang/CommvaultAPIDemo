package com.commvault.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.commvault.client.dao.CvApiMapper;
import com.commvault.client.entity.CvApi;
import com.commvault.client.service.CvCommonService;
import com.commvault.client.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CvCommonServiceImpl implements CvCommonService {

    private static Logger logger = LoggerFactory.getLogger(CvCommonServiceImpl.class);
    private static CvApiMapper cvInterfaceInfoMapper = new CvApiMapper();
    private int errorCode = -999;

    @Override
    public Map<String, Object> createOracleInstance(String clientName,
                                                    String instanceName,
                                                    String storagePolicyName,
                                                    String oracleHome,
                                                    String oracleUser,
                                                    String sqlConnectUser,
                                                    String sqlConnectPassword,
                                                    String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//clientName", clientName);
        properties.put("//instanceName", instanceName);
        properties.put("//oracleHome", oracleHome);
        properties.put("//useCatalogConnect", "false");
        properties.put("//dataArchiveGroup/storagePolicyName", storagePolicyName);
        properties.put("//commandLineStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//logBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//oracleUser/userName", oracleUser);
        properties.put("//sqlConnect/userName", sqlConnectUser);
        properties.put("//sqlConnect/password", sqlConnectPassword);
        Map<String, Object> result = processRequest("create_oracle_instance", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateOracleInstance(String clientName,
                                                    String instanceName,
                                                    String storagePolicyName,
                                                    String oracleHome,
                                                    String oracleUser,
                                                    String sqlConnectUser,
                                                    String sqlConnectPassword,
                                                    String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//entity/appName", CvAgentTypeEnum.ORACLE.getValue());
        properties.put("//entity/clientName", clientName);
        properties.put("//entity/instanceName", instanceName);
        properties.put("//instance/appName", CvAgentTypeEnum.ORACLE.getValue());
        properties.put("//instance/clientName", clientName);
        properties.put("//instance/instanceName", instanceName);
        properties.put("//oracleHome", oracleHome);
        properties.put("//useCatalogConnect", "false");
        properties.put("//commandLineStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//logBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//oracleUser/userName", oracleUser);
        properties.put("//sqlConnect/userName", sqlConnectUser);
        properties.put("//sqlConnect/password", sqlConnectPassword);
        Map<String, Object> result = processRequest("update_oracle_instance", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateOracleDatabaseSubclient(String appName, String clientName, String instanceName, String subclientName, String storagePolicyName, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//appName", appName);
        properties.put("//clientName", clientName);
        properties.put("//instanceName", instanceName);
        properties.put("//subclientName", subclientName);
        properties.put("//selectiveOnlineFull", "true");
        properties.put("//data", "true");
        properties.put("//dataBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//softwareCompression", "USE_STORAGE_POLICY_SETTINGS");
        properties.put("//backupSPFile", "true");
        properties.put("//backupControlFile", "true");
        properties.put("//dataFilesPerBFS", "1");
        properties.put("//skipInaccessible", "true");
        properties.put("//archiveDelete", "true");
        Map<String, Object> result = processRequest("update_oracle_subclient", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateOracleArchiveLogSubclient(String appName, String clientName, String instanceName, String subclientName, String storagePolicyName, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//appName", appName);
        properties.put("//clientName", clientName);
        properties.put("//instanceName", instanceName);
        properties.put("//subclientName", subclientName);
        properties.put("//selectiveOnlineFull", "false");
        properties.put("//data", "false");
        properties.put("//dataBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//softwareCompression", "USE_STORAGE_POLICY_SETTINGS");
        properties.put("//backupSPFile", "false");
        properties.put("//backupControlFile", "false");
        properties.put("//dataFilesPerBFS", "1");
        properties.put("//skipInaccessible", "false");
        properties.put("//archiveDelete", "false");
        Map<String, Object> result = processRequest("update_oracle_subclient", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> renameDatabaseSubclient(String appName, String clientName, String instanceName, String subclientName, String newName, String token) {
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("appName", appName);
        apiMap.put("clientName", clientName);
        apiMap.put("instanceName", instanceName);
        apiMap.put("subclientName", subclientName);
        apiMap.put("newName", newName);

        Map<String, Object> result = processRequest("rename_database_subclient", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, null, false);
        return result;
    }

    @Override
    public Map<String, Object> createOracleRACClient(String clientName, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//entity/clientName", clientName);
        Map<String, Object> result = processRequest("create_oracle_rac_client", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> createOracleRACInstanceAndAddPrimaryClient(String clientName,
                                                                          String primaryClientName,
                                                                          String instanceName,
                                                                          String databaseName,
                                                                          String userAccount,
                                                                          String oracleHome,
                                                                          String connectUser,
                                                                          String connectPassword,
                                                                          String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//instance/clientName", clientName);
        properties.put("//instance/instanceName", databaseName);
        properties.put("//instancePhysicalClient/clientName", primaryClientName);
        properties.put("//instanceOracleSID", instanceName);
        properties.put("//userAccount/userName", userAccount);
        properties.put("//oracleHome", oracleHome);
        properties.put("//racDBInstance/connectString/userName", connectUser);
        properties.put("//racDBInstance/connectString/password", connectPassword);
        properties.put("//racDBInstance/connectString/serviceName", instanceName);
        Map<String, Object> result = processRequest("create_oracle_rac_instance", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateOracleRACInstanceAddSecondaryClient(String clientName,
                                                                         String secondaryClientName,
                                                                         String instanceName,
                                                                         String databaseName,
                                                                         String userAccount,
                                                                         String oracleHome,
                                                                         String connectUser,
                                                                         String connectPassword,
                                                                         String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//entity/clientName", clientName);
        properties.put("//entity/instanceName", databaseName);
        properties.put("//instancePhysicalClient/clientName", secondaryClientName);
        properties.put("//instanceOracleSID", instanceName);
        properties.put("//userAccount/userName", userAccount);
        properties.put("//oracleHome", oracleHome);
        properties.put("//racDBInstance/connectString/userName", connectUser);
        properties.put("//racDBInstance/connectString/password", connectPassword);
        properties.put("//racDBInstance/connectString/serviceName", instanceName);
        Map<String, Object> result = processRequest("update_oracle_rac_instance", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getSubclientListByClientName(String clientName, String token) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("clientName", clientName);

        Map<String, Object> resmap = processRequest("get_subclient_by_client_name", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, null, true);
        JSONArray subclientList = (JSONArray) resmap.get("subClientProperties");
        if(subclientList == null || subclientList.isEmpty()) return result;
        subclientList.forEach(jsonObj -> {
            Map<String, Object> entryMap = JSONObject.toJavaObject((JSONObject)jsonObj, Map.class);
            if(entryMap.get("subClientEntity") != null) {
                Map<String, Object> subclientMap = JSONObject.toJavaObject((JSONObject)entryMap.get("subClientEntity"), Map.class);
                result.add(subclientMap);
            }
        });

        return result;
    }

    @Override
    public Map<String, Object> updateSchedulePolicyEntityAssoc(String schedulePolicyName, String appName, String clientName, String subClientName, String backupsetName, String instanceName, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//associations[@appName]", appName);
        properties.put("//associations[@clientName]", clientName);
        properties.put("//associations[@subclientName]", subClientName);
        properties.put("//associations[@backupsetName]", backupsetName);
        properties.put("//associations[@instanceName]", instanceName);

        properties.put("//task[@taskName]", schedulePolicyName);
        Map<String, Object> result = processRequest("update_schedule_policy_entity_assoc", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateOracleClientAdditionalSettings(String clientName, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//clientName", clientName);
        Map<String, Object> result = processRequest("update_oracle_client_additional_settings", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> createOracleDGInstance(String clientName,
                                                      String primaryClientName,
                                                      String instanceName,
                                                      String userAccount,
                                                      String oracleHome,
                                                      String connectUser,
                                                      String connectPassword,
                                                      String connectServiceName,
                                                      String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//instance/clientName", clientName);
        properties.put("//instance/instanceName", instanceName);
        properties.put("//instancePhysicalClient/clientName", primaryClientName);
        properties.put("//instanceOracleSID", instanceName);
        properties.put("//userAccount/userName", userAccount);
        properties.put("//oracleHome", oracleHome);
        properties.put("//racDBInstance/connectString/userName", connectUser);
        properties.put("//racDBInstance/connectString/password", connectPassword);
        properties.put("//racDBInstance/connectString/serviceName", connectServiceName);
        Map<String, Object> result = processRequest("create_oracle_rac_instance", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateOracleDGInstance(String clientName,
                                                      String secondaryClientName,
                                                      String instanceName,
                                                      String userAccount,
                                                      String oracleHome,
                                                      String connectUser,
                                                      String connectPassword,
                                                      String connectServiceName,
                                                      String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//entity/clientName", clientName);
        properties.put("//entity/instanceName", instanceName);
        properties.put("//instancePhysicalClient/clientName", secondaryClientName);
        properties.put("//instanceOracleSID", instanceName);
        properties.put("//userAccount/userName", userAccount);
        properties.put("//oracleHome", oracleHome);
        properties.put("//racDBInstance/connectString/userName", connectUser);
        properties.put("//racDBInstance/connectString/password", connectPassword);
        properties.put("//racDBInstance/connectString/serviceName", connectServiceName);
        Map<String, Object> result = processRequest("update_oracle_rac_instance", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> createClusterClient(String clientType, String clientName, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//clientType", clientType);
        properties.put("//clientName", clientName);
        properties.put("//hostName", clientName);
        Map<String, Object> result = processRequest("create_cluster_client", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateClientActivityControl(String clientName, String activityType, boolean enableActivity, String token) {
        Map<String, Object> result = null;
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("clientName", clientName);

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//entity/clientName", clientName);
        properties.put("//clientEntity/clientName", clientName);
        properties.put("//activityControlOptions/activityType", activityType);
        properties.put("//activityControlOptions/enableActivityType", String.valueOf(enableActivity));
        properties.put("//activityControlOptions/enableAfterADelay", "false");

        result = processRequest("update_client_by_name", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateClientDescription(String clientName, String clientDescription, String token) {
        Map<String, Object> result = null;
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("clientName", clientName);

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//entity/clientName", clientName);
        properties.put("//clientEntity/clientName", clientName);
        Function<Document, List<Node>> desFunction = document -> {
            List<Node> desNodes = new ArrayList<Node>();
            Node cdata = document.createCDATASection(clientDescription);
            desNodes.add(cdata);

            return desNodes;
        };
        properties.put("//client/clientDescription", desFunction);

        result = processRequest("update_client_by_name", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateOracleRACInstanceStoragePolicy(String clientName, String instanceName, String storagePolicyName, String token) {
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("storagePolicyName", storagePolicyName);

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//entity/clientName", clientName);
        properties.put("//entity/instanceName", instanceName);
        properties.put("//racDBOperationType", "OVERWRITE");
        properties.put("//dataBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//logBackupStoragePolicy/storagePolicyName", storagePolicyName);
        Map<String, Object> result = processRequest("update_oracle_rac_instance_storage_policy", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> addMemberToClusterClient(String clientName, List<String> members, String token) {
        Map<String, Object> result = null;

        try {
            Map<String, String> apiMap = new HashMap<String, String>();
            apiMap.put("clientName", clientName);

            HashMap<String, Object> properties = new HashMap<String, Object>();
            properties.put("//entity/clientName", clientName);
            properties.put("//clientEntity/clientName", clientName);
            properties.put("//clusterClientProperties/configureClusterClient", "false");
            properties.put("//clusterClientProperties/showAllAgents", "false");

            Function<Document, List<Node>> memberFunction = document -> {
                List<Node> memberNodes = new ArrayList<Node>();
                for(String member:members) {
                    Element clusterGroupAssociation = document.createElement("clusterGroupAssociation");
                    Node clientNameNode = document.createElement("clientName");
                    clientNameNode.setTextContent(member);
                    clusterGroupAssociation.appendChild(clientNameNode);
                    Node typeNode = document.createElement("type");
                    typeNode.setTextContent("GALAXY");
                    clusterGroupAssociation.appendChild(typeNode);

                    memberNodes.add((Node) clusterGroupAssociation);
                }
                return memberNodes;
            };

            properties.put("//clusterClientProperties", memberFunction);

            result = processRequest("update_client_by_name", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, properties, true);
        } catch (Exception e) {
            logger.error("Error occur while adding member to cluster client", e);
            return CvUtils.genError(errorCode, "Failed to add member to cluster client " + clientName);
        }

        return result;
    }

    @Override
    public Map<String, Object> getClientPropertiesByName(String clientName, String token) {
        Map<String, Object> result = null;

        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("clientName", clientName);

        result = processRequest("get_client_properties_by_name", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, null, true);

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getInstanceListByClientName(String clientName, String token) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("clientName", clientName);

        Map<String, Object> resmap = processRequest("get_instance_by_client_name", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, null, true);
        JSONArray instanceList = (JSONArray) resmap.get("instanceProperties");
        if(instanceList == null || instanceList.isEmpty()) return result;
        instanceList.forEach(jsonObj -> {
            Map<String, Object> entryMap = JSONObject.toJavaObject((JSONObject)jsonObj, Map.class);
            if(entryMap.get("instance") != null) {
                Map<String, Object> instanceMap = JSONObject.toJavaObject((JSONObject)entryMap.get("instance"), Map.class);
                result.add(instanceMap);
            }
        });

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getSchedulePolicy(String token) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        Map<String, Object> resmap = processRequest("get_schedule_policy", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, null, true);
        JSONArray taskList = (JSONArray) resmap.get("taskDetail");
        if(taskList == null || taskList.isEmpty()) return result;
        taskList.forEach(jsonObj -> {
            Map<String, Object> entryMap = JSONObject.toJavaObject((JSONObject)jsonObj, Map.class);
            if(entryMap.get("task") != null) {
                Map<String, Object> taskMap = JSONObject.toJavaObject((JSONObject)entryMap.get("task"), Map.class);
                result.add(taskMap);
            }
        });

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getStoragePolicy(String token) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        Map<String, Object> resmap = processRequest("get_storage_policy", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, null, true);
        JSONArray taskList = (JSONArray) resmap.get("policies");
        if(taskList == null || taskList.isEmpty()) return result;
        taskList.forEach(jsonObj -> {
            Map<String, Object> entryMap = JSONObject.toJavaObject((JSONObject)jsonObj, Map.class);
            result.add(entryMap);
        });

        return result;
    }

    @Override
    public Map<String, Object> getSubclientProperties(String subclientId, String token) {
        Map<String, Object> result = null;

        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("subclientId", subclientId);

        result = processRequest("get_subclient_properties", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, null, true);

        return result;
    }

    @Override
    public Map<String, Object> createFileSystemSubclient(String clientName, String backupsetName, String subClientName, String storagePolicyName, String contentPath, boolean useVss, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//subClientEntity/appName", CvAgentTypeEnum.FILESYSTEM.getValue());
        properties.put("//subClientEntity/clientName", clientName);
        properties.put("//subClientEntity/backupsetName", backupsetName);
        properties.put("//subClientEntity/subclientName", subClientName);
        properties.put("//contentOperationType", "ADD");
        properties.put("//softwareCompression", "USE_STORAGE_POLICY_SETTINGS");
        properties.put("//storagePolicyName", storagePolicyName);
        properties.put("//content/path", contentPath);
        if(useVss) {
            properties.put("//fsSubClientProp/useVSS", "true");
            properties.put("//fsSubClientProp/backupSystemState", "false");
        }
        Map<String, Object> result = processRequest("create_subclient", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> overwriteContentForFileSystemSubclientById(String subclientId, String clientName, String backupsetName, String subClientName, String newSubClientName, String storagePolicyName, String contentPath, boolean useVss, String token){
        Map<String, Object> result = null;

        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("subclientId", subclientId);

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//entity/appName", CvAgentTypeEnum.FILESYSTEM.getValue());
        properties.put("//entity/clientName", clientName);
        properties.put("//entity/backupsetName", backupsetName);
        properties.put("//entity/subclientName", subClientName);
        properties.put("//newName", newSubClientName);
        properties.put("//contentOperationType", "OVERWRITE");
        properties.put("//softwareCompression", "USE_STORAGE_POLICY_SETTINGS");
        properties.put("//storagePolicyName", storagePolicyName);
        properties.put("//content/path", contentPath);
        if(useVss) {
            properties.put("//fsSubClientProp/useVSS", "true");
            properties.put("//fsSubClientProp/backupSystemState", "false");
        }

        result = processRequest("update_subclient_by_id", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> addContentForFileSystemSubclientById(String subclientId, String clientName, String backupsetName, String subClientName, String storagePolicyName, String contentPath, String token) {
        Map<String, Object> result = null;

        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("subclientId", subclientId);

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//entity/appName", CvAgentTypeEnum.FILESYSTEM.getValue());
        properties.put("//entity/clientName", clientName);
        properties.put("//entity/backupsetName", backupsetName);
        properties.put("//entity/subclientName", subClientName);
        properties.put("//contentOperationType", "ADD");
        properties.put("//softwareCompression", "USE_STORAGE_POLICY_SETTINGS");
        properties.put("//storagePolicyName", storagePolicyName);
        properties.put("//content/path", contentPath);

        result = processRequest("update_subclient_by_id", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, properties, true);

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getSchedulesBySubclientId(String subclientId, String token) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("subclientId", subclientId);

        Map<String, Object> resmap = processRequest("get_schedules_by_subclient_id", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, null, true);
        JSONArray taskList = (JSONArray) resmap.get("taskDetail");
        if(taskList == null || taskList.isEmpty()) return result;
        taskList.forEach(jsonObj -> {
            Map<String, Object> entryMap = JSONObject.toJavaObject((JSONObject)jsonObj, Map.class);
            if(entryMap.get("task") != null) {
                Map<String, Object> taskMap = JSONObject.toJavaObject((JSONObject)entryMap.get("task"), Map.class);
                result.add(taskMap);
            }
        });

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getClientGroup(String token) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        Map<String, Object> resmap = processRequest("get_client_group", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, null, true);
        JSONArray groupList = (JSONArray) resmap.get("groups");
        if(groupList == null || groupList.isEmpty()) return result;
        groupList.forEach(jsonObj -> {
            Map<String, Object> entryMap = JSONObject.toJavaObject((JSONObject)jsonObj, Map.class);
            result.add(entryMap);
        });

        return result;
    }

    @Override
    public Map<String, Object> createClientGroup(String groupName, String clientName, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//clientGroupName", groupName);
        properties.put("//associatedClients/clientName", clientName);
        Map<String, Object> result = processRequest("create_client_group", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> createClientGroup(String groupName, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//clientGroupName", groupName);
        Map<String, Object> result = processRequest("create_client_group", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> deleteClientGroup(String clientGroupId, String token) {
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("clientGroupId", clientGroupId);
        Map<String, Object> result = processRequest("delete_client_group", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, null, true);

        return result;
    }

    @Override
    public Map<String, Object> updateClientGroup(String clientGroupId, String groupName, String token) {
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("clientGroupId", clientGroupId);

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//newName", groupName);
        Map<String, Object> result = processRequest("update_client_group", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> addClientToClientGroup(String clientGroupId, String groupName, String clientName, String token) {
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("clientGroupId", clientGroupId);

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//clientGroupName", groupName);
        properties.put("//associatedClients/clientName", clientName);
        properties.put("//associatedClientsOperationType", "2");
        Map<String, Object> result = processRequest("update_client_group", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> login(String domain, String username, String password, String commserver) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//DM2ContentIndexing_CheckCredentialReq[@domain]", domain);
        properties.put("//DM2ContentIndexing_CheckCredentialReq[@username]", username);
        properties.put("//DM2ContentIndexing_CheckCredentialReq[@password]", password);
        properties.put("//DM2ContentIndexing_CheckCredentialReq[@commserver]", commserver);
        Map<String, Object> result = processRequest("login", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), "", null, properties, true);

        return result;
    }

    @Override
    public boolean logout(String token) {
        Map<String, Object> resp = processRequest("logout", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, null, true);

        if(resp == null) return false;

        return true;
    }

    @Override
    public Map<String, Object> createSQLServerInstance(String clientName, String instanceName, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//clientName", clientName);
        properties.put("//instanceName", instanceName);
        Map<String, Object> result = processRequest("create_sql_server_instance", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> createSQLServerSubclient(String clientName,
            String instanceName,
            String subClientName,
            String storagePolicyName,
            String databaseName,
            String token){
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//clientName", clientName);
        properties.put("//instanceName", instanceName);
        properties.put("//subclientName", subClientName);
        properties.put("//dataBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//logBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//contentOperationType", "ADD");
        properties.put("//sqlSubclientType", "DATABASE");
        properties.put("//softwareCompression", "USE_STORAGE_POLICY_SETTINGS");
        properties.put("//databaseName", databaseName);
        Map<String, Object> result = processRequest("create_sql_server_subclient", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateSQLServerSubclient(String clientName,
            String instanceName,
            String subClientName,
            String storagePolicyName,
            String databaseName,
            String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//clientName", clientName);
        properties.put("//instanceName", instanceName);
        properties.put("//subclientName", subClientName);
        properties.put("//dataBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//logBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//contentOperationType", "ADD");
        properties.put("//softwareCompression", "USE_STORAGE_POLICY_SETTINGS");
        properties.put("//databaseName", databaseName);
        Map<String, Object> result = processRequest("update_sql_server_subclient", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> getSQLServerAG(String clientName, String instanceName, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//sqlClient[@clientName]", clientName);
        properties.put("//instanceEntity[@clientName]", clientName);
        properties.put("//instanceEntity[@instanceName]", instanceName);
        Map<String, Object> result = processRequest("get_sql_server_ag", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> getSQLServerAGReplica(String clientName, String instanceName, String availabilityGroup, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//App_GetAvailabilityGroupReplicasReq[@availabilityGroup]", availabilityGroup);
        properties.put("//sqlClient[@clientName]", clientName);
        properties.put("//instanceEntity[@clientName]", clientName);
        properties.put("//instanceEntity[@instanceName]", instanceName);
        Map<String, Object> result = processRequest("get_sql_server_ag_replica", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> createSQLServerAGClient(String primaryClientName, String instanceName, String clientName, String token) {
        Map<String, Object> result = getSQLServerAG(primaryClientName, instanceName, token);
        JSONArray availabilityGroupList = (JSONArray) result.get("SQLAvailabilityGroupList");
        String errorMessage = "Failed to create SQL Server AG client: ";
        if(availabilityGroupList == null || availabilityGroupList.isEmpty()) return CvUtils.genError(errorCode, errorMessage + "no availability group found");

        JSONObject groupMap = availabilityGroupList.getJSONObject(0);
        try {
            String availabilityGroup = groupMap.getString("name");
            result = getSQLServerAGReplica(primaryClientName, instanceName, availabilityGroup, token);

            Function<Document, List<Node>> avaGroupFunction = document -> {
                List<Node> avaGroupList = new ArrayList<Node>();
                Element avaGroup = document.createElement("availabilityGroup");
                avaGroup.setAttribute("backupPreference", groupMap.getString("backupPreference"));
                avaGroup.setAttribute("name", availabilityGroup);
                avaGroup.setAttribute("primaryReplicaServerName", groupMap.getString("primaryReplicaServerName"));
                avaGroup.setAttribute("uniqueId", groupMap.getInteger("uniqueId").toString());
                Element listenerList = document.createElement("SQLAvailabilityGroupListenerList");
                listenerList.setAttribute("availabilityGroupListenerName", availabilityGroupList.getJSONObject(0).getJSONArray("SQLAvailabilityGroupListenerList").getJSONObject(0).getString("availabilityGroupListenerName"));
                avaGroup.appendChild(listenerList);

                avaGroupList.add((Node) avaGroup);
                return avaGroupList;
            };

            JSONArray replicaList = (JSONArray) result.get("SQLAvailabilityReplicasList");
            Function<Document, List<Node>> replicaFunction = document -> {
                List<Node> replicaNodes = new ArrayList<Node>();
                for(int i=0; i<replicaList.size(); i++) {
                    JSONObject replicaJson = replicaList.getJSONObject(i);
                    Element replica = document.createElement("SQLAvailabilityReplicasList");
                    replica.setAttribute("availabilityMode", replicaJson.getInteger("availabilityMode").toString());
                    replica.setAttribute("backupPriority", replicaJson.getInteger("backupPriority").toString());
                    replica.setAttribute("connectionState", replicaJson.getInteger("connectionState").toString());
                    replica.setAttribute("failoverMode", replicaJson.getInteger("failoverMode").toString());
                    replica.setAttribute("name", replicaJson.getString("name"));
                    replica.setAttribute("role", replicaJson.getInteger("role").toString());
                    replicaNodes.add((Node) replica);
                }
                return replicaNodes;
            };

            HashMap<String, Object> properties = new HashMap<String, Object>();
            properties.put("//SQLServerInstance[@clientName]", primaryClientName);
            properties.put("//SQLServerInstance[@instanceName]", instanceName);
            properties.put("//mssqlagClientProperties", avaGroupFunction);
            properties.put("//SQLAvailabilityReplicasList", replicaFunction);
            properties.put("//entity[@clientName]", clientName);
            result = processRequest("create_sql_server_ag_client", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);
        } catch (Exception e) {
            logger.error("Error occur while generating SQL Server AG client creation request", e);
            return CvUtils.genError(errorCode, errorMessage + "no replica info found");
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getBackupsetListByClientName(String clientName, String token) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("clientName", clientName);

        Map<String, Object> resmap = processRequest("get_backupset_by_client_name", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, null, true);
        JSONArray backupsetList = (JSONArray) resmap.get("backupsetProperties");
        if(backupsetList == null || backupsetList.isEmpty()) return result;
        backupsetList.forEach(jsonObj -> {
            Map<String, Object> entryMap = JSONObject.toJavaObject((JSONObject)jsonObj, Map.class);
            if(entryMap.get("backupSetEntity") != null) {
                Map<String, Object> backupsetMap = JSONObject.toJavaObject((JSONObject)entryMap.get("backupSetEntity"), Map.class);
                result.add(backupsetMap);
            }
        });

        return result;
    }

    @Override
    public Map<String, Object> createDb2Instance(
            String appName,
            String clientName,
            String instanceName,
            String storagePolicyName,
            String homeDirectory,
            String token) {
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("appName", appName);
        apiMap.put("password", "");

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//clientName", clientName);
        properties.put("//instanceName", instanceName);
        properties.put("//commandLineStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//dataBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//logBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//userName", instanceName);
        properties.put("//homeDirectory", homeDirectory);
        Map<String, Object> result = processRequest("create_db2_instance", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateDb2Instance(
            String appName,
            String clientName,
            String instanceName,
            String storagePolicyName,
            String homeDirectory,
            String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//appName", appName);
        properties.put("//clientName", clientName);
        properties.put("//instanceName", instanceName);
        properties.put("//commandLineStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//dataBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//logBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//userName", instanceName);
        properties.put("//homeDirectory", homeDirectory);
        Map<String, Object> result = processRequest("update_db2_instance", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> createDb2Backupset(
            String appName,
            String clientName,
            String instanceName,
            String databaseName,
            String storagePolicyName,
            String token) {
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("storagePolicyName", storagePolicyName);

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//appName", appName);
        properties.put("//clientName", clientName);
        properties.put("//instanceName", instanceName);
        properties.put("//backupsetName", databaseName);
        Map<String, Object> result = processRequest("create_db2_backupset", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> createDb2Subclient(
            String appName,
            String clientName,
            String subclientName,
            String instanceName,
            String databaseName,
            String storagePolicyName,
            String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//appName", appName);
        properties.put("//subClientEntity/clientName", clientName);
        properties.put("//subClientEntity/subclientName", subclientName);
        properties.put("//subClientEntity/instanceName", instanceName);
        properties.put("//subClientEntity/backupsetName", databaseName);
        properties.put("//storagePolicyName", storagePolicyName);
        properties.put("//db2BackupData", "true");
        properties.put("//db2BackupType", "ENTIRE_DATABASE");
        properties.put("//db2BackupMode", "ONLINE_BACKUP");
        properties.put("//db2BackupLogFiles", "false");
        Map<String, Object> result = processRequest("create_db2_subclient", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateDb2Subclient(
            String appName,
            String clientName,
            String subclientName,
            String instanceName,
            String databaseName,
            String storagePolicyName,
            String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//appName", appName);
        properties.put("//entity/clientName", clientName);
        properties.put("//entity/subclientName", subclientName);
        properties.put("//entity/instanceName", instanceName);
        properties.put("//entity/backupsetName", databaseName);
        properties.put("//storagePolicyName", storagePolicyName);
        properties.put("//db2BackupData", "true");
        properties.put("//db2BackupType", "ENTIRE_DATABASE");
        properties.put("//db2BackupMode", "ONLINE_BACKUP");
        properties.put("//db2BackupLogFiles", "false");
        Map<String, Object> result = processRequest("update_db2_subclient", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> renameDb2Subclient(String appName, String clientName, String instanceName, String backupsetName, String subclientName, String newName, String token) {
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("appName", appName);
        apiMap.put("clientName", clientName);
        apiMap.put("instanceName", instanceName);
        apiMap.put("subclientName", subclientName);
        apiMap.put("backupsetName", backupsetName);
        apiMap.put("newName", newName);

        Map<String, Object> result = processRequest("rename_db2_subclient", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, null, false);
        return result;
    }

    @Override
    public Map<String, Object> createMysqlInstance(
            String clientName,
            String instanceName,
            String storagePolicyName,
            String osName,
            String binaryDirectory,
            String logDataDirectory,
            String configFile,
            String port,
            String saUser,
            String saPassword,
            String sysUser,
            String ntPassword,
            String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//clientName", clientName);
        properties.put("//instanceName", instanceName);
        properties.put("//BinaryDirectory", binaryDirectory);
        properties.put("//LogDataDirectory", logDataDirectory);
        properties.put("//ConfigFile", configFile);
        properties.put("//port", port);
        properties.put("//SAUser/userName", saUser);
        properties.put("//SAUser/password", saPassword);
        if(CvConstants.OSNAME_WINDOWS.equalsIgnoreCase(osName)) {
            properties.put("//NTUser/userName", sysUser);
            properties.put("//NTUser/password", ntPassword);
        } else {
            properties.put("//unixUser/userName", sysUser);
        }

        properties.put("//commandLineStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//logBackupStoragePolicy/storagePolicyName", storagePolicyName);
        Map<String, Object> result = processRequest("create_mysql_instance", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateMysqlInstance(
            String clientName,
            String instanceName,
            String storagePolicyName,
            String osName,
            String binaryDirectory,
            String logDataDirectory,
            String configFile,
            String port,
            String saUser,
            String saPassword,
            String sysUser,
            String ntPassword,
            String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//EnableAutoDiscovery", "true");
        properties.put("//clientName", clientName);
        properties.put("//instanceName", instanceName);
        properties.put("//BinaryDirectory", binaryDirectory);
        properties.put("//LogDataDirectory", logDataDirectory);
        properties.put("//ConfigFile", configFile);
        properties.put("//port", port);
        properties.put("//SAUser/userName", saUser);
        properties.put("//SAUser/password", saPassword);
        if(CvConstants.OSNAME_WINDOWS.equalsIgnoreCase(osName)) {
            properties.put("//NTUser/userName", sysUser);
            properties.put("//NTUser/password", ntPassword);
        } else {
            properties.put("//unixUser/userName", sysUser);
        }

        properties.put("//commandLineStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//logBackupStoragePolicy/storagePolicyName", storagePolicyName);
        Map<String, Object> result = processRequest("update_mysql_instance", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> updateMysqlSubclient(
            String clientName,
            String subclientName,
            String instanceName,
            String databaseName,
            String storagePolicyName,
            String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//entity/clientName", clientName);
        properties.put("//entity/instanceName", instanceName);
        properties.put("//entity/subclientName", subclientName);
        properties.put("//dataBackupStoragePolicy/storagePolicyName", storagePolicyName);
        properties.put("//contentOperationType", "ADD");
        properties.put("//mySQLContent/databaseName", databaseName);
        Map<String, Object> result = processRequest("update_mysql_subclient", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public List<Map<String, Object>> getClient(String token) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        Map<String, Object> resmap = processRequest("get_client", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, null, true);
        JSONArray clientList = (JSONArray) resmap.get("clientProperties");
        if(clientList == null || clientList.isEmpty()) return result;
        clientList.forEach(jsonObj -> {
            Map<String, Object> entryMap = JSONObject.toJavaObject((JSONObject)jsonObj, Map.class);
            result.add(entryMap);
        });

        return result;
    }

    @Override
    public Map<String, Object> browseBackupData(String clientName, String applicationId, String clientId, String subclientId, String backupsetId, String instanceId, String fromTime, String toTime, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//entity[@clientName]", clientName);
        properties.put("//entity[@applicationId]", applicationId);
        properties.put("//entity[@clientId]", clientId);
        properties.put("//entity[@subclientId]", subclientId);
        properties.put("//entity[@backupsetId]", backupsetId);
        properties.put("//entity[@instanceId]", instanceId);
        properties.put("//session[@sessionId]", String.valueOf(Calendar.getInstance().getTimeInMillis()));
        properties.put("//timeRange[@toTime]", toTime);
        properties.put("//timeRange[@fromTime]", fromTime);

        Map<String, Object> result = processRequest("browse_backup_data", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, false);
        return result;
    }

    @Override
    public Map<String, Object> restoreBackupData(String srcClientId, String appTypeId, String instanceId, String backupSetId, String subclientId, String fromTime, String toTime, String targetClientId, String targetClientName, String inPlace, String destPath, List<String> filePaths, String token) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//srcContent[@clientId]", srcClientId);
        properties.put("//srcContent[@appTypeId]", appTypeId);
        properties.put("//srcContent[@instanceId]", instanceId);
        properties.put("//srcContent[@backupSetId]", backupSetId);
        properties.put("//srcContent[@subclientId]", subclientId);
        properties.put("//srcContent[@fromTime]", fromTime);
        properties.put("//srcContent[@toTime]", toTime);
        properties.put("//destination[@clientId]", targetClientId);
        properties.put("//destination[@clientName]", targetClientName);
        properties.put("//destination[@inPlace]", inPlace);
        properties.put("//destPath[@val]", destPath);

        Function<Document, List<Node>> filePathsFunction = document -> {
            List<Node> filePathNodes = new ArrayList<Node>();
            for(String path:filePaths) {
                Element filePathsNode = document.createElement("filePaths");
                filePathsNode.setAttribute("val", path);

                filePathNodes.add((Node) filePathsNode);
            }
            return filePathNodes;
        };
        properties.put("//header", filePathsFunction);

        Map<String, Object> result = processRequest("restore_backup_data", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, false);
        return result;
    }

    @Override
    public Map<String, Object> restoreNdmpBackupData(String srcClientName, String srcSubclientName, String srcBackupsetName, String srcInstanceName, String srcPath, String fromTime, String toTime, String targetClientName, String inPlace, String destPath, String token){
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//associations/subclientName", srcSubclientName);
        properties.put("//associations/backupsetName", srcBackupsetName);
        properties.put("//associations/instanceName", srcInstanceName);
        properties.put("//associations/clientName", srcClientName);
        properties.put("//backupset/backupsetName", srcBackupsetName);
        properties.put("//backupset/clientName", srcClientName);
        properties.put("//fromTimeValue", fromTime);
        properties.put("//toTimeValue", toTime);
        properties.put("//destPath", destPath);
        properties.put("//destClient/clientName", targetClientName);
        properties.put("//inPlace", inPlace);
        properties.put("//sourceItem", srcPath);

        Map<String, Object> result = processRequest("restore_ndmp_backup_data", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, false);

        return result;
    }

    @Override
    public Map<String, Object> listMediaForAJob(String jobId, String token) {
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("jobId", jobId);

        Map<String, Object> result = processRequest("list_media_for_a_job", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, null, false);
        return result;
    }

    @Override
    public Map<String, Object> getJobSummary(String jobId, String token) {
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("jobId", jobId);

        Map<String, Object> result = processRequest("get_job_summary", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, null, false);
        return result;
    }

    @Override
    public Map<String, Object> getDb2BackupJobInfo(String clientName, String instanceName, String backupsetName, String token){
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("clientName", clientName);
        apiMap.put("instanceName", instanceName);
        apiMap.put("backupsetName", backupsetName);

        Map<String, Object> result = processRequest("get_db2_backup_job_info", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, null, false);
        return result;
    }

    @Override
    public Map<String, Object> restoreDb2BackupData(String srcSubclientName, String srcBackupsetName, String srcInstanceName, String srcClientName, String destClientName, String destInstanceName, String targetDbName, String targetPath, String backupTimeStamps, String token){
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//associations/subclientName", srcSubclientName);
        properties.put("//associations/backupsetName", srcBackupsetName);
        properties.put("//associations/instanceName", srcInstanceName);
        properties.put("//associations/clientName", srcClientName);
        properties.put("//backupset/backupsetName", srcBackupsetName);
        properties.put("//backupset/clientName", srcClientName);
        properties.put("//destClient/clientName", destClientName);
        properties.put("//destinationInstance/instanceName", destInstanceName);
        properties.put("//destinationInstance/clientName", destClientName);
        properties.put("//db2Option/targetDb", targetDbName);
        properties.put("//db2Option/targetPath", targetPath);
        properties.put("//db2Option/timeStamps", backupTimeStamps);
        Map<String, Object> result = processRequest("restore_db2_backup_data", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> restoreOracleBackupData(String srcClientName, String srcSubclientName, String srcBackupsetName, String srcInstanceName, String fromTime, String toTime, String destClientName, String destInstanceName, String targetPath, String controlFileName, String scn, String token){
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//associations/subclientName", srcSubclientName);
        properties.put("//associations/backupsetName", srcBackupsetName);
        properties.put("//associations/instanceName", srcInstanceName);
        properties.put("//associations/clientName", srcClientName);
        properties.put("//associations/appName", "Oracle");
        properties.put("//backupset/backupsetName", srcBackupsetName);
        properties.put("//backupset/clientName", srcClientName);
        properties.put("//fromTimeValue", fromTime);
        properties.put("//toTimeValue", toTime);
        properties.put("//destClient/clientName", destClientName);
        properties.put("//destinationInstance/instanceName", destInstanceName);
        properties.put("//destinationInstance/appName", "Oracle");
        properties.put("//destinationInstance/clientName", destClientName);
        properties.put("//controlFileTime/timeValue", currentTime);
        properties.put("//SPFileTime/timeValue", currentTime);
        properties.put("//restoreTime/timeValue", fromTime);
        properties.put("//recoverTime/timeValue", fromTime);
        properties.put("//renamePathForAllTablespaces", targetPath);
        properties.put("//ctrlBackupPiece", controlFileName);
        properties.put("//redirectTempFilesValue", targetPath);
        properties.put("//onlineLogDest", targetPath);
        if(StringUtils.isNotBlank(scn)){
            properties.put("//recoverFrom", "2");
            properties.put("//recoverSCN", scn);
        }

        Map<String, Object> result = processRequest("restore_oracle_backup_data", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, false);
        return result;
    }

    @Override
    public Map<String, Object> restoreMssqlBackupData(String srcDbName, String srcSubclientName, String srcInstanceName, String srcClientName, String destClientName, String destInstanceName, String targetDbName, String targetPath, String backupTimeStamps, List<String> devices, String token){
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//associations/subclientName", srcSubclientName);
        properties.put("//associations/instanceName", srcInstanceName);
        properties.put("//associations/clientName", srcClientName);
        properties.put("//backupset/clientName", srcClientName);
        properties.put("//destClient/clientName", destClientName);
        properties.put("//destinationInstance/instanceName", destInstanceName);
        properties.put("//destinationInstance/clientName", destClientName);
        properties.put("//sqlServerRstOption/restoreSource", srcDbName);
        properties.put("//sqlServerRstOption/database", srcDbName);
        properties.put("//toTimeValue", backupTimeStamps);
        Function<Document, List<Node>> devicesFunction = document -> {
            List<Node> deviceNodes = new ArrayList<Node>();
            for(String device:devices) {
                Element deviceNode = document.createElement("device");
                deviceNode.setTextContent(device);

                deviceNodes.add((Node) deviceNode);
            }
            return deviceNodes;
        };
        properties.put("//sqlServerRstOption", devicesFunction);
        Map<String, Object> result = processRequest("restore_mssql_backup_data", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> restoreMysqlBackupData(String srcDbName, String srcSubclientName, String srcInstanceName, String srcClientName, String destClientName, String destInstanceName, String fromTime, String toTime, String token){
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//associations/subclientName", srcSubclientName);
        properties.put("//associations/instanceName", srcInstanceName);
        properties.put("//associations/clientName", srcClientName);
        properties.put("//backupset/clientName", srcClientName);
        properties.put("//destClient/clientName", destClientName);
        properties.put("//destinationInstance/instanceName", destInstanceName);
        properties.put("//destinationInstance/clientName", destClientName);
        properties.put("//fileOption/sourceItem", srcDbName);
        properties.put("//fromTime/timeValue", toTime);
        properties.put("//refTime/timeValue", toTime);
        properties.put("//timeRange/fromTimeValue", fromTime);
        properties.put("//timeRange/toTimeValue", toTime);

        Map<String, Object> result = processRequest("restore_mysql_backup_data", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);

        return result;
    }

    @Override
    public Map<String, Object> getJobList(String subclientId, String fromTime, String toTime, String token){
        String body = "{" +
                "  \"category\": 2," +
                "  \"jobFilter\": {" +
                "    \"completedJobLookupTime\": " + Calendar.getInstance().getTimeInMillis()/1000L + "," +
                "    \"showAgedJobs\": true," +
                "    \"jobTypeList\": [" +
                "      4" +
                "    ]," +
                "    \"statusList\": [" +
                "      \"Completed\"," +
                "      \"Completed w/ one or more errors\"," +
                "      \"Completed w/ one or more warnings\"" +
                "    ]," +
                "    \"entity\": {" +
                "      \"subclientId\": " + subclientId +
                "    }," +
                "    \"startTimeRange\": {" +
                "      \"fromTime\": " + fromTime + "," +
                "      \"toTime\": " + toTime +
                "    }" +
                "  }" +
                "}";

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("body", body);
        Map<String, Object> result = processRequest("get_job_list", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);
        return result;
    }

    @Override
    public Map<String, Object> getMssqlRestoreOptions(String instanceId, String dbName, String token){
        String body = "{" +
                "  \"restoreDbType\": 0," +
                "  \"sourceInstanceId\": " + instanceId + "," +
                "  \"selectedDatabases\": [" +
                "    {" +
                "      \"databaseName\": \"" + dbName + "\"" +
                "    }" +
                "  ]" +
                "}";

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("body", body);
        Map<String, Object> result = processRequest("get_mssql_restoreoptions", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, true);
        return result;
    }

    @Override
    public Map<String, Object> installClient(String commCellName, String clientName, String osType, List<String> componentNameList, String userName, String password, String token){
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("//clientEntity/commCellName", commCellName);
        properties.put("//clientEntity/clientName", clientName);
        properties.put("//installOSType", osType.toUpperCase());
        properties.put("//clientAuthForJob/userName", userName);
        properties.put("//clientAuthForJob/password", password);
        Function<Document, List<Node>> componentInfoFunction = document -> {
            List<Node> componentInfoNodes = new ArrayList<Node>();
            for(String componentName:componentNameList) {
                Element componentInfoNode = document.createElement("componentInfo");

                Node componentNameNode = document.createElement("componentName");
                componentNameNode.setTextContent(componentName);
                componentInfoNode.appendChild(componentNameNode);

                Node osTypeNode = document.createElement("osType");
                osTypeNode.setTextContent(osType);
                componentInfoNode.appendChild(osTypeNode);

                Node consumeLicenseNode = document.createElement("consumeLicense");
                consumeLicenseNode.setTextContent("true");
                componentInfoNode.appendChild(consumeLicenseNode);

                Node clientSidePackageNode = document.createElement("clientSidePackage");
                clientSidePackageNode.setTextContent("true");
                componentInfoNode.appendChild(clientSidePackageNode);

                Node installCommandNode = document.createElement("installCommand");
                installCommandNode.setTextContent("");
                componentInfoNode.appendChild(installCommandNode);

                Node postInstallCommandNode = document.createElement("postInstallCommand");
                postInstallCommandNode.setTextContent("");
                componentInfoNode.appendChild(postInstallCommandNode);

                Node unInstallCommandNode = document.createElement("unInstallCommand");
                unInstallCommandNode.setTextContent("");
                componentInfoNode.appendChild(unInstallCommandNode);

                componentInfoNodes.add((Node) componentInfoNode);
            }
            return componentInfoNodes;
        };

        properties.put("//components", componentInfoFunction);

        Map<String, Object> result = processRequest("install_client", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, null, properties, false);

        return result;
    }

    @Override
    public Map<String, Object> executeWorkflow(String workflowName, String inputs, String token){
        Map<String, String> apiMap = new HashMap<String, String>();
        apiMap.put("workflowName", workflowName);

        String body = "{" +
                "\"Workflow_StartWorkflow\":" +
                "{   " +
                "  \"outputFormat\":\"1\"," +
                "    \"options\":{" +
                "      \"inputs\":{" + inputs +
                "      }" +
                "    }" +
                "}" +
                "}";

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("body", body);
        Map<String, Object> result = processRequest("execute_workflow", CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_VERSION), token, apiMap, properties, true);
        return result;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> processRequest(String apiName, String version, String token, Map<String, String> apiMap, Map<String, Object> xmlBodyMap, boolean removeEmptyNodes){
        logger.debug("Involving API: {}, version: {}", apiName, version);
        Map<String, Object> result = null;
        CvApi cvInterfaceInfo = cvInterfaceInfoMapper.queryByNameAndVersion(apiName, version);

        //如果按特定的版本查询不到，用默认版本
        if(cvInterfaceInfo == null) {
            logger.warn("No API info found: {}, version: {}", apiName, version);
            if(version.equals(CvConstants.API_DEFAULT_VERSION)) {
                return result;
            }

            cvInterfaceInfo = cvInterfaceInfoMapper.queryByNameAndVersion(apiName, CvConstants.API_DEFAULT_VERSION);
            if(cvInterfaceInfo == null) {
                logger.warn("No default API found: {}, version: {}", apiName, version);
                return result;
            }
        }

        String api = cvInterfaceInfo.getUrl();
        if (api == null) {
            return result;
        }
        //String body = StringUtils.isBlank(cvInterfaceInfo.getBody()) ? "<empty/>" : cvInterfaceInfo.getBody();
        String body = cvInterfaceInfo.getBody();
        String headers = StringUtils.isBlank(cvInterfaceInfo.getHeader()) ? "" : cvInterfaceInfo.getHeader() + ",";
        String type = cvInterfaceInfo.getType();

        //将值放入request url
        for(String key:(Optional.ofNullable(apiMap).map(map -> map.keySet()).orElse(new HashSet<String>()))) {
            api = api.replace("{" + key + "}", apiMap.get(key));
        }

        //将值放入request body
        Document doc = null;
        if(StringUtils.isNotBlank(body)){
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = builder.parse(new InputSource(new StringReader(body)));
                Element root = doc.getDocumentElement();
                for(String key:(Optional.ofNullable(xmlBodyMap).map(map -> map.keySet()).orElse(new HashSet<String>()))) {
                    Node property = DomUtils.selectSingleNode(root, key);
                    String regex = "\\[@\\w+\\]";
                    Object value = xmlBodyMap.get(key);
                    Pattern pattern = Pattern.compile(regex);
                    Matcher m = pattern.matcher(key);
                    if(m.find()) {
                        String attrName = m.group(0).substring(2, m.group(0).lastIndexOf("]"));
                        if(StringUtils.isBlank(String.valueOf(value))) {
                            ((Element) property).removeAttribute(attrName);
                        } else {
                            ((Element) property).setAttribute(attrName, String.valueOf(value));
                        }
                    } else {
                        if(value instanceof Function) {
                            List<Node> nodeList = (List<Node>) ((Function<Document, List<Node>>) value).apply(doc);
                            nodeList.forEach(node -> {
                                if(node instanceof Node) property.appendChild((Node) node);
                            });
                        }else {
                            property.setTextContent(String.valueOf(value));
                        }
                    }
                }

                if(removeEmptyNodes) DomUtils.removeEmptyNodes(doc);

            } catch (Exception e) {
                logger.error("Error occur while processing request body", e);
                return result;
            }
        }

        //将值放入header
        headers = "{" + headers + "\"Authtoken\":\"" + token + "\"}";
        Map<String, Object> headerMap = JSON.parseObject(headers);

        String response = "";
        try{
            if(type.equalsIgnoreCase(CvConstants.API_CLI)) {
                if(StringUtils.isNotBlank(body)){
                    body = "command=" + api + "&inputRequestXML=" + URLEncoder.encode(DomUtils.documentToString(doc),"UTF-8");
                } else {
                    body = "command=" + api;
                }

                response = RestfulUtils.doPostAPIRequest(CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_REST_URLPREFIX) + "/ExecuteQCommand", headerMap, body);
            }

            //REST api 格式：method + 空格 + url
            if (type.equalsIgnoreCase(CvConstants.API_REST)){
                String method = api.substring(0, api.indexOf(" "));
                api = api.substring(api.indexOf(" ") + 1);
                if(StringUtils.isNotBlank(body)){
                    body = DomUtils.documentToString(doc);
                } else if(headers.contains("\"Content-Type\":\"application/json\"")){
                    body = (String) xmlBodyMap.get("body");
                } else {
                    body = "";
                }

                if(method.equalsIgnoreCase("get")) {
                    response = RestfulUtils.doGetAPIRequest(CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_REST_URLPREFIX) + api, headerMap);
                }

                if(method.equalsIgnoreCase("post")) {
                    response = RestfulUtils.doPostAPIRequest(CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_REST_URLPREFIX) + api, headerMap, body);
                }

                if(method.equalsIgnoreCase("put")) {
                    response = RestfulUtils.doPutAPIRequest(CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_REST_URLPREFIX) + api, headerMap, body);
                }

                if(method.equalsIgnoreCase("delete")) {
                    response = RestfulUtils.doDeleteAPIRequest(CvProperties.getInstance().getProperty(CvConstants.PROPERTY_API_REST_URLPREFIX) + api, headerMap);
                }
            }
        } catch (IOException e){
            logger.error("Exception occurred while contacting with remote server", e);
            throw new RuntimeException("Fail to contact with Commvault server");
        }

        logger.debug("Request url: {}, header: {}, body: {}", api, headers, body);
        logger.debug("Response {}", response);

        try{
            result = JSON.parseObject(response);
        } catch (Exception e){
            result = new HashMap<>();
            result.put("response", response);
        }
        return result;
    }
}
