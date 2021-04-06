package com.commvault.client.util;

public class CvConstants {

	public static final String PROPERTY_API_VERSION = "cv.api.version";
	public static final String PROPERTY_API_REST_URLPREFIX = "cv.api.rest.urlprefix";
	
	public static final String API_DEFAULT_VERSION = "default"; //对应cv.yml的version字段的默认值
	public static final String API_CLI = "cli"; //对应cv.yml的type字段的值
	public static final String API_REST = "rest"; //对应cv.yml的type字段的值
	
	public static final String DEFAULT_SUBCLIENT_NAME = "default";
	public static final String DEFAULT_BACKUPSET_NAME = "defaultBackupSet";
	public static final String ORACLE_ARCHIVELOG_SUBCLIENT_NAME = "ArchiveLog";
	
	public static final String CLIENT_ACTIVITY_TYPE_RESTORE = "2";
	public static final String CLIENT_ACTIVITY_TYPE_BACKUP = "1";
	
	public static final String OSNAME_WINDOWS = "windows";
	
	public static final String UNIX_CLUSTER_CLIENT = "UNIX_CLUSTER_CLIENT";
	public static final String WINDOWS_CLUSTER_CLIENT = "WINDOWS_CLUSTER_CLIENT";
}
