package com.commvault.client.service;

import java.util.List;
import java.util.Map;

public interface CvCommonService {

    public Map<String, Object> createOracleInstance(String clientName,
                                                    String instanceName,
                                                    String storagePolicyName,
                                                    String oracleHome,
                                                    String oracleUser,
                                                    String sqlConnectUser,
                                                    String sqlConnectPassword,
                                                    String token);

    public Map<String, Object> updateOracleInstance(String clientName,
                                                    String instanceName,
                                                    String storagePolicyName,
                                                    String oracleHome,
                                                    String oracleUser,
                                                    String sqlConnectUser,
                                                    String sqlConnectPassword,
                                                    String token);

    public Map<String, Object> updateOracleDatabaseSubclient(String appName, String clientName, String instanceName, String subclientName, String storagePolicyName, String token);

    public Map<String, Object> updateOracleArchiveLogSubclient(String appName, String clientName, String instanceName, String subclientName, String storagePolicyName, String token);

    public Map<String, Object> renameDatabaseSubclient(String appName, String clientName, String instanceName, String subclientName, String newName, String token);

    public Map<String, Object> createOracleRACClient(String clientName, String token);

    public Map<String, Object> createOracleRACInstanceAndAddPrimaryClient(String clientName,
                                                                          String primaryClientName,
                                                                          String instanceName,
                                                                          String databaseName,
                                                                          String userAccount,
                                                                          String oracleHome,
                                                                          String connectUser,
                                                                          String connectPassword,
                                                                          String token);

    public Map<String, Object> updateOracleRACInstanceAddSecondaryClient(String clientName,
                                                                         String secondaryClientName,
                                                                         String instanceName,
                                                                         String databaseName,
                                                                         String userAccount,
                                                                         String oracleHome,
                                                                         String connectUser,
                                                                         String connectPassword,
                                                                         String token);

    public Map<String, Object> createOracleDGInstance(String clientName,
                                                      String primaryClientName,
                                                      String instanceName,
                                                      String userAccount,
                                                      String oracleHome,
                                                      String connectUser,
                                                      String connectPassword,
                                                      String connectServiceName,
                                                      String token);

    public Map<String, Object> updateOracleDGInstance(String clientName,
                                                      String secondaryClientName,
                                                      String instanceName,
                                                      String userAccount,
                                                      String oracleHome,
                                                      String connectUser,
                                                      String connectPassword,
                                                      String connectServiceName,
                                                      String token);

    public Map<String, Object> updateOracleRACInstanceStoragePolicy(String clientName, String instanceName, String storagePolicyName, String token);

    public List<Map<String, Object>> getSubclientListByClientName(String clientName, String token);

    public Map<String, Object> updateSchedulePolicyEntityAssoc(String schedulePolicyName, String appName, String clientName, String subClientName, String backupsetName, String instanceName, String token);

    public Map<String, Object> updateOracleClientAdditionalSettings(String clientName, String token);

    public Map<String, Object> createClusterClient(String clientType, String clientName, String token);

    public Map<String, Object> updateClientActivityControl(String clientName, String activityType, boolean enableActivity, String token);

    public Map<String, Object> updateClientDescription(String clientName, String clientDescription, String token);

    public Map<String, Object> addMemberToClusterClient(String clientName, List<String> members, String token);

    public Map<String, Object> getClientPropertiesByName(String clientName, String token);

    public List<Map<String, Object>> getInstanceListByClientName(String clientName, String token);

    public List<Map<String, Object>> getSchedulePolicy(String token);

    public List<Map<String, Object>> getStoragePolicy(String token);

    public Map<String, Object> getSubclientProperties(String subclientId, String token);

    public Map<String, Object> createFileSystemSubclient(String clientName, String backupsetName, String subClientName, String storagePolicyName, String contentPath, boolean useVss, String token);

    public Map<String, Object> overwriteContentForFileSystemSubclientById(String subclientId, String clientName, String backupsetName, String subClientName, String newSubClientName, String storagePolicyName, String contentPath, boolean useVss, String token);

    public Map<String, Object> addContentForFileSystemSubclientById(String subclientId, String clientName, String backupsetName, String subClientName, String storagePolicyName, String contentPath, String token);

    public List<Map<String, Object>> getSchedulesBySubclientId(String subclientId, String token);

    public List<Map<String, Object>> getClientGroup(String token);

    public Map<String, Object> createClientGroup(String groupName, String clientName, String token);

    public Map<String, Object> createClientGroup(String groupName, String token);

    public Map<String, Object> deleteClientGroup(String clientGroupId, String token);

    public Map<String, Object> updateClientGroup(String clientGroupId, String groupName, String token);

