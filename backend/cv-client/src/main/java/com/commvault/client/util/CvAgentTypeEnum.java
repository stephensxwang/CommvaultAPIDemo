package com.commvault.client.util;

public enum CvAgentTypeEnum {
	FILESYSTEM("File System"),
    ORACLE("Oracle"),
    ORACLERAC("Oracle RAC"),
    SQLSERVER("SQL Server"),
    DB2("DB2"),
    DB2UNIX("DB2 on Unix"),
    MYSQL("MySQL"),
	NAS("NAS");
	
	private String value;
	
	private CvAgentTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