    public Map<String, Object> addClientToClientGroup(String clientGroupId, String groupName, String clientName, String token);

    public Map<String, Object> login(String domain, String username, String password, String commserver);

    public boolean logout(String token);

    public Map<String, Object> createSQLServerInstance(String clientName, String instanceName, String token);

    public Map<String, Object> createSQLServerSubclient(String clientName, String instanceName, String subClientName, String storagePolicyName, String databaseName, String token);

    public Map<String, Object> updateSQLServerSubclient(String clientName, String instanceName, String subClientName, String storagePolicyName, String databaseName, String token);

    public Map<String, Object> getSQLServerAG(String clientName, String instanceName, String token);

    public Map<String, Object> getSQLServerAGReplica(String clientName, String instanceName, String availabilityGroup, String token);

    public Map<String, Object> createSQLServerAGClient(String primaryClientName, String instanceName, String clientName, String token);

    public List<Map<String, Object>> getBackupsetListByClientName(String clientName, String token);

    public Map<String, Object> createDb2Instance(String appName, String clientName,
                                                 String instanceName,
                                                 String storagePolicyName,
                                                 String homeDirectory,
                                                 String token);

    public Map<String, Object> updateDb2Instance(String appName,
                                                 String clientName,
                                                 String instanceName,
                                                 String storagePolicyName,
                                                 String homeDirectory,
                                                 String token);

    public Map<String, Object> createDb2Backupset(String appName, String clientName, String instanceName, String databaseName, String storagePolicyName, String token);

    public Map<String, Object> createDb2Subclient(String appName, String clientName, String subclientName, String instanceName, String databaseName, String storagePolicyName, String token);

    public Map<String, Object> updateDb2Subclient(String appName, String clientName, String subclientName, String instanceName, String databaseName, String storagePolicyName, String token);

    public Map<String, Object> renameDb2Subclient(String appName, String clientName, String instanceName, String backupsetName, String subclientName, String newName, String token);

    public Map<String, Object> createMysqlInstance(String clientName,
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
                                                   String token);

    public Map<String, Object> updateMysqlInstance(String clientName,
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
                                                   String token);

    public Map<String, Object> updateMysqlSubclient(String clientName, String subclientName, String instanceName, String databaseName, String storagePolicyName, String token);

    public List<Map<String, Object>> getClient(String token);

    public Map<String, Object> browseBackupData(String clientName, String applicationId, String clientId, String subclientId, String backupsetId, String instanceId, String fromTime, String toTime, String token);

    public Map<String, Object> restoreBackupData(String srcClientId, String appTypeId, String instanceId, String backupSetId, String subclientId, String fromTime, String toTime, String targetClientId, String targetClientName, String inPlace, String destPath, List<String> filePaths, String token);

    public Map<String, Object> restoreNdmpBackupData(String srcClientName, String srcSubclientName, String srcBackupsetName, String srcInstanceName, String srcPath, String fromTime, String toTime, String targetClientName, String inPlace, String destPath, String token);

    public Map<String, Object> listMediaForAJob(String jobId, String token);

    public Map<String, Object> getJobSummary(String jobId, String token);

    public Map<String, Object> getDb2BackupJobInfo(String clientName, String instanceName, String backupsetName, String token);

    public Map<String, Object> restoreDb2BackupData(String srcSubclientName, String srcBackupsetName, String srcInstanceName, String srcClientName, String destClientName, String destInstanceName, String targetDbName, String targetPath, String backupTimeStamps, String token);

    public Map<String, Object> restoreOracleBackupData(String srcClientName, String srcSubclientName, String srcBackupsetName, String srcInstanceName, String fromTime, String toTime, String destClientName, String destInstanceName, String targetPath, String controlFileName, String scn, String token);

    public Map<String, Object> restoreMssqlBackupData(String srcDbName, String srcSubclientName, String srcInstanceName, String srcClientName, String destClientName, String destInstanceName, String targetDbName, String targetPath, String backupTimeStamps, List<String> devices, String token);

    public Map<String, Object> restoreMysqlBackupData(String srcDbName, String srcSubclientName, String srcInstanceName, String srcClientName, String destClientName, String destInstanceName, String fromTime, String toTime, String token);

    public Map<String, Object> getJobList(String subclientId, String fromTime, String toTime, String token);

    public Map<String, Object> getMssqlRestoreOptions(String instanceId, String dbName, String token);

    public Map<String, Object> executeWorkflow(String workflowName, String inputs, String token);

    public Map<String, Object> installClient(String commCellName, String clientName, String osType, List<String> componentNameList, String userName, String password, String token);
}

